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
public class VoteRewardAdminsPro
{
	public static Logger _log = Logger.getLogger(VoteRewardAdminsPro.class.getName());
	
	public static int _delayForCheck = Config.VOTE_SYSTEM_CHECK_TIME * 1000;
	
	public static int _votesneed = 0;
	
	public static List<String> _ips = new ArrayList<>();
	
	public static List<String> _accounts = new ArrayList<>();
	
	public static int _lastVoteCount = 0;
	
	public VoteRewardAdminsPro()
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
			_log.info("VoteReward: Current Votes Top AdminsPro " + votes);
			
			if (votes >= (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT))
			{
				for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayers().values())
				{
					if (onlinePlayer.isOnline() && !onlinePlayer.getClient().isDetached() && !_accounts.contains(onlinePlayer.getAccountName()) && !_ips.contains(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress()))
					{
						String[] parase = Config.VOTE_SYSTEM_ITEM_ID_ADMINSPRO.split(",");
						String[] parase3 = Config.VOTE_SYSTEM_ITEM_COUNT_ADMINSPRO.split(",");
						
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
				
				_log.info("AutoVoteRewardManager AdminsPro: All players has been rewarded for voting");
				
				Announcements.getInstance().announceToAll("be rewarded!");
				Announcements.getInstance().announceToAll("for AdminsPro vote!");
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
			Announcements.getInstance().announceToAll("Missing " + _votesneed + " votes in Top AdminsPro!");
			
			_ips.clear();
			_accounts.clear();
		}
	}
	
	public int getVotes()
	{
		try
		{
			URL url = new URL("http://hopzone.com.es/getVotefromServer.php?ID=" + Config.VOTE_SYSTEM_PAGE_ADMINSPRO);
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.76");
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				return Integer.parseInt(inputLine);
			}
		}
		catch (IOException e)
		{
			Announcements.getInstance().announceToAll("Top AdminsPro is offline");
			_log.warning("AutoVoteRewardHandler: " + e);
		}
		
		return 0;
	}
	
	public void setLastVoteCount(int voteCount)
	{
		_lastVoteCount = voteCount;
	}
	
	public int getLastVoteCount()
	{
		return _lastVoteCount;
	}
	
	public static VoteRewardAdminsPro getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public static class SingletonHolder
	{
		protected static final VoteRewardAdminsPro _instance = new VoteRewardAdminsPro();
	}
}