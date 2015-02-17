
CREATE TABLE `AppDefinition` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(255),
  `appType` varchar(255),
  `config` text,
  `active` tinyint(1),
  `addedBy` varchar(8),
  `added` datetime,
  PRIMARY KEY (`id`)
);


CREATE TABLE `AppUser` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(8),
  `appId` int(9),
  `authenticationData` text,
  `added` datetime,
  PRIMARY KEY (`id`)
);