package com.l2jserver.gameserver.phoenix.events;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.phoenix.AbstractEvent;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.models.EventPlayer;
import com.l2jserver.gameserver.phoenix.models.SingleEventStatus;

/**
 * @author Rizel
 */
public class Mutant extends AbstractEvent
{
	public static boolean enabled = true;
	
	private class Core implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				switch (eventState)
				{
					case START:
						cancelAttack();// by fissban
						startParalize();// by fissban
						divideIntoTeams(1);
						teleportToTeamPos();
						preparePlayers();
						setStatus(EventState.FIGHT);
						schedule(10000);
						break;
					case FIGHT:
						unParalize(); // by fissban
						setTitleScore();// by fissban
						SpecialMsg(2, "Start"); // by fissban
						transformMutant(getRandomPlayer());
						setStatus(EventState.END);
						clock.start();
						break;
					case END:
						cancelAttack();// by fissban
						SpecialMsg(1, "Finish"); // by fissban
						clock.stop();
						untransformMutant();
						EventPlayer winner = getPlayerWithMaxScore();
						giveReward(winner);
						setStatus(EventState.INACTIVE);
						announce("Congratulation! " + winner.getName() + " won the event with " + winner.getScore() + " kills!");
						eventEnded();
						break;
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
				announce("Error! Event ended.");
				eventEnded();
			}
		}
	}
	
	private enum EventState
	{
		START,
		FIGHT,
		END,
		INACTIVE
	}
	
	EventState eventState;
	
	private final Core task;
	
	private EventPlayer mutant;
	
	@SuppressWarnings("synthetic-access")
	public Mutant(Integer containerId)
	{
		super(containerId);
		eventId = 13;
		createNewTeam(1, "All", Config.getInstance().getColor(getId(), "All"), Config.getInstance().getPosition(getId(), "All", 1));
		task = new Core();
		clock = new EventClock(Config.getInstance().getInt(getId(), "matchTime"));
	}
	
	@Override
	protected void endEvent()
	{
		setStatus(EventState.END);
		clock.stop();
	}
	
	@Override
	protected String getScorebar()
	{
		return "Max: " + getPlayerWithMaxScore().getScore() + "  Time: " + clock.getTimeInString() + "";
	}
	
	@Override
	protected void onClockZero()
	{
		setStatus(EventState.END);
		schedule(1);
	}
	
	@Override
	public void onDie(EventPlayer victim, EventPlayer killer)
	{
		super.onDie(victim, killer);
		addToResurrector(victim);
	}
	
	@Override
	public void onKill(EventPlayer victim, EventPlayer killer)
	{
		super.onKill(victim, killer);
		if (killer.getStatus() == 1)
		{
			killer.increaseScore();
		}
		if ((killer.getStatus() == 0) && (victim.getStatus() == 1))
		{
			transformMutant(killer);
		}
	}
	
	@Override
	public void onLogout(EventPlayer player)
	{
		super.onLogout(player);
		
		if (mutant.equals(player))
		{
			transformMutant(getRandomPlayer());
		}
	}
	
	@Override
	protected void schedule(int time)
	{
		ThreadPoolManager.getInstance().scheduleGeneral(task, time);
	}
	
	void setStatus(EventState s)
	{
		eventState = s;
	}
	
	@Override
	public void start()
	{
		setStatus(EventState.START);
		schedule(1);
	}
	
	void transformMutant(EventPlayer player)
	{
		player.setNameColor(255, 0, 0);
		player.transform(303);
		player.addSkill(Config.getInstance().getInt(getId(), "mutantBuffId"), 1);
		player.setStatus(1);
		untransformMutant();
		player.broadcastUserInfo();
		mutant = player;
	}
	
	void untransformMutant()
	{
		if (mutant != null)
		{
			mutant.setNameColor(Config.getInstance().getColor(getId(), "All")[0], Config.getInstance().getColor(getId(), "All")[1], Config.getInstance().getColor(getId(), "All")[2]);
			mutant.untransform();
			mutant.removeSkill(Config.getInstance().getInt(getId(), "mutantBuffId"), 1);
			mutant.setStatus(0);
			mutant.broadcastUserInfo();
			mutant = null;
		}
	}
	
	@Override
	public void createStatus()
	{
		status = new SingleEventStatus(containerId);
	}
}