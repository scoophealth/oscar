#this could fail if you already have it, just ignore in that case.
alter table billingservice add gstFlag tinyint(1) NOT NULL default 0;

