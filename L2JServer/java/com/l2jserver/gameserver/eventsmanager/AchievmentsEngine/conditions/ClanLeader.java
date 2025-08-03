package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class ClanLeader extends Condition
{
  public ClanLeader(Object value)
  {
    super(value);
    setName("Be Clan Leader");
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
      if (player.isClanLeader())
      {
        return true;
      }
    }

    return false;
  }
}