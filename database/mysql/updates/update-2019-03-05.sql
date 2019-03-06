alter table validations modify regularExp varchar(250);

INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) VALUES
('Provided/Revised/Reviewed', 'Provided|Revised|Reviewed', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('Mild/Moderate/Severe/Very Severe', 'Mild|Moderate|Severe|Very Severe', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('Yes/Not Applicable', 'Yes|Not Applicable', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
('Yes', 'Yes', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) VALUES ('Integer: 0 to 7', NULL, 7, 0, 1, NULL, NULL, NULL, NULL);
INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) values ('NYHA Class I-IV', 'Class I - no symptoms|Class II - symptoms with ordinary activity|Class III - symptoms with less than ordinary activity|Class IV - symptoms at rest', NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `validations` (`name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) values ('COPD Classification', 'Mild: FEV1 >= 80% predicted|Moderate:50% <= FEV1 < 80% predicted|Severe:30% <= FEV1 < 50% predicted|Very Severe : FEV1 < 30% predicted', NULL, NULL, NULL, NULL, NULL, NULL, NULL);


insert into measurementType (type, typeDisplayName, typeDescription, measuringInstruction, createDate, validation) values
('UMS', 'Urinary Microalbumin Screen', 'Urinary Microalbumin Screen', 'Records the value of the Urinary Microalbumin test: mg/L', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1BF', 'FEV1 (before puff)', 'FEV1 (before puff)', 'Forced Expiratory Volume: the volume of air that has been exhaled by the patient at the end of the first second of forced expiration', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FVCBF', 'FVC (before puff)', 'FVC (before puff)', 'Forced Vital Capacity: the volume of air that has been forcibly and maximally exhaled out by the patient until no more can be expired', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PCBF', 'FEV1% (before puff)', 'FEV1% (before puff)', 'The ratio of FEV1 to FVC calculated for the patient', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PRE', 'FEV1 predicted', 'FEV1 predicted', 'The FEV1 calculated in the population with similar characteristics (e.g. height, age, sex, race, weight, etc.)', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FVCPRE', 'FVC predicted', 'FVC predicted', 'Forced Vital Capacity predicted: calculated in the population with similar characteristics (height, age, sex, and sometimes race and weight)', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PCPRE', 'FEV1% predicted', 'FEV1% predicted', 'The ratio of FEV1 predicted to FVC predicted, calculated in the population with similar characteristics (height, age, sex, and sometimes race and weight)', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PCOFPREBF', 'FEV1% of predicted (before puff)', 'FEV1% of predicted (before puff)', 'FEV1% (before puff) of the patient divided by the average FEV1% predicted in the population with similar characteristics (e.g. height, age, sex, race, weight, etc.)', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FVCRTBF', 'FVC ratio (before puff)', 'FVC ratio (before puff)', 'FVC actual (before puff) / FVC predicted', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1FVCRTBF', 'FEV1 / FVC ratio (before puff)', 'FEV1 / FVC ratio (before puff)', 'FEV1 / FVC (before puff) actual divided by FEV1 / FVC predicted', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('PEFRBF', 'PEF personal (before puff)', 'PEF personal (before puff)', 'Peak Expiratory Flow: the maximal flow (or speed) achieved during the maximally forced expiration initiated at full inspiration', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1AFT', 'FEV1 (after puff)', 'FEV1 (after puff)', 'Forced Expiratory Volume: the volume of air that has been exhaled by the patient at the end of the first second of forced expiration', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FVCAFT', 'FVC (after puff)', 'FVC (after puff)', 'Forced Vital Capacity: the volume of air that has been forcibly and maximally exhaled out by the patient until no more can be expired', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PCAFT', 'FEV1% (after puff)', 'FEV1% (after puff)', 'The ratio of FEV1 to FVC calculated for the patient', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1PCOFPREAFT', 'FEV1% of predicted (after puff)', 'FEV1% of predicted (after puff)', 'FEV1% (after puff) of the patient divided by the average FEV1% predicted in the population with similar characteristics (e.g. height, age, sex, race, weight, etc.)', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FVCRTAFT', 'FVC ratio (after puff)', 'FVC ratio (after puff)', 'FVC actual (after puff) / FVC predicted', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('FEV1FVCRTAFT', 'FEV1 / FVC ratio (after puff)', 'FEV1 / FVC ratio (after puff)', 'FEV1 / FVC (after puff) actual divided by FEV1 / FVC predicted', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('PEFRAFT', 'PEF personal (after puff)', 'PEF personal (after puff)', 'Peak Expiratory Flow: the maximal flow (or speed) achieved during the maximally forced expiration initiated at full inspiration', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('ANELV', 'Asthma: # Of Exacerbations Requiring Clinical Evaluation since last assessment', 'Asthma: # Of Exacerbations Requiring Clinical Evaluation since last assessment', 'The number of exacerbations since the last assessment requiring clinical evaluations reported by the patient', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('CNOLE', 'COPD: # of Exacerbations since last assessment', 'COPD: # of Exacerbations since last assessment', 'The number of exacerbations due to COPD since last visit, as reported by the patient', now(),
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
('WHE', 'Wheezing', 'Wheezing', 'Records whether the patient is wheezing or not', now(),
(select id from validations where name='Yes/No' limit 1)),
('HFMR', 'HF Medication Review', 'Heart Failure Medication Review', 'Records whether medication adherence for Heart Failure purpose has been discussed with the patient', now(),
(select id from validations where name='Yes/No' limit 1));


INSERT INTO measurementType (type, typeDisplayName, typeDescription, measuringInstruction, createDate, validation) values
( 'ASWAN', 'Asthma # of School Work Absence', 'Asthma # of School Work Absence', 'Numeric Value greater than or equal to 0', '2018-10-01 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSFT', 'Heart Failure Symptom: Fatigue', 'Heart Failure Symptom: Fatigue', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSDZ', 'Heart Failure Symptom: Dizziness', 'Heart Failure Symptom: Dizziness', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSSC', 'Heart Failure Symptom: Syncope', 'Heart Failure Symptom: Syncope', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSDE', 'Heart Failure Symptom: Dyspnea on Exertion', 'Heart Failure Symptom: Dyspnea on Exertion', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSDR', 'Heart Failure Symptom: Dyspnea at Rest', 'Heart Failure Symptom: Dyspnea at Rest', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSON', 'Heart Failure Symptom: Orthopnea', 'Heart Failure Symptom: Orthopnea', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'HFSDP', 'Heart Failure Symptom: Paroxysmal Nocturnal Dyspnea', 'Heart Failure Symptom: Paroxysmal Nocturnal Dyspnea', 'Frequency/week', '2018-10-18 00:00:00',
(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)),
( 'SPIRT', 'Spirometry Test', 'Spirometry Test', 'Yes or none', '2018-10-18 00:00:00',
(select id from validations where name='Yes' limit 1)),
( 'COPDC', 'COPD Classification', 'COPD Classification', 'Mild/Moderate/Severe/Very Severe', '2018-10-18 00:00:00',
(select id from validations where name='Mild/Moderate/Severe/Very Severe' limit 1)),
( 'RABG2', 'Recommend ABG', 'Recommend ABG', 'Yes/Not Applicable', '2018-10-18 00:00:00',
(select id from validations where name='Yes/Not Applicable' limit 1)),
( 'EPR2', 'Exacerbation plan in place', 'Exacerbation plan in place', 'Provided/Revised/Reviewed', '2018-10-18 00:00:00',
(select id from validations where name='Provided/Revised/Reviewed' limit 1));


INSERT INTO measurementType (type, typeDisplayName, typeDescription, measuringInstruction, createDate, validation) values
( 'CODPW', 'Cough (days/week)', 'Cough (days/week)', 'days/week', '2018-10-31 00:00:00',
(select id from validations where name='Integer: 0 to 7' limit 1)),
( 'CTDPW', 'Chest tightness (days/week)', 'Chest tightness (days/week)', 'days/week', '2018-10-31 00:00:00',
(select id from validations where name='Integer: 0 to 7' limit 1)),
( 'DYDPW', 'Dyspnea (days/week)', 'Dyspnea (days/week)', 'days/week', '2018-10-31 00:00:00',
(select id from validations where name='Integer: 0 to 7' limit 1)),
( 'WHDPW', 'Wheeze (days/week)', 'Wheeze (days/week)', 'days/week', '2018-10-31 00:00:00',
(select id from validations where name='Integer: 0 to 7' limit 1));


update measurementType set measuringInstruction='Provided/Revised/Reviewed', validation=
(select id from validations where name='Provided/Revised/Reviewed' limit 1)
where type='AACP';

update measurementType set measuringInstruction='NYHA Class I-IV', validation=
(select id from validations where name='NYHA Class I-IV' limit 1)
where type='NYHA';

update measurementType set measuringInstruction='COPD Classification', validation=
(select id from validations where name='COPD Classification' limit 1)
where type='COPDC';

update measurementType set type='ACOSY', typeDisplayName='Cough', typeDescription='Cough', measuringInstruction='frequency/week',
validation=(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)
where type='CODPW';

update measurementType set type='ACTSY', typeDisplayName='Chest tightness', typeDescription='Chest tightness',
measuringInstruction='frequency/week', 
validation=(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)
where type='CTDPW';

update measurementType set type='ADYSY', typeDisplayName='Dyspnea', typeDescription='Dyspnea',
measuringInstruction='frequency/week', 
validation=(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)
where type='DYDPW';

update measurementType set type='AWHSY', typeDisplayName='Wheeze', typeDescription='Wheeze', measuringInstruction='frequency/week', 
validation=(select id from validations where name='Numeric Value greater than or equal to 0' limit 1)
where type='WHDPW';

update measurementType set typeDisplayName='Oxygen Saturation' where type='02';
update measurementType set typeDescription='' where type='CODC';

