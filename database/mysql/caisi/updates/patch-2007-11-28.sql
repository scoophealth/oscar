create table IntakeRequiredFields (fieldKey varchar(255) not null primary key , isRequired tinyint not null);

create table facility (
     `id` int NOT NULL auto_increment,
     `name` varchar(32) NOT NULL default '',
     `description` VARCHAR(150) NOT NULL default '',
     `disabled` tinyint(1) NOT NULL default '0',
     PRIMARY KEY (`id`),
     UNIQUE KEY `idx_facility_name` USING HASH (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


INSERT INTO facility (name, description) VALUES ('Default Facility', 'Default facility, please modify with a more appropriate name and description');

ALTER TABLE room ADD COLUMN facility_id int NOT NULL default 0;
ALTER TABLE room ADD CONSTRAINT `FK_room_facility` FOREIGN KEY (`facility_id`) REFERENCES `facility` (`id`);
ALTER TABLE bed ADD COLUMN facility_id int NOT NULL default 0;
ALTER TABLE bed MODIFY room_id  int(10) unsigned default NULL;

