package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class L2AntQueenZone extends L2ZoneType
{
	public L2AntQueenZone(final int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(final L2Character character)
	{
		if ((character.isPlayer()) && (character.getLevel() >= Config.MAX_LEVEL_FOR_AQ_ZONE) && (!character.isGM()))
		{
			character.setInsideZone(ZoneId.ALTERED, true);
			_log.info("Player " + character.getName() + " esta tentando entrar na zona de Ant Queen com level acima do permitido " + Config.MAX_LEVEL_FOR_AQ_ZONE + ".");
			character.teleToLocation(47591, 185820, -3486);
			character.sendPacket(new ExShowScreenMessage("Voce nao pode entrar, se voce tiver no level " + Config.MAX_LEVEL_FOR_AQ_ZONE + " ou superior", 5000));
		}
		else if (character.isServitor() && (character.getLevel() >= Config.MAX_LEVEL_FOR_AQ_ZONE) && (!character.isGM()))
		{
			character.setInsideZone(ZoneId.ALTERED, true);
			_log.info("Player " + character.getName() + " esta tentando entrar na zona de Ant Queen com summon com level acima do permitido " + Config.MAX_LEVEL_FOR_AQ_ZONE + ".");
			character.teleToLocation(47591, 185820, -3486);
			character.sendPacket(new ExShowScreenMessage("Voce nao pode entrar, se voce tiver no level " + Config.MAX_LEVEL_FOR_AQ_ZONE + " ou superior", 5000));
		}
		else if (character.isPet() && (character.getLevel() >= Config.MAX_LEVEL_FOR_AQ_ZONE) && (!character.isGM()))
		{
			character.setInsideZone(ZoneId.ALTERED, true);
			_log.info("Player " + character.getName() + " esta tentando entrar na zona de Ant Queen com Pet com level acima do permitido " + Config.MAX_LEVEL_FOR_AQ_ZONE + ".");
			character.teleToLocation(47591, 185820, -3486);
			character.sendPacket(new ExShowScreenMessage("Voce nao pode entrar, se voce tiver no level " + Config.MAX_LEVEL_FOR_AQ_ZONE + " ou superior", 5000));
		}
	}
	
	@Override
	protected void onExit(final L2Character character)
	{
		if (character.isPlayer() && (character.getLevel() >= Config.MAX_LEVEL_FOR_AQ_ZONE) && (!character.isGM()))
		{
			character.setInsideZone(ZoneId.ALTERED, false);
			_log.info("Player " + character.getName() + " esta deixando a zona de Ant Queen.");
		}
	}
	
	@Override
	public void onDieInside(final L2Character character)
	{
	}
	
	@Override
	public void onReviveInside(final L2Character character)
	{
	}
}