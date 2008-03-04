update access_type set name="can make program discharge" where name="can ake program discharge";

create table APP_CONFIG
(
  PARAMID       NUMBER(10) not null,
  PARAMETER     VARCHAR2(50) not null,
  PARAMVALUE    VARCHAR2(128) not null,
  EFFECTIVEDATE DATE,
  EXPIRYDATE    DATE,
  primary key (PARAMID)
)
;
insert into app_config(PARAMID,PARAMETER,PARAMVALUE,EFFECTIVEDATE,EXPIRYDATE)
values(1,'PIN_ENCRYPTED',0,to_date('2008-01-01','YYYY-MM-DD'),TO_DATE('2999-12-31','YYYY-MM-DD'));