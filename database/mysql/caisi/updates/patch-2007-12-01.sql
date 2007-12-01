-- Table to join dependents to clients

CREATE TABLE `joint_admissions` (
  `id` bigint(11) NOT NULL auto_increment,
  `client_id` bigint(11) NOT NULL default '0',
  `type_id` bigint(3) NOT NULL default '0',
  `provider_no` varchar(6) NOT NULL default '0',
  `admission_date` datetime default NULL,
  `head_client_id` bigint(11) NOT NULL default '0',
  `archived` tinyint(1) default '0',
  `archiving_provider_no` varchar(6) default NULL,
  PRIMARY KEY  (`id`),
  KEY `client_id` (`client_id`),
  KEY `head_client_id` (`head_client_id`)
);

ALTER TABLE `program_client_restriction` ADD COLUMN provider_no varchar(6) NOT NULL;
ALTER TABLE `program_client_restriction` ADD CONSTRAINT `FK_pcr_provider` FOREIGN KEY (`provider_no`) REFERENCES `provider` (`provider_no`);
ALTER TABLE `program_client_restriction` DROP KEY idx_program_gender_restr;
