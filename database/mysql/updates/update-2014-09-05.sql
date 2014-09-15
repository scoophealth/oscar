DROP TABLE IF EXISTS `faxes`;
CREATE TABLE `faxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255),
  `faxline` varchar(11),
  `destination` varchar(11),
  `status` varchar(32),
  `document` text,
  `numPages` int(11),
  `stamp` datetime,
  `user` varchar(255),
  `jobId` int(11),
  `oscarUser` varchar(6),
  PRIMARY KEY (`id`),
  KEY `faxline` (`faxline`),
  KEY `faxstatus` (`status`)
); 

CREATE TABLE `fax_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255),
  `siteUser` varchar(255),
  `passwd` varchar(255),
  `faxUser` varchar(255),
  `faxPasswd` varchar(255),
  `queue` varchar(255),
  `active` tinyint(1),
  `faxNumber` varchar(10),
  PRIMARY KEY (`id`)
);

