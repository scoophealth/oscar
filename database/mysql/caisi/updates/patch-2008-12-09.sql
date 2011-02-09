alter table casemgmt_issue_notes drop primary key;

create index casemgmt_issue_notes_id on casemgmt_issue_notes (id);

create index casemgmt_issue_notes_note_id on casemgmt_issue_notes (note_id);