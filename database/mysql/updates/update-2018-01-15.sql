alter table provider add `practitionerNoType` varchar(255);
alter table providerArchive add `practitionerNoType` varchar(255);
update provider set practitionerNoType = '';

