create table hl7TextInfo(
	id  int(10) NOT NULL auto_increment primary key,
	lab_no int(10) NOT NULL,
	sex varchar(1),
	health_no varchar(12),
	result_status varchar(1),
	final_result_count int(10),
	obr_date varchar(20),
	priority varchar(1),
	requesting_client varchar(100),
	discipline varchar(100),
	last_name varchar(30),
	first_name varchar(30),
	report_status varchar(1) NOT NULL,
	accessionNum varchar(20) NOT NULL
);

create table hl7TextMessage(
	lab_id int(10) NOT NULL auto_increment primary key,
	message longtext NOT NULL,
	type varchar(100)
);

create table oscarKeys(
	name varchar(100) NOT NULL primary key,
	pubKey text,
	privKey text
);

create table publicKeys(
	service varchar(100) NOT NULL primary key,
	type varchar(20) NOT NULL,
	pubKey text NOT NULL
);
