CREATE TABLE measurementsExt(
	id int(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	measurement_id int(10) NOT NULL,
	keyval varchar(20) NOT NULL,
	val varchar(20)	NOT NULL,
	INDEX(measurement_id),
        INDEX(val)
);

CREATE TABLE measurementMap(
	id int(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	loinc_code varchar(20) NOT NULL,
	ident_code varchar(20) NOT NULL,
	name varchar(255),
	lab_type varchar(10) NOT NULL,
	INDEX(ident_code)
);

CREATE TABLE incomingLabRules(
	id int(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	provider_no varchar(6) NOT NULL,
	status varchar(1),
	frwdProvider_no varchar(6),
	archive varchar(1) default 0
);

