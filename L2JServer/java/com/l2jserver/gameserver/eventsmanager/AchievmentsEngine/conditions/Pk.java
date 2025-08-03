package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Pk extends Condition
{
  public Pk(Object value)
  {
    super(value);
    setName("PK Count");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    int val = Integer.parseInt(getValue().toString());

    if (player.getPkKills() >= val)
    {
      return true;
    }

    return false;
  }
}