drop table vacancy;

CREATE TABLE `Vacancy` (
  `id` int(11) primary key NOT NULL AUTO_INCREMENT,
  `templateId` int(11) NOT NULL,
  `status` varchar(24) NOT NULL,
  `dateClosed` timestamp NULL,
  `reasonClosed` varchar(255),
  `emailNotificationAddressesCsv` varchar(255)
);

alter table formONAR add column pg1_labExtra1Name varchar(20);
alter table formONAR add column pg1_labExtra2Name varchar(20);
alter table formONAR add column pg1_labExtra3Name varchar(20);

alter table formONAR add column pg1_labExtra1Value varchar(20);
alter table formONAR add column pg1_labExtra2Value varchar(20);
alter table formONAR add column pg1_labExtra3Value varchar(20);

alter table formONAR add column pg2_labExtra1Name varchar(20);
alter table formONAR add column pg2_labExtra2Name varchar(20);
alter table formONAR add column pg2_labExtra3Name varchar(20);

alter table formONAR add column pg2_labExtra1Value varchar(20);
alter table formONAR add column pg2_labExtra2Value varchar(20);
alter table formONAR add column pg2_labExtra3Value varchar(20);


alter table formONAR add column pg1_commentsAR1_2 text;
alter table formONAR add column pg1_commentsAR1_3 text;
alter table formONAR add column pg1_commentsAR1_4 text;

alter table formONAR add column pg1_4ColCom tinyint(1);
alter table formONAR add column pg1_lockPage tinyint(1);
