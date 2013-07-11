-- Create demographicExtArchive table

CREATE TABLE `demographicExtArchive` (
  `id` int(10) NOT NULL auto_increment,
  `archiveId` bigint(20),
  `demographic_no` int(10),
  `provider_no` varchar(6),
  `key_val` varchar(64),
  `value` text,
  `date_time` datetime,
  `hidden` char(1),
  PRIMARY KEY  (`id`),
  INDEX (demographic_no)
) ;
