alter table tickler_update modify status char(1);
alter table tickler_update add column assignedTo varchar(6);
alter table tickler_update add column serviceDate datetime;
alter table tickler_update add column priority varchar(6);
