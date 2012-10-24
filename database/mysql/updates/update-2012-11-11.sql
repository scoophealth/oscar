alter table vacancy_template change name name varchar(100) not null;
insert into `secObjectName` (`objectName`) values ('_pmm_editProgram.vacancies');
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.vacancies','x',0,'999998');

