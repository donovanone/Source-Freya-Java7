/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * 
 * 
 * @author Matthew
 */
public class sellbuff implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = {"sellbuffs"};
		

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if(activeChar == null)
			return false;
		
		if(activeChar.isDead() || activeChar.isAlikeDead()){
			activeChar.sendMessage("You are dead , you can't sell at the moment");
			return false;
		}
		else if (activeChar.isInOlympiadMode())
								{
									activeChar.sendMessage("You are in olympiad , you can't sell buff at the moment.");
									return false;
		}
		else if(activeChar.atEvent){
			activeChar.sendMessage("You are in event , you can't sell at the moment");
			return false;
		}
		else if(!activeChar.isInsideZone(L2PcInstance.ZONE_PEACE)){
			activeChar.sendMessage("You are not in peacefull zone , you can sell only in peacefull zones");
			return false;
		}
		else if(isInRestrictedZone(activeChar)){
			activeChar.sendMessage("You can't sell in restricted zone");
			return false;
		}
		else if(activeChar.getPvpFlag() > 0 || activeChar.isInCombat() || activeChar.getKarma() > 0){
			activeChar.sendMessage("You are in combat mode , you can't sell at the moment");
			return false;
		}
		else if(!Config.SELL_BUFF_CLASS_LIST.contains(Integer.toString(activeChar.getClassId().getId()))){
			activeChar.sendMessage("Your class can't sell buffs");
		return false;
	}
		else if(activeChar.getLevel() < Config.SELL_BUFF_MIN_LVL){
			activeChar.sendMessage("You can sell buffs on "+Config.SELL_BUFF_MIN_LVL+" level");
			return false;
		}
		// summoner classes exception, buffs allowed from 56 level.
		else if(activeChar.getClassId().getId() == 96 || activeChar.getClassId().getId() == 14 || activeChar.getClassId().getId() == 104 || activeChar.getClassId().getId() == 28){
			if(activeChar.getLevel() < 56){
				activeChar.sendMessage("You can sell buffs on 56 level");
				return false;
			}
		}
		
		activeChar.getSellBuffMsg().sendSellerResponse(activeChar);

	    return true;
	}

	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
	
	private boolean isInRestrictedZone(L2PcInstance activeChar){
		for(int i=0; i<Config.SELL_BUFF_RESTRICTED_ZONES_IDS.size(); i++){
			try{
				if(activeChar.isInsideZone((byte) Integer.parseInt(Config.SELL_BUFF_RESTRICTED_ZONES_IDS.get(i)))){
					return true;
				}
			}catch(Exception e){
				return false;
			}
			
		}
		return false;
	}

}