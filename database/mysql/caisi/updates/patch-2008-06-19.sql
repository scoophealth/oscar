create table IntegratorConsentComplexForm
(
	integratorConsentID int not null, foreign key (integratorConsentID) references IntegratorConsent(id) on delete cascade,
	form varchar(32),
	printedFormLocation varchar(255),
	refusedToSign tinyint(1) not null
);


alter table formDischargeSummary change coveredByODB medicationProvided char(1);