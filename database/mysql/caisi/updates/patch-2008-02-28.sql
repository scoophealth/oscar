alter table admission add column admission_from_transfer tinyint(1) not null after admission_date;
alter table admission add column discharge_from_transfer tinyint(1) not null after discharge_date;
