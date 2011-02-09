
CREATE TABLE `oscar_annotations` (
  `id` int(10) NOT NULL auto_increment,
  `table_id` int(10) default NULL,
  `table_name` int(10) default NULL,
  `provider_no` varchar(6) default NULL,
  `demographic_no` int(10) default NULL,
  `create_date` datetime default NULL,
  `observation_date` datetime default NULL,
  `deleted` char(1) default '0',
  `note` text,
  `uuid` char(36) default NULL,
  PRIMARY KEY  (`id`)
);