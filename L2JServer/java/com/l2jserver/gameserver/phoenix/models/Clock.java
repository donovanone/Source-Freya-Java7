package com.l2jserver.gameserver.phoenix.models;

import com.l2jserver.gameserver.ThreadPoolManager;

/**
 * @author Rizel
 */
public abstract class Clock implements Runnable
{
	protected int counter;
	
	public Clock(int time)
	{
		counter = time;
	}
	
	public abstract void clockBody();
	
	public int getTimeInInt()
	{
		return counter;
	}
	
	public String getTimeInString()
	{
		String mins = "" + (counter / 60);
		String secs = ((counter % 60) < 10 ? "0" + (counter % 60) : "" + (counter % 60));
		return "" + mins + ":" + secs + "";
	}
	
	protected abstract void onZero();
	
	@Override
	public void run()
	{
		clockBody();
		
		if (counter == 0)
		{
			onZero();
		}
		else
		{
			counter--;
			ThreadPoolManager.getInstance().scheduleGeneral(this, 1000);
		}
	}
	
	public void start()
	{
		ThreadPoolManager.getInstance().scheduleGeneral(this, 1);
	}
	
	public void stop()
	{
		counter = 0;
	}
}
