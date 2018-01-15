alter table provider add `practitionerNoType` varchar(255);
alter table providerArchive add `practitionerNoType` varchar(255);
update provider set practitionerNoType = '';

INSERT INTO `LookupList` VALUES (\N,'practitionerNoType','Practitioner No Type List','Select list for disambiguating practitionerNo in provider record',NULL,1,'oscar',now());
SET @lid = LAST_INSERT_ID();
INSERT INTO `LookupListItem` VALUES (\N,@lid,'CPSO','College of Physicians and Surgeons of Ontario',3,1,'oscar',now()),(\N,@lid,'CNO','College of Nurses of Ontario (CNO)',3,1,'oscar',now());

