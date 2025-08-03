package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Donovan
 */

public class Logout implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
        	{ 
        		"logout"
        	};
        
    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
        {
                if (command.equalsIgnoreCase("logout"))
                {
                	activeChar.logout(false);
                }
                return true;
        }
    
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}