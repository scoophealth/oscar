CREATE TABLE `dsGuidelines` (
  `id` int(11) NOT NULL auto_increment,
  `uuid` varchar(60) NOT NULL,
  `title` varchar(100) NOT NULL,
  `version` int(11) default NULL,
  `author` varchar(60) NOT NULL,
  `xml` text default NULL,
  `source` varchar(60) NOT NULL,
  `engine` varchar(60) NOT NULL,
  `dateStart` datetime DEFAULT NULL,
  `dateDecomissioned` datetime DEFAULT NULL,
  `status` varchar(1) DEFAULT 'A',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;

CREATE TABLE `dsGuidelineProviderMap` (
  `mapid` int(11) NOT NULL auto_increment,
  `provider_no` varchar(11) NOT NULL,
  `guideline_uuid` varchar(60) NOT NULL,
  PRIMARY KEY (`mapid`)
) ENGINE=MyISAM;