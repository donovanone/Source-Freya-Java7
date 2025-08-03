package handlers.voicedcommandhandlers;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.SiegeInfo;
 
/**
 * Projeto PkElfo
 */

public class Siege implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"siege",
		"siege_gludio",
		"siege_dion",
		"siege_giran",
		"siege_oren",
		"siege_aden",
		"siege_innadril",
		"siege_goddard",
		"siege_rune",
		"siege_schuttgart"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.startsWith("siege"))
		{
			sendHtml(activeChar);
		}
		
		if (command.startsWith("siege_"))
		{
			if ((activeChar.getClan() != null) && !activeChar.isClanLeader())
			{
				activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return false;
			}
			
			int castleId = 0;
			
			if (command.startsWith("siege_gludio"))
			{
				castleId = 1;
			}
			else if (command.startsWith("siege_dion"))
			{
				castleId = 2;
			}
			else if (command.startsWith("siege_giran"))
			{
				castleId = 3;
			}
			else if (command.startsWith("siege_oren"))
			{
				castleId = 4;
			}
			else if (command.startsWith("siege_aden"))
			{
				castleId = 5;
			}
			else if (command.startsWith("siege_innadril"))
			{
				castleId = 6;
			}
			else if (command.startsWith("siege_goddard"))
			{
				castleId = 7;
			}
			else if (command.startsWith("siege_rune"))
			{
				castleId = 8;
			}
			else if (command.startsWith("siege_schuttgart"))
			{
				castleId = 9;
			}
			
			Castle castle = CastleManager.getInstance().getCastleById(castleId);
			if ((castle != null) && (castleId != 0))
			{
				activeChar.sendPacket(new SiegeInfo(castle));
			}
		}
		return true;
	}
	
	private void sendHtml(L2PcInstance activeChar)
	{
		String htmFile = "data/html/custom/CastleManager.htm";
		
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setFile(activeChar.getHtmlPrefix(), htmFile);
		activeChar.sendPacket(msg);
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}