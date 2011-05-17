ALTER TABLE appointment MODIFY type VARCHAR(50) NULL;

DROP TABLE IF EXISTS `appointmentType`;
CREATE TABLE `appointmentType` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NULL,
  `notes` varchar(80) NULL,
  `reason` varchar(80) NULL,
  `location` varchar(30) NULL,
  `resources` varchar(10) NULL,
  `duration` int(12),
  PRIMARY KEY (`id`)
);
