alter table program add program_status varchar(8) not null default 'active';
update program set program_status="active" where program_status="";