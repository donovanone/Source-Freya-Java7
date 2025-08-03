package com.l2jserver.gameserver.instancemanager.votereward;

import java.util.logging.Logger;

import com.l2jserver.Config;


/**
 * @author fissban
 */
public class VoteManager
{
	private static Logger _log = Logger.getLogger(VoteManager.class.getName());
	
	public VoteManager()
	{
		if (Config.VOTE_SYSTEM_ENABLE)
		{
			if (Config.VOTE_GLOBAL)
			{
				_log.info("[VoteReward] Global Enable");
				VoteRewardGlobal.getInstance();
			}
			else
			{
				if (Config.VOTE_SYSTEM_HOPZONE)
				{
					_log.info("[VoteReward] Hopzone Enable");
					VoteRewardHopzone.getInstance();
				}
				else
				{
					_log.info("[VoteReward] Hopzone Disable");
				}
				if (Config.VOTE_SYSTEM_TOPZONE)
				{
					_log.info("[VoteReward] Topzone Enable");
					VoteRewardTopzone.getInstance();
				}
				else
				{
					_log.info("[VoteReward] Topzone Disable");
				}
				if (Config.VOTE_SYSTEM_BRASIL)
				{
					_log.info("[VoteReward] TopBrasil Enable");
					VoteRewardBrasil.getInstance();
				}
				else
				{
					_log.info("[VoteReward] TopBrasil Disable");
				}
				if (Config.VOTE_SYSTEM_ADMINSPRO)
				{
					_log.info("[VoteReward] TopAdminsPro Enable");
					VoteRewardAdminsPro.getInstance();
				}
				else
				{
					_log.info("[VoteReward] TopAdminsPro Disable");
				}
			}
		}
		else
		{
			_log.info("[AutoVoteRewardManager] Disable.");
		}
	}
	
	public static VoteManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public static class SingletonHolder
	{
		protected static final VoteManager _instance = new VoteManager();
	}
}