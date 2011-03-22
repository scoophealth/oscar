DROP TABLE IF EXISTS `eyeform_followup`;
DROP TABLE IF EXISTS `EyeformFollowUp`;
CREATE TABLE `EyeformFollowUp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `demographic_no` int(11) ,
  `timespan` int(11) ,
  `timeframe` varchar(25) ,
  `followup_provider` varchar(100) ,
  `date` timestamp ,
  `type` varchar(25),
  `urgency` varchar(50),
  `comment` text,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `procedurebook`;
DROP TABLE IF EXISTS `EyeformProcedureBook`;
CREATE TABLE `EyeformProcedureBook` (
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
);

DROP TABLE IF EXISTS `testbookrecord`;
DROP TABLE IF EXISTS `EyeformTestBook`;
CREATE TABLE `EyeformTestBook` (
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
);

DROP TABLE IF EXISTS `eyeform`;
DROP TABLE IF EXISTS `Eyeform`;
CREATE TABLE `Eyeform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_no` int(11)  ,
  `discharge` varchar(20),
  `stat` varchar(20),
  `opt` varchar(20),
  `date` timestamp ,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `ocularprocedurehis`;
DROP TABLE IF EXISTS `EyeformOcularProcedure`;
CREATE TABLE `EyeformOcularProcedure` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date NOT NULL ,
  `eye` varchar(2) NOT NULL ,
  `procedureName` varchar(100) NOT NULL ,
  `procedureType` varchar(30) ,
  `procedureNote` text,
  `doctor` varchar(30) ,
  `location` varchar(30) ,
  `updateTime` datetime ,
  `status` varchar(2) ,
  `appointmentNo` int(11) ,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `specshis`;
DROP TABLE IF EXISTS `EyeformSpecsHistory`;
CREATE TABLE `EyeformSpecsHistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(11) NOT NULL ,
  `provider` varchar(60) ,
  `date` date ,
  `doctor` varchar(30) ,
  `type` varchar(30) ,
  `odSph` varchar(10),
  `odCyl` varchar(10) ,
  `odAxis` varchar(10) ,
  `odAdd` varchar(10) ,
  `odPrism` varchar(10) ,
  `osSph` varchar(10) ,
  `osCyl` varchar(10) ,
  `osAxis` varchar(10) ,
  `osAdd` varchar(10) ,
  `osPrism` varchar(10) ,
  `updateTime` datetime ,
  `status` varchar(2) ,
  `appointmentNo` int(11) ,
  PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `eyeform_macro`;
DROP TABLE IF EXISTS `EyeformMacro`;
CREATE TABLE `EyeformMacro` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `displayOrder` tinyint(4) NOT NULL,
  `impression` text,
  `followupNo` smallint(5),
  `followupUnit` varchar(50),
  `followupDoctor` varchar(6),
  `followupReason` varchar(255) ,
  `ticklerStaff` varchar(6) ,
  `billingVisitType` varchar(50) ,
  `billingVisitLocation` varchar(50) ,
  `billingCodes` text,
  `billingDxcode` varchar(50),
  `billingTotal` varchar(50) ,
  `billingComment` varchar(255),
  `billingBilltype` varchar(50)  ,
  `billingPayMethod` varchar(50) ,
  `billingBillto` varchar(50) ,
  `billingRemitto` varchar(50) ,
  `billingGstBilledTotal` varchar(50)  ,
  `billingPayment` varchar(50),
  `billingRefund` varchar(50),
  `billingGst` varchar(50),
  `testRecords` text,
  `dischargeFlag` varchar(20),
  `statFlag` varchar(20),
  `optFlag` varchar(20),
  PRIMARY KEY (`id`)
);

