-- Table to allow user to edit the fields showing on the page.
CREATE TABLE `caisi_editor` (
  `id` bigint(11) NOT NULL auto_increment,
  `category` varchar(50) NOT NULL,
  `label` varchar(255) NOT NULL,
  `type` varchar(20) NOT NULL,
  `labelValue` varchar(255) default NULL,
  `labelCode` varchar(50) default NULL,
  `horizontal` char(3) default NULL,
  `isActive` char(3) default NULL,  
  PRIMARY KEY  (`id`)
);

insert into access_type (name, type) values('Create service restriction','access'),('Disable service restriction','access');

