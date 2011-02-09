alter table hl7TextMessage change type type varchar(100) not null;
alter table hl7TextMessage add serviceName varchar(100) not null;
update hl7TextMessage set serviceName=type;
alter table hl7TextMessage add created datetime not null;
update hl7TextMessage set created=now();

alter table professionalSpecialists change eReferralUrl eDataUrl varchar(255);
alter table professionalSpecialists change eReferralOscarKey eDataOscarKey varchar(1024);
alter table professionalSpecialists change eReferralServiceKey eDataServiceKey varchar(1024);
alter table professionalSpecialists change eReferralServiceName eDataServiceName varchar(255);

alter table log change action action varchar(64);

alter table publicKeys add column privateKey text not null;
update publicKeys set privateKey='not available';