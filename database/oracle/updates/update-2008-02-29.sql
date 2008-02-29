alter table ADMISSION add column admission_from_transfer number(1) default 0;
alter table ADMISSION add column discharge_from_transfer number(1) default 0;
update ADMISSION set admission_from_transfer=0, discharge_from_transfer=0;
alter table ADMISSION modify column admission_from_transfer number(1) NOT NULL;
alter table ADMISSION modify column admission_from_transfer number(1) NOT NULL;