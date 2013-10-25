INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'IBPL', 'Income below poverty line', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FAS', 'Folic Acid supplementation', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'COGA', 'Cognitive Assessment', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'HPNP', 'Hearing protection/Noise control programs', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SBLT', 'Seat belts', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SDET', 'Smoke detector that works', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'PWC', 'Parents with children', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SUNP', 'Sun protection', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'BTFP', 'Brush teeth with fluoride toothpaste', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FLOS', 'Floss', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'DILY', 'Dentist in the last year', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00');

 update issue set type = 'icd9' where length(type) = 0 or type is null;
 
 update issue set type = 'system' where code = 'OMeds' or code = 'SocHistory' or code = 'MedHistory' or code = 'Concerns' or code = 'Reminders' or code = 'FamHistory';
