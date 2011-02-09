alter table log drop primary key;
alter table log add index datetime (dateTime,provider_no);

--for new report by Template
alter table reportTemplates add column type varchar(32) default null;
