package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import java.util.StringTokenizer;

public class ItemsCount extends Condition
{
  public ItemsCount(Object value)
  {
    super(value);
    setName("Items Count");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    String s = getValue().toString();
    StringTokenizer st = new StringTokenizer(s, ",");
    int id = 0;
    int ammount = 0;
    try
    {
      id = Integer.parseInt(st.nextToken());
      ammount = Integer.parseInt(st.nextToken());
      if (player.getInventory().getInventoryItemCount(id, 0) >= ammount)
      {
        return true;
      }
    }
    catch (NumberFormatException nfe)
    {
      nfe.printStackTrace();
    }

    return false;
  }
}