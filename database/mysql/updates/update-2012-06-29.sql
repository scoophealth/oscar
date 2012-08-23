drop table Vacancy;

CREATE TABLE `vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `wlProgramId` int(11) NOT NULL,
  `dateCreated` date NOT NULL,
  `emailNotificationAddressesCsv` varchar(255)
);
