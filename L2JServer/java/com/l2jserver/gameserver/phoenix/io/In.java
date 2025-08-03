package com.l2jserver.gameserver.phoenix.io;

import com.l2jserver.gameserver.phoenix.AbstractEvent.AbstractPhase;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.ManagerNpc;
import com.l2jserver.gameserver.phoenix.container.EventContainer;
import com.l2jserver.gameserver.phoenix.container.PlayerContainer;
import com.l2jserver.gameserver.phoenix.functions.Buffer;
import com.l2jserver.gameserver.phoenix.functions.Vote;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;

/**
 * @author Rizel
 */
public class In
{
	private static class SingletonHolder
	{
		protected static final In _instance = new In();
	}
	
	public static final In getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public boolean areTeammates(EventPlayer player, EventPlayer target)
	{
		if (player.getEvent() == null)
		{
			return false;
		}
		if (player.getEvent().numberOfTeams() == 1)
		{
			return false;
		}
		if (player.getEvent().numberOfTeams() > 1)
		{
			if (player.getMainTeam() == target.getMainTeam())
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean areTeammates(Integer player, Integer target)
	{
		if ((PlayerContainer.getInstance().getPlayer(player) != null) && (PlayerContainer.getInstance().getPlayer(target) != null))
		{
			return areTeammates(PlayerContainer.getInstance().getPlayer(player), PlayerContainer.getInstance().getPlayer(target));
		}
		return false;
	}
	
	public boolean canAttack(EventPlayer player, EventPlayer target)
	{
		return player.getEvent().canAttack(player, target);
	}
	
	public boolean canAttack(Integer player, Integer target)
	{
		if ((PlayerContainer.getInstance().getPlayer(player) != null) && (PlayerContainer.getInstance().getPlayer(target) != null))
		{
			return canAttack(PlayerContainer.getInstance().getPlayer(player), PlayerContainer.getInstance().getPlayer(target));
		}
		return true;
	}
	
	public boolean canTargetPlayer(EventPlayer target, EventPlayer self)
	{
		if ((self != null) && (self.getEvent().getAbstractPhase() == AbstractPhase.RUNNING))
		{
			if ((isParticipating(target) && isParticipating(self)) || (!isParticipating(target) && !isParticipating(self)))
			{
				return true;
			}
			return false;
		}
		return true;
	}
	
	public boolean canTargetPlayer(Integer target, Integer self)
	{
		if ((PlayerContainer.getInstance().getPlayer(target) != null) && (PlayerContainer.getInstance().getPlayer(self) != null))
		{
			return canTargetPlayer(PlayerContainer.getInstance().getPlayer(target), PlayerContainer.getInstance().getPlayer(self));
		}
		return true;
	}
	
	public boolean canUseSkill(Integer player, Integer skill)
	{
		return Config.getInstance().getBoolean(PlayerContainer.getInstance().getPlayer(player).getEvent().getId(), "allowUseMagic");
	}
	
	public void eventOnLogout(Integer player)
	{
		EventPlayer pi = PlayerContainer.getInstance().getPlayer(player);
		if (pi != null)
		{
			PlayerContainer.getInstance().getPlayer(player).getEvent().onLogout(pi);
		}
	}
	
	public boolean isParticipating(EventPlayer player)
	{
		if (player.getEvent() != null)
		{
			return player.getEvent().getAbstractPhase() == AbstractPhase.RUNNING;
		}
		return false;
	}
	
	public boolean isParticipating(Integer player)
	{
		if (isRegistered(player))
		{
			return isParticipating(PlayerContainer.getInstance().getPlayer(player));
		}
		return false;
	}
	
	public boolean isRegistered(Integer player)
	{
		if (PlayerContainer.getInstance().getPlayer(player) != null)
		{
			return true;
		}
		return false;
	}
	
	public boolean isRunning(Integer player)
	{
		if ((PlayerContainer.getInstance().getPlayer(player) != null) && (PlayerContainer.getInstance().getPlayer(player).getEvent() != null))
		{
			if (PlayerContainer.getInstance().getPlayer(player).getEvent().getAbstractPhase() == AbstractPhase.RUNNING)
			{
				return true;
			}
		}
		return false;
	}
	
	public void onDie(EventPlayer victim, EventPlayer killer)
	{
		victim.getEvent().onDie(victim, killer);
	}
	
	public void onDie(Integer player, Integer target)
	{
		if ((PlayerContainer.getInstance().getPlayer(player) != null) && (PlayerContainer.getInstance().getPlayer(target) != null))
		{
			onDie(PlayerContainer.getInstance().getPlayer(player), PlayerContainer.getInstance().getPlayer(target));
		}
		else
		{
			return;
		}
	}
	
	public void onHit(EventPlayer actor, EventPlayer target)
	{
		actor.getEvent().onHit(actor, target);
	}
	
	public void onHit(Integer actor, Integer target)
	{
		if ((PlayerContainer.getInstance().getPlayer(actor) != null) && (PlayerContainer.getInstance().getPlayer(target) != null))
		{
			onHit(PlayerContainer.getInstance().getPlayer(actor), PlayerContainer.getInstance().getPlayer(target));
		}
		else
		{
			return;
		}
	}
	
	public void onKill(EventPlayer victim, EventPlayer killer)
	{
		killer.getEvent().onKill(victim, killer);
	}
	
	public void onKill(Integer player, Integer target)
	{
		if ((PlayerContainer.getInstance().getPlayer(player) != null) && (PlayerContainer.getInstance().getPlayer(target) != null))
		{
			onKill(PlayerContainer.getInstance().getPlayer(player), PlayerContainer.getInstance().getPlayer(target));
		}
		else
		{
			return;
		}
	}
	
	public void onLogout(Integer player)
	{
		if (Config.getInstance().getBoolean(0, "voteEnabled") && Vote.getInstance().votes.containsKey(player))
		{
			Vote.getInstance().votes.remove(player);
		}
		if (PlayerContainer.getInstance().getPlayer(player) != null)
		{
			PlayerContainer.getInstance().deleteInfo(player);
		}
	}
	
	public void onSay(int type, EventPlayer player, String text)
	{
		player.getEvent().onSay(type, player, text);
	}
	
	public void onSay(int type, Integer player, String text)
	{
		EventPlayer pi = PlayerContainer.getInstance().getPlayer(player);
		if (pi != null)
		{
			onSay(type, pi, text);
		}
	}
	
	public boolean onTalkNpc(Integer npc, Integer player)
	{
		EventPlayer pi = PlayerContainer.getInstance().getPlayer(player);
		if (pi != null)
		{
			return pi.getEvent().onTalkNpc(npc, pi);
		}
		return false;
	}
	
	public boolean onUseItem(Integer player, Integer item)
	{
		EventPlayer pi = PlayerContainer.getInstance().getPlayer(player);
		if (pi != null)
		{
			return pi.getEvent().onUseItem(pi, item);
		}
		return false;
	}
	
	public boolean onUseMagic(Integer player, Integer skill)
	{
		EventPlayer pi = PlayerContainer.getInstance().getPlayer(player);
		if (pi != null)
		{
			return pi.getEvent().onUseMagic(pi, skill);
		}
		return false;
	}
	
	public boolean getBoolean(String propName, Integer player)
	{
		if (player == 0)
		{
			return Config.getInstance().getBoolean(0, propName);
		}
		return Config.getInstance().getBoolean(PlayerContainer.getInstance().getPlayer(player).getEvent().getId(), propName);
	}
	
	public int getInt(String propName, Integer player)
	{
		if (player == 0)
		{
			return Config.getInstance().getInt(0, propName);
		}
		return Config.getInstance().getInt(PlayerContainer.getInstance().getPlayer(player).getEvent().getId(), propName);
	}
	
	public void registerPlayer(Integer player, Integer eventId)
	{
		EventContainer.getInstance().getEvent(eventId).registerPlayer(player);
	}
	
	public void showVoteList(Integer player)
	{
		ManagerNpc.getInstance().showVoteList(player);
	}
	
	public void unregisterPlayer(Integer player, Integer eventId)
	{
		EventContainer.getInstance().getEvent(eventId).unregisterPlayer(player);
	}
	
	public void showFirstHtml(Integer player, int obj)
	{
		ManagerNpc.getInstance().showMain(player);
	}
	
	public void showRegisterPage(Integer player, Integer event)
	{
		ManagerNpc.getInstance().showRegisterPage(player, event, 0);
	}
	
	public void addVote(Integer player, int event)
	{
		Vote.getInstance().addVote(player, event);
	}
	
	public boolean logout(Integer player)
	{
		if (isParticipating(player) && !getBoolean("restartAllowed", 0))
		{
			PlayerContainer.getInstance().getPlayer(player).sendMessage("[Event Manager]: You cannot logout while you are a participant in an event.");
			return true;
		}
		onLogout(player);
		return false;
	}
	
	public void shutdown()
	{
		Buffer.getInstance().updateSQL();
	}
	
	public boolean talkNpc(Integer player, Integer npc)
	{
		if (npc == Config.getInstance().getInt(0, "managerNpcId"))
		{
			ManagerNpc.getInstance().showMain(player);
			return true;
		}
		
		if (isParticipating(player))
		{
			if (onTalkNpc(npc, player))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean doAttack(Integer self, Integer target)
	{
		if (isParticipating(self) && isParticipating(target))
		{
			// if (areTeammates(self, target) && Config.getInstance().getBoolean(0, "friendlyFireEnabled"))
			if (areTeammates(self, target) && getBoolean("friendlyFireEnabled", 0))
			{
				return true;
			}
		}
		if (!canAttack(self, target))
		{
			return true;
		}
		return false;
	}
}