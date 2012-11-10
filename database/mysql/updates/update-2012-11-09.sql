ALTER TABLE vacancy ADD vacancyName VARCHAR(255) NOT NULL AFTER id;

CREATE TABLE `vacancy_client_match` (
  `match_id` int NOT NULL AUTO_INCREMENT,
  `vacancy_id` int(11),
  `client_id` int(11),
  `contact_attempts` int(11),
  `last_contact_date` datetime,
  `status` varchar(30),
  `rejection_reason` text,
  `form_id` int(10),
  `match_percent` double,
  PRIMARY KEY (`match_id`),
 UNIQUE KEY `vacancy_id` (`vacancy_id`,`client_id`,`form_id`)
) ;
