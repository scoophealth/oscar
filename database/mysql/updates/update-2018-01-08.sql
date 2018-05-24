CREATE TABLE CVCMedication (
  `id` int(11) NOT NULL auto_increment,
  `versionId` integer,
  `din` integer,
  `dinDisplayName` varchar(255),
  `snomedCode` varchar(255),
  `snomedDisplay` varchar(255),
  `status` varchar(40),
  `isBrand` tinyint(1),
  `manufacturerId` integer,
  `manufacturerDisplay` varchar(255),
  PRIMARY KEY  (`id`)
);

CREATE TABLE CVCMedicationGTIN (
  `id` int(11) NOT NULL auto_increment,
  `cvcMedicationId` integer NOT NULL,
  `gtin` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
);

CREATE TABLE CVCMedicationLotNumber (
  `id` int(11) NOT NULL auto_increment,
  `cvcMedicationId` integer NOT NULL,
  `lotNumber` varchar(255) NOT NULL,
  `expiryDate` date,
  PRIMARY KEY  (`id`)
);

CREATE TABLE CVCImmunization (
  `id` int(11) NOT NULL auto_increment,
  `versionId` integer,
  `snomedConceptId` varchar(255),
  `displayName` varchar(255),
  `picklistName` varchar(255),
  `generic` tinyint(1),
  `prevalence` int,
  `parentConceptId` varchar(255),
  `ispa` tinyint(1),
  PRIMARY KEY  (`id`)
);

alter table preventions modify prevention_type varchar(255);

alter table preventions add snomedId varchar(255);

CREATE TABLE `CVCMapping` (
   `id` int(10) NOT NULL auto_increment,
   `oscarName` varchar(255),
   `cvcSnomedId` varchar(255),
   `preferCVC` tinyint(1),
  PRIMARY KEY (`id`)
);

