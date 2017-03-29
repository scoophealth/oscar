ALTER TABLE LookupList ADD `listTitle` varchar(50) AFTER `name`;
INSERT INTO `LookupList` (`listTitle`,`name`, description, categoryId, active, createdBy, dateCreated) VALUES('Consultation Request Appointment Instructions List', 'consultApptInst', 'Select list for the consultation appointment instruction select list', NULL, '1', 'oscar', NOW() );
INSERT INTO `LookupListItem` (lookupListId, value, label, displayOrder, active, createdBy, dateCreated)( 
SELECT id, UUID(), 'Please reply to sending facility by fax or phone with appointment','1', '1','oscar', NOW() FROM `LookupList` WHERE `name` = "consultApptInst" );

ALTER TABLE consultationRequests ADD appointmentInstructions varchar (256) AFTER `urgency`;
