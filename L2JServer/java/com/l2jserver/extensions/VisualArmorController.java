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
package com.l2jserver.extensions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.templates.item.L2Armor;
import com.l2jserver.gameserver.templates.item.L2ArmorType;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.templates.item.L2Weapon;
import com.l2jserver.gameserver.templates.item.L2WeaponType;

/**
 * @author giorgakis
 *
 */
public class VisualArmorController
{
   //As of freya there are 19 weapon types.
   public static final int totalWeaponTypes = 19;
   
   //As of freya there are 6 armor types.
   public static final int totalArmorTypes = 6;
   
   public static boolean[][] weaponMapping = new boolean[totalWeaponTypes][totalWeaponTypes];
   public static boolean[][] armorMapping = new boolean[totalArmorTypes][totalArmorTypes];
   
   public static void migrate()
   {
       System.out.println("[VisualArmor]:Migrating the database.");
       Connection con = null;
       try
       {
           con = L2DatabaseFactory.getInstance().getConnection();
           PreparedStatement statement = con.prepareStatement(VisualArmorModel.CREATE);
           statement.execute();
           statement.close();
       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }
       finally
       {
           try { con.close(); } catch (Exception e) {}
       }
   }
   
   public static void load()
   {
       migrate();
       generateMappings();
   }
   
   /**
    * All same type armors and same type weapons can get visual. All different types
    * cannot get visual unless it is stated in here.
    */
   public static void generateMappings()
   {
       for(int i =0; i< weaponMapping.length; i++)
           for(int j = 0; j< weaponMapping.length; j++)
               weaponMapping[i][j]=false;
      
       for(int i =0; i< armorMapping.length; i++)
           for(int j = 0; j< armorMapping.length; j++)
               armorMapping[i][j]=false;
      
       callRules();
      
   }
   
   public static void callRules()
   {
       //Example: a Virtual sword can mount an Equipped blunt.
       weaponMapping[L2WeaponType.SWORD.ordinal()][L2WeaponType.BLUNT.ordinal()] = true;
      
       //Example: a Virtual blunt can mount an Equipped sword.
       weaponMapping[L2WeaponType.BLUNT.ordinal()][L2WeaponType.SWORD.ordinal()] = true;
      
       weaponMapping[L2WeaponType.BIGSWORD.ordinal()][L2WeaponType.BIGBLUNT.ordinal()] = true;
       weaponMapping[L2WeaponType.BIGBLUNT.ordinal()][L2WeaponType.BIGSWORD.ordinal()] = true;
      
       armorMapping[L2ArmorType.SIGIL.ordinal()][L2ArmorType.SHIELD.ordinal()] = true;
       armorMapping[L2ArmorType.SHIELD.ordinal()][L2ArmorType.SIGIL.ordinal()] = true;
      
       //armorMapping[L2ArmorType.HEAVY.ordinal()][L2ArmorType.LIGHT.ordinal()] = true;
       //armorMapping[L2ArmorType.HEAVY.ordinal()][L2ArmorType.MAGIC.ordinal()] = true;
      
       //armorMapping[L2ArmorType.LIGHT.ordinal()][L2ArmorType.HEAVY.ordinal()] = true;
       //armorMapping[L2ArmorType.LIGHT.ordinal()][L2ArmorType.MAGIC.ordinal()] = true;
      
       //armorMapping[L2ArmorType.MAGIC.ordinal()][L2ArmorType.LIGHT.ordinal()] = true;
       //armorMapping[L2ArmorType.MAGIC.ordinal()][L2ArmorType.HEAVY.ordinal()] = true;
   }
   
   /**
    * Checks if the weapon is the same type. If that is true then return
    * the matching virtual id. Else check the mapping tables if any
    * rule states that the two different weapon types should be matched.
    * @param virtual
    * @param equiped
    * @param matchId
    * @param noMatchId
    * @return
    */
   public static int weaponMatching(L2WeaponType virtual, L2WeaponType equiped, int matchId, int noMatchId)
   {
       if(virtual == equiped)
           return matchId;

       if(weaponMapping[virtual.ordinal()][equiped.ordinal()] == true)
       {
           return matchId;
       }
      
       return noMatchId;
   }
   
