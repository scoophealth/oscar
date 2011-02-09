drop table IntegratorConsent;

create table IntegratorConsent
(
	id int primary key auto_increment,

	facilityId int not null, foreign key (facilityId) references Facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
    providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	createdDate datetime not null,
	
	integratorFacilityId int not null,
	restrictConsentToHic tinyint(1) NOT NULL,
	
	consentToSearches tinyint(1) NOT NULL,
	consentToBasicPersonalData tinyint(1) NOT NULL,
	consentToMentalHealthData tinyint(1) NOT NULL,
	consentToHealthNumberRegistry tinyint(1) NOT NULL,

	formVersion varchar(32),
	printedFormLocation varchar(255),
	refusedToSign tinyint(1) not null
);

update program set userDefined = 1;