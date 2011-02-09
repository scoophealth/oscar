create table health_safety
(
  id             	bigint(20) not null auto_increment,
  demographic_no 	bigint(20) default '0' not null,
  message        	text,
  username       	varchar(128),
  updatedate    	datetime,
  primary key (id)
);

create table provider_facility
(
	provider_id int not null,
	facility_id int not null,
	unique (provider_id, facility_id),
	index (facility_id)
);