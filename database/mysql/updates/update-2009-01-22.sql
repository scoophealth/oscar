ALTER TABLE `allergies` ADD `start_date` date DEFAULT NULL AFTER `archived`;

ALTER TABLE `document` ADD `reviewer` varchar(30) DEFAULT '';
ALTER TABLE `document` ADD `reviewdatetime` datetime DEFAULT NULL;

CREATE TABLE `casemgmt_note_link` (
  `id` int(10) NOT NULL auto_increment,
  `table_name` int(6) NOT NULL,
  `table_id` int(10) NOT NULL,
  `note_id` int(10) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE `casemgmt_note_ext` (
  `id` int(10) NOT NULL auto_increment,
  `note_id` int(10) NOT NULL,
  `key_val` varchar(64) NOT NULL,
  `value` text,
  `date_value` date,
  PRIMARY KEY (`id`)
);

