DROP TABLE IF EXISTS IntegratorConsent;
DROP TABLE IF EXISTS IntegratorConsentComplexForm;
DROP TABLE IF EXISTS IntegratorConsentComplexExitInterview;

create table IntegratorConsent
(
	facilityId int not null, foreign key (facilityId) references facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	primary key (facilityId,demographicId),

    provider_no varchar(6) not null, foreign key (provider_no) references provider(provider_no),
	lastUpdate datetime not null,
	consentToStatistics tinyint(1) NOT NULL,
	consentToBasicPersonalId tinyint(1) NOT NULL,
	consentToHealthCardId tinyint(1) NOT NULL,
	consentToIssues tinyint(1) NOT NULL,
	consentToNotes tinyint(1) NOT NULL,
	restrictConsentToHic tinyint(1) NOT NULL
);

create table IntegratorConsentComplexForm
(
	facilityId int not null, foreign key (facilityId) references facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	primary key (facilityId,demographicId),

	form varchar(32),
	printedFormLocation varchar(255),
	refusedToSign tinyint(1) not null
);

create table IntegratorConsentComplexExitInterview
(
	facilityId int not null, foreign key (facilityId) references facility(id),
	demographicId int not null, foreign key (demographicId) references demographic(demographic_no),
	primary key (facilityId,demographicId),

	spokenLanguage varchar(64),
	readLanguage varchar(64),
	education varchar(64),
	timeToReviewConsent varchar(16),
	timeToReviewConsentComments varchar(255),
	pressured varchar(16),
	pressuredComments varchar(255),
	moreInfo varchar(16),
	moreInfoComments varchar(255),
	reAskConsent varchar(32),
	reAskConsentComments varchar(255),
	additionalComments varchar(255)
);