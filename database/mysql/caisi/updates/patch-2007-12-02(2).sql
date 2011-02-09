
alter table  room  add  assigned_bed  tinyint(1)  NOT NULL default '1';
alter table  room  add  occupancy  int(10) NULL default '0';

CREATE TABLE `room_demographic` (
  `room_id` int(10) unsigned NOT NULL default '0',
  `demographic_no` int(10) unsigned NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default '',
  `assign_start` date  NULL default '0000-00-00',
  `assign_end` date  NULL default '0000-00-00',
  `comment` varchar(50)  NULL default '',

  PRIMARY KEY  (`room_id`,`demographic_no`)
);


CREATE TABLE `room_bed` (
  `room_id` int(10) unsigned NOT NULL default '0',
  `bed_id` int(10) unsigned NOT NULL default '0',
  `assign_start` date NULL default '0000-00-00',
  `assign_end` date NULL default '0000-00-00',
  `comment` varchar(50)  NULL default '',

  PRIMARY KEY  (`room_id`,`bed_id`)
);


