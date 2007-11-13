-- DROP TABLE IF EXISTS `caisi_form_data_tmpsave`;
CREATE TABLE `caisi_form_data_tmpsave` (
  `tmp_form_data_id` bigint(20) NOT NULL auto_increment,
  `tmp_instance_id` bigint(20) default NULL,
  `page_number` bigint(20) default NULL,
  `section_id` bigint(20) default NULL,
  `question_id` bigint(20) default NULL,
  `value` text default NULL,
  `data_key` varchar(255) default NULL,
  PRIMARY KEY  (`tmp_form_data_id`),
  KEY `caisi_form_data_tmpsave_key1` (`tmp_instance_id`)
--  KEY `FKC253B2E74497F4F` (`instance_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `caisi_form_instance`
--

-- DROP TABLE IF EXISTS `caisi_form_instance_tmpsave`;
CREATE TABLE `caisi_form_instance_tmpsave` (
  `tmp_instance_id` bigint(20) NOT NULL auto_increment,
  `instance_id` bigint(20) NOT NULL,
  `form_id` bigint(20) default NULL,
  `description` varchar(255) default NULL,
  `date_created` datetime default NULL,
  `user_id` bigint(20) default NULL,
  `username` varchar(255) default NULL,
  `client_id` bigint(20) default NULL,
  PRIMARY KEY  (`tmp_instance_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
