package com.l2jserver.gameserver.phoenix.events;

import javolution.util.FastList;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.skills.AbnormalEffect;
import com.l2jserver.gameserver.phoenix.AbstractEvent;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.container.NpcContainer;
import com.l2jserver.gameserver.phoenix.models.EventNpc;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;
import com.l2jserver.gameserver.phoenix.models.SingleEventStatus;
import com.l2jserver.util.Rnd;

/**
 * @author Rizel
 */
public class Lucky extends AbstractEvent
{
	public static boolean enabled = true;
	
	private class Core implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				switch (eventState)
				{
					case START:
						cancelAttack();// by fissban
						startParalize();// by fissban
						divideIntoTeams(1);
						teleportToTeamPos();
						preparePlayers();
						setStatus(EventState.FIGHT);
						schedule(10000);
						break;
					case FIGHT:
						unParalize(); // by fissban
						setTitleScore();// by fissban
						SpecialMsg(2, "Start"); // by fissban
						setStatus(EventState.END);
						clock.start();
						break;
					case END:
						cancelAttack();// by fissban
						SpecialMsg(1, "Finish"); // by fissban
						clock.stop();
						unSpawnChests();
						EventPlayer winner = getPlayerWithMaxScore();
						giveReward(winner);
						setStatus(EventState.INACTIVE);
						announce("Congratulation! " + winner.getName() + " won the event with " + winner.getScore() + " opened chests!");
						eventEnded();
						break;
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				announce("Error! Event ended.");
				eventEnded();
			}
		}
	}
	
	private enum EventState
	{
		START,
		FIGHT,
		END,
		INACTIVE
	}
	
	EventState eventState;
	
	private final Core task;
	
	private final FastList<EventNpc> chests;
	
	protected int restriction = 1;
	
	@SuppressWarnings("synthetic-access")
	public Lucky(Integer containerId)
	{
		super(containerId);
		eventId = 5;
		createNewTeam(1, "All", Config.getInstance().getColor(getId(), "All"), Config.getInstance().getPosition(getId(), "All", 1));
		task = new Core();
		chests = new FastList<>();
		clock = new EventClock(Config.getInstance().getInt(getId(), "matchTime"));
	}
	
	@Override
	protected void endEvent()
	{
		setStatus(EventState.END);
		clock.stop();
	}
	
	@Override
	protected String getScorebar()
	{
		return "Max: " + getPlayerWithMaxScore().getScore() + "  Time: " + clock.getTimeInString() + "";
	}
	
	@Override
	protected void onClockZero()
	{
		setStatus(EventState.END);
		schedule(1);
		
	}
	
	@Override
	public boolean onTalkNpc(Integer npc, EventPlayer player)
	{
		EventNpc npci = NpcContainer.getInstance().getNpc(npc);
		
		if (npci == null)
		{
			return false;
		}
		if (!chests.contains(npci))
		{
			return false;
		}
		if (Rnd.nextInt(3) == 0)
		{
			((L2Npc) L2World.getInstance().findObject(npci.getId())).startAbnormalEffect(AbnormalEffect.FLAME);
			player.doDieNpc(npci.getId());
			addToResurrector(player);
		}
		else
		{
			npci.doDie();
			player.increaseScore();
		}
		
		npci.unspawn();
		chests.remove(npc);
		
		if (chests.size() == 0)
		{
			clock.stop();
		}
		
		return true;
	}
	
	@Override
	protected void schedule(int time)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(task, time);
	}
	
	void setStatus(EventState s)
	{
		eventState = s;
	}
	
	@Override
	public void start()
	{
		int[] coor = Config.getInstance().getPosition(getId(), "Chests", 1);
		for (int i = 0; i < Config.getInstance().getInt(getId(), "numberOfChests"); i++)
		{
			chests.add(NpcContainer.getInstance().createNpc(coor[0] + (Rnd.nextInt(coor[3] * 2) - coor[3]), coor[1] + (Rnd.nextInt(coor[3] * 2) - coor[3]), coor[2], Config.getInstance().getInt(getId(), "chestNpcId"), instanceId));
		}
		setStatus(EventState.START);
		schedule(1);
	}
	
	void unSpawnChests()
	{
		for (EventNpc s : chests)
		{
			s.unspawn();
			chests.remove(s);
		}
	}
	
	@Override
	public void createStatus()
	{
		status = new SingleEventStatus(containerId);
	}
}