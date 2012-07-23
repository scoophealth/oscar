CREATE TABLE `PrintResourceLog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  resourceName varchar(100) NOT NULL,
  resourceId varchar(50) NOT NULL,
  dateTime timestamp not null,
  providerNo varchar(10),
  externalLocation varchar(200),
  externalMethod varchar(100),
  PRIMARY KEY (`id`)
);

ALTER TABLE `RemoteIntegratedDataCopy` DROP KEY `RIDopy_demo_dataT_sig_fac_arch`, ADD KEY `RIDopy_demo_dataT_sig_fac_arch` (`demographic_no`,`datatype`(165),`signature`(165),`facilityId`,`archived`);

ALTER TABLE `drugReason` DROP KEY `codingSystem`, ADD KEY `codingSystem` (`codingSystem`(30),`code`(30));
