package com.l2jserver.gameserver.communitybbs.Manager;

import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import javolution.util.FastList;

import com.l2jserver.Config;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.communitybbs.Model.BaseBBSManager;
import com.l2jserver.gameserver.datatables.ClassListData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.instancemanager.TownManager;
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.L2ManufactureItem;
import com.l2jserver.gameserver.model.L2ManufactureList;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.templates.item.L2Item;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.Say2;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * TODO desactivado la vista de player en estado "manufacture" por un mal funcionamiento en la vista de sus items
 * @author fissban
 */
public class RegionBBSManager extends BaseBBSManager
{
	// Gludio
	private final static List<String> _onlineListGludio = new FastList<>();
	private final static List<String> _onlineListGludio_Sell = new FastList<>();
	private final static List<String> _onlineListGludio_SellBuff = new FastList<>();
	private final static List<String> _onlineListGludio_Buy = new FastList<>();
	// private final static List<String> _onlineListGludio_Manu = new FastList<>();
	// Dion
	private final static List<String> _onlineListDion = new FastList<>();
	private final static List<String> _onlineListDion_Sell = new FastList<>();
	private final static List<String> _onlineListDion_SellBuff = new FastList<>();
	private final static List<String> _onlineListDion_Buy = new FastList<>();
	// private final static List<String> _onlineListDion_Manu = new FastList<>();
	// Giran
	private final static List<String> _onlineListGiran = new FastList<>();
	private final static List<String> _onlineListGiran_Sell = new FastList<>();
	private final static List<String> _onlineListGiran_SellBuff = new FastList<>();
	private final static List<String> _onlineListGiran_Buy = new FastList<>();
	// private final static List<String> _onlineListGiran_Manu = new FastList<>();
	// Oren
	private final static List<String> _onlineListOren = new FastList<>();
	private final static List<String> _onlineListOren_Sell = new FastList<>();
	private final static List<String> _onlineListOren_SellBuff = new FastList<>();
	private final static List<String> _onlineListOren_Buy = new FastList<>();
	// private final static List<String> _onlineListOren_Manu = new FastList<>();
	// Aden
	private final static List<String> _onlineListAden = new FastList<>();
	private final static List<String> _onlineListAden_Sell = new FastList<>();
	private final static List<String> _onlineListAden_SellBuff = new FastList<>();
	private final static List<String> _onlineListAden_Buy = new FastList<>();
	// private final static List<String> _onlineListAden_Manu = new FastList<>();
	// Heine
	private final static List<String> _onlineListHeine = new FastList<>();
	private final static List<String> _onlineListHeine_Sell = new FastList<>();
	private final static List<String> _onlineListHeine_SellBuff = new FastList<>();
	private final static List<String> _onlineListHeine_Buy = new FastList<>();
	// private final static List<String> _onlineListHeine_Manu = new FastList<>();
	// Goddard
	private final static List<String> _onlineListGoddard = new FastList<>();
	private final static List<String> _onlineListGoddard_Sell = new FastList<>();
	private final static List<String> _onlineListGoddard_SellBuff = new FastList<>();
	private final static List<String> _onlineListGoddard_Buy = new FastList<>();
	// private final static List<String> _onlineListGoddard_Manu = new FastList<>();
	// Township
	private final static List<String> _onlineListTownship = new FastList<>();
	private final static List<String> _onlineListTownship_Sell = new FastList<>();
	private final static List<String> _onlineListTownship_SellBuff = new FastList<>();
	private final static List<String> _onlineListTownship_Buy = new FastList<>();
	// private final static List<String> _onlineListTownship_Manu = new FastList<>();
	// Schuttgart
	private final static List<String> _onlineListSchuttgart = new FastList<>();
	private final static List<String> _onlineListSchuttgart_Sell = new FastList<>();
	private final static List<String> _onlineListSchuttgart_SellBuff = new FastList<>();
	private final static List<String> _onlineListSchuttgart_Buy = new FastList<>();
	// private final static List<String> _onlineListSchuttgart_Manu = new FastList<>();
	
	// Variables para todos los players y los gms
	private final static List<String> _onlineList = new FastList<>();
	private final static List<String> _onlineListGm = new FastList<>();
	
	private final static String[] name_city =
	{
		"L2UI_CT1.EventBtnWnd_DF_PresentRankBtn",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Gludio",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Dion",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Giran",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Oren",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Aden",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Innadril",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Goddard",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Rune",
		"L2UI_CT1.clan_DF_TerritoryWarIcon_Schuttgart"
	};
	
	private final static String[] coloresRGB =
	{
		"FF0000",// fire
		"0080FF",// water
		"9AC3C0",// wind
		"724E2B",// earth
		"FFFFFF",// holy
		"B50EE8",// dark
	};
	
	private final static String[] coloresName =
	{
		"Fire",
		"Water",
		"Wind",
		"Earth",
		"Holy",
		"Dark",
	};
	
	private final static String[] coloresImg =
	{
		"Gauge_DF_Attribute_Fire",
		"Gauge_DF_Attribute_Water",
		"Gauge_DF_Attribute_Wind",
		"Gauge_DF_Attribute_Earth",
		"Gauge_DF_Attribute_Divine",
		"Gauge_DF_Attribute_Dark",
	};
	
