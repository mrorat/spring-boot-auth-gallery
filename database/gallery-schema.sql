-- MySQL dump 10.14  Distrib 5.5.68-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: gallery
-- ------------------------------------------------------
-- Server version	5.5.68-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `albumid` varchar(36) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(200) NOT NULL,
  `path` varchar(500) NOT NULL,
  PRIMARY KEY (`albumid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `album_permissions`
--

DROP TABLE IF EXISTS `album_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album_permissions` (
  `userid` varchar(36) NOT NULL,
  `albumid` varchar(36) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`userid`,`albumid`),
  KEY `fk_album_perm_album_idx` (`albumid`),
  CONSTRAINT `fk_album_perm_album` FOREIGN KEY (`albumid`) REFERENCES `album` (`albumid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_album_perm_user` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `imageid` varchar(36) NOT NULL,
  `albumid` varchar(36) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(200) NOT NULL,
  `path` varchar(500) NOT NULL,
  `next_image_id` varchar(36) DEFAULT NULL,
  `previous_image_id` varchar(36) DEFAULT NULL,
  `is_vertical` tinyint(4) DEFAULT '0',
  `duplicate_of_imageid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`imageid`),
  KEY `FK_image_album` (`albumid`),
  CONSTRAINT `FK_image_album` FOREIGN KEY (`albumid`) REFERENCES `album` (`albumid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `roleid` varchar(36) NOT NULL,
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` varchar(100) NOT NULL,
  `deleted_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `role_name_unq` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `a` int(11) NOT NULL AUTO_INCREMENT,
  `b` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`a`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userid` varchar(36) NOT NULL,
  `username` varchar(254) NOT NULL,
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_date` timestamp NULL DEFAULT NULL,
  `is_deleted` varchar(36) NOT NULL DEFAULT 'NOT_DELETED',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `user_unique_name` (`username`,`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_roles` (
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_date` timestamp NULL DEFAULT NULL,
  `userid` varchar(36) NOT NULL,
  `roleid` varchar(36) NOT NULL,
  KEY `userid` (`userid`),
  KEY `roleid` (`roleid`),
  CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO role (roleid, name) 
	VALUES 
	(UUID(), 'ROLE_ADMIN'),
	(UUID(), 'ROLE_USER'),
	(UUID(), 'ROLE_GUEST');



/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


