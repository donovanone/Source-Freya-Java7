package com.l2jserver.gameserver.model.zone;

/**
 * Zone Ids.
 * @author Zoey76
 */
public enum ZoneId
{
	PVP(0),
	PEACE(1),
	SIEGE(2),
	MOTHER_TREE(3),
	CLAN_HALL(4),
	LANDING(5),
	NO_LANDING(6),
	WATER(7),
	JAIL(8),
	MONSTER_TRACK(9),
	CASTLE(10),
	SWAMP(11),
	NO_SUMMON_FRIEND(12),
	FORT(13),
	NO_STORE(14),
	TOWN(15),
	SCRIPT(16),
	HQ(17),
	DANGER_AREA(18),
	ALTERED(19),
	NO_BOOKMARK(20),
	NO_ITEM_DROP(21),
	NO_RESTART(22);
	
	private final int _id;
	
	private ZoneId(int id)
	{
		_id = id;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public static int getZoneCount()
	{
		return values().length;
	}
}
