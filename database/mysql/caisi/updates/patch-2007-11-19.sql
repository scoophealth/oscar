alter table program add column userDefined tinyint(1) not null after program_id;
update program set userDefined=1;
update program set userDefined=0 where program_id in (10001, 10002, 10003, 10004, 10005, 10006, 10007, 10008, 10009, 10010, 10011, 10012, 10013, 10014);
