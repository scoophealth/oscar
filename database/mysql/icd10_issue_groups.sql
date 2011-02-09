# make all the issue groups
insert into IssueGroup (name) select distinct description from issue where code like 'CTCMM%';

# populate the issue groups
insert into IssueGroupIssues select IssueGroup.id,issue.issue_id from IssueGroup,issue where IssueGroup.name=issue.description;

# all ICD 10 issues are physical health issues except the ones listed here
insert into IssueGroupIssues select (select id from IssueGroup where name='Physical Health'),issue.issue_id from issue,icd10 where issue.code=icd10 and not (issue.code>='F1000' and issue.code<='F19zz') and not (issue.code>='F0000' and issue.code<='F09zz') and not (issue.code>='F2000' and issue.code<='F99zz') and not (issue.code>='R4100' and issue.code<='R41zz') and not (issue.code>='R4400' and issue.code<='R46zz');

# all ICD 10 addiction codes : f10xx-f19xx
insert into IssueGroupIssues select (select id from IssueGroup where name='Addictions'),issue.issue_id from issue,icd10 where issue.code=icd10 and issue.code>='F1000' and issue.code<='F19zz';

# all ICD 10 mental health codes : F00xx - F09xx, F20xx - F99xx, R41xx, R44xx, R45xx, R46xx
insert into IssueGroupIssues select (select id from IssueGroup where name='Mental Health'),issue.issue_id from issue,icd10 where issue.code=icd10 and ( (issue.code>='F0000' and issue.code<='F09zz') or (issue.code>='F2000' and issue.code<='F99zz') or (issue.code>='R4100' and issue.code<='R41zz') or (issue.code>='R4400' and issue.code<='R46zz') );