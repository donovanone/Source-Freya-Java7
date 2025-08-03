package com.l2jserver.gameserver.phoenix.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import javolution.text.TextBuilder;
import javolution.util.FastList;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.phoenix.Config;
import com.l2jserver.gameserver.phoenix.container.EventContainer;
import com.l2jserver.gameserver.phoenix.io.Out;
import com.l2jserver.gameserver.phoenix.models.ManagerNpcHtml;

/**
 * @author Rizel
 */
public class Scheduler
{
	private class SchedulerTask implements Runnable
	{
		@Override
		public void run()
		{
			currentCal = Calendar.getInstance();
			Integer hour = currentCal.get(Calendar.HOUR_OF_DAY);
			Integer mins = currentCal.get(Calendar.MINUTE);
			
			for (Integer[] element : scheduleList)
			{
				if (element[0].equals(hour) && element[1].equals(mins))
				{
					if (element[2].equals(0))
					{
						EventContainer.getInstance().createRandomEvent();
					}
					else
					{
						EventContainer.getInstance().createEvent(element[2]);
					}
				}
			}
		}
	}
	
	Calendar currentCal;
	
	final FastList<Integer[]> scheduleList;
	
	@SuppressWarnings("synthetic-access")
	private Scheduler()
	{
		scheduleList = new FastList<>();
		makeList();
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new SchedulerTask(), 1, 50000);
	}
	
	private String loadFile()
	{
		String beolvasott = "";
		String str;
		try (BufferedReader bf = new BufferedReader(new FileReader("config/Events/EventScheduler.txt")))
		{
			while ((str = bf.readLine()) != null)
			{
				beolvasott += str;
			}
		}
		catch (IOException e)
		{
			System.out.println("Error on reading the scheduler file!");
			return "";
		}
		return beolvasott;
	}
	
	private void makeList()
	{
		StringTokenizer st = new StringTokenizer(loadFile(), ";");
		while (st.hasMoreTokens())
		{
			StringTokenizer sti = new StringTokenizer(st.nextToken(), ":");
			Integer ora = Integer.parseInt(sti.nextToken());
			Integer perc = Integer.parseInt(sti.nextToken());
			Integer event = Integer.parseInt(sti.nextToken());
			scheduleList.add(new Integer[]
			{
				ora,
				perc,
				event
			});
		}
	}
	
	public void scheduleList(Integer player)
	{
		TextBuilder builder = new TextBuilder();
		
		int count = 0;
		
		builder.append("<center><table width=470 bgcolor=4f4f4f><tr><td width=70><font color=ac9775>Scheduler</font></td></tr></table><br>");
		
		for (Integer[] event : scheduleList)
		{
			count++;
			builder.append("<center><table width=270 " + ((count % 2) == 1 ? "" : "bgcolor=4f4f4f") + "><tr><td width=30><font color=ac9775>" + (event[0] < 10 ? "0" + event[0] : event[0]) + ":" + (event[1] < 10 ? "0" + event[1] : event[1]) + "</font></td><td width=210><font color=9f9f9f>" + Config.getInstance().getString(event[2], "eventName") + "</font></td></tr></table>");
		}
		Out.html(player, new ManagerNpcHtml(builder.toString()).string());
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final Scheduler _instance = new Scheduler();
	}
	
	public static final Scheduler getInstance()
	{
		return SingletonHolder._instance;
	}
}