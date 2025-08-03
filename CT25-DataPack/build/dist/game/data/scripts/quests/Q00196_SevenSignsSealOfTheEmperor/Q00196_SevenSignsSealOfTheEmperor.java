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
package quests.Q00196_SevenSignsSealOfTheEmperor;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.NpcSay;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import quests.Q00195_SevenSignsSecretRitualOfPriests.Q00195_SevenSignsSecretRitualOfPriests;

/**
 ** @author Gnacik 2010-12-10 Based on official server Naia
 */
public class Q00196_SevenSignsSealOfTheEmperor extends Quest
{
	// NPC's
	private static final int _iason = 30969;
	private static final int _merchant = 32584;
	private static final int _shunaiman = 32586;
	private static final int _corpse = 32598;
	private static final int _wood = 32593;
	// Items
	private static final int _water = 13808;
	private static final int _sword = 15310;
	private static final int _staff = 13809;
	private static final int _seal = 13846;
	// Misc
	private static boolean _mspawned = false;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (event.equalsIgnoreCase("mdespawn") && _mspawned)
		{
			_mspawned = false;
			npc.decayMe();
		}
		if (st == null)
		{
			return htmltext;
		}
		
		if (npc.getId() == _iason)
		{
			if (event.equalsIgnoreCase("30969-05.htm"))
			{
				st.setState(State.STARTED);
				st.set("cond", "1");
				st.playSound("ItemSound.quest_accept");
			}
			else if (event.equalsIgnoreCase("30969-06.htm"))
			{
				_mspawned = true;
				L2Npc merchant = addSpawn(_merchant, 109743, 219975, -3510, 0, false, 0);
				merchant.broadcastPacket(new NpcSay(merchant.getObjectId(), Say2.ALL, merchant.getId(), "Who dares summon the Merchant of Mammon?!"));
				startQuestTimer("mdespawn", 60000, merchant, player);
			}
			else if (event.equalsIgnoreCase("30969-12.htm"))
			{
				st.set("cond", "6");
				st.playSound("ItemSound.quest_middle");
			}
		}
		else if ((npc.getId() == _merchant) && event.equalsIgnoreCase("32584-05.htm"))
		{
			_mspawned = false;
			npc.decayMe();
			st.set("cond", "2");
			st.playSound("ItemSound.quest_middle");
		}
		else if (npc.getId() == _shunaiman)
		{
			if (event.equalsIgnoreCase("32586-07.htm"))
			{
				st.set("cond", "4");
				st.playSound("ItemSound.quest_middle");
				st.giveItems(_sword, 1);
				st.giveItems(_water, 1);
				player.sendPacket(SystemMessage.getSystemMessage(3031));
				player.sendPacket(SystemMessage.getSystemMessage(3039));
			}
			else if (event.equalsIgnoreCase("32586-15.htm"))
			{
				st.takeItems(_sword, -1);
				st.takeItems(_water, -1);
				st.takeItems(_staff, -1);
				st.takeItems(_seal, -1);
				st.set("cond", "5");
				st.playSound("ItemSound.quest_middle");
			}
		}
		else if ((npc.getId() == _wood) && event.equalsIgnoreCase("32593-02.htm"))
		{
			st.unset("cond");
			st.addExpAndSp(25000000, 250000);
			st.playSound("ItemSound.quest_finish");
			st.exitQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		if (st.getState() == State.COMPLETED)
		{
			return getAlreadyCompletedMsg(player);
		}
		
		if (npc.getId() == _iason)
		{
			if (player.getLevel() < 79)
			{
				htmltext = "30969-00.htm";
			}
			else if ((player.getQuestState(Q00195_SevenSignsSecretRitualOfPriests.class.getSimpleName()) == null) || (player.getQuestState(Q00195_SevenSignsSecretRitualOfPriests.class.getSimpleName()).getState() != State.COMPLETED))
			{
				htmltext = "30969-00.htm";
			}
			else if (st.getState() == State.CREATED)
			{
				htmltext = "30969-01.htm";
			}
			else if (st.getState() == State.STARTED)
			{
				if (st.getInt("cond") == 1)
				{
					if (!_mspawned)
					{
						htmltext = "30969-05.htm";
					}
					else
					{
						htmltext = "30969-07.htm";
					}
				}
				else if (st.getInt("cond") == 2)
				{
					st.set("cond", "3");
					st.playSound("ItemSound.quest_middle");
					htmltext = "30969-08.htm";
				}
				else if (st.getInt("cond") == 3)
				{
					htmltext = "30969-09.htm";
				}
				else if (st.getInt("cond") == 5)
				{
					htmltext = "30969-10.htm";
				}
				else if (st.getInt("cond") == 6)
				{
					htmltext = "30969-13.htm";
				}
			}
		}
		else if (npc.getId() == _merchant)
		{
			if ((st.getState() == State.STARTED) && (st.getInt("cond") == 1))
			{
				htmltext = "32584-01.htm";
			}
		}
		else if (npc.getId() == _shunaiman)
		{
			if ((st.getState() == State.STARTED) && (st.getInt("cond") == 3))
			{
				htmltext = "32586-01.htm";
			}
			else if ((st.getState() == State.STARTED) && (st.getInt("cond") == 4))
			{
				if (st.getQuestItemsCount(_seal) >= 4)
				{
					htmltext = "32586-11.htm";
				}
				else
				{
					if (st.hasQuestItems(_sword) && st.hasQuestItems(_water))
					{
						htmltext = "32586-09.htm";
					}
					else
					{
						if (!st.hasQuestItems(_sword))
						{
							st.giveItems(_sword, 1);
						}
						if (!st.hasQuestItems(_water))
						{
							st.giveItems(_water, 1);
						}
						htmltext = "32586-10.htm";
					}
					player.sendPacket(SystemMessage.getSystemMessage(3031));
					player.sendPacket(SystemMessage.getSystemMessage(3039));
				}
			}
			else if ((st.getState() == State.STARTED) && (st.getInt("cond") == 5))
			{
				htmltext = "32586-16.htm";
			}
		}
		else if (npc.getId() == _corpse)
		{
			player.sendPacket(SystemMessage.getSystemMessage(3040));
			if (st.hasQuestItems(_staff))
			{
				htmltext = "32598-00.html";
			}
			else
			{
				st.giveItems(_staff, 1);
				htmltext = "32598-01.html";
			}
		}
		else if ((npc.getId() == _wood) && (st.getState() == State.STARTED) && (st.getInt("cond") == 6))
		{
			htmltext = "32593-01.htm";
		}
		
		return htmltext;
	}
	
	public Q00196_SevenSignsSealOfTheEmperor(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(_iason);
		addTalkId(_iason);
		addTalkId(_merchant);
		addTalkId(_shunaiman);
		addTalkId(_corpse);
		addTalkId(_wood);
	}
	
	public static void main(String[] args)
	{
		new Q00196_SevenSignsSealOfTheEmperor(196, Q00196_SevenSignsSealOfTheEmperor.class.getSimpleName(), "Seven Signs, Seal of the Emperor");
	}
}
