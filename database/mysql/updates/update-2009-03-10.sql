CREATE TABLE `view` (
  `id` int(10) NOT NULL auto_increment,
  `view_name` varchar(255) NOT NULL default '',
  `name` varchar(255) NOT NULL default '',
  `value` text,
  `role` varchar(255) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
