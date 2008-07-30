drop table IntegratorConsentComplexForm;
drop table IntegratorConsent;

create table IntegratorConsent
(
	id int primary key auto_increment,

	facilityId int not null, foreign key (facilityId) references Facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
    providerNo varchar(6) not null, foreign key (providerNo) references provider(provider_no),
	createdDate datetime not null,
	consentToStatistics tinyint(1) NOT NULL,
	consentToBasicPersonalId tinyint(1) NOT NULL,
	consentToHealthCardId tinyint(1) NOT NULL,
	consentToIssues tinyint(1) NOT NULL,
	consentToNotes tinyint(1) NOT NULL,
	restrictConsentToHic tinyint(1) NOT NULL,

	formVersion varchar(32),
	printedFormLocation varchar(255),
	refusedToSign tinyint(1) not null
);