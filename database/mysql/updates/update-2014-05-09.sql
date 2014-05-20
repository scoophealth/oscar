ALTER TABLE `measurementType` ADD UNIQUE INDEX `type_instruction`(`type`, `measuringInstruction`);


INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ABO', 'Blood Group', 'ABO RhD blood type group', 'Blood Type', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AFP', 'AFP', 'Alpha Fetoprotein', 'ug/L Range under 7', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALB', 'Albumin', 'Serum Albumin', 'g/L Range 35-50', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALP', 'ALP', 'Alkaline Phosphatase', 'U/L Range 50-300', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ANA', 'ANA', 'Antinuclear Antibodies', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'APOB', 'APO B', 'Apolipoprotein B', 'g/L Range 0.5-1.2', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BILI', 'Bilirubin', 'Total Bilirubin', 'umol/L Range under 20', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BUN', 'BUN', 'Blood Urea Nitrogen', 'mmol/L Range 2-9', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Clpl', 'Chloride', 'Chloride', 'mmol/L Range 98-106', '5', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CK', 'CK', 'Creatinine Kinase', 'U/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CRP', 'CRP', 'C reactive protein', 'mg/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CA', 'Calcium', 'Calcium', 'mmol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C125', 'CA 125', 'CA 125', 'kU/L Range under 36', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C153', 'CA 15-3', 'CA 15-3', 'kU/L Range under 23', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C199', 'CA 19-9', 'CA 19-9', 'kU/L Range under 27', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEA', 'CEA', 'CEA', 'umol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CHLM', 'CHLM', 'Chlamydia', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'C3', 'C3', 'Complement component 3', 'umol/L', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CMBS', 'Coombs', 'Coombs', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIG', 'Digoxin', 'Digoxin Level', 'nmol/L Range 1-2.6', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIL', 'Dilantin', 'Dilantin (Phenytoin) level', 'umol/L Range 40-80', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ENA', 'ENA', 'Extractable Nuclear Antigens', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ESR', 'ESR', 'Erythrocyte sedimentation rate', 'mm/h Range under 20', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Fer', 'Ferritin', 'Ferritin', 'ug/L Range 15-180', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FIT', 'FIT', 'Fecal Immunochemical Test', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FOBT', 'FOBT', 'Fecal Occult Blood', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FT3', 'FT3', 'Free T3', 'pmol/L Range 4-8', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FT4', 'FT4', 'Free T4', 'pmol/L Range 11-22', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GBS', 'GBS', 'Group B Strep', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GC', 'Gonococcus', 'Gonococcus', 'test result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GGT', 'GGT', 'Gamma-glutamyl transferase', 'U/L Range 10-58', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GCT', '50g Glucose Challenge', '1h 50g Glucose Challenge', 'mmol/L Range under 7.8', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GT1', '75g OGTT 1h', '1h 75g Glucose Tolerance Test', 'mmol/L Range under 10.6', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'GT2', '75g OGTT 2h', '2h 75g Glucose Tolerance Test', 'mmol/L Range under 9', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HCO3', 'Bicarbonate', 'Bicarbonate', 'mmol/L Range 20-29', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBEB', 'AntiHBeAg', 'AntiHBeAg', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBEG', 'HBeAg', 'HBeAg', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HBVD', 'HBV DNA', 'HBV DNA', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPYL', 'H Pylori', 'H Pylori', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LITH', 'Lithium', 'Lithium', 'mmol/L Range 0.6-0.8', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MG', 'Mg', 'Magnesium', 'mmol/L Range 0.7-1.2', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCV', 'MCV', 'Mean corpuscular volume', 'fL Range 82-98', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PB19', 'Parvovirus', 'Parvovirus B19', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PHOS', 'Phosphate', 'Phosphate', 'mmol/L Range 0.8-1.4', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PLT', 'Platelets', 'Platelets', 'x10 9/L Range 150-400', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PROT', 'Protein', 'Total Protein Serum', 'g/L Range 60-80', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PSA', 'PSA', 'Prostatic specific antigen', 'ug/L Range under 5', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iPTH', 'iPTH', 'intact Parathyroid Hormone', 'pmol/L Range 1-6', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RF', 'RF', 'Rheumatoid Factor', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Rh', 'Rh', 'RhD blood type group', 'result', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RUB', 'Rubella', 'Rubella titre', 'titre', '11', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TSAT', 'Transferrin Saturation', 'Transferrin Saturation', 'percent Range 20-50', '4', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'URIC', 'Uric Acid', 'Uric Acid', 'umol/L Range 230-530', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'VZV', 'Zoster', 'Varicella Zoster', 'result', '17', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=17;
INSERT INTO`measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'WBC', 'WBC', 'White Cell Count', 'x10 9/L Range 4-11', '14', '2014-05-09 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;


ALTER TABLE `measurementType` DROP INDEX `type_instruction`;

INSERT INTO`validations` (`id`, `name`, `regularExp`) VALUES 
(17,'pos or neg', 'pos|neg|positive|negative') ON DUPLICATE KEY UPDATE `name`='pos or neg', `regularExp`= 'pos|neg|positive|negative';



