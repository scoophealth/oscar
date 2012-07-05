alter table ProviderPreference add eRxEnabled tinyint(1) not null;
alter table ProviderPreference add eRx_SSO_URL varchar(128);
alter table ProviderPreference add eRxUsername varchar(32);
alter table ProviderPreference add eRxPassword varchar(64);
alter table ProviderPreference add eRxFacility varchar(32);
alter table ProviderPreference add eRxTrainingMode tinyint(1) not null;