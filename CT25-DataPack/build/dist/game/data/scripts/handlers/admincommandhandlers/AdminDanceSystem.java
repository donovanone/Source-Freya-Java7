package handlers.admincommandhandlers;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.PlaySound;
import com.l2jserver.gameserver.skills.AbnormalEffect;

/**
 *
 * @author  NeverMore
 */
public class AdminDanceSystem implements IAdminCommandHandler
{
 boolean _temp = false;
 
 private static final String[] ADMIN_COMMANDS = { "admin_dance" , "admin_gangnam", "admin_sexi"};
 
 @Override
 public boolean useAdminCommand(String command, L2PcInstance activeChar)
 {
 if (command.equals("admin_dance"))
 { 
 AdminHelpPage.showHelpPage(activeChar, "dancesystem.htm");
 }    
 
 if (command.equals("admin_gangnam"))
 { 
 if (_temp == true)
 {
 ExShowScreenMessage message1 = new ExShowScreenMessage("ya existe un evento de danza! Intente mas tarde!", 4000); 
 activeChar.sendPacket(message1);
 return false;
 }
 _temp = true;
 ExShowScreenMessage message1 = new ExShowScreenMessage("Vamos a tener un poco de diversion ! En 30 seg el evento de danza comenzara!", 4000);
 activeChar.sendPacket(message1);
       ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
       {
       @Override
       public void run()
       {
       try
               {
    	   for(L2PcInstance player : L2World.getInstance().getAllPlayers().values())
       {
       PlaySound _song = new PlaySound(1, "Gangnam", 0, 0, 0, 0, 0);
       player.sendPacket(_song);
      ExShowScreenMessage message1 = new ExShowScreenMessage( "Muestrenme lo que ustedes tienen, vamos sacudir el bote  !", 8000);
       player.sendPacket(message1);
       player.setIsParalyzed(true);
       player.setIsInvul(true);
       player.broadcastSocialAction(10);
       ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 3500);
       ThreadPoolManager.getInstance().scheduleGeneral(new MyTask2(), 40000);
       }
               }
       catch (Exception e)
       {
       }
       }
       }, (30000));
 }    

 if (command.equals("admin_sexi"))
 { 
 if (_temp == true)
 {
 ExShowScreenMessage message1 = new ExShowScreenMessage("ya existe un evento de danza! Intente mas tarde!", 4000); 
 activeChar.sendPacket(message1);
 return false;
 }
           _temp = true;
 ExShowScreenMessage message1 = new ExShowScreenMessage("Vamos a tener un poco de diversion ! En 30 seg el evento de danza comenzara!", 4000);
 activeChar.sendPacket(message1);
       ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
       {
           @Override
           public void run()
           {
               try
               {
            	   for(L2PcInstance player : L2World.getInstance().getAllPlayers().values())
                   {
           PlaySound _song = new PlaySound(1, "sexi", 0, 0, 0, 0, 0);
           player.sendPacket(_song);
           ExShowScreenMessage message1 = new ExShowScreenMessage( "Muestrenme lo que ustedes tienen, vamos sacudir el bote !", 8000);
           player.sendPacket(message1);
           player.setIsParalyzed(true);
           player.setIsInvul(true);
           player.broadcastSocialAction(10);
           ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 3500);
           ThreadPoolManager.getInstance().scheduleGeneral(new MyTask2(), 43000);
                   }
               }
               catch (Exception e)
               {
               }
           }
       }, (30000)); 
 }    

 return false;
 }
 
 class MyTask implements Runnable
 {
     @Override
 public void run()
     {
      if(_temp == true)
      {
    	  for(L2PcInstance player : L2World.getInstance().getAllPlayers().values())
      {
      player.broadcastSocialAction(18);
      }
      ThreadPoolManager.getInstance().scheduleGeneral(new MyTask(), 18000);
      }
     }
 }
 class MyTask2 implements Runnable
 {

     @Override
 public void run()
     {
    	 for(L2PcInstance player : L2World.getInstance().getAllPlayers().values())
     {
     _temp = false;
    player.setIsParalyzed(false);
    player.setIsInvul(false);
     player.broadcastSocialAction(10);
     player.broadcastSocialAction(11);
     player.stopAbnormalEffect(AbnormalEffect.MAGIC_CIRCLE);
     }
     }
 }

 @Override
 public String[] getAdminCommandList()
       {
               return ADMIN_COMMANDS;
       }
}