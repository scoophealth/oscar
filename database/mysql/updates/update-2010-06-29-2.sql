alter table gstControl add column id int auto_increment primary key;
alter table billingperclimit drop primary key;
alter table billingperclimit add column id integer auto_increment primary key;
