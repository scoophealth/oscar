ALTER TABLE studydata ADD COLUMN deleted tinyint not null;
update studydata set deleted = 0;