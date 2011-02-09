# Let users set their personal default billing form
alter table preference add column default_servicetype varchar(10) default NULL;

