alter table issue add archived tinyint(1) not null;
update issue set archived=0;

