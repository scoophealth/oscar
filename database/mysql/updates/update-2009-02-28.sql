CREATE TABLE `tickler_link` (
  `id` int(10) NOT NULL auto_increment,
  `table_name` char(3) NOT NULL,
  `table_id` int(10) NOT NULL,
  `tickler_no` int(10) NOT NULL,
  PRIMARY KEY  (`id`)
) ;