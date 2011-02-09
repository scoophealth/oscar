#Modify rschedule and scheduledate so we can mark schedules as archived rather than physically delete them

alter table rschedule drop primary key;
alter table rschedule add column id int(6) NOT NULL auto_increment primary key;
alter table rschedule add column status char(1) NOT NULL;

alter table scheduledate drop primary key;
alter table scheduledate add column id int(6) NOT NULL auto_increment primary key;
alter table scheduledate add column status char(1) NOT NULL;

#Activate all current schedules

update rschedule set status = 'A';
update scheduledate set status = 'A';