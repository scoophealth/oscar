-- MySQL dump 10.9
--
-- Host: localhost    Database: oscar_mcmaster
-- ------------------------------------------------------
-- Server version	4.1.22-standard

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `formConsult`
--

DROP TABLE IF EXISTS `formConsult`;
CREATE TABLE `formConsult` (
  `ID` int(10) NOT NULL auto_increment,
  `provider_no` int(10) default NULL,
  `doc_name` varchar(60) default NULL,
  `cl_name` varchar(60) default NULL,
  `cl_address1` varchar(170) default NULL,
  `cl_address2` varchar(170) default NULL,
  `cl_phone` varchar(16) default NULL,
  `cl_fax` varchar(16) default NULL,
  `billingreferral_no` int(10) default NULL,
  `t_name` varchar(60) default NULL,
  `t_address1` varchar(170) default NULL,
  `t_address2` varchar(170) default NULL,
  `t_phone` varchar(16) default NULL,
  `t_fax` varchar(16) default NULL,
  `demographic_no` int(10) not null default 0,
  `p_name` varchar(60) default NULL,
  `p_address1` varchar(170) default NULL,
  `p_address2` varchar(170) default NULL,
  `p_phone` varchar(16) default NULL,
  `p_birthdate` varchar(30) default NULL,
  `p_healthcard` varchar(20) default NULL,
  `comments` text,
  `formCreated` date default NULL,
  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `formConsult`
--

-- MySQL dump 10.9
--
-- Host: localhost    Database: oscar_mcmaster
-- ------------------------------------------------------
-- Server version	4.1.22-standard

--
-- Table structure for table `encounterForm`
--

--
-- Dumping data for table `encounterForm`
--

LOCK TABLES `encounterForm` WRITE;
/*!40000 ALTER TABLE `encounterForm` DISABLE KEYS */;

insert into encounterForm values("Letterhead", "../form/formConsultant.jsp?demographic_no=", "formConsult", 0);
/*!40000 ALTER TABLE `encounterForm` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

