-- MySQL dump 10.11
--
-- Host: localhost    Database: oscar_svn
-- ------------------------------------------------------
-- Server version	5.0.45-Debian_1ubuntu3.3-log

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
-- Table structure for table `appointment_status`
--


CREATE TABLE `appointment_status` (
  `id` int(11) NOT NULL auto_increment,
  `status` char(2) NOT NULL,
  `description` char(30) NOT NULL default 'no description',
  `color` char(7) NOT NULL default '#cccccc',
  `icon` char(30) NOT NULL default '''''',
  `active` int(1) NOT NULL default '1',
  `editable` int(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `appointment_status`
--

LOCK TABLES `appointment_status` WRITE;
/*!40000 ALTER TABLE `appointment_status` DISABLE KEYS */;
INSERT INTO `appointment_status` VALUES (1,'t','To Do','#FDFEC7','starbill.gif',1,0),(2,'T','Daysheet Printed','#FDFEC7','todo.gif',1,0),(3,'H','Here','#00ee00','here.gif',1,1),(4,'P','Picked','#FFBBFF','picked.gif',1,1),(5,'E','Empty Room','#FFFF33','empty.gif',1,1),(11,'N','No Show','#cccccc','noshow.gif',1,0),(12,'C','Cancelled','#999999','cancel.gif',1,0),(13,'B','Billed','#3ea4e1','billed.gif',1,0),(6,'a','Customized 1','#897DF8','1.gif',1,1),(7,'b','Customized 2','#897DF8','2.gif',1,1),(8,'c','Customized 3','#897DF8','3.gif',0,1),(9,'d','Customized 4','#897DF8','4.gif',1,1),(10,'e','Customized 5','#897DF8','5.gif',1,1);
/*!40000 ALTER TABLE `appointment_status` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-04-16  6:07:59
