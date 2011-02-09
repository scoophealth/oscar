#added 3 signature columns to the rourke 2006 form
#changed the old lone signature on p1 to refer to a 2 week visit

alter table formRourke2006 change p1_signature p1_signature2w varchar(250);
alter table formRourke2006 add column p1_signature1w varchar(250);
alter table formRourke2006 add column p1_signature1m varchar(250);
alter table formRourke2006 add column p2_signature6m varchar(250);
