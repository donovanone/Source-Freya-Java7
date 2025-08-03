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
package com.l2jserver.gameserver.custom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import javolution.text.TextBuilder;
import javolution.util.FastList;

import com.l2jserver.Config;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.templates.skills.L2SkillType;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.L2DatabaseFactory;


/**
 * @author Matthew Mazan
 *
 */
public class SellBuffMsg
{	
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(SellBuffMsg.class.getName());
	
	private int _lastPage = 0;
	private int _subPage = 0;

	public void setLastPage(int page){
		_lastPage = page;
	}
	
	public int getLastPage(){
		return _lastPage;
	}
	
	public void setSubPage(int subPage){
		_subPage = subPage;
	}
	
	public int getSubPage(){
		return _subPage;
	}
	
	/**
	 * Construct response to buff buyer.
	 * @param seller
	 * @param buyer
	 * @return
	 */
	private TextBuilder buyerResponse(L2PcInstance seller, L2PcInstance buyer){
		TextBuilder tb = new TextBuilder();
		int buffsPerPage = 8; //8 is maximum
		int buffsCount = 0; //count all player buffs
		int sellBuffsCount = 0; // count buffs on actual page
		String buffFor = " player";
		
		if(_lastPage == 1 || _lastPage == 2){
			if(_lastPage == 2){buffFor = " pet"; }
			tb.append("<html><title>"+seller.getName()+"("+seller.getLevel()+") buff price for"+buffFor+": "+seller.getBuffPrice()+" Medal</title><body><br>");
			
			FastList<L2Skill> ba = this.getBuffList(seller);
			
			for(L2Skill p : ba){	
				if(Config.SELL_BUFF_SKILL_LIST.containsKey(p.getId())){
					buffsCount++;
					if(buffsCount > buffsPerPage * _subPage && buffsCount <= buffsPerPage * (_subPage + 1)){// show subpage with buffs.
						//_log.info("party skill entered:"+p.getTargetType().name());
						if(!Config.ALLOW_PARTY_BUFFS && p.getTargetType() == L2Skill.SkillTargetType.TARGET_PARTY){
							if(buyer.getParty() != null && seller.getParty() != null && buyer.getParty().equals(seller.getParty())){ //check is in party.
								sellBuffsCount++;
								createHtmlSkillField(tb, _lastPage, p.getName()+" ("+p.getLevel()+")", p.getId(), buffsCount);
							}
						}else if(!Config.ALLOW_CLAN_BUFFS && (p.getTargetType() == L2Skill.SkillTargetType.TARGET_CLAN || p.getTargetType() == L2Skill.SkillTargetType.TARGET_ALLY)){
							if((buyer.getClan() != null && seller.getClan() != null && buyer.getClanId() == seller.getClanId())||(buyer.getClan() != null && seller.getClan() != null && buyer.getClan().getAllyName() != null && seller.getClan().getAllyName() != null && buyer.getClan().getAllyName().equals(seller.getClan().getAllyName()))){  //check is in clan or ally.
								sellBuffsCount++;
								createHtmlSkillField(tb, _lastPage, p.getName()+" ("+p.getLevel()+")", p.getId(), buffsCount);
							}
						}else{
							sellBuffsCount++;
							createHtmlSkillField(tb, _lastPage, p.getName()+" ("+p.getLevel()+")", p.getId(), buffsCount);
						}
					}
				}
			}
			
			
			if(sellBuffsCount > 0){
				for(int x=(buffsCount-(_subPage*buffsPerPage)) ; x<buffsPerPage; x++){ 
					this.createHtmlEmptySkillField(tb); 
				}
			}else{
				tb.append("<center><table><tr><td width=270 align=center height="+36*buffsPerPage+"><font color=FF0000>I am sorry. I have no buffs for you now.</font></td></tr></table></center>");
			}
			
			tb.append("<br>");
			tb.append("<center><table><tr>");
			
			if(sellBuffsCount > 0){
				if(buffsCount > 1*buffsPerPage){
					this.subPageButton(tb, 0);
					this.subPageButton(tb, 1);
				}
				if(buffsCount > 2*buffsPerPage){
					this.subPageButton(tb, 2);
				}
				if(buffsCount > 3*buffsPerPage){
					this.subPageButton(tb, 3);
				}
				if(buffsCount > 4*buffsPerPage){
					this.subPageButton(tb, 4);
				}
			}
			
			tb.append("<td width=102>");
			tb.append("<button value=\"Back\" action=\"bypass -h sellbuffpage0\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\">");
			tb.append("</td></tr></table></center>");
			
			tb.append("</body></html>");
		
		}else{
			tb.append("<html><title>"+seller.getName()+"("+seller.getLevel()+") price for 1 buff: "+seller.getBuffPrice()+" Medal</title><body>");
			
			tb.append("<br><center>Choose buffs for:<br>");
			tb.append("<table><tr>");
			tb.append("<td><center><button value=\"Player\" action=\"bypass -h sellbuffpage1\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
			tb.append("<td><center><button value=\"Pet\" action=\"bypass -h sellbuffpage2\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
			tb.append("</tr></table>");
			
			tb.append("<br>");
			
			if(seller.getClassId().getId() != 96 && seller.getClassId().getId() != 14 && seller.getClassId().getId() != 104 && seller.getClassId().getId() != 28){
				if(Config.SELL_BUFFSET_ENABLED && Config.SELL_BUFF_MIN_LVL <= seller.getLevel() && Config.SELL_BUFFSET_MIN_LVL <= seller.getLevel() && (Config.SELL_BUFFSET_WARRIOR.size() > 0 || Config.SELL_BUFFSET_MAGE.size() > 0 || Config.SELL_BUFFSET_RECHARGER.size() > 0 || Config.SELL_BUFFSET_TANKER.size() > 0)){
					tb.append("<br><br><center>Schemes</center><br>");
					tb.append("<table><tr>");
					tb.append("<td><center>Player</center></td><td><center>Pet</center></td>");
					tb.append("</tr><tr>");
					tb.append("<td><center><button value=\"Fighter\" action=\"bypass -h zbuffset1\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("<td><center><button value=\"Fighter\" action=\"bypass -h zpetbuff1\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("</tr><tr>");
					tb.append("<td><center><button value=\"Mage\" action=\"bypass -h zbuffset2\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("<td><center><button value=\"Mage\" action=\"bypass -h zpetbuff2\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("</tr><tr>");
					tb.append("<td><center><button value=\"Recharger\" action=\"bypass -h zbuffset3\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("<td><center><button value=\"Recharger\" action=\"bypass -h zpetbuff3\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("</tr><tr>");
					tb.append("<td><center><button value=\"Tank\" action=\"bypass -h zbuffset4\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("<td><center><button value=\"Tank\" action=\"bypass -h zpetbuff4\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\"></center></td>");
					tb.append("</tr></table>");
				}
			}
			if(seller.getBuffPrice() >= Config.SELL_BUFF_PUNISHED_PRICE){
				tb.append("<br><br><font color=FF0000>Be careful! <br>Buff price is very high!</font>");
			}
			tb.append("</center></body></html>");
		}
		
		
		return tb;
		
	}
	
	/**
	 * Send html response to buyer.
	 * @param seller
	 * @param buyer
	 * @param error - if set true, return first page and send msg to buyer with error type.
	 */
	public void sendBuyerResponse(L2PcInstance seller, L2PcInstance buyer, boolean error){
		
		if(seller != null){
			if(seller.isSellBuff() && seller != buyer){
				TextBuilder tb = null;
				if(!error){
					tb = buyer.getSellBuffMsg().buyerResponse(seller, buyer); //buyer == this from l2pcinstance
				}else{
					this.setLastPage(0);
					this.setSubPage(0);
					tb = buyer.getSellBuffMsg().buyerResponse(seller, buyer);
				}
				NpcHtmlMessage n = new NpcHtmlMessage(0);
				n.setHtml(tb.toString());
				buyer.sendPacket(n);
			}
		}
	}
	
	public void sendSellerResponse(L2PcInstance seller){
		 TextBuilder tb = new TextBuilder(0);
		 //create sell
		 if(!seller.isSellBuff()){
			 tb.append("<html><body><center>");
			 tb.append("Hello , by completing this form<br>you will be able to sell buffs.");
			 tb.append("<br>Players will be able,<br>targeting you to take your buff services<br>");
			 tb.append("<br>You will be rewarded with Medals<br>for each buff a player takes.");
			 tb.append("<br><center>Now choose the price:</center><br>");
			 tb.append("<center><edit var=\"pri\" width=120 height=15></center><br><br>");
			 tb.append("<center><button value=\"Confirm\" action=\"bypass -h actr $pri\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\">");
			 tb.append("</center></body></html>");
		 //abort sell
		 }else{
			 tb.append("<html><body><center>");
			 tb.append("<br>Actual buff price: "+seller.getBuffPrice()+"<br>");
			 tb.append("Would you abort selling buffs?<br><br>");
			 tb.append("<center><button value=\"Abort\" action=\"bypass -h pctra\" width=100 height=23 back=\"L2UI_ch3.bigbutton_down\" fore=\"L2UI_ch3.bigbutton\">");
			 tb.append("</center></body></html>");
		 }
		 NpcHtmlMessage n = new NpcHtmlMessage(0);
		 n.setHtml(tb.toString());
		 seller.sendPacket(n);
		   
	}
	
	private void createHtmlSkillField(TextBuilder tb, int buffPage, String skillName, int skillId, int buffRowNumber){
		//buffPage mean buff for player = 1, or buff for pet = 2.
		String buffType = "";
		if(buffPage == 2){
			buffType = "pet";
		}
		
		String col1 = "";
		String col2 = "LEVEL";
		
		if(buffRowNumber % 2 == 0){
			col1 =  " bgcolor=000000";
			col2 = "FFFFFF";
		}
		
		int desc_skillId = skillId;
		
		// if summoners skills (exception)
		if(skillId == 4699 || skillId == 4700){
			skillId = 1331;
		}
		if(skillId == 4702 || skillId == 4703){
			skillId = 1332;
		}
		
		String tmp_skillId = ""+skillId;
		if(skillId < 1000){
			tmp_skillId = "0"+skillId;
		}

		
		
		tb.append("<table"+col1+">");
		tb.append("<tr>");
		tb.append("<td width=40><button action=\"bypass -h "+buffType+"buff"+ desc_skillId+"\" width=32 height=32 back=\"icon.skill"+tmp_skillId+"\" fore=\"icon.skill"+tmp_skillId+"\"></td>");
		tb.append("<td width=240>");
		tb.append("<table>");
		tb.append("<tr><td width=240 align=left><font color=\""+col2+"\">"+skillName+"</td></tr>");
		tb.append("<tr><td width=240 align=left><font color=\"ae9977\">"+this.getSkillDescription(desc_skillId)+"</font></td></tr>");
		tb.append("</table>");
		tb.append("</td>");
		tb.append("</tr>");
		tb.append("</table>");
	}
	
	private void subPageButton(TextBuilder tb, int page){
		int btnValue = page+1;
		tb.append("<td width=34>");
		if(_subPage == page){
			tb.append("<button value=\""+btnValue+"\" action=\"bypass -h sellbuffpage"+_lastPage+"sub"+page+"\" width=32 height=23 back=\"L2UI_ch3.bigbutton_dow\" fore=\"L2UI_ch3.bigbutto\">"); //wrong button bg for black bg.
		}else{
			tb.append("<button value=\""+btnValue+"\" action=\"bypass -h sellbuffpage"+_lastPage+"sub"+page+"\" width=32 height=23 back=\"\" fore=\"\">");
		}
		tb.append("</td>");
	}
	
	private void createHtmlEmptySkillField(TextBuilder tb){			
		tb.append("<table>");
		tb.append("<tr>");
		tb.append("<td height=36></td>");
		tb.append("</tr>");
		tb.append("</table>");
	}
	
	private String getSkillDescription(int skillId){
		String desc = "&nbsp;";
			
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("Select buffId, description from sellbuff_describe where buffId = ?");
			statement.setInt(1, skillId);
			ResultSet rs = statement.executeQuery();

			while(rs.next())
			{
				desc = rs.getString("description");
			}

			rs.close();
			statement.close();
			statement = null;
			rs = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			L2DatabaseFactory.close(con);
			con = null;
		}
		return desc;
	}
	
	public FastList<L2Skill> getBuffList(L2PcInstance seller){
		L2Skill[] skills = seller.getAllSkills();
		FastList<L2Skill> ba = new FastList<L2Skill>();
		
		for(L2Skill s : skills){
			if(s == null)
				continue;
			// if not summoner classes and skill type == buff
			if((seller.getClassId().getId() != 96 && seller.getClassId().getId() != 14 && seller.getClassId().getId() != 104 && seller.getClassId().getId() != 28)&&((s.getSkillType() == L2SkillType.BUFF || s.getSkillType() == L2SkillType.HEAL_PERCENT || s.getSkillType() == L2SkillType.COMBATPOINTHEAL) && s.isActive())){
				if(Config.SELL_BUFF_FILTER_ENABLED){
					if(seller.getClassId().getId() == 115 || seller.getClassId().getId() == 51){ //manual useless buffs check for dominator and overlord class 
						if(s.getId() != 1002 && s.getId() != 1006 && s.getId() != 1007 && s.getId() != 1009){
							ba.add(s);
						}
					}else if(seller.getClassId().getId() == 116 || seller.getClassId().getId() == 52){ //doomcryer and warcryer
						if(s.getId() != 1003 && s.getId() != 1005){
							ba.add(s);
						}
					}else if(seller.getClassId().getId() == 97 || seller.getClassId().getId() == 16){ //cardinal and bishop
						if(s.getId() == 1353 || s.getId() == 1307 || s.getId() == 1311){
						ba.add(s);
					}
					}else{
						ba.add(s);
					}
				}else{
					ba.add(s);
				}
			}else{
				L2Skill skill = null;
				if(s.getId() == 1332){
					skill = SkillTable.getInstance().getInfo(4702, getSkillLevel(seller, 1332));
					ba.add(skill);
					skill = SkillTable.getInstance().getInfo(4703, getSkillLevel(seller, 1332));
					ba.add(skill);
				}
				if(s.getId() == 1331){
					skill = SkillTable.getInstance().getInfo(4699, getSkillLevel(seller, 1331));
					ba.add(skill);
					skill = SkillTable.getInstance().getInfo(4700, getSkillLevel(seller, 1331));
					ba.add(skill);				
				}
			}
		}
		return ba;
	}
	
	public FastList<L2Skill> getBuffsetList(L2PcInstance seller, FastList<String> buffset){
		
		L2Skill[] skills = seller.getAllSkills();
		FastList<L2Skill> ba = new FastList<L2Skill>();
		
		for(L2Skill s : skills){
			if(s == null)
				continue;
			
			if(buffset.contains(Integer.toString(s.getId()))){
				// if not summoner classes and skill type == buff
				if((seller.getClassId().getId() != 96 && seller.getClassId().getId() != 14 && seller.getClassId().getId() != 104 && seller.getClassId().getId() != 28)&&((s.getSkillType() == L2SkillType.BUFF || s.getSkillType() == L2SkillType.HEAL_PERCENT || s.getSkillType() == L2SkillType.COMBATPOINTHEAL) && s.isActive())){
					if(Config.SELL_BUFF_FILTER_ENABLED){
						if(seller.getClassId().getId() == 115 || seller.getClassId().getId() == 51){ //manual useless buffs check for dominator and overlord class 
							if(s.getId() != 1002 && s.getId() != 1006 && s.getId() != 1007 && s.getId() != 1009){
								ba.add(s);
							}
						}else if(seller.getClassId().getId() == 116 || seller.getClassId().getId() == 52){ //doomcryer and warcryer
							if(s.getId() != 1003 && s.getId() != 1005){
								ba.add(s);
							}
						}else if(seller.getClassId().getId() == 97 || seller.getClassId().getId() == 16){ //cardinal and bishop
							if(s.getId() == 1353 || s.getId() == 1307 || s.getId() == 1311){
								ba.add(s);
							}
						}else{
							ba.add(s);
						}
					}else{
						ba.add(s);
					}
				}else{
					if(s.getId() == 4699){
						ba.add(s);
					}
					if(s.getId() == 4700){
						ba.add(s);				
					}
					if(s.getId() == 4702){
						ba.add(s);
					}
					if(s.getId() == 4703){
						ba.add(s);
					}
				}
			}
		}
		return ba;
	}
	
	public void setPage(L2PcInstance seller, L2PcInstance buyer, int page, int subPage){
		if(page == 1){
			buyer.getSellBuffMsg().setLastPage(1);
			buyer.getSellBuffMsg().setSubPage(subPage);
		}else if(page == 2){
			buyer.getSellBuffMsg().setLastPage(2);
			buyer.getSellBuffMsg().setSubPage(subPage);
		}else{
			buyer.getSellBuffMsg().setLastPage(0);
			buyer.getSellBuffMsg().setSubPage(0);
		}
		buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);	
	}
	
	public void buffBuyer(L2PcInstance seller, L2PcInstance buyer, int buffId){
		if(buyer.getDistanceSq(seller) > 5000){
			buyer.sendMessage("You must be closer the buffer!");
			//buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.setTarget(null);
		}else if(!seller.isSellBuff()){
			buyer.sendMessage("You cant buy buffs now!");
			return;
		}else if(!this.enoughSpiritOreForBuff(seller, buffId)){
			if(!seller.isOffline()){
				seller.sendMessage("You can't sell buff, you haven't Spirit Ore for use skill!");
			}
			buyer.sendMessage("You can't buff now. Buff seller has no item for use skill!");
			buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false); 
		}else{
			int buffprice = seller.getBuffPrice();
			
			if(buyer.getInventory().getItemByItemId(57) == null || buyer.getInventory().getItemByItemId(57).getCount() < buffprice){
				buyer.sendMessage("Not enought Medal!");
			}else{
				try{
					//get buff data
					L2Skill skill = SkillTable.getInstance().getInfo(buffId, getSkillLevel(seller, buffId));
					
					//single skill mp consume
					if(Config.SELL_BUFF_SKILL_MP_ENABLED){
						if(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER >= (skill.getMpConsume()+1)){
							seller.setCurrentMp(Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER - skill.getMpConsume())/Config.SELL_BUFF_SKILL_MP_MULTIPLIER);
						}else{
							buyer.sendMessage("Buffer has no Mana Points ("+Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+" / "+Math.round(seller.getMaxMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+")");
							buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);
							return;
						}
					}
				//add adena for seller
					seller.addItem("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice, seller, true);
					
					//remove adena from buyer
					buyer.destroyItemByItemId("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice, buyer, false);
					
					//destroy Spirit Ore:
					destroySpiritOre(seller, buffId);
					
					//give buffs for buyer
					skill.getEffects(buyer, buyer);

					buyer.sendMessage("You buyed: "+skill.getName()+" for "+seller.getBuffPrice()+"Medal");	
					buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);

				}catch(Exception e){
						e.printStackTrace();
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
				}
			}
		}
	}

	public void buffBuyerPet(L2PcInstance seller, L2PcInstance buyer, int buffId){
		//checking pet/summon if error return to first page;
		if(buyer.getPet() == null || buyer.getPet().isDead()){
			//buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.sendMessage("You must summon your pet first!");
			buyer.setTarget(null);
		}else if(buyer.getDistanceSq(seller) > 10000){
			//buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.sendMessage("Your pet must be closer the buffer!");	
			buyer.setTarget(null);
		}else if(!seller.isSellBuff()){
			buyer.sendMessage("You cant buy buffs now!");
			return;
		}else if(!this.enoughSpiritOreForBuff(seller, buffId)){
			if(!seller.isOnline()){
				seller.sendMessage("You can't sell buff, you haven't Spirit Ore for use skill!");
			}
			buyer.sendMessage("You can't buff now. Buff seller has no item for use skill!");
			buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false); 
		}else{
			int buffprice = seller.getBuffPrice();
			
			if(buyer.getInventory().getItemByItemId(57) == null || buyer.getInventory().getItemByItemId(57).getCount() < buffprice){
				buyer.sendMessage("Not enought Medal!");
			}else{
				
				try{
					//get buff data
					L2Skill skill = SkillTable.getInstance().getInfo(buffId, getSkillLevel(seller, buffId));
					
					// single skill mp consume
					if(Config.SELL_BUFF_SKILL_MP_ENABLED){
						if(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER >= (skill.getMpConsume()+1)){
							seller.setCurrentMp(Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER - skill.getMpConsume())/Config.SELL_BUFF_SKILL_MP_MULTIPLIER);
						}else{
							buyer.sendMessage("Buffer has no Mana Points ("+Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+" / "+Math.round(seller.getMaxMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+")");
							buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
							return;
						}
					}
					
					//buff seller
					seller.addItem("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice, seller, true);
					
					//buff buyer
					buyer.destroyItemByItemId("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice, buyer, false);
	
					//destroy Spirit Ore:
					destroySpiritOre(seller, buffId);
	
					//give buffs for pet
					skill.getEffects(buyer.getPet(), buyer.getPet());
						
						
					buyer.sendMessage("You buyed: "+skill.getName()+" for "+seller.getBuffPrice()+" Medal, for your pet.");	
					buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);
	
				
				
				}catch(Exception e){
						e.printStackTrace();
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
				}
			}
		}
	}
	
	public void buffsetBuyer(L2PcInstance seller, L2PcInstance buyer, int setId){	
		String buffSetName = " ";
		FastList<L2Skill> ba = new FastList<L2Skill>();
		//----------------------------------------------------------------------------------------------
		if(setId == 1){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_WARRIOR);
			buffSetName = " Warrior ";
		}else if(setId == 2){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_MAGE);
			buffSetName = " Mage ";
		}else if(setId == 3){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_RECHARGER);
			buffSetName = " Recharger ";
		}else if(setId == 4){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_TANKER);
			buffSetName = " Tanker ";
		}
	
		int buffsetSize = ba.size();
	
		if(buyer.getDistanceSq(seller) > 5000){
			buyer.sendMessage("You must be closer the buffer!");
			//buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.setTarget(null);
		}else if(!seller.isSellBuff()){
			buyer.sendMessage("You cant buy buffs now!");
			return;
		}else{
			int buffprice = seller.getBuffPrice();
			if(seller.getBuffPrice() == 0){
				buyer.sendMessage("This buffset is empty!");
				buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			}else if(buyer.getInventory().getItemByItemId(57) == null || buyer.getInventory().getItemByItemId(57).getCount() < buffprice*buffsetSize){
				buyer.sendMessage("Not enought Medal!");
		}else{
				try{
					boolean mpOK = true;
					boolean soOK = true;
					int mpConsumeSum = 0;
					int soConsumeSum = 0; //Spirit Ore sum.
					
					if(Config.SELL_BUFF_SKILL_MP_ENABLED || Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
						// calculate all skills set mana use:
						for(L2Skill p : ba){
							L2Skill skill = SkillTable.getInstance().getInfo(p.getId(), getSkillLevel(seller, p.getId()));
							if(Config.SELL_BUFF_SKILL_MP_ENABLED){
								mpConsumeSum += skill.getMpConsume();
							}
							if(Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
								soConsumeSum += getSkillConsumeSOCount(p.getId(), p.getLevel());
							}
						}
						// set ok == false if buffset mp cost greater than seller current mp.
						if(mpConsumeSum > 0 && (mpConsumeSum + 1) > seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER){
							mpOK = false;
						}	
						// set ok == false if buffset require more SO count than count SO in inventory.
						if(soConsumeSum > 0 && (seller.getInventory().getItemByItemId(3031) == null || soConsumeSum > seller.getInventory().getItemByItemId(3031).getCount())){
							soOK = false;
						}	
					}

					if(mpOK && soOK){
						for(L2Skill p : ba){
							L2Skill skill = SkillTable.getInstance().getInfo(p.getId(), getSkillLevel(seller, p.getId()));
							
							//give buffs
							skill.getEffects(buyer, buyer);
		
							//destroy Spirit Ore:
							destroySpiritOre(seller, skill.getId());
							
							buyer.sendMessage("You buyed: "+skill.getName());	
							buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);
							if(Config.SELL_BUFF_SKILL_MP_ENABLED){
								seller.setCurrentMp(Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER - skill.getMpConsume())/Config.SELL_BUFF_SKILL_MP_MULTIPLIER);
							}
						}
					
						//buff seller
						seller.addItem("buff sell", Config.STORE_SELL_CURRENCY_BUFF, buffprice*buffsetSize, seller, true);
						
						//buff buyer
						buyer.destroyItemByItemId("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice*buffsetSize, buyer, false);
						
						buyer.sendMessage("You bought"+buffSetName+"buffs for: "+buffprice*buffsetSize+"Medal");
						
					}else{
						if(!mpOK){
							buyer.sendMessage("Buffer has no Mana Points for this set ("+Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+" / "+Math.round(seller.getMaxMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+")");
						}
						if(!soOK){
							buyer.sendMessage("You can't buff now. Buff seller has no item for use skill!");
							seller.sendMessage("You can't sell buff, you haven't Spirit Ore for use skill!");
						}
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
					}
				}catch(Exception e){
						e.printStackTrace();
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
				}
			}
		}
	}
	
	public void buffsetBuyerPet(L2PcInstance seller, L2PcInstance buyer, int setId){			
		String buffSetName = " ";
		FastList<L2Skill> ba = new FastList<L2Skill>();
		//----------------------------------------------------------------------------------------------
		if(setId == 1){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_WARRIOR);
			buffSetName = " Warrior ";
		}else if(setId == 2){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_MAGE);
			buffSetName = " Mage ";
		}else if(setId == 3){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_RECHARGER);
			buffSetName = " Recharger ";
		}else if(setId == 4){
			ba = seller.getSellBuffMsg().getBuffsetList(seller, Config.SELL_BUFFSET_TANKER);
			buffSetName = " Tanker ";
		}
	
		int buffsetSize = ba.size();
	
		//checking pet/summon if error return to first page;
		if(buyer.getPet() == null || buyer.getPet().isDead()){
			buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.sendMessage("You must summon your pet first!");
		}else if(buyer.getDistanceSq(seller) > 10000){
			//buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			buyer.sendMessage("Your pet must be closer the buffer!");	
			buyer.setTarget(null);
		}else if(!seller.isSellBuff()){
			buyer.sendMessage("You cant buy buffs now!");
			return;
		}else{
			int buffprice = seller.getBuffPrice();
			if(seller.getBuffPrice() == 0){
				buyer.sendMessage("This buffset is empty!");
				buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
			}else if(buyer.getInventory().getItemByItemId(57) == null || buyer.getInventory().getItemByItemId(57).getCount() < buffprice){
				buyer.sendMessage("Not enought Medal!");
			}else{
				
				try{
					boolean mpOK = true;
					boolean soOK = true;
					int mpConsumeSum = 0;
					int soConsumeSum = 0; //Spirit Ore sum.
					
					if(Config.SELL_BUFF_SKILL_MP_ENABLED || Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
						// calculate all skills set mana use:
						for(L2Skill p : ba){
							L2Skill skill = SkillTable.getInstance().getInfo(p.getId(), getSkillLevel(seller, p.getId()));
							if(Config.SELL_BUFF_SKILL_MP_ENABLED){
								mpConsumeSum += skill.getMpConsume();
							}
							if(Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
								soConsumeSum += getSkillConsumeSOCount(p.getId(), p.getLevel());
							}
						}
						// set ok == false if buffset mp cost greater than seller current mp.
						if(mpConsumeSum > 0 && (mpConsumeSum + 1) > seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER){
							mpOK = false;
						}	
						// set ok == false if buffset require more SO count than count SO in inventory.
						if(soConsumeSum > 0 && (seller.getInventory().getItemByItemId(3031) == null || soConsumeSum > seller.getInventory().getItemByItemId(3031).getCount())){
							soOK = false;
						}	
					}

					if(mpOK && soOK){
						for(L2Skill p : ba){
							L2Skill skill = SkillTable.getInstance().getInfo(p.getId(), getSkillLevel(seller, p.getId()));
							
							//give buffs
							skill.getEffects(buyer.getPet(), buyer.getPet());
		
							//destroy Spirit Ore:
							destroySpiritOre(seller, skill.getId());
							
							buyer.sendMessage("You buyed: "+skill.getName());	
							buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, false);
							if(Config.SELL_BUFF_SKILL_MP_ENABLED){
								seller.setCurrentMp(Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER - skill.getMpConsume())/Config.SELL_BUFF_SKILL_MP_MULTIPLIER);
							}
						}
					
						//buff seller
						seller.addItem("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice*buffsetSize, seller, true);
						
						//buff buyer
						buyer.destroyItemByItemId("buff sell", Config.STORE_SELL_CURRENCY_BUFF , buffprice*buffsetSize, buyer, false);
						
						buyer.sendMessage("You bought"+buffSetName+"buffs for: "+buffprice*buffsetSize+" Medal");
					}else{
						if(!mpOK){
							buyer.sendMessage("Buffer has no Mana Points for this set ("+Math.round(seller.getCurrentMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+" / "+Math.round(seller.getMaxMp()*Config.SELL_BUFF_SKILL_MP_MULTIPLIER)+")");
						}
						if(!soOK){
							buyer.sendMessage("You can't buff now. Buff seller has no item for use skill!");
							seller.sendMessage("You can't sell buff, you haven't Spirit Ore for use skill!");
						}
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
					}
				}catch(Exception e){
						e.printStackTrace();
						buyer.getSellBuffMsg().sendBuyerResponse(seller, buyer, true);
				}
			}
		}
	}
	
	public void startBuffStore(L2PcInstance seller, int price, boolean saveSellerData){
		if(price <= 0){
			seller.sendMessage("Too low price. Min is 1 Medal.");
			return;
		}
						
		if(price > 2000000000){
			seller.sendMessage("Too big price. Max is 2 000 000 000 Medal.");
			return;
		}
		
		if(saveSellerData){
			saveSellerData(seller);
		}
		
		seller.setBuffPrice(price);
		seller.sitDown();
		seller.setSellBuff(true);
		
		if(seller.getClassId().getId() < 90){ //if 2nd profession or lower.
			seller.getAppearance().setTitleColor(0xFFDD); //dark green (yellow now)
		}else{
			seller.getAppearance().setTitleColor(0xFF00); //light green
		}
		seller.setTitle(getClassName(seller.getClassId().getId())); //set title like class name
		seller.getAppearance().setNameColor(0x1111);				   
		seller.broadcastUserInfo();
		seller.broadcastTitleInfo();
	    
	}
	
	public void stopBuffStore(L2PcInstance seller){

		restoreSellerData(seller);
		
		seller.broadcastUserInfo();
		seller.broadcastTitleInfo();
		
		seller.setSellBuff(false);
		seller.standUp();
		
	}
	
	private void saveSellerData(L2PcInstance seller){
		Connection con = null;	
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement;
			statement = con.prepareStatement("CALL sellbuff_saveSellerData(?,?,?,?)");
			statement.setInt(1, seller.getObjectId());
			statement.setString(2, seller.getTitle());
			statement.setInt(3, seller.getAppearance().getTitleColor());
			statement.setInt(4, seller.getAppearance().getNameColor());
			statement.execute();
			statement.close();
			statement = null;
		}
		catch(Exception e)
		{
			if(Config.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
		}
		finally
		{
			L2DatabaseFactory.close(con);
			con = null;
		}
	}
	
	public void restoreSellerData(L2PcInstance seller){
		//int defaultNickColor = 16777215; // white
		//int defaultTitleColor = 16777079; // light blue
		
		Connection con = null;	
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement;
			statement = con.prepareStatement("CALL sellbuff_restoreSellerData(?)");
			statement.setInt(1, seller.getObjectId());
			
			ResultSet res = statement.executeQuery();

			while(res.next())
			{
				seller.setTitle(res.getString("lastTitle"));
				seller.getAppearance().setTitleColor(Integer.parseInt(res.getString("lastTitleColor")));
				seller.getAppearance().setNameColor(Integer.parseInt(res.getString("lastNameColor")));
			}
			res.close();
			res = null;
			statement.close();
			statement = null;
		}
		catch(Exception e)
		{
			if(Config.ENABLE_ALL_EXCEPTIONS)
				e.printStackTrace();
		}
		finally
		{
			L2DatabaseFactory.close(con);
			con = null;
		}
	}

	/**
	 * Returns skill consume count (Spirit Ore count).
	 * @param skillId
	 * @param skillLvl
	 * @return
	 */
	private int getSkillConsumeSOCount(int skillId, int skillLvl){
		// Buffs what consume items like Spirit Ore
		// {skillId, skillLvl, itemConsumeId, itemConsumeCount}
		int[][] skillsData = {
			{1388,1,3031,1},
			{1388,2,3031,2},
			{1388,3,3031,3},
			{1389,1,3031,1},
			{1389,2,3031,2},
			{1389,3,3031,3},
			{1356,1,3031,10},
			{1397,1,3031,1},
			{1397,2,3031,2},
			{1397,3,3031,3},
			{1355,1,3031,10},
			{1357,1,3031,10},
			{1416,1,3031,20},
			{1414,1,3031,40},
			{1391,1,3031,4},
			{1391,2,3031,8},
			{1391,3,3031,12},
			{1390,1,3031,4},
			{1390,2,3031,8},
			{1390,3,3031,12},
			{1363,1,3031,40},
			{1413,1,3031,40},
			{1323,1,3031,5}
		};
		
		for(int i=0 ; i<skillsData.length ; i++){
			if(skillsData[i][0] == skillId && skillsData[i][1] == skillLvl){
				return skillsData[i][3];
			}
		}
		return 0;
	}
	
	private boolean enoughSpiritOreForBuff(L2PcInstance seller, int skillId){
		if(Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
			if(getSkillConsumeSOCount(skillId, seller.getSkillLevel(skillId)) > 0){
				// 3031 == Spirit Ore ID
				if(seller.getInventory().getItemByItemId(3031) == null || seller.getInventory().getItemByItemId(3031).getCount() < getSkillConsumeSOCount(skillId, seller.getSkillLevel(skillId))){
					return false;
				}
			}
		}
		return true;
	}
	
	private void destroySpiritOre(L2PcInstance seller, int skillId){	
		if(Config.SELL_BUFF_SKILL_ITEM_CONSUME_ENABLED){
			if(seller.getInventory().getItemByItemId(3031) != null && seller.getInventory().getItemByItemId(3031).getCount() >= getSkillConsumeSOCount(skillId, seller.getSkillLevel(skillId))){
				seller.destroyItemByItemId("buff sell", 3031, getSkillConsumeSOCount(skillId, seller.getSkillLevel(skillId)), seller, false);
			}
		}
	}
	
	private String getClassName(int classId){
		switch (classId) { 
			case 14:  return "Warlock";
			case 96:  return "ArcanaLord";
			case 16:  return "Bishop";
			case 97:  return "Cardinal";
			case 17:  return "Prophet";
			case 98:  return "Hierophant";
			case 21:  return "SwordSinger";
			case 100: return "SwordMuse";
			case 28:  return "Elem.Summoner";
			case 104: return "Elem.Master";
			case 29:  return "Oracle";
			case 30:  return "Elder";
			case 105: return "EvaSaint";
			case 34:  return "BladeDancer";
			case 107: return "SpectralDancer";
			case 42:  return "ShilenOracle";
			case 43:  return "ShilenElder";
			case 112: return "ShilenSaint";
			case 50:  return "Shaman";
			case 51:  return "Overlord";
			case 115: return "Dominator";
			case 52:  return "Warcryer";
			case 116: return "Doomcryer";

			default:  return "Buff Sell";
		 }
	}
	
	/**
	 * Special summoners skill level or normal skill level.
	 * @param seller
	 * @param skillId
	 * @return
	 */
	private int getSkillLevel(L2PcInstance seller, int skillId){
	
		if(seller.getClassId().getId() != 96 && seller.getClassId().getId() != 14 && seller.getClassId().getId() != 104 && seller.getClassId().getId() != 28){
			return seller.getSkillLevel(skillId);
		}
		
		// summon lvl != summon buff lvl, example: feline queen skill lvl != feline queen buff lvl.
		// skill levels by current db: min: 5, max: 8.
		if(seller.getLevel() >= 56 && seller.getLevel() <= 57){
			return 5;
	}
	if(seller.getLevel() >= 58 && seller.getLevel() <= 67){
			return 6;
		}
		if(seller.getLevel() >= 68 && seller.getLevel() <= 73){
			return 7;
		}
		if(seller.getLevel() >= 74){
			return 8;
		}
		return 1; 
	}
	
}