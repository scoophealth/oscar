# Add the fields for the dispensing units
ALTER TABLE oscar_mcmaster.drugs ADD COLUMN `dispensingUnits` VARCHAR(20) AFTER `quantity`;
ALTER TABLE oscar_mcmaster.favorites ADD COLUMN `dispensingUnits` VARCHAR(20) AFTER `quantity`;