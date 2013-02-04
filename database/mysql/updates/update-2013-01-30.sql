CREATE TABLE `default_issue` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `assigned_time` datetime NOT NULL,
  `issue_ids` text,
  `provider_no` varchar(6) NOT NULL,
  PRIMARY KEY (`id`)
);