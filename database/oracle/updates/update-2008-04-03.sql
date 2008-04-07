-- Create table
create table lst_orgcd
(
  code         VARCHAR2(8) not null,
  description  VARCHAR2(240),
  activeyn     VARCHAR2(1),
  orderbyindex NUMBER,
  codetree      VARCHAR2(80),
  primary key (code)  
)
;
create index IDX_ORGCD_CODE on lst_orgcd (codetree);
-- add description in the role list
alter table secrole add  description varchar2(60);
;

-- adding 3 new columns id,orgcd, activeyn to the secuserrole table, 
-- id is added to allow one user multiple roles/org combinations

create table secuserrole_tmp as select * from secuserrole;
drop table secuserrole;
create table secuserrole(
  id  NUMBER not null,  -- should be defined as auto increase in mysql
  provider_no VARCHAR2(6) not null,
  role_name   VARCHAR2(30) not null,
  orgcd       VARCHAR2(80) default 'R0000001',
  activeyn    NUMBER(1),
  primary key (id)
);
insert into secuserrole (id, provider_no, role_name, orgcd, activeyn)
select hibernate_sequence.nextval, a.provider_no,a.role_name,'R0000001',1
from secuserrole_tmp a
;
drop table secuserrole_tmp;
-- this is for oracle to handle the auto increase id field
CREATE OR REPLACE TRIGGER TRI_SECUSERROLE_ID
BEFORE INSERT
ON SECUSERROLE
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.ID IS NULL THEN
        SELECT MAX(ID)+1 INTO IDX FROM SECUSERROLE;
        :new.ID := IDX;
     END IF;
     :new.ORGCD := 'R0000001';
END;

-- add descriptions to the object list
alter table secobjectname add description varchar2(60);
alter table secobjectname add orgapplicable number(1);

