alter table professionalSpecialists drop remoteHl7ReferralUrl;
alter table professionalSpecialists add eReferralUrl varchar(255);
alter table professionalSpecialists add eReferralOscarKey varchar(1024);
alter table professionalSpecialists add eReferralServiceKey varchar(1024);
