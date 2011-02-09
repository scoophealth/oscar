alter table casemgmt_note change demographic_no demographic_no int(10);
drop index demographic_no on casemgmt_note ;
create index demographic_no  on casemgmt_note (demographic_no);
