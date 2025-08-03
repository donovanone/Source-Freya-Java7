import sys
from com.l2jserver.gameserver.model.actor.instance import L2PcInstance
from com.l2jserver.gameserver.model.actor.instance import L2NpcInstance
from java.util import Iterator
from com.l2jserver import L2DatabaseFactory
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest

qn = "10016_ClanManager"

NPC=[10016]

PriceIDClanLevel8= 10017
PriceCountClanLevel8= 20

ReputationScoreCount2= 116000
PriceIDReputationScore2= 10017
PriceCountReputationScore2= 15

QuestId     = 10016
QuestName   = "ClanManager"
QuestDesc   = "custom"
InitialHtml = "Start.htm"

print "                                                                               "
print "                            * Clan Manager                               [ Ok ]"

class Quest (JQuest) :

	def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

	def onEvent(self,event,st):
               htmltext = "<html><head><body>I have nothing to say you</body></html>"
               count=st.getQuestItemsCount(PriceIDClanLevel8)
               if event == "ClanLevel8.htm" :
                   if st.getPlayer().isClanLeader() and st.getPlayer().getClan().getLevel()<8:
                       if st.getPlayer().isNoble() and count >= PriceCountClanLevel8:
                            htmltext=event
                            st.getPlayer().getClan().changeLevel(8)
                            st.playSound("ItemSound.quest_finish")
                            st.takeItems(PriceIDClanLevel8,PriceCountClanLevel8)
                       else :
                            htmltext="NoPoints.htm"
                            st.exitQuest(1)
                   else :
                       htmltext="NoPoints.htm"
                       st.exitQuest(1)					   

               elif event == "Reputation2.htm" :
                   if st.getPlayer().isClanLeader() and st.getPlayer().getClan().getLevel() >= 5 and st.getPlayer().getClan().getReputationScore() < ReputationScoreCount2 :
                       if st.getPlayer().isNoble() and count > PriceCountReputationScore2:
                            htmltext=event
                            st.getPlayer().getClan().setReputationScore(ReputationScoreCount2, 1);
                            st.playSound("ItemSound.quest_finish")
                            st.takeItems(PriceIDReputationScore2,PriceCountReputationScore2)
                       else :
                            htmltext="NoPoints.htm"
                            st.exitQuest(1)
                   else :
                       htmltext="NoPoints.htm"
                       st.exitQuest(1)					   
               return htmltext

	def onTalk (self,npc,player):
	   htmltext = "<html><head><body>I have nothing to say you</body></html>"
           st = player.getQuestState(qn)
           if not st : return htmltext
           npcId = npc.getNpcId()
           id = st.getState()
           if id == CREATED :
               htmltext="Start.htm"
           elif id == COMPLETED :
               htmltext = "<html><head><body>This quest have already been completed.</body></html>"
           else :
               st.exitQuest(1)
           return htmltext


QUEST = Quest(10016,qn,"village_master")
CREATED     = State('Start', QUEST)
STARTING    = State('Starting', QUEST)
STARTED     = State('Started', QUEST)
COMPLETED   = State('Completed', QUEST)
QUEST.setInitialState(CREATED)

for npcId in NPC:
 QUEST.addStartNpc(npcId)
 QUEST.addTalkId(npcId)
