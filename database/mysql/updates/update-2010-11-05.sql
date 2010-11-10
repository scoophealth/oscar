--
-- Table structure for table `eyeform_followup`
--

DROP TABLE IF EXISTS `eyeform_followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eyeform_followup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `demographic_no` int(11) ,
  `timespan` int(11) ,
  `timeframe` varchar(25) ,
  `followup_provider` varchar(100) ,
  `date` timestamp ,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eyeform_macro`
--

DROP TABLE IF EXISTS `eyeform_macro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eyeform_macro` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(255) COLLATE utf8_bin NOT NULL,
  `display_order` tinyint(4) NOT NULL,
  `impression` text COLLATE utf8_bin,
  `followup_no` smallint(5),
  `followup_unit` varchar(50) COLLATE utf8_bin ,
  `followup_doctor` varchar(6) COLLATE utf8_bin ,
  `followup_reason` varchar(255) COLLATE utf8_bin ,
  `tickler_staff` varchar(6) COLLATE utf8_bin ,
  `billing_visit_type` varchar(50) COLLATE utf8_bin,
  `billing_visit_location` varchar(50) COLLATE utf8_bin ,
  `billing_codes` text COLLATE utf8_bin,
  `billing_dxcode` varchar(50) COLLATE utf8_bin ,
  `billing_total` varchar(50) COLLATE utf8_bin ,
  `billing_comment` varchar(255) COLLATE utf8_bin ,
  `billing_billtype` varchar(50) COLLATE utf8_bin ,
  `billing_pay_method` varchar(50) COLLATE utf8_bin ,
  `billing_billto` varchar(50) COLLATE utf8_bin ,
  `billing_remitto` varchar(50) COLLATE utf8_bin ,
  `billing_gstBilledTotal` varchar(50) COLLATE utf8_bin ,
  `billing_payment` varchar(50) COLLATE utf8_bin ,
  `billing_refund` varchar(50) COLLATE utf8_bin ,
  `billing_gst` varchar(50) COLLATE utf8_bin ,
  `test_records` text COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ocularprocedurehis`
--

DROP TABLE IF EXISTS `ocularprocedurehis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ocularprocedurehis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date NOT NULL ,
  `eye` varchar(2) NOT NULL ,
  `procedure_name` varchar(100) NOT NULL ,
  `procedure_type` varchar(30) ,
  `procedure_note` text,
  `doctor` varchar(30) ,
  `location` varchar(30) ,
  `update_time` datetime ,
  `status` varchar(2) ,
  `appointment_no` int(11) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procedurebook`
--

DROP TABLE IF EXISTS `procedurebook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procedurebook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eyeform_id` bigint(20) ,
  `demographic_no` int(11) ,
  `appointment_no` int(11) ,
  `provider` varchar(60) ,
  `procedure_name` varchar(30) ,
  `eye` varchar(20) ,
  `location` varchar(50) ,
  `urgency` varchar(10) ,
  `comment` text,
  `date` datetime ,
  `status` varchar(2) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specshis`
--

DROP TABLE IF EXISTS `specshis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specshis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date ,
  `doctor` varchar(30) ,
  `type` varchar(30) ,
  `od_sph` varchar(10),
  `od_cyl` varchar(10) ,
  `od_axis` varchar(10) ,
  `od_add` varchar(10) ,
  `od_prism` varchar(10) ,
  `os_sph` varchar(10) ,
  `os_cyl` varchar(10) ,
  `os_axis` varchar(10) ,
  `os_add` varchar(10) ,
  `os_prism` varchar(10) ,
  `update_time` datetime ,
  `status` varchar(2) ,
  `appointment_no` int(11) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `testbookrecord`
--

DROP TABLE IF EXISTS `testbookrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `testbookrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `testname` varchar(60) ,
  `appointment_no` int(11) ,
  `demographic_no` int(11) ,
  `provider` varchar(60) ,
  `eyeform_id` bigint(20) ,
  `eye` varchar(20) ,
  `urgency` varchar(30) ,
  `comment` varchar(100) ,
  `date` datetime ,
  `status` varchar(2) ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 ;
/*!40101 SET character_set_client = @saved_cs_client */;

