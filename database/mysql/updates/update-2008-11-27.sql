alter table log drop primary key;
alter table log add index (dateTime,provider_no);
