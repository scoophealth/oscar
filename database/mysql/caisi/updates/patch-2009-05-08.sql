create table DigitalSignature
(
	id int primary key auto_increment,
	facilityId int not null, foreign key (facilityId) references Facility(id),
	providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	dateSigned datetime not null,
	signatureImage blob not null
);

alter table IntegratorConsent add digitalSignatureId int;
alter table IntegratorConsent add foreign key (digitalSignatureId) references DigitalSignature(id);
