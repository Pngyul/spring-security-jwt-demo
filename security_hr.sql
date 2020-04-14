/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 50729
Source Host           : 47.107.86.45:3306
Source Database       : security_hr

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-04-14 20:47:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `hr`
-- ----------------------------
DROP TABLE IF EXISTS `hr`;
CREATE TABLE `hr` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'hrID',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hr
-- ----------------------------
INSERT INTO `hr` VALUES ('1', 'pngyul', '$2a$10$0WjZpkSCAHKrcQ/a3.CwOuWP3arJOloYs2oecrC9oPny9HWdnZ.Ti');
INSERT INTO `hr` VALUES ('3', 'lisi', '$2a$10$0WjZpkSCAHKrcQ/a3.CwOuWP3arJOloYs2oecrC9oPny9HWdnZ.Ti');
INSERT INTO `hr` VALUES ('5', 'xiaohong', '$2a$10$NRYzZTFPSDorqpIVXJaSxuOwITR6NJmFTJZUBkLcBK7JyRNxdfxTy');
INSERT INTO `hr` VALUES ('6', 'xiaoming', '$2a$10$NRYzZTFPSDorqpIVXJaSxuOwITR6NJmFTJZUBkLcBK7JyRNxdfxTy');

-- ----------------------------
-- Table structure for `hr_role`
-- ----------------------------
DROP TABLE IF EXISTS `hr_role`;
CREATE TABLE `hr_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hrid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hr_role
-- ----------------------------
INSERT INTO `hr_role` VALUES ('1', '1', '6');
INSERT INTO `hr_role` VALUES ('2', '3', '1');
INSERT INTO `hr_role` VALUES ('3', '3', '3');
INSERT INTO `hr_role` VALUES ('4', '5', '2');
INSERT INTO `hr_role` VALUES ('5', '5', '3');
INSERT INTO `hr_role` VALUES ('6', '5', '4');
INSERT INTO `hr_role` VALUES ('7', '6', '2');

-- ----------------------------
-- Table structure for `menu`
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '/home', '主页', null);
INSERT INTO `menu` VALUES ('2', '/emp/basic/**', '员工信息', '1');
INSERT INTO `menu` VALUES ('3', '/per/train/**', '员工培训', '1');
INSERT INTO `menu` VALUES ('4', '/emp/adv/**', '员工高级资料', '1');
INSERT INTO `menu` VALUES ('5', '/sal/**', '工资', '1');
INSERT INTO `menu` VALUES ('6', '/sta/**', '统计', '1');
INSERT INTO `menu` VALUES ('7', '/sys/**', '系统操作', '6');

-- ----------------------------
-- Table structure for `menu_role`
-- ----------------------------
DROP TABLE IF EXISTS `menu_role`;
CREATE TABLE `menu_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mid` int(11) DEFAULT NULL,
  `rid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu_role
-- ----------------------------
INSERT INTO `menu_role` VALUES ('1', '4', '1');
INSERT INTO `menu_role` VALUES ('2', '4', '6');
INSERT INTO `menu_role` VALUES ('3', '3', '6');
INSERT INTO `menu_role` VALUES ('4', '3', '4');
INSERT INTO `menu_role` VALUES ('5', '2', '6');
INSERT INTO `menu_role` VALUES ('6', '2', '4');
INSERT INTO `menu_role` VALUES ('7', '2', '3');
INSERT INTO `menu_role` VALUES ('8', '2', '2');
INSERT INTO `menu_role` VALUES ('9', '2', '1');
INSERT INTO `menu_role` VALUES ('15', '7', '6');
INSERT INTO `menu_role` VALUES ('16', '6', '6');
INSERT INTO `menu_role` VALUES ('17', '6', '4');
INSERT INTO `menu_role` VALUES ('18', '5', '6');
INSERT INTO `menu_role` VALUES ('19', '5', '4');
INSERT INTO `menu_role` VALUES ('20', '5', '3');
INSERT INTO `menu_role` VALUES ('21', '5', '1');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `nameZh` varchar(64) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'ROLE_manager', '部门经理');
INSERT INTO `role` VALUES ('2', 'ROLE_personnel', '人事专员');
INSERT INTO `role` VALUES ('3', 'ROLE_recruiter', '招聘主管');
INSERT INTO `role` VALUES ('4', 'ROLE_train', '培训主管');
INSERT INTO `role` VALUES ('5', 'ROLE_performance', '薪酬绩效主管');
INSERT INTO `role` VALUES ('6', 'ROLE_admin', '系统管理员');
INSERT INTO `role` VALUES ('13', 'ROLE_test2', '测试角色2');
INSERT INTO `role` VALUES ('14', 'ROLE_test1', '测试角色1');
INSERT INTO `role` VALUES ('17', 'ROLE_test3', '测试角色3');
INSERT INTO `role` VALUES ('18', 'ROLE_test4', '测试角色4');
INSERT INTO `role` VALUES ('19', 'ROLE_test4', '测试角色4');
INSERT INTO `role` VALUES ('20', 'ROLE_test5', '测试角色5');
INSERT INTO `role` VALUES ('21', 'ROLE_test6', '测试角色6');
