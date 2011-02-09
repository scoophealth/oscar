alter table Facility change useQuickConsent allowQuickConsent tinyint(1) not null;
alter table IntegratorConsent add column consentToPhoto tinyint(1) NOT NULL after consentToPreventions;