package com.l2jserver.gameserver.communitybbs;

import java.util.StringTokenizer;

import com.l2jserver.Config;
import com.l2jserver.gameserver.communitybbs.Manager.BuffBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.ClanBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.PostBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.RegionBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.TeleportBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.TopBBSManager;
import com.l2jserver.gameserver.communitybbs.Manager.TopicBBSManager;
import com.l2jserver.gameserver.datatables.MultiSell;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

public class CommunityBoard
{
	private CommunityBoard()
	{
	}
	
	public static CommunityBoard getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void handleCommands(L2GameClient client, String command)
	{
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		
		switch (Config.COMMUNITY_TYPE)
		{
			default:
			case 0: //disabled
				activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CB_OFFLINE));
				break;
			case 1: // old
				RegionBBSManager.getInstance().parseCmd(command, activeChar);
				break;
			case 2: // new
			
				if (command.startsWith("_bbsclan") && Config.ENABLE_CLAN)
				{
					ClanBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsmemo") && Config.ENABLE_MAIL)
				{
					TopicBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbstopics") && Config.ENABLE_MAIL)
				{
					TopicBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsposts"))// FIXME
				{
					PostBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbstop"))
				{
					TopBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsloc") && Config.ENABLE_REGION)
				{
					RegionBBSManager.getInstance().parseCmd(command, activeChar);
				}
				else if (command.startsWith("_bbsteleport;"))
				{
					TeleportBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbs_buff"))
				{
					BuffBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsmultisell;"))
				{
					if(activeChar.isDead() || activeChar.isAlikeDead() || TvTEvent.isStarted() || activeChar.isInSiege() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isInJail() || activeChar.isFlying() || activeChar.getKarma() > 0 || activeChar.isInDuel()){
		                activeChar.sendMessage("Can not Use");
						return;
		            } 
					StringTokenizer st = new StringTokenizer(command, ";");
		            st.nextToken();
		            TopBBSManager.getInstance().parsecmd("_bbstop;" + st.nextToken(), activeChar);
		            int multisell = Integer.parseInt(st.nextToken());
		            MultiSell.getInstance().separateAndSend(multisell, activeChar, null, false);
					{
						activeChar.setUsingCommunity(true);
					}
				}
				else if (command.startsWith("_bbshome"))
				{
					TopBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsmultisell;"))
				{
					if(activeChar.isDead() || activeChar.isAlikeDead()  || TvTEvent.isStarted() || activeChar.isInSiege() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isInJail() || activeChar.isFlying() || activeChar.getKarma() > 0 || activeChar.isInDuel()){
		                activeChar.sendMessage("Can not Use");
						return;
		            } 
					StringTokenizer st = new StringTokenizer(command, ";");
		            st.nextToken();
		            TopBBSManager.getInstance().parsecmd("_bbstop;" + st.nextToken(), activeChar);
		            int multisell = Integer.parseInt(st.nextToken());
		            MultiSell.getInstance().separateAndSend(multisell, activeChar, null, false);
				}
				else if (command.startsWith("_bbsloc"))
				{
					RegionBBSManager.getInstance().parseCmd(command, activeChar);
				}
				else
				{
					ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command
							+ " is not implemented yet</center><br><br></body></html>", "101");
					activeChar.sendPacket(sb);
					activeChar.sendPacket(new ShowBoard(null, "102"));
					activeChar.sendPacket(new ShowBoard(null, "103"));
				}
				break;
		}
	}
	
	/**
	 * @param client
	 * @param url
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	public void handleWriteCommands(L2GameClient client, String url, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar == null)
			return;
		
		switch (Config.COMMUNITY_TYPE)
		{
			case 2:
				if (url.equals("Topic"))
				{
					TopicBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Post"))
				{
					PostBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Region"))
				{
					RegionBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Notice"))
				{
					ClanBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else
				{
					ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + url
							+ " is not implemented yet</center><br><br></body></html>", "101");
					activeChar.sendPacket(sb);
					activeChar.sendPacket(new ShowBoard(null, "102"));
					activeChar.sendPacket(new ShowBoard(null, "103"));
				}
				break;
			case 1:
				RegionBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				break;
			default:
			case 0:
				ShowBoard sb = new ShowBoard("<html><body><br><br><center>The Community board is currently disabled</center><br><br></body></html>", "101");
				activeChar.sendPacket(sb);
				activeChar.sendPacket(new ShowBoard(null, "102"));
				activeChar.sendPacket(new ShowBoard(null, "103"));
				break;
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final CommunityBoard _instance = new CommunityBoard();
	}
}
