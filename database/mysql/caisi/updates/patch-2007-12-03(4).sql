CREATE TABLE `caisi_form_question` (
  `id` bigint(20) NOT NULL auto_increment,
  `page` bigint(20),
  `section` bigint(20),
  `question` bigint(20),
  `description` varchar(255) default NULL,
  `form_id` bigint(20),
  `form_question_id` bigint(20),
  `type` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

