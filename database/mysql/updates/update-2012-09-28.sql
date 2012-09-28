--
-- Table structure for table `formNoShowPolicy`
--
CREATE TABLE `formNoShowPolicy` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `provider_no` int(10),
  `demographic_no` int(10) NOT NULL,
  `formCreated` date,
  `formEdited` timestamp,
  `formVersion` varchar(10),
  PRIMARY KEY (`ID`)
);
