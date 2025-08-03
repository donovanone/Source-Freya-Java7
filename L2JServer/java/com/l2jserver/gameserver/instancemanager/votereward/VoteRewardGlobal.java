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
public class VoteRewardGlobal
{
	public static Logger _log = Logger.getLogger(VoteRewardGlobal.class.getName());
	
	private static final int delayForCheck = Config.VOTE_SYSTEM_CHECK_TIME * 1000;
	public int votesneed;
	
	public static List<String> _ips = new ArrayList<>();
	public static List<String> _accounts = new ArrayList<>();
	
	private static int lastVoteCount = 0;
	public int Hopzone = 0;
	public int Topzone = 0;
	public int L2jBrasil = 0;
	public int AdminsPro = 0;
	
	public VoteRewardGlobal()
	{
		setLastVoteCount(getVotes());
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoReward(), delayForCheck, delayForCheck);
	}
	
	public class AutoReward implements Runnable
	{
		@Override
		public void run()
		{
			int votos = getVotes();
			_log.info("VoteReward: Current votes " + votos);
			
			if (votos >= (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT))
			{
				for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayers().values())
				{
					if (onlinePlayer.isOnline() && !onlinePlayer.getClient().isDetached() && !_accounts.contains(onlinePlayer.getAccountName()) && !_ips.contains(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress()))
					{
						String[] parase = Config.VOTE_SYSTEM_ITEM_ID.split(",");
						String[] parase3 = Config.VOTE_SYSTEM_ITEM_COUNT.split(",");
						
						for (int o = 0; o < parase.length; o++)
						{
							int parase2 = Integer.parseInt(parase[o]);
							int parase4 = Integer.parseInt(parase3[o]);
							
							onlinePlayer.addItem("vote_reward", parase2, parase4, onlinePlayer, true);
							_log.info(onlinePlayer.getAccountNamePlayer() + " has been rewarded for voting");
						}
						
						_ips.add(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress());
						_accounts.add(onlinePlayer.getAccountName());
					}
				}
				
				_log.info("AutoVoteReward Global: All players has been rewared!");
				
				Announcements.getInstance().announceToAll("be rewarded!");
				Announcements.getInstance().announceToAll("for vote!");
				setLastVoteCount(getLastVoteCount() + Config.VOTE_SYSTEM_COUNT);
			}
			
			if (getLastVoteCount() == 0)
			{
				setLastVoteCount(votos);
			}
			else if ((((getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votos) > Config.VOTE_SYSTEM_COUNT) || (votos > (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT)))
			{
				setLastVoteCount(votos);
			}
			
			votesneed = (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votos;
			
			if (votesneed == 0)
			{
				votesneed = Config.VOTE_SYSTEM_COUNT;
			}
			
			Announcements.getInstance().announceToAll("== VoteReward ==");
			Announcements.getInstance().announceToAll("Contamos con: " + votos + " totales.");
			
			if (Config.VOTE_SYSTEM_HOPZONE)
			{
				Announcements.getInstance().announceToAll("Current votes " + Hopzone + " in Hopzone.");
			}
			if (Config.VOTE_SYSTEM_TOPZONE)
			{
				Announcements.getInstance().announceToAll("Current votes " + Topzone + " in Topzone.");
			}
			if (Config.VOTE_SYSTEM_BRASIL)
			{
				Announcements.getInstance().announceToAll("Current votes " + L2jBrasil + " in TopBrasil.");
			}
			if (Config.VOTE_SYSTEM_ADMINSPRO)
			{
				Announcements.getInstance().announceToAll("Current votes " + AdminsPro + " in Top AdminsPro.");
			}
			Announcements.getInstance().announceToAll("Missing " + votesneed + " votes!");
			
			_ips.clear();
			_accounts.clear();
		}
	}
	
	public int getVotes()
	{
		int votos = 0;
		if (Config.VOTE_SYSTEM_BRASIL)
		{
			L2jBrasil = getVotesBrasil();
			votos += L2jBrasil;
		}
		if (Config.VOTE_SYSTEM_HOPZONE)
		{
			Hopzone = getVotesHopzone();
			votos += Hopzone;
		}
		if (Config.VOTE_SYSTEM_TOPZONE)
		{
			Topzone = getVotesTopzone();
			votos += Topzone;
		}
		if (Config.VOTE_SYSTEM_ADMINSPRO)
		{
			AdminsPro = getVotesAdmins();
			votos += AdminsPro;
		}
		return votos;
	}
	
	private int getVotesBrasil()
	{
		try
		{
			URL url = new URL(Config.VOTE_SYSTEM_PAGE_BRASIL);
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.76");
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			
			boolean search = false;
			int count = 0;
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("<td class=\"stats_left\">Este M"))
				{
					search = true;
				}
				else if (search && inputLine.contains("		<td class=\"stats2\">"))
				{
					if (count > 0)
					{
						String votes = inputLine.replace("		<td class=\"stats2\">", "");
						votes = votes.replace("</td>", "");
						return Integer.parseInt(votes);
					}
					count++;
				}
			}
		}
		catch (IOException e)
		{
			Announcements.getInstance().announceToAll("TopBrasil is offline");
			_log.warning("AutoVoteRewardHandler: " + e);
		}
		return 0;
	}
	
	private int getVotesHopzone()
	{
		try
		{
			URL url = new URL(Config.VOTE_SYSTEM_PAGE_HOPZONE);
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.76");
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("rank anonymous tooltip"))
				{
					return Integer.valueOf(inputLine.split(">")[2].replace("</span", ""));
				}
			}
		}
		catch (IOException e)
		{
			Announcements.getInstance().announceToAll("Hopzone is offline");
			_log.warning("AutoVoteRewardHandler: " + e);
		}
		return 0;
	}
	
	private int getVotesTopzone()
	{
		try
		{
			URL url = new URL(Config.VOTE_SYSTEM_PAGE_TOPZONE);
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.76");
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			
			while ((inputLine = in.readLine()) != null)
			{
				if (inputLine.contains("<div class=\"rank\"><div class=\"votes2\">Votes:<br>"))
				{
					String votes = inputLine.replace("<div class=\"rank\"><div class=\"votes2\">Votes:<br>", "");
					votes = votes.replace("</div></div>", "");
					votes = votes.trim();
					int o = Integer.parseInt(votes);
					return Integer.valueOf(o);
				}
			}
		}
		catch (IOException e)
		{
			Announcements.getInstance().announceToAll("Topzone is offline");
			_log.warning("AutoVoteRewardHandler: " + e);
		}
		return 0;
	}
	
	private int getVotesAdmins()
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
		lastVoteCount = voteCount;
	}
	
	public int getLastVoteCount()
	{
		return lastVoteCount;
	}
	
	public static VoteRewardGlobal getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public static class SingletonHolder
	{
		protected static final VoteRewardGlobal _instance = new VoteRewardGlobal();
	}
}