-- MySQL dump 10.9
2 	--
3 	-- Host: localhost    Database: oscar_mcmaster
4 	-- ------------------------------------------------------
5 	-- Server version       4.1.22-standard
6 	
7 	/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
8 	/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
9 	/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
10 	/*!40101 SET NAMES utf8 */;
11 	/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
12 	/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
13 	/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
14 	/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
15 	
16 	--
17 	-- Table structure for table `formConsult`
18 	--
19 	
20 	DROP TABLE IF EXISTS `formConsult`;
21 	CREATE TABLE `formConsult` (
22 	  `ID` int(10) NOT NULL auto_increment,
23 	  `provider_no` int(10) default NULL,
24 	  `doc_name` varchar(60) default NULL,
25 	  `cl_name` varchar(60) default NULL,
26 	  `cl_address1` varchar(170) default NULL,
27 	  `cl_address2` varchar(170) default NULL,
28 	  `cl_phone` varchar(16) default NULL,
29 	  `cl_fax` varchar(16) default NULL,
30 	  `billingreferral_no` int(10) default NULL,
31 	  `t_name` varchar(60) default NULL,
32 	  `t_address1` varchar(170) default NULL,
33 	  `t_address2` varchar(170) default NULL,
34 	  `t_phone` varchar(16) default NULL,
35 	  `t_fax` varchar(16) default NULL,
36 	  `demographic_no` int(10) not null default 0,
37 	  `p_name` varchar(60) default NULL,
38 	  `p_address1` varchar(170) default NULL,
39 	  `p_address2` varchar(170) default NULL,
40 	  `p_phone` varchar(16) default NULL,
41 	  `p_birthdate` varchar(30) default NULL,
42 	  `p_healthcard` varchar(20) default NULL,
43 	  `comments` text,
44 	  `formCreated` date default NULL,
45 	  `formEdited` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
46 	  PRIMARY KEY  (`ID`)
47 	) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
48 	
49 	--
50 	-- Dumping data for table `formConsult`
51 	--
52 	
53 	-- MySQL dump 10.9
54 	--
55 	-- Host: localhost    Database: oscar_mcmaster
56 	-- ------------------------------------------------------
57 	-- Server version       4.1.22-standard
58 	
59 	--
60 	-- Table structure for table `encounterForm`
61 	--
62 	
63 	--
64 	-- Dumping data for table `encounterForm`
65 	--
66 	
67 	LOCK TABLES `encounterForm` WRITE;
68 	/*!40000 ALTER TABLE `encounterForm` DISABLE KEYS */;
69 	
70 	insert into encounterForm values("Letterhead", "../form/formConsultant.jsp?demographic_no=", "formConsult", 0);
71 	/*!40000 ALTER TABLE `encounterForm` ENABLE KEYS */;
72 	UNLOCK TABLES;
73 	
74 	/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
75 	/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
76 	/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
77 	/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
78 	/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
79 	/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
80 	/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;