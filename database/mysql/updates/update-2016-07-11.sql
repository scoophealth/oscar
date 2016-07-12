alter table security modify password varchar(255);
alter table security modify pin varchar(255);
alter table security add passwordUpdateDate datetime;
alter table security add pinUpdateDate datetime;
alter table security add lastUpdateUser varchar(20);
alter table security add lastUpdateDate timestamp;

CREATE TABLE `SecurityArchive` (
 `id` int(11) NOT NULL auto_increment,
  security_no int(6) NOT NULL,
  user_name varchar(30) NOT NULL,
  password varchar(255) NOT NULL,
  provider_no varchar(6) default NULL,
  pin varchar(255),
  b_ExpireSet int(1),
  date_ExpireDate date,
  b_LocalLockSet int(1),
  b_RemoteLockSet int(1),
  forcePasswordReset tinyint(1),
  passwordUpdateDate datetime,
  pinUpdateDate datetime,
  lastUpdateUser varchar(20),
  lastUpdateDate timestamp,
 PRIMARY KEY  (`id`)
);

