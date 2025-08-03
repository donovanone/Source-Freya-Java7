package com.l2jserver.gameserver.phoenix.events;

import javolution.util.FastList;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.phoenix.AbstractEvent;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.container.NpcContainer;
import com.l2jserver.gameserver.phoenix.models.EventNpc;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;
import com.l2jserver.gameserver.phoenix.models.TeamEventStatus;

/**
 * @author Rizel
 */
public class Domination extends AbstractEvent
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
						
						if (winnerTeam == 0)
						{
							winnerTeam = getWinnerTeam();
						}
						
						giveReward(getPlayersOfTeam(winnerTeam));
						unSpawnZones();
						setStatus(EventState.INACTIVE);
						announce("Congratulation! The " + teams.get(winnerTeam).getName() + " team won the event with " + teams.get(winnerTeam).getScore() + " points!");
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
	
	private final FastList<EventNpc> zones;
	
	@SuppressWarnings("synthetic-access")
	public Domination(Integer containerId)
	{
		super(containerId);
		eventId = 2;
		createNewTeam(1, "Blue", Config.getInstance().getColor(getId(), "Blue"), Config.getInstance().getPosition(getId(), "Blue", 1));
		createNewTeam(2, "Red", Config.getInstance().getColor(getId(), "Red"), Config.getInstance().getPosition(getId(), "Red", 1));
		task = new Core();
		zones = new FastList<>();
		winnerTeam = 0;
		clock = new EventClock(Config.getInstance().getInt(getId(), "matchTime"));
	}
	
	@Override
	protected void clockTick()
	{
		int team1 = 0;
		int team2 = 0;
		
		for (EventPlayer player : getPlayerList())
		{
			switch (player.getMainTeam())
			{
				case 1:
					if (Math.sqrt(player.getPlanDistanceSq(zones.getFirst().getNpc())) <= Config.getInstance().getInt(getId(), "zoneRadius"))
					{
						team1++;
					}
					break;
				case 2:
					if (Math.sqrt(player.getPlanDistanceSq(zones.getFirst().getNpc())) <= Config.getInstance().getInt(getId(), "zoneRadius"))
					{
						team2++;
					}
					break;
			}
		}
		
		if (team1 > team2)
		{
			for (EventPlayer player : getPlayersOfTeam(1))
			{
				player.increaseScore();
			}
			teams.get(1).increaseScore();
		}
		if (team2 > team1)
		{
			for (EventPlayer player : getPlayersOfTeam(2))
			{
				player.increaseScore();
			}
			teams.get(2).increaseScore();
		}
		
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
		return "" + teams.get(1).getName() + ": " + teams.get(1).getScore() + "  " + teams.get(2).getName() + ": " + teams.get(2).getScore() + "  Time: " + clock.getTimeInString();
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
	public void schedule(int time)
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
		int[] npcpos = Config.getInstance().getPosition(getId(), "Zone", 1);
		zones.add(NpcContainer.getInstance().createNpc(npcpos[0], npcpos[1], npcpos[2], Config.getInstance().getInt(getId(), "zoneNpcId"), instanceId));
		setStatus(EventState.START);
		schedule(1);
	}
	
	void unSpawnZones()
	{
		for (EventNpc s : zones)
		{
			s.unspawn();
			zones.remove(s);
		}
	}
	
	@Override
	public void createStatus()
	{
		status = new TeamEventStatus(containerId);
	}
}