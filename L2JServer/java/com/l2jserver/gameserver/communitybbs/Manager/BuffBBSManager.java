package com.l2jserver.gameserver.communitybbs.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import com.l2jserver.Config;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class BuffBBSManager extends BaseBBSManager
{

    private static BuffBBSManager _instance = new BuffBBSManager();
    public int allskillid_1[][];

    public BuffBBSManager()
    {
	Load();
    }
	
	public static BuffBBSManager getInstance()
    {
        if(_instance == null)
            _instance = new BuffBBSManager();
        return _instance;
    }
	public void Load()
	{
        
        Connection connn = null;
        try
        {
            connn = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement count = connn.prepareStatement("SELECT COUNT(*) FROM communitybuff");
            ResultSet countt = count.executeQuery();
            countt.next();
            allskillid_1 = new int[countt.getInt(1)][4];
            PreparedStatement table = connn.prepareStatement("SELECT * FROM communitybuff");
            ResultSet skills = table.executeQuery();
            for(int i = 0; i < allskillid_1.length; i++)
            {
                skills.next();
                allskillid_1[i][0] = skills.getInt(2);
                allskillid_1[i][1] = skills.getInt(3);
                allskillid_1[i][2] = skills.getInt(4);
                allskillid_1[i][3] = skills.getInt(5);
            }

            count.close();
            countt.close();
            skills.close();
            table.close();
			connn.close();
        }
        catch(Exception ignored) { }
	}
    @Override
	public void parsecmd(String command, L2PcInstance activeChar)
    {
		String[] parts = command.split("_");
		boolean petbuff = false;
        if(activeChar.isDead() || activeChar.isAlikeDead() || TvTEvent.isStarted() || activeChar.isInSiege() || activeChar.isCastingNow() || activeChar.isInCombat() || activeChar.isAttackingNow() || activeChar.isInOlympiadMode() || activeChar.isInJail() || activeChar.isFlying() || activeChar.getKarma() > 0 || activeChar.isInDuel())
        {
        	activeChar.sendMessage("can not be used in period event");
			return;
        } 
		if (!(parts[2].startsWith("buff")))
			return;
		String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/60.htm");
		separateAndSend(content, activeChar);
		
            if(parts[4] != null && parts[4].startsWith(" Player"))
                petbuff = false;
            if(parts[4] != null && parts[4].startsWith(" Pet"))
                petbuff = true;
            if(parts[3].startsWith("FIGHERLIST"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        FIGHERLIST(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    FIGHERLIST(activeChar, petbuff);
                }
            if(parts[3].startsWith("DANCEFIGHTERLIST"))
            	{
            		if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        DANCEFIGHTERLIST(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    DANCEFIGHTERLIST(activeChar, petbuff);
                }
            	}
            if(parts[3].startsWith("MAGELIST"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        MAGELIST(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    MAGELIST(activeChar, petbuff);
                }
            if(parts[3].startsWith("DANCEMAGELIST"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        DANCEMAGELIST(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    DANCEMAGELIST(activeChar, petbuff);
                }
            if(parts[3].startsWith("SAVE"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        SAVE(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                } else
                {
                    SAVE(activeChar, petbuff);
                }
            if(parts[3].startsWith("BUFF"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        BUFF(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                } else
                {
                    BUFF(activeChar, petbuff);
                }
            if(parts[3].startsWith("CANCEL"))
                if(Config.BUFF_PEACE)
                {
                	
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        CANCEL(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    CANCEL(activeChar, petbuff);
                }
            if(parts[3].startsWith("REGMP"))
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                        REGMP(activeChar, petbuff);
                    else
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                }
				else
                {
                    REGMP(activeChar, petbuff);
                }
            for(int key = 0; key < allskillid_1.length; key++)
            {
                L2Skill skill;
                int skilllevel;
                if(Config.BUFF_PEACE)
                {
                    if(activeChar.isInsideZone(L2Character.ZONE_PEACE))
                    {
                        skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[key][0]);
                        skill = SkillTable.getInstance().getInfo(allskillid_1[key][0], skilllevel);
                        if(parts[3].startsWith(skill.getName()))
                            SKILL(activeChar, petbuff, key, skill);
                    } else
                    {
                        activeChar.sendPacket(new ExShowScreenMessage("Sorry, you not town!!", 3000));
                    }
                    continue;
                }
                skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[key][0]);
                skill = SkillTable.getInstance().getInfo(allskillid_1[key][0], skilllevel);
                if(parts[3].startsWith(skill.getName()))
                    SKILL(activeChar, petbuff, key, skill);
            }
    }

    private void FIGHERLIST(L2PcInstance activeChar, boolean petbuff)
    {
        int arr$[][] = allskillid_1;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int aSkillid[] = arr$[i$];
            if(aSkillid[1] != 1 && aSkillid[1] != 3)
                continue;
            L2Skill skill;
            int skilllevel;
            if(Config.MANI_BUFF)
            {
                if(activeChar.destroyItemByItemId(null, aSkillid[3], aSkillid[2], activeChar, true))
                {
                    skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
                    skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
                    if(!petbuff)
                        skill.getEffects(activeChar, activeChar);
                    else
                        skill.getEffects(activeChar.getPet(), activeChar.getPet());
                }
				else
                {
                    activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                }
                continue;
            }
            skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
            skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
            if(!petbuff)
                skill.getEffects(activeChar, activeChar);
            else
                skill.getEffects(activeChar.getPet(), activeChar.getPet());
        }

    }

    private void DANCEFIGHTERLIST(L2PcInstance activeChar, boolean petbuff)
    {
        int arr$[][] = allskillid_1;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int aSkillid[] = arr$[i$];
            if(aSkillid[1] != 4 && aSkillid[1] != 6)
                continue;
            L2Skill skill;
            int skilllevel;
            if(Config.MANI_BUFF)
            {
                if(activeChar.destroyItemByItemId(null, aSkillid[3], aSkillid[2], activeChar, true))
                {
                    skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
                    skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
                    if(!petbuff)
                        skill.getEffects(activeChar, activeChar);
                    else
                        skill.getEffects(activeChar.getPet(), activeChar.getPet());
                }
				else
                {
                    activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                }
                continue;
            }
            skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
            skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
            if(!petbuff)
                skill.getEffects(activeChar, activeChar);
            else
                skill.getEffects(activeChar.getPet(), activeChar.getPet());
        }

    }

    private void MAGELIST(L2PcInstance activeChar, boolean petbuff)
    {
        int arr$[][] = allskillid_1;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int aSkillid[] = arr$[i$];
            if(aSkillid[1] != 2 && aSkillid[1] != 3)
                continue;
            L2Skill skill;
            int skilllevel;
            if(Config.MANI_BUFF)
            {
                if(activeChar.destroyItemByItemId(null, aSkillid[3], aSkillid[2], activeChar, true))
                {
                    skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
                    skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
                    if(!petbuff)
                        skill.getEffects(activeChar, activeChar);
                    else
                        skill.getEffects(activeChar.getPet(), activeChar.getPet());
                }
				else
                {
                    activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                }
                continue;
            }
            skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
            skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
            if(!petbuff)
                skill.getEffects(activeChar, activeChar);
            else
                skill.getEffects(activeChar.getPet(), activeChar.getPet());
        }

    }

    private void DANCEMAGELIST(L2PcInstance activeChar, boolean petbuff)
    {
        int arr$[][] = allskillid_1;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int aSkillid[] = arr$[i$];
            if(aSkillid[1] != 5 && aSkillid[1] != 6)
                continue;
            L2Skill skill;
            int skilllevel;
            if(Config.MANI_BUFF)
            {
                if(activeChar.destroyItemByItemId(null, aSkillid[3], aSkillid[2], activeChar, true))
                {
                    skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
                    skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
                    if(!petbuff)
                        skill.getEffects(activeChar, activeChar);
                    else
                        skill.getEffects(activeChar.getPet(), activeChar.getPet());
                }
				else
                {
                    activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                }
                continue;
            }
            skilllevel = SkillTable.getInstance().getMaxLevel(aSkillid[0]);
            skill = SkillTable.getInstance().getInfo(aSkillid[0], skilllevel);
            if(!petbuff)
                skill.getEffects(activeChar, activeChar);
            else
                skill.getEffects(activeChar.getPet(), activeChar.getPet());
        }

    }

    private void BUFF(L2PcInstance activeChar, boolean petbuff)
    {
        Connection con = null;
        try
        {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM community_skillsave WHERE charId=?;");
            statement.setInt(1, activeChar.getObjectId());
            ResultSet rcln = statement.executeQuery();
            rcln.next();
            if(!petbuff)
            {
                char allskills[] = rcln.getString(2).toCharArray();
                if(allskills.length == allskillid_1.length)
                {
                    for(int i = 0; i < allskillid_1.length; i++)
                        if(allskills[i] == '1')
                            if(Config.MANI_BUFF)
                            {
                                if(activeChar.destroyItemByItemId(null, allskillid_1[i][3], allskillid_1[i][2], activeChar, true))
                                {
                                    int skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[i][0]);
                                    L2Skill skill = SkillTable.getInstance().getInfo(allskillid_1[i][0], skilllevel);
                                    skill.getEffects(activeChar, activeChar);
                                    activeChar.getLevel();
                                } else
                                {
                                    activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                                }
                            } else
                            {
                                int skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[i][0]);
                                L2Skill skill = SkillTable.getInstance().getInfo(allskillid_1[i][0], skilllevel);
                                skill.getEffects(activeChar, activeChar);
                            }

                }
            }
			else
            {
                char petskills[] = rcln.getString(3).toCharArray();
                if(petskills.length == allskillid_1.length)
                {
                    for(int i = 0; i < allskillid_1.length; i++)
                    {
                        if(petskills[i] != '1')
                            continue;
                        if(Config.MANI_BUFF)
                        {
                            if(activeChar.destroyItemByItemId(null, allskillid_1[i][3], allskillid_1[i][2], activeChar, true))
                            {
                                int skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[i][0]);
                                L2Skill skill = SkillTable.getInstance().getInfo(allskillid_1[i][0], skilllevel);
                                skill.getEffects(activeChar.getPet(), activeChar.getPet());
                            } else
                            {
                                activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
                            }
                        } else
                        {
                            int skilllevel = SkillTable.getInstance().getMaxLevel(allskillid_1[i][0]);
                            L2Skill skill = SkillTable.getInstance().getInfo(allskillid_1[i][0], skilllevel);
                            skill.getEffects(activeChar.getPet(), activeChar.getPet());
                        }
                    }

                }
            }
            rcln.close();
            statement.close();
        }
        catch(Exception ignored)
        {
            try
            {
                if(con != null)
                    con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }

        }
        try
        {
            if(con != null)
                con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private void CANCEL(L2PcInstance activeChar, boolean petbuff)
    {
        if(!petbuff)
            activeChar.stopAllEffects();
        else
            activeChar.getPet().stopAllEffects();
    }

    private void REGMP(L2PcInstance activeChar, boolean petbuff)
    {
        if(!petbuff)
            activeChar.setCurrentMp(activeChar.getMaxMp());
        else
            activeChar.getPet().setCurrentMp(activeChar.getPet().getMaxMp());
    }

    private void SKILL(L2PcInstance activeChar, boolean petbuff, int key, L2Skill skill)
    {
        if(Config.MANI_BUFF)
        {
            if(activeChar.destroyItemByItemId(null, allskillid_1[key][3], allskillid_1[key][2], activeChar, true))
            {
                if(!petbuff)
                    skill.getEffects(activeChar, activeChar);
                else
                    skill.getEffects(activeChar.getPet(), activeChar.getPet());
            }
			else
            {
                activeChar.sendPacket(new ExShowScreenMessage("Sorry, not item!!", 3000));
            }
        }
		else
        if(!petbuff)
            skill.getEffects(activeChar, activeChar);
        else
            skill.getEffects(activeChar.getPet(), activeChar.getPet());
    }

    private void SAVE(L2PcInstance activeChar, boolean petbuff)
    {
        Connection con = null;
        try
        {
            con = L2DatabaseFactory.getInstance().getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT COUNT(*) FROM community_skillsave WHERE charId=?;");
            stat.setInt(1, activeChar.getObjectId());
            ResultSet rset = stat.executeQuery();
            rset.next();
            String allbuff = "";
            if(!petbuff)
            {
                L2Effect skill[] = activeChar.getAllEffects();
                boolean flag = true;
                int arr$[][] = allskillid_1;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    int aSkillid[] = arr$[i$];
                    for(int j = 0; j < skill.length; j++)
                    {
                        if(aSkillid[0] == skill[j].getId())
                        {
                            allbuff = (new StringBuilder()).append(allbuff).append(1).toString();
                            flag = false;
                        }
                        if(j == skill.length - 1 && flag)
                            allbuff = (new StringBuilder()).append(allbuff).append(0).toString();
                    }

                    flag = true;
                }

                if(rset.getInt(1) == 0)
                {
                    PreparedStatement statement1 = con.prepareStatement("INSERT INTO community_skillsave (charId,skills) values (?,?)");
                    statement1.setInt(1, activeChar.getObjectId());
                    statement1.setString(2, allbuff);
                    statement1.execute();
                    statement1.close();
                }
				else
                {
                    PreparedStatement statement = con.prepareStatement("UPDATE community_skillsave SET skills=? WHERE charId=?;");
                    statement.setString(1, allbuff);
                    statement.setInt(2, activeChar.getObjectId());
                    statement.execute();
                    statement.close();
                }
            }
			else
            {
                L2Effect skill[] = activeChar.getPet().getAllEffects();
                boolean flag = true;
                int arr$[][] = allskillid_1;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    int aSkillid[] = arr$[i$];
                    for(int j = 0; j < skill.length; j++)
                    {
                        if(aSkillid[0] == skill[j].getId())
                        {
                            allbuff = (new StringBuilder()).append(allbuff).append(1).toString();
                            flag = false;
                        }
                        if(j == skill.length - 1 && flag)
                            allbuff = (new StringBuilder()).append(allbuff).append(0).toString();
                    }

                    flag = true;
                }

                if(rset.getInt(1) == 0)
                {
                    PreparedStatement statement1 = con.prepareStatement("INSERT INTO community_skillsave (charId,pet) values (?,?)");
                    statement1.setInt(1, activeChar.getObjectId());
                    statement1.setString(2, allbuff);
                    statement1.execute();
                    statement1.close();
                } else
                {
                    PreparedStatement statement = con.prepareStatement("UPDATE community_skillsave SET pet=? WHERE charId=?;");
                    statement.setString(1, allbuff);
                    statement.setInt(2, activeChar.getObjectId());
                    statement.execute();
                    statement.close();
                }
            }
            rset.close();
            stat.close();
        }
        catch(Exception ignored)
        {
            try
            {
                if(con != null)
                    con.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            if(con != null)
                con.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
	public void parsewrite(String s, String s1, String s2, String s3, String s4, L2PcInstance l2pcinstance)
    {
    }
}