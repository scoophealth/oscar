CREATE TABLE `EncounterType` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255),
  `global` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
);

insert into EncounterType (value,global) VALUES ('face to face encounter with client',1),('telephone encounter with client',1),('email encounter with client',1),('encounter without client',1),('group face to face encounter',0),('group telephone encounter',0),('group encounter with client',0),('group encounter without group',0);

CREATE TABLE `ProgramEncounterType` (
  `programId` int(10) NOT NULL,
  `encounterTypeId` int(10) NOT NULL,
  PRIMARY KEY (`programId`,`encounterTypeId`)
);

