CREATE TABLE `tickler_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(55),
  `description` varchar(255),
  `active` bit(1),
  PRIMARY KEY (`id`)
);

INSERT INTO `tickler_category` VALUES ('1', 'To Call In', 'Call this patient in for a follow-up visit', b'1'), ('2', 'Reminder Note', 'Send a reminder note to this patient', b'1'), ('3', 'Follow-up Billing', 'Follow-up Additional Billing', b'1');

ALTER TABLE tickler ADD category_id int(11);

ALTER TABLE security ADD COLUMN oneIdKey VARCHAR(255);
ALTER TABLE security ADD COLUMN `oneIdEmail` VARCHAR(255);
ALTER TABLE security ADD COLUMN `delegateOneIdEmail` VARCHAR(255);