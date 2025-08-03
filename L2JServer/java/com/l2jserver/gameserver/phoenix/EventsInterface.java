package com.l2jserver.gameserver.phoenix;

import com.l2jserver.gameserver.phoenix.functions.Buffer;
import com.l2jserver.gameserver.phoenix.io.In;

/**
 * version 2.0
 * @author Rizel
 */
public class EventsInterface
{
	public static boolean areTeammates(Integer player, Integer target)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().areTeammates(player, target);
	}
	
	public static boolean canAttack(Integer player, Integer target)
	{
		if (!Main.isLoaded())
		{
			return true;
		}
		
		return In.getInstance().canAttack(player, target);
	}
	
	public static boolean canTargetPlayer(Integer target, Integer self)
	{
		if (!Main.isLoaded())
		{
			return true;
		}
		
		return In.getInstance().canTargetPlayer(target, self);
	}
	
	public static boolean canUseSkill(Integer player, Integer skill)
	{
		if (!Main.isLoaded())
		{
			return true;
		}
		
		return In.getInstance().canUseSkill(player, skill);
	}
	
	public static boolean doAttack(Integer self, Integer target)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().doAttack(self, target);
	}
	
	public static void eventOnLogout(Integer player)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().eventOnLogout(player);
	}
	
	public static boolean getBoolean(String propName, Integer player)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().getBoolean(propName, player);
	}
	
	public static int getInt(String propName, Integer player)
	{
		if (!Main.isLoaded())
		{
			return 0;
		}
		
		return In.getInstance().getInt(propName, player);
	}
	
	public static boolean isParticipating(Integer player)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		if (player != null)
		{
			return In.getInstance().isParticipating(player);
		}
		return false;
	}
	
	public static boolean isRegistered(Integer player)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().isRegistered(player);
	}
	
	public static boolean logout(Integer player)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		return In.getInstance().logout(player);
	}
	
	public static void onDie(Integer victim, Integer killer)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().onDie(victim, killer);
	}
	
	public static void onHit(Integer actor, Integer target)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().onHit(actor, target);
	}
	
	public static void onKill(Integer victim, Integer killer)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().onKill(victim, killer);
	}
	
	public static void onLogout(Integer player)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().onLogout(player);
	}
	
	public static void onSay(int type, Integer player, String text)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().onSay(type, player, text);
	}
	
	public static boolean onTalkNpc(Integer npc, Integer player)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().onTalkNpc(npc, player);
	}
	
	public static boolean onUseItem(Integer player, Integer item, Integer objectId)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().onUseItem(player, item);
	}
	
	public static boolean onUseMagic(Integer player, Integer skill)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		return In.getInstance().onUseMagic(player, skill);
	}
	
	public static void showFirstHtml(Integer player, int obj)
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().showFirstHtml(player, obj);
	}
	
	public static void shutdown()
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		In.getInstance().shutdown();
	}
	
	public static void start()
	{
		if (!Main.isLoaded())
		{
			return;
		}
		
		try
		{
			Events.eventStart();
			
			if (In.getInstance().getBoolean("eventBufferEnabled", 0))
			{
				Buffer.getInstance();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean talkNpc(Integer player, Integer npc)
	{
		if (!Main.isLoaded())
		{
			return false;
		}
		
		return In.getInstance().talkNpc(player, npc);
	}
}