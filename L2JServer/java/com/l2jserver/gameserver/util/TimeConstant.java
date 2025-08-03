/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.util;

/**
 * Hold number of milliseconds for wide-using time periods
 * @author GKR
 */

public enum TimeConstant
{
	NONE(-1L, "", ""),
	SECOND(1000L, "second", "s"),
	MINUTE(60000L, "minute", "m"),
	HOUR(3600000L, "hour", "h"),
	DAY(86400000L, "day", "d"),
	WEEK(604800000L, "week", "w"),
	MONTH(2592000000L, "Month", "M");
	
	/** Count of milliseconds */
	private final long _millis;
	/** Mnemonic name of period */
	private final String _name;
	/** Short name of period */
	private final String _shortName;
	
	private TimeConstant(long millis, String name, String shortName)
	{
		_millis = millis;
		_name = name;
		_shortName = shortName;
	}
	
 /**
  * @return number of millisecond in time period 
  */
	public long getTimeInMillis()
	{
		return _millis;
	}
	
 /**
  * @return mnemonic name of time period 
  */
	public String getName()
	{
		return _name;
	}
	
 /**
  * @return short name of time period 
  */
	public String getShortName()
	{
		return _shortName;
	}
}