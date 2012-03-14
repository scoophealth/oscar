alter table billingreferral change column referral_no referral_no varchar(6) not null;
alter table billingreferral drop index referral_no_2;
