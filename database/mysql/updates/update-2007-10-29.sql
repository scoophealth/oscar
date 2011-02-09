ALTER TABLE demographic ADD COLUMN alias varchar(70) default NULL;
ALTER TABLE demographic ADD COLUMN previousAddress varchar(255) default NULL;
ALTER TABLE demographic ADD COLUMN children varchar(255) default NULL;
ALTER TABLE demographic ADD COLUMN sourceOfIncome varchar(255) default NULL;
ALTER TABLE demographic ADD COLUMN citizenship varchar(40) default NULL;
ALTER TABLE demographic ADD COLUMN sin varchar(15) default NULL;