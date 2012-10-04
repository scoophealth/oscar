alter table studydata add column keyname varchar(32);
create table providerstudy (
	study_no int(10), 
	provider_no varchar(6), 
	creator varchar(6), 
	`timestamp` timestamp
);