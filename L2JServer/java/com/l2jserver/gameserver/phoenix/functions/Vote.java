package com.l2jserver.gameserver.phoenix.functions;

import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.phoenix.AbstractEvent;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.ManagerNpc;
import com.l2jserver.gameserver.phoenix.container.EventContainer;
import com.l2jserver.gameserver.phoenix.io.Out;
import com.l2jserver.gameserver.phoenix.models.Clock;
import com.l2jserver.gameserver.util.Broadcast;

/**
 * @author Rizel
 */
public class Vote
{
	private static class SingletonHolder
	{
		protected static final Vote _instance = new Vote();
	}
	
	private class VoteCore implements Runnable
	{
		@Override
		public void run()
		{
			switch (phase)
			{
				case VOTE:
					announce("Vote phase started! You have " + (Config.getInstance().getInt(0, "voteTime") / 60) + " mins to vote!");
					voteCountdown = new VoteCountdown(Config.getInstance().getInt(0, "voteTime"));
					voteCountdown.start();
					break;
				case CHECK:
					if (votes.size() > 0)
					{
						setCurrentEvent(EventContainer.getInstance().createEvent(getVoteWinner()));
					}
					else
					{
						setCurrentEvent(EventContainer.getInstance().createRandomEvent());
					}
					
					setVotePhase(VotePhase.RUNNING);
					break;
			}
		}
	}
	
	public class VoteCountdown extends Clock
	{
		public VoteCountdown(int time)
		{
			super(time);
		}
		
		@Override
		public void clockBody()
		{
			if ((counter == Config.getInstance().getInt(0, "showVotePopupAt")) && Config.getInstance().getBoolean(0, "votePopupEnabled"))
			{
				for (Integer playerId : Out.getEveryPlayer())
				{
					if (!popupOffList.contains(playerId))
					{
						ManagerNpc.getInstance().showVoteList(playerId);
					}
				}
			}
			
			switch (counter)
			{
				case 1800:
				case 1200:
				case 600:
				case 300:
				case 60:
					announce("" + (counter / 60) + " minutes left to vote.");
					break;
				case 30:
				case 10:
				case 5:
					announce("" + counter + " seconds left to vote.");
					break;
			}
		}
		
		@Override
		protected void onZero()
		{
			setVotePhase(VotePhase.CHECK);
			voteSchedule(1);
		}
	}
	
	private enum VotePhase
	{
		VOTE,
		RUNNING,
		CHECK,
	}
	
	public static final Vote getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public FastMap<Integer, Integer> votes;
	private final FastList<Integer> bannedEvents;
	VoteCountdown voteCountdown;
	
	VotePhase phase;
	
	private final VoteCore voteCore;
	
	private AbstractEvent currentEvent;
	
	final FastList<Integer> popupOffList;
	
	@SuppressWarnings("synthetic-access")
	public Vote()
	{
		votes = new FastMap<>();
		voteCore = new VoteCore();
		bannedEvents = new FastList<>();
		popupOffList = new FastList<>();
		
		setVotePhase(VotePhase.VOTE);
		voteSchedule(1);
	}
	
	public boolean addVote(Integer player, int eventId)
	{
		if (votes.containsKey(player))
		{
			L2World.getInstance().getPlayer(player).sendMessage("[Event Manager]: You already voted for an event!");
			return false;
		}
		L2World.getInstance().getPlayer(player).sendMessage("[Event Manager]: You succesfully voted for the event");
		votes.put(player, eventId);
		return true;
	}
	
	public void announce(String text)
	{
		Broadcast.toAllOnlinePlayers(new CreatureSay(0, 18, "", "[Event] " + text));
	}
	
	public void checkIfCurrent(AbstractEvent event)
	{
		if (getCurrentEvent() == event)
		{
			announce("Next event in " + (Config.getInstance().getInt(0, "voteTime") / 60) + " mins!");
			setVotePhase(VotePhase.VOTE);
			voteSchedule(1);
		}
	}
	
	public FastList<Integer> getBannedEvents()
	{
		return bannedEvents;
	}
	
	public AbstractEvent getCurrentEvent()
	{
		return currentEvent;
	}
	
	public int getVoteCount()
	{
		return votes.size();
	}
	
	public int getVoteCount(int event)
	{
		try
		{
			int count = 0;
			
			if (votes == null)
			{
				return 0;
			}
			if (votes.values() == null)
			{
				return 0;
			}
			
			for (int e : votes.values())
			{
				if (e == event)
				{
					count++;
				}
			}
			return count;
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public FastMap<Integer, Integer> getVotes()
	{
		return votes;
	}
	
	public String getVoteTimeLeft()
	{
		return voteCountdown.getTimeInString();
	}
	
	int getVoteWinner()
	{
		int old = 0;
		FastMap<Integer, Integer> temp = new FastMap<>();
		
		for (int vote : votes.values())
		{
			if (!temp.containsKey(vote))
			{
				temp.put(vote, 1);
			}
			else
			{
				old = temp.get(vote);
				old++;
				temp.getEntry(vote).setValue(old);
			}
		}
		
		int max = temp.head().getNext().getValue();
		int result = temp.head().getNext().getKey();
		
		for (Map.Entry<Integer, Integer> entry : temp.entrySet())
		{
			if (entry.getValue() > max)
			{
				max = entry.getValue();
				result = entry.getKey();
			}
		}
		
		votes.clear();
		temp = null;
		return result;
	}
	
	void setCurrentEvent(AbstractEvent event)
	{
		currentEvent = event;
	}
	
	void setVotePhase(VotePhase p)
	{
		phase = p;
	}
	
	public void switchPopup(Integer player)
	{
		if (popupOffList.contains(player))
		{
			popupOffList.remove(player);
		}
		else
		{
			popupOffList.add(player);
		}
	}
	
	protected void voteSchedule(int time)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(voteCore, time);
	}
}