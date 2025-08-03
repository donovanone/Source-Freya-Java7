package handlers.voicedcommandhandlers;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;


public class Teleport1 implements IVoicedCommandHandler
{
 private static final String[] VOICED_COMMANDS =
 {
	    "giran",
	    "aden",
	    "oren",
	    "dion",
	    "goddard",
	    "floran",
	    "gludin",
	    "gludio",
	    "rune",
	    "heine",
	    "dwarvenvillage",
	    "darkelvenvillage",
	    "elvenvillage",
	    "orcvillage",
	    "talkingisland",
	    "schuttgart",
	    "huntersvillage",
	    "antharas",
	    "valakas",
	    "baium",
	    "queenant",
	    "zaken",
	    "orfen",
	    "core"
 };

 @Override
public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
 {
	    {
	        if (Config.ALLOW_TELEPORT_VOICECOMMAND)
	        {
	    
			    if (activeChar.isFestivalParticipant())
			    {
			    activeChar.sendMessage("Voce nao pode usar esse comando vc esta em um Evento!");
			    return false;
			    }
			    else if(activeChar.isInJail())
			    {
			    activeChar.sendMessage("Voce nao pode sair da jail");
			    return false;
			    }
			    else if(activeChar.isDead())
			    {
			    activeChar.sendMessage("Voce nao pode teleportar pois esta morto.");
			    return false;
			    }
			    else if(activeChar.isInCombat())
			    {
			    activeChar.sendMessage("You can't teleport while you are in combat.");
			    return false;
			    }
			    else if (activeChar.isInDuel())
			    {
			    activeChar.sendMessage("You can't teleport while you are doing a duel.");
			    return false;
			    }
			    else if (activeChar.isInOlympiadMode())
			    {
			    activeChar.sendMessage("You can't teleport while you are in olympiad");
			    return false;
			    }
			    else if (activeChar.inObserverMode())
			    {
			    activeChar.sendMessage("You can't teleport while you are in observer mode");
			    return false;
			    }
			    else if (!activeChar.inObserverMode() && !activeChar.isInOlympiadMode() && !activeChar.isInDuel() && !activeChar.isInCombat() && !activeChar.isDead() && !activeChar.isInJail())
			    {
			   
			    if(command.startsWith("giran"))
			    {
			    activeChar.teleToLocation(82337, 148602, -3467);
			    }
			    else if(command.startsWith("dion"))
			    {
			    activeChar.teleToLocation(18492, 145386, -3118);
			    }
			    else if(command.startsWith("oren"))
			    {
			    activeChar.teleToLocation(82769, 53573, -1498);
			    }
			    else if(command.startsWith("gludio"))
			    {
			    activeChar.teleToLocation(-12864, 122716, -3117);
			    }
			    else if(command.startsWith("gludin"))
			    {
			    activeChar.teleToLocation(-80928, 150055, -3044);
			    }
			    else if(command.startsWith("aden"))
			    {
			    activeChar.teleToLocation(147361, 26953, -2205);
			    }
			    else if(command.startsWith("schuttgart"))
			    {
			    activeChar.teleToLocation(87359, -143224, -1293);
			    }
			    else if(command.startsWith("orcvillage"))
			    {
			    activeChar.teleToLocation(-44429, -113596, -220);
			    }
			    else if(command.startsWith("darkelvenvillage"))
			    {
			    activeChar.teleToLocation(11620, 16780, -4662);
			    }
			    else if(command.startsWith("elvenvillage"))
			    {
			    activeChar.teleToLocation(47050, 50767, -2996);
			    }
			    else if(command.startsWith("dwarvenvillage"))
			    {
			    activeChar.teleToLocation(115526, -178660, -945);
			    }
			    else if(command.startsWith("heine"))
			    {
			    activeChar.teleToLocation(111396, 219254, -3546);
			    }
			    else if(command.startsWith("huntersvillage"))
			    {
			    activeChar.teleToLocation(116440, 76320, -2730);
			    }
			    else if(command.startsWith("floran"))
			    {
			    activeChar.teleToLocation(17144, 170156, -3502);
			    }
			    else if(command.startsWith("goddard"))
			    {
			    activeChar.teleToLocation(147720, -55560, -2735);
			    }
			    else if(command.startsWith("rune"))
			    {
			    activeChar.teleToLocation(43848, -48033, -797);
			    }
			    else if(command.startsWith("valakas"))
			    {
			    activeChar.teleToLocation(213604, -114795, -1636);
			    }
			    else if(command.startsWith("baium"))
			    {
			    activeChar.teleToLocation(115213, 16623, 10080);
			    }
			    else if(command.startsWith("queenant"))
			    {
			    activeChar.teleToLocation(-21610, 181594, -5734);
			    }
			    else if(command.startsWith("zaken"))
			    {
			    activeChar.teleToLocation(55312, 219168, -3223);
			    }
			    else if(command.startsWith("orfen"))
			    {
			    activeChar.teleToLocation(43021, 17378, -4394);
			    }
			    else if(command.startsWith("core"))
			    {
			    activeChar.teleToLocation(17682, 111439, -6584);
			    }
			    else if(command.startsWith("antharas"))
			    {
			    activeChar.teleToLocation(185708, 114298, -8221);
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