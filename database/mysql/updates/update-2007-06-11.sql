#allowing longer pins (used as indivo id by the indivo project)

ALTER TABLE demographic CHANGE pin pin varchar(255) DEFAULT NULL;