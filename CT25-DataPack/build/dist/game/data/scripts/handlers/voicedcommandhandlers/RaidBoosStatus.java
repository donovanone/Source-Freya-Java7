package handlers.voicedcommandhandlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.l2jserver.gameserver.datatables.NpcTable;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.instancemanager.GrandBossManager;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Donovan
 */

public class RaidBoosStatus implements IVoicedCommandHandler
{
	static final Logger _log = Logger.getLogger(RaidBoosStatus.class.getName());
    private static final String[] _voicedCommands =
    {
		"grandboss"
	};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (command.startsWith("grandboss"))
		{
			return Status(activeChar);
		}
		return false;
	}

	public boolean Status(L2PcInstance activeChar)
    {
		int[] BOSSES = { 29001, 29006, 29014, 29019, 29020, 29022, 29028, 29062, 29065, 29118 };
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		activeChar.sendMessage("==========<Grand Bosses>==========");
        for (int boss : BOSSES)
		{
			String name = NpcTable.getInstance().getTemplate(boss).getName();
            StatsSet stats = GrandBossManager.getInstance().getStatsSet(boss);
            if (stats == null)
			{
				activeChar.sendMessage("Estatistca de Grand Boss " + boss + " no encontrado!");
				continue;
			}
			if (boss == 29019)
			{
				long dmax = 0;
                for (int i = 29066; i <= 29068; i++)
				{
					StatsSet s = GrandBossManager.getInstance().getStatsSet(i);
                    if (s == null)
                    	continue;
					long d = s.getLong("respawn_time");
                    if (d >= dmax)
					{
						dmax = d;
                        stats = s;
					}
				}
			}
            long delay = stats.getLong("respawn_time");
            long currentTime = System.currentTimeMillis();
            if (delay <= currentTime)
			{
				activeChar.sendMessage(name +" = esta vivo");
			}
			else
			{
				activeChar.sendMessage(name +" = esta muerto ( "+sdf.format(new Date(delay))+" )");
			}
		}
		activeChar.sendMessage("==============================");
		return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}