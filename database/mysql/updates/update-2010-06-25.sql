alter table hl7TextMessage change type type varchar(100) not null;
alter table hl7TextMessage add serviceName varchar(100) not null;
update hl7TextMessage set serviceName=type;
alter table hl7TextMessage add created datetime not null;
update hl7TextMessage set created=now();