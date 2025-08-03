package com.l2jserver.gameserver.phoenix.io;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Rizel
 */
public class Out
{
	// TODO revisar q es importante q el evento transcurra en una instancia
	// public static void createInstance(int id)
	// {
	// InstanceManager.getInstance().createInstance(id);
	// }
	
	// public static void createParty2(FastList<EventPlayer> players)
	// {
	// L2Party party = new L2Party(players.get(0).getOwner(), 1);
	//
	// for (EventPlayer player : players.subList(1, players.size()))
	// {
	// player.joinParty(party);
	// }
	// }
	
	public static void html(Integer player, String html)
	{
		NpcHtmlMessage msg = new NpcHtmlMessage(0);
		msg.setHtml(html);
		L2World.getInstance().getPlayer(player).sendPacket(msg);
	}
	
	public static Collection<Integer> getEveryPlayer()
	{
		List<Integer> list = new LinkedList<>();
		for (Integer player : L2World.getInstance().getAllPlayers().keySet())
		{
			list.add(player);
		}
		return list;
	}
}