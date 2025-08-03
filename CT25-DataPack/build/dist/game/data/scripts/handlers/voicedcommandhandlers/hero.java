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
package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.SocialAction;

/**
 * @author Kinho!
 */
public class hero implements IVoicedCommandHandler
{
 private static final String[] VOICED_COMMANDS = {"hero"};

 public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
 if (command.equalsIgnoreCase("hero"))
 {
 if(activeChar.getInventory().getItemByItemId(Config.HERO_ITEM_ID) != null && activeChar.getInventory().getItemByItemId(Config.HERO_ITEM_ID).getCount() >= Config.HERO_ITEM_COUNT)
 {
 activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
 activeChar.setHero(true);
 activeChar.sendMessage("You became hero untill restart and gave 1 gold dragon");
 activeChar.broadcastUserInfo();
 }
 else
 {
 activeChar.sendMessage("You need 1 gold dragon to become hero.");
 return true;
 }
 }
 return false;
 }
 public String[] getVoicedCommandList()
 {
 return VOICED_COMMANDS;
 }
}
