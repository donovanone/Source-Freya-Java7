import sys
from java.lang import System
from com.l2jserver import Config
from com.l2jserver	import L2DatabaseFactory
from com.l2jserver.gameserver.cache import HtmCache
from com.l2jserver.gameserver.model.quest import State
from com.l2jserver.gameserver.model.quest import QuestState
from com.l2jserver.gameserver.model.quest.jython import QuestJython as JQuest
from com.l2jserver.gameserver.model.actor.instance import L2PcInstance
from com.l2jserver.gameserver.model.base import ClassId
from com.l2jserver.gameserver.model.base import Race
from com.l2jserver.gameserver.datatables import SkillTable
from com.l2jserver.gameserver.datatables import ClanTable
from com.l2jserver.gameserver.datatables import ItemTable
from com.l2jserver.gameserver.datatables import CharTemplateTable
from com.l2jserver.gameserver.datatables import HennaTreeTable
from com.l2jserver.gameserver.instancemanager import QuestManager
from com.l2jserver.gameserver.instancemanager import TownManager
from com.l2jserver.gameserver.instancemanager import CastleManager
from com.l2jserver.gameserver.instancemanager import FortManager
from com.l2jserver.gameserver.instancemanager import FortSiegeManager
from com.l2jserver.gameserver.instancemanager import SiegeManager
from com.l2jserver.gameserver.model.olympiad import Olympiad
from com.l2jserver.gameserver.network import SystemMessageId
from com.l2jserver.gameserver.network.serverpackets import SystemMessage
from com.l2jserver.gameserver.network.serverpackets import NpcHtmlMessage
from com.l2jserver.gameserver.network.serverpackets import SetSummonRemainTime
from com.l2jserver.gameserver.network.serverpackets import SetupGauge
from com.l2jserver.gameserver.network.serverpackets import MagicSkillLaunched
from com.l2jserver.gameserver.network.serverpackets import MagicSkillUse
from com.l2jserver.gameserver.network.serverpackets import AcquireSkillList
from com.l2jserver.gameserver.network.serverpackets import AcquireSkillDone
from com.l2jserver.gameserver.network.serverpackets import HennaEquipList
from com.l2jserver.gameserver.network.serverpackets import HennaRemoveList

##############################
GM_RELOAD_PANEL = True       #
##############################
GM_ACCESS_LEVEL = 127        #
##############################
ALLOW_VIP = False            #
##############################
VIP_ACCESS_LEVEL = 1         #
##############################
CLASSMASTER_GIFT = 6622      #
##############################
CLASSMASTER_ITEM_COUNT = 1   # 
##############################
REQUEST_ITEMS_FOR_SUB = True #
##############################
SUB_REQ_ITEM = 3483          #
##############################
SUB_AMOUNT_ITEM = 100        #
##############################
NOBLE_ITEMID = 3483          #
##############################
NOBLE_ITEM_COUNT = 25        #
##############################
NOBLE_LEVEL = 80             # 
##############################
NOBLESS_TIARA = 7694         #
##############################
ALLOW_KARMA_PLAYER = False   #
##############################
FREE_TELEPORT = False        #
##############################
FREE_BUFFS = False           #
##############################
CONSUMABLE_ID = 57           #
##############################
BUFFS_PRICE = 100000         #
##############################
BUFF_REMOVE_PRICE = 10000    #
##############################
HEAL_PRICE = 10000           #
##############################
TIME_OUT = True              #
##############################
TIME_OUT_TIME = 10           #
##############################
BLOCK_TIME = 20              #
##############################
DELAY = 3	         	     #
##############################

npcId         = 955
QuestId       = 955
QuestName     = "NpcService"
QUEST_INFO    = str(QuestId)+"_"+QuestName
QuestDesc     = "custom"

print "============================="
print "L2Ghoul - Services Manager"
print "============================="

def MainHtml3(st) :
	MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
	MAIN_HTML += "<br><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
	MAIN_HTML += "<font color=\"LEVEL\">.::Subclass Master Opciones::.</font><br><br>"
	if st.player.getTotalSubClasses() == 0 and REQUEST_ITEMS_FOR_SUB == True:
		MAIN_HTML += "<font color=\"FFFFFF\">If you want to add any Subclass, your<br1>current occupation must be second or<br1>third, reach level <font color=\"FFFF00\">"+str(75)+" or above</font> and get<br1><font color=\"FFFF00\">"+str(SUB_AMOUNT_ITEM)+" "+getitemname(SUB_REQ_ITEM)+"</font>.</font><br>"
	if st.player.getTotalSubClasses() < Config.MAX_SUBCLASS :
		MAIN_HTML += "<button value=\"Add Subclass\" action=\"bypass -h Quest " +QUEST_INFO+ " getracemenu addsub 0 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"
	if st.player.getTotalSubClasses() > 0 :
		MAIN_HTML += "<button value=\"Change Subclass\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass changesub 0 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"
		MAIN_HTML += "<button value=\"Remove Subclass\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass deletesub 0 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"
	MAIN_HTML += "<br><font color=\"303030\">"+getmaster()+"</font>"	
	MAIN_HTML += "</center></body></html>"
	return MAIN_HTML

def MainHtml4(st) : 
	MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
	MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
	MAIN_HTML += "<font color=\"LEVEL\">.::Clan Options::.</font><br><br1>"
	if st.player.getClanId() == 0:
		MAIN_HTML += "<button value=\"Create New Clan\" action=\"bypass -h Quest " +QUEST_INFO+ " createclan 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
	else:
		MAIN_HTML += "<button value=\"Delegate Clan Leader\" action=\"bypass -h Quest " +QUEST_INFO+ " giveclanl 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Increase Clan Level\" action=\"bypass -h Quest " +QUEST_INFO+ " increaseclan 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Disband Clan\" action=\"bypass -h Quest " +QUEST_INFO+ " DisbandClan 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Restore Clan\" action=\"bypass -h Quest " +QUEST_INFO+ " RestoreClan 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Acquire Clan Skill\" action=\"bypass -h Quest " +QUEST_INFO+ " learn_clan_skills 0 0 0\" width=140 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<br><font color=\"LEVEL\">.::Alliance Options::.</font><br><br1>"
		clan=st.player.getClan()
		if clan.getAllyId() == 0:
			MAIN_HTML += "<button value=\"Create a Alliance\" action=\"bypass -h Quest " +QUEST_INFO+ " createally 0 0 0\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		else:
			MAIN_HTML += "<button value=\"Dissolve Alliance\" action=\"bypass -h Quest " +QUEST_INFO+ " dissolve_ally 0 0 0\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
	MAIN_HTML += "<br><font color=\"303030\">"+getmaster()+"</font>"	
	MAIN_HTML += "</center></body></html>"
	return MAIN_HTML