	@Override
	public void parseCmd(String command, L2PcInstance activeChar)
	{
		if (command.equals("_bbsloc"))
		{
			String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/custom/region/index.htm");
			if (content == null)
			{
				content = "<html><body><br><br><center>404 :File not found: 'data/html/CommunityBoard/custom/region/index.htm' </center></body></html>";
				separateAndSend(content, activeChar);
				return;
			}
			
	
			content = content.replaceAll("%gm%", String.valueOf(_onlineListGm.size()));
			content = content.replaceAll("%gludio%", String.valueOf(_onlineListGludio.size()));
			content = content.replaceAll("%dion%", String.valueOf(_onlineListDion.size()));
			content = content.replaceAll("%giran%", String.valueOf(_onlineListGiran.size()));
			content = content.replaceAll("%oren%", String.valueOf(_onlineListOren.size()));
			content = content.replaceAll("%aden%", String.valueOf(_onlineListAden.size()));
			content = content.replaceAll("%heine%", String.valueOf(_onlineListHeine.size()));
			content = content.replaceAll("%goddard%", String.valueOf(_onlineListGoddard.size()));
			content = content.replaceAll("%township%", String.valueOf(_onlineListTownship.size()));
			content = content.replaceAll("%schuttgart%", String.valueOf(_onlineListSchuttgart.size()));
			
			separateAndSend(content, activeChar);
		}
		else if (command.startsWith("_bbsloc"))
		{
			final StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();// _bbsloc
			final String idp = st.nextToken();
			
			String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/custom/region/" + idp + ".htm");
			
			if (content == null)
			{
				content = "<html><body><br><br><center>Content Empty: The command " + command + " points to an invalid or empty html file(" + idp + ".htm).</center></body></html>";
				separateAndSend(content, activeChar);
				return;
			}
			
			if (idp.equals("town"))
			{
				if (st.hasMoreTokens())
				{
					final String town = st.nextToken();
					content = content.replaceAll("%current_header%", generateHeader(town));
					if (town.equals("all"))
					{
						content = content.replaceAll("%current_body%", "");
					}
					else
					{
						content = content.replaceAll("%current_body%", generateBody(town));
					}
					
					if (st.hasMoreTokens())
					{
						final int page = Integer.valueOf(st.nextToken());
						content = content.replaceAll("%current_footer%", generateFooter(town, page));
					}
					else
					{
						content = content.replaceAll("%current_footer%", generateFooter(town, 1));
					}
				}
				else
				{
					content = content.replaceAll("%current_header%", "");
					content = content.replaceAll("%current_body%", "");
					content = content.replaceAll("%current_footer%", "");
				}
			}
			else if (idp.equals("private"))
			{
				final String target = st.nextToken();
				
				content = content.replaceAll("%current_all%", Private(target));
				// content = content.replaceAll("%search_player%", Search(target));
			}
			else if (idp.equals("buy"))
			{
				final String target = st.nextToken();
				
				content = content.replaceAll("%current_all%", Buy(target));
			}
			else if (idp.equals("sell"))
			{
				final String target = st.nextToken();
				
				content = content.replaceAll("%current_all%", Sell(target));
			}
			// else if (idp.equals("manu"))
			// {
			// final String target = st.nextToken();
			//
			// content = content.replaceAll("%current_all%", Manufacture(target));
			// }
			else if (idp.equals("search"))
			{
				final String name = st.nextToken();
				
				final L2PcInstance player = L2World.getInstance().getPlayer(name);
				
				if (player == null)
				{
					content = content.replaceAll("%current_all%", NotExist(name));
				}
				else
				{
					activeChar.getRadar().addMarker(player.getX(), player.getY(), player.getZ());
					content = content.replaceAll("%current_all%", Search(name));
				}
			}
			separateAndSend(content, activeChar);
		}
	}
	
	/**
	 * Generamos el html luego de ejecutar la busqueda de un player en el mapa
	 * @param name
	 * @return
	 */
	private static String Search(String name)
	{
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=300 HEIGHT=32 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=32 align=center valign=top><img src=L2UI_CH3.partymatchicon WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=80 align=center><font color=E95600>Just follow the arrow and find " + name + " store</font></td>");
		tb.append("</tr>");
		tb.append("</table>");
		return tb.toString();
	}
	
	/**
	 * Generamos el html por si no se encuentra un player<br>
	 * Usado por si un usuario se desconecta y la lista aun no actualiza al ser buscado por otro player
	 * @param name
	 * @return
	 */
	private static String NotExist(String name)
	{
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=755 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right><img src=L2UI_CH3.PremiumItemBtn_Over WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=480 align=left><font color=E95600> the user " + name + "does not exist or offline" + "</font></td>");
		tb.append("<td FIXWIDTH=400 align=center><button value=\"\" action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		return tb.toString();
	}
	
	@SuppressWarnings("unused")
	private static String Manufacture(String name)
	{
		final L2PcInstance player = L2World.getInstance().getPlayer(name);
		
		if (player == null)
		{
			return NotExist(name);
		}
		
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=755 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right><img src=L2UI_CH3.PremiumItemBtn_Over WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=480 align=left><font color=E95600> Manufacture items from: </font><font color=AAAAAA>" + name + "</font></td>");
		tb.append("<td FIXWIDTH=400 align=center><button value=\"\" action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=192 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=16 HEIGHT=16 align=center><img src=L2UI_CH3.partymatchbutton_down WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=100 HEIGHT=16 align=left><a action=\"bypass _bbsloc;search;" + player.getName() + "\">Search Player</a></td>");
		tb.append("<td FIXWIDTH=16 HEIGHT=16 align=center><img src=L2UI_CH3.msnicon11 WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=60 HEIGHT=16 align=left><a action=\"bypass _bbsloc;private;" + player.getName() + "\">Send PM</a></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br><br>");
		
		L2ManufactureList createList = player.getCreateList();
		L2ManufactureItem item;
		
		int color = 0;
		for (int i = 0; i < createList.size(); i++)
		{
			item = createList.getList().get(i);
			L2Item useItem = ItemTable.getInstance().getTemplate(item.getRecipeId());
			
			tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=400 HEIGHT=48 bgcolor=" + ColorTable(color) + ">");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=40 HEIGHT=96 align=center valign=center><img src=" + useItem.getIcon() + " WIDTH=32 HEIGHT=32></td>");
			tb.append("<td FIXWIDTH=360 align=center valign=center>");
			tb.append("<table border=0 cellspacing=1 cellpadding=2 WIDTH=360 HEIGHT=48>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center>");
			switch (useItem.getCrystalType())
			{
				case 0:
					tb.append("<img src=L2UI_CH3.joypad_shortcut WIDTH=16 HEIGHT=16>");
					break;
				case 1:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_D WIDTH=16 HEIGHT=16>");
					break;
				case 2:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_C WIDTH=16 HEIGHT=16>");
					break;
				case 3:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_B WIDTH=16 HEIGHT=16>");
					break;
				case 4:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_A WIDTH=16 HEIGHT=16>");
					break;
				case 5:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_S WIDTH=16 HEIGHT=16>");
					break;
				case 6:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_80 WIDTH=16 HEIGHT=16>");
					break;
				case 7:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_84 WIDTH=16 HEIGHT=16>");
					break;
			}
			tb.append("</td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center><font color=99FF00>" + useItem.getName() + "</font></td>");
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Price: <font color=99FF00>" + colorPrice(item.getCost()) + "</font> Adena's</td>");
			tb.append("</tr>");
			tb.append("</table>");
			tb.append("</td>");
			tb.append("</tr>");
			tb.append("</table>");
		}
		
		return tb.toString();
	}
	
