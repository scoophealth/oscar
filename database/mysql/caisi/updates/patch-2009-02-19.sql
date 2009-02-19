alter table IntegratorConsent change consentToBasicPersonalData consentToAllNonDomainData tinyint(1) NOT NULL;

alter table casemgmt_note add column position int(10) default 0;

alter table client_image add column contents longblob;