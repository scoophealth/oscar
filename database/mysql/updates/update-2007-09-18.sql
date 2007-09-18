CREATE TABLE demographic_merged(
    id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    demographic_no INT(10) NOT NULL,
    merged_to INT(10) NOT NULL,
    deleted INT(1) NOT NULL DEFAULT 0
);

CREATE INDEX dem_merged ON demographic_merged (demographic_no, merged_to, deleted);
CREATE INDEX dem_merged_dem ON demographic_merged (demographic_no, deleted);
CREATE INDEX dem_merged_merge ON demographic_merged (merged_to, deleted);