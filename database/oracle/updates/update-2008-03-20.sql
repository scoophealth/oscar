-- update the Provider_no from int to varchar initiate by Ted on Mar 18, 2008
create table client_referral_tmp as select * from client_referral;
drop table client_referral;
create table CLIENT_REFERRAL
(
  REFERRAL_ID              NUMBER(20) not null,
  AGENCY_ID                NUMBER(20) default '0' not null,
  CLIENT_ID                NUMBER(20) default '0' not null,
  REFERRAL_DATE            DATE,
  PROVIDER_NO              VARCHAR2(6) default '0' not null,
  NOTES                    VARCHAR2(4000),
  PROGRAM_ID               NUMBER(20) default '0' not null,
  STATUS                   VARCHAR2(30),
  COMPLETION_NOTES         VARCHAR2(4000),
  TEMPORARY_ADMISSION_FLAG NUMBER(1),
  COMPLETION_DATE          DATE,
  SOURCE_AGENCY_ID         NUMBER(20),
  PRESENT_PROBLEMS         VARCHAR2(4000),
  RADIOREJECTIONREASON     VARCHAR2(10) default '0',
  FACILITY_ID              NUMBER(10),
  primary key (REFERRAL_ID)
)
;
insert into client_referral select * from client_referral_tmp;
drop table client_referral;