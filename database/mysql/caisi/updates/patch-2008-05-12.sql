create table IntegratorConsent
(
	id int primary key auto_increment,
	facilityId int not null,
	demographicId int not null,
	unique(facilityId,demographicId),
	consentLevel varchar(128) not null,
	lastUpdate datetime not null
);
