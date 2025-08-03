/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.entity;

import java.util.StringTokenizer;

import javolution.util.FastMap;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.MapRegionTable;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
/**
 * @author Anarchy
 *
 */
public class PartyControl
{
	public static boolean canContinue(L2PcInstance p)
	{
		if (p.getParty() == null)
		{
			p.sendMessage("You are not in a party.");
			return false;
		}
		
		if (p.getParty().getLeader() != p)
		{
			p.sendMessage("Only your party leader can control your party.");
			return false;
		}
		
		return true;
	}
	
	public static void handleBypassAction(L2PcInstance p, int action, StringTokenizer st)
	{
		switch (action)
		{
			case 0:
			{
				NpcHtmlMessage htm = new NpcHtmlMessage(0);
				htm.setFile("data/html/mods/partycontrol/"+st.nextToken());
				
				p.sendPacket(htm);
				
				break;
			}
			case 1:
			{
				if (!Config.ALLOW_PARTY_PVP_MODE)
				{
					p.sendMessage("This option is disabled by the admin.");
					return;
				}
				
				if (p.getParty().isInPvpMode())
				{
					p.sendMessage("Your party is already in pvp mode.");
					return;
				}
				
				p.getParty().setIsInPvpMode(true);
				for (L2PcInstance pl : p.getParty().getPartyMembers())
				{
					if (Config.ALLOW_PARTY_MEMBERS_PVP_MODE_COLOR)
					{
						if (Config.PARTY_MEMBERS_PVP_MODE_COLOR_MODE.equals("NAME"))
						{
							pl.getAppearance().setNameColor(Config.PARTY_MEMBERS_PVP_MODE_COLOR);
						}
						else
						{
							pl.getAppearance().setTitleColor(Config.PARTY_MEMBERS_PVP_MODE_COLOR);
						}
						pl.broadcastUserInfo();
					}
					pl.sendMessage("Your party is now in pvp mode.");
				}
				
				break;
			}
			case 2:
			{
				if (!Config.ALLOW_PARTY_PVP_MODE)
				{
					p.sendMessage("This option is disabled by the admin.");
					return;
				}
				
				if (!p.getParty().isInPvpMode())
				{
					p.sendMessage("Your party is not in pvp mode.");
					return;
				}
				
				p.getParty().setIsInPvpMode(false);
				for (L2PcInstance pl : p.getParty().getPartyMembers())
				{
					if (Config.ALLOW_PARTY_MEMBERS_PVP_MODE_COLOR)
					{
						if (Config.PARTY_MEMBERS_PVP_MODE_COLOR_MODE.equals("NAME"))
						{
							pl.getAppearance().setNameColor(0xFFFFFF);
						}
						else
						{
							pl.getAppearance().setTitleColor(0xFFFFFF);
						}
						pl.broadcastUserInfo();
					}
					pl.sendMessage("Your party is now out of pvp mode.");
				}
				
				break;
			}
			case 3:
			{
				if (!Config.ALLOW_RECALL_PARTY_MEMBERS)
				{
					p.sendMessage("This option is disabled by the admin.");
					return;
				}
				
				if (p.getKarma() > 0)
				{
					p.sendMessage("You can't recall your party members while you have karma.");
					return;
				}
				
				for (L2PcInstance pl : p.getParty().getPartyMembers())
				{
					if (pl == p || pl.isIn7sDungeon() || pl.isInOlympiadMode())
					{
						continue;
					}
					if (pl.getKarma() > 0)
					{
						p.sendMessage(pl.getName()+" has not been recalled because he has karma.");
						continue;
					}
					if (pl.getPvpFlag() > 0)
					{
						p.sendMessage(pl.getName()+" has not been recalled because he is flagged.");
						continue;
					}
					
					pl.teleToLocation(p.getX(), p.getY(), p.getZ(), true);
					if (pl != p)
					{
						pl.sendMessage("You have been recalled by your party leader.");
					}
					else
					{
						pl.sendMessage("You have recalled your party members.");
					}
				}
				
				break;
			}
			case 4:
			{
				if (!Config.ALLOW_TELEPORT_PARTY_MEMBERS)
				{
					p.sendMessage("This option is disabled by the admin.");
					return;
				}
				
				if (p.getKarma() > 0)
				{
					p.sendMessage("You can't teleport your party members while you have karma.");
					return;
				}
				if (p.getPvpFlag() > 0)
				{
					p.sendMessage("You can't teleport your party members while you are flagged.");
					return;
				}
				if (p.isIn7sDungeon() || p.isInOlympiadMode())
				{
					p.sendMessage("You can't teleport your party members while you are in an event.");
					return;
				}
				
				int[] xyz = new int[3];
				xyz[0] = Integer.valueOf(st.nextToken());
				xyz[1] = Integer.valueOf(st.nextToken());
				xyz[2] = Integer.valueOf(st.nextToken());
				
				for (L2PcInstance pl : p.getParty().getPartyMembers())
				{
					if ((pl.isIn7sDungeon() || pl.isInOlympiadMode()) && pl != p)
					{
						continue;
					}
					if (pl.getKarma() > 0 && pl != p)
					{
						p.sendMessage(pl.getName()+" has not been teleported because he has karma.");
						continue;
					}
					if (pl.getPvpFlag() > 0 && pl != p)
					{
						p.sendMessage(pl.getName()+" has not been teleported because he is flagged.");
						continue;
					}
					
					pl.teleToLocation(xyz[0], xyz[1], xyz[2], true);
					if (pl != p)
					{
						pl.sendMessage("You have been teleported by your party leader.");
					}
					else
					{
						pl.sendMessage("You have teleported your party members.");
					}
				}
				
				break;
			}
			case 5:
			{
				if (!Config.ALLOW_PARTY_MAIN_ASSISTER)
				{
					p.sendMessage("This option is disabled by the admin.");
					return;
				}
				
				if (!p.getParty().isInPvpMode())
				{
					p.sendMessage("Your party needs to be in pvp mode to set the main assister.");
					return;
				}
				
				String plname = st.nextToken();
				L2PcInstance pl = L2World.getInstance().getPlayer(plname);
				
				if (pl == null)
				{
					p.sendMessage("The character you have selected doesn't exist or isn't online.");
					return;
				}
				if (!p.getParty().getPartyMembers().contains(pl))
				{
					p.sendMessage("The character you have selected isn't a party member of yours.");
					return;
				}
				
				p.getParty().setMainAssister(pl);
				for (L2PcInstance pla : p.getParty().getPartyMembers())
				{
					if (pl == pla)
					{
						pl.sendMessage("You are now the party's main assister.");
						continue;
					}
					
					pla.sendMessage(plname+" is now the party's main assister.");
				}
				
				break;
			}
		}
	}
	
