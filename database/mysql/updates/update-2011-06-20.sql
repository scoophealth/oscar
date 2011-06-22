alter table drugs add hide_cpp tinyint(1);
update drugs set hide_cpp=0;

alter table demographic add roster_termination_date date;
alter table demographicArchive add roster_termination_date date;

