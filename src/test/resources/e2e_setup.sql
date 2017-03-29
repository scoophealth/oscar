-- Header
TRUNCATE TABLE `clinic`;
INSERT INTO `clinic` VALUES (123456,'McMaster Hospital','Hamilton','Hamilton','L0R 4K3','555-555-5555','555-555-5555','444','A','Ontario','','');
delete from `demographic`;
INSERT INTO `demographic` VALUES (1,'MR','CLEESE','JOHN','1234 Street','city','BC','A1B 2C3','250-000-0001','250-000-0002','test@test.com',NULL,'1940','09','25','448000001','','',NULL,NULL,'','AC','2013-09-25','2013-09-25','','English','','999998','M',NULL,NULL,NULL,'BC',NULL,'<rdohip></rdohip><rd></rd>',NULL,NULL,NULL,NULL,NULL,'','-1','Unknown',NULL,'999998','2013-09-26');
INSERT INTO `demographic` VALUES (2,'MR','SUBJECT','MISSING',NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,'1970','09','25','448000002','','',NULL,NULL,'','AC','2013-09-25','2013-09-25','','English','','999998','M',NULL,NULL,NULL,'BC',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','-1','Unknown',NULL,'999998','2013-09-26');
delete from `provider`;
INSERT INTO `provider` VALUES ('999998','oscardoc','doctor','doctor',null,'','','','0001-01-01','','250-999-9998','250-999-9999','ohip','','billing','','1','','','cpsid','','','test2@test2.com','','','0001-01-01 00:00:00','0001-01-01 00:00:00');

-- Allergies
TRUNCATE TABLE `allergies`;
INSERT INTO `allergies` VALUES (1,1,'2013-09-26','PENICILLINS, COMBINATIONS WITH OTHER ANTIBACTERIAL',NULL,NULL,NULL,NULL,8,'reaction','43507',0,'1935-01-01',NULL,'4','4',NULL,'C',0,'2013-03-05 13:30:47',NULL);

-- Clinically Measured Observations
TRUNCATE TABLE `measurements`;
INSERT INTO `measurements` VALUES (1,'BP',1,'999998','130/85','sitting position','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);
INSERT INTO `measurements` VALUES (2,'HT',1,'999998','187','in cm','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);
INSERT INTO `measurements` VALUES (3,'HR',1,'999998','85','in bpm (nnn) Range:40-180','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);
INSERT INTO `measurements` VALUES (4,'TEMP',1,'999998','37','degrees celcius','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);
INSERT INTO `measurements` VALUES (5,'WAIS',1,'999998','92','Waist Circum in cm','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);
INSERT INTO `measurements` VALUES (6,'WT',1,'999998','95','in kg','','2013-09-25 00:00:00','2013-09-25 15:51:13',0);

-- Encounters
TRUNCATE TABLE `casemgmt_note`;
INSERT INTO `casemgmt_note` VALUES (1,'2013-09-25 15:50:33','2013-09-25 15:50:33',1,'999998','[25-Sep-2013 .: Tel-Progress Notes]',0,0,'','','','10016','1','0','[25-Sep-2013 .: Tel-Progress Notes]',NULL,'0',0,0,'2da90304-4809-4777-a6d7-c8eb0fcc3698',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (2,'2013-09-25 15:51:23','2013-09-25 15:50:00',1,'999998','[25-Sep-2013 .: Tel-Progress Notes]\nBP    130/85 sitting position \nHT    187 in cm \nHR    85 in bpm (nnn) Range:40-180 \nTEMP    37 degrees celcius \nWAIS    92 Waist Circum in cm \nWT    95 in kg',0,0,'','','','10016','1','0','[25-Sep-2013 .: Tel-Progress Notes]\nBP    130/85 sitting position \nHT    187 in cm \nHR    85 in bpm (nnn) Range:40-180 \nTEMP    37 degrees celcius \nWAIS    92 Waist Circum in cm \nWT    95 in kg\n   ----------------History Record----------------   \n[25-Sep-2013 .: Tel-Progress Notes]\n',NULL,'0',0,0,'2da90304-4809-4777-a6d7-c8eb0fcc3698',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (6,'2013-09-26 16:18:23','2013-09-26 16:18:23',1,'999998','Situational Crisis',1,1,'999998','','','10016','1','0','Situational Crisis',NULL,'0',1,0,'481d5e06-5854-4a04-8ae3-6be35f0b7176',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (7,'2013-09-26 16:19:01','2013-09-26 16:19:01',1,'999998','Family History Crisis',1,1,'999998','','','10016','1','0','Vitamin D3',NULL,'0',0,2,'604ee129-a4e0-4efc-b508-98c9911cde2f',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (8,'2013-09-26 16:19:59','2013-09-26 16:19:59',1,'999998','Vitamin C',1,1,'999998','','','10016','1','0','Vitamin C',NULL,'0',0,1,'bc185582-c00b-4fd8-ad2a-3918e2274110',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (9,'2013-09-26 16:20:10','2013-09-26 16:20:10',1,'999998','Ginseng Tincture',1,1,'999998','','','10016','1','0','Ginseng Tincture',NULL,'0',0,0,'20c8f109-a40b-4e4f-a222-9464d2d2cfff',0,NULL,NULL,NULL,NULL);
INSERT INTO `casemgmt_note` VALUES (10,'2013-09-26 16:20:35','2013-09-26 16:20:35',1,'999998','Heart Attack',1,1,'999998','','','10016','1','0','Heart Attack',NULL,'0',1,0,'de752b1b-9eb5-451d-870b-6e2a59f8d055',0,NULL,NULL,NULL,NULL);

-- Alerts, Family History, Risk Factors
TRUNCATE TABLE `casemgmt_issue`;
INSERT INTO `casemgmt_issue` VALUES (1,'1',66,0,0,0,0,10016,'nurse','2013-09-26 16:18:23');
INSERT INTO `casemgmt_issue` VALUES (2,'1',70,0,0,0,0,10016,'nurse','2013-09-26 16:19:01');
INSERT INTO `casemgmt_issue` VALUES (3,'1',69,0,0,0,0,10016,'nurse','2013-09-26 16:20:35');
TRUNCATE TABLE `casemgmt_issue_notes`;
INSERT INTO `casemgmt_issue_notes` VALUES (1,6);
INSERT INTO `casemgmt_issue_notes` VALUES (2,7);
INSERT INTO `casemgmt_issue_notes` VALUES (3,10);
TRUNCATE TABLE `casemgmt_note_ext`;
INSERT INTO `casemgmt_note_ext` VALUES (1,7,'Age at Onset','45',NULL);
INSERT INTO `casemgmt_note_ext` VALUES (2,7,'Hide Cpp','0',NULL);
INSERT INTO `casemgmt_note_ext` VALUES (3,7,'Life Stage','A',NULL);
INSERT INTO `casemgmt_note_ext` VALUES (4,7,'Relationship','Father',NULL);
INSERT INTO `casemgmt_note_ext` VALUES (5,7,'Treatment','Test Treatment',NULL);

-- Immunizations
TRUNCATE TABLE `preventions`;
INSERT INTO `preventions` VALUES (1,1,'2013-09-27 14:01:22','2012-09-01','999998',NULL,'Td','0','0','2015-06-10','0',999998,'2013-09-27 14:01:22',0,NULL);
INSERT INTO `preventions` VALUES (2,1,'2013-09-27 14:01:44','2009-02-01','999998',NULL,'Flu','0','0','2014-02-01','0',999998,'2013-09-27 14:01:44',0,NULL);
INSERT INTO `preventions` VALUES (3,1,'2013-09-27 14:02:19','2012-10-31','999998',NULL,'Pneumovax','0','0','2013-10-31','0',999998,'2013-09-27 14:02:19',0,NULL);
TRUNCATE TABLE `preventionsExt`;
INSERT INTO `preventionsExt` VALUES (1,1,'location','clinic'),(2,1,'lot','1234'),(3,1,'route','left deltoid'),(4,1,'dose',''),(5,1,'comments','comment'),(6,1,'neverReason','test'),(7,1,'manufacture',''),(8,1,'name','');
INSERT INTO `preventionsExt` VALUES (9,2,'location',''),(10,2,'lot',''),(11,2,'route',''),(12,2,'dose',''),(13,2,'comments',''),(14,2,'neverReason','allergic'),(15,2,'manufacture',''),(16,2,'name','');
INSERT INTO `preventionsExt` VALUES (17,3,'location','hospital'),(18,3,'lot',''),(19,3,'route','right deltoid'),(20,3,'dose',''),(21,3,'comments',''),(22,3,'neverReason',''),(23,3,'manufacture',''),(24,3,'name','');

-- Labs
TRUNCATE TABLE `hl7TextInfo`;
INSERT INTO `hl7TextInfo` VALUES (9,9,'M','9055555555','A',128,'2013-06-27 12:13:29',NULL,'BOB MDCARE','HAEM1/HAEM3/CHEM4/CHEM29/REFER1','EXCELLERIS','APATIENT','F','13-999955528',NULL,NULL,NULL);
INSERT INTO `measurements` VALUES (26,'',1,'0','158','','','2013-05-31 10:20:12','2013-09-26 03:56:15',0);
INSERT INTO `measurements` VALUES (31,'',1,'0','12.6','','','2013-05-31 10:20:12','2013-09-26 03:56:15',0);
INSERT INTO `measurements` VALUES (40,'SCR',1,'0','68','in umol/L','','2013-05-31 10:20:12','2013-09-26 03:56:15',0);
INSERT INTO `measurements` VALUES (41,'EGFR',1,'0','113','in ml/min','','2013-05-31 10:20:12','2013-09-26 03:56:15',0);
TRUNCATE TABLE `measurementsExt`;
INSERT INTO `measurementsExt` VALUES (172,26,'lab_no','9'),(173,26,'abnormal','N'),(174,26,'identifier','718-7'),(175,26,'name','Hemoglobin'),(176,26,'labname','LIFELABS'),(177,26,'accession','13-999955528'),(178,26,'request_datetime','2013-05-27 13:40:00'),(179,26,'datetime','2013-05-31 10:20:12'),(180,26,'olis_status','F'),(181,26,'unit','g/L'),(182,26,'minimum','133'),(183,26,'other_id','0-0'),(184,26,'comments','result notes');
INSERT INTO `measurementsExt` VALUES (231,31,'lab_no','9'),(232,31,'abnormal','N'),(233,31,'identifier','788-0'),(234,31,'name','RDW'),(235,31,'labname','LIFELABS'),(236,31,'accession','13-999955528'),(237,31,'request_datetime','2013-05-27 13:40:00'),(238,31,'datetime','2013-05-31 10:20:12'),(239,31,'olis_status','F'),(240,31,'unit','%'),(241,31,'minimum','11.5'),(242,31,'other_id','0-1');
INSERT INTO `measurementsExt` VALUES (338,40,'lab_no','9'),(339,40,'abnormal','A'),(340,40,'identifier','14682-9'),(341,40,'name','Creatinine'),(342,40,'labname','LIFELABS'),(343,40,'accession','13-999955528'),(344,40,'request_datetime','2013-05-27 13:40:00'),(345,40,'datetime','2013-05-31 10:20:12'),(346,40,'olis_status','F'),(347,40,'unit','umol/L'),(348,40,'minimum','70'),(349,40,'other_id','1-0');
INSERT INTO `measurementsExt` VALUES (350,41,'lab_no','9'),(351,41,'abnormal','N'),(352,41,'identifier','33914-3'),(353,41,'name','Estimated GFR'),(354,41,'labname','LIFELABS'),(355,41,'accession','13-999955528'),(356,41,'request_datetime','2013-05-27 13:40:00'),(357,41,'datetime','2013-05-31 10:20:12'),(358,41,'olis_status','F'),(359,41,'unit','mL/min'),(360,41,'range','>=60');
TRUNCATE TABLE `patientLabRouting`;
INSERT INTO `patientLabRouting` VALUES (1,9,'HL7',27,'2013-09-26 00:00:00',NULL);

-- Medications
TRUNCATE TABLE `drugs`;
INSERT INTO `drugs` VALUES (1,'999998',1,'2013-09-27','2013-11-22','2013-09-27',NULL,'AVA-RAMIPRIL 5MG',6227,NULL,1,1,'OD','28','D','28','',1,NULL,0,0,'AVA-RAMIPRIL 5MG\nTake 1 PO OD 28 days\nQty:28 Repeats:1',NULL,0,'RAMIPRIL','C09AA05',1,'02363283','MG','Take','PO','TABLET','2013-09-27 12:51:23','5.0 MG',0,NULL,0,1,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,1,NULL,0,'2013-09-27 12:51:23',0);
INSERT INTO `drugs` VALUES (2,'999998',1,'2013-09-27','2013-11-22','2013-09-27',NULL,'SPIRONOLACTONE 25MG TABLET',63449,NULL,1,1,'QAM','28','','D','28',1,NULL,0,0,'SPIRONOLACTONE 25MG TABLET\nTake 1 PO QAM 28 days\nQty:28 Repeats:1',NULL,0,'SPIRONOLACTONE','C03DA01',1,'00613215','MG','Take','PO','TABLET','2013-09-27 12:51:23','25.0 MG',0,NULL,0,1,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,2,NULL,0,'2013-09-27 12:51:23',0);
INSERT INTO `drugs` VALUES (3,'999998',1,'2014-09-27','2014-11-22','2014-09-27',NULL,'AVA-RAMIPRIL 5MG',6227,NULL,1,1,'OD','28','D','28','',1,NULL,0,0,'AVA-RAMIPRIL 5MG\nTake 1 PO OD 28 days\nQty:28 Repeats:1',NULL,0,'RAMIPRIL','C09AA05',1,'02363283','MG','Take','PO','TABLET','2014-09-27 12:51:23','5.0 MG',0,NULL,0,1,0,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,0,1,NULL,0,'2014-09-27 12:51:23',0);

-- Problem List
TRUNCATE TABLE `dxresearch`;
INSERT INTO `dxresearch` VALUES (1,1,'2013-09-26','2013-09-26','A','428','icd9',0,'999998');
INSERT INTO `dxresearch` VALUES (2,1,'2013-09-26','2013-09-26','A','401','icd9',0,'999998');
