package com.l2jserver.gameserver.phoenix.events;

import javolution.util.FastList;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.phoenix.AbstractEvent;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.container.NpcContainer;
import com.l2jserver.gameserver.phoenix.models.EventNpc;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;
import com.l2jserver.gameserver.phoenix.models.TeamEventStatus;
import com.l2jserver.util.Rnd;

/**
 * @author Rizel
 */
public class Bomb extends AbstractEvent
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
						divideIntoTeams(2);
						teleportToTeamPos();
						preparePlayers();
						createPartyOfTeam(1);
						createPartyOfTeam(2);
						giveSkill();
						setStatus(EventState.FIGHT);
						schedule(10000);
						break;
					case FIGHT:
						unParalize();// by fissban
						setTitleScore();// by fissban
						SpecialMsg(2, "Start"); // by fissban
						setStatus(EventState.END);
						clock.start();
						break;
					case END:
						clock.stop();
						cancelAttack();// by fissban
						SpecialMsg(1, "Finish"); // by fissban
						
						if (winnerTeam == 0)
						{
							winnerTeam = getWinnerTeam();
						}
						
						removeSkill();
						giveReward(getPlayersOfTeam(winnerTeam));
						setStatus(EventState.INACTIVE);
						announce("Congratulation! The " + teams.get(winnerTeam).getName() + " team won the event with " + teams.get(winnerTeam).getScore() + " kills!");
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
	
	private class BombTask implements Runnable
	{
		private final EventPlayer owner;
		private final EventNpc bomb;
		private FastList<EventPlayer> victims;
		
		BombTask(EventPlayer p)
		{
			owner = p;
			bomb = NpcContainer.getInstance().createNpc(owner.getOwnerLoc().getX(), owner.getOwnerLoc().getY(), owner.getOwnerLoc().getZ(), Config.getInstance().getInt(getId(), "bombNpcId"), instanceId);
			bomb.setTitle(owner.getMainTeam() == 1 ? "Blue" : "Red");
			bomb.broadcastStatusUpdate();
			
			for (EventPlayer pl : getPlayerList())
			{
				pl.sendAbstractNpcInfo(bomb);
			}
			
			ThreadPoolManager.getInstance().scheduleGeneral(this, 3000);
		}
		
		@Override
		public void run()
		{
			victims = new FastList<>();
			
			for (EventPlayer victim : getPlayerList())
			{
				if ((owner.getMainTeam() != victim.getMainTeam()) && (Math.sqrt(victim.getPlanDistanceSq(bomb.getNpc())) <= Config.getInstance().getInt(getId(), "bombRadius")))
				{
					victim.doDie();
					owner.increaseScore();
					
					victims.add(victim);
					
					if (victim.getMainTeam() == 1)
					{
						teams.get(2).increaseScore();
					}
					if (victim.getMainTeam() == 2)
					{
						teams.get(1).increaseScore();
					}
				}
			}
			if (victims.size() != 0)
			{
				bomb.showBombEffect(victims);
				victims.clear();
			}
			
			bomb.unspawn();
		}
	}
	
	private enum EventState
	{
		START,
		FIGHT,
		END,
		TELEPORT,
		INACTIVE
	}
	
	EventState eventState;
	
	private final Core task;
	
	@SuppressWarnings("synthetic-access")
	public Bomb(Integer containerId)
	{
		super(containerId);
		eventId = 12;
		createNewTeam(1, "Blue", Config.getInstance().getColor(getId(), "Blue"), Config.getInstance().getPosition(getId(), "Blue", 1));
		createNewTeam(2, "Red", Config.getInstance().getColor(getId(), "Red"), Config.getInstance().getPosition(getId(), "Red", 1));
		task = new Core();
		winnerTeam = 0;
		clock = new EventClock(Config.getInstance().getInt(getId(), "matchTime"));
	}
	
	@Override
	public boolean canAttack(EventPlayer player, EventPlayer target)
	{
		return false;
	}
	
	@Override
	public void dropBomb(EventPlayer player)
	{
		new BombTask(player);
	}
	
	@Override
	protected void endEvent()
	{
		winnerTeam = players.head().getNext().getValue().getMainTeam();
		
		setStatus(EventState.END);
		clock.stop();
	}
	
	@Override
	protected String getScorebar()
	{
		return "" + teams.get(1).getName() + ": " + teams.get(1).getScore() + "  " + teams.get(2).getName() + ": " + teams.get(2).getScore() + "  Time: " + clock.getTimeInString();
	}
	
	@Override
	public int getWinnerTeam()
	{
		if (teams.get(1).getScore() > teams.get(2).getScore())
		{
			return 1;
		}
		if (teams.get(2).getScore() > teams.get(1).getScore())
		{
			return 2;
		}
		if (teams.get(1).getScore() == teams.get(2).getScore())
		{
			if (Rnd.nextInt(1) == 1)
			{
				return 1;
			}
			return 2;
		}
		return 1;
	}
	
	void giveSkill()
	{
		for (EventPlayer player : getPlayerList())
		{
			player.addSkill(Config.getInstance().getInt(getId(), "bombSkillId"), 1);
		}
	}
	
	@Override
	protected void onClockZero()
	{
		setStatus(EventState.END);
		schedule(1);
	}
	
	@Override
	public void onDie(EventPlayer victim, EventPlayer killer)
	{
		super.onDie(victim, killer);
		addToResurrector(victim);
	}
	
	@Override
	public void onLogout(EventPlayer player)
	{
		player.removeSkill(Config.getInstance().getInt(getId(), "bombSkillId"), 1);
	}
	
	@Override
	public boolean onUseMagic(EventPlayer player, Integer skill)
	{
		if (Integer.valueOf(skill) == Config.getInstance().getInt(getId(), "bombSkillId"))
		{
			return true;
		}
		
		return false;
	}
	
	void removeSkill()
	{
		for (EventPlayer player : getPlayerList())
		{
			player.removeSkill(Config.getInstance().getInt(getId(), "bombSkillId"), 1);
		}
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
		setStatus(EventState.START);
		schedule(1);
	}
	
	@Override
	public void createStatus()
	{
		status = new TeamEventStatus(containerId);
	}
}
