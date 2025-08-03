package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class ClanLevel extends Condition
{
  public ClanLevel(Object value)
  {
    super(value);
    setName("Clan Level");
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

      if (player.getClan().getLevel() >= val)
      {
        return true;
      }
    }
    return false;
  }
}