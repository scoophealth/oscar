create table OcanSubmissionLog (
submissionId int primary key auto_increment,
submitDateTime timestamp,
result varchar(255),
transactionId varchar(100),
resultMessage text,
submissionData longtext
);


alter table OcanStaffForm add submissionId integer;

