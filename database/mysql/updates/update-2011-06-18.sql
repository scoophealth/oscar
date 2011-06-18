CREATE TABLE `HRMCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `HRMDocument` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeReceived` datetime ,
  `reportType` varchar(50),
  `reportHash` varchar(64),
  `reportLessTransactionInfoHash` varchar(64) ,
  `reportStatus` varchar(10) ,
  `reportFile` varchar(255) ,
  `unmatchedProviders` varchar(255),
  `numDuplicatesReceived` int(11),
  `reportDate` datetime ,
  `parentReport` int(11) ,
  `reportLessDemographicInfoHash` varchar(64) ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentSubClass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hrmDocumentId` int(11) ,
  `subClass` varchar(50) ,
  `subClassMnemonic` varchar(50) ,
  `subClassDescription` varchar(255) ,
  `subClassDateTime` date ,
  `isActive` tinyint(4) NOT NULL ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentToDemographic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographicNo` varchar(20) ,
  `hrmDocumentId` varchar(20) ,
  `timeAssigned` datetime ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMDocumentToProvider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `providerNo` varchar(20) ,
  `hrmDocumentId` varchar(20) ,
  `signedOff` int(11) ,
  `signedOffTimestamp` datetime ,
  PRIMARY KEY (`id`)
) ;

CREATE TABLE `HRMSubClass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(255) ,
  `subClassName` varchar(255) ,
  `subClassMnemonic` varchar(255) ,
  `subClassDescription` varchar(255) ,
  `sendingFacilityId` varchar(50) NOT NULL,
  `hrmCategoryId` int(11) ,
  PRIMARY KEY (`id`)
);

CREATE TABLE `dataExport` (
  `id` int(11) primary key auto_increment,
  `file` varchar(255),
  `daterun` timestamp,
  `user` varchar(255),
  `type` varchar(255),
  `contactLName` varchar(255),
  `contactFName` varchar(255),
  `contactPhone` varchar(255),
  `contactEmail` varchar(255)
);
