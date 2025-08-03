package handlers.admincommandhandlers;

import java.util.Collection;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * Projeto Donovan
 */

// Comando custom para enviar Mensagens na tela
public class AdminMensaje implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_msjall",
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_msjall"))
		{
			command = command.substring(13);
			command = activeChar.getName() + ": " + command;
                       
			final ExShowScreenMessage cs; 
			cs = new ExShowScreenMessage (command, 1500);
			Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();
			for (L2PcInstance playersOnline : pls)
			{
				if (playersOnline == null)
				{
					continue;
				}
				playersOnline.sendPacket(cs);// envio de mensage
			}
			AdminHelpPage.showHelpPage(activeChar, "main_menu.htm");
		}
		else
		{
			activeChar.sendMessage("Comando Correto: //msjall + texto ");
			return false; 
		}
		return false;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}