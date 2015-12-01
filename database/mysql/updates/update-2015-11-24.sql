CREATE TABLE `ResourceStorage` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `resourceType` varchar(100),
  `resourceName` varchar(100),
  `uuid` varchar(40),
  `fileContents` mediumblob,
  `uploadDate` datetime,
  `active` tinyint(1),
  PRIMARY KEY (`id`),
  KEY `ResourceStorage_resourceType_active` (`resourceType`(10),`active`)
);