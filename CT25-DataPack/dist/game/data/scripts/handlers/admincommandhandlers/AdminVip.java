package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.GmListTable;
import com.l2jserver.gameserver.handler.IAdminCommandHandler;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.EtcStatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * Give / Take Status Vip to Player
 * Changes name color and title color if enabled
 *
 * Uses:
 * setvip [<player_name>] [<time_duration in days>]
 * removevip [<player_name>]
 *
 * If <player_name> is not specified, the current target player is used.
 *
 *
 * @author KhayrusS
 *
 */
public class AdminVip implements IAdminCommandHandler
{
        private static String[] _adminCommands = { "admin_setvip", "admin_removevip" };
        private final static Logger _log = Logger.getLogger(AdminVip.class.getName());

        public boolean useAdminCommand(String command, L2PcInstance activeChar)
        {
 /*if (!Config.ALT_PRIVILEGES_ADMIN)
                                        if (!(checkLevel(activeChar.getAccessLevel()) && activeChar.isGM()))
                                        {
                                                        GmListTable.broadcastMessageToGMs("Player "+activeChar.getName()+ " tryed illegal action set vip stat");
                                                        return false;
                                        }*/

                        if (command.startsWith("admin_setvip"))
                        {
                                        StringTokenizer str = new StringTokenizer(command);
                                        L2Object target = activeChar.getTarget();

                                        L2PcInstance player = null;
                                        SystemMessage sm = new SystemMessage(SystemMessageId.S1);

                                        if (target != null && target instanceof L2PcInstance)
                                                        player = (L2PcInstance)target;
                                        else
                                                        player = activeChar;

                                        try
                                        {
                                                        str.nextToken();
                                                        String time = str.nextToken();
                                                        if (str.hasMoreTokens())
                                                        {
                                                                        String playername = time;
                                                                        time = str.nextToken();
                                                                        player = L2World.getInstance().getPlayer(playername);
                                                                        doVip(activeChar, player, playername, time);
                                                        }
                                                        else
                                                        {
                                                                        String playername = player.getName();
                                                                        doVip(activeChar, player, playername, time);
                                                        }
                                                        if(!time.equals("0"))
                                                        {
                                                                        sm.addString("You are now a Vip , congratulations!");
                                                                        player.sendPacket(sm);
                                                        }
                                        }
                                        catch(Exception e)
                                        {
                                                        activeChar.sendMessage("Usage: //setvip <char_name> [time](in days)");
                                        }

                                        player.broadcastUserInfo();
                                        if(player.isVip())
                                                        return true;
                        }
                        else if(command.startsWith("admin_removevip"))
                        {
                                        StringTokenizer str = new StringTokenizer(command);
                                        L2Object target = activeChar.getTarget();

                                        L2PcInstance player = null;

                                        if (target != null && target instanceof L2PcInstance)
                                                        player = (L2PcInstance)target;
                                        else
                                                        player = activeChar;

                                        try
                                        {
                                                        str.nextToken();
                                                        if (str.hasMoreTokens())
                                                        {
                                                                        String playername = str.nextToken();
                                                                        player = L2World.getInstance().getPlayer(playername);
                                                                        removeVip(activeChar, player, playername);
                                                        }
                                                        else
                                                        {
                                                                        String playername = player.getName();
                                                                        removeVip(activeChar, player, playername);
                                                        }
                                        }
                                        catch(Exception e)
                                        {
                                                        activeChar.sendMessage("Usage: //removevip <char_name>");
                                        }
                                        player.broadcastUserInfo();
                                        if(!player.isVip())
                                                        return true;
                        }
                        return false;
        }

        public void doVip(L2PcInstance activeChar, L2PcInstance _player, String _playername, String _time)
        {
                        int days = Integer.parseInt(_time);
                        if (_player == null)
                        {
                                        activeChar.sendMessage("not found char" + _playername);
                                        return;
                        }

                        if(days > 0)
                        {
                                        _player.setVip(true);
                                        _player.setEndTime("vip", days);

                                        Connection connection = null;
                                        try
                                        {
                                                        connection = L2DatabaseFactory.getInstance().getConnection();

                                                        PreparedStatement statement = connection.prepareStatement("UPDATE characters SET vip=1, vip_end=? WHERE charId=?");
                                                        statement.setLong(1, _player.getVipEndTime());
                                                        statement.setInt(2, _player.getObjectId());
                                                        statement.execute();
                                                        statement.close();
                                                        connection.close();

                                                        if(Config.ALLOW_VIP_NCOLOR && activeChar.isVip())
                                                                        _player.getAppearance().setNameColor(Config.VIP_NCOLOR);

                                                        if(Config.ALLOW_VIP_TCOLOR && activeChar.isVip())
                                                                        _player.getAppearance().setTitleColor(Config.VIP_TCOLOR);

                                                        _player.broadcastUserInfo();
                                                        _player.sendPacket(new EtcStatusUpdate(_player));
                                                        GmListTable.broadcastMessageToGMs("GM "+ activeChar.getName()+ " set vip stat for player "+ _playername + " for " + _time + " day(s)");
                                        }
                                        catch (Exception e)
                                        {
                                                        _log.log(Level.WARNING,"could not set vip stats of char:", e);
                                        }
                                        finally
                                        {
                                                        try {
                                                                        connection.close();
                                                        } catch (SQLException e) {
                                                                        // TODO Auto-generated catch block
                                                                        e.printStackTrace();
                                                        }
                                        }
                        }
                        else
                        {
                                        removeVip(activeChar, _player, _playername);
                        }
        }

        public void removeVip(L2PcInstance activeChar, L2PcInstance _player, String _playername)
        {
                        _player.setVip(false);
                        _player.setVipEndTime(0);

                        Connection connection = null;
                        try
                        {
                                        connection = L2DatabaseFactory.getInstance().getConnection();

                                        PreparedStatement statement = connection.prepareStatement("UPDATE characters SET vip=0, vip_end=0 WHERE charId=?");
                                        statement.setInt(1, _player.getObjectId());
                                        statement.execute();
                                        statement.close();
                                        connection.close();

                                        _player.getAppearance().setNameColor(0xFFFFFF);
                                        _player.getAppearance().setTitleColor(0xFFFFFF);
                                        _player.broadcastUserInfo();
                                        _player.sendPacket(new EtcStatusUpdate(_player));
                                        GmListTable.broadcastMessageToGMs("GM "+activeChar.getName()+" remove vip stat of player "+ _playername);
                        }
                        catch (Exception e)
                        {
                                        _log.log(Level.WARNING,"could not remove vip stats of char:", e);
                        }
                        finally
                        {
                                        try {
                                                        connection.close();
                                        } catch (SQLException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                        }
                        }
        }

        public String[] getAdminCommandList()
        {
                        return _adminCommands;
        }
}
