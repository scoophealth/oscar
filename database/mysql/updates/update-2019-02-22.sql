alter table demographic add middleNames varchar(100);
alter table demographic add mailingAddress varchar(60);
alter table demographic add mailingCity varchar(50);
alter table demographic add mailingProvince varchar(20);
alter table demographic add mailingPostal varchar(9);
alter table demographic add roster_enrolled_to varchar(20) after roster_termination_reason;

alter table demographicArchive add middleNames varchar(100);
alter table demographicArchive add mailingAddress varchar(60);
alter table demographicArchive add mailingCity varchar(50);
alter table demographicArchive add mailingProvince varchar(20);
alter table demographicArchive add mailingPostal varchar(9);
alter table demographicArchive add roster_enrolled_to varchar(20) after roster_termination_reason;
