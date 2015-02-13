alter table professionalSpecialists add privatePhoneNumber varchar(30);
alter table professionalSpecialists add cellPhoneNumber varchar(30);
alter table professionalSpecialists add pagerNumber varchar(30);
alter table professionalSpecialists add salutation varchar(10);
update professionalSpecialists set privatePhoneNumber='';
update professionalSpecialists set cellPhoneNumber='';
update professionalSpecialists set pagerNumber='';
update professionalSpecialists set salutation='';

