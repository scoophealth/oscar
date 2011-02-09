DROP TABLE IF EXISTS `program_clientstatus`;
CREATE TABLE `program_clientstatus` (
  `clientstatus_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `program_id` bigint(20) default NULL,
  PRIMARY KEY  (`clientstatus_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


alter table admission add `clientstatus_id` bigint(20) DEFAULT 0;

alter table program add `bed_program_link_id` int(10) default 0;

