package com.l2jserver.gameserver.phoenix;

import com.l2jserver.gameserver.phoenix.functions.Buffer;
import com.l2jserver.gameserver.phoenix.functions.Scheduler;
import com.l2jserver.gameserver.phoenix.functions.Vote;

/**
 * @author Rizel
 */
public class Events
{
	public static void eventStart()
	{
		Config.getInstance();
		
		if (Config.getInstance().getBoolean(0, "voteEnabled"))
		{
			Vote.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "schedulerEnabled"))
		{
			Scheduler.getInstance();
		}
		if (Config.getInstance().getBoolean(0, "eventBufferEnabled"))
		{
			Buffer.getInstance();
		}
	}
}