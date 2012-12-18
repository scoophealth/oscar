CREATE TABLE `casemgmt_note_lock` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `session_id` varchar(255),
  `ip_address` varchar(15),
  `provider_no` varchar(6),  
  `note_id` int(10),
  `demographic_no` int(10),
  `lock_acquired` datetime,
  PRIMARY KEY (`id`),
  KEY `casemgmt_note_lock_providerNo` (`provider_no`),
  KEY `casemgmt_note_lock_note_id` (`note_id`),
  KEY `casemgmt_note_lock_providerNo_noteId` (`provider_no`,`note_id`)
);