package handlers.voicedcommandhandlers;
 
import javolution.text.TextBuilder;
import java.text.SimpleDateFormat;
 
import com.l2jserver.Config;
import com.l2jserver.gameserver.Prem;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
 
public class Premium implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = { 
        "premium" 
    };
 
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (command.startsWith("premium"))
        {   
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            if(activeChar.getPremiumService() == 0)
            {
                NpcHtmlMessage preReply = new NpcHtmlMessage(5);
                TextBuilder html3 = new TextBuilder("<html><body><title>Normal Account</title><center>");
                html3.append("<table>");
                html3.append("<tr><td><center>Your account :<font color=\"LEVEL\">Normal<br></font></td></tr>");
                html3.append("<tr><td><center>Details<br1></td></tr>");
                html3.append("<tr><td>Rate EXP: <font color=\"LEVEL\"> x"+Config.RATE_XP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate SP: <font color=\"LEVEL\"> x"+Config.RATE_SP+"  <br1></font></td></tr>");
                html3.append("<tr><td>Rate EXP Party: <font color=\"LEVEL\"> x"+Config.RATE_PARTY_XP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate SP Party: <font color=\"LEVEL\"> x"+Config.RATE_PARTY_SP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Spoil: <font color=\"LEVEL\"> x"+Config.RATE_DROP_SPOIL+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Adena: <font color=\"LEVEL\"> x15 <br1></font></td></tr>");
                html3.append("<tr><td>Rate Drop: <font color=\"LEVEL\"> x"+Config.RATE_DROP_ITEMS+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Quest: <font color=\"LEVEL\"> x"+Config.RATE_QUEST_DROP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Raid Drop: <font color=\"LEVEL\"> x"+Config.RATE_DROP_ITEMS_BY_RAID+" <br1></font></td></tr>");
                html3.append("<tr><td>Expires : <font color=\"00A5FF\"> Never (Normal Account)<br1></font></td></tr>");
                html3.append("<tr><td>Current Date : <font color=\"70FFCA\"> :"+ String.valueOf(format.format(System.currentTimeMillis()))+ " <br><br></font></td></tr><br><br1><br1>");
                html3.append("<tr><td><font color=\"LEVEL\"><center>Premium Info & Rules<br1></font></td></tr>");
                html3.append("<tr><td>Upgrade to Premium Account : <font color=\"70FFCA\"> http://www.l2champiom.com</font></td></tr>");
                html3.append("<tr><td>Premium Account : <font color=\"70FFCA\"> Benefits<br1></font></td></tr>");
                html3.append("<tr><td>Rate EXP: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_XP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate SP: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_SP+"  <br1></font></td></tr>");
                html3.append("<tr><td>Rate Spoil: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_SPOIL+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Adena: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Drop: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Quest: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_QUEST+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Raid Drop: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS_BY_RAID+" <br1></font></td></tr>");
                html3.append("<tr><td> <font color=\"70FFCA\">1.Premium  benefits CAN NOT BE TRANSFERED.<br1></font></td></tr><br>");
                html3.append("<tr><td> <font color=\"70FFCA\">2.Premium benefits effect ALL characters in same account.<br1></font></td></tr><br>");
                html3.append("<tr><td> <font color=\"70FFCA\">3.Does not effect Party members.</font></td></tr>");
                html3.append("</table>");
                html3.append("</center></body></html>");
               
                preReply.setHtml(html3.toString());
                activeChar.sendPacket(preReply);
            }
            else
            {
                long _end_prem_date;
                _end_prem_date = Prem.getInstance().getPremServiceData(activeChar.getAccountName());
                NpcHtmlMessage preReply = new NpcHtmlMessage(5);
               
                TextBuilder html3 = new TextBuilder("<html><body><title>Premium Account Details</title><center>");
                html3.append("<table>");
                html3.append("<tr><td><center>Thank you for supporting L2Ghoul.<br><br></td></tr>");
                html3.append("<tr><td><center>Your account : <font color=\"LEVEL\">Premium<br></font></td></tr>");
                html3.append("<tr><td><center>Details<br1></center></td></tr>");
                html3.append("<tr><td>Rate EXP: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_XP+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate SP: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_SP+"  <br1></font></td></tr>");
                html3.append("<tr><td>Rate Spoil: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_SPOIL+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Adena: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Drop: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Quest: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_QUEST+" <br1></font></td></tr>");
                html3.append("<tr><td>Rate Raid Drop: <font color=\"LEVEL\"> x"+Config.PREMIUM_RATE_DROP_ITEMS_BY_RAID+" <br1></font></td></tr>");
                html3.append("<tr><td>Expires : <font color=\"00A5FF\"> "+ String.valueOf(format.format(_end_prem_date))+" (Premium added)</font></td></tr>");
                html3.append("<tr><td>Current Date : <font color=\"70FFCA\"> :"+ String.valueOf(format.format(System.currentTimeMillis()))+ " <br><br></font></td></tr>");
                html3.append("<tr><td><font color=\"LEVEL\"><center>Premium Info & Rules<br1></font></center></td></tr>");
                html3.append("<tr><td><font color=\"70FFCA\">1.Premium Account CAN NOT BE TRANSFERED.<br1></font></td></tr>");
                html3.append("<tr><td><font color=\"70FFCA\">2.Premium Account effects ALL characters in same account.<br1></font></td></tr>");
                html3.append("<tr><td><font color=\"70FFCA\">3.Does not effect Party members.<br><br></font></td></tr>");
                html3.append("</table>");
                html3.append("</center></body></html>");
               
                preReply.setHtml(html3.toString());
                activeChar.sendPacket(preReply);
 
            }
        }
        return true;
    }
 
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}