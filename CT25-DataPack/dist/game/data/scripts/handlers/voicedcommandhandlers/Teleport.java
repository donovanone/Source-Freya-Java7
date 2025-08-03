package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Donovan
 */

public class Teleport implements IVoicedCommandHandler
	{
		private static final String[] VOICED_COMMANDS =
			{
				"up10",
				"up20",
				"up30",
				"up40",
				"up50",
				"up60",
				"up70",
				"up80"
			};

@Override
public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
	 {
		 if (Config.ALLOW_TELEPORT_VOICECOMMAND)
		 {
       
        	if (activeChar.isFestivalParticipant())
        	{
        		activeChar.sendMessage("Comando bloqueado estando en Evento!");
        		return false;
        	}
        	else if (activeChar.isInJail())
        	{
        		activeChar.sendMessage("Comando bloqueado, estando en jail");
        		return false;
        	}
        	else if (activeChar.isDead())
        	{
        		activeChar.sendMessage("Comando bloqueado estando muerto.");
        		return false;
        	}
        	else if(activeChar.isInCombat())
        	{
        		activeChar.sendMessage("Comando bloqueado estando en pvp.");
        		return false;
        	}
        	else if (activeChar.isInDuel())
        	{
        		activeChar.sendMessage("Comando bloqueado estando en Duelo.");
        		return false;
        	}
        	else if (activeChar.isInOlympiadMode())
        	{
        		activeChar.sendMessage("Comando bloqueado estando en olympiada.");
        		return false;
        	}
        	else if (activeChar.getKarma() > 0)
        	{
        		activeChar.sendMessage("Comando bloqueado estando con karma");
        		return false;
        	}
        	else if (activeChar.inObserverMode())
        	{
        		activeChar.sendMessage("Comando Bloqueado estando en observador.");
        		return false;
        	}
        	else if (!activeChar.inObserverMode() && !activeChar.isInOlympiadMode() && !activeChar.isInDuel() && !activeChar.isInCombat() && !activeChar.isDead() && !activeChar.isInJail())
        	{
                       
        		if(command.startsWith("up10"))
        		{
        			activeChar.teleToLocation(-20043, 137688, -3896);
        			activeChar.sendMessage("Up de level 10 a 20, Bienvenido a Ruins of Despair ");
        			return false;
        		}
        		else if (command.startsWith("up20"))
        		{
        			activeChar.teleToLocation(48730, 146809, -3401);
        			activeChar.sendMessage("Up de level 20 a 30, Bienvenido a Execution ");
        			return false;
        		}
        		else if (command.startsWith("up30"))
        		{
        			activeChar.teleToLocation(102656, 101463, -3571);
        			activeChar.sendMessage("Up de level 30 a 40, Bienvenido a Hardin Private Academy ");
        			return false;
        		}
        		else if (command.startsWith("up40"))
        		{
        			activeChar.teleToLocation(19883, 117008, -12086);
        			activeChar.sendMessage("Up de level 40 a 50, Bienvenido a cruma tower ");
        			return false;
        		}              
        		else if (command.startsWith("up50"))
        		{
        			activeChar.teleToLocation(167312, 20289, -3330);
        			activeChar.sendMessage("Up de level 50 a 60, Bienvenido a The Cemetary");
        			return false;
        		}
        		else if (command.startsWith("up60"))
        		{
        			activeChar.teleToLocation(134260 , 114445, -3718);
        			activeChar.sendMessage("Up de level 60 a 70, Bienvenido a Antharas Lair");
        			return false;
        		}
        		else if (command.startsWith("up70"))
        		{
        			activeChar.teleToLocation(146428 , 109789, -3425);
        			activeChar.sendMessage("Up de level 70 a 80, Bienvenido a Antharas Lair 2");
        			return false;
        		}
        		else if (command.startsWith("up80"))
        		{
        			activeChar.teleToLocation(344 , 235040, -3268);
        			activeChar.sendMessage("Up encima de level 80, Bienvenido a Chimeras");
        			return false;
        		}
        	}
		 }
	 }
	return true;
 }

@Override
public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}