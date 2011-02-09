create table IssueGroup (id int primary key auto_increment, name varchar(255) not null);
create table IssueGroupIssues (issueGroupId int not null, issue_id int not null, unique(issueGroupId,issue_id));
