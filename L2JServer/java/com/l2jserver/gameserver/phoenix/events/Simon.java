package com.l2jserver.gameserver.phoenix.events;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
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
public class Simon extends AbstractEvent
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
						setStatus(EventState.SAY);
						schedule(20000);
						break;
					case SAY:
						if (round == 0)
						{
							// unParalize(); // by fissban
							// setTitleScore();// by fissban
							SpecialMsg(2, "Start"); // by fissban
						}
						round++;
						say = createNewRandomString(Config.getInstance().getInt(getId(), "lengthOfFirstWord") + (Config.getInstance().getInt(getId(), "increasePerRound") * round));
						sendToPlayers(say.toUpperCase());
						setStatus(EventState.CHECK);
						schedule(Config.getInstance().getInt(getId(), "roundTime") * 1000);
						break;
					case CHECK:
						if (removeAfkers())
						{
							setAllToFalse();
							setStatus(EventState.SAY);
							schedule(Config.getInstance().getInt(getId(), "roundTime") * 1000);
						}
						break;
					case END:
						// cancelAttack();// by fissban
						SpecialMsg(1, "Finish"); // by fissban
						setStatus(EventState.INACTIVE);
						unParalize();
						
						if (winner != null)
						{
							giveReward(winner);
							announce("Congratulation! " + winner.getName() + " won the event!");
							eventEnded();
						}
						else
						{
							announce("The mathc ended as a tie!");
						}
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
		SAY,
		CHECK,
		END,
		INACTIVE
	}
	
	EventState eventState;
	
	private final Core task;
	
	int round;
	
	String say;
	
	private EventNpc spawn;
	
	EventPlayer winner;
	
	protected int restriction = 1;
	
	@SuppressWarnings("synthetic-access")
	public Simon(Integer containerId)
	{
		super(containerId);
		eventId = 6;
		createNewTeam(1, "All", Config.getInstance().getColor(getId(), "All"), Config.getInstance().getPosition(getId(), "All", 1));
		task = new Core();
		round = 0;
		spawn = null;
		winner = null;
	}
	
	String createNewRandomString(int size)
	{
		String str = "";
		
		for (int i = 0; i < size; i++)
		{
			str = str + (char) (Rnd.nextInt(26) + 97);
		}
		
		return str;
	}
	
	@Override
	protected void endEvent()
	{
		winner = players.head().getNext().getValue();
		setStatus(EventState.END);
		schedule(1);
	}
	
	@Override
	protected String getScorebar()
	{
		return "";
	}
	
	@Override
	protected void onClockZero()
	{
	}
	
	@Override
	public void onSay(int type, EventPlayer player, String text)
	{
		if ((eventState == EventState.CHECK) && (player.getStatus() != -1))
		{
			if (text.equalsIgnoreCase(say))
			{
				player.setStatus(1);
				player.sendPacket(new ExShowScreenMessage("Correct!", 5000));
				player.increaseScore();
				player.setNameColor(0, 255, 0);
				player.broadcastUserInfo();
			}
			
			else
			{
				player.setStatus(-1);
				player.sendPacket(new ExShowScreenMessage("Wrong!", 5000));
				player.setNameColor(255, 0, 0);
				player.broadcastUserInfo();
			}
			
			int falses = 0;
			EventPlayer falsed = null;
			for (EventPlayer p : getPlayerList())
			{
				if (p.getStatus() == 0)
				{
					falses++;
					falsed = p;
				}
			}
			
			if (falses == 1)
			{
				int count = 0;
				for (EventPlayer pla : getPlayerList())
				{
					if (pla.getStatus() == 1)
					{
						count++;
					}
				}
				
				if (count >= 1)
				{
					falsed.sendPacket(new ExShowScreenMessage("Last one!", 5000));
					falsed.setNameColor(255, 0, 0);
					falsed.broadcastUserInfo();
					falsed.setStatus(-1);
				}
				
				if (count == 0)
				{
					winner = getPlayersWithStatus(0).head().getNext().getValue();
					setStatus(EventState.END);
					schedule(1);
				}
				
			}
			
			if (countOfPositiveStatus() == 1)
			{
				winner = getPlayersWithStatus(1).head().getNext().getValue();
				setStatus(EventState.END);
				schedule(1);
			}
		}
	}
	
	boolean removeAfkers()
	{
		for (EventPlayer player : getPlayerList())
		{
			if (player.getStatus() == 0)
			{
				player.sendPacket(new ExShowScreenMessage("Timeout!", 5000));
				player.setNameColor(255, 0, 0);
				player.broadcastUserInfo();
				player.setStatus(-1);
			}
			
			if (countOfPositiveStatus() == 1)
			{
				if (getPlayersWithStatus(1).size() == 1)
				{
					winner = getPlayersWithStatus(1).head().getNext().getValue();
				}
				else
				{
					winner = null;
				}
				
				SpecialMsg(1, "Finish");
				setStatus(EventState.END);
				schedule(1);
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void reset()
	{
		super.reset();
		round = 0;
		say = "";
		
		if (spawn != null)
		{
			spawn.unspawn();
		}
		
		spawn = null;
	}
	
	@Override
	protected void schedule(int time)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(task, time);
	}
	
	void sendToPlayers(String text)
	{
		for (EventPlayer player : getPlayerList())
		{
			player.simon(spawn.getId(), text);
		}
	}
	
	void setAllToFalse()
	{
		for (EventPlayer player : getPlayerList())
		{
			if (player.getStatus() != -1)
			{
				player.setStatus(0);
				player.setNameColor(255, 255, 255);
				player.broadcastUserInfo();
			}
		}
	}
	
	void setStatus(EventState s)
	{
		eventState = s;
	}
	
	@Override
	public void start()
	{
		int[] npcpos = Config.getInstance().getPosition(getId(), "Simon", 1);
		spawn = NpcContainer.getInstance().createNpc(npcpos[0], npcpos[1], npcpos[2], Config.getInstance().getInt(getId(), "simonNpcId"), instanceId);
		setStatus(EventState.START);
		schedule(1);
	}
	
	@Override
	public void createStatus()
	{
		status = new SingleEventStatus(containerId);
	}
}