drop table vacancy;

CREATE TABLE `Vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `emailNotificationAddressesCsv` varchar(255)
);
