package handlers.voicedcommandhandlers;

import gov.nasa.worldwind.formats.dds.DDSConverter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.l2jserver.Config;
import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.PledgeCrest;
import com.l2jserver.gameserver.skills.AbnormalEffect;

/**
 *
 * @author Pipiou211
 *
 */
public class Captcha implements IVoicedCommandHandler //when you click on confirm, also this code is running or something else? this, only, and just //unpara the targetpl
{
	private static final String[] _voicedCommands =
	{
		"captcha"
	};
	
    public static StringBuilder finalString = new StringBuilder();
	NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
	private static BufferedImage generateCaptcha()
	{    
		   Color textColor = new Color(98, 213, 43);
		   Color circleColor = new Color(98, 213, 43);
		   Font textFont = new Font("comic sans ms", Font.BOLD, 24);
		   int charsToPrint = 5;
		   int width = 256;
		   int height = 64;
		   int circlesToDraw = 8;
		   float horizMargin = 20.0f;
		   double rotationRange = 0.7; // this is radians
		   BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		   Graphics2D g = (Graphics2D) bufferedImage.getGraphics();

		   //Draw an oval
		   g.setColor(new Color(30,31,31));
		   g.fillRect(0, 0, width, height);

		   // lets make some noisey circles
		   g.setColor(circleColor);
		   for ( int i = 0; i < circlesToDraw; i++ ) {
		     int circleRadius = (int) (Math.random() * height / 2.0);
		     int circleX = (int) (Math.random() * width - circleRadius);
		     int circleY = (int) (Math.random() * height - circleRadius);
		     g.drawOval(circleX, circleY, circleRadius * 2, circleRadius * 2);
		   }

		   g.setColor(textColor);
		   g.setFont(textFont);

		   FontMetrics fontMetrics = g.getFontMetrics();
		   int maxAdvance = fontMetrics.getMaxAdvance();
		   int fontHeight = fontMetrics.getHeight();
		   
		   // Suggestions ----------------------------------------------------------------------
		   // i removed 1 and l and i because there are confusing to users...
		   // Z, z, and N also get confusing when rotated
		   // 0, O, and o are also confusing...
		   // lowercase G looks a lot like a 9 so i killed it
		   // this should ideally be done for every language...
		   // i like controlling the characters though because it helps prevent confusion
		   // So recommended chars are:
		   // String elegibleChars = "1234567890";
		   // Suggestions ----------------------------------------------------------------------
		   String elegibleChars = "1234567890";
		   char[] chars = elegibleChars.toCharArray();

		   float spaceForLetters = -horizMargin * 2 + width;
		   float spacePerChar = spaceForLetters / (charsToPrint - 1.0f);

		   for ( int i = 0; i < charsToPrint; i++ ) {
		     double randomValue = Math.random();
		     int randomIndex = (int) Math.round(randomValue * (chars.length - 1));
		     char characterToShow = chars[randomIndex];
		     finalString.append(characterToShow);

		     // this is a separate canvas used for the character so that
		     // we can rotate it independently
		     int charWidth = fontMetrics.charWidth(characterToShow);
		     int charDim = Math.max(maxAdvance, fontHeight);
		     int halfCharDim = (charDim / 2);

		     BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
		     Graphics2D charGraphics = charImage.createGraphics();
		     charGraphics.translate(halfCharDim, halfCharDim);
		     double angle = (Math.random() - 0.5) * rotationRange;
		     charGraphics.transform(AffineTransform.getRotateInstance(angle));
		     charGraphics.translate(-halfCharDim,-halfCharDim);
		     charGraphics.setColor(textColor);
		     charGraphics.setFont(textFont);

		     int charX = (int) (0.5 * charDim - 0.5 * charWidth);
		     charGraphics.drawString("" + characterToShow, charX, 
		                            ((charDim - fontMetrics.getAscent()) 
		                                   / 2 + fontMetrics.getAscent()));

		     float x = horizMargin + spacePerChar * (i) - charDim / 2.0f;
		     int y = ((height - charDim) / 2);
		     g.drawImage(charImage, (int) x, y, charDim, charDim, null, null);

		     charGraphics.dispose();
		   }
		   
			g.dispose();     
			
			return bufferedImage;
			}
	
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
		if (command.equalsIgnoreCase("captcha") && !activeChar.isCodeRight())
		{
							if (activeChar.getTries() > 1)
				{
				activeChar.setTries(activeChar.getTries() -1);
				//Random image file name
				int imgId = IdFactory.getInstance().getNextId();
				//Convertion from .png to .dds, and crest packed send
				try
				{
					File captcha = new File("data/captcha/captcha.png");    
					ImageIO.write(generateCaptcha(), "png", captcha);
					PledgeCrest packet = new PledgeCrest(imgId, DDSConverter.convertToDDS(captcha).array()); //Convertion to DDS where is antybot
					activeChar.sendPacket(packet);
				}
				catch (Exception e)
				{    
					_log.warning(e.getMessage());
				}
				//Paralyze, abnormal effect, invul, html with captcha output and start of the 1 min counter
				adminReply.setHtml("<html><title>Captcha Antibot System</title><body><center>Enter the 5-digits code below and click Confirm.<br><img src=\"Crest.crest_" + Config.SERVER_ID + "_" + imgId + "\" width=256 height=64><br><font color=\"888888\">(There are only english uppercase letters.)</font><br1><font color=\"FF0000\">Tries Left: " + activeChar.getTries() +"</font><br><edit var=\"antibot\" width=110><br><button value=\"Confirm\" action=\"bypass -h voice .antibot $antibot\" width=80 height=26 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br>If you close by mistake this window,<br1>you can re-open it by typing \".captcha\" on Chat.<br1>You have 3 minutes to answer or you<br1>will get jailed.<br1>You have 3 tries, if you will<br1>answer wrong to all of them you<br1>will get punished.</center></body></html>");
				activeChar.sendPacket(adminReply);
				activeChar.setCode(finalString);
				finalString.replace(0, 5, "");
				return false;
				}
				activeChar.setTries(3);
				//here will run method with jailing player
				activeChar.stopAbnormalEffect(AbnormalEffect.REAL_TARGET);
				npcHtmlMessage.setHtml("<html><title>Captcha Antibot System</title><body><center><font color=\"FF0000\">You have wasted your Tries.<br><br></font><font color=\"66FF00\"><center></font><font color=\"FF0000\">You will be jailed.</font><br><button value=\"Exit\" action=\"bypass -h npc_%objectId%_Quest\" width=45 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"></center></body></html>");
				if (activeChar.isFlyingMounted())
					activeChar.untransform();
				activeChar.setPunishLevel(L2PcInstance.PunishLevel.JAIL, Config.ANTIBOT_TIME_JAIL);
				activeChar.setIsInvul(false);
				activeChar.setIsParalyzed(false);
				activeChar.sendPacket(npcHtmlMessage);
			return false;
		}
		else
		{
			return false;
		}
		//return false;
	}
	
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}