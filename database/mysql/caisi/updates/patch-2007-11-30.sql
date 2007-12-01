ALTER TABLE facility ADD COLUMN contact_name varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN contact_email varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN contact_phone varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN hic tinyint(1) NOT NULL default false;

create table `program_client_restriction` (
    `id` bigint(22) NOT NULL auto_increment,
    `program_id` int(10) NOT NULL,
    `demographic_no` int(10) NOT NULL,
    `comments` varchar(255) default null,
    `is_enabled` tinyint(1) NOT NULL default TRUE,
    `start_date` date not null,
    `end_date` date not null,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_program_gender_restr` USING HASH (`program_id`, `demographic_no`),
    CONSTRAINT `FK_pcr_program` FOREIGN KEY (`program_id`) REFERENCES `program` (`id`),
    CONSTRAINT `FK_pcr_demographic` FOREIGN KEY (`demographic_no`) REFERENCES `demographic` (`demographic_no`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
