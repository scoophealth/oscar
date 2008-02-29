alter table ADMISSION add admission_from_transfer number(1) default 0;
alter table ADMISSION add discharge_from_transfer number(1) default 0;
update ADMISSION set admission_from_transfer=0, discharge_from_transfer=0;
alter table ADMISSION modify admission_from_transfer number(1) NOT NULL;
alter table ADMISSION modify discharge_from_transfer number(1) NOT NULL;