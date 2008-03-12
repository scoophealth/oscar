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
