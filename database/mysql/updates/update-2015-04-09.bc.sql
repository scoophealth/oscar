-- ----------------------------
--  Table structure for `formBPMH`
-- ----------------------------
CREATE TABLE `formBPMH` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10) NOT NULL,
  `provider_no` int(10) NOT NULL,
  `formCreated` date,
  `formEdited` date,
  `familyDrName` varchar(55),
  `familyDrPhone` varchar(15),
  `familyDrFax` varchar(15),
  `note` varchar(255),
  `allergies` blob,
  `drugs` blob,
  PRIMARY KEY (`id`)
);


insert into encounterForm values ('BPMH', '../formBPMH.do?demographic_no=', 'formBPMH', 0);

