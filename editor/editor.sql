/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 6.0.11-alpha-community : Database - editor
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`editor` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `editor`;

/*Table structure for table `_tt_alltypes` */

DROP TABLE IF EXISTS `_tt_alltypes`;

CREATE TABLE `_tt_alltypes` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `jbyte` tinyint(4) DEFAULT NULL,
  `jdouble` double DEFAULT NULL,
  `jfloat` float DEFAULT NULL,
  `jint` int(11) DEFAULT NULL,
  `jlong` bigint(20) DEFAULT NULL,
  `jshort` smallint(6) DEFAULT NULL,
  `jstring` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `jstringarray` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `_tt_alltypes` */

/*Table structure for table `_tt_arrayrule` */

DROP TABLE IF EXISTS `_tt_arrayrule`;

CREATE TABLE `_tt_arrayrule` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `constraint_id` bigint(20) DEFAULT NULL,
  `description` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `idx` int(11) NOT NULL,
  `keyField` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `keyValue` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `tableName` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `targetField` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `_tt_arrayrule` */

insert  into `_tt_arrayrule`(`code`,`constraint_id`,`description`,`idx`,`keyField`,`keyValue`,`tableName`,`targetField`) values (1,-1,'1st',1,'type','1','talent','value'),(2,-1,'2nd',2,'type','1','talent','value'),(3,-1,'3rd',3,'type','1','talent','value'),(4,-1,'1dx',1,'type','2','talent','value'),(5,-1,'2dx',2,'type','2','talent','value'),(6,-1,'1only',1,'type','3','talent','value'),(7,-1,'2only',2,'type','3','talent','value'),(8,-1,'数组关联1',1,'type','4','talent','value'),(9,5,'数组关联2',2,'type','4','talent','value'),(10,5,'数组关联3',3,'type','4','talent','value');

/*Table structure for table `_tt_constraint` */

DROP TABLE IF EXISTS `_tt_constraint`;

CREATE TABLE `_tt_constraint` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `aname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `apos` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bname` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `bpos` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `_tt_constraint` */

insert  into `_tt_constraint`(`code`,`aname`,`apos`,`bname`,`bpos`) values (1,'_tt_arrayrule','constraint_id','_tt_constraint','code'),(2,'mission','pre','mission','code'),(3,'mission','next','mission','code'),(4,'talent','value','_tt_alltypes','code'),(5,'talent','value','mission','code');

/*Table structure for table `mission` */

DROP TABLE IF EXISTS `mission`;

CREATE TABLE `mission` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `conditionz` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ctype` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `next` bigint(20) NOT NULL,
  `pre` bigint(20) NOT NULL,
  `previewlvl` int(11) NOT NULL,
  `repeatable` tinyint(1) NOT NULL,
  `startTime` bigint(20) NOT NULL,
  `talk` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `time` bigint(20) NOT NULL,
  `timeType` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `mission` */

insert  into `mission`(`code`,`conditionz`,`ctype`,`level`,`name`,`next`,`pre`,`previewlvl`,`repeatable`,`startTime`,`talk`,`time`,`timeType`,`type`) values (1,'1',1,111,'任务1',-1,-1,1,1,1111,'？？？',1111,1,1),(2,'1',1,11,'任务2',-1,-1,1,1,2222,'！！！',2222,1,1);

/*Table structure for table `talent` */

DROP TABLE IF EXISTS `talent`;

CREATE TABLE `talent` (
  `code` bigint(20) NOT NULL AUTO_INCREMENT,
  `Cimage` int(11) NOT NULL,
  `Csize` int(11) NOT NULL,
  `level` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `lost` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `pos` int(11) NOT NULL,
  `pre` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `type` int(11) NOT NULL,
  `value` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `talent` */

insert  into `talent`(`code`,`Cimage`,`Csize`,`level`,`lost`,`name`,`pos`,`pre`,`type`,`value`) values (1,15,14,'12','','天赋1',11,'0',23,'7,-1,6,-1');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
