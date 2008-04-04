--Change primary key of hash audit table
alter table hash_audit drop primary key;
alter table hash_audit add column pkid int(10) auto_increment primary key first;
