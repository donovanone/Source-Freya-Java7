/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.skills.effects;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;
import com.l2jserver.gameserver.templates.skills.L2SkillType;

/**
 * @author Lambda
 *
 */
public final class EffectDeflectBuff extends L2Effect
{
    /**
    * @param env
    * @param template
    */
    public EffectDeflectBuff(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    /*
    * (non-Javadoc)
    *
    * @see com.l2jserver.gameserver.model.L2Effect#getEffectType()
    */
    @Override
    public L2EffectType getEffectType()
    {
        return L2EffectType.PREVENT_BUFF;
    }

    /*
    * (non-Javadoc)
    *
    * @see com.l2jserver.gameserver.model.L2Effect#onActionTime()
    */
    @Override
    public boolean onActionTime()
    {
        // Only cont skills shouldn't end
        if(getSkill().getSkillType() != L2SkillType.CONT)
            return false;
    
        double manaDam = calc();
    
        if(manaDam > getEffected().getCurrentMp())
        {
            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
            getEffected().sendPacket(sm);
            return false;
        }
    
        getEffected().reduceCurrentMp(manaDam);
        return true;
    }

    /*
    * (non-Javadoc)
    *
    * @see com.l2jserver.gameserver.model.L2Effect#onStart()
    */
    @Override
    public boolean onStart()
    {
        getEffected().setIsBuffProtected(true);
        return true;
    }

    /*
    * (non-Javadoc)
    * @see com.l2jserver.gameserver.model.L2Effect#onExit()
    */
    @Override
    public void onExit()
    {
        getEffected().setIsBuffProtected(false);
    }
}
