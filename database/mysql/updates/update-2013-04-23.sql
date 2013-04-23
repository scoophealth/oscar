update allergies set archived='0' where archived is null;
update allergies set archived='0' where archived='n';
update allergies set archived='0' where archived='N';
update allergies set archived='0' where archived='f';
update allergies set archived='0' where archived='F';
update allergies set archived='1' where archived<>'0';
alter table allergies change archived archived tinyint(1) not null;