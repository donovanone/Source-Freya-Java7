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
package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.datatables.MapRegionTable;

/**
 *
 * A Clan War Zone
 *
 */
public class L2ClanWarZone extends L2ZoneType
{
 public L2ClanWarZone(int id)
 {
 super(id);
 }
 
 L2Skill noblesse = SkillTable.getInstance().getInfo(1323, 1);
 
 @Override
 protected void onEnter(L2Character character)
 {
       if (character instanceof L2PcInstance)
       {
    L2PcInstance activeChar = ((L2PcInstance) character);
        if(((L2PcInstance)character).getClan() != null)
        {    
     character.setInsideZone(L2Character.ZONE_CLANWAR, true);
         ((L2PcInstance)character).sendMessage("You have entered a Clan War Zone. Prepare for fight.");
 noblesse.getEffects(activeChar, activeChar);
 if(activeChar.getPvpFlag() == 0)
 activeChar.updatePvPFlag(1);
        }
 
        else
        {
        ((L2PcInstance) character).sendMessage("This is strict area for clan members ONLY. You will be teleported at the nearest town.");
 ((L2PcInstance) character).teleToLocation(MapRegionTable.TeleportWhereType.Town);
        }
 }
 }
 
 @Override
 protected void onExit(L2Character character)
 {
 character.setInsideZone(L2Character.ZONE_CLANWAR, false);
 if (character instanceof L2PcInstance)
 {
 L2PcInstance activeChar = ((L2PcInstance) character);
 activeChar.stopPvPFlag();
 }
 }
 
 @Override
 public void onDieInside(L2Character character)
 {
 }
 
 @Override
 public void onReviveInside(L2Character character)
 {
 onEnter(character);
 if (character instanceof L2PcInstance)
 {
 L2PcInstance activeChar = ((L2PcInstance) character);
 noblesse.getEffects(activeChar, activeChar);
 heal(activeChar);
 }
 }

 static void heal(L2PcInstance activeChar)
 {
 activeChar.setCurrentHp(activeChar.getMaxHp());
 activeChar.setCurrentCp(activeChar.getMaxCp());
 activeChar.setCurrentMp(activeChar.getMaxMp());
 }
 
}