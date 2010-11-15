--
-- Table structure for table `eyeform`
--

DROP TABLE IF EXISTS `eyeform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eyeform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `discharge` varchar(20),
  `stat` varchar(20),
  `opt` varchar(20),
  `date` timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 ;
/*!40101 SET character_set_client = @saved_cs_client */;

