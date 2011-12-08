CREATE TABLE `SentToPHRTracking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `demographicNo` int(10) NOT NULL,
  `objectName` varchar(60) NOT NULL,
  `lastObjectId` int(10) NOT NULL,
  `sentDatetime` datetime NOT NULL,
  `sentToServer` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
);
