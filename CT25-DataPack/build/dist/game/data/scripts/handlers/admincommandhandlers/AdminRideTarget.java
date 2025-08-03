    /*
     * This program is free software: you can redistribute it and/or modify it under
     * the terms of the GNU General Public License as published by the Free Software
     * Foundation, either version 3 of the License, or (at your option) any later
     * version.
     *
     * This program is distributed in the hope that it will be useful, but WITHOUT
     * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
     * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
     * details.
     *
     * You should have received a copy of the GNU General Public License along with
     * this program. If not, see <http://www.gnu.org/licenses/>.
     */
    package handlers.admincommandhandlers;

    import com.l2jserver.gameserver.handler.IAdminCommandHandler;
    import com.l2jserver.gameserver.instancemanager.TransformationManager;
    import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
    import com.l2jserver.gameserver.model.L2Object;

    /**
     * @author
     */
    public class AdminRideTarget implements IAdminCommandHandler
    {
      private static final String[] ADMIN_COMMANDS =
      {
          "admin_ride_target_horse",
          "admin_ride_target_wyvern",
          "admin_ride_target_strider",
          "admin_unride_target"
      };
      private int _petRideId;
     
      private static final int PURPLE_MANED_HORSE_TRANSFORMATION_ID = 106;

      public boolean useAdminCommand(String command, L2PcInstance activeChar)
      {
          L2PcInstance player = null;
          L2Object target = activeChar.getTarget();
          if(target instanceof L2PcInstance)
            player = (L2PcInstance) target;
           
          if(player!=null)
          {
            if (command.startsWith("admin_ride_target"))
            {
                if (player.isMounted() || player.getPet() != null)
                {
                  activeChar.sendMessage("No puedes usar el comando sobre tu target en este momento");
                  return false;
                }
                if (command.startsWith("admin_ride_target_wyvern"))
                {
                  _petRideId = 12621;
                }
                else if (command.startsWith("admin_ride_target_strider"))
                {
                  _petRideId = 12526;
                }
                else if (command.startsWith("admin_ride_target_wolf"))
                {
                  _petRideId = 16041;
                }
                else if (command.startsWith("admin_ride_target_horse")) // handled using transformation
                {
                  if (player.isTransformed() || player.isInStance())
                      activeChar.sendMessage("No puedes usar el comando sobre tu target en este momento");
                  else
                      TransformationManager.getInstance().transformPlayer(PURPLE_MANED_HORSE_TRANSFORMATION_ID, player);
                 
                  return true;
                }
                else
                {
                  activeChar.sendMessage("Command '" + command + "' not recognized");
                  return false;
                }
               
                player.mount(_petRideId, 0, false);
               
                return false;
            }
            else if (command.startsWith("admin_unride_target"))
            {
                if (activeChar.getTransformationId() == PURPLE_MANED_HORSE_TRANSFORMATION_ID)
                  player.untransform();
                else
                  player.dismount();
            }
          } 
          else
            activeChar.sendMessage("No tiene a nadie targeteado.");
          return true;
      }
     
      public String[] getAdminCommandList()
      {
          return ADMIN_COMMANDS;
      }
     
    }