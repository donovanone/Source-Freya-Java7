package custom.AugmentShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;

import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;

/**
 * @author EternalSuffering
 */
public class AugmentShop extends Quest
{
	private final static int ITEM_ID = 3470;
	private final static int ITEM_COUNT = 500;
	private final static String qn = "AugmentShop";
	private final static int NPC = 36618;
	
	public AugmentShop(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addFirstTalkId(NPC);
		addStartNpc(NPC);
		addTalkId(NPC);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("active"))
		{
			htmltext = "active.htm";
		}
		
		else if (event.equalsIgnoreCase("passive"))
		{
			htmltext = "passive.htm";
		}
		
		else if (event.equalsIgnoreCase("chance"))
		{
			htmltext = "chance.htm";
		}
		
		else
		{
			
			updateAugment(player, Integer.parseInt(event.substring(0, 5)), Integer.parseInt(event.substring(6, 10)), Integer.parseInt(event.substring(11, 13)));
		}
		
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState qs = player.getQuestState(qn);
		if (qs == null)
		{
			qs = newQuestState(player);
		}
		htmltext = "main.htm";
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new AugmentShop(-1, qn, "AugmentShop");
	}
	
	private static void updateAugment(L2PcInstance player, int attribute, int skill, int level)
	{
		L2ItemInstance item = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if (player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) == null)
		
		{
			player.sendMessage("Usted Tiene Que Equiparce Su Arma.");
			return;
		}
		
		if (player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).isAugmented())
		{
			player.sendMessage("El Arma Ya Es Augmentada.");
			return;
		}
		
		if (player.getInventory().getInventoryItemCount(ITEM_ID, -1) < ITEM_COUNT)
		{
			player.sendMessage("Usted No Tiene Los Items Necesarios.");
			return;
		}
		
		Connection con = null;
		try
		{
			player.destroyItemByItemId("Consume", ITEM_ID, ITEM_COUNT, player, true);
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("REPLACE INTO item_attributes VALUES(?,?,?,?)");
			statement.setInt(1, item.getObjectId());
			
			statement.setInt(2, (attribute * 65536) + 1);
			statement.setInt(3, skill);
			statement.setInt(4, level);
			
			statement.executeUpdate();
			player.sendMessage("Augmentada Con Exito Debes Hacer Restart Ahora.");
			statement.close();
			
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Could not augment item: " + item.getObjectId() + " ", e);
		}
		finally
		{
			L2DatabaseFactory.close(con);
		}
	}
	
}