def RaceMenu(st,case,case2) :
	temp = st.player.getRace().ordinal()
	if temp == 5:
		return subclassopcions(st, case, "5", case2)
	else:
		MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
		MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
		MAIN_HTML += "<font color=\"LEVEL\">.::Chooce a Race::.</font><br><br1>"
		MAIN_HTML += "<button value=\"Human\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+case+" 0 "+case2+"\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		if temp != 2: MAIN_HTML += "<button value=\"Elf\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+case+" 1 "+case2+"\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		if temp != 1: MAIN_HTML += "<button value=\"Dark Elf\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+case+" 2 "+case2+"\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Orc\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+case+" 3 "+case2+"\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<button value=\"Dwarf\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+case+" 4 "+case2+"\" width=120 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
		MAIN_HTML += "<br><font color=\"303030\">"+getmaster()+"</font>"
		MAIN_HTML += "</center></body></html>"
		return MAIN_HTML

def NobleMenu(st):
	MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
	MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
	MAIN_HTML += "<font color=\"LEVEL\">.::Noblesse Manager::.</font><br><br>"
	MAIN_HTML += "<table border=\"0\" cellspacing=\"0\">"
	MAIN_HTML += "<tr><td valign=top><img src=icon.skill0325 width=32 height=32 align=left></td><td valign=top><img src=icon.skill0326 width=32 height=32 align=left></td>"
	MAIN_HTML += "<td valign=top><img src=icon.skill0327 width=32 height=32 align=left></td><td valign=top><img src=icon.skill1323 width=32 height=32 align=left></td>"
	MAIN_HTML += "<td valign=top><img src=icon.skill1324 width=32 height=32 align=left></td><td valign=top><img src=icon.skill1325 width=32 height=32 align=left></td>"
	MAIN_HTML += "<td valign=top><img src=icon.skill1326 width=32 height=32 align=left></td><td valign=top><img src=icon.skill1327 width=32 height=32 align=left></td></tr>"
	MAIN_HTML += "</table><br><br><br>"
	MAIN_HTML += "<font color=\"FFFFFF\">If you want to be Noblesse first you need<br1>reach level <font color=\"FFFF00\">"+str(NOBLE_LEVEL)+"</font> and get <font color=\"FFFF00\">"+str(NOBLE_ITEM_COUNT)+" "+getitemname(NOBLE_ITEMID)+"</font>.</font><br><br1>"
	MAIN_HTML += "<button value=\"I'm ready, make me noblesse\" action=\"bypass -h Quest " +QUEST_INFO+ " getnoble 2 0 0\" width=200 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
	MAIN_HTML += "<br><font color=\"303030\">"+getmaster()+"</font>"
	MAIN_HTML += "</center></body></html>"
	return MAIN_HTML  

def getitemname(case):
	try: val =ItemTable.getInstance().createDummyItem(case).getItemName()
	except: val = "0"
	return val

def getnames(case):
	try: val = CharTemplateTable.getInstance().getClassNameById(case)
	except: val = "0"
	return val

def getmaster():
	xi="vice";xe="l";xf="e";xg="n";xa="B";xb="y";xc=" ";xd="A";xk="ger";xh="Ser";xj="Mana";val=xa+xb+xc+xd+xe+xe+xf+xg
	return val

def showText(type,text) :
	MESSAGE = "<html><head><title>L2Ghoul</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
	MESSAGE += "<font color=\"LEVEL\">"+type+"</font><br>"+text+"<br>"
	MESSAGE += "<font color=\"303030\">"+getmaster()+"</font></center></body></html>"
	return MESSAGE

def addTimeout(st,gaugeColor,amount,offset) :
	endtime = int(System.currentTimeMillis()/1000) + amount
	st.set("blockUntilTime",str(endtime))
	st.getPlayer().sendPacket(SetupGauge(gaugeColor, amount * 1000 + offset))
	return True

def ReloadConfig(st,npcid) :
	try:
		HtmCache.getInstance().reload(Config.DATAPACK_ROOT)
		if QuestManager.getInstance().reload(QuestId): st.player.sendMessage("The Script and Htmls have been reloaded successfully.")
		else: st.player.sendMessage("Script Reloaded Failed. you edited something wrong! :P, fix it and restart the server")
				
	except: st.player.sendMessage("Script Reloaded Failed. you edited something wrong! :P, fix it and restart the server")
	filename = "data/html/teleporter/mainhtmls/mainhtml-0.htm"
	html = NpcHtmlMessage(int(npcid))	
	html.setFile(None,filename)
	html.replace("%objectId%", npcid)
	st.player.sendPacket(html)

def optionsymbol(st, cases,num):
	if cases == "draws":
		henna = HennaTreeTable.getInstance().getAvailableHenna(st.player.getClassId())
		hel = HennaEquipList(st.player, henna)
		st.player.sendPacket(hel)
	elif cases == "deletes":
		hasHennas = False
		for i in range(3):
			henna = st.player.getHenna(i+1)
			if henna != None: hasHennas = True
		if hasHennas == True:
			st.player.sendPacket(HennaRemoveList(st.player))
			return
		else:
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You don't have any symbol to remove!</font><br>"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

def heal(st,case) :
	if st.player.isInCombat() :
		return False
	if case == "0":
		st.getPlayer().getStatus().setCurrentHp(st.getPlayer().getStat().getMaxHp())
		st.getPlayer().getStatus().setCurrentMp(st.getPlayer().getStat().getMaxMp())
		st.getPlayer().getStatus().setCurrentCp(st.getPlayer().getStat().getMaxCp())

	if case == "1" and st.player.getPet() != None :
		st.player.getPet().getStatus().setCurrentHp(st.player.getPet().getStat().getMaxHp())
		st.player.getPet().getStatus().setCurrentMp(st.player.getPet().getStat().getMaxMp())
		try:
			st.player.getPet().setCurrentFed(st.player.getPet().getMaxFed())
			st.player.sendPacket(SetSummonRemainTime(st.player.getPet().getMaxFed(), st.player.getPet().getCurrentFed()))
		except: 
			try: 
				st.player.getPet().decTimeRemaining(st.player.getPet().getTimeRemaining() - st.player.getPet().getTotalLifeTime())
				st.player.sendPacket(SetSummonRemainTime(st.player.getPet().getTotalLifeTime(), st.player.getPet().getTimeRemaining()))
			except: pass
	return True

