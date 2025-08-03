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
import java.sql.ResultSet;
import java.sql.SQLException;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;

/**
 * @author Issle
 *
 */
public class VisualArmorModel
{
   private static final String RESTORE_VISUAL_ARMOR = "SELECT GlovesId,ChestId,BootsId,PantsId,LeftHandId,RightHandId,DoubleHandId FROM visual_armor WHERE CharId=?";
   private static final String UPDATE_VISUAL_ARMOR = "UPDATE visual_armor SET GlovesId=?,ChestId=?,BootsId=?,PantsId=?,LeftHandId=?,RightHandId=?,DoubleHandId=? WHERE CharId=?";
   private static final String CREATE_VISUAL_ARMOR = "INSERT INTO visual_armor (CharId,GlovesId,ChestId,BootsId,PantsId,LeftHandId,RightHandId,DoubleHandId) values (?,?,?,?,?,?,?,?)";
   
   public static final String CREATE =
       "CREATE TABLE IF NOT EXISTS `visual_armor` (" +
       "`CharId` int(11) NOT NULL," +
       "`GlovesId` int(11) NOT NULL DEFAULT '0'," +
       "`BootsId` int(11) NOT NULL DEFAULT '0'," +
       "`ChestId` int(11) NOT NULL DEFAULT '0'," +
       "`PantsId` int(11) NOT NULL DEFAULT '0'," +
       "`LeftHandId` int(11) NOT NULL DEFAULT '0'," +
       "`RightHandId` int(11) NOT NULL DEFAULT '0'," +
       "`DoubleHandId` int(11) NOT NULL DEFAULT '0',PRIMARY KEY (`CharId`))";
   
   public static final String DROP =
       "DROP TABLE 'visual_armor'";
   
   public int glovesTextureId=0;
   public int armorTextureId=0;
   public int pantsTextureId=0;
   public int bootsTextureId=0;
   public int weaponLHANDId=0;
   public int weaponRHANDId=0;
   public int weaponLRHANDId=0;
   public int ownerId;
   
   
   public void updateVisualArmor()
   {
       Connection con = null;
       try
       {
           con = L2DatabaseFactory.getInstance().getConnection();
           PreparedStatement statement = con.prepareStatement(UPDATE_VISUAL_ARMOR);
           statement.setInt(1, glovesTextureId);
           statement.setInt(2, armorTextureId);
           statement.setInt(3, bootsTextureId);
           statement.setInt(4, pantsTextureId);
           statement.setInt(5, weaponLHANDId);
           statement.setInt(6, weaponRHANDId);
           statement.setInt(7, weaponLRHANDId);
           statement.setInt(8, ownerId);
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
   
   public VisualArmorModel(L2PcInstance activeChar)
   {
       ownerId = activeChar.getObjectId();
       Connection con = null;
       try
       {
           con = L2DatabaseFactory.getInstance().getConnection();
          
           PreparedStatement statement = con.prepareStatement(RESTORE_VISUAL_ARMOR);
           statement.setInt(1, ownerId);
           ResultSet rset = statement.executeQuery();
           boolean got = false;
           while(rset.next())
           {
               glovesTextureId = rset.getInt("GlovesId");
               armorTextureId = rset.getInt("ChestId");
               pantsTextureId = rset.getInt("PantsId");
               bootsTextureId = rset.getInt("BootsId");
               weaponLHANDId = rset.getInt("LeftHandId");
               weaponRHANDId = rset.getInt("RightHandId");
               weaponLRHANDId = rset.getInt("DoubleHandId");
               got = true;
      
           }
          
           rset.close();
           statement.close();
          
           if(got == false)
           {
               createVisualArmor();
           }
          
           InventoryUpdate iu = new InventoryUpdate();
           activeChar.sendPacket(iu);
           activeChar.broadcastUserInfo();
           InventoryUpdate iu2 = new InventoryUpdate();
           activeChar.sendPacket(iu2);
           activeChar.broadcastUserInfo();
           activeChar.sendMessage("You changed clothes.");
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
   
   public void createVisualArmor() throws SQLException
   {
       Connection con = null;
      
       try
       {
           con = L2DatabaseFactory.getInstance().getConnection();
           PreparedStatement statement = con.prepareStatement(CREATE_VISUAL_ARMOR);
          
           statement.setInt(1, ownerId);
           statement.setInt(2, 0);
           statement.setInt(3, 0);
           statement.setInt(4, 0);
           statement.setInt(5, 0);
           statement.setInt(6, 0);
           statement.setInt(7, 0);
           statement.setInt(8, 0);
          
           statement.executeUpdate();
           statement.close();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
       finally
       {
           try { con.close(); } catch (Exception e) {}
       }
   }
   
}

