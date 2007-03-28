DROP TABLE IF EXISTS `intake_answer`;
DROP TABLE IF EXISTS `intake`;
DROP TABLE IF EXISTS `intake_node`;
DROP TABLE IF EXISTS `intake_answer_element`;
DROP TABLE IF EXISTS `intake_answer_validation`;
DROP TABLE IF EXISTS `intake_node_template`;
DROP TABLE IF EXISTS `intake_node_type`;
DROP TABLE IF EXISTS `intake_node_label`;

--
-- Table structure for table `intake_node_label`
--
CREATE TABLE `intake_node_label` (
  `intake_node_label_id` int(10) unsigned NOT NULL auto_increment,
  `lbl` text NOT NULL,
  PRIMARY KEY  (`intake_node_label_id`)
) ENGINE=InnoDB;


--
-- Table structure for table `intake_node_type`
--
CREATE TABLE `intake_node_type` (
  `intake_node_type_id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_node_type_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_node_template`
--
CREATE TABLE `intake_node_template` (
  `intake_node_template_id` int(10) unsigned NOT NULL auto_increment,
  `remote_intake_node_template_id` int(10) unsigned default NULL,
  `intake_node_type_id` int(10) unsigned NOT NULL,
  `intake_node_label_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`intake_node_template_id`),
  KEY `IDX_intake_node_template_intake_node_type` (`intake_node_type_id`),
  KEY `IDX_intake_node_template_intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_template_intake_node_label` FOREIGN KEY (`intake_node_label_id`) REFERENCES `intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_template_intake_node_type` FOREIGN KEY (`intake_node_type_id`) REFERENCES `intake_node_type` (`intake_node_type_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_answer_validation`
--
CREATE TABLE `intake_answer_validation` (
  `intake_answer_validation_id` int(10) unsigned NOT NULL auto_increment,
  `type` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_answer_validation_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_answer_element`
--
CREATE TABLE `intake_answer_element` (
  `intake_answer_element_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_template_id` int(10) unsigned NOT NULL,
  `intake_answer_validation_id` int(10) unsigned default NULL,
  `dflt` tinyint(1) default '0',
  `element` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_answer_element_id`),
  KEY `IDX_intake_answer_element_intake_node_template` (`intake_node_template_id`),
  KEY `IDX_intake_answer_element_intake_answer_validation` (`intake_answer_validation_id`),
  CONSTRAINT `FK_intake_answer_element_intake_node_template` FOREIGN KEY (`intake_node_template_id`) REFERENCES `intake_node_template` (`intake_node_template_id`),
  CONSTRAINT `FK_intake_answer_element_intake_answer_validation` FOREIGN KEY (`intake_answer_validation_id`) REFERENCES `intake_answer_validation` (`intake_answer_validation_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_node`
--
CREATE TABLE `intake_node` (
  `intake_node_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_template_id` int(10) unsigned NOT NULL,
  `intake_node_label_id` int(10) unsigned default NULL,
  `pos` int(10) unsigned default '0',
  `parent_intake_node_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`intake_node_id`),
  KEY `IDX_intake_node_intake_node_template` (`intake_node_template_id`),
  KEY `IDX_intake_node_intake_node_label` (`intake_node_label_id`),
  KEY `IDX_intake_node_intake_node` (`parent_intake_node_id`),
  CONSTRAINT `FK_intake_node_intake_node_template` FOREIGN KEY (`intake_node_template_id`) REFERENCES `intake_node_template` (`intake_node_template_id`),
  CONSTRAINT `FK_intake_node_intake_node_label` FOREIGN KEY (`intake_node_label_id`) REFERENCES `intake_node_label` (`intake_node_label_id`),
  CONSTRAINT `FK_intake_node_intake_node` FOREIGN KEY (`parent_intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake`
--
CREATE TABLE `intake` (
  `intake_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_id` int(10) unsigned NOT NULL,
  `client_id` int(10) unsigned NOT NULL,
  `staff_id` varchar(6) NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY  (`intake_id`),
  KEY `IDX_intake_intake_node` (`intake_node_id`),
  KEY `IDX_intake_client_creation_date` (`client_id`,`creation_date`),
  CONSTRAINT `FK_intake_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_answer`
--
CREATE TABLE `intake_answer` (
  `intake_answer_id` int(10) unsigned NOT NULL auto_increment,
  `intake_id` int(10) unsigned NOT NULL,
  `intake_node_id` int(10) unsigned NOT NULL,
  `val` text NOT NULL,
  PRIMARY KEY  (`intake_answer_id`),
  KEY `IDX_intake_answer_intake` (`intake_id`),
  KEY `IDX_intake_answer_intake_node` (`intake_node_id`),
  CONSTRAINT `FK_intake_answer_intake` FOREIGN KEY (`intake_id`) REFERENCES `intake` (`intake_id`),
  CONSTRAINT `FK_intake_answer_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Populate intake node labels
--
INSERT INTO `intake_node_label` VALUES
	(1, ''),
	(2, 'Registration Intake'),
	(3, 'Follow-up Intake'), 
	(4, 'Page'),
	(5, 'Section'),
	(6, 'Question'),
	(7, 'Compound Answer'),
	(8, 'Boolean'),
	(9, 'String'),
	(10, 'Date'),
	(11, 'Integer'),
	(12, 'Email'),
	(13, 'Phone'),
	(14, 'Note'),
	(15, 'Program Intake');
		
--
-- Populate intake node types
--
INSERT INTO `intake_node_type` VALUES
	(1, 'intake'),
	(2, 'page'),
	(3, 'section'),
	(4, 'question'),
	(5, 'answer compound'),
	(6, 'answer scalar choice'),
	(7, 'answer scalar text'),
	(8, 'answer scalar note');

--
-- Populate intake node templates
--
INSERT INTO `intake_node_template` VALUES
	(1, 1, 1, 2),
	(2, 2, 1, 3),
	(3, 3, 2, 4),
	(4, 4, 3, 5),
	(5, 5, 4, 6),
	(6, 6, 5, 7),
	(7, 7, 6, 8),
	(8, 8, 7, 9),
	(9, 9, 7, 10),
	(10, 10, 7, 11),
	(11, 11, 7, 12),
	(12, 12, 7, 13),
	(13, 13, 8, 14),
	(14, 14, 1, 15);

--
-- Populate intake answer validation
--
INSERT INTO `intake_answer_validation` VALUES
	(1, 'date'),
	(2, 'integer'),
	(3, 'email'),
	(4, 'phone');

--
-- Populate intake answer element
--
INSERT INTO `intake_answer_element` VALUES
	(1, 7, NULL, 0, 'T'),
	(2, 7, NULL, 1, 'F'),
	(3, 8, NULL, 0, ''),
	(4, 9, 1, 0, ''),
	(5, 10, 2, 0, ''),
	(6, 11, 3, 0, ''),
	(7, 12, 4, 0, ''),
	(8, 13, NULL, 0, '');

--
-- Populate intake node
--
INSERT INTO `intake_node` VALUES
	(1, 1, NULL, 0, NULL),
	(2, 2, NULL, 0, NULL);

--
-- Alter agency table structure
--
ALTER TABLE `agency`
	ADD COLUMN `intakes_combined` boolean NOT NULL DEFAULT 0 AFTER `id`,
	ADD COLUMN `intake_quick` INTEGER UNSIGNED NOT NULL DEFAULT 1 AFTER `intakes_combined`,
	ADD COLUMN `intake_indepth` INTEGER UNSIGNED DEFAULT 2 AFTER `intake_quick`;

--
-- Alter program table structure
--
ALTER TABLE `program`
	ADD COLUMN `intake_program` INTEGER UNSIGNED AFTER `agency_id`;
