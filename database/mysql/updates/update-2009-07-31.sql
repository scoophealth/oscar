-- After this update migrate the data by logging in and going to oscar_mcmaster/billing/CA/BC/wcbMigrate.jsp  (Change oscar_mcmaster for your context name)

alter table billingmaster add column wcb_id int(10) default NULL;
create index billingmaster_wcb_id on billingmaster(wcb_id);