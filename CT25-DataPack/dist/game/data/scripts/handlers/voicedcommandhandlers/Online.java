package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Donovan
 */

public class Online implements IVoicedCommandHandler
{
    private static String[] _voicedCommands =
    {
        "online"
    };

    @Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
		if(Config.CMD_FAKE)
		{
			activeChar.sendMessage("=======<Jugadores Online>======");
			activeChar.sendMessage("Jugadores online: " + (Config.FAKE_PLAYERS + L2World.getInstance().getAllPlayers().size()));
			activeChar.sendMessage("===============================");
		}
		else
		{
			activeChar.sendMessage("=======<Jugadores Online>======");
			activeChar.sendMessage("Jugadores online: " + L2World.getInstance().getAllPlayers().size());
			activeChar.sendMessage("===============================");
		}
        return true;
    }

    @Override
	public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}