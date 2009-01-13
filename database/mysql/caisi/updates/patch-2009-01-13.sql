alter table provider
add init varchar(10),
add job_title varchar(100),
add email varchar(60),
add title varchar(20),
add lastUpdateUser varchar(6),
add lastUpdateDate date;

alter table demographic_merged
add lastUpdateUser varchar(6),
add lastUpdateDate date;

alter table program
add userDefined int(1),
add shelter_id int(11) default 0,
add facility_id int(10) default 0,
add capacity_funding int(10),
add capacity_space int(10),
add lastUpdateUser varchar(6),
add lastUpdateDate date;

alter table intake
add program_id int(10),
add lastUpdateDate date,
add end_date date;


alter table program_queue
add intake_id int(10);

update program set userDefined = 1 where type='community';

