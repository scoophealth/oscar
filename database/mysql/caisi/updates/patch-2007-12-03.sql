alter table program add column (ageMin int not null, ageMax int not null);
update program set ageMin=1;
update program set ageMax=200;

ALTER TABLE program ADD COLUMN maximum_restriction_days tinyint(11) default NULL;
ALTER TABLE program ADD COLUMN default_restriction_days tinyint(11) not null default 30;
