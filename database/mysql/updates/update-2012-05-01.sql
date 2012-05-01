update program set transgender=0 where transgender is null;
alter table program change transgender transgender tinyint(1) not null;

update program set firstNation=0 where firstNation is null;
alter table program change firstNation firstNation tinyint(1) not null;

update program set bedProgramAffiliated=0 where bedProgramAffiliated is null;
alter table program change bedProgramAffiliated bedProgramAffiliated tinyint(1) not null;

update program set alcohol=0 where alcohol is null;
alter table program change alcohol alcohol tinyint(1) not null;

update program set physicalHealth=0 where physicalHealth is null;
alter table program change physicalHealth physicalHealth tinyint(1) not null;

update program set mentalHealth=0 where mentalHealth is null;
alter table program change mentalHealth mentalHealth tinyint(1) not null;

update program set housing=0 where housing is null;
alter table program change housing housing tinyint(1) not null;

update program set ageMin=1 where ageMin is null;
alter table program change ageMin ageMin int not null;

update program set ageMax=200 where ageMax is null;
alter table program change ageMax ageMax int not null;
