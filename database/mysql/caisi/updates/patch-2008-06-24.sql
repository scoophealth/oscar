alter table facility add column useQuickConsent tinyint(1) not null;
update facility set useQuickConsent=1;

update bed set facility_id=1 where facility_id=0;
update room set facility_id=1 where facility_id=0;
update intake set facility_id=1 where facility_id is NULL;
update room set occupancy=20 where occupancy=0;