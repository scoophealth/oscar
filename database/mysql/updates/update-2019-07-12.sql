alter table demographic change mailingAddress  residentialAddress  varchar(60);
alter table demographic change mailingCity  residentialCity  varchar(50);
alter table demographic change mailingProvince  residentialProvince  varchar(20);
alter table demographic change mailingPostal  residentialPostal  varchar(9);

alter table demographicArchive change mailingAddress  residentialAddress  varchar(60);
alter table demographicArchive change mailingCity  residentialCity  varchar(50);
alter table demographicArchive change mailingProvince  residentialProvince  varchar(20);
alter table demographicArchive change mailingPostal  residentialPostal  varchar(9);

