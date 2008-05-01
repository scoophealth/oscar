alter table formDischargeSummary add doctor4 varchar(30) default NULL after location3;
alter table formDischargeSummary add phoneNumber4 varchar(30) default NULL after doctor4;
alter table formDischargeSummary add date4 varchar(20) default NULL after phoneNumber4;
alter table formDischargeSummary add location4 varchar(100) default NULL after date4;
alter table formDischargeSummary add notes text default NULL;

