INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) VALUES
('Yes/No/Maybe', 'YES|yes|Yes|Y|NO|no|No|N|MAYBE|maybe|Maybe', NULL, NULL, NULL, NULL, NULL, NULL, NULL);

insert into encounterForm (`form_name`, `form_value`, `form_table`, `hidden`) values('Patient Encounter Worksheet','../form/patientEncounterWorksheet.jsp?demographic_no=','',0);
