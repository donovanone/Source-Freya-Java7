package com.l2jserver.gameserver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import com.l2jserver.Config;

/**
 * @author L2JRussia
 */
public class Restart
{
	// Variables globales
	private static Restart _instance = null;
	protected static final Logger _log = Logger.getLogger(Restart.class.getName());
	private Calendar NextRestart;
	private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	
	public static Restart getInstance()
	{
		if (_instance == null)
		{
			_instance = new Restart();
		}
		return _instance;
	}
	
	public String getRestartNextTime()
	{
		if (NextRestart.getTime() != null)
		{
			return format.format(NextRestart.getTime());
		}
		return "Error";
	}
	
	private Restart()
	{
		//
	}
	
	public void StartCalculationOfNextRestartTime()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar testStartTime = null;
			long flush2 = 0, timeL = 0;
			int count = 0;
			
			for (String timeOfDay : Config.RESTART_INTERVAL_BY_TIME_OF_DAY)
			{
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				testStartTime.set(Calendar.SECOND, 00);
				
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				
				timeL = testStartTime.getTimeInMillis() - currentTime.getTimeInMillis();
				
				if (count == 0)
				{
					flush2 = timeL;
					NextRestart = testStartTime;
				}
				
				if (timeL < flush2)
				{
					flush2 = timeL;
					NextRestart = testStartTime;
				}
				
				count++;
			}
			
			_log.info("[AutoRestart]: Next Restart Time: " + NextRestart.getTime().toString());
			ThreadPoolManager.getInstance().scheduleGeneral(new StartRestartTask(), flush2);
		}
		catch (Exception e)
		{
			System.out.println("[AutoRestart]: The restart automated server presented error in load restarts period config !");
		}
	}
	
	class StartRestartTask implements Runnable
	{
		@Override
		public void run()
		{
			_log.info("[AutoRestart]: Start automated restart GameServer.");
			Shutdown.getInstance().autoRestart(Config.RESTART_SECONDS);
		}
	}
}