#allowing longer pins (used as indivo id by the indivo project)
ALTER TABLE demographic CHANGE pin pin varchar(255) DEFAULT NULL;

#let users set default billing type for different billing forms
CREATE TABLE ctl_billingtype (
	servicetype varchar(10) not null,
	billtype    varchar(5)  not null,
	primary key (servicetype));