   /**
    * Checks if the armor is the same type. If that is true then return
    * the matching virtual id. Else check the mapping tables if any
    * rule states that the two different armor types should be matched.
    * @param virtual
    * @param equiped
    * @param matchId
    * @param noMatchId
    * @return
    */
   public static int armorMatching(L2ArmorType virtual, L2ArmorType equiped, int matchId , int noMatchId)
   {
       if(virtual == equiped)
           return matchId;
      
       if(armorMapping[virtual.ordinal()][equiped.ordinal()] == true)
           return matchId;
      
       return noMatchId;
   }
   
   
   
   public static void setVirtualRhand(L2PcInstance actor)
   {
       actor.visualArmor.weaponRHANDId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
   }
   
   public static void setVirtualLhand(L2PcInstance actor)
   {
       actor.visualArmor.weaponLHANDId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);
   }
   
   public static void setVirtualGloves(L2PcInstance actor)
   {
       actor.visualArmor.glovesTextureId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES);
   }
   
   public static void setVirtualBody(L2PcInstance actor)
   {   
       actor.visualArmor.armorTextureId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
   }  
   
   public static void setVirtualPants(L2PcInstance actor)
   {
       int chestId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
       int pantsId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS);
      
       if(chestId != 0 && pantsId==0)
           actor.visualArmor.pantsTextureId = chestId;
       else
           actor.visualArmor.pantsTextureId = pantsId;
   }
   
   public static void setVirtualBoots(L2PcInstance actor)
   {   
       actor.visualArmor.bootsTextureId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET);
   }
   
   //TODO: Merge the armor getters in one function.
   public static int getVirtualGloves(L2PcInstance actor)
   {
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES);
       if(equipedItem == null)
           return 0;
       //ClassCastException wont happen unless some jackass changes the values from the database.
       L2Armor equipedGloves = (L2Armor)actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_GLOVES).getItem();
       int equipedGlovesId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_GLOVES);
      
       int glovesTextureId = actor.visualArmor.glovesTextureId;
       L2Armor virtualGloves = (L2Armor)ItemTable.getInstance().getTemplate(glovesTextureId);
      
       if(glovesTextureId != 0)
           return armorMatching(virtualGloves.getItemType(), equipedGloves.getItemType(),glovesTextureId, equipedGlovesId);
       else
           return equipedGlovesId;
   }
   
   public static int getVirtualBody(L2PcInstance actor)
   {
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
       if(equipedItem == null)
           return 0;
       //ClassCastException wont happen unless some jackass changes the values from the database.
       L2Armor equipedChest = (L2Armor)actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST).getItem();
       int equipedChestId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CHEST);
      
       int chestTextureId = actor.visualArmor.armorTextureId;
       L2Armor virtualChest = (L2Armor)ItemTable.getInstance().getTemplate(chestTextureId);
      
       if(chestTextureId != 0)
           return armorMatching(virtualChest.getItemType(), equipedChest.getItemType(),chestTextureId, equipedChestId);
       else
           return equipedChestId;
   }
   
   /**
    * This is a brain fu**er handling the pants since they are
    * also part of a fullbody armor.
    * @param actor
    * @return
    */
   public static int getVirtualPants(L2PcInstance actor)
   {
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LEGS);
      
       //Here comes the tricky part. If pants are null, then check for a fullbody armor.
       if(equipedItem == null)
           equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
       if(equipedItem == null)
           return 0;
          
       int pantsTextureId = actor.visualArmor.pantsTextureId;
      
       L2Armor equipedPants = (L2Armor) equipedItem.getItem();
      
       if(equipedPants.getBodyPart() != L2Item.SLOT_FULL_ARMOR && equipedPants.getBodyPart() != L2Item.SLOT_LEGS)
           return 0;
       int equipedPantsId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LEGS);
      
      
       L2Armor virtualPants = (L2Armor)ItemTable.getInstance().getTemplate(pantsTextureId);
      
       if(pantsTextureId != 0)
           return armorMatching(virtualPants.getItemType(), equipedPants.getItemType(),pantsTextureId, equipedPantsId);
       else
           return equipedPantsId;
   }
   
   public static int getVirtualBoots(L2PcInstance actor)
   {
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET);
       if(equipedItem == null)
           return 0;
       //ClassCastException wont happen unless some jackass changes the values from the database.
       L2Armor equipedBoots = (L2Armor)actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FEET).getItem();
       int equipedBootsId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_FEET);
      
       int bootsTextureId = actor.visualArmor.bootsTextureId;
       L2Armor virtualGloves = (L2Armor)ItemTable.getInstance().getTemplate(bootsTextureId);
      
       if(bootsTextureId != 0)
           return armorMatching(virtualGloves.getItemType(), equipedBoots.getItemType(),bootsTextureId, equipedBootsId);
       else
           return equipedBootsId;
   }
   
   public static int getLHAND(L2PcInstance actor)
   {
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LHAND);
       int equipedItemId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_LHAND);
      
       int weaponLHANDId = actor.visualArmor.weaponLHANDId;
       L2Item virtualItem = ItemTable.getInstance().getTemplate(weaponLHANDId);
      
       if(equipedItem == null || weaponLHANDId == 0)
           return equipedItemId;
      
       //Only check same weapon types. Virtual replacement should not happen between armor/weapons.
       if(equipedItem.getItem() instanceof L2Weapon && virtualItem instanceof L2Weapon)
       {
           L2Weapon weapon = (L2Weapon) equipedItem.getItem();
           L2Weapon virtualweapon = (L2Weapon)virtualItem;
          
           return weaponMatching(virtualweapon.getItemType(), weapon.getItemType(), weaponLHANDId, equipedItemId);
       }
       else if(equipedItem.getItem() instanceof L2Armor && virtualItem instanceof L2Armor)
       {
           L2Armor armor = (L2Armor) equipedItem.getItem();
           L2Armor virtualarmor = (L2Armor)virtualItem;
          
           return armorMatching(virtualarmor.getItemType(), armor.getItemType(), weaponLHANDId, equipedItemId);
       }
       return equipedItemId;
   }
   
   public static int getRHAND(L2PcInstance actor)
   {
       int weaponRHANDId = actor.visualArmor.weaponRHANDId;
       L2ItemInstance equipedItem = actor.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
       int equipedItemId = actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_RHAND);
       L2Item virtualItem = ItemTable.getInstance().getTemplate(weaponRHANDId);
      
       if(equipedItem == null || weaponRHANDId == 0)
           return equipedItemId;
      
       //Only check same weapon types. Virtual replacement should not happen between armor/weapons.
       if(equipedItem.getItem() instanceof L2Weapon && virtualItem instanceof L2Weapon)
       {
           L2Weapon weapon = (L2Weapon) equipedItem.getItem();
           L2Weapon virtualweapon = (L2Weapon)virtualItem;
          
           return weaponMatching(virtualweapon.getItemType(), weapon.getItemType(), weaponRHANDId, equipedItemId);
       }
       else if(equipedItem.getItem() instanceof L2Armor && virtualItem instanceof L2Armor)
       {
           L2Armor armor = (L2Armor) equipedItem.getItem();
           L2Armor virtualarmor = (L2Armor)virtualItem;
          
           return armorMatching(virtualarmor.getItemType(), armor.getItemType(), weaponRHANDId, equipedItemId);
       }
       return equipedItemId;
      
   }
   
   
   
   public static int getCloak(L2PcInstance actor)
   {
       if(actor.visualArmor.weaponLRHANDId == 1)
           return 0;
       else
           return actor.getInventory().getPaperdollItemId(Inventory.PAPERDOLL_CLOAK);
          
   }
   
   public static void dressMe(L2PcInstance activeChar)
   {
       setVirtualBody(activeChar);
       setVirtualGloves(activeChar);
       setVirtualPants(activeChar);
       setVirtualBoots(activeChar);
       setVirtualLhand(activeChar);
       setVirtualRhand(activeChar);
      
       InventoryUpdate iu = new InventoryUpdate();
        activeChar.sendPacket(iu);
       activeChar.broadcastUserInfo();
       InventoryUpdate iu2 = new InventoryUpdate();
        activeChar.sendPacket(iu2);
       activeChar.broadcastUserInfo();
       activeChar.sendMessage("You changed clothes."); 
       activeChar.visualArmor.updateVisualArmor();
   }
}
