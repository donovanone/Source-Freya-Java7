package handlers.admincommandhandlers;

import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;



public class AdminRecallAll implements IAdminCommandHandler
{
 private static final String[] ADMIN_COMMANDS = { "admin_recallall" };

 private void teleportTo(L2PcInstance activeChar, int x, int y, int z)
 {

 activeChar.teleToLocation(x, y, z, false);

 }
 public boolean useAdminCommand(String command, L2PcInstance activeChar)
 {
 if (command.startsWith("admin_recallall"))
 {
 for(L2PcInstance players :L2World.getInstance().getAllPlayers().values())
 {
 teleportTo(players, activeChar.getX(), activeChar.getY(), activeChar.getZ());
 }
 }
 return false;
 }
 public String[] getAdminCommandList()
 {
 return ADMIN_COMMANDS;
 }
}