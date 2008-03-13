CREATE OR REPLACE TRIGGER TRI_relationships_id
BEFORE INSERT
ON relationships
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN

     SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
     :new.id := IDX;
END;

create table REDIRECTLINKTRACKING
(
  redirectDate DATE not null,
  PROVIDER_NO    VARCHAR2(6) not null,
  REDIRECTLINKID INTEGER not null
)
;
create index IDX_REDIRECTLINKRTRACKING on REDIRECTLINKTRACKING (DATE1, REDIRECTLINKID);
CREATE OR REPLACE TRIGGER TRI_FACILITY_ID
BEFORE INSERT
ON FACILITY
FOR EACH ROW
DECLARE
       IDX NUMBER;
BEGIN
     IF :new.ID IS NULL THEN
          SELECT HIBERNATE_SEQUENCE.NEXTVAL INTO IDX FROM DUAL;
          :new.ID := IDX;
     END IF;
END;
-- update provider_facility's provider_no to varchar2(6) from int
create table provider_facility_tmp as select * from provider_facility;
drop table provider_facility;
create table provider_facility(provider_no varchar2(6), facility_id number, primary key (provider_no,facility_id));
insert into provider_facility select * from provider_facility_tmp;
drop table provider_facility_tmp;

