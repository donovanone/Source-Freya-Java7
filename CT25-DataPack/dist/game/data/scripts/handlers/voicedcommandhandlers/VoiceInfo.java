package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.GameServer;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
 
/**
 * Projeto PkElfo
 */

public class VoiceInfo implements IVoicedCommandHandler
{
	private static String[]	VOICED_COMMANDS	=
		{ 
			"infos"
		};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		String htmFile = "data/html/ServerInfo.htm";
		String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), htmFile);
		if (htmContent != null)
		{
			NpcHtmlMessage infoHtml = new NpcHtmlMessage(1);
			infoHtml.setHtml(htmContent);
			infoHtml.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
			infoHtml.replace("%server_core_version%", String.valueOf(Config.SERVER_VERSION));
			infoHtml.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
			infoHtml.replace("%server_free_mem%", String.valueOf((Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()) / 1048576));
			infoHtml.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
			infoHtml.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
			infoHtml.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
			infoHtml.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
			infoHtml.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
			infoHtml.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(57)));
			infoHtml.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
			infoHtml.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
			infoHtml.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
			infoHtml.replace("%rate_quest_reward%", String.valueOf(Config.RATE_QUEST_REWARD));
			infoHtml.replace("%rate_drop_quest%", String.valueOf(Config.RATE_QUEST_DROP));
			infoHtml.replace("%pet_rate_xp%", String.valueOf(Config.PET_XP_RATE));
			infoHtml.replace("%sineater_rate_xp%", String.valueOf(Config.SINEATER_XP_RATE));
			infoHtml.replace("%pet_food_rate%", String.valueOf(Config.PET_FOOD_RATE));
			activeChar.sendPacket(infoHtml);
		}
		else
		{
			activeChar.sendMessage("omg lame error! onde esta " + htmFile + " ! culpar o Admin Server");
		}
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}