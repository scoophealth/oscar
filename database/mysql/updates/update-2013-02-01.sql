--
-- Merge legacy tables `measurementType` and `validations` into source
-- Works from any prior to new merge

DROP TABLE IF EXISTS measurementTypeTEMP;
CREATE TABLE measurementTypeTEMP (
  id int UNSIGNED AUTO_INCREMENT,
  type varchar(4) NOT NULL,
  typeDisplayName varchar(255) NOT NULL,
  typeDescription varchar(255) NOT NULL,
  measuringInstruction varchar(255) NOT NULL,
  validation varchar(100) NOT NULL,
  createDate datetime not null,
  PRIMARY KEY(id)
);

-- 248 rows of unique measuring types from various legacy sources, and then cleaned 

INSERT INTO `measurementTypeTEMP` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '02', 'Oxygen Saturation', 'Oxygen Saturation', 'percent', '4', '2013-02-01 00:00:00'),
( '24UA', '24 hour urine albumin', '24 hour urine albumin', 'mg/24h (nnn.n) Range:0-500 Interval:12mo.', '14', '2013-02-01 00:00:00'),
( '24UR', '24-hr Urine cr clearance & albuminuria', 'Renal 24-hr Urine cr clearance & albuminuria', 'q 6-12 months, unit mg', '3', '2013-02-01 00:00:00'),
( '5DAA', '5 Day Adherence if on ART', '5 Day Adherence if on ART', 'number', '4', '2013-02-01 00:00:00'),
( 'A1C', 'A1C', 'A1C', 'Range:0.040-0.200', '3', '2013-02-01 00:00:00'),
( 'AACP', 'Asthma Action Plan ', 'Asthma Action Plan ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ACOS', 'Asthma Coping Strategies', 'Asthma Coping Strategies', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ACR', 'Alb creat ratio', 'ACR', 'in mg/mmol', '5', '2013-02-01 00:00:00'),
( 'ACS', 'Acute Conronary Syndrome', 'Acute Conronary Syndrome', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AEDR', 'Asthma Education Referral', 'Asthma Education Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AELV', 'Exacerbations since last visit requiring clincal evaluation', 'Exacerbations since last visit requiring clincal evaluation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AENC', 'Asthma Environmental Control', 'Asthma Environmental Control', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AHGM', 'Anit-hypoglycemic Medication', 'Anit-hypoglycemic Medication', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AIDU', 'Active Intravenous Drug Use', 'Active Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ALB', 'Albumin', 'Albumin', 'in g per litre', '5', '2013-02-01 00:00:00'),
( 'ALP', 'Alk Phos', 'Alk Phosphatase', 'in units per litre', '5', '2013-02-01 00:00:00'),
( 'BILC', 'Bili conj', 'Conjugated Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00'),
( 'BILT', 'Bilirubin', 'Total Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00'),
( 'BILU', 'Bili unconj', 'Unconjugated Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00'),
( 'CK', 'CK', 'Creatinine Kinase', 'in units per litre', '14', '2013-02-01 00:00:00'),
( 'Clpl', 'Cl', 'Plasma Chloride', 'in mmol per litre', '5', '2013-02-01 00:00:00'),
( 'UA', 'Urate', 'Uric Acid', 'in mmol per litre', '5', '2013-02-01 00:00:00'),
( 'ALC', 'Alcohol', 'Alcohol', 'Yes/No/X', '12', '2013-02-01 00:00:00'),
( 'ALPA', 'Asthma Limits Physical Activity', 'Asthma Limits Physical Activity', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ALT', 'ALT', 'ALT', 'in U/L', '5', '2013-02-01 00:00:00'),
( 'Ang', 'Angina', 'Angina', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ANR', 'Asthma Needs Reliever   ', 'Asthma Needs Reliever   ', 'frequency per week', '14', '2013-02-01 00:00:00'),
( 'ANSY', 'Asthma Night Time Symtoms', 'Asthma Night Time Symtoms', 'frequency per week', '14', '2013-02-01 00:00:00'),
( 'AORA', 'ACE-I OR ARB', 'ACE-I OR ARB', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ARAD', 'Review Asthma Definition', 'Review Asthma Definition', 'Review Asthma Definition', '7', '2013-02-01 00:00:00'),
( 'ARDT', 'Asthma  Review Device Technique optimal', 'Asthma  Review Device Technique optimal', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ARMA', 'Asthma Review Med Adherence', 'Asthma Review Med Adherence', 'Asthma Review Med Adherence', '7', '2013-02-01 00:00:00'),
( 'ASAU', 'ASA Use', 'ASA Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ASPR', 'Asthma Specialist Referral', 'Asthma Specialist Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AST', 'AST', 'AST', 'in U/L', '4', '2013-02-01 00:00:00'),
( 'ASTA', 'Asthma Trigger Avoidance', 'Asthma Trigger Avoidance', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ASWA', 'Asthma Absence School Work', 'Asthma Absence School Work', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ASYM', 'Asthma Symptoms', 'Asthma Symptoms', 'frequency per week', '14', '2013-02-01 00:00:00'),
( 'BCTR', 'Birth Control', 'Birth Control', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'BG', 'Blood Glucose', 'Blood Glucose', 'in mmol/L (nn.n) Range:1.5-30.0', '7', '2013-02-01 00:00:00'),
( 'BMED', 'Blood Pressure Medication Changes', 'BP Med Changes', 'Changed', '7', '2013-02-01 00:00:00'),
( 'BMI', 'Body Mass Index', 'BMI', 'BMI', '4', '2013-02-01 00:00:00'),
( 'CASA', 'Consider ASA', 'Consider ASA', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CD4', 'CD4', 'CD4', 'in x10e9/l', '14', '2013-02-01 00:00:00'),
( 'CD4P', 'CD4 Percent', 'CD4 Percent', 'in %', '4', '2013-02-01 00:00:00'),
( 'CDMP', 'Attended CDM Self Management Program', 'Attended CDM Self Management Program', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDE', 'Education Exercise', 'Education Exercise', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDM', 'Education Patient Meds', 'Education Patient Meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDS', 'Education Salt fluid ', 'Education Salt fluid ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDW', 'Education Daily Weight Monitoring', 'Education Daily Weight Monitoring', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CERV', 'ER visits for HF', 'ER visits for HF', 'integer', '2', '2013-02-01 00:00:00'),
( 'CGSD', 'Collaborative Goal Setting', 'Collaborative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CIMF', 'Child Immunization recall', 'Child Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'CMVI', 'CMV IgG', 'CMV IgG', 'Positive', '7', '2013-02-01 00:00:00'),
( 'CODC', 'COD Classification', 'COD Classification', 'null', '11', '2013-02-01 00:00:00'),
( 'COPE', 'Provide COP Education Materials ', 'Provide COP Education Materials ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COPM', 'Review COP Med use and Side effects', 'Review COP Med use and Side effects', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COPS', 'COP Specialist Referral', 'COP Specialist Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COUM', 'Warfarin Weekly Dose', 'WarfarinDose', 'Total mg Warfarin per week', '5', '2013-02-01 00:00:00'),
( 'CRCL', 'Creatinine Clearance', 'Creatinine Clearance', 'in ml/h', '5', '2013-02-01 00:00:00'),
( 'CVD', 'CVD', 'Cerebrovascular disease', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CXR', 'CXR', 'CXR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DEPR', 'Depression', 'Depression', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DESM', 'Dental Exam Every 6 Months', 'Dental Exam Every 6 Months', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DiaC', 'Diabetes Counseling Given', 'Diabetes Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIER', 'Diet and Exercise', 'Diet and Exercise', 'Reviewed', '7', '2013-02-01 00:00:00'),
( 'DIFB', 'Impaired FB', 'Impaired FB', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIGT', 'Impaired GT', 'Impaired Glucose Tolerance', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DM', 'DM', 'Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DMME', 'Diabetes Education', 'Diabetes Education', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'DMSM', 'Diabetes Self Management Goals', 'Diabetes Self Management Goals', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'DOLE', 'Date of last Exacerbation', 'Date of last Exacerbation', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00'),
( 'DpSc', 'Depression Screen', 'Feeling Sad, blue or depressed for 2 weeks or more', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DRCO', 'Drug Coverage', 'Drug Coverage', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DRPW', 'Drinks per Week', 'Drinks per Week', 'Number of Drinks per week', '5', '2013-02-01 00:00:00'),
( 'DT1', 'Type I', 'Diabetes Type 1', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DT2', 'Type II', 'Diabetes Type 2', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DTYP', 'Diabetes Type', 'Diabetes Type', '1 or 2', '10', '2013-02-01 00:00:00'),
( 'ECG', 'ECG', 'ECG', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDC', 'EDC', 'Expected Date of Confinement', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00'),
( 'EDDD', 'Education Diabetes', 'Education Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDF', 'EDF', 'Erectile Dysfunction', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDGI', 'Autonomic Neuropathy', 'Autonomic Neuropathy', 'Present', '7', '2013-02-01 00:00:00'),
( 'EDND', 'Education Nutrition Diabetes', 'Education Nutrition Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDNL', 'Education Nutrition Lipids', 'Education Nutrition Lipids', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EGFR', 'EGFR', 'EGFR', 'in ml/min', '5', '2013-02-01 00:00:00'),
( 'EPR', 'Exacerbation plan in place or reviewed', 'Exacerbation plan in place or reviewed', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EXE', 'Exercise', 'Exercise', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ExeC', 'Exercise Counseling Given', 'Exercise Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Exer', 'Exercise', 'Exercise', '[min/week 0-1200]', '14', '2013-02-01 00:00:00'),
( 'EYEE', 'Dilated Eye Exam', 'Eye Exam', 'Exam Done', '7', '2013-02-01 00:00:00'),
( 'FAHS', 'Risk of Falling', 'Risk of Falling', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FBPC', '2 hr PC BG', '2 hr PC BG', 'in mmol/L', '3', '2013-02-01 00:00:00'),
( 'FBS', 'FBS', 'Glucose FBS', 'FBS', '3', '2013-02-01 00:00:00'),
( 'FEV1', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', '14', '2013-02-01 00:00:00'),
( 'FGLC', 'Fasting Glucose meter , lab comparison', 'Fasting glucose meter, lab comparison', 'Within 20 percent', '7', '2013-02-01 00:00:00'),
( 'FICO', 'Financial Concerns', 'Financial Concerns', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FLUF', 'Flu Recall', 'Flu Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'FOBF', 'FOBT prevention recall', 'FOBT Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'FRAM', 'Framingham 10 year CAD', 'Framingham 10 year CAD', 'percent', '11', '2013-02-01 00:00:00'),
( 'FTE', 'Foot Exam', 'Foot Exam', 'Normal', '7', '2013-02-01 00:00:00'),
( 'FTEx', 'Foot Exam: Significant Pathology', 'Significant Pathology', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTIn', 'Foot Exam: Infection', 'Infection', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTIs', 'Foot Exam: Ischemia', 'Ischemia', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTLS', 'Foot Exam  Test loss of Sensation', 'Foot Exam  Loss of Sensation', 'Normal', '7', '2013-02-01 00:00:00'),
( 'FTNe', 'Foot Exam: Neuropathy', 'Neuropathy', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTOt', 'Foot Exam: Other Vascular abnomality', 'Other Vascular abnomality', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTRe', 'Foot Exam: Referral made', 'Referral made', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FTST', 'Free Testost', 'Free Testost', 'in nmol/L', '14', '2013-02-01 00:00:00'),
( 'FTUl', 'Foot Exam: Ulcer', 'Ulcer', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'G', 'Gravida', 'Gravida', 'Gravida', '3', '2013-02-01 00:00:00'),
( 'G6PD', 'G6PD', 'G6PD', 'Positive', '7', '2013-02-01 00:00:00'),
( 'Hb', 'Hb', 'Hb', 'in g/L', '5', '2013-02-01 00:00:00'),
( 'Hchl', 'Hypercholesterolemia', 'Hypercholesterolemia', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HDL', 'HDL', 'High Density Lipid', 'in mmol/L (n.n) Range:0.4-4.0', '2', '2013-02-01 00:00:00'),
( 'HEAD', 'Head circumference', 'Head circumference', 'in cm (nnn) Range:30-70 Interval:2mo.', '4', '2013-02-01 00:00:00'),
( 'HIP', 'Hip Circ.', 'Hip Circumference', 'at 2 cm above navel', '14', '2013-02-01 00:00:00'),
( 'HFCG', 'HF Collaorative Goal Setting', 'HF Collaorative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFCS', 'HF Self Management Challenge', 'HF Self Management Challenge', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMD', 'HF Mod Risk Factor Diabetes', 'HF Mod Risk Factor Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMH', 'HF Mod Risk Factor Hyperlipidemia', 'HF Mod Risk Factor Hyperlipidemia', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMO', 'HF Mod Risk Factor Overweight', 'HF Mod Risk Factor Overweight', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMS', 'HF Mod Risk Factor Smoking', 'HF Mod Risk Factor Smoking', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMT', 'HF Mod Risk Factor Hypertension', 'HF Mod Risk Factor Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HIVG', 'HIV genotype', 'HIV genotype', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HLA', 'HLA B5701', 'HLA B5701', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HpAI', 'Hep A IgG', 'Hep A IgG', 'Positive', '7', '2013-02-01 00:00:00'),
( 'HpBA', 'Hep BS Ab', 'Hep BS Ab', 'Positive', '7', '2013-02-01 00:00:00'),
( 'HPBC', 'Hep B CAb', 'Hep B CAb', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HPBP', 'Hep B PCR', 'Hep B PCR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HpBS', 'Hep BS Ag', 'Hep BS Ag', 'Positive', '7', '2013-02-01 00:00:00'),
( 'HpCA', 'Hep C Ab', 'Hep C Ab', 'Positive', '7', '2013-02-01 00:00:00'),
( 'HPCG', 'Hep C Genotype', 'Hep C Genotype', 'integer Range 1-7', '2', '2013-02-01 00:00:00'),
( 'HPCP', 'Hep C PCR', 'Hep C PCR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HRMS', 'Review med use and side effects', 'HTN Review of Medication use and side effects', 'null', '11', '2013-02-01 00:00:00'),
( 'HSMC', 'Self Management Challenges', 'HTN Self Management Challenges', 'null', '11', '2013-02-01 00:00:00'),
( 'HSMG', 'Self Management Goal', 'HTN Self Management Goal', 'null', '11', '2013-02-01 00:00:00'),
( 'HT', 'HT', 'Height', 'in cm', '5', '2013-02-01 00:00:00'),
( 'HTN', 'HTN', 'Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HYPE', 'Hypoglycemic Episodes', 'Number of Hypoglycemic Episodes', 'since last visit', '3', '2013-02-01 00:00:00'),
( 'HYPM', 'Hypoglycemic Management', 'Hypoglycemic Management', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'IART', 'Currently On ART', 'Currently On ART', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iDia', 'Eye Exam: Diabetic Retinopathy', 'Diabetic Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iEx', 'Eye Exam: Significant Pathology', 'Significant Pathology', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iHyp', 'Eye Exam: Hypertensive Retinopathy', 'Hypertensive Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'INR', 'INR', 'INR', 'INR Blood Work', '5', '2013-02-01 00:00:00'),
( 'INSL', 'Insulin', 'Insulin', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iOth', 'Eye Exam: Other Vascular Abnomality', 'Other Vascular Abnormality', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iRef', 'Eye Exam: Refferal Made', 'Refferal Made', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'JVPE', 'JPV Elevation', 'JPV Elevation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Kpl', 'Potassium', 'Potassium', 'in mmol/L', '2', '2013-02-01 00:00:00'),
( 'LcCt', 'Locus of Control Screen', 'Feeling lack of control over daily life', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LDL', 'LDL', 'Low Density Lipid', 'monitor every 1-3 year', '2', '2013-02-01 00:00:00'),
( 'LEFP', 'LEFS Pain', 'Lower Extremity Functional Scale - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'LETH', 'Lethargy', 'Lethargic', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LHAD', 'Lung Related Hospital Admission', 'Lung Related Hospital Admission', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LMED', 'Lipid Lowering Medication Changes', 'Lipid Med Changes', 'Changed', '7', '2013-02-01 00:00:00'),
( 'LMP', 'Last Menstral Period', 'LMP', 'date', '13', '2013-02-01 00:00:00'),
( 'LUCR', 'Lung Crackles', 'Lung Crackles', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MACA', 'Macroalbuminuria', 'Renal Macrobalbumnuria', 'q 3-6 months', '3', '2013-02-01 00:00:00'),
( 'MACC', 'MAC culture', 'MAC culture', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MAMF', 'MAM Recall', 'Mammogram Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'MCCE', 'Motivation Counseling Compeleted Exercise', 'Motivation Counseling Compeleted Exercise', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MCCN', 'Motivation Counseling Compeleted Nutrition', 'Motivation Counseling Compeleted Nutrition', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MCCO', 'Motivation Counseling Compeleted Other', 'Motivation Counseling Compeleted Other', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MCCS', 'Motivation Counseling Compeleted Smoking Cessation', 'Motivation Counseling Compeleted Smoking Cessation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedA', 'Medication adherence access barriers', 'Difficulty affording meds or getting refills on time', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedG', 'Medication adherence general problem', 'Any missed days or doses of meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedN', 'Medication adherence negative beliefs', 'Concerns about side effects or medication is not working', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedR', 'Medication adherence recall barriers', 'Difficulty remembering to take meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MI', 'MI', 'MI', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Napl', 'Sodium', 'Sodium', 'in mmol/L', '5', '2013-02-01 00:00:00'),
( 'NDIP', 'CMCC NDI Pain', 'CMCC Neck Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'NDIS', 'CMCC NDI Score', 'CMCC Neck Disability Index - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'NOSK', 'Number of Cigarettes per day', 'Smoking', 'Cigarettes per day', '5', '2013-02-01 00:00:00'),
( 'NOVS', 'Need for nocturnal ventilated support', 'Need for nocturnal ventilated support', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'NtrC', 'Diet/Nutrition Counseling Given', 'Diet/Nutrition Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'NYHA', 'NYHA Functional Capacity Classification', 'NYHA Functional Capacity Classification', 'Class 1-4', '9', '2013-02-01 00:00:00'),
( 'OSWP', 'Oswestry BDI Pain', 'Oswestry Back Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'OSWS', 'Oswestry BDI Score', 'Oswestry Back Disability Index - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'OTCO', 'Other Concerns', 'Other Concerns', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'OthC', 'Other Counseling Given', 'Other Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'OUTR', 'Outside Spirometry Referral', 'Outside Spirometry Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'P', 'Para', 'Para', 'Para', '3', '2013-02-01 00:00:00'),
( 'PANE', 'Painful Neuropathy', 'Painful Neuropathy', 'Present', '7', '2013-02-01 00:00:00'),
( 'PAPF', 'Pap Recall', 'Pap Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'PEDE', 'Pitting Edema', 'Pitting Edema', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PEFR', 'PEFR value', 'PEFR value', 'null', '14', '2013-02-01 00:00:00'),
( 'PHIN', 'Pharmacological Intolerance', 'Pharmacological Intolerance', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PIDU', 'Previous Intravenous Drug Use', 'Previous Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PPD', 'PPD', 'PPD', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PRRF', 'Pulmonary Rehabilitation Referral', 'Pulmonary Rehabilitation Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PSPA', 'Patient Sets physical Activity Goal', 'Patient Sets physical Activity Goal', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PSSC', 'Psychosocial Screening', 'Psychosocial Screening', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PsyC', 'Psychosocial Counseling Given', 'Psychosocial Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HR', 'P', 'Heart Rate', 'in bpm (nnn) Range:40-180', '5', '2013-02-01 00:00:00'),
( 'PVD', 'PVD', 'Peripheral vascular disease', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'QDSH', 'QuickDASH Score', 'Disabilities of the Arm, Shoulder and Hand - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'RABG', 'Recommend ABG', 'Recommend ABG', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'REBG', 'Review Blood Glucose Records', 'Review Glucose Records', 'Reviewed', '7', '2013-02-01 00:00:00'),
( 'RESP', 'RR', 'Respiratory Rate', 'Breaths per minute', '4', '2013-02-01 00:00:00'),
( 'RETI', 'Retinopathy', 'null', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'RPHR', 'Review PHR', 'Review PHR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'RPPT', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'RVTN', 'Revascularization', 'Revascularization', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SCR', 'Serum Creatinine', 'Creatinine', 'in umol/L', '14', '2013-02-01 00:00:00'),
( 'SEXF', 'Sexual Function', 'Sexual Function', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SKST', 'Smoking Status', 'Smoking Status', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SMBG', 'Self monitoring BG', 'Self Monitoring Blood Glucose', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SmCC', 'Smoking Cessation Counseling Given', 'Smoking Cessation Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SMCD', 'Self Management Challenges', 'Self Management Challenges', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SMCP', 'Smoking Cessation Program', 'Smoking Cessation Program', 'null', '11', '2013-02-01 00:00:00'),
( 'SMCS', 'Smoking Cessation', 'Smoking Cessation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SMK', 'Smoking', 'Smoking', 'Yes/No/X', '12', '2013-02-01 00:00:00'),
( 'SmkA', 'Smoking Advice', 'Advised to Quid', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SmkC', 'Cigarette Smoking Cessation', 'Cigarette Smoking Cessation', 'Date last quit (yyyy-MM-dd)', '13', '2013-02-01 00:00:00'),
( 'SmkD', 'Daily Packs', 'Packs of Cigarets Daily', 'fraction or integer', '11', '2013-02-01 00:00:00'),
( 'SmkF', 'Smoking Followup', 'Followup Requested', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SmkPY', 'Cigarette Smoking History', 'Cigarette Smoking History', '[Cum. pack yrs 0-110]', '5', '2013-02-01 00:00:00'),
( 'SmkS', 'Cigarette Smoking Status', 'Cigarette Smoking Status', '[cig/day 0-80]', '4', '2013-02-01 00:00:00'),
( 'SODI', 'Salt Intake', 'Salt Intake', 'On Low Sodium Diet', '7', '2013-02-01 00:00:00'),
( 'SOHF', 'Symptoms of Heart Failure', 'Symptoms of Heart Failure', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SPIR', 'Spirometry', 'Spirometry', 'null', '14', '2013-02-01 00:00:00'),
( 'SSEX', 'Practicing Safe Sex', 'Practicing Safe Sex', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'STRE', 'Stress Testing', 'Stress Testing', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'StSc', 'Stress Screen', 'Several periods of irritability, feeling filled with anxiety, or difficulty sleeping b/c of stress', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SUAB', 'Substance Use', 'Substance Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SUO2', 'Need for supplemental oxygen', 'Need for supplemental oxygen', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'TCHD', 'TC/HDL', 'LIPIDS TD/HDL', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00'),
( 'TCHL', 'Total Cholestorol', 'Total Cholestorol', 'in mmol/L (nn.n) Range:2.0-12.0', '2', '2013-02-01 00:00:00'),
( 'TEMP', 'Temp', 'Temperature', 'degrees celcius', '3', '2013-02-01 00:00:00'),
( 'TG', 'TG', 'LIPIDS TG', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00'),
( 'TOXP', 'Toxoplasma IgG', 'Toxoplasma IgG', 'Positive', '7', '2013-02-01 00:00:00'),
( 'TRIG', 'Triglycerides', 'Triglycerides', 'in mmol/L (nn.n) Range:0.0-12.0', '3', '2013-02-01 00:00:00'),
( 'TSH', 'TSH', 'Thyroid Stimulating Hormone', 'null', '4', '2013-02-01 00:00:00'),
( 'TUG', 'Timed Up and Go', 'Timed Up and Go', 'Number of Seconds', '14', '2013-02-01 00:00:00'),
( 'UACR', 'Alb creat ratio', 'UACR', 'in mg/mmol', '5', '2013-02-01 00:00:00'),
( 'UAIP', 'Update AIDS defining illness in PMH', 'Update AIDS defining illness in PMH', 'Changed', '7', '2013-02-01 00:00:00'),
( 'UALB', 'Urine alb: creat ratio', 'Urine alb: creat ratio', 'in mg/mmol (nnn.n) Rnage:0-100 Interval:12mo.', '4', '2013-02-01 00:00:00'),
( 'UDUS', 'Update Drug Use', 'Update Drug Use', 'Changed', '7', '2013-02-01 00:00:00'),
( 'UHTP', 'Update HIV Test History in PMH', 'Update HIV Test History in PMH', 'Changed', '7', '2013-02-01 00:00:00'),
( 'URBH', 'Update Risk Behaviours', 'Update Risk Behaviours', 'Changed', '7', '2013-02-01 00:00:00'),
( 'USSH', 'Update Sexual Identity in Social History', 'Update Sexual Identity in Social History', 'Changed', '7', '2013-02-01 00:00:00'),
( 'VB12', 'Vit B12', 'Vitamin B12', 'Range >0 pmol/l', '14', '2013-02-01 00:00:00'),
( 'VDRL', 'VDRL', 'VDRL', 'Positive', '7', '2013-02-01 00:00:00'),
( 'VLOA', 'Viral Load', 'Viral Load', 'in x10e9/L', '14', '2013-02-01 00:00:00'),
( 'WAIS', 'Waist', 'Waist', 'Waist Circum in cm', '5', '2013-02-01 00:00:00'),
( 'WHR', 'Waist:Hip', 'Waist Hip Ratio', 'Range:0.5-2 Interval:3mo.', '2', '2013-02-01 00:00:00');

-- if an existing measurement is in the above list make most of the fields match
UPDATE `measurementType` m, `measurementTypeTEMP` t  SET m.measuringInstruction=t.measuringInstruction, m.typeDescription=t.typeDescription, m.typeDisplayName=t.typeDisplayName  WHERE m.type=t.type;

-- merge duplicated measurement types
-- UPDATE measurements SET type='02' WHERE type='O2SA';
-- UPDATE measurements SET type='A1C', measuringInstruction='Range:0.040-0.200' WHERE (type='HbAic' OR type='HbA1');
-- UPDATE measurements SET type='EDC' WHERE type='EDD';
-- UPDATE measurements SET type='HIP' WHERE type='HC';
-- UPDATE measurements SET type='PULS' WHERE type='HR';
-- UPDATE measurements SET type='SmkD' WHERE type='POSK';
-- UPDATE measurements SET type='SmkPY' WHERE type='SmkH';
-- UPDATE measurements SET type='WAIS' WHERE type='WS';
UPDATE measurements SET type='HR' WHERE type='PULS';
UPDATE measurements SET type='ACR' WHERE type='UACR';


-- now relink existing measurements
-- UPDATE `measurements` m, `measurementTypeTEMP` t  SET m.measuringInstruction=t.measuringInstruction where m.measuringInstruction IS NULL;

-- Fix any measurementGroups to point to the new measurements
-- UPDATE measurementGroup SET typeDisplayName='A1C' WHERE (typeDisplayName='HbAic' OR typeDisplayName='HbA1c');
-- UPDATE measurementGroup SET typeDisplayName='EDC' WHERE typeDisplayName='EDD';
-- UPDATE measurementGroup SET typeDisplayName='Daily Packs' WHERE typeDisplayName='Packs of Cigarettes per day';
-- UPDATE measurementGroup SET typeDisplayName='Waist' WHERE typeDisplayName='Waist Circ.';

-- Fix measurementMaps to point to new measures
-- UPDATE measurementMap SET name='Waist', ident_code='WAIS' WHERE ident_code='WC';

DROP TABLE IF EXISTS measurementTypeTEMP;

-- clean data for any measurements not in the above list with safe defaults avoiding nulls
UPDATE `measurementType` SET `createDate`='2013-02-01' WHERE `createDate` IS NULL;
UPDATE `measurementType` SET `validation`=11 WHERE `validation`>11 and `validation`<15;
UPDATE `measurementType` SET `measuringInstruction`="-" WHERE `measuringInstruction` IS NULL;
UPDATE `measurementType` SET `typeDisplayName`=`typeDescription` WHERE `typeDisplayName` IS NULL;

-- delete duplicates, they are never good in this type of table
CREATE TEMPORARY TABLE tmpTable (id int);
INSERT  tmpTable
        (id)
SELECT  id
FROM `measurementType` m1 
WHERE EXISTS
        (
	SELECT * FROM `measurementType` m2 
	WHERE m2.`type`=m1.`type` 
		AND m2.`measuringInstruction`=m1.`measuringInstruction`
		AND m2.`id`>m1.`id`
        );
DELETE FROM `measurementType` WHERE id IN (SELECT id from tmpTable);

-- Now its safe to set an unique index
ALTER TABLE `measurementType` ADD UNIQUE INDEX `type_instruction`(`type`, `measuringInstruction`);


-- Now merge in any missing measurements, updating validations if necessary
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '02', 'Oxygen Saturation', 'Oxygen Saturation', 'percent', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '24UA', '24 hour urine albumin', '24 hour urine albumin', 'mg/24h (nnn.n) Range:0-500 Interval:12mo.', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '24UR', '24-hr Urine cr clearance & albuminuria', 'Renal 24-hr Urine cr clearance & albuminuria', 'q 6-12 months, unit mg', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '5DAA', '5 Day Adherence if on ART', '5 Day Adherence if on ART', 'number', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'A1C', 'A1C', 'A1C', 'Range:0.040-0.200', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AACP', 'Asthma Action Plan ', 'Asthma Action Plan ', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ACOS', 'Asthma Coping Strategies', 'Asthma Coping Strategies', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ACR', 'Alb creat ratio', 'ACR', 'in mg/mmol', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ACS', 'Acute Conronary Syndrome', 'Acute Conronary Syndrome', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AEDR', 'Asthma Education Referral', 'Asthma Education Referral', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AELV', 'Exacerbations since last visit requiring clincal evaluation', 'Exacerbations since last visit requiring clincal evaluation', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AENC', 'Asthma Environmental Control', 'Asthma Environmental Control', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AHGM', 'Anit-hypoglycemic Medication', 'Anit-hypoglycemic Medication', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AIDU', 'Active Intravenous Drug Use', 'Active Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALB', 'Albumin', 'Albumin', 'in g per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALP', 'Alk Phos', 'Alk Phosphatase', 'in units per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BILC', 'Bili conj', 'Conjugated Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BILT', 'Bilirubin', 'Total Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BILU', 'Bili unconj', 'Unconjugated Bilirubin', 'in mmol per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CK', 'CK', 'Creatinine Kinase', 'in units per litre', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Clpl', 'Cl', 'Plasma Chloride', 'in mmol per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UA', 'Urate', 'Uric Acid', 'in mmol per litre', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALC', 'Alcohol', 'Alcohol', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALPA', 'Asthma Limits Physical Activity', 'Asthma Limits Physical Activity', 'Yes/No/X', '12', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=12;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ALT', 'ALT', 'ALT', 'in U/L', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Ang', 'Angina', 'Angina', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ANR', 'Asthma Needs Reliever   ', 'Asthma Needs Reliever   ', 'frequency per week', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ANSY', 'Asthma Night Time Symtoms', 'Asthma Night Time Symtoms', 'frequency per week', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AORA', 'ACE-I OR ARB', 'ACE-I OR ARB', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ARAD', 'Review Asthma Definition', 'Review Asthma Definition', 'Review Asthma Definition', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ARDT', 'Asthma  Review Device Technique optimal', 'Asthma  Review Device Technique optimal', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ARMA', 'Asthma Review Med Adherence', 'Asthma Review Med Adherence', 'Asthma Review Med Adherence', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ASAU', 'ASA Use', 'ASA Use', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ASPR', 'Asthma Specialist Referral', 'Asthma Specialist Referral', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'AST', 'AST', 'AST', 'in U/L', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ASTA', 'Asthma Trigger Avoidance', 'Asthma Trigger Avoidance', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ASWA', 'Asthma Absence School Work', 'Asthma Absence School Work', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ASYM', 'Asthma Symptoms', 'Asthma Symptoms', 'frequency per week', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BCTR', 'Birth Control', 'Birth Control', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BG', 'Blood Glucose', 'Blood Glucose', 'in mmol/L (nn.n) Range:1.5-30.0', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BMED', 'Blood Pressure Medication Changes', 'BP Med Changes', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BMI', 'Body Mass Index', 'BMI', 'BMI', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BP', 'BP', 'Blood Pressure', 'BP Tru', '6', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=6;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BP', 'BP', 'Blood Pressure', 'supine', '6', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=6;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BP', 'BP', 'Blood Pressure', 'standing position', '6', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=6;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'BP', 'BP', 'Blood Pressure', 'sitting position', '6', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=6;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CASA', 'Consider ASA', 'Consider ASA', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CD4', 'CD4', 'CD4', 'in x10e9/l', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CD4P', 'CD4 Percent', 'CD4 Percent', 'in %', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CDMP', 'Attended CDM Self Management Program', 'Attended CDM Self Management Program', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEDE', 'Education Exercise', 'Education Exercise', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEDM', 'Education Patient Meds', 'Education Patient Meds', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEDS', 'Education Salt fluid ', 'Education Salt fluid ', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CEDW', 'Education Daily Weight Monitoring', 'Education Daily Weight Monitoring', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CERV', 'ER visits for HF', 'ER visits for HF', 'integer', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CGSD', 'Collaborative Goal Setting', 'Collaborative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CIMF', 'Child Immunization recall', 'Child Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CMVI', 'CMV IgG', 'CMV IgG', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CODC', 'COD Classification', 'COD Classification', 'null', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'COPE', 'Provide COP Education Materials ', 'Provide COP Education Materials ', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'COPM', 'Review COP Med use and Side effects', 'Review COP Med use and Side effects', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'COPS', 'COP Specialist Referral', 'COP Specialist Referral', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'COUM', 'Warfarin Weekly Dose', 'WarfarinDose', 'Total mg Warfarin per week', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('CRCL', 'Creatinine Clearance', 'Creatinine Clearance', 'in ml/h', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CVD', 'CVD', 'Cerebrovascular disease', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'CXR', 'CXR', 'CXR', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DARB', 'ACE AARB', 'ACE AARB', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DEPR', 'Depression', 'Depression', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
('DESM', 'Dental Exam Every 6 Months', 'Dental Exam Every 6 Months', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DiaC', 'Diabetes Counseling Given', 'Diabetes Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIER', 'Diet and Exercise', 'Diet and Exercise', 'Reviewed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIET', 'Diet', 'Diet', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIFB', 'Impaired FB', 'Impaired FB', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DIGT', 'Impaired GT', 'Impaired Glucose Tolerance', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DM', 'DM', 'Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DMED', 'Diabetes Medication Changes', 'DM Med Changes', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DMME', 'Diabetes Education', 'Diabetes Education', 'Discussed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DMSM', 'Diabetes Self Management Goals', 'Diabetes Self Management Goals', 'Discussed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DMSM', 'Diabetes Self Management Goals', 'Diabetes Self Management Goals', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DOLE', 'Date of last Exacerbation', 'Date of last Exacerbation', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=13;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DpSc', 'Depression Screen', 'Feeling Sad, blue or depressed for 2 weeks or more', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DRCO', 'Drug Coverage', 'Drug Coverage', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DRPW', 'Drinks per Week', 'Drinks per Week', 'Number of Drinks per week', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DT1', 'Type I', 'Diabetes Type 1', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DT2', 'Type II', 'Diabetes Type 2', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'DTYP', 'Diabetes Type', 'Diabetes Type', '1 or 2', '10', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=10;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ECG', 'ECG', 'ECG', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDC', 'EDC', 'Expected Date of Confinement', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=13;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDDD', 'Education Diabetes', 'Education Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDF', 'EDF', 'Erectile Dysfunction', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDGI', 'Autonomic Neuropathy', 'Autonomic Neuropathy', 'Present', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDND', 'Education Nutrition Diabetes', 'Education Nutrition Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EDNL', 'Education Nutrition Lipids', 'Education Nutrition Lipids', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EGFR', 'EGFR', 'EGFR', 'in ml/min', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EPR', 'Exacerbation plan in place or reviewed', 'Exacerbation plan in place or reviewed', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EXE', 'Exercise', 'Exercise', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'ExeC', 'Exercise Counseling Given', 'Exercise Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Exer', 'Exercise', 'Exercise', '[min/week 0-1200]', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'EYEE', 'Dilated Eye Exam', 'Eye Exam', 'Exam Done', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FAHS', 'Risk of Falling', 'Risk of Falling', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FBPC', '2 hr PC BG', '2 hr PC BG', 'in mmol/L', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FBS', 'FBS', 'Glucose FBS', 'FBS', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEET', 'FEET', 'Feet Check skin', 'sensation (Yes/No)', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEET', 'FEET', 'Feet Check skin', 'vibration (Yes/No)', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEET', 'FEET', 'Feet Check skin', 'reflexes (Yes/No)', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEET', 'FEET', 'Feet Check skin', 'pulses (Yes/No)', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEET', 'FEET', 'Feet Check skin', 'infection (Yes/No)', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FEV1', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FGLC', 'Fasting Glucose meter , lab comparison', 'Fasting glucose meter, lab comparison', 'Within 20 percent', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FICO', 'Financial Concerns', 'Financial Concerns', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FLUF', 'Flu Recall', 'Flu Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FOBF', 'FOBT prevention recall', 'FOBT Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FRAM', 'Framingham 10 year CAD', 'Framingham 10 year CAD', 'percent', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTE', 'Foot Exam', 'Foot Exam', 'Normal', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTEx', 'Foot Exam: Significant Pathology', 'Significant Pathology', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTIn', 'Foot Exam: Infection', 'Infection', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTIs', 'Foot Exam: Ischemia', 'Ischemia', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTLS', 'Foot Exam  Test loss of Sensation', 'Foot Exam  Loss of Sensation', 'Normal', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTNe', 'Foot Exam: Neuropathy', 'Neuropathy', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTOt', 'Foot Exam: Other Vascular abnomality', 'Other Vascular abnomality', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTRe', 'Foot Exam: Referral made', 'Referral made', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTST', 'Free Testost', 'Free Testost', 'in nmol/L', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'FTUl', 'Foot Exam: Ulcer', 'Ulcer', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'G', 'Gravida', 'Gravida', 'Gravida', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'G6PD', 'G6PD', 'G6PD', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Hb', 'Hb', 'Hb', 'in g/L', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HIP', 'Hip Circ.', 'Hip Circumference', 'at 2 cm above navel', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Hchl', 'Hypercholesterolemia', 'Hypercholesterolemia', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HDL', 'HDL', 'High Density Lipid', 'in mmol/L (n.n) Range:0.4-4.0', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HEAD', 'Head circumference', 'Head circumference', 'in cm (nnn) Range:30-70 Interval:2mo.', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFCG', 'HF Collaorative Goal Setting', 'HF Collaorative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFCS', 'HF Self Management Challenge', 'HF Self Management Challenge', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFMD', 'HF Mod Risk Factor Diabetes', 'HF Mod Risk Factor Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFMH', 'HF Mod Risk Factor Hyperlipidemia', 'HF Mod Risk Factor Hyperlipidemia', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFMO', 'HF Mod Risk Factor Overweight', 'HF Mod Risk Factor Overweight', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFMS', 'HF Mod Risk Factor Smoking', 'HF Mod Risk Factor Smoking', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HFMT', 'HF Mod Risk Factor Hypertension', 'HF Mod Risk Factor Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HIVG', 'HIV genotype', 'HIV genotype', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HLA', 'HLA B5701', 'HLA B5701', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HpAI', 'Hep A IgG', 'Hep A IgG', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HpBA', 'Hep BS Ab', 'Hep BS Ab', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPBC', 'Hep B CAb', 'Hep B CAb', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPBP', 'Hep B PCR', 'Hep B PCR', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HpBS', 'Hep BS Ag', 'Hep BS Ag', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HpCA', 'Hep C Ab', 'Hep C Ab', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPCG', 'Hep C Genotype', 'Hep C Genotype', 'integer Range 1-7', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HPCP', 'Hep C PCR', 'Hep C PCR', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HR', 'P', 'Heart Rate', 'in bpm (nnn) Range:40-180', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HRMS', 'Review med use and side effects', 'HTN Review of Medication use and side effects', 'null', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HSMC', 'Self Management Challenges', 'HTN Self Management Challenges', 'null', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HSMG', 'Self Management Goal', 'HTN Self Management Goal', 'null', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HT', 'HT', 'Height', 'in cm', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HTN', 'HTN', 'Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HYPE', 'Hypoglycemic Episodes', 'Number of Hypoglycemic Episodes', 'since last visit', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'HYPM', 'Hypoglycemic Management', 'Hypoglycemic Management', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'IART', 'Currently On ART', 'Currently On ART', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iDia', 'Eye Exam: Diabetic Retinopathy', 'Diabetic Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iEx', 'Eye Exam: Significant Pathology', 'Significant Pathology', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iHyp', 'Eye Exam: Hypertensive Retinopathy', 'Hypertensive Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'INR', 'INR', 'INR', 'INR Blood Work', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'INSL', 'Insulin', 'Insulin', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iOth', 'Eye Exam: Other Vascular Abnomality', 'Other Vascular Abnormality', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'iRef', 'Eye Exam: Refferal Made', 'Refferal Made', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'JVPE', 'JPV Elevation', 'JPV Elevation', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Kpl', 'Potassium', 'Potassium', 'in mmol/L', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LcCt', 'Locus of Control Screen', 'Feeling lack of control over daily life', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LDL', 'LDL', 'Low Density Lipid', 'monitor every 1-3 year', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LEFP', 'LEFS Pain', 'Lower Extremity Functional Scale - Pain', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LETH', 'Lethargy', 'Lethargic', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LHAD', 'Lung Related Hospital Admission', 'Lung Related Hospital Admission', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LMED', 'Lipid Lowering Medication Changes', 'Lipid Med Changes', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LMP', 'Last Menstral Period', 'LMP', 'date', '13', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=13;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'LUCR', 'Lung Crackles', 'Lung Crackles', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MACA', 'Macroalbuminuria', 'Renal Macrobalbumnuria', 'q 3-6 months', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MACC', 'MAC culture', 'MAC culture', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MAMF', 'MAM Recall', 'Mammogram Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCCE', 'Motivation Counseling Compeleted Exercise', 'Motivation Counseling Compeleted Exercise', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCCN', 'Motivation Counseling Compeleted Nutrition', 'Motivation Counseling Compeleted Nutrition', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCCO', 'Motivation Counseling Compeleted Other', 'Motivation Counseling Compeleted Other', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MCCS', 'Motivation Counseling Compeleted Smoking Cessation', 'Motivation Counseling Compeleted Smoking Cessation', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MedA', 'Medication adherence access barriers', 'Difficulty affording meds or getting refills on time', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MedG', 'Medication adherence general problem', 'Any missed days or doses of meds', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MedN', 'Medication adherence negative beliefs', 'Concerns about side effects or medication is not working', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MedR', 'Medication adherence recall barriers', 'Difficulty remembering to take meds', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'MI', 'MI', 'MI', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'Napl', 'Sodium', 'Sodium', 'in mmol/L', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NDIP', 'CMCC NDI Pain', 'CMCC Neck Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NDIS', 'CMCC NDI Score', 'CMCC Neck Disability Index - Score', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NOSK', 'Number of Cigarettes per day', 'Smoking', 'Cigarettes per day', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NOVS', 'Need for nocturnal ventilated support', 'Need for nocturnal ventilated support', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NtrC', 'Diet/Nutrition Counseling Given', 'Diet/Nutrition Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'NYHA', 'NYHA Functional Capacity Classification', 'NYHA Functional Capacity Classification', 'Class 1-4', '9', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=9;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OSWP', 'Oswestry BDI Pain', 'Oswestry Back Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OSWS', 'Oswestry BDI Score', 'Oswestry Back Disability Index - Score', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OTCO', 'Other Concerns', 'Other Concerns', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OthC', 'Other Counseling Given', 'Other Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'OUTR', 'Outside Spirometry Referral', 'Outside Spirometry Referral', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'P', 'Para', 'Para', 'Para', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PANE', 'Painful Neuropathy', 'Painful Neuropathy', 'Present', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PAPF', 'Pap Recall', 'Pap Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PEDE', 'Pitting Edema', 'Pitting Edema', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PEFR', 'PEFR value', 'PEFR value', 'null', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PHIN', 'Pharmacological Intolerance', 'Pharmacological Intolerance', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PIDU', 'Previous Intravenous Drug Use', 'Previous Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'POSK', 'Packs of Cigarettes per day', 'Smoking', 'Packs per day', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PPD', 'PPD', 'PPD', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PRRF', 'Pulmonary Rehabilitation Referral', 'Pulmonary Rehabilitation Referral', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PSPA', 'Patient Sets physical Activity Goal', 'Patient Sets physical Activity Goal', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PSSC', 'Psychosocial Screening', 'Psychosocial Screening', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PsyC', 'Psychosocial Counseling Given', 'Psychosocial Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'PVD', 'PVD', 'Peripheral vascular disease', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'QDSH', 'QuickDASH Score', 'Disabilities of the Arm, Shoulder and Hand - Score', 'number', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RABG', 'Recommend ABG', 'Recommend ABG', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'REBG', 'Review Blood Glucose Records', 'Review Glucose Records', 'Reviewed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RESP', 'RR', 'Respiratory Rate', 'Breaths per minute', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RETI', 'Retinopathy', 'null', 'Discussed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RPHR', 'Review PHR', 'Review PHR', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RPPT', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'RVTN', 'Revascularization', 'Revascularization', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SCR', 'Serum Creatinine', 'Creatinine', 'in umol/L', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SEXF', 'Sexual Function', 'Sexual Function', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SKST', 'Smoking Status', 'Smoking Status', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SMBG', 'Self monitoring BG', 'Self Monitoring Blood Glucose', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkA', 'Smoking Advice', 'Advised to Quid', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkC', 'Cigarette Smoking Cessation', 'Cigarette Smoking Cessation', 'Date last quit (yyyy-MM-dd)', '13', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=13;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkD', 'Daily Packs', 'Packs of Cigarets Daily', 'fraction or integer', '11', '2013-02-01 00:00:00')  ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkF', 'Smoking Followup', 'Followup Requested', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkPY', 'Cigarette Smoking History', 'Cigarette Smoking History', '[Cum. pack yrs 0-110]', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmkS', 'Cigarette Smoking Status', 'Cigarette Smoking Status', '[cig/day 0-80]', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SmCC', 'Smoking Cessation Counseling Given', 'Smoking Cessation Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SMCD', 'Self Management Challenges', 'Self Management Challenges', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SMCP', 'Smoking Cessation Program', 'Smoking Cessation Program', 'null', '11', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=11;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SMCS', 'Smoking Cessation', 'Smoking Cessation', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SMK', 'Smoking', 'Smoking', 'Yes/No/X', '12', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=12;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SODI', 'Salt Intake', 'Salt Intake', 'On Low Sodium Diet', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SOHF', 'Symptoms of Heart Failure', 'Symptoms of Heart Failure', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SPIR', 'Spirometry', 'Spirometry', 'null', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SSEX', 'Practicing Safe Sex', 'Practicing Safe Sex', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'STRE', 'Stress Testing', 'Stress Testing', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'StSc', 'Stress Screen', 'Several periods of irritability, feeling filled with anxiety, or difficulty sleeping b/c of stress', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SUAB', 'Substance Use', 'Substance Use', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'SUO2', 'Need for supplemental oxygen', 'Need for supplemental oxygen', 'Yes/No', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TCHD', 'TC/HDL', 'LIPIDS TD/HDL', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TCHL', 'Total Cholestorol', 'Total Cholestorol', 'in mmol/L (nn.n) Range:2.0-12.0', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TEMP', 'Temp', 'Temperature', 'degrees celcius', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TG', 'TG', 'LIPIDS TG', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TOXP', 'Toxoplasma IgG', 'Toxoplasma IgG', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TRIG', 'Triglycerides', 'Triglycerides', 'in mmol/L (nn.n) Range:0.0-12.0', '3', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=3;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TSH', 'TSH', 'Thyroid Stimulating Hormone', 'null', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'TUG', 'Timed Up and Go', 'Timed Up and Go', 'Number of Seconds', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UACR', 'Alb creat ratio', 'UACR', 'in mg/mmol', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UAIP', 'Update AIDS defining illness in PMH', 'Update AIDS defining illness in PMH', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UALB', 'Urine alb: creat ratio', 'Urine alb: creat ratio', 'in mg/mmol (nnn.n) Rnage:0-100 Interval:12mo.', '4', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=4;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UDUS', 'Update Drug Use', 'Update Drug Use', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'UHTP', 'Update HIV Test History in PMH', 'Update HIV Test History in PMH', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'URBH', 'Update Risk Behaviours', 'Update Risk Behaviours', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'USSH', 'Update Sexual Identity in Social History', 'Update Sexual Identity in Social History', 'Changed', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'VB12', 'Vit B12', 'Vitamin B12', 'Range >0 pmol/l', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'VDRL', 'VDRL', 'VDRL', 'Positive', '7', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=7;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'VLOA', 'Viral Load', 'Viral Load', 'in x10e9/L', '14', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=14;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'WAIS', 'Waist', 'Waist', 'Waist Circum in cm', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'WHR', 'Waist:Hip', 'Waist Hip Ratio', 'Range:0.5-2 Interval:3mo.', '2', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=2;
INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( 'WT', 'WT', 'Weight', 'in kg', '5', '2013-02-01 00:00:00') ON DUPLICATE KEY UPDATE `validation`=5;

-- Now drop the unique index
ALTER TABLE `measurementType` DROP INDEX `type_instruction`;

--
-- Table structure for table `validations`
--

DROP TABLE IF EXISTS `validations`;
CREATE TABLE IF NOT EXISTS `validations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `regularExp` varchar(100),
  `maxValue1` double,
  `minValue` double,
  `maxLength` int(3),
  `minLength` int(3),
  `isNumeric` tinyint(1),
  `isTrue` tinyint(1),
  `isDate` tinyint(1),
  PRIMARY KEY (`id`)
);

--
-- Data for table `validations` that matches the `measurementType` data above
--

INSERT INTO `validations` (`id`, `name`, `regularExp`, `maxValue1`, `minValue`, `maxLength`, `minLength`, `isNumeric`, `isTrue`, `isDate`) VALUES
(1, 'Numeric Value: 0 to 1', NULL, 10, 0, NULL, NULL, 1, NULL, NULL),
(2, 'Numeric Value: 0 to 10', NULL, 10, 0, NULL, NULL, 1, NULL, NULL),
(3, 'Numeric Value: 0 to 50', NULL, 50, 0, NULL, NULL, 1, NULL, NULL),
(4, 'Numeric Value: 0 to 100', NULL, 100, 0, NULL, NULL, 1, NULL, NULL),
(5, 'Numeric Value: 0 to 300', NULL, 300, 0, NULL, NULL, 1, NULL, NULL),
(6, 'Blood Pressure', '[0-9]{2,3}/{1}[0-9]{2,3}', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(7, 'Yes/No/NA', 'YES|yes|Yes|Y|NO|no|No|N|NotApplicable|NA', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(8, 'Integer: 1 to 5', NULL, 5, 1, 1, NULL, NULL, NULL, NULL),
(9, 'Integer: 1 to 4', NULL, 4, 1, 1, NULL, NULL, NULL, NULL),
(10,'Integer: 1 to 3', NULL, 3, 1, 1, NULL, NULL, NULL, NULL),
(11,'No Validations', NULL, 0, 0, 0, 0, 0, NULL, 0),
(12,'Yes/No/X','YES|yes|Yes|Y|NO|no|No|N|X|x',0,0,0,0,0,NULL,0),
(13,'Date',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1),
(14,'Numeric Value greater than or equal to 0',NULL,0,0,0,0,1,NULL,0);


