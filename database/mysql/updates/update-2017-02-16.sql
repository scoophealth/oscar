CREATE TABLE `ContactType` (
  id int(10) NOT NULL auto_increment,
  name varchar(255),
  male tinyint(1),
  female tinyint(1),
  inverseRelationship tinyint(1),
  active tinyint(1),
  PRIMARY KEY  (`id`)
) ;



CREATE TABLE `ProgramContactType` (
 `programId` int(11) NOT NULL,
 `contactTypeId` int(11) NOT NULL,
 `category` varchar(255) not null,
 PRIMARY KEY  (`programId`,`contactTypeId`,`category`)
);


