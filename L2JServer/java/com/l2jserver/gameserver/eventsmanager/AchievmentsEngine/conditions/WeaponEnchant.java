package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.conditions;

import com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base.Condition;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.L2ItemInstance;

public class WeaponEnchant extends Condition
{
  public WeaponEnchant(Object value)
  {
    super(value);
    setName("Weapon Enchant");
  }
@Override
  public boolean meetConditionRequirements(L2PcInstance player)
  {
    if (getValue() == null)
    {
      return false;
    }

    int val = Integer.parseInt(getValue().toString());

    L2ItemInstance weapon = player.getInventory().getPaperdollItem(5);

    if (weapon != null)
    {
      if (weapon.getEnchantLevel() >= val)
      {
        return true;
      }
    }

    return false;
  }
}