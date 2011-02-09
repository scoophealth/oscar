
CREATE TABLE `site` (
  `site_id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `short_name` varchar(10) NOT NULL default '',
  `phone` varchar(50) default '',
  `fax` varchar(50) default '',
  `bg_color` varchar(20) NOT NULL default '',
  `address` varchar(255) default '',
  `city` varchar(25) default '',
  `province` varchar(25) default '',
  `postal` varchar(10) default '',
  `status` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`site_id`),
  UNIQUE KEY `unique_name` (`name`),
  UNIQUE KEY `unique_shortname` (`short_name`)
) TYPE=MyISAM;


CREATE TABLE `providersite` (
  `provider_no` varchar(6) NOT NULL,
  `site_id` int(11) NOT NULL,
  PRIMARY KEY  (`provider_no`,`site_id`)
) TYPE=MyISAM;

-- add a clinic column to store the site when billing was made
ALTER TABLE `billing_on_cheader1` ADD `clinic` VARCHAR(30) NULL AFTER `timestamp1` ;

-- enlarge the column size to fix a bug that results from cutoff due to lengthy content --
ALTER TABLE `rschedule` CHANGE `avail_hour` `avail_hour` TEXT NULL  ;


INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`) VALUES ('_team_schedule_only', 'Restrict schedule to only login provider and his team', '0');
INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`) VALUES ('_team_billing_only', 'Restrict billing access to only login provider and his team', '0');

-- add index to improve performance
ALTER TABLE `appointment` ADD INDEX `location` (`location`) ;
ALTER TABLE `billing_on_cheader1` ADD INDEX `clinic` (`clinic`) ;
