package net.vortextengine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.vortextengine.tasks.MonthlyResetTask;
import net.vortextengine.tasks.TriesResetTask;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class VoteMain
{
	private static boolean hasVotedHop;
	private static boolean hasVotedTop;
	
	public VoteMain()
	{
	}
	
	public static void load()
	{
		System.out.println("Vortex Vote Reward System Started Successfully.");
		System.out.println("Modicated By Donovan.");
		System.out.println("Cracked and reworked.");
		TriesResetTask.getInstance();
		MonthlyResetTask.getInstance();
	}
	
	protected static int getHopZoneVotes()
	{
		try
		{
			URL url = new URL(Config.VOTE_LINK_HOPZONE);
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
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	protected static int getTopZoneVotes()
	{
		int votes = 0;
		try
		{
			URLConnection con = new URL(Config.VOTE_LINK_TOPZONE).openConnection();
			
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
			e.printStackTrace();
		}
		return votes;
	}
	
	public static String hopCd(L2PcInstance player)
	{
		long hopCdMs = 0;
		long voteDelay = 43200000L;
		PreparedStatement statement = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT lastVoteHopzone FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				hopCdMs = rset.getLong("lastVoteHopzone");
			}
		}
		catch (Exception e)
		{
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		
		Date resultdate = new Date(hopCdMs + voteDelay);
		return sdf.format(resultdate);
	}
	
	public static String topCd(L2PcInstance player)
	{
		long topCdMs = 0;
		long voteDelay = 43200000L;
		PreparedStatement statement = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT lastVoteTopzone FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				topCdMs = rset.getLong("lastVoteTopzone");
			}
		}
		catch (Exception e)
		{
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		
		Date resultdate = new Date(topCdMs + voteDelay);
		return sdf.format(resultdate);
	}
	
	public static String whosVoting()
	{
		for (L2PcInstance voter : L2World.getInstance().getAllPlayers().values())
		{
			if (voter.isVoting())
			{
				return voter.getName();
			}
		}
		return "None";
	}
	
	public static void hopvote(final L2PcInstance player)
	{
		long lastVoteHopzone = 0L;
		long voteDelay = 43200000L;
		final int firstvoteshop;
		
		firstvoteshop = getHopZoneVotes();
		
		class hopvotetask implements Runnable
		{
			private final L2PcInstance p;
			
			public hopvotetask(L2PcInstance player)
			{
				p = player;
			}
			
			@Override
			public void run()
			{
				if (firstvoteshop < getHopZoneVotes())
				{
					p.setIsVoting(false);
					VoteMain.setHasVotedHop(player);
					p.sendMessage("Thank you for voting for us!");
					VoteMain.updateLastVoteHopzone(p);
					VoteMain.updateVotes(p);
				}
				else
				{
					p.setIsVoting(false);
					p.sendMessage("You did not vote.Please try again.");
					VoteMain.setTries(player, VoteMain.getTries(p) - 1);
				}
			}
			
		}
		
		PreparedStatement statement = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT lastVoteHopzone FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				lastVoteHopzone = rset.getLong("lastVoteHopzone");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (getTries(player) <= 0)
		{
			player.sendMessage("Due to your multiple failures in voting you lost your chance to vote today");
		}
		else if (((lastVoteHopzone + voteDelay) < System.currentTimeMillis()) && (getTries(player) > 0))
		{
			for (L2PcInstance j : L2World.getInstance().getAllPlayers().values())
			{
				if (j.isVoting())
				{
					player.sendMessage("Someone is already voting.Wait for your turn please!");
					return;
				}
			}
			
			player.setIsVoting(true);
			player.sendMessage("Go fast on the site and vote on the hopzone banner!");
			player.sendMessage("You have " + Config.SECS_TO_VOTE + " seconds.Hurry!");
			ThreadPoolManager.getInstance().scheduleGeneral(new hopvotetask(player), Config.SECS_TO_VOTE * 1000);
		}
		else if ((getTries(player) <= 0) && ((lastVoteHopzone + voteDelay) < System.currentTimeMillis()))
		{
			for (L2PcInstance j : L2World.getInstance().getAllPlayers().values())
			{
				if (j.isVoting())
				{
					player.sendMessage("Someone is already voting.Wait for your turn please!");
					return;
				}
			}
			
			player.setIsVoting(true);
			player.sendMessage("Go fast on the site and vote on the hopzone banner!");
			player.sendMessage("You have " + Config.SECS_TO_VOTE + " seconds.Hurry!");
			ThreadPoolManager.getInstance().scheduleGeneral(new hopvotetask(player), Config.SECS_TO_VOTE * 1000);
			
		}
		else
		{
			player.sendMessage("12 hours have to pass till you are able to vote again.");
		}
		
	}
	
	public static void topvote(final L2PcInstance player)
	{
		long lastVoteTopzone = 0L;
		long voteDelay = 43200000L;
		final int firstvotestop;
		
		firstvotestop = getTopZoneVotes();
		
		class topvotetask implements Runnable
		{
			private final L2PcInstance p;
			
			public topvotetask(L2PcInstance player)
			{
				p = player;
			}
			
			@Override
			public void run()
			{
				if (firstvotestop < getTopZoneVotes())
				{
					p.setIsVoting(false);
					VoteMain.setHasVotedTop(p);
					p.sendMessage("Thank you for voting for us!");
					VoteMain.updateLastVoteTopzone(p);
					VoteMain.updateVotes(p);
				}
				else
				{
					p.setIsVoting(false);
					p.sendMessage("You did not vote.Please try again.");
					VoteMain.setTries(p, VoteMain.getTries(p) - 1);
				}
			}
			
		}
		
		PreparedStatement statement = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT lastVoteTopzone FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				lastVoteTopzone = rset.getLong("lastVoteTopzone");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (getTries(player) <= 0)
		{
			player.sendMessage("Due to your multiple failures in voting you lost your chance to vote today");
		}
		else if ((getTries(player) <= 0) && ((lastVoteTopzone + voteDelay) < System.currentTimeMillis()))
		{
			for (L2PcInstance j : L2World.getInstance().getAllPlayers().values())
			{
				if (j.isVoting())
				{
					player.sendMessage("Someone is already voting.Wait for your turn please!");
					return;
				}
			}
			player.setIsVoting(true);
			player.sendMessage("Go fast on the site and vote on the topzone banner!");
			player.sendMessage((new StringBuilder()).append("You have ").append(Config.SECS_TO_VOTE).append(" seconds.Hurry!").toString());
			ThreadPoolManager.getInstance().scheduleGeneral(new topvotetask(player), Config.SECS_TO_VOTE * 1000);
		}
		else if (((lastVoteTopzone + voteDelay) < System.currentTimeMillis()) && (getTries(player) > 0))
		{
			for (L2PcInstance j : L2World.getInstance().getAllPlayers().values())
			{
				if (j.isVoting())
				{
					player.sendMessage("Someone is already voting.Wait for your turn please!");
					return;
				}
			}
			player.setIsVoting(true);
			player.sendMessage("Go fast on the site and vote on the topzone banner!");
			player.sendMessage((new StringBuilder()).append("You have ").append(Config.SECS_TO_VOTE).append(" seconds.Hurry!").toString());
			ThreadPoolManager.getInstance().scheduleGeneral(new topvotetask(player), Config.SECS_TO_VOTE * 1000);
		}
		else
		{
			player.sendMessage("12 hours have to pass till you are able to vote again.");
		}
		
	}
	
	public static void hasVotedHop(L2PcInstance player)
	{
		int hasVotedHop = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT hasVotedHop FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				hasVotedHop = rset.getInt("hasVotedHop");
			}
			
			if (hasVotedHop == 1)
			{
				setHasVotedHop(true);
			}
			else if (hasVotedHop == 0)
			{
				setHasVotedHop(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void hasVotedTop(L2PcInstance player)
	{
		int hasVotedTop = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT hasVotedTop FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				hasVotedTop = rset.getInt("hasVotedTop");
			}
			
			if (hasVotedTop == 1)
			{
				setHasVotedTop(true);
			}
			else if (hasVotedTop == 0)
			{
				setHasVotedTop(false);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void updateVotes(L2PcInstance activeChar)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET monthVotes=?, totalVotes=? WHERE charId=?");
			
			statement.setInt(1, getMonthVotes(activeChar) + 1);
			statement.setInt(2, getTotalVotes(activeChar) + 1);
			statement.setInt(3, activeChar.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setHasVotedHop(L2PcInstance activeChar)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET hasVotedHop=? WHERE charId=?");
			
			statement.setInt(1, 1);
			statement.setInt(2, activeChar.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setHasVotedTop(L2PcInstance activeChar)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET hasVotedTop=? WHERE charId=?");
			
			statement.setInt(1, 1);
			statement.setInt(2, activeChar.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setHasNotVotedHop(L2PcInstance activeChar)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET hasVotedHop=? WHERE charId=?");
			
			statement.setInt(1, 0);
			statement.setInt(2, activeChar.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setHasNotVotedTop(L2PcInstance activeChar)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET hasVotedTop=? WHERE charId=?");
			
			statement.setInt(1, 0);
			statement.setInt(2, activeChar.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int getTries(L2PcInstance player)
	{
		int tries = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT tries FROM characters WHERE charId=?");
			statement.setInt(1, player.getObjectId());
			for (ResultSet rset = statement.executeQuery(); rset.next();)
			{
				tries = rset.getInt("tries");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tries;
	}
	
	public static void setTries(L2PcInstance player, int tries)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET tries=? WHERE charId=?");
			
			statement.setInt(1, tries);
			statement.setInt(2, player.getObjectId());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int getMonthVotes(L2PcInstance player)
	{
		int monthVotes = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT monthVotes FROM characters WHERE charId=?");
			
			statement.setInt(1, player.getObjectId());
			for (ResultSet rset = statement.executeQuery(); rset.next();)
			{
				monthVotes = rset.getInt("monthVotes");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return monthVotes;
	}
	
	public static int getTotalVotes(L2PcInstance player)
	{
		int totalVotes = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT totalVotes FROM characters WHERE charId=?");
			
			statement.setInt(1, player.getObjectId());
			for (ResultSet rset = statement.executeQuery(); rset.next();)
			{
				totalVotes = rset.getInt("totalVotes");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return totalVotes;
	}
	
	public static int getBigTotalVotes(L2PcInstance player)
	{
		int bigTotalVotes = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT SUM(totalVotes) FROM characters");
			
			for (ResultSet rset = statement.executeQuery(); rset.next();)
			{
				bigTotalVotes = rset.getInt("SUM(totalVotes)");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bigTotalVotes;
	}
	
	public static int getBigMonthVotes(L2PcInstance player)
	{
		int bigMonthVotes = -1;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT SUM(monthVotes) FROM characters");
			
			for (ResultSet rset = statement.executeQuery(); rset.next();)
			{
				bigMonthVotes = rset.getInt("SUM(monthVotes)");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bigMonthVotes;
	}
	
	public static void updateLastVoteHopzone(L2PcInstance player)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET lastVoteHopzone=? WHERE charId=?");
			statement.setLong(1, System.currentTimeMillis());
			statement.setInt(2, player.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void updateLastVoteTopzone(L2PcInstance player)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET lastVoteTopzone=? WHERE charId=?");
			statement.setLong(1, System.currentTimeMillis());
			statement.setInt(2, player.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// Getters and Setters
	public static boolean hasVotedHop()
	{
		return hasVotedHop;
	}
	
	public static void setHasVotedHop(boolean hasVotedHop)
	{
		VoteMain.hasVotedHop = hasVotedHop;
	}
	
	public static boolean hasVotedTop()
	{
		return hasVotedTop;
	}
	
	public static void setHasVotedTop(boolean hasVotedTop)
	{
		VoteMain.hasVotedTop = hasVotedTop;
	}
}