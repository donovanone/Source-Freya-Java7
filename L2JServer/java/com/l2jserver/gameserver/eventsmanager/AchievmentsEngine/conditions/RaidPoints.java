package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.instancemanager.RaidBossPointsManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class RaidPoints extends Condition
{
  public RaidPoints(Object value)
  {
    super(value);
    setName("Raid Points");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    int val = Integer.parseInt(getValue().toString());

    RaidBossPointsManager.getInstance();
    if (RaidBossPointsManager.getPointsByOwnerId(player.getObjectId()) >= val)
    {
      return true;
    }
    return false;
  }
}