package com.l2jserver.gameserver.communitybbs.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javolution.text.TextBuilder;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;

public class TeleportBBSManager extends BaseBBSManager
{
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(TeleportBBSManager.class.getName());
	
	public class CBteleport
	{
		public int TpId = 0;	    // Teport location ID
		public String TpName = "";	// Location name
		public int PlayerId = 0;	// charID
		public int xC = 0;			// Location coords X
		public int yC = 0;			// Location coords Y
		public int zC = 0;			// Location coords Z
	}

	private static TeleportBBSManager _Instance = null;

	public static TeleportBBSManager getInstance()
	{
		if(_Instance == null)
			_Instance = new TeleportBBSManager();
		return _Instance;
	}
	
	public String points[][];

	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if(command.equals("_bbsteleport;"))
		{
			showTp(activeChar);
		}
		else if(command.startsWith("_bbsteleport;delete;"))
		{
				    StringTokenizer stDell = new StringTokenizer(command, ";");
					stDell.nextToken();
					stDell.nextToken();
					int TpNameDell = Integer.parseInt(stDell.nextToken());
			        delTp(activeChar, TpNameDell);
					showTp(activeChar);
		}
		else if(command.startsWith("_bbsteleport;save;"))
		{
				    StringTokenizer stAdd = new StringTokenizer(command, ";");
					stAdd.nextToken();
					stAdd.nextToken();
					String TpNameAdd = stAdd.nextToken();
			        AddTp(activeChar, TpNameAdd);
					showTp(activeChar);
		}
        else if(command.startsWith("_bbsteleport;teleport;"))
		{
				    StringTokenizer stGoTp = new StringTokenizer(command, " ");
					stGoTp.nextToken();
					int xTp = Integer.parseInt(stGoTp.nextToken());
					int yTp = Integer.parseInt(stGoTp.nextToken());
					int zTp = Integer.parseInt(stGoTp.nextToken());
					int priceTp = Integer.parseInt(stGoTp.nextToken());
			        goTp(activeChar, xTp, yTp, zTp, priceTp);
					showTp(activeChar);
		}
		else
		{
					ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command
							+ " is not implemented yet</center><br><br></body></html>", "101");
					activeChar.sendPacket(sb);
					activeChar.sendPacket(new ShowBoard(null, "102"));
					activeChar.sendPacket(new ShowBoard(null, "103"));
		}
	}
	@SuppressWarnings("cast")
	private void goTp(L2PcInstance activeChar, int xTp, int yTp, int zTp, int priceTp)
	{
		if(activeChar.isDead() || activeChar.isAlikeDead() || TvTEvent.isStarted() || activeChar.isAio() || activeChar.isInSiege() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isInJail() || activeChar.isFlying() || activeChar.getKarma() > 0 || activeChar.isInDuel()){
                activeChar.sendMessage("can not be used");
				return;
            } 
			if(priceTp > 0 && activeChar.getAdena() < priceTp)
			{
               activeChar.sendMessage("does not have to be transported adena");
				return;
			}

			else
            {
                        if(priceTp > 0)
						{
							activeChar.reduceAdena("Teleport", (long) priceTp, activeChar, true);
						}
						activeChar.teleToLocation(xTp,yTp,zTp);
            }
	}
	private void showTp(L2PcInstance activeChar)
	{
		CBteleport tp;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement st = con.prepareStatement("SELECT * FROM comteleport WHERE charId=?;");
			st.setLong(1, activeChar.getObjectId());
			ResultSet rs = st.executeQuery();
			TextBuilder html = new TextBuilder();
			html.append("<table width=220>");
			while(rs.next())
			{		
				
				tp = new CBteleport();
				tp.TpId = rs.getInt("TpId");
				tp.TpName = rs.getString("name");
				tp.PlayerId = rs.getInt("charId");
				tp.xC = rs.getInt("xPos");
				tp.yC = rs.getInt("yPos");
				tp.zC = rs.getInt("zPos");
                html.append("<tr>");
                html.append("<td>");
                html.append("<button value=\""+ tp.TpName +"\" action=\"bypass -h _bbsteleport;teleport; " + tp.xC + " " + tp.yC + " " + tp.zC + " " + 100000 + "\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td>");
                html.append("<td>");
                html.append("<button value=\"Cant Use\" action=\"bypass -h _bbsteleport;delete;" + tp.TpId + "\" width=100 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">");
                html.append("</td>");
                html.append("</tr>");
			}
			html.append("</table>");

        String content = HtmCache.getInstance().getHtmForce(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/50.htm");
        NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
        adminReply.setHtml(content);
        adminReply.replace("%tp%", html.toString());
        /*separateAndSend(adminReply.getHtm(), activeChar);*/
        return;
		
		}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}

	}
	private void delTp(L2PcInstance activeChar, int TpNameDell)
	{
					Connection conDel = null;
					try
						{
							conDel = L2DatabaseFactory.getInstance().getConnection();
							PreparedStatement stDel = conDel.prepareStatement("DELETE FROM comteleport WHERE charId=? AND TpId=?;");
							stDel.setInt(1, activeChar.getObjectId());
							stDel.setInt(2, TpNameDell);
							stDel.execute();
						}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				conDel.close();
			}
			catch (Exception e)
			{
			}
		}

	}
	
	private void AddTp(L2PcInstance activeChar, String TpNameAdd)
	{
        if(activeChar.isDead() || activeChar.isAlikeDead() || activeChar.isAio()  || activeChar.isCastingNow() || activeChar.isAttackingNow())
        {
            activeChar.sendMessage("Cant Use");
            return;
        }

        if(activeChar.isInCombat())
        {
            activeChar.sendMessage("Cant Use");
            return;
        }
		
        if(activeChar.isInsideZone((byte)11) || activeChar.isInsideZone((byte)5) || activeChar.isInsideZone((byte)9) || activeChar.isInsideZone((byte)10) || activeChar.isInsideZone((byte)3) || activeChar.isInsideZone((byte)16) || activeChar.isInsideZone((byte)8) || activeChar.isFlying())
        {
            activeChar.sendMessage("Cant Use");
            return;
        }
		if(TpNameAdd.equals("") || TpNameAdd.equals(null))
		{
			activeChar.sendMessage("Cant Use");
			return;
		}
					Connection con = null;
					try
						{
							con = L2DatabaseFactory.getInstance().getConnection();
										
							PreparedStatement st = con.prepareStatement("SELECT COUNT(*) FROM comteleport WHERE charId=?;");
							st.setLong(1, activeChar.getObjectId());
							ResultSet rs = st.executeQuery();
							rs.next();
								if(rs.getInt(1) <= 9)
								{	
									PreparedStatement st1 = con.prepareStatement("SELECT COUNT(*) FROM comteleport WHERE charId=? AND name=?;");
									st1.setLong(1, activeChar.getObjectId());
									st1.setString(2, TpNameAdd);
									ResultSet rs1 = st1.executeQuery();
									rs1.next();
									if(rs1.getInt(1) == 0)
										{		
											PreparedStatement stAdd = con.prepareStatement("INSERT INTO comteleport (charId,xPos,yPos,zPos,name) VALUES(?,?,?,?,?)");
											stAdd.setInt(1, activeChar.getObjectId());
											stAdd.setInt(2, activeChar.getX());
											stAdd.setInt(3, activeChar.getY());
											stAdd.setInt(4, activeChar.getZ());
											stAdd.setString(5, TpNameAdd);
											stAdd.execute();
										}
										else
										{
											PreparedStatement stAdd = con.prepareStatement("UPDATE comteleport SET xPos=?, yPos=?, zPos=? WHERE charId=? AND name=?;");
											stAdd.setInt(1, activeChar.getObjectId());
											stAdd.setInt(2, activeChar.getX());
											stAdd.setInt(3, activeChar.getY());
											stAdd.setInt(4, activeChar.getZ());
											stAdd.setString(5, TpNameAdd);
											stAdd.execute();
										}
								}
								else
								{
								activeChar.sendMessage("Cant Use");
								}

						}
		catch (Exception e)
		{
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (Exception e)
			{
			}
		}
	}
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
	
	}
}