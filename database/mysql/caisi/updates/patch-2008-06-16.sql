drop table if exists `IntegratorConsent`;
create table IntegratorConsent
(
	id int primary key auto_increment,
	facilityId int not null,
	demographicId int not null,
	unique(facilityId,demographicId),
    provider_no varchar(6) not null, foreign key (provider_no) references provider(provider_no),
	lastUpdate datetime not null,
	consentToStatistics tinyint(1) NOT NULL,
	consentToBasicPersonalId tinyint(1) NOT NULL,
	consentToHealthCardId tinyint(1) NOT NULL,
	consentToIssues tinyint(1) NOT NULL,
	consentToNotes tinyint(1) NOT NULL
);