alter table program add column (ageMin int not null, ageMax int not null);
update program set ageMin=1;
update program set ageMax=200;