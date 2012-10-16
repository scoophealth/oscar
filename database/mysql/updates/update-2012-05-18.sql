create table billing_on_payment (payment_id int (10) NOT NULL auto_increment primary key, billing_no int(6) NOT NULL, pay_date timestamp NOT NULL);
insert into billing_on_payment (billing_no) select distinct billing_no from billing_on_ext;
alter table billing_on_ext add payment_id int(10);
update billing_on_ext b inner join billing_on_payment p on b.billing_no=p.billing_no set b.payment_id=p.payment_id;
update billing_on_payment p inner join billing_on_ext b on b.billing_no=p.billing_no set p.pay_date=b.value where b.key_val='payDate';