def classmaster(st):
	classId = st.player.getClassId()
	level = st.player.getLevel()
	jobLevel= classId.level()
	newJobLevel = jobLevel + 1
	MAIN_HTML = "<html><title>Class Master</title><body><center>"
	
	if jobLevel ==2 and level > 75 or jobLevel ==1 and level > 39 or jobLevel ==0 and level > 19:
		MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
		MAIN_HTML += "<font color=\"LEVEL\">Change Subclass:<br1>Which class do you wish to change?</font><br><br1>"
		for child in ClassId.values():
			if child.childOf(classId) and child.level() == newJobLevel:
				MAIN_HTML += "<button value=\""+getnames(child.getId())+"\" action=\"bypass -h Quest " +QUEST_INFO+ " changeclass "+str(child.getId())+" 0 0 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
	else: 
		if jobLevel ==0 and level < 20: MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">Come back here when you reach level 20<br1>to change your class.</font><br>"
		elif jobLevel <=1 and level < 40: MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">Come back here when you reach level 40<br1>to change your class.</font><br>"
		elif jobLevel <=2 and level < 76: MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">Come back here when you reach level 76<br1>to change your class.</font><br>"
		else: MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">There is no class change available<br>for you anymore.</font><br>"
	MAIN_HTML += "</center></body></html>"
	return MAIN_HTML

def subclasslist(st,cases,case2,index) :
	if st.player.getLevel() < 75 and int(index) == 0 or st.getPlayer().getClassId().level() < 2:
		HTML = "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You cannot add a new subclass!<br1>First your current occupation must be<br1>second or third job and all of your sub<br1>classes must be at level 75 or above.</font><br>"
		return HTML

	currentBaseId = st.player.getBaseClass()
	baseCID = ClassId.values()[currentBaseId]
	if baseCID.level() > 2: baseClassId = baseCID.getParent().ordinal()
	else: baseClassId = currentBaseId
	num = 0
	if baseClassId in [5,6,20,33]: subclasses = [5,6,20,33,57,51]
	elif baseClassId in [8,23,36]: subclasses = [8,23,36,57,51]
	elif baseClassId in [9,24,37]: subclasses = [9,24,37,57,51]
	elif baseClassId in [12,27,40]: subclasses = [12,27,40,57,51]
	else: subclasses = [baseClassId,57,51]
	for i in range(Config.MAX_SUBCLASS):
		if st.player.getSubClasses().containsKey(i+1):
			if st.player.getSubClasses().get(i+1).getLevel() < 75 and int(index) == 0:
				HTML = "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You cannot add a new subclass!<br1>First your current occupation must be<br1>second or third job and all of your sub<br1>classes must be at level 75 or above.</font><br>"
				return HTML
			temp = ClassId.values()[st.player.getSubClasses().get(i+1).getClassId()]
			if temp.level() > 2: subclasses += [temp.getParent().ordinal()]
			else: subclasses += [temp.getId()]

	HTML = "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
	if int(index) == 0: HTML += "<font color=\"LEVEL\">Add Subclass:<br>Which subclass do you wish to add?</font><br><br1>"
	if int(index) > 0: HTML += "<font color=\"LEVEL\">Please<br>select a new subclass to change.</font><br1><font color=\"LEVEL\">Warning!</font> Your previous subclass and all the skills will be removed."
	
	for child in ClassId.values():
		if child.getRace() == Race.values()[int(case2)] and child.level() == 2 and child.getId() not in subclasses:
			num = 1
			HTML += "<button value=\""+getnames(child.getId())+"\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass "+cases+" "+str(child.getId())+" "+index+"\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
	if num == 0: 
		HTML = "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">There are no available subclasses at this moment.</font><br>"
	return HTML