	/**
	 * Generamos el html para visualizar los items q se compran
	 * @param name
	 * @return
	 */
	private static String Sell(String name)
	{
		final L2PcInstance player = L2World.getInstance().getPlayer(name);
		
		if (player == null)
		{
			return NotExist(name);
		}
		
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=755 bgcolor=111111 HEIGHT=40>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right><img src=L2UI_CH3.PremiumItemBtn_Over WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=480 align=left><font color=E95600> Sell items from: </font><font color=AAAAAA>" + name + "</font></td>");
		tb.append("<td FIXWIDTH=400 align=center><button value=\"\"  action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=192 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=16 align=center><img src=L2UI_CH3.partymatchbutton_down WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=100 align=left><a action=\"bypass _bbsloc;search;" + player.getName() + "\">Search Player</a></td>");
		tb.append("<td FIXWIDTH=16 HEIGHT=16 align=center><img src=L2UI_CH3.msnicon11 WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=60 align=left><a action=\"bypass _bbsloc;private;" + player.getName() + "\">Send PM</a></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br><br>");
		
		List<Integer> itemsRepetidosId = new FastList<>();
		List<Integer> itemsRepetidosCount = new FastList<>();
		
		int itemId = 0;
		int count = 1;
		
		final L2ItemInstance[] _itemsList = player.getInventory().getUniqueItems(false, true);
		final com.l2jserver.gameserver.model.TradeList.TradeItem[] _sellList = player.getSellList().getItems();
		
		for (com.l2jserver.gameserver.model.TradeList.TradeItem item : _sellList)
		{
			if (itemId == item.getItem().getDisplayId())
			{
				count++;
			}
			else if (count > 1)
			{
				itemsRepetidosId.add(itemId);
				itemsRepetidosCount.add(count);
				count = 1;
			}
			else
			{
				itemId = item.getItem().getDisplayId();
			}
		}
		
		if (count > 1)
		{
			itemsRepetidosId.add(itemId);
			itemsRepetidosCount.add(count);
		}
		
		int cont = 0;
		int countRep = 1;
		boolean repetido = false;
		int contSell = 0;
		
		int color = 0;
		for (com.l2jserver.gameserver.model.TradeList.TradeItem item : _sellList)
		{
			L2ItemInstance sellItem = null;
			final L2Item useItem = ItemTable.getInstance().getTemplate(item.getItem().getDisplayId());
			
			for (L2ItemInstance searchItem : _itemsList)
			{
				if (searchItem.getItemId() == item.getItem().getDisplayId())
				{
					sellItem = searchItem;
					break;
				}
			}
			
			if (cont != 0)
			{
				if (repetido && (countRep <= itemsRepetidosId.get(cont - 1)))
				{
					countRep++;
					if (contSell >= _sellList.length)
					{
						break;
					}
					continue;
				}
				
				repetido = false;
				countRep = 0;
			}
			
			if (!itemsRepetidosId.isEmpty() && (useItem.getId() == itemsRepetidosId.get(cont)))
			{
				repetido = true;
			}
			
			contSell++;
			
			tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=550 HEIGHT=96 bgcolor=" + ColorTable(color) + ">");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=40 align=center valign=center><img src=" + useItem.getIcon() + " WIDTH=32 HEIGHT=32></td>");
			tb.append("<td FIXWIDTH=360 align=center valign=center>");
			tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=360 HEIGHT=96>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left>");
			
			switch (useItem.getCrystalType())
			{
				case 0:
					tb.append("<img src=L2UI_CH3.joypad_shortcut WIDTH=16 HEIGHT=16>");
					break;
				case 1:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_D WIDTH=16 HEIGHT=16>");
					break;
				case 2:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_C WIDTH=16 HEIGHT=16>");
					break;
				case 3:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_B WIDTH=16 HEIGHT=16>");
					break;
				case 4:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_A WIDTH=16 HEIGHT=16>");
					break;
				case 5:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_S WIDTH=16 HEIGHT=16>");
					break;
				case 6:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_80 WIDTH=16 HEIGHT=16>");
					break;
				case 7:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_84 WIDTH=16 HEIGHT=16>");
					break;
			}
			
			tb.append("</td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center><font color=99FF00>" + useItem.getName() + "</font></td>");
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			if (repetido)
			{
				tb.append("<td FIXWIDTH=344 HEIGHT=16 align=left valign=center>Buy Items: <font color=99FF00>" + itemsRepetidosCount.get(cont) + "</font></td>");
				cont++;
			}
			else
			{
				tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Buy Item: <font color=99FF00>1</font></td>");
			}
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Price: <font color=99FF00>" + colorPrice(item.getPrice()) + "</font> Adena's</td>");
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Enchant Lvl: <font color=99FF00>+ " + item.getEnchant() + "</font></td>");
			tb.append("</tr>");
			tb.append("</table>");
			tb.append("</td>");
			tb.append("<td>");
			tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=150 HEIGHT=96>");
			
			if (sellItem.isArmor())
			{
				for (byte i = 0; i < 6; i++)
				{
					tb.append("<tr>");
					tb.append("<td FIXWIDTH=16 HEIGHT=16 align=left valign=center><img src=L2UI_CT1." + coloresImg[i] + " WIDTH=16 HEIGHT=16></td>");
					tb.append("<td FIXWIDTH=100 HEIGHT=16 align=left valign=center><font color=" + coloresRGB[i] + ">Element: " + coloresName[i] + "</font></td>");
					tb.append("<td FIXWIDTH=34 HEIGHT=16 align=center valign=center><font color=" + coloresRGB[i] + ">" + sellItem.getElementDefAttr(i) + "</font></td>");
					tb.append("</tr>");
				}
			}
			else if (sellItem.isWeapon())
			{
				for (byte i = 0; i < 6; i++)
				{
					if (item.getAttackElementType() == i)
					{
						tb.append("<tr>");
						tb.append("<td FIXWIDTH=16 HEIGHT=16 align=left valign=center><img src=L2UI_CT1." + coloresImg[i] + " WIDTH=16 HEIGHT=16></td>");
						tb.append("<td FIXWIDTH=100 HEIGHT=16 align=left valign=center><font color=" + coloresRGB[i] + ">Element: " + coloresName[i] + "</font></td>");
						tb.append("<td FIXWIDTH=34 HEIGHT=16 align=center valign=center><font color=" + coloresRGB[i] + ">" + sellItem.getAttackElementPower() + "</font></td>");
						tb.append("</tr>");
					}
					else
					{// TODO necesario ?
						tb.append("<tr>");
						tb.append("<td FIXWIDTH=16 HEIGHT=16 align=left valign=center>");
						tb.append("<img src=L2UI_CT1." + coloresImg[i] + " WIDTH=16 HEIGHT=16>");
						tb.append("</td>");
						tb.append("<td FIXWIDTH=100 HEIGHT=16 align=left valign=center>");
						tb.append("<font color=" + coloresRGB[i] + ">Element: " + coloresName[i] + "</font>");
						tb.append("</td>");
						tb.append("<td FIXWIDTH=34 HEIGHT=16 align=center valign=center>");
						tb.append("<font color=" + coloresRGB[i] + "> 0 </font>");
						tb.append("</td>");
						tb.append("</tr>");
					}
				}
			}
			else
			{
				for (byte i = 0; i < 6; i++)
				{
					tb.append("<tr>");
					tb.append("<td FIXWIDTH=16 HEIGHT=16 align=left valign=center><img src=L2UI_CT1." + coloresImg[i] + " WIDTH=16 HEIGHT=16></td>");
					tb.append("<td FIXWIDTH=100 HEIGHT=16 align=left valign=center><font color=" + coloresRGB[i] + ">Element: " + coloresName[i] + "</font></td>");
					tb.append("<td FIXWIDTH=34 HEIGHT=16 align=center valign=center><font color=" + coloresRGB[i] + "> - </font></td>");
					tb.append("</tr>");
				}
			}
			tb.append("</table>");
			tb.append("</td>");
			tb.append("</tr>");
			tb.append("</table>");
			color++;
		}
		
