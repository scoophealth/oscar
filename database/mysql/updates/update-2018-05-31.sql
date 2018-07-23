alter table view add providerNo varchar(6);

INSERT INTO `LookupListItem` VALUES (\N,(select id from LookupList where name = 'practitionerNoType'),'OCP','Ontario College of Pharmacists (OCP)',3,1,'oscar',now());