def subclassopcions(st,cases,id,index) :  
   	if st.player.isCastingNow() or st.player.isAllSkillsDisabled():
		st.player.sendPacket(SystemMessage(SystemMessageId.SUBCLASS_NO_CHANGE_OR_CREATE_WHILE_SKILL_IN_USE))
		return
		
   	if Olympiad.getInstance().isRegistered(st.player):
		st.player.sendMessage("You have already been registered in a Olympiad game.")
		return
		
   	if cases == "addsub":
		if st.player.getTotalSubClasses() >= Config.MAX_SUBCLASS :
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
                	st.player.sendMessage("You can now only change one of your current sub classes.")
                        return
		else:
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += subclasslist(st,"acceptsub",id,"0")
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

	if cases == "acceptsub":
			if REQUEST_ITEMS_FOR_SUB == True and st.player.getTotalSubClasses() == 0:
				if st.getQuestItemsCount(SUB_REQ_ITEM) < SUB_AMOUNT_ITEM:
					return showText("Sorry","You don't have the required items!<br>You will need: <font color =\"LEVEL\">"+str(SUB_AMOUNT_ITEM)+" "+getitemname(SUB_REQ_ITEM)+"</font><br>to add any subclass")					
				else: st.takeItems(SUB_REQ_ITEM,SUB_AMOUNT_ITEM)
			if not st.player.addSubClass(int(id),st.player.getTotalSubClasses() + 1):
				st.player.sendMessage("The sub class could not be added.")
				return
			st.player.setActiveClass(st.player.getTotalSubClasses())
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<br><br>Add Subclass:<br>The sub class of <font color=\"LEVEL\">"+getnames(int(id))+"</font><br> has been added.<br>"
			MAIN_HTML += "</center></body></html>"
			st.player.sendPacket(SystemMessage(SystemMessageId.CLASS_TRANSFER))
			return MAIN_HTML

	if cases == "acceptchangesub":
        	if not st.player.setActiveClass(int(index)):
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
                	st.player.sendMessage("The sub class could not be changed.")
                        return
		else:
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<br><br>Change Subclass:<br>Your active class is now a:<br><font color=\"LEVEL\">"+getnames(int(id))+"</font>"
			MAIN_HTML += "</center></body></html>"
			st.player.sendPacket(SystemMessage(SystemMessageId.ADD_NEW_SUBCLASS))
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
			return MAIN_HTML

	if cases == "changesub":

        	if st.player.getTotalSubClasses() > Config.MAX_SUBCLASS :
                	st.player.sendMessage("You can now only delete one of your current sub classes.")
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
                        return
		else:
			j=0
			MAIN_HTML = "<html><title>AIO Grand Master</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
			MAIN_HTML += "<font color=\"LEVEL\">Change Subclass:<br1>Which sub class do you wish to change?</font><br>"
			if st.player.isSubClassActive():
				j=1
				MAIN_HTML += "<font color=\"LEVEL\">Main-class</font>"
				MAIN_HTML += "<button value=\""+getnames(st.player.getBaseClass())+"\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass acceptchangesub "+str(st.player.getBaseClass())+" 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"
			for i in range(Config.MAX_SUBCLASS):
				if st.player.getSubClasses().containsKey(i+1):
					xsubclassid = st.player.getSubClasses().get(i+1).getClassId()	
					if int(st.player.getClassId().getId()) != xsubclassid:
						j=i+1
						MAIN_HTML += "<font color=\"LEVEL\">Sub-class "+str(i+1)+"</font>"
						MAIN_HTML += "<button value=\""+getnames(xsubclassid)+"\" action=\"bypass -h Quest " +QUEST_INFO+ " subclass acceptchangesub "+str(xsubclassid)+" "+str(i+1)+"\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
			if j == 0:
				MAIN_HTML = "<html><title>AIO Grand Master</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">There are no sub classes available<br>to change at this time.</font><br>"
				if TIME_OUT == True: 
					if addTimeout(st,3,BLOCK_TIME,300) : pass	 
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

	if cases == "deletesub":

        	if st.player.getTotalSubClasses() > Config.MAX_SUBCLASS :
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
                	st.player.sendMessage("You can now only delete one of your current sub classes.")
                        return
		else:
			j=0
			MAIN_HTML = "<html><title>AIO Grand Master</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"
			MAIN_HTML += "<font color=\"LEVEL\">Which sub class do you wish to delete?</font><br>"
			for i in range(Config.MAX_SUBCLASS):
				if st.player.getSubClasses().containsKey(i+1):
					j=i+1
					MAIN_HTML += "<font color=\"LEVEL\">Sub-class "+str(i+1)+"</font>"
					MAIN_HTML += "<button value=\""+getnames(st.player.getSubClasses().get(i+1).getClassId())+"\" action=\"bypass -h Quest " +QUEST_INFO+ " getracemenu acceptnewsub "+str(i+1)+" 0\" width=150 height=30 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" 
			MAIN_HTML += "<br>If you change a sub class, you'll start at level 40<br1>after the 2nd class transfer."
			if j == 0:
				MAIN_HTML = "<html><title>AIO Grand Master</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">There are no sub classes available<br>to delete at this time.</font><br>"
				if TIME_OUT == True: 
					if addTimeout(st,3,BLOCK_TIME,300) : pass
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

	if cases == "acceptnewsub":
		MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
		MAIN_HTML += subclasslist(st,"acceptdelsub",id,index)
		MAIN_HTML += "</center></body></html>"
		return MAIN_HTML  
 
	if cases == "acceptdelsub":
		if st.player.modifySubClass(int(index), int(id)):
                    	st.player.stopAllEffects()
                    	st.player.setActiveClass(int(index))
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<br><br>Change Subclass:<br>Your sub class has been changed to<br1><font color=\"LEVEL\">"+getnames(int(id))+"</font>"
			MAIN_HTML += "</center></body></html>"
			st.player.sendPacket(SystemMessage(SystemMessageId.ADD_NEW_SUBCLASS))
			if TIME_OUT == True:
				if addTimeout(st,3,BLOCK_TIME,300) : pass
			return MAIN_HTML
             	else:
                        player.setActiveClass(0)
			if TIME_OUT == True: 
				if addTimeout(st,3,BLOCK_TIME,300) : pass
                        st.player.sendMessage("The sub class could not be added, you have been reverted to your base class.")
                        return 
						
def reloadPvpPkList(self) :
	con = L2DatabaseFactory.getInstance().getConnection()
	pvps = con.prepareStatement("SELECT char_name,pvpkills,pkkills FROM characters WHERE pvpkills>0 and accesslevel=0 OR pvpkills>0 and accesslevel="+str(VIP_ACCESS_LEVEL)+" ORDER BY pvpkills DESC,pkkills DESC,char_name LIMIT 100")
	pvprs = pvps.executeQuery()
	self.htmltext1 = []; htmlt1 = ""; i=0; j=0

	while (pvprs.next()) :
		i+=1;j+=1
		htmlt1 += "<tr><td width=40 align=\"center\"><font color =\"FFFFFF\">" + str(i) + "</td><td width=100 align=\"left\"><font color =\"FFFFFF\">" + pvprs.getString("char_name") +"</td><td width=30 align=\"left\"><font color =\"LEVEL\">" + pvprs.getString("pvpkills") + "</td><td width=30 align=\"left\"><font color =\"FFFFFF\">" + pvprs.getString("pkkills") + "</td></tr>"
		if j == 20:
			self.htmltext1 += [htmlt1]
			j = 0; htmlt1 = ""

	if j > 0 : self.htmltext1 += [htmlt1]
	self.pvppage = len(self.htmltext1)

	pks = con.prepareStatement("SELECT char_name,pvpkills,pkkills FROM characters WHERE pkkills>0 and accesslevel=0 OR pkkills>0 and accesslevel="+str(VIP_ACCESS_LEVEL)+" ORDER BY pkkills DESC,pvpkills DESC,char_name LIMIT 100")
	pkrs = pks.executeQuery()
	self.htmltext2 = []; htmlt2 = ""; i=0; j=0

	while (pkrs.next()) :
		i+=1;j+=1
		htmlt2 += "<tr><td width=40 align=\"center\"><font color =\"FFFFFF\">" + str(i) + "</td><td width=100 align=\"left\"><font color =\"FFFFFF\">" + pkrs.getString("char_name") +"</td><td width=30 align=\"left\"><font color =\"FFFFFF\">" + pkrs.getString("pvpkills") + "</td><td width=30 align=\"left\"><font color =\"LEVEL\">" + pkrs.getString("pkkills") + "</td></tr>"
		if j == 20:
			self.htmltext2 += [htmlt2]
			j = 0; htmlt2 = ""

	if j > 0 : self.htmltext2 += [htmlt2]
	self.pkpage = len(self.htmltext2)

	try:
		pvps.close()
		pks.close()
		pvprs.close()
		pkrs.close()
		con.close()
	except : pass
	return True

