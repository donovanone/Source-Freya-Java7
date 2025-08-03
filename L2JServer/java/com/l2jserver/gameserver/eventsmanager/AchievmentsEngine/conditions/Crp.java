package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Crp extends Condition
{
  public Crp(Object value)
  {
    super(value);
    setName("Clan Reputation");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    if (player.getClan() != null)
    {
      int val = Integer.parseInt(getValue().toString());

      if (player.getClan().getReputationScore() >= val)
      {
        return true;
      }
    }
    return false;
  }
}