	public static void showPartyControlWindow(L2PcInstance activeChar)
	{
		String htmFile = "data/html/mods/partycontrol/main.htm";

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile(activeChar.getHtmlPrefix(), htmFile);
		activeChar.sendPacket(msg);
	}
	
	public static void showPvpPartiesWindow(L2PcInstance activeChar)
	{

		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		String htmFile = "data/html/mods/partycontrol/pvpparties.htm";
		msg.setFile(activeChar.getHtmlPrefix(), htmFile);
		msg.replace("%pvpparties%", getPvpParties(activeChar));
		
		activeChar.sendPacket(msg);
	}
	
	private static String getPvpParties(L2PcInstance p)
	{
		String pvpparties = "";
		int i = 0;
		for (L2Party party : L2Party._allParties)
		{
			if (party.isInPvpMode())
			{
				pvpparties += "Leader: <font color=\"LEVEL\">"+party.getLeader().getName()+"</font> Members: <font color=\"LEVEL\">"+party.getMemberCount()+"</font> Near: <font color=\"LEVEL\">"+getNearestTown(p)+"</font><br1>";
				i++;
			}
		}
		if (i == 0)
		{
			pvpparties = "There are not pvp parties.";
		}
		else
		{
			pvpparties += "<br>A total of "+i+" pvp part"+(i == 1 ? "y." : "ies.");
		}
		
		return pvpparties;
	}
	
	private static String getNearestTown(L2PcInstance p)
	{
		FastMap<String, Integer> results = new FastMap<String, Integer>();
		for (L2PcInstance pl : p.getParty().getPartyMembers())
		{
			String areaname = MapRegionTable.getInstance().getClosestTownName(pl);
			if (results.containsKey(areaname))
			{
				int currvotes = results.get(areaname);
				results.remove(areaname);
				results.put(areaname, currvotes++);
			}
			else
			{
				results.put(areaname, 1);
			}
		}
		
		String result = null;
		int resultnum = 0;
		for (String s : results.keySet())
		{
			if (results.get(s) > resultnum)
			{
				result = s;
				resultnum = results.get(s);
			}
		}
		
		return result;
	}
}