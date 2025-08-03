package com.l2jserver.gameserver.phoenix.models;

import javolution.text.TextBuilder;

import com.l2jserver.gameserver.phoenix.AbstractEvent;

/**
 * @author Rizel
 */
public abstract class EventStatus
{
	protected AbstractEvent event;
	
	protected TextBuilder sb = new TextBuilder();
	
	public abstract String generateList();
}
