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

-- program restriction based on gender
drop table if exists `program_client_restriction`;
create table `program_client_restriction` (
    `id` bigint(22) NOT NULL auto_increment,
    `program_id` int(10) NOT NULL,
    `demographic_no` int(10) NOT NULL,
    `provider_no` varchar(6) NOT NULL,
    `comments` varchar(255) default null,
    `is_enabled` tinyint(1) NOT NULL default TRUE,
    `start_date` datetime not null,
    `end_date` datetime not null,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_pcr_program` FOREIGN KEY (`program_id`) REFERENCES `program` (`id`),
    CONSTRAINT `FK_pcr_provider` FOREIGN KEY (`provider_no`) REFERENCES `provider` (`provider_no`),
    CONSTRAINT `FK_pcr_demographic` FOREIGN KEY (`demographic_no`) REFERENCES `demographic` (`demographic_no`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

insert into access_type (name, type) values ('Service restriction override on referral','access'),('Service restriction override on admission','access');

insert into secObjectName values ('_merge');