alter table program add column facility_id int not null after program_id;
alter table program add index (facility_id);
update program,room set program.facility_id=room.facility_id where program.program_id=room.program_id;
update program set facility_id=1 where facility_id=0;
alter table program add foreign key (facility_id) references facility(id);
