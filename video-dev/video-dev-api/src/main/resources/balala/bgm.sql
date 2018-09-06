/*
Navicat MySQL Data Transfer

Source Server         : hr_mysql
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : springboot

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2018-09-06 17:31:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bgm
-- ----------------------------
DROP TABLE IF EXISTS `bgm`;
CREATE TABLE `bgm` (
  `id` varchar(64) NOT NULL,
  `author` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '播放地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bgm
-- ----------------------------
INSERT INTO `bgm` VALUES ('18052674D26HH3X7', '测试歌手名字', 'abc歌曲~~', '\\bgm\\summer.mp3');
INSERT INTO `bgm` VALUES ('18052674D26HH3X8', '测试歌手名字', 'abc歌曲~~', '\\bgm\\summer.mp3');
INSERT INTO `bgm` VALUES ('18052674D26HH3X9', '测试歌手名字', 'abc歌曲~~', '\\bgm\\summer.mp3');
INSERT INTO `bgm` VALUES ('1805290R3WTDMT9P', 'aa', 'aa', '\\bgm\\summer.mp3');
INSERT INTO `bgm` VALUES ('180530DXKK4YYGTC', '达瓦', 'dwadw', '\\bgm\\summer.mp3');
