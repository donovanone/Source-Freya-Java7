package com.l2jserver.gameserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;

public class Prem
{
	
	private long _end_pr_date;
	public static final Prem getInstance()
	{
		return SingletonHolder._instance;
	}
	public long getPremServiceData(String playerAcc)
	{
		
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT premium_service,enddate FROM character_premium WHERE account_name=?");
			statement.setString(1, playerAcc);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				if (Config.USE_PREMIUMSERVICE)
				{
					_end_pr_date = rset.getLong("enddate");
				}
			}
			statement.close();			
		}
		catch (Exception e)
		{

		}
		finally
		{
			L2DatabaseFactory.close(con);
		}
		return _end_pr_date;
	}
	//Êîíåö
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final Prem _instance = new Prem();
	}
}