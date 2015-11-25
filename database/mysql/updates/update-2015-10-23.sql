# Add the fields for the dispensing units
ALTER TABLE drugs ADD COLUMN `dispensingUnits` VARCHAR(20) AFTER `quantity`;
ALTER TABLE favorites ADD COLUMN `dispensingUnits` VARCHAR(20) AFTER `quantity`;
