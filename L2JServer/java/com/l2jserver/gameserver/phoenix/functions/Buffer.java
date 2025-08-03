package com.l2jserver.gameserver.phoenix.functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.io.Out;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;
import com.l2jserver.gameserver.phoenix.models.ManagerNpcHtml;

/**
 * @author Rizel
 */
public class Buffer
{
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		static final Buffer _instance = new Buffer();
	}
	
	private class UpdateTask implements Runnable
	{
		@Override
		public void run()
		{
			updateSQL();
		}
	}
	
	public static Buffer getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final FastMap<String, FastList<Integer>> buffTemplates;
	
	private final FastMap<String, Boolean> changes;
	
	private final UpdateTask updateTask;
	
	@SuppressWarnings("synthetic-access")
	private Buffer()
	{
		updateTask = new UpdateTask();
		changes = new FastMap<>();
		buffTemplates = new FastMap<>();
		loadSQL();
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(updateTask, 600000, 600000);
	}
	
	public void buffPlayer(EventPlayer player)
	{
		String playerId = "" + player.getPlayersId() + player.getClassIndex();
		
		if (!buffTemplates.containsKey(playerId))
		{
			return;
		}
		
		for (int skillId : buffTemplates.get(playerId))
		{
			if (player.isInOlympiadMode())
			{
				return;
			}
			player.getEffects(skillId, 99);
		}
	}
	
	public void changeList(Integer player, int buff, boolean action)
	{
		String playerId = "" + player + L2World.getInstance().getPlayer(player).getClassIndex();
		// String playerId = "" + player + Out.getClassIndex(player);
		
		if (!buffTemplates.containsKey(playerId))
		{
			buffTemplates.put(playerId, new FastList<Integer>());
			changes.put(playerId, true);
		}
		else
		{
			if (!changes.containsKey(playerId))
			{
				changes.put(playerId, false);
			}
			if (action)
			{
				buffTemplates.get(playerId).add(buff);
			}
			else
			{
				buffTemplates.get(playerId).remove(buffTemplates.get(playerId).indexOf(buff));
			}
		}
	}
	
	private void loadSQL()
	{
		if (!Config.getInstance().getBoolean(0, "eventBufferEnabled"))
		{
			return;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT * FROM event_buffs"))
		{
			try (ResultSet rset = statement.executeQuery())
			{
				while (rset.next())
				{
					buffTemplates.put(rset.getString("player"), new FastList<Integer>());
					
					StringTokenizer st = new StringTokenizer(rset.getString("buffs"), ",");
					
					FastList<Integer> templist = new FastList<>();
					
					while (st.hasMoreTokens())
					{
						templist.add(Integer.parseInt(st.nextToken()));
					}
					
					buffTemplates.getEntry(rset.getString("player")).setValue(templist);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("EventBuffs SQL catch");
		}
	}
	
	public boolean playerHaveTemplate(Integer player)
	{
		String playerId = "" + player + L2World.getInstance().getPlayer(player).getClassIndex();
		// String playerId = "" + player + Out.getClassIndex(player);
		
		if (buffTemplates.containsKey(playerId))
		{
			return true;
		}
		return false;
	}
	
	public void showHtml(Integer player)
	{
		try
		{
			// String playerId = "" + player + Out.getClassIndex(player);
			String playerId = "" + player + L2World.getInstance().getPlayer(player).getClassIndex();
			
			if (!buffTemplates.containsKey(playerId))
			{
				buffTemplates.put(playerId, new FastList<Integer>());
				changes.put(playerId, true);
			}
			
			StringTokenizer st = new StringTokenizer(Config.getInstance().getString(0, "allowedBuffsList"), ",");
			
			FastList<Integer> skillList = new FastList<>();
			
			while (st.hasMoreTokens())
			{
				skillList.add(Integer.parseInt(st.nextToken()));
			}
			
			TextBuilder sb = new TextBuilder();
			
			sb.append("<center><table width=270 bgcolor=4f4f4f><tr><td width=70><font color=ac9775>Edit Buffs</font></td><td width=80></td><td width=120><font color=9f9f9f>Remaining slots:</font> <font color=ac9775>" + (Config.getInstance().getInt(0, "maxBuffNum") - buffTemplates.get(playerId).size()) + "</font></td></tr></table><br><br>");
			sb.append("<center><table width=270 bgcolor=4f4f4f><tr><td><font color=ac9775>Added buffs:</font></td></tr></table><br>");
			sb.append("<center><table width=270>");
			
			int c = 0;
			for (int skillId : buffTemplates.get(playerId))
			{
				c++;
				String skillStr = "0000";
				if (skillId < 100)
				{
					skillStr = "00" + skillId;
				}
				else if ((skillId > 99) && (skillId < 1000))
				{
					skillStr = "0" + skillId;
				}
				else if ((skillId > 4698) && (skillId < 4701))
				{
					skillStr = "1331";
				}
				else if ((skillId > 4701) && (skillId < 4704))
				{
					skillStr = "1332";
				}
				else
				{
					skillStr = "" + skillId;
				}
				
				if ((c % 2) == 1)
				{
					sb.append("<tr><td width=33><img src=\"Icon.skill" + skillStr + "\" width=32 height=32></td><td width=100><a action=\"bypass -h eventmanager buffer " + skillId + " 0\"><font color=9f9f9f>" + SkillTable.getInstance().getInfo(skillId, 1).getName() + "</font></a></td>");
				}
				if ((c % 2) == 0)
				{
					sb.append("<td width=33><img src=\"Icon.skill" + skillStr + "\" width=32 height=32></td><td width=100><a action=\"bypass -h eventmanager buffer " + skillId + " 0\"><font color=9f9f9f>" + SkillTable.getInstance().getInfo(skillId, 1).getName() + "</font></a></td></tr>");
				}
			}
			
			if ((c % 2) == 1)
			{
				sb.append("<td width=33></td><td width=100></td></tr>");
			}
			
			sb.append("</table><br>");
			
			sb.append("<br><br><center><table width=270 bgcolor=5A5A5A><tr><td><font color=ac9775>Available buffs:</font></td></tr></table><br>");
			sb.append("<center><table width=270>");
			
			c = 0;
			for (int skillId : skillList)
			{
				String skillStr = "0000";
				if (skillId < 100)
				{
					skillStr = "00" + skillId;
				}
				else if ((skillId > 99) && (skillId < 1000))
				{
					skillStr = "0" + skillId;
				}
				else if ((skillId > 4698) && (skillId < 4701))
				{
					skillStr = "1331";
				}
				else if ((skillId > 4701) && (skillId < 4704))
				{
					skillStr = "1332";
				}
				else
				{
					skillStr = "" + skillId;
				}
				
				if (!buffTemplates.get(playerId).contains(skillId))
				{
					c++;
					if ((c % 2) == 1)
					{
						sb.append("<tr><td width=32><img src=\"Icon.skill" + skillStr + "\" width=32 height=32></td><td width=100>" + ((Config.getInstance().getInt(0, "maxBuffNum") - buffTemplates.get(playerId).size()) != 0 ? "<a action=\"bypass -h eventmanager buffer " + skillId + " 1\"><font color=9f9f9f>" : "") + SkillTable.getInstance().getInfo(skillId, 1).getName() + ((Config.getInstance().getInt(0, "maxBuffNum") - buffTemplates.get(playerId).size()) != 0 ? "</font></a>" : "") + "</td>");
					}
					if ((c % 2) == 0)
					{
						sb.append("<td width=32><img src=\"Icon.skill" + skillStr + "\" width=32 height=32></td><td width=100>" + ((Config.getInstance().getInt(0, "maxBuffNum") - buffTemplates.get(playerId).size()) != 0 ? "<a action=\"bypass -h eventmanager buffer " + skillId + " 1\"><font color=9f9f9f>" : "") + SkillTable.getInstance().getInfo(skillId, 1).getName() + ((Config.getInstance().getInt(0, "maxBuffNum") - buffTemplates.get(playerId).size()) != 0 ? "</font></a>" : "") + "</td></tr>");
					}
				}
			}
			
			if ((c % 2) == 1)
			{
				sb.append("<td width=33></td><td width=100></td></tr>");
			}
			
			sb.append("</table>");
			Out.html(player, new ManagerNpcHtml(sb.toString()).string());
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public void updateSQL()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			for (Map.Entry<String, Boolean> player : changes.entrySet())
			{
				TextBuilder sb = new TextBuilder();
				
				int c = 0;
				for (int buffid : buffTemplates.get(player.getKey()))
				{
					if (c == 0)
					{
						sb.append(buffid);
						c++;
					}
					else
					{
						sb.append("," + buffid);
					}
				}
				
				if (player.getValue())
				{
					try (PreparedStatement statement = con.prepareStatement("INSERT INTO event_buffs(player,buffs) VALUES (?,?)"))
					{
						statement.setString(1, player.getKey());
						statement.setString(2, sb.toString());
						
						statement.executeUpdate();
					}
				}
				else
				{
					try (PreparedStatement statement = con.prepareStatement("UPDATE event_buffs SET buffs=? WHERE player=?"))
					{
						statement.setString(1, sb.toString());
						statement.setString(2, player.getKey());
						
						statement.executeUpdate();
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("EventBuffs SQL catch");
		}
		
		changes.clear();
	}
}