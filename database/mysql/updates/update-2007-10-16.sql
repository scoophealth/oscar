ALTER TABLE `formConsult` ADD COLUMN t_name2 varchar(60) DEFAULT NULL AFTER `t_name`;
ALTER TABLE `formConsult` ADD COLUMN consultTime date DEFAULT NULL;
UPDATE `formConsult` set consultTime = formCreated;

ALTER TABLE scheduletemplate MODIFY COLUMN timecode text;
ALTER TABLE demographicExt MODIFY COLUMN key_val varchar(20);