class Quest (JQuest) :
	
	def __init__(self,id,name,descr):
		JQuest.__init__(self,id,name,descr)
		self.Reload_PvpPk_Time = 0

	def onAdvEvent (self,event,npc,player) :
		try: st = player.getQuestState(QUEST_INFO)
		except: return
		tempevent = event
		
		if event.startswith("gototeleport"):
			event = event.replace("]","")
			event = event.replace("["," ")
			varSplit = event.split(" ")
			try: noblesGK = int(varSplit[2])
			except : noblesGK = 0
			if st.player.isGM() == 1 :
				try:
					st.player.teleToLocation(int(varSplit[5]), int(varSplit[6]), int(varSplit[7]), True)
					st.player.sendMessage("You have been teleported to " + varSplit[5] +" "+varSplit[6]+ " "+varSplit[7])
				except : st.player.sendMessage("You should check the coordinates. Something is wrong!")
				return

			if noblesGK == 1 and st.player.isNoble() == 0 :
				return showText("Sorry","Only a <font color =\"LEVEL\">Noblesse</font> can be teleported to this place")

			if noblesGK > 1 and st.player.isGM() == 0 :
				return showText("Sorry","Only a <font color =\"LEVEL\">GM</font> can be teleported to this place")

			else:
				newevent="confteleport["+varSplit[3]+"]["+varSplit[4]+"] "+varSplit[5]+" "+varSplit[6]+" "+varSplit[7]
				filename = "data/html/teleporter/gatekeeper/confirmation.htm"
				html = NpcHtmlMessage(npc.getObjectId())	
				html.setFile(None,filename)
				html.replace("%place%", varSplit[1].replace("-"," "))
				html.replace("%itemName%", getitemname(int(varSplit[3])))
				html.replace("%reqitem%", varSplit[4])
				html.replace("%event%", newevent)
				html.replace("%objectId%", str(npc.getObjectId()))
				st.player.sendPacket(html)
			return
				
		event = event.replace(","," ")
		eventSplit = event.split(" ")
		event = eventSplit[0]
		eventParam1 = eventSplit[1]
		eventParam2 = eventSplit[2]
		eventParam3 = eventSplit[3]

		if event == "reloadscript": return ReloadConfig(st,eventParam1)

		if event.startswith("confteleport"):
			event = event.replace("]","")
			event = event.replace("["," ")
			GKSplit = event.split(" ")
			try: TELEPORT_ITEM_ID = int(GKSplit[1]); TELEPORT_PRICE = int(GKSplit[2])
			except : TELEPORT_ITEM_ID = 57; TELEPORT_PRICE = 15000
		
			if SiegeManager.getInstance().getSiege(int(eventParam1), int(eventParam2), int(eventParam3)) != None:
				st.player.sendPacket(SystemMessage(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE))
				return
			elif TownManager.townHasCastleInSiege(int(eventParam1), int(eventParam2)):
				st.player.sendPacket(SystemMessage(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE))
				return
			elif not Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK and st.player.getKarma() > 0:
				st.player.sendMessage("Go away, you're not welcome here.")
				return
			elif st.player.isAlikeDead():
				return
			if FREE_TELEPORT == False :
				if st.getQuestItemsCount(TELEPORT_ITEM_ID) < TELEPORT_PRICE :
					return showText("Sorry","You don't have enough Items:<br>You need: <font color =\"LEVEL\">"+str(TELEPORT_PRICE)+" "+getitemname(TELEPORT_ITEM_ID)+"!")
				else :
					st.takeItems(TELEPORT_ITEM_ID,TELEPORT_PRICE)
					st.player.teleToLocation(int(eventParam1), int(eventParam2), int(eventParam3), True)
					st.player.sendMessage("You have been teleported to " + eventParam1 +" "+eventParam2+ " "+eventParam3)
			else: 
				st.player.teleToLocation(int(eventParam1), int(eventParam2), int(eventParam3), True)
				st.player.sendMessage("You have been teleported to " + eventParam1 +" "+eventParam2+ " "+eventParam3)	

		if event == "heal" :
			if int(System.currentTimeMillis()/1000) > st.getInt("blockUntilTime"):
				if st.getQuestItemsCount(CONSUMABLE_ID) < HEAL_PRICE  :
					return showText("Sorry","You don't have the required Item.<br>You need: <font color =\"LEVEL\">"+str(HEAL_PRICE)+" "+str(getitemname(CONSUMABLE_ID))+"!")
				else :
					if heal(st,eventParam2) :pass					
					st.takeItems(CONSUMABLE_ID,HEAL_PRICE)					
					if TIME_OUT == True:
						if addTimeout(st,1,TIME_OUT_TIME,0): pass
			filename = "data/html/teleporter/buffer/NpcBuffer-"+eventParam1+".htm"
			html = NpcHtmlMessage(npc.getObjectId())	
			html.setFile(None,filename)
			html.replace("%objectId%", str(npc.getObjectId()))
			st.player.sendPacket(html)
			
		if event == "removeBuffs" :
			if int(System.currentTimeMillis()/1000) > st.getInt("blockUntilTime"):						
				if st.getQuestItemsCount(CONSUMABLE_ID) < BUFF_REMOVE_PRICE :
					return showText("Sorry","You don't have the required Item.<br>You need: <font color =\"LEVEL\">"+str(BUFF_REMOVE_PRICE)+" "+str(getitemname(CONSUMABLE_ID))+"!")
				else :
					if eventParam2 == "Remove_Pet" :
						if st.player.getPet() != None :
							st.player.getPet().stopAllEffects()
					else :
						st.getPlayer().stopAllEffects()
                				if st.player.getCubics() != None:
                       					for cubic in st.player.getCubics().values():  
                            					cubic.stopAction() 
                            					st.player.delCubic(cubic.getId())   
					st.takeItems(CONSUMABLE_ID,BUFF_REMOVE_PRICE)
					if TIME_OUT == True:
						if addTimeout(st,2,TIME_OUT_TIME,0): pass
			filename = "data/html/teleporter/buffer/NpcBuffer-"+eventParam1+".htm"
			html = NpcHtmlMessage(npc.getObjectId())	
			html.setFile(None,filename)
			html.replace("%objectId%", str(npc.getObjectId()))
			st.player.sendPacket(html)

		if event == "giveBuffs" :
			if int(System.currentTimeMillis()/1000) > st.getInt("blockUntilTime"):
				temp = 0
				if eventParam2 != "Buff_Set" and eventParam2 != "Pet_Buff_Set":
					buffSplit = eventParam3.split("-")
					try: skill=SkillTable.getInstance().getInfo(int(buffSplit[1]),int(buffSplit[0]))
					except: st.player.sendMessage("This skill has an error. Contact a GM to be fixed")
					temp = 1
				else:
					listSplit = eventParam3.split("_")
					j=0;k=0
					while j == 0 :
						try: buff = listSplit[int(k)];k+=1
						except: temp = int(k);j=1

				if FREE_BUFFS == False and eventParam2 != "Buff_Set" and eventParam2 != "Pet_Buff_Set":
					if st.getQuestItemsCount(CONSUMABLE_ID) < BUFFS_PRICE * temp: 
						return showText("Sorry","You don't have the required items!<br>You will need: <font color =\"LEVEL\">"+str(BUFFS_PRICE * int(temp))+" "+getitemname(CONSUMABLE_ID)+"!")					
				
				if eventParam2 == "Buff_Pet" :
					if st.player.getPet() != None :
						try : skill.getEffects(st.getPlayer().getPet(),st.getPlayer().getPet())
						except : st.player.sendMessage("This skill has an error. Contact a GM to be fixed")

					else: return showText("Sorry","You can't buff your pet!<br>Summon it and try again!")

				elif eventParam2 == "Pet_Buff_Set":
					if st.player.getPet() != None :					
						for i in range (temp) : 
							buff = listSplit[int(i)]
							buffSplit = buff.split("-")
							Buff_lvl = buffSplit[0]
							Buff_Id = buffSplit[1]
							try : SkillTable.getInstance().getInfo(int(Buff_Id),int(Buff_lvl)).getEffects(st.getPlayer().getPet(),st.getPlayer().getPet())
							except : st.player.sendMessage("some skills have an error. Contact a GM to be fixed")
					else: return showText("Sorry","You can't buff your pet!<br>Summon it and try again!")

				elif eventParam2 == "Buff_Cubic" :
                			if st.player.getCubics() != None:
                       				for cubic in st.player.getCubics().values():  
                            				cubic.stopAction() 
                            				st.player.delCubic(cubic.getId())
					if st.getQuestItemsCount(skill.getItemConsumeId()) < skill.getItemConsume():
						return showText("Sorry","You don't have the required items!<br>You will need: <font color =\"LEVEL\">"+str(skill.getItemConsume())+" "+getitemname(skill.getItemConsumeId())+"!")
					try : st.getPlayer().useMagic(skill,False,False)
					except : st.player.sendMessage("This skill has an error. Contact a GM to be fixed")

				elif eventParam2 == "Buff_Set":
					for i in range (temp) : 
						buff = listSplit[int(i)]
						buffSplit = buff.split("-")
						Buff_lvl = buffSplit[0]
						Buff_Id = buffSplit[1]
						try : SkillTable.getInstance().getInfo(int(Buff_Id),int(Buff_lvl)).getEffects(st.getPlayer(),st.getPlayer())
						except : st.player.sendMessage("This skill has an error. Contact a GM to be fixed")
				else:
					try : skill.getEffects(st.getPlayer(),st.getPlayer())
					except : st.player.sendMessage("This skill has an error. Contact a GM to be fixed")

				st.takeItems(CONSUMABLE_ID,BUFFS_PRICE*temp)
				if TIME_OUT == True:
					if int(temp) > 1:
						if eventParam2 == "Pet_Buff_Set" : 
							if heal(st,"1") :pass
						else:
							if heal(st,"0") :pass
						if addTimeout(st,3,TIME_OUT_TIME,0): pass
			filename = "data/html/teleporter/buffer/NpcBuffer-"+eventParam1+".htm"
			html = NpcHtmlMessage(npc.getObjectId())	
			html.setFile(None,filename)
			html.replace("%objectId%", str(npc.getObjectId()))
			st.player.sendPacket(html)

   		if event == "chat3" : return MainHtml3(st)

   		if event == "chat4" : return MainHtml4(st)

		if event == "class_master": return classmaster(st)

		if event == "getracemenu": return RaceMenu(st,eventParam1,eventParam2)

   		if event == "NoblesseMenu": return NobleMenu(st)
	
   		if event == "symbol" : return optionsymbol(st, eventParam1,eventParam2)

   		if event == "subclass" : 
			if int(System.currentTimeMillis()/1000) > st.getInt("blockUntilTime"):
				return subclassopcions(st, eventParam1, eventParam2, eventParam3)
			return MainHtml3(st)

   		if tempevent.startswith("PKlistoption",3):
			timer = tempevent.replace("PKlistoption","")
			a = int(timer.split(" ")[0])
			if a == 999: st.set("blockUntilTime",str(System.currentTimeMillis()/1000 + 2 + DELAY))
			if System.currentTimeMillis() - self.Reload_PvpPk_Time > 0:
				self.Reload_PvpPk_Time = System.currentTimeMillis() + 1800000
				if reloadPvpPkList(self): pass
			self.startQuestTimer(tempevent.replace(str(a),""), DELAY*1000, npc, player)
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<br><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">.::Wait a Moment::.</font><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">"+str(DELAY)+"</font> seconds...</font><br>"
			MAIN_HTML += "<br><font color=\"303030\">"+getmaster()+"</font>"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

   		if event == "PKlistoption":
			temp= "LEVEL"; temp1= "FFFFFF"
			if eventParam1 == "2": temp= "FFFFFF"; temp1= "LEVEL"
			htmltext = "<html><head><title>Top PVP/PK Players</title></head><body><center>"
			i = 1;tmp = self.pvppage
			if eventParam1 == "2": tmp = self.pkpage
			if tmp > 1:
				htmltext += "<table border=\"0\"><tr>"
				while i <= tmp:
					if tmp > 5 : width = "25"; pageName = "P"
					else : width = "50"; pageName = "Page "
					htmltext += "<td width=\""+width+"\" align=\"center\"><button value=\""+pageName+""+str(i)+"\" action=\"bypass -h Quest " + QUEST_INFO + " 999PKlistoption "+eventParam1+" "+str(i-1)+" 0\" width="+width+" height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>"
					i += 1
				htmltext += "</tr></table></center>"
			htmltext +="<table width=280><tr><td width=40 align=\"center\"><font color =\"FFFFFF\">Pos.</td><td width=100 align=\"left\"><font color =\"FFFFFF\">Char Name</color></td><td width=30 align=\"left\"><a action=\"bypass -h Quest " +QUEST_INFO+ " 999PKlistoption 1 0 0\"><font color=\""+temp+"\">PVPs</font></a></td><td width=30 align=\"left\"><a action=\"bypass -h Quest " +QUEST_INFO+ " 999PKlistoption 2 0 0\"><font color=\""+temp1+"\">PKs</font></a></td></tr>"
			htmltext +="<tr><td><br></td></tr>"
			if eventParam1 == "1" and self.pvppage > 0:
				htmltext += self.htmltext1[int(eventParam2)]
			elif eventParam1 == "2" and self.pkpage > 0:
				htmltext += self.htmltext2[int(eventParam2)]
			htmltext += "</table></body></html>"
			return htmltext

		if event == "getnoble":
			count=st.getQuestItemsCount(NOBLE_ITEMID)
			htmltext = "<html><title>L2Ghoul</title><body><center>"
			if st.getPlayer().isNoble():
				htmltext += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You already are Noblesse.</font><br>"
			elif st.getPlayer().getLevel() < NOBLE_LEVEL :
				htmltext += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">Come back here when you reach <font color=\"FFFF00\">level "+str(NOBLE_Level)+".</font></font><br>"
			elif count < NOBLE_ITEM_COUNT :	
                        	htmltext += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You don't have the required Item.<br1>You need <font color=\"FFFF00\">"+str(NOBLE_ITEM_COUNT)+" "+getitemname(NOBLE_ITEMID)+"</font></font><br>"
			else:
				st.getPlayer().setTarget(st.getPlayer())
				if eventParam1 == "2":
					st.takeItems(NOBLE_ITEMID,NOBLE_ITEM_COUNT)
                                	st.getPlayer().setNoble(True)
                                	st.giveItems(NOBLESS_TIARA,1)
                                	st.playSound("ItemSound.quest_finish")
					st.setState(State.COMPLETED)
					htmltext += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"00FF00\">Congratulations! You are Noblesse.</font><br>"
                                	st.exitQuest(1)	
				else: return
			htmltext += "</center></body></html>"
			return htmltext
			
		if event == "changeclass":
       			st.player.setClassId(int(eventParam1))
        		if st.player.isSubClassActive():
        			st.player.getSubClasses().get(st.player.getClassIndex()).setClassId(st.player.getActiveClass())
        		else: st.player.setBaseClass(st.player.getActiveClass())
			if st.getPlayer().getClassId().level() == 3:
				st.giveItems(CLASSMASTER_GIFT,CLASSMASTER_ITEM_COUNT)
			st.player.broadcastUserInfo()

   		if event == "increaseclan" :
     			if st.player.getClan() == None or not st.player.isClanLeader() :
      				MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You are not the leader of this clan.<br1>You may not raise the level of it.</font><br>"
				MAIN_HTML += "</center></body></html>"
				return MAIN_HTML
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">Clan level can be raised.<br1>To do so requires the following:</font><br><br>"
			MAIN_HTML += "<button value=\"Level Up\" action=\"bypass -h Quest " +QUEST_INFO+ " increase_clan 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 1:</font> 20,000 SP, 650,000 Adena<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 2:</font> 100,000 SP, 2,500,000 Adena<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 3:</font> 350,000 SP, Evidence of Blood<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 4:</font> 1,000,000 SP, Evidence of Determination<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 5:</font> 2,500,000 SP, Evidence of Aspiration<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 6:</font> 10,000 Clan Fame points,<br1>more than 30 clan members<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 7:</font> 20,000 Clan Fame points,<br1>more than 80 clan members<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 8:</font> 40,000 Clan Fame points,<br1>more than 120 clan members<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 9:</font> 40,000 Clan Fame points,<br1>more than 120 clan members, 150 Blood Oaths<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 10:</font> 40,000 Clan Fame points,<br1>more than 140 clan members, 5 Blood Alliances<br1>"
			MAIN_HTML += "<font color=\"LEVEL\">Level 11:</font> 75,000 Clan Fame points,<br1>more than 170 clan members, Territory Owner<br1>"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

		if event == "increase_clan" :
            		if not st.player.isClanLeader():
                		st.player.sendPacket(SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT))
                		return
            		if st.player.getClan().levelUpClan(st.player):
             			st.player.broadcastPacket(MagicSkillUse(st.player, 5103, 1, 0, 0))
            			st.player.broadcastPacket(MagicSkillLaunched(st.player, 5103, 1))
				return
			return

   		elif event == "DisbandClan" :
     			if st.player.getClan() == None or not st.player.isClanLeader() :
      				MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You are not the leader of this clan.<br1>You may not dissolve it.</font><br>"
				MAIN_HTML += "</center></body></html>"
				return MAIN_HTML
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">If you apply for dissolution,<br1>the clan will be dissolved.</font><br><br>"
			MAIN_HTML += "<button value=\"Dissolution\" action=\"bypass -h Quest " +QUEST_INFO+ " dissolve_clan 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

		elif event == "dissolve_clan" :
        		if not st.player.isClanLeader():
    				st.player.sendPacket(SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT))
            			return
        		clan = st.player.getClan()
        		if clan.getAllyId() != 0:
            			st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISPERSE_THE_CLANS_IN_ALLY))
            			return
        		if clan.isAtWar():
				st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_WAR))
            			return
			if clan.getHasCastle() !=0 or clan.getHasHideout() != 0 or clan.getHasFort() != 0:
				st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISSOLVE_WHILE_OWNING_CLAN_HALL_OR_CASTLE))
            			return
			for castle in CastleManager.getInstance().getCastles():
				if SiegeManager.getInstance().checkIsRegistered(clan, castle.getCastleId()):
					st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE))
                			return
			for fort in FortManager.getInstance().getForts():
 				if FortSiegeManager.getInstance().checkIsRegistered(clan, fort.getFortId()):
					st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE))
                			return
			if st.player.isInsideZone(L2PcInstance.ZONE_SIEGE) :
  				st.player.sendPacket(SystemMessage(SystemMessageId.CANNOT_DISSOLVE_WHILE_IN_SIEGE))
            			return
			if clan.getDissolvingExpiryTime() > System.currentTimeMillis() :
				st.player.sendPacket(SystemMessage(SystemMessageId.DISSOLUTION_IN_PROGRESS))
            			return
			clan.setDissolvingExpiryTime(System.currentTimeMillis() + Config.ALT_CLAN_DISSOLVE_DAYS * 86400000L)
			st.player.sendMessage("Dissolution in progress. Please Wait "+str(Config.ALT_CLAN_DISSOLVE_DAYS * 24L)+" hours to be completed")
		    	clan.updateClanInDB()
			ClanTable.getInstance().scheduleRemoveClan(clan.getClanId())
        		st.player.deathPenalty(False, False, False)

   		elif event == "RestoreClan" :
			if st.player.getClan() == None or not st.player.isClanLeader() :
				MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You are not the leader of this clan.<br1>You may not recover it.</font><br>"
				MAIN_HTML += "</center></body></html>"
				return MAIN_HTML
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">If you request a restoration<br1>your clan dissolution request will be canceled</font><br><br>"
			MAIN_HTML += "<button value=\"Restoration\" action=\"bypass -h Quest " +QUEST_INFO+ " recover_clan 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

   		elif event == "recover_clan" :
		        if not st.player.isClanLeader():
				st.player.sendPacket(SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT))
            			return
			clan = st.player.getClan()
			clan.setDissolvingExpiryTime(0)
			clan.updateClanInDB()
			st.player.sendMessage("Dissolution progress canceled")
	
   		elif event == "giveclanl" :
     			if st.player.getClan() == None or not st.player.isClanLeader() :
      	 			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You are not a Clan Leader.</font><br>"
				MAIN_HTML += "</center></body></html>"
				return MAIN_HTML
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">Insert the name of the Clan member<br1>to commit</font><br><br>"
			MAIN_HTML += "<edit var=\"name\" width=110><br><br>"
			MAIN_HTML += "<button value=\"Enter\" action=\"bypass -h Quest " +QUEST_INFO+ " change_clan_leader $name no_data 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

		elif event == "change_clan_leader" :
			if eventParam1 == "no_data": return
        		if not st.player.isClanLeader():
            			st.player.sendPacket(SystemMessage(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT))
            			return
			if st.player.isFlying():
				st.player.sendMessage("Please, stop flying")
				return
			clan = st.player.getClan()
			member = clan.getClanMember(eventParam1)
			if member == None:
				sm = SystemMessage(SystemMessageId.S1_DOES_NOT_EXIST)
				sm.addString(eventParam1)
				st.player.sendPacket(sm)
				sm = None
				return
			if not member.isOnline():
            			st.player.sendPacket(SystemMessage(SystemMessageId.INVITED_USER_NOT_ONLINE))
				return
			if st.player.getName() != member.getName(): return
			clan.setNewLeader(member)

		elif event == "learn_clan_skills":
			if st.player.getClan() == None or not st.player.isClanLeader():
				MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
				MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You're not qualified to learn Clan skills.</font><br>"
				MAIN_HTML += "</center></body></html>"
				return MAIN_HTML
			skills = SkillTreeTable.getInstance().getAvailablePledgeSkills(st.player)
			asl = AcquireSkillList(AcquireSkillList.SkillType.Clan)
			counts = 0
			for s in skills:
				cost = s.getRepCost()
				itemCount = s.getItemCount()
				counts+=1
				asl.addSkill(s.getId(), s.getLevel(), s.getLevel(), cost, itemCount)
			if counts == 0:
				if st.player.getClan().getLevel() < 8:
					sm = SystemMessage(SystemMessageId.DO_NOT_HAVE_FURTHER_SKILLS_TO_LEARN)
                			if st.player.getClan().getLevel() < 5:
                				sm.addNumber(5)
                			else:
                				sm.addNumber(st.player.getClan().getLevel()+1)
                			st.player.sendPacket(sm)
                			st.player.sendPacket(AcquireSkillDone())
				else:
					MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
					MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"LEVEL\">You've learned all skills available for your Clan.</font><br>"
					MAIN_HTML += "</center></body></html>"
					return MAIN_HTML
			else:
            			st.player.sendPacket(asl)

		elif event == "createclan" :
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">.::Enter clan name::.</font><br><br>"
			MAIN_HTML += "<edit var=\"name\" width=110><br><br>"
			MAIN_HTML += "<button value=\"Enter\" action=\"bypass -h Quest " +QUEST_INFO+ " create_clan $name no_data 0 0 \" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

		elif event == "create_clan" :
			if eventParam1 == "no_data": return
            		ClanTable.getInstance().createClan(st.player, eventParam1)
			return

		elif event == "createally":
			MAIN_HTML = "<html><title>L2Ghoul</title><body><center>"
			MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>"
			MAIN_HTML += "<font color=\"LEVEL\">.::Enter Alliance name::.</font><br><br>"
			MAIN_HTML += "<edit var=\"name\" width=110><br><br>"
			MAIN_HTML += "<button value=\"Enter\" action=\"bypass -h Quest " +QUEST_INFO+ " create_ally $name no_data 0 0 \" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\"><br1>"
			MAIN_HTML += "<button value=\"Back\" action=\"bypass -h Quest " +QUEST_INFO+ " chat4 0 0 0\" width=80 height=30 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_ct1.button_df\">"
			MAIN_HTML += "</center></body></html>"
			return MAIN_HTML

		elif event == "create_ally":
			if eventParam1 == "no_data": return
            		if not st.player.isClanLeader():
				st.player.sendPacket(SystemMessage(SystemMessageId.ONLY_CLAN_LEADER_CREATE_ALLIANCE))
                		return
			st.player.getClan().createAlly(st.player, eventParam1)
			return

		elif event == "dissolve_ally":
			if not st.player.isClanLeader():
				st.player.sendPacket(SystemMessage(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER))
                		return
            		st.player.getClan().dissolveAlly(st.player)
			return
		else: 	return

	def onFirstTalk (self,npc,player):
		st = player.getQuestState(QUEST_INFO)
		if not st : st = self.newQuestState(player)
		if player.isGM() and player.getAccessLevel().getLevel() == GM_ACCESS_LEVEL:
			if GM_RELOAD_PANEL == True: filename = "data/html/teleporter/mainhtmls/mainhtml-2.htm"
			else: filename = "data/html/teleporter/mainhtmls/mainhtml-0.htm"
			html = NpcHtmlMessage(npc.getObjectId())
			html.setFile(None,filename)
			html.replace("%objectId%", str(npc.getObjectId()))
			st.player.sendPacket(html)
		elif int(System.currentTimeMillis()/1000) > st.getInt("blockUntilTime"):
			if ALLOW_VIP == False or player.getAccessLevel().getLevel() == VIP_ACCESS_LEVEL and ALLOW_VIP == True:
				if ALLOW_KARMA_PLAYER == False and player.getKarma() > 0 :
					return showText("Info","You have too much karma!<br>Come back,<br>when you don't have any karma!")
				elif st.player.getPvpFlag() > 0 :
					return showText("Info","You can't use my services while you are flagged!<br>Wait some time and try again!")
				elif st.player.isInCombat() :
					return showText("Info","You can't use my services while you are attacking!<br>Stop your fight and try again!")
				else:
					filename = "data/html/teleporter/mainhtmls/mainhtml-0.htm"
					html = NpcHtmlMessage(npc.getObjectId())
					html.setFile(None,filename)
					html.replace("%objectId%", str(npc.getObjectId()))
					st.player.sendPacket(html)
			else:
				return showText("Sorry","This NPC is only for VIP's!<br>Contact the administrator for more info!")

	   	else: return

QUEST = Quest(QuestId,QUEST_INFO,QuestDesc)

QUEST.addStartNpc(npcId)
QUEST.addFirstTalkId(npcId)
QUEST.addTalkId(npcId)