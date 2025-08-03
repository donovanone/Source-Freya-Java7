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
package custom.DeLevelManager;

import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;

/**
 * @author Marcos-Sayan,BossForever,Souger123
 */
public class DeLevelManager extends Quest
{
	private static final int npcid = 36650; // npc id
	private static final int MinLevel = 10; // Minimum Level, (e.g if you set 10, players wont be able to be level 9).
	private static final int ItemConsumeId = 57; // Item Consume id
	private int levels ; // Item Consume id
	private static final int ItemConsumeNumEveryLevel = 100; // Item ItemConsumeNumEveryLevel
	private static String ItemName = ItemTable.getInstance().createDummyItem(ItemConsumeId).getItemName();
	
	public DeLevelManager(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(npcid);
		addTalkId(npcid);
		addStartNpc(npcid);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.startsWith("dlvl"))
		{
			Dlvl(event, npc, player, event);
		}

		return "";
	}
	
	private void Dlvl(String event, L2Npc npc, L2PcInstance player, String command)
	{
		
		try
		{
			String val = command.substring(5);
			int pointer = Integer.parseInt(val);
			int k = player.getLevel();
			levels = k - pointer;
			if (player.getInventory().getItemByItemId(ItemConsumeId) == null)
			{
				player.sendMessage("You don't have enough items!");
				return;
			}
			if (val == null)
			{
				player.sendMessage("Something went wrong!");
				return;
			}
			if (pointer < 10)
			{
				player.sendMessage("Incorrect Level Number!");
				return;
			}
			if (pointer < MinLevel)
			{
				player.sendMessage("Incorrect Level Number!");
				return;
			}
			if (player.getLevel() <= pointer)
			{
				player.sendMessage("Your level is already lower.");
				return;
			}
			if (player.getInventory().getItemByItemId(ItemConsumeId).getCount() < ItemConsumeNumEveryLevel*levels)
			{
				
				player.sendMessage("You don't have enough items!");
				return;
			}
			if (player.getInventory().getItemByItemId(ItemConsumeId).getCount() >= ItemConsumeNumEveryLevel)
			{
				k = player.getLevel();
				final byte lvl = Byte.parseByte(pointer + "");	
				player.getStat().setLevel(lvl);
				player.sendMessage("Congratulations! You are now "+pointer+" level.");
				for(L2Skill sk : player.getAllSkills())
				{
					player.removeSkill(sk);
				}
				player.broadcastStatusUpdate();
				player.broadcastUserInfo();
				player.sendPacket(new UserInfo(player));
				player.sendSkillList();
				levels = k - pointer;
				player.destroyItemByItemId("DlvlManager", ItemConsumeId, ItemConsumeNumEveryLevel*levels, player, true);
			}
		}
		catch (Exception e)
		{
			player.sendMessage("Something went wrong try again.");
		}
	}
	
	
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		final int npcId = npc.getId();
		if (player.getQuestState(getName()) == null)
		{
			newQuestState(player);
		}
		if (npcId == npcid)
		{
			String filename = "./data/scripts/custom/DeLevelManager/1.htm";	
			NpcHtmlMessage html = new NpcHtmlMessage(1);
			html.setFile(filename);
			html.replace("%player%", player.getName());
			html.replace("%itemname%", ItemName);
			html.replace("%price%", ""+ItemConsumeNumEveryLevel+"");
			
			player.sendPacket(html);
			filename = null;
			html = null;
		}
		return "";
	}
	
	public static void main(final String[] args)
	{
		new DeLevelManager(-1, DeLevelManager.class.getSimpleName(), "custom");
		System.out.println("De Level Manager by `Heroin has been loaded successfully!");
	}
}