alter table OcanStaffForm add clientStartDate date after startDate;
alter table OcanStaffForm add clientCompletionDate date after completionDate;
alter table OcanStaffForm add clientDateOfBirth varchar(10) after dateOfBirth;
alter table OcanStaffForm add ocanType varchar(20) not null after ocanFormVersion;
alter table OcanStaffForm modify gender varchar(10) null;

update OcanStaffForm set assessmentStatus="Completed" where assessmentStatus="Complete";
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="Active";

update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus is null;
update OcanStaffForm set assessmentStatus="In Progress" where assessmentStatus="";
 
update OcanStaffForm set ocanType="FULL";
 
INSERT INTO `OcanFormOption` VALUES (1213,'1.2','OCAN Assessment Status','In Progress','In Progress');
INSERT INTO `OcanFormOption` VALUES (1214,'1.2','OCAN Assessment Status','Completed','Completed');
INSERT INTO `OcanFormOption` VALUES (1215,'1.2','OCAN Assessment Status','Cancelled','Cancelled');
