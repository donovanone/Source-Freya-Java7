package handlers.admincommandhandlers;

import java.util.logging.Logger;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class AdminMassHero implements IAdminCommandHandler
{
       protected static final Logger _log = Logger.getLogger(AdminMassHero.class.getName());
       
       @Override
       public String[] getAdminCommandList()
       {
               return ADMIN_COMMANDS;
       }

       @Override
       public boolean useAdminCommand(String command, L2PcInstance activeChar)
       {
               if(activeChar == null)
                       return false;

               if(command.startsWith("admin_masshero"))
               {
                       for(L2PcInstance player : L2World.getInstance().getAllPlayers().values())
                       {
                               if(player instanceof L2PcInstance)
                               {
                                       /* Check to see if the player already is Hero and if aren't in Olympiad Mode */
                                       if(!player.isHero() || !player.isInOlympiadMode())
                                       {
                                               player.setHero(true);
                                               player.sendMessage("Admin is rewarding all online players with Hero Status.");
                                               player.broadcastUserInfo();
                                       }
                                       player = null;
                               }
                       }
               }
               return true;
       }

       private static String[] ADMIN_COMMANDS = { "admin_masshero" };
       
}
