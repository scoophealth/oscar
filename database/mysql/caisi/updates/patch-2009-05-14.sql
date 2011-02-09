alter table app_lookuptable add readonly tinyint(1);
update app_lookuptable set readonly=0;
alter table app_lookuptable_fields add fieldlength int(10);
update app_lookuptable_fields set fieldlength=0;

alter table IntegratorConsent drop restrictConsentToHic;
alter table IntegratorConsent drop consentToSearches;
alter table IntegratorConsent drop consentToHealthNumberRegistry;
alter table IntegratorConsent drop formVersion;
alter table IntegratorConsent drop printedFormLocation;
alter table IntegratorConsent drop refusedToSign;

alter table IntegratorConsent change consentToAllNonDomainData consentToShareData tinyint(1) not null;
alter table IntegratorConsent change consentToMentalHealthData excludeMentalHealthData tinyint(1) not null;

update IntegratorConsent set excludeMentalHealthData=2 where excludeMentalHealthData=1;
update IntegratorConsent set excludeMentalHealthData=1 where excludeMentalHealthData=0;
update IntegratorConsent set excludeMentalHealthData=0 where excludeMentalHealthData=2;

alter table IntegratorConsent add index (createdDate);