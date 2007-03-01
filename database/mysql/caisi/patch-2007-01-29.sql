DROP TABLE IF EXISTS `intake_answer`;
DROP TABLE IF EXISTS `intake_instance`;
DROP TABLE IF EXISTS `intake_label`;
DROP TABLE IF EXISTS `intake_node`;
DROP TABLE IF EXISTS `intake_node_type`;

--
-- Table structure for table `intake_node_type`
--
CREATE TABLE `intake_node_type` (
  `intake_node_type_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`intake_node_type_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_node`
--
CREATE TABLE `intake_node` (
  `intake_node_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_type_id` int(10) unsigned NOT NULL,
  `pos` int(10) unsigned default 0,
  `parent_intake_node_id` int(10) unsigned default NULL,
  PRIMARY KEY  (`intake_node_id`),
  KEY `IDX_intake_node_intake_node_type` (`intake_node_type_id`),
  KEY `IDX_intake_node_intake_node` (`parent_intake_node_id`),
  CONSTRAINT `FK_intake_node_intake_node_type` FOREIGN KEY (`intake_node_type_id`) REFERENCES `intake_node_type` (`intake_node_type_id`),
  CONSTRAINT `FK_intake_node_intake_node` FOREIGN KEY (`parent_intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_label`
--
CREATE TABLE `intake_label` (
  `intake_label_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_id` int(10) unsigned NOT NULL,
  `pos` int(10) unsigned default 0,
  `lbl` text NOT NULL,
  PRIMARY KEY  (`intake_label_id`),
  KEY `IDX_intake_label_intake_node` (`intake_node_id`),
  CONSTRAINT `FK_intake_label_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_instance`
--
CREATE TABLE `intake_instance` (
  `intake_instance_id` int(10) unsigned NOT NULL auto_increment,
  `intake_node_id` int(10) unsigned NOT NULL,
  `client_id` int(10) unsigned NOT NULL,
  `staff_id` varchar(6) NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY  (`intake_instance_id`),
  KEY `IDX_intake_instance_intake_node` (`intake_node_id`),
  KEY `IDX_intake_instance_client_creation_date` (`client_id`,`creation_date`),
  CONSTRAINT `FK_intake_instance_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Table structure for table `intake_answer`
--
CREATE TABLE `intake_answer` (
  `intake_answer_id` int(10) unsigned NOT NULL auto_increment,
  `intake_instance_id` int(10) unsigned NOT NULL,
  `intake_node_id` int(10) unsigned NOT NULL,
  `val` text NOT NULL,
  PRIMARY KEY  (`intake_answer_id`),
  KEY `IDX_intake_answer_intake_instance` (`intake_instance_id`),
  KEY `IDX_intake_answer_intake_node` (`intake_node_id`),
  CONSTRAINT `FK_intake_answer_intake_instance` FOREIGN KEY (`intake_instance_id`) REFERENCES `intake_instance` (`intake_instance_id`),
  CONSTRAINT `FK_intake_answer_intake_node` FOREIGN KEY (`intake_node_id`) REFERENCES `intake_node` (`intake_node_id`)
) ENGINE=InnoDB;

--
-- Populate intake node types
--
INSERT INTO `intake_node_type` VALUES
	(1, 'quick intake'),
	(2, 'in-depth intake'),
	(3, 'program intake'),
	(4, 'page'),
	(5, 'section'),
	(6, 'question'),
	(7, 'question single choice'),
	(8, 'question multiple choice'),
	(9, 'answer boolean'),
	(10, 'answer date'),
	(11, 'answer integer'),
	(12, 'answer string'),
	(13, 'answer telephone'),
	(14, 'answer email');

--
-- Populate intake nodes
--
INSERT INTO `intake_node` VALUES
	(1, 1, NULL, NULL),
	(2, 2, NULL, NULL);

--
-- Populate intake labels
--
INSERT INTO `intake_label` VALUES
	(1, 1, 0, 'Quick Intake'),
	(2, 2, 0, 'In-depth Intake');

--
-- Alter agency table structure
--
--ALTER TABLE `agency`
--	DROP COLUMN `intakes_combined`,
--	DROP COLUMN `intake_quick`,
--	DROP COLUMN `intake_indepth`;

ALTER TABLE `agency`
	ADD COLUMN `intakes_combined` BOOLEAN NOT NULL DEFAULT 0 AFTER `id`,
	ADD COLUMN `intake_quick` INTEGER UNSIGNED NOT NULL DEFAULT 1 AFTER `intakes_combined`,
	ADD COLUMN `intake_indepth` INTEGER UNSIGNED DEFAULT 2 AFTER `intake_quick`;

--
-- Alter program table structure
--
--ALTER TABLE `program`
--	DROP COLUMN `intake_program`;

ALTER TABLE `program`
	ADD COLUMN `intake_program` INTEGER UNSIGNED AFTER `agency_id`;
