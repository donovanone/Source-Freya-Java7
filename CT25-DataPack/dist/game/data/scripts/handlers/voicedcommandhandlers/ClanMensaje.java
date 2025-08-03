package handlers.voicedcommandhandlers;
    
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.L2ClanMember;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * Donovan
 */

public class ClanMensaje implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
	"clanmsg"
	};
       
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("clanmsg") && activeChar.isGM())
		{
			target = activeChar.getName() + ": " + target;
            
			final ExShowScreenMessage cs = new ExShowScreenMessage(target, 1500);
            
			if (activeChar.getClan() != null)
			{
				if (activeChar.isClanLeader())
				{
					for (L2ClanMember member : activeChar.getClan().getMembers())
					{
						if (member.isOnline())
						{
							member.getPlayerInstance().sendPacket(cs);
						}
					}
					return true;
				}
				activeChar.sendMessage("Voce nao e lider de um Clan!");
				return false;                
			}
			activeChar.sendMessage("Voce nao tem Clan!");
			return false;
		}
		activeChar.sendMessage("Comando correto: .clanmsg + texto ");
		return false;          
	}
      
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}