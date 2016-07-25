CREATE TABLE `dashboard` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255),
  `description` varchar(255),
  `creator` varchar(11),
  `edited` datetime,
  `active` bit(1),
  `locked` bit(1),
  PRIMARY KEY (`id`)
);

CREATE TABLE `indicatorTemplate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dashboardId` int(11),
  `name` varchar(255),
  `category` varchar(255),
  `subCategory` varchar(255),
  `framework` varchar(255),
  `frameworkVersion` date,
  `definition` tinytext,
  `notes` tinytext,
  `template` mediumtext,
  `active` bit(1),
  `locked` bit(1),
  PRIMARY KEY (`id`)
);