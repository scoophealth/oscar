-- Create table
create table HEALTH_SAFETY
(
  ID             NUMBER(20) not null,
  DEMOGRAPHIC_NO NUMBER(20) default '0' not null,
  MESSAGE        VARCHAR2(4000),
  USERNAME       VARCHAR2(128),
  UPDATEDATE     DATE,
  PRIMARY KEY (ID)
)
