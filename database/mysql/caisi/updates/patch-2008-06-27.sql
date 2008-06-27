rename table facility to Facility;
alter table Facility change org_id orgId int not null;
alter table Facility change sector_id sectorId int not null;
alter table Facility change contact_email contactEmail varchar(255);
alter table Facility change contact_name contactName varchar(255);
alter table Facility change contact_phone contactPhone varchar(255);
alter table Facility add column enableIntegratedReferrals tinyint(1) not null;