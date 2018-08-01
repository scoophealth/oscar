create table tmp_bm_nos (id int(10));
insert into tmp_bm_nos select billingmaster_no from billingmaster bm,billing b where bm.billing_no = b.billing_no and bm.billingstatus='S' and b.billingtype='Pri';
update billingmaster set billingstatus='A' where billingmaster_no in (select id from tmp_bm_nos);
drop table tmp_bm_nos;

