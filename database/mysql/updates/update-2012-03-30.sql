alter table DemographicContact add facilityId int not null;
alter table DemographicContact add creator varchar(20) not null;
update DemographicContact set facilityId=1;

