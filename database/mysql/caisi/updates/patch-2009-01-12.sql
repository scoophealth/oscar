alter table ClientLink add column facilityId int not null after id;
update ClientLink set facilityId=(select id from Facility limit 1);
alter table ClientLink add foreign key (facilityId) references Facility(id);

create table HnrDataValidation
(
	id int primary key auto_increment,
	facilityId int not null, index(facilityId), foreign key (facilityId) references Facility(id),
	clientId int not null, index(clientId), foreign key (clientId) references demographic(demographic_no),
	created datetime not null,
	validatorProviderNo varchar(6) not null, foreign key (validatorProviderNo) references provider(provider_no),
	valid tinyint(1) not null,
	validationType varchar(32) not null, index(validationType),
	validationCrc bigint not null
);

