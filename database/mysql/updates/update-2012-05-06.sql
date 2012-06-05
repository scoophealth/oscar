alter table vacancy_template add `WL_PROGRAM_ID` int(11) NOT NULL;
update vacancy_template set WL_PROGRAM_ID=0;

