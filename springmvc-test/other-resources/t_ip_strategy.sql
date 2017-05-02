/*
 Navicat Premium Data Transfer

 Source Server         : dsdLocal
 Source Server Type    : MySQL
 Source Server Version : 50636
 Source Host           : localhost
 Source Database       : dsd

 Target Server Type    : MySQL
 Target Server Version : 50636
 File Encoding         : utf-8

 Date: 05/02/2017 10:30:39 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_ip_strategy`
-- ----------------------------
DROP TABLE IF EXISTS `t_ip_strategy`;
CREATE TABLE `t_ip_strategy` (
  `id` varchar(50) NOT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `ip_type` int(11) NOT NULL DEFAULT '0',
  `from_ip` varchar(20) DEFAULT NULL,
  `to_ip` varchar(20) DEFAULT NULL,
  `forbidden_reason` varchar(200) DEFAULT NULL,
  `forbidden_type` int(11) NOT NULL DEFAULT '0',
  `forbidden_start_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `forbidden_end_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_enabled` int(11) NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(50) DEFAULT NULL,
  `enabled_by` varchar(50) DEFAULT NULL,
  `disabled_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_ip_strategy`
-- ----------------------------
BEGIN;
INSERT INTO `t_ip_strategy` VALUES ('8f5c951ad2e84771a52265db332daaca3310', '127.0.0.1', '0', null, null, null, '1', '2017-04-21 10:46:28', '2117-04-21 10:46:28', '1', '2017-04-21 10:46:28', null, null, null);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
