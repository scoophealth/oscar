CREATE TABLE IF NOT EXISTS `cr_cert` (
  `cert_id` varchar(37) NOT NULL default '',
  `user_specific` tinyint(1) default NULL,
  `static_ip` tinyint(1) default NULL,
  `unassigned` tinyint(1) default NULL,
  `ip` varchar(15) default NULL,
  `user_id` varchar(64) default NULL,
  `machine_id` varchar(37) default NULL,
  `verification_needed` tinyint(1) default NULL,
  `usage_times_before_reverify` int(11) default NULL,
  `policy_id` varchar(37) default NULL,
  `created_timestamp` datetime default NULL,
  `last_changed` datetime default NULL,
  `signature` bigint(20) default NULL,
  PRIMARY KEY  (`cert_id`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_machine` (
  `machine_id` varchar(37) NOT NULL default '',
  `ip` varchar(15) default NULL,
  `machine_name` varchar(255) default NULL,
  PRIMARY KEY  (`machine_id`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_policy` (
  `policy_id` varchar(37) NOT NULL default '',
  `static_ip` tinyint(1) default NULL,
  `ip` varchar(15) default NULL,
  `remote_access` tinyint(1) default NULL,
  `generate_super_certs` tinyint(1) default NULL,
  `administrate_policies` tinyint(1) default NULL,
  `administrate_questions` tinyint(1) default NULL,
  `remove_bans` tinyint(1) default NULL,
  `user_id` varchar(64) default NULL,
  `role_id` varchar(37) default NULL,
  `priority` int(11) default NULL,
  `usage_times_before_reverify` int(11) default NULL,
  `max_time_between_usage` int(11) default NULL,
  `expire_cookie` int(11) default NULL,
  `ip_filter` varchar(128) default NULL,
  `certs_max` int(11) default NULL,
  `certs_current` int(11) default NULL,
  `default_answer` varchar(16) default NULL,
  PRIMARY KEY  (`policy_id`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_securityquestion` (
  `question_id` varchar(37) NOT NULL default '',
  `user_id` varchar(128) default NULL,
  `question` varchar(255) default NULL,
  `answer` varchar(255) default NULL,
  PRIMARY KEY  (`question_id`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_user` (
  `user_id` varchar(64) NOT NULL default '',
  `password_digest` varchar(128) default NULL,
  `disabled` tinyint(1) default NULL,
  `lockedout` tinyint(1) default NULL,
  `password_expired` tinyint(1) default NULL,
  PRIMARY KEY  (`user_id`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_userrole` (
  `user_id` varchar(64) NOT NULL default '',
  `user_role` varchar(64) NOT NULL default '',
  PRIMARY KEY  (`user_id`,`user_role`)
) TYPE=MyISAM;

CREATE TABLE IF NOT EXISTS `cr_iprange` (
  `id` varchar(37) NOT NULL default '',
  `range` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO cr_policy (policy_id, static_ip, ip, remote_access, generate_super_certs, administrate_policies, administrate_questions, remove_bans, user_id, role_id, priority, usage_times_before_reverify, max_time_between_usage, expire_cookie, ip_filter, certs_max, certs_current, default_answer)
VALUES ('admin-policy', 0, null, 1, 1, 1, 1, 1, null, 'admin', 10, 10000000, 2592000, 315360000, null, 1000000, 1, null);

INSERT INTO cr_policy (policy_id, static_ip, ip, remote_access, generate_super_certs, administrate_policies, administrate_questions, remove_bans, user_id, role_id, priority, usage_times_before_reverify, max_time_between_usage, expire_cookie, ip_filter, certs_max, certs_current, default_answer)
VALUES ('doctor-policy', 0, null, 1, 0, 0, 0, 0, null, 'doctor', 10, 10000000, 2592000, 315360000, null, 1000000, 1, null);

INSERT INTO cr_policy (policy_id, static_ip, ip, remote_access, generate_super_certs, administrate_policies, administrate_questions, remove_bans, user_id, role_id, priority, usage_times_before_reverify, max_time_between_usage, expire_cookie, ip_filter, certs_max, certs_current, default_answer)
VALUES ('nurse-policy', 0, null, 1, 0, 0, 0, 0, null, 'nurse', 10, 10000000, 2592000, 315360000, null, 1000000, 1, null);

INSERT INTO cr_policy (policy_id, static_ip, ip, remote_access, generate_super_certs, administrate_policies, administrate_questions, remove_bans, user_id, role_id, priority, usage_times_before_reverify, max_time_between_usage, expire_cookie, ip_filter, certs_max, certs_current, default_answer)
VALUES ('remote-access-policy', 0, null, 1, 0, 0, 0, 0, null, 'remote_access', 10, 10000000, 2592000, 315360000, null, 1000000, 1, null);

INSERT INTO cr_policy (policy_id, static_ip, ip, remote_access, generate_super_certs, administrate_policies, administrate_questions, remove_bans, user_id, role_id, priority, usage_times_before_reverify, max_time_between_usage, expire_cookie, ip_filter, certs_max, certs_current, default_answer)
VALUES ('default', 0, null, 1, 0, 0, 0, 0, null, null, 10, 10000000, 2592000, 315360000, null, 1000000, 1, null);

INSERT INTO secRole (role_name) 
VALUES ('remote_access');
