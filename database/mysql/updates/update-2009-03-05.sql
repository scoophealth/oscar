alter table measurementsExt drop key val;
alter table measurementsExt modify val text not null;

create table labRequestReportLink(
	id int(10) not null auto_increment,
	request_table varchar(60),
	request_id int(10),
	request_date date,
	report_table varchar(60) not null,
	report_id int(10) not null,
	primary key (id) );

