INSERT INTO `LookupListItem` (lookupListId, value, label, displayOrder, active, createdBy, dateCreated) VALUES
((SELECT id FROM `LookupList` WHERE `name` = 'practitionerNoType'), 'CPSBC', 'College of Physicians and Surgeons of British Columbia',3,1,'oscar',now()),
((SELECT id FROM `LookupList` WHERE `name` = 'practitionerNoType'), 'CNPBC', 'College of Naturopathic Physicians of BC',3,1,'oscar',now()),
((SELECT id FROM `LookupList` WHERE `name` = 'practitionerNoType'), 'CRNBC', 'College of Registered Nurses of BC',3,1,'oscar',now()),
((SELECT id FROM `LookupList` WHERE `name` = 'practitionerNoType'), 'CPBC', 'College of Psychologists BC',3,1,'oscar',now()),
((SELECT id FROM `LookupList` WHERE `name` = 'practitionerNoType'), 'CMBC', 'College of Midwives of BC',3,1,'oscar',now());

