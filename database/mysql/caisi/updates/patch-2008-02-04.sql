CREATE TABLE `facility_message` (
  `id` int(10) NOT NULL auto_increment,
  `message` text NOT NULL,
  `creation_date` datetime NOT NULL default '1900-01-01 00:00:00',
  `expiry_date` datetime NOT NULL default '1900-01-01 00:00:00',
  `facility_id` int,
  `facility_name` varchar(32),
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM;


