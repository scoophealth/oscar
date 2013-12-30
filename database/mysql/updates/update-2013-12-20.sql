/* Updates for PHV */ 
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ECHK', 'Do you have your eyes regularly checked?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'HCON', 'Do you have any hearing concerns?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'PCPR', 'Poison control prevention', 'NULL', 'Review', '16', '2013-12-20 13:00:00'),
( 'NFSW', 'Non-flammable sleepwear', 'NULL', 'Review', '16', '2013-12-20 13:00:00'),
( 'HOTW', 'Hot water thermostat <54deg. C', 'NULL', 'Review', '16', '2013-12-20 13:00:00'),
( 'FAMR', 'Family/Relationships', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'WKED', 'Work/Education', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SDUS', 'Street Drug Use', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SHAB', 'Sleep Habits', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SEXH', 'Sexual History', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'PXAM', 'Physical Exam', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'FUPP', 'Assessment/Follow-up plans', 'NULL', 'Review', '16', '2013-12-30 13:00:00');

/*this was added by PHV and requested to delete by PHV*/
delete from measurementType where type='FLOS';

update measurementType set type='BTFT' where type='BTFP';
update measurementType set typeDisplayName = 'Brush teeth with fluoride toothpaste and floss' where type = 'BTFT';

INSERT INTO `validations` (`name`, `regularExp`, `maxLength` , `isNumeric`, `isDate` ) VALUES ('Review', 'REVIEWED|reviewed|Reviewed', 0, 0, 0);