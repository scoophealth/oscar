alter table measurementType change type type varchar(50) not null;
alter table measurementTypeDeleted change type type varchar(50) not null;
alter table billingservice change sliFlag sliFlag TINYINT(1) NOT NULL default 0;
alter table billingnote change note note text;
