--Drop table
drop table lst_admission_status;
-- Create table
create table lst_admission_status
(
  code          varchar(20) not null,
  description   VARCHAR2(80),
  isactive      NUMBER(1),
  displayorder  NUMBER,
  primary key (code)
);
INSERT INTO lst_admission_status(code,description, isactive, displayorder)
values ('current','Current', 1, 10);
INSERT INTO lst_admission_status(code,description, isactive, displayorder)
values ('discharged','Discharged', 1, 20);
--Drop Table
drop table lst_program_type;
-- Create table
create table lst_program_type
(
  code         VARCHAR2(20) not null,
  description  VARCHAR2(80),
  isactive     NUMBER(1),
  displayorder NUMBER,
  primary key (code)
);
--insert records
insert into lst_program_type values('01','BED',1,1);
insert into lst_program_type values('02','SERVICE',1,2);
insert into lst_program_type values('03','EXTERNAL',1,3);