#Required for the updated reportByTemplate module
#first three lines change the varchar id to int + auto_increment
#the last line renames a column

ALTER TABLE reportTemplates DROP PRIMARY KEY;
UPDATE reportTemplates SET templateid='intermediate';
ALTER TABLE reportTemplates CHANGE templateid templateid int(11) NOT NULL auto_increment, ADD PRIMARY KEY(templateid);
ALTER TABLE reportTemplates CHANGE templateparamxml templatexml text NOT NULL DEFAULT '';