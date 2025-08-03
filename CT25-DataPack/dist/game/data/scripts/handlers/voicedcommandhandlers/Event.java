package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.phoenix.ManagerNpc;

/**
 * @author fissban
 */
public class Event implements IVoicedCommandHandler
{
	@Override
	public String[] getVoicedCommandList()
	{
		return new String[]
		{
			"event"
		};
	}
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String texto)
	{
		if (command.equalsIgnoreCase("event"))
		{
			ManagerNpc.getInstance().showMain(activeChar.getObjectId());
		}
		return true;
	}
}