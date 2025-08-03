package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Donovan
 */

public class Res implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"res" 
    };

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {   
        if (command.equalsIgnoreCase("res") && activeChar.isGM())
        {
              if (!activeChar.isAlikeDead())
              {
                 activeChar.sendMessage("Usted no puede ser resucitado estando vivo.");
                 {
                	 return false;
                 }
              }
           if(activeChar.isInOlympiadMode())
           {
              activeChar.sendMessage("Usted no puede ser resucitado estando en olympiad.");
              {
            	  return false;
              }
           }
           if(activeChar.getInventory().getItemByItemId(57) == null)
           {
              activeChar.sendMessage("Necesita 10 kk para ser resucitado.");
              {
            	  return false;
              }
           }
              activeChar.sendMessage("You have been ressurected!");
              activeChar.getInventory().destroyItemByItemId("RessSystem", 57, 100000, activeChar, activeChar.getTarget());
              activeChar.doRevive();
              activeChar.broadcastUserInfo();
              activeChar.sendMessage("10kk de Adena a desaparecido!");
        }
       return true;
    }
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}