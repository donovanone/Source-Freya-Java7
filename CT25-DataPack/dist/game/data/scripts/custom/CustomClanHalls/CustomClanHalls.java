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
    package custom.CustomClanHalls;
    
    import com.l2jserver.Config;
    import com.l2jserver.gameserver.model.quest.Quest;
    
    /**
     * @author Gnacik
     */
    public class CustomClanHalls extends Quest
    {
           public CustomClanHalls(int id, String name, String descr)
           {
                  super(id, name, descr);
                  
                  if (Config.USE_CUSTOM_CLANHALLS)
                  {
                         _log.info("Custom ClanHalls active, spawning NPC's");
                         // Titanum Chamber
                         addSpawn(30772, 80634, 56474, -1560, 47997, false, 0);
                         addSpawn(30772, 80622, 56732, -1545, 16962, false, 0);
                         addSpawn(35441, 80769, 56740, -1545, 17358, false, 0);
                         
                         // Knights Chamber
                         addSpawn(30773, 83132, 56281, -1510, 14750, false, 0);
                         addSpawn(30773, 82993, 56135, -1525, 48920, false, 0);
                         addSpawn(35443, 82976, 56275, -1510, 16905, false, 0);
                         
                         // Phoenix Chamber
                         addSpawn(35440, 81618, 53136, -1483, 49151, false, 0);
                         addSpawn(35440, 81751, 53278, -1496, 15640, false, 0);
                         addSpawn(35451, 81775, 53138, -1483, 49700, false, 0);
                         
                         // Waterfall Hall
                         addSpawn(30772, 114260, 217223, -3550, 0, false, 0 );
                         addSpawn(30772, 114184, 217225, -3547, 32767, false, 0 );
                         addSpawn(35441, 113954, 217226, -3628, 32964, false, 0 );
                         
                         // Giants Hall
                         addSpawn(30773, 114107, 222400, -3547, 16011, false, 0 );
                         addSpawn(30773, 114044, 222490, -3547, 49151, false, 0 );
                         addSpawn(35443, 114047, 222708, -3626, 16058, false, 0 );
                         
                         // Earth Hall
                         addSpawn(35440, 108622, 222209, -3520, 16109, false, 0 );
                         addSpawn(35440, 108623, 222118, -3522, 48539, false, 0 );
                         addSpawn(35441, 108620, 222411, -3599, 17251, false, 0 );
                         
                         // Wenus Chamber
                         addSpawn(30772, 108635, 218169, -3646, 33807, false, 0 );
                         addSpawn(30772, 108885, 218007, -3645, 15344, false, 0 );
                         addSpawn(35443, 108967, 218009, -3645, 16818, false, 0 );
                         
                         // Saturn Chamber
                         addSpawn(30773, 107878, 220906, -3585, 17184, false, 0 );
                         addSpawn(30773, 107718, 220671, -3584, 65220, false, 0 );
                         addSpawn(35443, 107716, 220589, -3584,   193, false, 0 );
                         
                         // Hunters Hall
                         addSpawn(30772, 120792, 77066, -2129, 51707, false, 0 );
                         addSpawn(30772, 120753, 77157, -2143, 18809, false, 0 );
                         addSpawn(35441, 120931, 77111, -2128, 51052, false, 0 );
                         
                         // Forbidden Hall
                         addSpawn(30773, 119917, 78354, -1803,  7348, false, 0 );
                         addSpawn(30773, 119765, 78403, -1820, 36123, false, 0 );
                         addSpawn(35443, 119833, 78450, -1802,  7152, false, 0 );
                         
                         // Enchanted Hall
                         addSpawn(35440, 118983, 79624, -1612, 57343, false, 0 );
                         addSpawn(35440, 119025, 79784, -1596, 24027, false, 0 );
                         addSpawn(35441, 118931, 79701, -1596, 22723, false, 0 );
                         
                         // Lion Hall
                         addSpawn(30772, 17155, 169757, -3483, 46596, false, 0 );
                         addSpawn(30772, 17257, 169844, -3500, 15229, false, 0 );
                         addSpawn(35443, 17274, 169753, -3483, 49151, false, 0 );
                         
                         // Puma Hall
                         addSpawn(30773, 17883, 170530, -3504, 49400, false, 0 );
                         addSpawn(30773, 17982, 170638, -3488, 16699, false, 0 );
                         addSpawn(35443, 17874, 170637, -3488, 16383, false, 0 );
                  }
           }
    
           public static void main(String[] args)
           {
                  new CustomClanHalls(-1, "CustomClanHalls", "custom");
           }
    }