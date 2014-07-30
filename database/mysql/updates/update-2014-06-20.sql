ALTER TABLE `appointment_status` 
	ADD COLUMN `short_letter_colour` INT(11) NULL COMMENT 'The colour of the short letters in the system'  AFTER `editable` , 
	ADD COLUMN `short_letters` VARCHAR(5) NULL COMMENT 'The short letter representation of the appointment status'  AFTER `short_letter_colour` ;
