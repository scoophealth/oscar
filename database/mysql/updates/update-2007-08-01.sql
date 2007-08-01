

DROP TABLE IF EXISTS `waitingList`;
CREATE TABLE `waitingList` (
  `id` bigint(20) NOT NULL auto_increment,
  `listID` int(11) default NULL,
  `demographic_no` int(10) NOT NULL default '0',
  `note` varchar(255) default NULL,
  `position` bigint(20) NOT NULL default '0',
  `onListSince` datetime NOT NULL default '0000-00-00 00:00:00',
  `is_history` char(1) default NULL,
  PRIMARY KEY  (`id`)
);

DROP TABLE IF EXISTS `waitingListName`;
CREATE TABLE `waitingListName` (
  `ID` bigint(11) NOT NULL auto_increment,
  `name` varchar(80) NOT NULL default '',
  `group_no` varchar(10) default '',
  `provider_no` varchar(6) default '',
  `create_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `is_history` char(1) default 'N',
  PRIMARY KEY  (`ID`)
);

