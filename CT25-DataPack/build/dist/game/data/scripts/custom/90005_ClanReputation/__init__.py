import sys
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.model.actor.instance import L2PcInstance
from com.l2jserver.gameserver.network.serverpackets import NpcHtmlMessage
from com.l2jserver.gameserver.network.serverpackets import SystemMessage
from com.l2jserver.gameserver.network import SystemMessageId
 
#####################
###CONFIGURACIONES###
#####################
 
# Elige el Id y la cantidad que costara por cantidad.
# Ejemplo: 500 adena por 500 reputacion, 1000 adena por 1000 reputacion
# Adena ID: 57
ADENA = 6673
COUNT = 10
 
# Limite de Level de Clan.
# 0 = Disabled.
LEVEL_LIMIT = 5
 
# Reputacion que se dara.
# Desde los htm se definira la cantidad multiplicada por lo que quiera.
# Ejemplo: bypass -h Quest 90005_ClanReputation setreputation 1
# Daria 500, si pusieras 2 daria 1000 y asi...
REPUTATION = 500
 
# Status
ALLOW_KARMA = False
ALLOW_PVP = False
 
# Permitir a los miembros del clan dar clan reputation o solo puede el lider
REQUIRE_LEADER = False
 
 
##########################
########INFORMACION#######
##########################
npcId         = 40001 # Poner el que querais.
QuestId       = 90005
QuestName     = "ClanReputation"
QUEST_INFO    = str(QuestId)+"_"+QuestName
QuestDesc     = "custom"
 
 
def error(st,player,npc,text):
    html = NpcHtmlMessage(npc.getObjectId())
    html.setFile(None,"data/scripts/custom/90005_ClanReputation/error.htm")
    html.replace("%text%",text)
    st.player.sendPacket(html)
 
def DefaultHtml(st,player,npc):
    html = NpcHtmlMessage(npc.getObjectId())
    html.setFile(None,"data/html/default/40001.htm")
    html.replace("%objectId%",str(npc.getObjectId()))
    st.player.sendPacket(html)
 
def CompletedHtml(st,player,npc):
    html = NpcHtmlMessage(npc.getObjectId())
    html.setFile(None,"data/scripts/custom/90005_ClanReputation/completed.htm")
    st.player.sendPacket(html)
 
def setreputation(st,player,npc,m):
    if st.getQuestItemsCount(ADENA) < COUNT * m:
        return error(st,player,npc,"You not have enough items required<br1>You need "+str(COUNT * m)+" Gold Bars.")
    st.takeItems(ADENA,COUNT * m)
    st.player.getClan().addReputationScore(REPUTATION * m, True)
    st.player.sendPacket(SystemMessage(1777).addNumber(REPUTATION * m))
    return CompletedHtml(st,player,npc)
 
class Quest (JQuest) :
   
    def __init__(self,id,name,descr):
        JQuest.__init__(self,id,name,descr)
 
    def onAdvEvent (self,event,npc,player) :
        try: st = player.getQuestState(QUEST_INFO)
        except: return
        varSplit = event.split(" ")
        event = varSplit[0]
        if event == "setreputation":
            return setreputation(st,player,npc,int(varSplit[1]))
 
    def onFirstTalk (self,npc,player):
        st = player.getQuestState(QUEST_INFO)
        if not st : st = self.newQuestState(player)
        if st.player.getClan() == None:
            st.getPlayer().sendMessage("Come to me again when you have clan.")
        elif st.player.getClan().getLevel() < LEVEL_LIMIT:
            st.getPlayer().sendMessage("Come to me again when you have clan lvl "+str(LEVEL_LIMIT)+".")
        elif st.player.getClan().getLeaderName() != st.player.getName() and REQUIRE_LEADER == True:
            return error(st,player,npc,"No puedes usar mis servicios.<br1>You not are leader of clan.")
        elif st.player.getPvpFlag() > 0 and ALLOW_PVP == False:
            return error(st,player,npc,"No puedes usar mis servicios.<br1>Come to me again when you not have Flag.")
        elif st.player.getKarma() > 0 and ALLOW_KARMA == False:
            return error(st,player,npc,"No puedes usar mis servicios.<br1>Come to me again when you not have Karma.")
        else :
            return DefaultHtml(st,player,npc)
       
 
QUEST = Quest(QuestId,QUEST_INFO,QuestDesc)
QUEST.addStartNpc(npcId)
QUEST.addFirstTalkId(npcId)
QUEST.addTalkId(npcId)