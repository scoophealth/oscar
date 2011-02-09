ALTER TABLE facility ADD COLUMN contact_name varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN contact_email varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN contact_phone varchar(255) default NULL;
ALTER TABLE facility ADD COLUMN hic tinyint(1) NOT NULL default false;

