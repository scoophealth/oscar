drop table IntegratorConsent;

create table IntegratorConsent
(
	id int primary key auto_increment,

	facilityId int not null, foreign key (facilityId) references Facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
    providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	createdDate datetime not null, index(createdDate),
	
	excludeMentalHealthData tinyint(1) NOT NULL,
	clientConsentStatus varchar(32) NOT NULL,

	digitalSignatureId int, foreign key (digitalSignatureId) references DigitalSignature(id)
);

create table IntegratorConsentShareDataMap
(
	IntegratorConsent_id int not null,
	mapkey int not null,
	element tinyint(1) not null
);