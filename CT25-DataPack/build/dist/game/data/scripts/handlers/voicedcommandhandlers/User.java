package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GameTimeController;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author NeverMore
 */

public class User implements IVoicedCommandHandler
{
       public static final String[] VOICED_COMMANDS = { "user" , "user1", "user2", "user3" };
      
       @Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
       {
       	   if (Config.SHOW_USER)
    	   {
       		   if(command.equalsIgnoreCase("user"))
       		   {
       			   User.showUserPage(activeChar, "user.htm");
       		   }      
      		   if(command.equalsIgnoreCase("user1"))
       		   {
       			   User.showUserPage(activeChar, "user1.htm");
       		   }  
      		   if(command.equalsIgnoreCase("user2"))
       		   {
       			   User.showUserPage(activeChar, "user2.htm");
       		   }
      		   if(command.equalsIgnoreCase("user3"))
       		   {
       			   User.showUserPage(activeChar, "user3.htm");
       		   }
    	   }
       	   else
       	   {
   					activeChar.sendMessage("This command is disabled");
   					ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
   					activeChar.sendPacket(message1);
   					return false;
       	   }    
			return false;      
       }    	  
      
       public static String getServerRunTime()
       {
    	   int timeSeconds = (GameTimeController.getGameTicks() - 36000) / 10;
    	   String timeResult = "";
    	   if (timeSeconds >= 86400)
    		   timeResult = Integer.toString(timeSeconds / 86400) + " Days " + Integer.toString((timeSeconds % 86400) / 3600) + " hours";
    	   else
    		   timeResult = Integer.toString(timeSeconds / 3600) + " Hours " + Integer.toString((timeSeconds % 3600) / 60) + " mins";
    	   return timeResult;
       }
             
       public static String getRealOnline()
       {
    	   int counter = 0;
    	   for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayers().values())
    	   {
    		   if (onlinePlayer.isOnline() && (onlinePlayer.getClient() != null && !onlinePlayer.getClient().isDetached()))
    		   {
    			   counter++;
    		   }
    	   }
    	   String realOnline = "<tr><td fixwidth=11></td><td FIXWIDTH=280>Players Active</td><td FIXWIDTH=470><font color=FF6600>" + counter + "</font></td></tr>" + "<tr><td fixwidth=11></td><td FIXWIDTH=280>Players Shops</td><td FIXWIDTH=470><font color=FF6600>" + (L2World.getInstance().getAllPlayersCount() - counter) + "</font></td></tr>";
    	   return realOnline;
       }
       
       public static void showUserPage(L2PcInstance targetChar, String filename)
       {
    	   String content = HtmCache.getInstance().getHtmForce(targetChar.getHtmlPrefix(), "data/html/userpanel/" + filename);
    	   NpcHtmlMessage UserPanelReply = new NpcHtmlMessage(5);
    	   UserPanelReply.setHtml(content);
    	   UserPanelReply.replace("%online%", String.valueOf(L2World.getInstance().getAllPlayers().size()));
    	   UserPanelReply.replace("%name%", String.valueOf(targetChar.getName()));
    	   UserPanelReply.replace("%serveronline%", getRealOnline());
    	   UserPanelReply.replace("%servercapacity%", Integer.toString(Config.MAXIMUM_ONLINE_USERS));
    	   UserPanelReply.replace("%serverruntime%", getServerRunTime());
    	   UserPanelReply.replace("%playernumber%", String.valueOf(L2World.getInstance().getAllPlayers().size()));
   		   if (!Config.ENABLE_SPECIAL_EFFECT)
		   {
   			UserPanelReply.replace("%effect%", "Disabled");
		   }
		   else if (L2PcInstance._isoneffect == true)
		   {
			   UserPanelReply.replace("%effect%", "ON");
		   }
		   else  
		   {
			   UserPanelReply.replace("%effect%", "OFF");
		   }
   		   if (!Config.ENABLE_PM_REFUSAL)
   		   {
   			   UserPanelReply.replace("%pm%", "Disabled");
   		   }
   		   else if (L2PcInstance._ispmrefusal == true)
   		   {
   			   UserPanelReply.replace("%pm%", "ON");
   		   }
   		   else
   		   {
   			   UserPanelReply.replace("%pm%", "OFF");
   		   }
   		   if (!Config.ENABLE_TRADE_REFUSAL)
   		   {
   			   UserPanelReply.replace("%trade%", "Disabled");
   		   }
   		   else if (L2PcInstance._istraderefusal == true)
   		   {
   			   UserPanelReply.replace("%trade%", "ON");
   		   }
   		   else
   		   {
   			   UserPanelReply.replace("%trade%", "OFF");
   		   }
		   if (!Config.ENABLE_SPECIAL_EFFECT)
		   {
			   UserPanelReply.replace("%effect%", "Disabled");
		   }
		   else if (L2PcInstance._isoneffect == true)
		   {
			   UserPanelReply.replace("%effect%", "ON");
		   }
		   else  
		   {
			   UserPanelReply.replace("%effect%", "OFF");
		   }   		   
   		   targetChar.sendPacket(UserPanelReply);
	 }

	
	@Override
	public String[] getVoicedCommandList()
       {
               return VOICED_COMMANDS;
       }
}