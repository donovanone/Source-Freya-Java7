package com.l2jserver.gameserver.phoenix.events;

import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jserver.gameserver.ThreadPoolManager;
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
public class Russian extends AbstractEvent
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
						spawnRussians();
						setStatus(EventState.CHOOSE);
						schedule(20000);
						break;
					case CHOOSE:
						if (round == 0)
						{
							unParalize(); // by fissban
							setTitleScore();// by fissban
							SpecialMsg(2, "Start"); // by fissban
						}
						
						round++;
						announce("Choose a russian!");
						setStatus(EventState.CHECK);
						schedule(Config.getInstance().getInt(getId(), "roundTime") * 1000);
						break;
					case CHECK:
						removeAfkers();
						killRandomRussian();
						
						if (countOfPositiveStatus() != 0)
						{
							if (russians.size() != 1)
							{
								for (EventPlayer player : getPlayersWithStatus(1))
								{
									player.setStatus(0);
									player.increaseScore();
									player.setNameColor(255, 255, 255);
									player.broadcastUserInfo();
								}
								
								for (FastList<EventPlayer> chose : choses.values())
								{
									chose.reset();
								}
								
								setStatus(EventState.CHOOSE);
								schedule(Config.getInstance().getInt(getId(), "roundTime") * 1000);
							}
							else
							{
								for (EventPlayer player : getPlayersWithStatus(1))
								{
									giveReward(player);
								}
								
								unspawnRussians();
								announce("Congratulation! " + countOfPositiveStatus() + " player survived the event!");
								eventEnded();
							}
						}
						else
						{
							unspawnRussians();
							announce("Unfortunatly noone survived the event!");
							eventEnded();
						}
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
		CHOOSE,
		CHECK,
		INACTIVE
	}
	
	EventState eventState;
	
	private final Core task;
	
	int round;
	
	final FastMap<Integer, EventNpc> russians;
	
	final FastMap<Integer, FastList<EventPlayer>> choses;
	
	@SuppressWarnings("synthetic-access")
	public Russian(Integer containerId)
	{
		super(containerId);
		eventId = 11;
		createNewTeam(1, "All", Config.getInstance().getColor(getId(), "All"), Config.getInstance().getPosition(getId(), "All", 1));
		task = new Core();
		round = 0;
		russians = new FastMap<>();
		choses = new FastMap<>();
	}
	
	@Override
	public boolean canAttack(EventPlayer player, EventPlayer target)
	{
		return false;
	}
	
	@Override
	protected void endEvent()
	{
		EventPlayer winner = players.head().getNext().getValue();
		giveReward(winner);
		
		unspawnRussians();
		announce("Congratulation! 1 player survived the event!");
		eventEnded();
	}
	
	@Override
	protected String getScorebar()
	{
		return "";
	}
	
	void killRandomRussian()
	{
		FastList<Integer> ids = new FastList<>();
		for (int id : russians.keySet())
		{
			ids.add(id);
		}
		int russnum = ids.get(Rnd.nextInt(ids.size()));
		EventNpc russian = russians.get(russnum);
		russian.unspawn();
		announce(getPlayerList(), "The #" + russnum + " russian died.");
		
		for (EventPlayer victim : choses.get(russnum))
		{
			victim.setStatus(-1);
			victim.doDieNpc(russian.getId());
			victim.sendMessage("Your russian died!");
			victim.setNameColor(255, 255, 255);
		}
		russians.remove(russnum);
	}
	
	@Override
	protected void onClockZero()
	{
		
	}
	
	@Override
	public void onLogout(EventPlayer player)
	{
		super.onLogout(player);
		
		for (FastList<EventPlayer> list : choses.values())
		{
			if (list.contains(player))
			{
				list.remove(player);
			}
		}
	}
	
	@Override
	public boolean onTalkNpc(Integer npc, EventPlayer player)
	{
		
		EventNpc npci = NpcContainer.getInstance().getNpc(npc);
		
		if (npci == null)
		{
			return false;
		}
		
		if (!russians.containsValue(npci))
		{
			return false;
		}
		
		if (player.getStatus() != 0)
		{
			return true;
		}
		
		for (Map.Entry<Integer, EventNpc> russian : russians.entrySet())
		{
			if (russian.getValue().equals(npci))
			{
				choses.get(russian.getKey()).add(player);
				player.setNameColor(0, 255, 0);
				player.broadcastUserInfo();
				player.setStatus(1);
			}
		}
		return true;
	}
	
	void removeAfkers()
	{
		for (EventPlayer player : getPlayerList())
		{
			if (player.getStatus() == 0)
			{
				player.sendMessage("Timeout!");
				player.doDie();
				player.setStatus(-1);
			}
		}
	}
	
	@Override
	protected void reset()
	{
		super.reset();
		round = 0;
		russians.clear();
		choses.clear();
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
	
	void spawnRussians()
	{
		for (int i = 1; i <= Config.getInstance().getInt(getId(), "numberOfRussians"); i++)
		{
			int[] pos = Config.getInstance().getPosition(getId(), "Russian", i);
			russians.put(i, NpcContainer.getInstance().createNpc(pos[0], pos[1], pos[2], Config.getInstance().getInt(getId(), "russianNpcId"), instanceId));
			choses.put(i, new FastList<EventPlayer>());
			russians.get(i).setTitle("--" + i + "--");
		}
	}
	
	@Override
	public void start()
	{
		setStatus(EventState.START);
		schedule(1);
	}
	
	void unspawnRussians()
	{
		for (EventNpc russian : russians.values())
		{
			russian.unspawn();
		}
	}
	
	@Override
	public void createStatus()
	{
		status = new SingleEventStatus(containerId);
	}
}