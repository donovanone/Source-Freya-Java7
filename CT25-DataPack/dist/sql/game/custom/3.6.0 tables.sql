SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `rank_pvp_system`
-- ----------------------------
DROP TABLE IF EXISTS `rank_pvp_system`;
CREATE TABLE `rank_pvp_system` (
`killer_id`  int(10) NOT NULL ,
`victim_id`  int(10) NOT NULL ,
`kills`  int(10) NOT NULL DEFAULT 0 ,
`kills_today`  int(10) NOT NULL DEFAULT 0 ,
`kills_legal`  int(10) NOT NULL DEFAULT 0 ,
`kills_today_legal`  int(10) NOT NULL DEFAULT 0 ,
`rank_points`  bigint(18) NOT NULL DEFAULT 0 ,
`rank_points_today`  bigint(18) NOT NULL DEFAULT 0 ,
`war_kills_legal`  int(10) NOT NULL DEFAULT 0 ,
`war_kills`  int(10) NOT NULL DEFAULT 0 ,
`kill_time`  bigint(18) NOT NULL DEFAULT 0 ,
`kill_day`  bigint(18) NOT NULL DEFAULT 0 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `rank_pvp_system_character_rank_rewards`
-- ----------------------------
DROP TABLE IF EXISTS `rank_pvp_system_character_rank_rewards`;
CREATE TABLE `rank_pvp_system_character_rank_rewards` (
`charId`  int(10) NOT NULL ,
`reward_id`  int(10) NOT NULL 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci

;

-- ----------------------------
-- Table structure for `rank_pvp_system_rank_rewards`
-- ----------------------------
DROP TABLE IF EXISTS `rank_pvp_system_rank_rewards`;
CREATE TABLE `rank_pvp_system_rank_rewards` (
`reward_id`  int(10) NOT NULL AUTO_INCREMENT ,
`item_id`  int(10) NOT NULL ,
`item_amount`  bigint(18) NOT NULL ,
`min_rank_points`  int(10) NOT NULL COMMENT 'Should be the same like in: CustomConfig:RankMinPoints' ,
PRIMARY KEY (`reward_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=43

;

-- ----------------------------
-- Records of rank_pvp_system_rank_rewards
-- ----------------------------
BEGIN;
INSERT INTO `rank_pvp_system_rank_rewards` VALUES ('1', '57', '50000', '1'), ('2', '57', '100000', '2'), ('3', '57', '150000', '5'), ('4', '57', '200000', '10'), ('5', '57', '250000', '20'), ('6', '57', '300000', '30'), ('7', '57', '400000', '50'), ('8', '57', '450000', '90'), ('9', '57', '500000', '120'), ('10', '57', '550000', '200'), ('11', '57', '600000', '400'), ('12', '57', '650000', '600'), ('13', '57', '50000', '900'), ('14', '57', '50000', '1200'), ('15', '57', '50000', '5000'), ('16', '57', '50000', '6000'), ('17', '57', '50000', '8000'), ('18', '57', '50000', '10000'), ('19', '57', '50000', '11111'), ('20', '57', '50000', '12222'), ('21', '57', '50000', '13333'), ('22', '57', '50000', '14444'), ('23', '57', '50000', '15555'), ('24', '57', '50000', '16666'), ('25', '57', '50000', '17777'), ('26', '57', '50000', '18888'), ('27', '57', '50000', '19999'), ('28', '57', '50000', '100000'), ('29', '57', '50000', '111111'), ('30', '57', '50000', '122222'), ('31', '57', '50000', '133333'), ('32', '57', '50000', '144444'), ('33', '57', '50000', '155555'), ('34', '57', '50000', '166666'), ('35', '57', '50000', '177777'), ('36', '57', '50000', '188888'), ('37', '57', '50000', '199999'), ('38', '57', '50000', '111111'), ('39', '57', '50000', '1111111'), ('40', '57', '50000', '1111222'), ('41', '57', '50000', '1666666'), ('42', '57', '50000', '1999999');
COMMIT;

-- ----------------------------
-- Auto increment value for `rank_pvp_system_rank_rewards`
-- ----------------------------
ALTER TABLE `rank_pvp_system_rank_rewards` AUTO_INCREMENT=43;
