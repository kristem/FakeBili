/*
 Navicat Premium Data Transfer

 Source Server         : newLocal
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : bilibili

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 10/03/2018 20:30:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `password` varchar(75) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `photoUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `bigVip` bit(1) NOT NULL,
  `level` int(11) NOT NULL,
  `coin` int(11) NOT NULL,
  `commentNumber` int(11) DEFAULT 0,
  `upNumber` int(11) DEFAULT NULL,
  `userSign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `signInDate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `IsAdmin` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `videoName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `coinNum` int(11) DEFAULT 0,
  `simpleUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `coverUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `summary` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `upUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `barrageNumber` int(11) DEFAULT 0,
  `sonDiv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `fatherDiv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `videoSize` bigint(20) DEFAULT NULL,
  `hour` int(11) DEFAULT NULL,
  `min` int(11) DEFAULT NULL,
  `sec` int(11) DEFAULT NULL,
  `videoUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isPass` bit(1) DEFAULT b'0',
  `videoDate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `upUserId` int(11) DEFAULT NULL,
  `realPath` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `hits` int(11) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `video_ibfk_1`(`upUser`) USING BTREE,
  CONSTRAINT `video_ibfk_1` FOREIGN KEY (`upUser`) REFERENCES `user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 164 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
