package com.l2jserver.gameserver.model.entity;

import java.util.logging.Logger;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.util.Rnd;


public class PcPoint implements Runnable
{
        Logger _log = Logger.getLogger(PcPoint.class.getName());
        private static PcPoint _instance;
        
        public static PcPoint getInstance()
        {
                if (_instance == null)
                {
                        _instance = new PcPoint();
                }
                
                return _instance;
        }
        
        private PcPoint()
        {
                _log.info("PcBang point event started.");
        }
        
        @Override
        public void run()
        {
                
                int score = 0;
                for (L2PcInstance activeChar : L2World.getInstance().getAllPlayers().values())
                {
                        
                        if (activeChar.getLevel() > Config.PCB_MIN_LEVEL)
                        {
                                score = Rnd.get(Config.PCB_POINT_MIN, Config.PCB_POINT_MAX);
                                
                                if (Rnd.get(100) <= Config.PCB_CHANCE_DUAL_POINT)
                                {
                                        score *= 2;
                                        
                                        activeChar.addPcBangScore(score);
                                        
                                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT_DOUBLE);
                                        sm.addNumber(score);
                                        activeChar.sendPacket(sm);
                                        sm = null;
                                        
                                        activeChar.updatePcBangWnd(score, true, true);
                                }
                                else
                                {
                                        activeChar.addPcBangScore(score);
                                        
                                        SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_PCPOINT);
                                        sm.addNumber(score);
                                        activeChar.sendPacket(sm);
                                        sm = null;
                                        
                                        activeChar.updatePcBangWnd(score, true, false);
                                }
                        }
                        
                        activeChar = null;
                }
        }
}