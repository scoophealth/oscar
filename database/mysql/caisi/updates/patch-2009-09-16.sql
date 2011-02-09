create table CdsFormOption
(
	id int primary key auto_increment,
	cdsFormVersion varchar(16) not null,
	cdsDataCategory varchar(16) not null,
	cdsDataCategoryName varchar(255) not null
);

source ../init_cds_form_4_options.sql;