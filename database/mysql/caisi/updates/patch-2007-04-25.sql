ALTER TABLE `agency` DROP COLUMN `intakes_combined`;
ALTER TABLE `agency` ADD COLUMN `intake_quick_state` CHAR(2) NOT NULL DEFAULT 'HS' AFTER `intake_quick`;
