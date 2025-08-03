/*
 * Copyright (C) 2004-2014 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.communitybbs.Manager.RegionBBSManager;
import com.l2jserver.gameserver.datatables.CharNameTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.communityserver.CommunityServerThread;
import com.l2jserver.gameserver.network.communityserver.writepackets.WorldInfo;
import com.l2jserver.gameserver.network.serverpackets.PartySmallWindowAll;
import com.l2jserver.gameserver.network.serverpackets.PartySmallWindowDeleteAll;

/**
 * Change name voiced command.
 * @author Zoey76
 */
public class ChangeName implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands =
    {
        "changename"
    };
    
    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
    {
        if ((command.startsWith("changename")))
        {
            // Check if parameters have been sent.
            if (params == null)
            {
                activeChar.sendMessage("A name is required!");
                return false;
            }
            
            // Check if the parameter doesn't have special characters.
            if (!params.matches("[A-Za-z0-9]+"))
            {
                activeChar.sendMessage("This name is not allowed.");
                return false;
            }
            
            // Check if the parameter is an existing name.
            if (CharNameTable.getInstance().getIdByName(params) > 0)
            {
                activeChar.sendMessage("This name already exist.");
                return false;
            }
            
            activeChar.sendMessage("Your name has been changed to " + params + ".");
            activeChar.setName(params);
            activeChar.broadcastUserInfo();
            
            // Party update.
            if (activeChar.isInParty())
            {
                // Delete party window for other party members
            	activeChar.getParty().broadcastToPartyMembers(activeChar, new PartySmallWindowDeleteAll());
				for (L2PcInstance member : activeChar.getParty().getPartyMembers())
                {
                    // And re-add
                    if (member != activeChar)
                    {
                        member.sendPacket(new PartySmallWindowAll(member, activeChar.getParty()));
                    }
                }
            }
            // Clan update.
            if (activeChar.getClan() != null)
            {
                activeChar.getClan().broadcastClanStatus();
            }
            // Community update.
            CommunityServerThread.getInstance().sendPacket(new WorldInfo(activeChar, null, WorldInfo.TYPE_UPDATE_PLAYER_DATA));
            RegionBBSManager.getInstance().changeCommunityBoard();
        }
        return true;
    }
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}