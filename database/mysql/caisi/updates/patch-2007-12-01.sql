#Table to join dependents to clients

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