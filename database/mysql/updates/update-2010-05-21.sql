alter table professionalSpecialists add remoteHl7ReferralUrl varchar(255);
alter table professionalSpecialists add lastUpdated datetime not null;

update professionalSpecialists set lastUpdated=now();