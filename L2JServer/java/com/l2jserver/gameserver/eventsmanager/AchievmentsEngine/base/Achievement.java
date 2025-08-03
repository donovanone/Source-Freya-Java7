package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base;

import java.util.logging.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Achievement
{
	private static Logger _log = Logger.getLogger(Achievement.class.getName());
	private final int _id;
	private final String _name;
	private final String _reward;
	private String _description = "No Description!";
	private final boolean _repeatable;
	private final FastMap<Integer, Long> _rewardList;
	private final FastList<Condition> _conditions;
	
	@SuppressWarnings(
	{
		"rawtypes",
		"unchecked"
	})
	public Achievement(int id, String name, String description, String reward, boolean repeatable, FastList<Condition> conditions)
	{
		this._rewardList = new FastMap();
		this._id = id;
		this._name = name;
		this._description = description;
		this._reward = reward;
		this._conditions = conditions;
		this._repeatable = repeatable;
		
		createRewardList();
	}
	
	private void createRewardList()
	{
		for (String s : this._reward.split(";"))
		{
			if ((s != null) && (!s.isEmpty()))
			{
				String[] split = s.split(",");
				Integer item = Integer.valueOf(0);
				Long count = new Long(0L);
				try
				{
					item = Integer.valueOf(split[0]);
					count = Long.valueOf(split[1]);
				}
				catch (NumberFormatException nfe)
				{
					_log.warning("AchievementsEngine: Error wrong reward " + nfe);
				}
				this._rewardList.put(item, count);
			}
		}
	}
	
	public boolean meetAchievementRequirements(L2PcInstance player)
	{
		for (Condition c : getConditions())
		{
			if (!c.meetConditionRequirements(player))
			{
				return false;
			}
		}
		return true;
	}
	
	public int getID()
	{
		return this._id;
	}
	
	public String getName()
	{
		return this._name;
	}
	
	public String getDescription()
	{
		return this._description;
	}
	
	public String getReward()
	{
		return this._reward;
	}
	
	public boolean isRepeatable()
	{
		return this._repeatable;
	}
	
	public FastMap<Integer, Long> getRewardList()
	{
		return this._rewardList;
	}
	
	public FastList<Condition> getConditions()
	{
		return this._conditions;
	}
}