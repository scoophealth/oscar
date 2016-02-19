alter table consultationRequests add `lastUpdateDate` datetime not null;

alter table professionalSpecialists add eformId int(10);

alter table consultationRequests add fdid int(10);
alter table consultationRequests add source varchar(50);


