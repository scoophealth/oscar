ALTER TABLE `issue` ADD INDEX `description_index`(`description`(20));
ALTER TABLE `hl7TextInfo` ADD INDEX `labno_index`(`lab_no`);
ALTER TABLE `providerLabRouting` ADD INDEX `labno_index`(`lab_no`);
