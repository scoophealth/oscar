alter table DemographicContact add consentToContact tinyint(1);
alter table DemographicContact add active tinyint(1);
update DemographicContact set consentToContact=1 where consentToContact is null;
update DemographicContact set active=1 where active is null;

