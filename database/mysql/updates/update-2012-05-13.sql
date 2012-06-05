alter table vacancy add `WL_PROGRAM_ID` int(11) NOT NULL;
update vacancy set WL_PROGRAM_ID=0;
alter table vacancy add `DATE_CREATE` date NOT NULL;
update vacancy set DATE_CREATE=now();
