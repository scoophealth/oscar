CREATE TABLE `tickler_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(55),
  `description` varchar(255),
  `active` bit(1),
  PRIMARY KEY (`id`)
);

INSERT INTO `tickler_category` VALUES ('1', 'To Call In', 'Call this patient in for a follow-up visit', b'1'), ('2', 'Reminder Note', 'Send a reminder note to this patient', b'1'), ('3', 'Follow-up Billing', 'Follow-up Additional Billing', b'1');

ALTER TABLE tickler ADD category_id int(11);
CREATE TABLE `preventionsBilling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `preventionType` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `billingServiceCodeAndUnit` varchar(50) NOT NULL,
  `billingType` varchar(50) NOT NULL,
  `visitType` varchar(50) NOT NULL,
  `billingDxCode` varchar(50) NOT NULL,
  `visitLocation` varchar(50) NOT NULL,
  `sliCode` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pbtype` (`preventionType`)
);

alter table Consent add column edit_date timestamp;
