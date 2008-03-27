-- this updates from Ted

create index casemgmt_note_program_no on casemgmt_note(program_no); 
create index admission_program_id on admission(program_id); 
create index admission_client_id on admission(client_id); 
create index issue_code on issue(code);
create index IssueGroupIssues_issue_id on IssueGroupIssues(issue_id);