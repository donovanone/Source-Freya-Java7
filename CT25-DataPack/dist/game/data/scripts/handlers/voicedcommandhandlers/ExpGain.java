package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class ExpGain implements IVoicedCommandHandler
{
 private String[] _voicedCommands = {
				 "expon",
				 "xpon",
				 "expoff",
				 "xpoff"
 };

 public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
 if (Config.ALLOW_EXP_GAIN_COMMAND)
 {
		 if (command.equalsIgnoreCase("expon") || command.equalsIgnoreCase("xpon"))
		 {
				 activeChar.setExpOn(true);
				 						activeChar.sendMessage("Exp refusal enabled");
				 						ExShowScreenMessage message1 = new ExShowScreenMessage("Exp/sp refusal mode is now enabled!", 4000);
				 						activeChar.sendPacket(message1);
		 }
		 else if (command.equalsIgnoreCase("expoff") || command.equalsIgnoreCase("xpoff"))
		 {
				 activeChar.setExpOn(false);
				 						activeChar.sendMessage("Exp/sp refusal disabled");
				 						ExShowScreenMessage message1 = new ExShowScreenMessage("Exp/sp refusal mode is now disabled!", 4000);
				 						activeChar.sendPacket(message1);
		 }
 }

	 else
	 {
		 activeChar.sendMessage("Command Disable By Admin");
	 }
		 return true;
		
 }

 public String[] getVoicedCommandList()
 {
		 return _voicedCommands;
 }
}