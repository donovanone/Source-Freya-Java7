package com.l2jserver.gameserver.eventsmanager.AchievmentsEngine.base;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public abstract class Condition
{
  private final Object _value;
  private String _name;

  public Condition(Object value)
  {
    this._value = value;
  }

  public abstract boolean meetConditionRequirements(L2PcInstance paramL2PcInstance);

  public Object getValue()
  {
    return this._value;
  }

  public void setName(String s)
  {
    this._name = s;
  }

  public String getName()
  {
    return this._name;
  }
}