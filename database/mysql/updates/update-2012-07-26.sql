CREATE TABLE `eyeform_macro_def` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `macroName` varchar(255),
  `lastUpdated` datetime,
  `copyFromLastImpression` tinyint(1),
  `impressionText` text,
  `planText` text,
  PRIMARY KEY (`id`)
);

CREATE TABLE `eyeform_macro_billing` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `macroId` int(11),
  `billingServiceCode` varchar(50),
  `multiplier` double,
  PRIMARY KEY (`id`)
);

INSERT INTO `issue` (`code`, `description`, `role`, `update_date`, `priority`, `type`)
VALUES
	('eyeformFollowUp', 'Follow-Up Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformCurrentIssue', 'Current Presenting Issue Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformPlan', 'Plan Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformImpression', 'Impression History Item for Eyeform', 'nurse', NOW(), NULL, 'system'),
	('eyeformProblem', 'Problem List Item for Eyeform', 'nurse', NOW(), NULL, 'system');