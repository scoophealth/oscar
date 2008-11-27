alter table log drop primary key;
alter table log add index datetime (dateTime,provider_no);
