package handlers.voicedcommandhandlers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.l2jserver.gameserver.handler.IVoicedCommandHandler;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.util.Util;

/**
 * @author of adaptation and fixes: @Frost TODO to improve: agregar mas debugs.
 */

/* Creada clase del java, sino no funciona xD */
public class CombineTalism implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"combinetalismans"
	};
	
	/* Creada clase del voice, sino no funciona xD */
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		// Else borrado, no debe ir.
		if (command.equalsIgnoreCase("combinetalismans"))
		{
			activeChar.sendMessage("Combinando talismanes...");
			try
			{
				// TalismanId, List<TalismansWithThisId>
				Map<Integer, List<L2ItemInstance>> talismans = new HashMap<>();
				
				for (L2ItemInstance item : activeChar.getInventory().getItems())
				{
					if ((item == null) || !item.isShadowItem())
					{
						continue;
					}
					
					int itemId = item.getId();
					// Get only the talismans.
					if (!Util.contains(TALISMAN_IDS, itemId))
					{
						continue;
					}
					
					if (!talismans.containsKey(itemId))
					{
						talismans.put(itemId, new ArrayList<L2ItemInstance>());
					}
					
					talismans.get(itemId).add(item);
				}
				
				activeChar.sendMessage("... resultados:");
				
				// Now same talismans are under 1 list. Loop this list to combine them.
				for (Entry<Integer, List<L2ItemInstance>> n3 : talismans.entrySet())
				{
					List<L2ItemInstance> sameTalismans = n3.getValue();
					if (sameTalismans.size() <= 1)
					{
						continue;
					}
					
					List<L2ItemInstance> talismansToCharge = new ArrayList<>(); // The talisman(s) that isnt(arent) going to be destroyed, but charged.
					
					// First, find the equipped talisman, it is with charge priority.
					for (L2ItemInstance talisman : sameTalismans)
					{
						if (talisman.isEquipped())
						{
							talismansToCharge.add(talisman); // Add to the chargable talismans.
							sameTalismans.remove(talisman); // and remove it from the list, because we will loop it again and we dont want that item there.
						}
					}
					
					if (talismansToCharge.isEmpty())
					{
						talismansToCharge.add(sameTalismans.remove(0));
					}
					
					// Second loop, charge the talismans.
					int index = 0;
					for (L2ItemInstance talisman : sameTalismans)
					{
						if (index >= talismansToCharge.size())
						{
							index = 0;
						}
						
						L2ItemInstance talismanToCharge = talismansToCharge.get(index++);
						int chargeMana = talisman.getMana();
						if (activeChar.destroyItem("Talisman Charge Delete", talisman.getObjectId(), 1, talismanToCharge, false))
						{
							talismanToCharge.decreaseMana(false, -chargeMana); // Minus in decrease = increase :P
						}
					}
					/*
					 * Esto sirve para avisarle al jugador que talismanes se combinaron bien. TODO: No recuerdo como es el nuevo metodo para los DummyItem. Necesita modificarse.
					 */
					// String talismanIdName = ItemData.getInstance().createDummyItem(n3.getKey()).getName();
					// activeChar.sendMessage(talismanIdName + " has successfully combined.");
					activeChar.sendMessage("Combinacion satisfactoria!");
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("There was a problem while combining your talismans, please consult with an admin, and tell him this date: " + (new Date(System.currentTimeMillis())));
			}
		}
		/* Creado return false, sino no funciona */
		activeChar.sendMessage("Gracias por confiar en nosotros");
		return false;
	}
	
	public static final int[] TALISMAN_IDS =
	{
		9914, // Blue Talisman of Power
		9915, // Blue Talisman of Wild Magic
		9916, // Blue Talisman of Defense
		9917, // Red Talisman of Minimum Clarity
		9918, // Red Talisman of Maximum Clarity
		9919, // Blue Talisman of Reflection
		9920, // Blue Talisman of Invisibility
		9921, // Blue Talisman - Shield Protection
		9922, // Black Talisman - Mending
		9923, // Black Talisman - Escape
		9924, // Blue Talisman of Healing
		9925, // Red Talisman of Recovery
		9926, // Blue Talisman of Defense
		9927, // Blue Talisman of Magic Defense
		9928, // Red Talisman of Mental Regeneration
		9929, // Blue Talisman of Protection
		9930, // Blue Talisman of Evasion
		9931, // Red Talisman of Meditation
		9932, // Blue Talisman - Divine Protection
		9933, // Yellow Talisman of Power
		9934, // Yellow Talisman of Violent Haste
		9935, // Yellow Talisman of Arcane Defense
		9936, // Yellow Talisman of Arcane Power
		9937, // Yellow Talisman of Arcane Haste
		9938, // Yellow Talisman of Accuracy
		9939, // Yellow Talisman of Defense
		9940, // Yellow Talisman of Alacrity
		9941, // Yellow Talisman of Speed
		9942, // Yellow Talisman of Critical Reduction
		9943, // Yellow Talisman of Critical Damage
		9944, // Yellow Talisman of Critical Dodging
		9945, // Yellow Talisman of Evasion
		9946, // Yellow Talisman of Healing
		9947, // Yellow Talisman of CP Regeneration
		9948, // Yellow Talisman of Physical Regeneration
		9949, // Yellow Talisman of Mental Regeneration
		9950, // Grey Talisman of Weight Training
		9951, // Grey Talisman of Mid-Grade Fishing
		9952, // Orange Talisman - Hot Springs CP Potion
		9953, // Orange Talisman - Elixir of Life
		9954, // Orange Talisman - Elixir of Mental Strength
		9955, // Black Talisman - Vocalization
		9956, // Black Talisman - Arcane Freedom
		9957, // Black Talisman - Physical Freedom
		9958, // Black Talisman - Rescue
		9959, // Black Talisman - Free Speech
		9960, // White Talisman of Bravery
		9961, // White Talisman of Motion
		9962, // White Talisman of Grounding
		9963, // White Talisman of Attention
		9964, // White Talisman of Bandages
		9965, // White Talisman of Protection
		9966, // White Talisman of Freedom
		10141, // Grey Talisman - Yeti Transform
		10142, // Grey Talisman - Buffalo Transform
		10158, // Grey Talisman of Upper Grade Fishing
		10416, // Blue Talisman - Explosion
		10417, // Blue Talisman - Magic Explosion
		10418, // White Talisman - Storm
		10419, // White Talisman - Darkness
		10420, // White Talisman - Water
		10421, // White Talisman - Fire
		10422, // White Talisman - Light
		10423, // Blue Talisman - Self-Destruction
		10424, // Blue Talisman - Greater Healing
		10518, // Red Talisman - Life Force
		10519, // White Talisman - Earth
		10533, // Blue Talisman - P. Atk.
		10534, // Blue Talisman - Shield Defense
		10535, // Yellow Talisman - P. Def.
		10536, // Yellow Talisman - M. Atk.
		10537, // Yellow Talisman - Evasion
		10538, // Yellow Talisman - Healing Power
		10539, // Yellow Talisman - CP Recovery Rate
		10540, // Yellow Talisman - HP Recovery Rate
		10541, // Yellow Talisman - Low Grade MP Recovery Rate
		10542, // Red Talisman - HP/CP Recovery
		10543, // Yellow Talisman - Speed
		12815, // Red Talisman - Max CP
		12816, // Red Talisman - CP Regeneration
		12817, // Yellow Talisman - Increase Force
		12818, // Yellow Talisman - Damage Transition
		14604, // Red Talisman - Territory Guardian
		14605, // Red Talisman - Territory Guard
		14810, // Blue Talisman - Buff Cancel
		14811, // Blue Talisman - Buff Steal
		14812, // Red Talisman - Territory Guard
		14813, // Blue Talisman - Lord's Divine Protection
		14814, // White Talisman - All Resistance
		17051, // Talisman - STR
		17052, // Talisman - DEX
		17053, // Talisman - CON
		17054, // Talisman - WIT
		17055, // Talisman - INT
		17056, // Talisman - MEN
		17057, // Talisman - Resistance to Stun
		17058, // Talisman - Resistance to Sleep
		17059, // Talisman - Resistance to Hold
		17060, // Talisman - Paralyze Resistance
		17061, // Talisman - ALL STAT
		22326, // Blue Talisman - Buff Cancel
		22327
	// Blue Talisman - Buff Steal
	};
	
	/* Creado _voicedCommands; sino no funciona el voice. */
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}