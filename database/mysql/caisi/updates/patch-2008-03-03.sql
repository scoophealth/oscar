create table health_safety
(
  id             	bigint(20) not null auto_increment,
  demographic_no 	bigint(20) default '0' not null,
  message        	text,
  username       	varchar(128),
  updatedate    	datetime,
  primary key (id)
);