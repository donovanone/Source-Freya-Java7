package handlers.admincommandhandlers;

import java.util.Collection;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
/**
 * @author Ventic
 * @author Elfocrash
 * @editado ChuChuQui
 */
public class AdminSitDeport implements IAdminCommandHandler
{
   private static String[] _adminCommands =
   {
       "admin_deport",
       "admin_rangedeport",
       "admin_sit",
       "admin_stand",
       "admin_rangesit",
       "admin_rangestand"
   };
   

   @Override
   public boolean useAdminCommand(String command, L2PcInstance activeChar)
   {

      if(activeChar.getTarget() instanceof L2PcInstance)
      {
         if(command.startsWith("admin_deport"))
         {
            ((L2PcInstance) activeChar.getTarget()).teleToLocation(82698, 148638, -3473);
         }
      }
      
 /**
 * Esta linea editada por mi
 * @editado ChuChuQui
 */
      if (command.startsWith("admin_rangedeport"))
      {
              Collection<L2Character> players = activeChar.getKnownList().getKnownCharactersInRadius(2000);
              for (L2Character p : players)
              {
                      if (p instanceof L2PcInstance)
                      {
                              ((L2PcInstance) p).teleToLocation(82698, 148638, -3473);
                      }
              }
      }
      
      if (command.startsWith("admin_sit"))
      {
    	  L2Object target = activeChar.getTarget();
              if (target instanceof L2NpcInstance)
              {
                      activeChar.sendMessage("You can not use it at NPCs!");
                      return false;
              }
              else if (target == null)
              {
                      activeChar.sendMessage("You have no target!");
                      return false;
              }
              else
              {
                      ((L2PcInstance) target).sitDown();
              }
      }
     
      if (command.startsWith("admin_stand"))
      {
    	  L2Object target = activeChar.getTarget();
              if (target instanceof L2NpcInstance)
              {
                      activeChar.sendMessage("You can not use it at NPCs!");
                      return false;
              }
              else if (target == null)
              {
                      activeChar.sendMessage("You have no target!");
                      return false;
              }
              else
              {
                      ((L2PcInstance) target).standUp();
              }
      }
     
      if (command.startsWith("admin_rangesit"))
      {
              Collection<L2Character> players = activeChar.getKnownList().getKnownCharactersInRadius(2000);
              for (L2Character p : players)
              {
                      if (p instanceof L2PcInstance)
                      {
                              ((L2PcInstance) p).sitDown();
                      }
              }
      }
     
      if (command.startsWith("admin_rangestand"))
      {
              Collection<L2Character> players = activeChar.getKnownList().getKnownCharactersInRadius(2000);
              for (L2Character p : players)
              {
                      if (p instanceof L2PcInstance)
                      {
                              ((L2PcInstance) p).standUp();
                      }
              }
             
      }
      
      
      return false;

   }
   
   @Override
   public String[] getAdminCommandList()
   {
      return _adminCommands;
   }
}