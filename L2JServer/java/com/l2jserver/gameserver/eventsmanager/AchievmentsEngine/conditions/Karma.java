package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Karma extends Condition
{
  public Karma(Object value)
  {
    super(value);
    setName("Karma Count");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    int val = Integer.parseInt(getValue().toString());

    if (player.getKarma() >= val)
    {
      return true;
    }

    return false;
  }
}