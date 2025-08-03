package handlers.bypasshandlers;

import java.util.StringTokenizer;

import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.phoenix.EventsInterface;
import com.l2jserver.gameserver.phoenix.ManagerNpc;
import com.l2jserver.gameserver.phoenix.container.EventContainer;
import com.l2jserver.gameserver.phoenix.functions.Buffer;
import com.l2jserver.gameserver.phoenix.functions.Scheduler;
import com.l2jserver.gameserver.phoenix.functions.Vote;

/**
 * @author fissban
 */
public class EventPhoenix implements IBypassHandler
{
	@Override
	public String[] getBypassList()
	{
		return new String[]
		{
			"eventmanager"
		};
	}
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (command.startsWith("eventmanager"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();// bypass eventmanager
			final int player = activeChar.getObjectId();
			
			switch (st.nextToken())
			{
				case "vote":
					Vote.getInstance().addVote(player, Integer.parseInt(st.nextToken()));
					break;
				case "buffershow":
					Buffer.getInstance().showHtml(player);
					break;
				case "buffer":
					Buffer.getInstance().changeList(player, Integer.parseInt(command.substring(20, command.length() - 2)), (Integer.parseInt(command.substring(command.length() - 1)) == 0 ? false : true));
					Buffer.getInstance().showHtml(player);
					break;
				case "register":
					EventContainer.getInstance().getEvent(Integer.parseInt(st.nextToken())).registerPlayer(player);
					break;
				case "showreg":
					ManagerNpc.getInstance().showRegisterPage(player, Integer.parseInt(st.nextToken(), command.length() - 2), Integer.parseInt(command.substring(command.length() - 1)));
					break;
				case "unregister":
					EventContainer.getInstance().getEvent(Integer.parseInt(st.nextToken())).unregisterPlayer(player);
					break;
				case "showvotelist":
					ManagerNpc.getInstance().showVoteList(player);
					break;
				case "mainmenu":
					EventsInterface.showFirstHtml(player, 0);
					break;
				case "scheduler":
					Scheduler.getInstance().scheduleList(player);
					break;
				case "status":
					ManagerNpc.getInstance().showStatusPage(player, Integer.parseInt(st.nextToken()));
					break;
				case "running":
					ManagerNpc.getInstance().showRunningList(player);
					break;
			}
		}
		return false;
	}
}