		return tb.toString();
	}
	
	/**
	 * Generamos el html para visualizar los items q se venden
	 * @param name
	 * @return String
	 */
	private static String Buy(String name)
	{
		final L2PcInstance player = L2World.getInstance().getPlayer(name);
		
		if (player == null)
		{
			return NotExist(name);
		}
		
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=755 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right><img src=L2UI_CH3.PremiumItemBtn_Over WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=480 align=left><font color=E95600> Buy items from: </font><font color=AAAAAA>" + name + "</font>");
		tb.append("<td FIXWIDTH=400 align=center><button value=\"\" action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=192 HEIGHT=40 bgcolor=111111>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=16 align=center><img src=L2UI_CH3.partymatchbutton_down WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=100 align=left><a action=\"bypass _bbsloc;search;" + player.getName() + "\">Search Player</a></td>");
		tb.append("<td FIXWIDTH=16 align=center><img src=L2UI_CH3.msnicon11 WIDTH=16 HEIGHT=16></td>");
		tb.append("<td FIXWIDTH=60 align=left><a action=\"bypass _bbsloc;private;" + player.getName() + "\">Send PM</a></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br><br>");
		
		final List<Integer> itemsRepetidosId = new FastList<>();
		final List<Integer> itemsRepetidosCount = new FastList<>();
		
		int itemId = 0;
		int count = 1;
		
		final com.l2jserver.gameserver.model.TradeList.TradeItem[] _buyList = player.getBuyList().getAvailableItems(player.getInventory());
		
		for (com.l2jserver.gameserver.model.TradeList.TradeItem item : _buyList)
		{
			if (itemId == item.getItem().getDisplayId())
			{
				count++;
			}
			else if (count > 1)
			{
				itemsRepetidosId.add(itemId);
				itemsRepetidosCount.add(count);
				count = 1;
			}
			else
			{
				itemId = item.getItem().getDisplayId();
			}
		}
		
		if (count > 1)
		{
			itemsRepetidosId.add(itemId);
			itemsRepetidosCount.add(count);
		}
		
		int cont = 0;
		int countRep = 1;
		boolean repetido = false;
		int contBuy = 0;
		
		int color = 0;
		
		for (com.l2jserver.gameserver.model.TradeList.TradeItem item : _buyList)
		{
			L2Item useItem = ItemTable.getInstance().getTemplate(item.getItem().getDisplayId());
			
			if (cont != 0)
			{
				if (repetido && (countRep <= itemsRepetidosId.get(cont - 1)))
				{
					countRep++;
					if (contBuy >= _buyList.length)
					{
						break;
					}
					continue;
				}
				repetido = false;
				countRep = 0;
			}
			
			if (!itemsRepetidosId.isEmpty() && (useItem.getId() == itemsRepetidosId.get(cont)))
			{
				repetido = true;
			}
			
			contBuy++;
			
			tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=400 HEIGHT=96 bgcolor=" + ColorTable(color) + ">");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=40 align=center valign=center><img src=" + useItem.getIcon() + " WIDTH=32 HEIGHT=32></td>");
			tb.append("<td FIXWIDTH=368 align=center valign=center>");
			tb.append("<table border=0 cellspacing=0 cellpadding=0 WIDTH=360 HEIGHT=96>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center>");
			
			switch (useItem.getCrystalType())
			{
				case 0:
					tb.append("<img src=L2UI_CH3.joypad_shortcut WIDTH=16 HEIGHT=16>");
					break;
				case 1:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_D WIDTH=16 HEIGHT=16>");
					break;
				case 2:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_C WIDTH=16 HEIGHT=16>");
					break;
				case 3:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_B WIDTH=16 HEIGHT=16>");
					break;
				case 4:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_A WIDTH=16 HEIGHT=16>");
					break;
				case 5:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_S WIDTH=16 HEIGHT=16>");
					break;
				case 6:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_80 WIDTH=16 HEIGHT=16>");
					break;
				case 7:
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_84 WIDTH=16 HEIGHT=16>");
					break;
			}
			
			tb.append("</td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center><font color=99FF00>" + useItem.getName() + "</font></td>");
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=14 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			
			if (repetido)
			{
				tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Buy Items: <font color=99FF00>" + itemsRepetidosCount.get(cont) + "</font></td>");
				cont++;
			}
			else
			{
				tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Buy Item: <font color=99FF00> 1</font></td>");
			}
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			tb.append("<td FIXWIDTH=344 align=left valign=center>Price: <font color=99FF00>" + colorPrice(item.getPrice()) + "</font> Adena's</td>");
			tb.append("</tr>");
			tb.append("<tr>");
			tb.append("<td FIXWIDTH=16 HEIGHT=24 align=left valign=center><img src=L2UI_CT1.Chatwindow_DF_ItemInfoIcon_Over WIDTH=16 HEIGHT=16></td>");
			tb.append("<td FIXWIDTH=344 HEIGHT=24 align=left valign=center>Enchant Lvl: <font color=99FF00>+ " + item.getEnchant() + "</font></td>");
			tb.append("</tr>");
			tb.append("</table>");
			tb.append("</td>");
			tb.append("</tr>");
			tb.append("</table>");
			
			color++;
		}
		return tb.toString();
	}
	
	private static String Private(String name)
	{
		final L2PcInstance player = L2World.getInstance().getPlayer(name);
		
		if (player == null)
		{
			return NotExist(name);
		}
		
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=2 WIDTH=755 bgcolor=111111 HEIGHT=40>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right><img src=L2UI_CH3.mailbtn_over WIDTH=32 HEIGHT=32></td>");
		tb.append("<td FIXWIDTH=480 align=left><font color=E95600>Send Private Message to : </font>" + player.getName() + "</td>");
		tb.append("<td FIXWIDTH=400 align=center><button value=\"\"  action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		tb.append("<multiedit var=pm WIDTH=400 HEIGHT=48>");
		tb.append("<button value=Send action=\"Write Region PM " + player.getName() + " pm pm pm\" WIDTH=200 HEIGHT=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>");
		return tb.toString();
	}
	
	private static String generateHeader(String town)
	{
		if (town.endsWith("_Sell"))
		{
			town = town.replace("_Sell", "");
		}
		else if (town.endsWith("_Buy"))
		{
			town = town.replace("_Buy", "");
		}
		else if (town.endsWith("_Manu"))
		{
			town = town.replace("_Manu", "");
		}
		else if (town.endsWith("_SellBuff"))
		{
			town = town.replace("_SellBuff", "");
		}
		else if (town.endsWith("_all"))
		{
			town = town.replace("_all", "");
		}
		
		TextBuilder tb = new TextBuilder();
		tb.append("<table border=0 cellspacing=1 cellpadding=2 WIDTH=755 bgcolor=111111 HEIGHT=40>");
		tb.append("<tr>");
		tb.append("<td FIXWIDTH=100 align=right>");
		switch (town)
		{
			case "all":
				tb.append("<img src=" + name_city[0] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Gludio":
				tb.append("<img src=" + name_city[1] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Dion":
				tb.append("<img src=" + name_city[2] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Giran":
				tb.append("<img src=" + name_city[3] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Oren":
				tb.append("<img src=" + name_city[4] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Aden":
				tb.append("<img src=" + name_city[5] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Heine":
				tb.append("<img src=" + name_city[6] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Goddard":
				tb.append("<img src=" + name_city[7] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Township":
				tb.append("<img src=" + name_city[8] + " WIDTH=32 HEIGHT=32>");
				break;
			case "Schuttgart":
				tb.append("<img src=" + name_city[9] + " WIDTH=32 HEIGHT=32>");
				break;
		}
		tb.append("</td>");
		if (town.equals("all"))
		{
			tb.append("<td FIXWIDTH=480 align=left><font color=E95600>All Users</font><br1><font color=AAAAAA>User Online " + _onlineList.size() + "</font></td>");
		}
		else
		{
			tb.append("<td FIXWIDTH=480 align=left><font color=E95600>Town of " + town + " </font><br1><font color=AAAAAA>List of users in this region</font></td>");
		}
		tb.append("<td FIXWIDTH=160 align=center><button value=\"\" action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("<br>");
		return tb.toString();
	}
	
	private static String generateBody(String town)
	{
		if (town.endsWith("_Sell"))
		{
			town = town.replace("_Sell", "");
		}
		else if (town.endsWith("_Buy"))
		{
			town = town.replace("_Buy", "");
		}
		else if (town.endsWith("_Manu"))
		{
			town = town.replace("_Manu", "");
		}
		else if (town.endsWith("_SellBuff"))
		{
			town = town.replace("_SellBuff", "");
		}
		else if (town.endsWith("_all"))
		{
			town = town.replace("_all", "");
		}
		
		TextBuilder _body = new TextBuilder();
		_body.append("<table border=0 cellspacing=1 cellpadding=2 HEIGHT=50>");
		_body.append("<tr>");
		_body.append("<td><button action=\"bypass _bbsloc;town;" + town + "\" value=All WIDTH=100 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		_body.append("<td><button action=\"bypass _bbsloc;town;" + town + "_Sell\" value=Sell WIDTH=100 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		_body.append("<td><button action=\"bypass _bbsloc;town;" + town + "_Buy\" value=Buy WIDTH=100 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		// FIXME
		// _body.append("<td><button action=\"bypass _bbsloc;town;" + town + "_Manu\" value=Manufacture WIDTH=100 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		if (Config.SELL_BUFF_ENABLED)
		{
			_body.append("<td><button action=\"bypass _bbsloc;town;" + town + "_SellBuff\" value=SellBuff WIDTH=100 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		}
		_body.append("</tr>");
		_body.append("</table>");
		return _body.toString();
	}
	
	private static String GenerateFooterHtml(List<String> list, int page, String town, String state)
	{
		final int limit = 20;
		final int contadorFinal = limit * page;
		final int contadorInicial = contadorFinal - (limit);
		
		TextBuilder tb = new TextBuilder();
		tb.append("<br>");
		
		int color = 0;
		for (int cont = contadorInicial; (cont < contadorFinal) && (cont != list.size()); cont++)
		{
			final L2PcInstance player = L2World.getInstance().getPlayer(list.get(cont));
			
			if (player == null)
			{
				continue;
			}
			
			// prevent null point
			if ((state.equals("buy") || state.equals("sell")) && (player.getPrivateStoreType() == 0))
			{
				continue;
			}
			
			if (state.equals("sellbuff") && !player.isSellBuff())
			{
				continue;
			}
			
			if (player.isGM() && player.getAppearance().getInvisible())
			{
				continue;
			}
			
			if (player.isInJail())
			{
				continue;
			}
			
			tb.append("<table border=0 cellspacing=1 cellpadding=1 WIDTH=700 HEIGHT=16 bgcolor=" + ColorTable(color) + ">");
			tb.append("<tr>");
			tb.append("<td align=center FIXWIDTH=16>");
			if (player.isGM())
			{
				tb.append("<img src=L2UI.bbs_gm WIDTH=15 HEIGHT=15>");
				tb.append("</td>");
				tb.append("<td align=left FIXWIDTH=80>");
				tb.append("<font color=LEVEL>" + player.getName() + "</font>");
			}
			else
			{
				if (player.getLevel() >= 84)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_84 WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 80)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_80 WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 76)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_S WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 61)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_A WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 52)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_B WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 40)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_C WIDTH=16 HEIGHT=16>");
				}
				else if (player.getLevel() >= 20)
				{
					tb.append("<img src=L2UI_CT1.Icon_DF_ItemGrade_D WIDTH=16 HEIGHT=16>");
				}
				else
				{
					tb.append("<img src=L2UI_CH3.aboutotpicon WIDTH=16 HEIGHT=16>");
				}
				
				tb.append("</td>");
				tb.append("<td align=left FIXWIDTH=80>");
				tb.append(player.getName());
			}
			
			tb.append("</td>");
			tb.append("<td align=center FIXWIDTH=16><img src=L2UI_CH3.msnicon11 WIDTH=16 HEIGHT=16></td>");
			tb.append("<td align=left FIXWIDTH=80><a action=\"bypass _bbsloc;private;" + player.getName() + "\">Send PM</a></td>");
			
			if (player.getAppearance().getSex())
			{
				tb.append("<td align=center FIXWIDTH=16><img src=L2UI_CH3.chatting_msn4_over WIDTH=16 HEIGHT=16></td>");
			}
			else
			{
				tb.append("<td align=center FIXWIDTH=16><img src=L2UI_CH3.chatting_msn1_over WIDTH=16 HEIGHT=16></td>");
			}
			if (player.getAppearance().getSex())
			{
				tb.append("<td align=left FIXWIDTH=80><font color=FF4040>Female</font></td>");
			}
			else
			{
				tb.append("<td align=left FIXWIDTH=80><font color=6161FF>Male</font></td>");
			}
			
			if (state.equals("buy") || state.equals("sell")/** || state.equals("manu") */
			)
			{
				tb.append("<td align=center FIXWIDTH=16><img src=L2UI_CH3.QuestWndInfoIcon_1 WIDTH=16 HEIGHT=16></td>");
				tb.append("<td align=left FIXWIDTH=80><a action=\"bypass _bbsloc;" + state + ";" + player.getName() + "\">View Item(s)</a></td>");
			}
			
			if (state.equals("buy"))
			{
				if (player.getBuyList().getTitle() != null)
				{
					tb.append("<td align=center FIXWIDTH=141>" + player.getBuyList().getTitle() + "</td>");
				}
				else
				{
					tb.append("<td align=center FIXWIDTH=141>No Message</td>");
				}
				tb.append("<td align=center FIXWIDTH=1></td>");
			}
			else if (state.equals("sell"))
			{
				if (player.getSellList().getTitle() != null)
				{
					tb.append("<td align=center FIXWIDTH=141>" + player.getSellList().getTitle() + "</td>");
				}
				else
				{
					tb.append("<td align=center FIXWIDTH=141>No Message</td>");
				}
				
				tb.append("<td align=center FIXWIDTH=1></td>");
			}
			// else if (state.equals("manu"))
			// {
			// tb.append("<td align=center FIXWIDTH=141>");
			// if (player.getCreateList().getStoreName() != null)
			// {
			// tb.append("<center>" + player.getCreateList().getStoreName() + "</center>");
			// }
			// else
			// {
			// tb.append("<center>No Message</center>");
			// }
			// tb.append("</td>");
			// tb.append("<td align=center FIXWIDTH=1>");
			// tb.append("");
			// tb.append("</td>");
			// }
			else if (state.equals("sellbuff"))
			{
				tb.append("<td align=center FIXWIDTH=16><img src=L2UI_CH3.partymatchbutton_down WIDTH=16 HEIGHT=16></td>");
				tb.append("<td align=left FIXWIDTH=80><a action=\"bypass _bbsloc;search;" + player.getName() + "\">Search Player</a></td>");
				tb.append("<td align=center FIXWIDTH=80>Class: " + ClassListData.getInstance().getClass(player.getClassId()).getClassName() + "</td>");
				tb.append("<td align=center FIXWIDTH=41>Lvl: " + player.getLevel() + "</td>");
			}
			else
			{
				if (player.getClan() != null)
				{
					tb.append("<td align=center FIXWIDTH=80>Clan: " + player.getClan().getName() + "</td>");
					if (player.getClan().getAllyId() > 0)
					{
						tb.append("<td align=center FIXWIDTH=80>Ally: " + player.getClan().getAllyName() + "</td>");
					}
					else
					{
						tb.append("<td align=center FIXWIDTH=80>Not in Ally</td>");
					}
				}
				else
				{
					tb.append("<td align=center FIXWIDTH=80>Not in Clan</td>");
					tb.append("<td align=center FIXWIDTH=80>Not in Ally</td>");
				}
			}
			tb.append("</tr>");
			tb.append("</table>");
			color++;
		}
		
		tb.append("<center>");
		tb.append("<table border=0 cellspacing=1 cellpadding=1 HEIGHT=30>");
		tb.append("<tr>");
		tb.append("<td><button action=\"bypass _bbsloc;town;" + town + ";" + 1 + "\" value=" + 1 + " WIDTH=50 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
		
		int boton = 1;
		int pag = limit;
		for (int cont = 0; cont <= list.size(); cont++)
		{
			if (cont > pag)
			{
				boton++;
				pag += limit;
				tb.append("<td><button action=\"bypass _bbsloc;town;" + town + ";" + boton + "\" value=" + boton + " WIDTH=30 HEIGHT=30 back=L2UI_CT1.Tab_DF_Tab_Selected fore=L2UI_CT1.Tab_DF_Tab_Unselected></td>");
			}
			
		}
		tb.append("</tr>");
		tb.append("</table>");
		tb.append("</center>");
		
		return tb.toString();
	}
	
	/**
	 * Generamos el html para mostrar los players segun sea requerido
	 * @param town
	 * @param page
	 * @return
	 */
	private static String generateFooter(String town, int page)
	{
		switch (town)
		{
			case "all":
				return GenerateFooterHtml(_onlineList, page, town, "");
			case "Gludio":
				return GenerateFooterHtml(_onlineListGludio, page, town, "");
			case "Gludio_Sell":
				return GenerateFooterHtml(_onlineListGludio_Sell, page, town, "sell");
			case "Gludio_Buy":
				return GenerateFooterHtml(_onlineListGludio_Buy, page, town, "buy");
				// case "Gludio_Manu":
				// return GenerateFooterHtml(_onlineListGludio_Manu, page, town, "manu");
			case "Gludio_SellBuff":
				return GenerateFooterHtml(_onlineListGludio_SellBuff, page, town, "sellbuff");
			case "Dion":
				return GenerateFooterHtml(_onlineListDion, page, town, "");
			case "Dion_Sell":
				return GenerateFooterHtml(_onlineListDion_Sell, page, town, "sell");
			case "Dion_Buy":
				return GenerateFooterHtml(_onlineListDion_Buy, page, town, "buy");
				// case "Dion_Manu":
				// return GenerateFooterHtml(_onlineListDion_Manu, page, town, "manu");
			case "Dion_SellBuff":
				return GenerateFooterHtml(_onlineListDion_SellBuff, page, town, "sellbuff");
			case "Giran":
				return GenerateFooterHtml(_onlineListGiran, page, town, "");
			case "Giran_Sell":
				return GenerateFooterHtml(_onlineListGiran_Sell, page, town, "sell");
			case "Giran_Buy":
				return GenerateFooterHtml(_onlineListGiran_Buy, page, town, "buy");
				// case "Giran_Manu":
				// return GenerateFooterHtml(_onlineListGiran_Manu, page, town, "manu");
			case "Giran_SellBuff":
				return GenerateFooterHtml(_onlineListGiran_SellBuff, page, town, "sellbuff");
			case "Oren":
				return GenerateFooterHtml(_onlineListOren, page, town, "");
			case "Oren_Sell":
				return GenerateFooterHtml(_onlineListOren_Sell, page, town, "sell");
			case "Oren_Buy":
				return GenerateFooterHtml(_onlineListOren_Buy, page, town, "buy");
				// case "Oren_Manu":
				// return GenerateFooterHtml(_onlineListOren_Manu, page, town, "manu");
			case "Oren_SellBuff":
				return GenerateFooterHtml(_onlineListOren_SellBuff, page, town, "sellbuff");
			case "Aden":
				return GenerateFooterHtml(_onlineListAden, page, town, "");
			case "Aden_Sell":
				return GenerateFooterHtml(_onlineListAden_Sell, page, town, "sell");
			case "Aden_Buy":
				return GenerateFooterHtml(_onlineListAden_Buy, page, town, "buy");
				// case "Aden_Manu":
				// return GenerateFooterHtml(_onlineListAden_Manu, page, town, "manu");
			case "Aden_SellBuff":
				return GenerateFooterHtml(_onlineListAden_SellBuff, page, town, "sellbuff");
			case "Heine":
				return GenerateFooterHtml(_onlineListHeine, page, town, "");
			case "Heine_Sell":
				return GenerateFooterHtml(_onlineListHeine_Sell, page, town, "sell");
			case "Heine_Buy":
				return GenerateFooterHtml(_onlineListHeine_Buy, page, town, "buy");
				// case "Heine_Manu":
				// return GenerateFooterHtml(_onlineListHeine_Manu, page, town, "manu");
			case "Heine_SellBuff":
				return GenerateFooterHtml(_onlineListHeine_SellBuff, page, town, "sellbuff");
			case "Goddard":
				return GenerateFooterHtml(_onlineListGoddard, page, town, "");
			case "Goddard_Sell":
				return GenerateFooterHtml(_onlineListGoddard_Sell, page, town, "sell");
			case "Goddard_Buy":
				return GenerateFooterHtml(_onlineListGoddard_Buy, page, town, "buy");
				// case "Goddard_Manu":
				// return GenerateFooterHtml(_onlineListGoddard_Manu, page, town, "manu");
			case "Goddard_SellBuff":
				return GenerateFooterHtml(_onlineListGoddard_SellBuff, page, town, "sellbuff");
			case "Township":
				return GenerateFooterHtml(_onlineListTownship, page, town, "");
			case "Township_Sell":
				return GenerateFooterHtml(_onlineListTownship_Sell, page, town, "sell");
			case "Township_Buy":
				return GenerateFooterHtml(_onlineListTownship_Buy, page, town, "buy");
				// case "Township_Manu":
				// return GenerateFooterHtml(_onlineListTownship_Manu, page, town, "manu");
			case "Township_SellBuff":
				return GenerateFooterHtml(_onlineListTownship_SellBuff, page, town, "sellbuff");
			case "Schuttgart":
				return GenerateFooterHtml(_onlineListSchuttgart, page, town, "");
			case "Schuttgart_Sell":
				return GenerateFooterHtml(_onlineListSchuttgart_Sell, page, town, "sell");
			case "Schuttgart_Buy":
				return GenerateFooterHtml(_onlineListSchuttgart_Buy, page, town, "buy");
				// case "Schuttgart_Manu":
				// return GenerateFooterHtml(_onlineListSchuttgart_Manu, page, town, "manu");
			case "Schuttgart_SellBuff":
				return GenerateFooterHtml(_onlineListSchuttgart_SellBuff, page, town, "sellbuff");
			default:
				return "";
		}
	}
	
	/**
	 * Comenzamos el Task para cargar las listas<br>
	 * La primer lista se ejecuta de inmediato<br>
	 * La segunda se ejecuta cada un determinado tiempo (1min)
	 */
	public void startBoard()
	{
		// generateList();
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new UpdateCommunity(), 100, Config.REGION_LOAD_TIMER * 60 * 1000);
	}
	
	public class UpdateCommunity implements Runnable
	{
		@Override
		public void run()
		{
			generateList();
		}
	}
	
	/**
	 * Actualizamos todas las listas usadas en el community
	 */
	public void changeCommunityBoard()
	{
		generateList();
	}
	
	/**
	 * Generamos las listas necesarias para el comunity<br>
	 * Diferenciados por ciudad, y si estan vendiendo, comprando o vendiendo buffs
	 */
	public void generateList()
	{
		// Gludio
		_onlineListGludio.clear();
		_onlineListGludio_Sell.clear();
		_onlineListGludio_SellBuff.clear();
		_onlineListGludio_Buy.clear();
		// _onlineListGludio_Manu.clear();
		// Dion
		_onlineListDion.clear();
		_onlineListDion_Sell.clear();
		_onlineListDion_SellBuff.clear();
		_onlineListDion_Buy.clear();
		// _onlineListDion_Manu.clear();
		// Giran
		_onlineListGiran.clear();
		_onlineListGiran_Sell.clear();
		_onlineListGiran_SellBuff.clear();
		_onlineListGiran_Buy.clear();
		// _onlineListGiran_Manu.clear();
		// Oren
		_onlineListOren.clear();
		_onlineListOren_Sell.clear();
		_onlineListOren_SellBuff.clear();
		_onlineListOren_Buy.clear();
		// _onlineListOren_Manu.clear();
		// Aden
		_onlineListAden.clear();
		_onlineListAden_Sell.clear();
		_onlineListAden_SellBuff.clear();
		_onlineListAden_Buy.clear();
		// _onlineListAden_Manu.clear();
		// Heine
		_onlineListHeine.clear();
		_onlineListHeine_Sell.clear();
		_onlineListHeine_SellBuff.clear();
		_onlineListHeine_Buy.clear();
		// _onlineListHeine_Manu.clear();
		// Goddard
		_onlineListGoddard.clear();
		_onlineListGoddard_Sell.clear();
		_onlineListGoddard_SellBuff.clear();
		_onlineListGoddard_Buy.clear();
		// _onlineListGoddard_Manu.clear();
		// Township
		_onlineListTownship.clear();
		_onlineListTownship_Sell.clear();
		_onlineListTownship_SellBuff.clear();
		_onlineListTownship_Buy.clear();
		// _onlineListTownship_Manu.clear();
		// Schuttgart
		_onlineListSchuttgart.clear();
		_onlineListSchuttgart_Sell.clear();
		_onlineListSchuttgart_SellBuff.clear();
		_onlineListSchuttgart_Buy.clear();
		// _onlineListSchuttgart_Manu.clear();
		
		_onlineList.clear();
		_onlineListGm.clear();
		
		for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayers().values())
		{
			addPlayerList(onlinePlayer);
		}
	}
	
	/**
	 * Removemos a un gm de la lista de GM visibles
	 * @param player
	 */
	public static void removeGmList(L2PcInstance player)
	{
		if (_onlineListGm.contains(player.getName()))
		{
			_onlineListGm.remove(player.getName());
		}
	}
	
	/**
	 * Agregamos a las diferentes listas a un determinado player
	 * @param onlinePlayer
	 */
	public void addPlayerList(L2PcInstance onlinePlayer)
	{
		if (_onlineList.contains(onlinePlayer.getName()))
		{
			return;
		}
		
		if (onlinePlayer.isGM())
		{
			if (onlinePlayer.getAppearance().getInvisible())
			{
				return;
			}
			_onlineListGm.add(onlinePlayer.getName());
		}
		
		_onlineList.add(onlinePlayer.getName());
		
		L2ZoneType zone = TownManager.getTown(onlinePlayer.getX(), onlinePlayer.getY(), onlinePlayer.getZ());
		if (zone != null)
		{
			if (zone == TownManager.getTown(7))// Gludio
			{
				_onlineListGludio.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) && (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListGludio_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListGludio_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListGludio_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListGludio_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(8))// Dion
			{
				_onlineListDion.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) && (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListDion_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListDion_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListDion_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListDion_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(9))// Giran
			{
				_onlineListGiran.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListGiran_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListGiran_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListGiran_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListGiran_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(10))// Oren
			{
				_onlineListOren.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListOren_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListOren_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListOren_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListOren_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(12))// Aden
			{
				_onlineListAden.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListAden_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListAden_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListAden_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListAden_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(13))// Goddard
			{
				_onlineListGoddard.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListGoddard_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListGoddard_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListGoddard_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListGoddard_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(14))// Rune
			{
				_onlineListTownship.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListTownship_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListTownship_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListTownship_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListTownship_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(15))// Heine
			{
				_onlineListHeine.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListHeine_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListHeine_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListHeine_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListHeine_SellBuff.add(onlinePlayer.getName());
				}
			}
			else if (zone == TownManager.getTown(17))// Schuttgart
			{
				_onlineListSchuttgart.add(onlinePlayer.getName());
				
				if ((onlinePlayer.getPrivateStoreType() == 1) || (onlinePlayer.getPrivateStoreType() == 8))
				{
					_onlineListSchuttgart_Sell.add(onlinePlayer.getName());
				}
				else if (onlinePlayer.getPrivateStoreType() == 3)
				{
					_onlineListSchuttgart_Buy.add(onlinePlayer.getName());
				}
				// else if (onlinePlayer.getPrivateStoreType() == 5)
				// {
				// _onlineListSchuttgart_Manu.add(onlinePlayer.getName());
				// }
				else if (Config.SELL_BUFF_ENABLED && onlinePlayer.isSellBuff())
				{
					_onlineListSchuttgart_SellBuff.add(onlinePlayer.getName());
				}
			}
		}
	}
	
	@Override
	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return;
		}
		
		if (ar1.equals("PM"))
		{
			try
			{
				L2PcInstance receiver = L2World.getInstance().getPlayer(ar2);
				
				TextBuilder tb = new TextBuilder();
				tb.append("<html><body>");
				tb.append("<table border=0 cellspacing=1 cellpadding=2 WIDTH=700 bgcolor=111111 HEIGHT=40>");
				tb.append("<tr>");
				tb.append("<td FIXWIDTH=100 align=right valign=center><img src=L2UI_CH3.mailbtn_over WIDTH=32 HEIGHT=32></td>");
				tb.append("<td FIXWIDTH=480 align=left><font color=E95600>Send Private Message</font><br1><font color=AAAAAA>" + ar2 + "</font></td>");
				tb.append("<td FIXWIDTH=120 align=center><button value=\"\" action=\"bypass _bbsloc\" back=L2UI_CT1.MiniMap_DF_MinusBtn_Red fore=L2UI_CT1.MiniMap_DF_MinusBtn_Red WIDTH=32 HEIGHT=32/></td>");
				tb.append("</tr>");
				tb.append("</table>");
				tb.append("<br>");
				
				int opc = 0;
				
				if (receiver == null)
				{
					tb.append("<br><center><font name=hs12>The player does not exist or is not online</font></center>");
					opc++;
				}
				else if (Config.JAIL_DISABLE_CHAT && receiver.isInJail())
				{
					opc++;
					tb.append("<br><center><font name=hs12>The player is in Jail and can not receive messages</font></center>");
				}
				else if (receiver.isChatBanned())
				{
					opc++;
					tb.append("<br><center><font name=hs12>The player has Ban Chat</font></center>");
				}
				else if (activeChar.isInJail() && Config.JAIL_DISABLE_CHAT)
				{
					opc++;
					tb.append("<br><center><font name=hs12>You can not send messages, you are in Jail</font></center>");
				}
				else if (activeChar.isChatBanned())
				{
					opc++;
					tb.append("<br><center><font name=hs12>You can not send messages, you are in Ban Chat</font></center>");
				}
				
				if (opc > 0)
				{
					tb.append("</body></html>");
					separateAndSend(tb.toString(), activeChar);
				}
				else if (receiver != null)
				{
					if (!receiver.isSilenceMode() && !BlockList.isBlocked(receiver, activeChar))
					{
						receiver.sendPacket(new CreatureSay(activeChar.getObjectId(), Say2.TELL, activeChar.getName(), ar3));
						activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), Say2.TELL, "->" + receiver.getName(), ar3));
						tb.append("<br><center><font name=hs12>Message Send</font></center></body></html>");
						separateAndSend(tb.toString(), activeChar);
					}
					else
					{
						activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE));
						parseCmd("_bbsloc" + receiver.getName(), activeChar);// TODO cambiar
					}
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// ignore
			}
		}
		else
		{
			String content = "<html><body><br><br><center>the command: " + ar1 + " is not implemented yet</center><br><br></body></html>";
			ShowBoard sb = new ShowBoard(content, "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
	}
	
	/**
	 * Podemos asignar un color a la tabla dependiendo si es par o impar
	 * @param color
	 * @return
	 */
	private static String ColorTable(int color)
	{
		if ((color % 2) == 0)
		{
			return "8B4513";
		}
		return "291405";
	}
	
	/**
	 * Podemos asignar un color al precio de los items dependiendo del monto
	 * @param price
	 * @return
	 */
	private static String colorPrice(long price)
	{
		String color = "";
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		
		if (price >= 10000000)
		{
			color = "LEVEL";
		}
		else if (price >= 1000000)
		{
			color = "LEVEL";
		}
		else if (price >= 100000)
		{
			color = "E52FC1";
		}
		else if (price >= 10000)
		{
			color = "0EE8E8";
		}
		return "<font color=" + color + ">" + df.format(price) + "</font>";
	}
	
	public static RegionBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final RegionBBSManager _instance = new RegionBBSManager();
	}
}