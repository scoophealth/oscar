CREATE TABLE `vacancy_template` (
  `TEMPLATE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROGRAM_ID` int(11) NOT NULL,
  `NAME` varchar(32) NOT NULL,
  `ACTIVE` tinyint(1) NOT NULL,
  PRIMARY KEY (`TEMPLATE_ID`)
);
