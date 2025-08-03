package com.l2jserver.gameserver.instancemanager.votereward;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.Announcements;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;


/**
 * @author fissban
 */
public class VoteRewardTopzone
{
	public static Logger _log = Logger.getLogger(VoteRewardTopzone.class.getName());
	
	public static int _delayForCheck = Config.VOTE_SYSTEM_CHECK_TIME * 1000;
	
	public static int _votesneed = 0;
	
	public static List<String> _ips = new ArrayList<>();
	
	public static List<String> _accounts = new ArrayList<>();
	
	public static int _lastVoteCount = 0;
	
	public VoteRewardTopzone()
	{
		setLastVoteCount(getVotes());
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoReward(), _delayForCheck, _delayForCheck);
	}
	
	public class AutoReward implements Runnable
	{
		@Override
		public void run()
		{
			int votes = getVotes();
			_log.info("VoteReward: Current Votes Hopcone/Topzone: " + votes);
			
			if (votes >= (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT))
			{
				for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayers().values())
				{
					if (onlinePlayer.isOnline() && !onlinePlayer.getClient().isDetached() && !_accounts.contains(onlinePlayer.getAccountName()) && !_ips.contains(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress()))
					{
						String[] parase = Config.VOTE_SYSTEM_ITEM_ID_TOPZONE.split(",");
						String[] parase3 = Config.VOTE_SYSTEM_ITEM_COUNT_TOPZONE.split(",");
						
						for (int o = 0; o < parase.length; o++)
						{
							int parase2 = Integer.parseInt(parase[o]);
							int parase4 = Integer.parseInt(parase3[o]);
							
							onlinePlayer.addItem("vote_reward", parase2, parase4, onlinePlayer, true);
						}
						
						_ips.add(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress());
						_accounts.add(onlinePlayer.getAccountName());
					}
				}
				
				_log.info("VoteReward Topzone: All players has been rewared!");
				
				Announcements.getInstance().announceToAll("be rewarded!");
				Announcements.getInstance().announceToAll("for Topzone vote!");
				setLastVoteCount(getLastVoteCount() + Config.VOTE_SYSTEM_COUNT);
			}
			
			if (getLastVoteCount() == 0)
			{
				setLastVoteCount(votes);
			}
			else if ((((getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votes) > Config.VOTE_SYSTEM_COUNT) || (votes > (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT)))
			{
				setLastVoteCount(votes);
			}
			
			_votesneed = (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votes;
			
			if (_votesneed == 0)
			{
				_votesneed = Config.VOTE_SYSTEM_COUNT;
			}
			
			Announcements.getInstance().announceToAll("== VoteReward ==");
			Announcements.getInstance().announceToAll("Current votes " + votes + ".");
			Announcements.getInstance().announceToAll("Missing " + _votesneed + " votes in Topzone!");
			
			_ips.clear();
			_accounts.clear();
		}
	}
	
	/**
	 * Get the votes of TOPZONE
	 * @return
	 */
	public static int getVotes()
	{
		int votes = 0;
		try
		{
			URLConnection con = new URL(Config.VOTE_SYSTEM_PAGE_TOPZONE).openConnection();
			
			con.addRequestProperty("User-Agent", "L2TopZone");
			con.setConnectTimeout(5000);
			
			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
			{
				
				String inputLine;
				while ((inputLine = in.readLine()) != null)
				{
					if (inputLine.contains("fa fa-fw fa-lg fa-thumbs-up"))
					{
						return Integer.valueOf(inputLine.split(">")[3].replace("</span", ""));
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Error while getting Topzone server vote count.");
		}
		return votes;
	}
	
	public void setLastVoteCount(int voteCount)
	{
		_lastVoteCount = voteCount;
	}
	
	public int getLastVoteCount()
	{
		return _lastVoteCount;
	}
	
	public static VoteRewardTopzone getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public static class SingletonHolder
	{
		protected static final VoteRewardTopzone _instance = new VoteRewardTopzone();
	}
}