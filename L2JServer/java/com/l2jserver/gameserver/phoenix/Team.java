package com.l2jserver.gameserver.phoenix;

/**
 * @author Rizel
 */
public class Team
{
	private int _score;
	private final String _name;
	private final int[] _nameColor;
	private final int[] _startPos;
	private final int _id;
	
	public Team(int id, String name, int[] color, int[] startPos)
	{
		_id = id;
		_score = 0;
		_name = name;
		_nameColor = color;
		_startPos = startPos;
	}
	
	public String getHexaColor()
	{
		String hexa;
		Integer i1 = _nameColor[0];
		Integer i2 = _nameColor[1];
		Integer i3 = _nameColor[2];
		hexa = (i1 > 15 ? Integer.toHexString(i1) : "0" + Integer.toHexString(i1)) + (i2 > 15 ? Integer.toHexString(i2) : "0" + Integer.toHexString(i2)) + (i3 > 15 ? Integer.toHexString(i3) : "0" + Integer.toHexString(i3));
		return hexa;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getScore()
	{
		return _score;
	}
	
	public int[] getTeamColor()
	{
		return _nameColor;
	}
	
	public int[] getTeamPos()
	{
		return _startPos;
	}
	
	public void increaseScore()
	{
		_score++;
	}
	
	public void increaseScore(int ammount)
	{
		_score += ammount;
	}
	
	public void setScore(int ammount)
	{
		_score = ammount;
	}
}
