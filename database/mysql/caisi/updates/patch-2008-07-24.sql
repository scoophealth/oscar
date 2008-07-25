DROP TABLE IF EXISTS `oncall_questionnaire`;
CREATE TABLE `oncall_questionnaire` (
	`id` int(10) unsigned NOT NULL auto_increment,
	`providerNo` varchar(40) NOT NULL,
	`type` varchar(250) NOT NULL,
	`health_type` varchar(50) NOT NULL,
	`nurse_involved` varchar(50) NOT NULL,
	`course_of_action` varchar(50) NOT NULL,
	`physician_consultation_reqd` varchar(50) NOT NULL,
	`call_time` datetime NOT NULL,
	PRIMARY KEY  (`id`)
);
