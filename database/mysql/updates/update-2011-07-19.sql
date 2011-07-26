alter table HRMDocument add column hrmCategoryId int;

drop table HRMCategory;

CREATE TABLE `HRMCategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  subClassNameMnemonic varchar(255) not null,
  PRIMARY KEY (`id`)
);

insert into HRMCategory values (null, 'General Oscar Lab', 'DEFAULT');
insert into HRMCategory values (null, 'Oscar HRM Category CT:ABDW' ,'CT:ABDW');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP5' ,'RAD:CSP5');
insert into HRMCategory values (null, 'Oscar HRM Category NM:THYSAN' ,'NM:THYSAN');
insert into HRMCategory values (null, 'Oscar HRM Category NM:BLDPOL' ,'NM:BLDPOL');
insert into HRMCategory values (null, 'Oscar HRM Category US:ABDC' ,'US:ABDC');
insert into HRMCategory values (null, 'Oscar HRM Category US:PELVLT' ,'US:PELVLT');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD' ,'RAD:ABD');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CXR2' ,'RAD:CXR2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD2' ,'RAD:ABD2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ANKB' ,'RAD:ANKB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP' ,'RAD:CSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:TSP' ,'RAD:TSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:LSP4ER' ,'RAD:LSP4ER');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:DIGB' ,'RAD:DIGB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ELBB' ,'RAD:ELBB');
insert into HRMCategory values (null, 'Oscar HRM Category MAM:MAMMOB' ,'MAM:MAMMOB');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:ECHO' ,'ECHO:ECHO');
insert into HRMCategory values (null, 'Oscar HRM Category ECHOWL:ECH0520' ,'ECHOWL:ECH0520');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:MDAB' ,'ECHO:MDAB');


