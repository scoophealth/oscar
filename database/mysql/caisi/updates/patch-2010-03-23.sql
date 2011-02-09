alter table Facility add enableGroupNotes tinyint(1) not null;
alter table OcanClientForm add assessmentStatus varchar(50);
create table group_note_link (
        id int primary key auto_increment,
        created timestamp,
        noteId int(10) not null,
        demographicNo int(10) not null,
        key(noteId),
        key(demographicNo)
);

