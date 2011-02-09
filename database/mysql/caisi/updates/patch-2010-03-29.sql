#drop table group_note_link;
create table GroupNoteLink (
        id int primary key auto_increment,
        created timestamp,
        noteId int(10) not null,
        demographicNo int(10) not null,
	anonymous tinyint(1),
	active tinyint(1),
        key(noteId),
        key(demographicNo),
	key(anonymous),
	key(active)
);

