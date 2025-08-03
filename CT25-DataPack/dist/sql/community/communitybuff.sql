/*
Navicat MySQL Data Transfer

Source Server         : l2jdb
Source Server Version : 50509
Source Host           : localhost:3306
Source Database       : l2jdb

Target Server Type    : MYSQL
Target Server Version : 50509
File Encoding         : 65001

Date: 2011-03-01 22:31:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `communitybuff`
-- ----------------------------
DROP TABLE IF EXISTS `communitybuff`;
CREATE TABLE `communitybuff` (
  `key` int(11) DEFAULT NULL,
  `skillID` int(11) DEFAULT NULL,
  `buff_id` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `itemid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of communitybuff
-- ----------------------------
INSERT INTO `communitybuff` VALUES ('1', '7041', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('2', '7042', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('3', '7043', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('4', '7044', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('5', '7045', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('6', '7046', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('7', '7047', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('8', '7049', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('9', '7050', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('10', '7051', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('11', '7052', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('12', '7053', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('13', '7055', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('14', '7056', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('15', '7057', '1', '10000', '57');
INSERT INTO `communitybuff` VALUES ('16', '7060', '3', '10000', '57');
INSERT INTO `communitybuff` VALUES ('17', '7048', '2', '10000', '57');
INSERT INTO `communitybuff` VALUES ('18', '7054', '2', '10000', '57');
INSERT INTO `communitybuff` VALUES ('19', '7058', '2', '10000', '57');
INSERT INTO `communitybuff` VALUES ('20', '7059', '2', '10000', '57');
INSERT INTO `communitybuff` VALUES ('21', '264', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('22', '265', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('23', '267', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('24', '268', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('25', '269', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('26', '271', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('27', '274', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('28', '275', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('29', '304', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('30', '310', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('31', '349', '6', '10000', '57');
INSERT INTO `communitybuff` VALUES ('33', '364', '4', '10000', '57');
INSERT INTO `communitybuff` VALUES ('32', '273', '5', '10000', '57');
INSERT INTO `communitybuff` VALUES ('33', '276', '5', '10000', '57');
INSERT INTO `communitybuff` VALUES ('34', '363', '5', '10000', '57');
INSERT INTO `communitybuff` VALUES ('36', '1413', '5', '10000', '57');
INSERT INTO `communitybuff` VALUES ('35', '365', '5', '10000', '57');
