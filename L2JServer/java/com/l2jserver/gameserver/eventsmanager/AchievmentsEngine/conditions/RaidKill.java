package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.instancemanager.RaidBossPointsManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.util.Iterator;
import java.util.Map;

public class RaidKill extends Condition
{
  public RaidKill(Object value)
  {
    super(value);
    setName("Raid Kill");
  }

@Override
@SuppressWarnings("rawtypes")
public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    int val = Integer.parseInt(getValue().toString());

    Map list = RaidBossPointsManager.getList(player);
    Iterator i$;
    if (list != null)
    {
      for (i$ = list.keySet().iterator(); i$.hasNext(); ) { int bid = ((Integer)i$.next()).intValue();

        if (bid == val)
        {
          if (RaidBossPointsManager.getList(player).get(Integer.valueOf(bid)).intValue() > 0)
          {
            return true;
          }
        }
      }
    }
    return false;
  }
}