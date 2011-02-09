
ALTER TABLE `waitingList` 
    ADD COLUMN `id` bigint(20) NOT NULL auto_increment PRIMARY KEY FIRST,
    ADD COLUMN `is_history` char(1) DEFAULT NULL AFTER `onListSince`,
    MODIFY COLUMN `listID` int(11) DEFAULT NULL,
    MODIFY COLUMN `position` bigint(20) NOT NULL default 0;


ALTER TABLE `waitingListName` 
  MODIFY COLUMN `ID` bigint(11) NOT NULL auto_increment,
  MODIFY COLUMN `name` varchar(80) NOT NULL default '',
  ADD COLUMN `group_no` varchar(10) default '' AFTER `name`,
  ADD COLUMN `provider_no` varchar(6) default '' AFTER  `group_no`,
  ADD COLUMN `create_date` datetime NOT NULL default '0000-00-00 00:00:00' AFTER `provider_no`,
  ADD COLUMN `is_history` char(1) default 'N' AFTER `create_date`;

