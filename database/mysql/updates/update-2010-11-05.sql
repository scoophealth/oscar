--
-- Table structure for table `eyeform_followup`
--

DROP TABLE IF EXISTS `eyeform_followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eyeform_followup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11) DEFAULT NULL,
  `demographic_no` int(11) DEFAULT NULL,
  `timespan` int(11) DEFAULT NULL,
  `timeframe` varchar(25) DEFAULT NULL,
  `followup_provider` varchar(100) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
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
  `followup_no` smallint(5) DEFAULT NULL,
  `followup_unit` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `followup_doctor` varchar(6) COLLATE utf8_bin DEFAULT NULL,
  `followup_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tickler_staff` varchar(6) COLLATE utf8_bin DEFAULT NULL,
  `billing_visit_type` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_visit_location` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_codes` text COLLATE utf8_bin,
  `billing_dxcode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_total` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_comment` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `billing_billtype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_pay_method` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_billto` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_remitto` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_gstBilledTotal` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_payment` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_refund` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `billing_gst` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `test_records` text COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ocularprocedurehis`
--

DROP TABLE IF EXISTS `ocularprocedurehis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ocularprocedurehis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(11) NOT NULL DEFAULT '0',
  `provider` varchar(60) DEFAULT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  `eye` varchar(2) NOT NULL DEFAULT '',
  `procedure_name` varchar(100) NOT NULL DEFAULT '',
  `procedure_type` varchar(30) DEFAULT NULL,
  `procedure_note` text,
  `doctor` varchar(30) DEFAULT NULL,
  `location` varchar(30) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` varchar(2) DEFAULT 'A',
  `appointment_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `procedurebook`
--

DROP TABLE IF EXISTS `procedurebook`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procedurebook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eyeform_id` bigint(20) DEFAULT NULL,
  `demographic_no` int(11) DEFAULT NULL,
  `appointment_no` int(11) DEFAULT NULL,
  `provider` varchar(60) DEFAULT NULL,
  `procedure_name` varchar(30) DEFAULT NULL,
  `eye` varchar(20) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `urgency` varchar(10) DEFAULT NULL,
  `comment` text,
  `date` datetime DEFAULT NULL,
  `status` varchar(2) DEFAULT 'A',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `specshis`
--

DROP TABLE IF EXISTS `specshis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `specshis` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(11) NOT NULL DEFAULT '0',
  `provider` varchar(60) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `doctor` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `od_sph` varchar(10) DEFAULT NULL,
  `od_cyl` varchar(10) DEFAULT NULL,
  `od_axis` varchar(10) DEFAULT NULL,
  `od_add` varchar(10) DEFAULT NULL,
  `od_prism` varchar(10) DEFAULT NULL,
  `os_sph` varchar(10) DEFAULT NULL,
  `os_cyl` varchar(10) DEFAULT NULL,
  `os_axis` varchar(10) DEFAULT NULL,
  `os_add` varchar(10) DEFAULT NULL,
  `os_prism` varchar(10) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` varchar(2) DEFAULT 'A',
  `appointment_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `testbookrecord`
--

DROP TABLE IF EXISTS `testbookrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `testbookrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `testname` varchar(60) DEFAULT NULL,
  `appointment_no` int(11) DEFAULT NULL,
  `demographic_no` int(11) DEFAULT NULL,
  `provider` varchar(60) DEFAULT NULL,
  `eyeform_id` bigint(20) DEFAULT NULL,
  `eye` varchar(20) DEFAULT NULL,
  `urgency` varchar(30) DEFAULT NULL,
  `comment` varchar(100) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `status` varchar(2) DEFAULT 'A',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

