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
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.PartyControl;
import com.l2jserver.gameserver.network.serverpackets.GMViewItemList;

/**
 * Voice Command - PartyControlVC
 * @author swarlog
 */

public class PartyControlVC implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"partycontrol",
		"showpvpparties",
		"viewinventory"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equals("partycontrol"))
		{
			if (activeChar == null)
			{
				return false;
			}
			
			if (activeChar.getParty() == null)
			{
				activeChar.sendMessage("You are not in a party.");
				return false;
			}
			
			if (activeChar.getParty().getLeader() != activeChar)
			{
				activeChar.sendMessage("Only your party leader can control your party.");
				return false;
			}
			
			PartyControl.showPartyControlWindow(activeChar);
		}
		
		if (command.equals("showpvpparties"))
		{
			if (activeChar == null)
			{
				return false;
			}
			
			if (Config.ALLOW_PARTY_PVP_MODE && Config.ALLOW_PVP_PARTIES_COMMAND)
			{
				PartyControl.showPvpPartiesWindow(activeChar);
			}
		}
		
		if (command.equals("viewinventory"))
		{
			if (activeChar == null)
			{
				return false;
			}
			
			if (!Config.ALLOW_VIEW_INVENTORY_COMMAND)
			{
				return false;
			}
			
			L2Object obj = activeChar.getTarget();
			if (obj == null)
			{
				activeChar.sendMessage("You need to target a party member to use this.");
				return false;
			}
			if (!(obj instanceof L2PcInstance))
			{
				activeChar.sendMessage("You need to target a party member to use this.");
				return false;
			}
			
			L2PcInstance trg = (L2PcInstance)obj;
			if (activeChar.getParty() == null)
			{
				activeChar.sendMessage("You need to target a party member to use this.");
				return false;
			}
			if (!activeChar.getParty().getPartyMembers().contains(trg))
			{
				activeChar.sendMessage("You need to target a party member to use this.");
				return false;
			}
			
			activeChar.sendPacket(new GMViewItemList(trg));
		}
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
