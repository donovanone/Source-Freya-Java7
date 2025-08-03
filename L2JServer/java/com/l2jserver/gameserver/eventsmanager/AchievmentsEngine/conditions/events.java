package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class events extends Condition
{
  public events(Object value)
  {
    super(value);
    setName("Events played");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    @SuppressWarnings("unused")
	int val = Integer.parseInt(getValue().toString());

    return false;
  }
}