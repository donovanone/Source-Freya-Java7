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

import com.l2jserver.extensions.VisualArmorController;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;

public class VisualArmor implements IVoicedCommandHandler
{
   private static final String[] VOICED_COMMANDS =
   {
       "dressme", "dressMe", "DressMe", "cloakOn", "cloakOff"
   };
   
   
   public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
   {
       if(command.contains("cloakOn"))
       {
           activeChar.visualArmor.weaponLRHANDId =0;
           InventoryUpdate iu = new InventoryUpdate();
           activeChar.sendPacket(iu);
           activeChar.broadcastUserInfo();
           InventoryUpdate iu2 = new InventoryUpdate();
           activeChar.sendPacket(iu2);
           activeChar.broadcastUserInfo();
           activeChar.sendMessage("Cloak enabled.");
       }
       else if(command.contains("cloakOff"))
       {
           activeChar.visualArmor.weaponLRHANDId =1;
           InventoryUpdate iu = new InventoryUpdate();
           activeChar.sendPacket(iu);
           activeChar.broadcastUserInfo();
           InventoryUpdate iu2 = new InventoryUpdate();
           activeChar.sendPacket(iu2);
           activeChar.broadcastUserInfo();
           activeChar.sendMessage("Cloak disabled.");
       }
       else
           VisualArmorController.dressMe(activeChar);
      
       return true;
   }
   
   
   public String[] getVoicedCommandList()
   {
       return VOICED_COMMANDS;
   }
}
