package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class BuyRec implements IVoicedCommandHandler
{
 private static final String[] VOICED_COMMANDS =
 {
	 "buyrec"
 };

 public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
	 if (command.equalsIgnoreCase("buyrec"))
	 {
		 if(Config.REC_BUY)
		 {
			 if(activeChar.getInventory().getItemByItemId(Config.REC_ITEM_ID) != null && activeChar.getInventory().getItemByItemId(Config.REC_ITEM_ID).getCount() >= Config.REC_PRICE)
			 {
				 activeChar.getInventory().destroyItemByItemId("Rec", Config.REC_ITEM_ID, Config.REC_PRICE, activeChar, activeChar.getTarget());
				 activeChar.setRecomHave(activeChar.getRecomHave() + Config.REC_REWARD);
				 activeChar.sendMessage("You earned "+Config.REC_REWARD+" recommendations.");
				 ExShowScreenMessage message1 = new ExShowScreenMessage("You earned "+Config.REC_REWARD+" recommendations!", 4000);
				 activeChar.sendPacket(message1);
				 activeChar.broadcastUserInfo();
			 }
			 else
			 {
				 activeChar.sendMessage("You don't have enought items");
				 return true;
			 }
		 }
		 else
		 {
			 activeChar.sendMessage("Command Disable By Admin");
		 }
	 }
	 return false;
 }
 public String[] getVoicedCommandList()
 {
	 return VOICED_COMMANDS;
 }
}