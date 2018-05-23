-- MySQL dump 9.07
--
-- Host: localhost    Database: oscar_mcmaster
-- -------------------------------------------------------
-- Server version	4.0.12-standard

INSERT INTO `Icd9Synonym` VALUES ('172', 'Skin Cancer', '1'), ('173', 'basal cell carcinoma', '2'), ('2429', 'Hyperthyroid', '3'), ('2449', 'Hypothyroid', '4'), ('2564', 'polycystic ovarian syndrome', '5'), ('2720', 'Hypercholesterolemia', '6'), ('2722', 'Mixed hyperlipidemia', '7'), ('2724', 'Cholesterol', '8'), ('274', 'Gout', '9'), ('2768', 'hypokalemia', '10'), ('2778', 'Retinitis pigmentosa', '11'), ('2901', 'Dementia', '12'), ('2963', 'Depression/Mood', '13'), ('2967', 'Bipolar', '14'), ('3000', 'Anxiety', '15'), ('3003', 'OCD', '16'), ('30981', 'PTSD', '17'), ('3339', 'Restless leg syndrome', '18'), ('3540', 'carpal tunnel syndrome', '19'), ('356', 'Neuropathy/Neuropathic pain', '20'), ('401', 'Hypertension', '21'), ('4140', 'CAD', '22'), ('4273', 'Atrial Fibrilation', '23'), ('453', 'Deep vein thrombosis', '24'), ('4781', 'Nasal congestion', '25'), ('4912', 'COPD', '26'), ('530', 'Barret\'s esophagus', '27'), ('53081', 'GERD/Reflux', '28'), ('555', 'Cholitis/Crohn\'s', '29'), ('5718', 'Fatty liver', '30'), ('59651', 'Overactive bladder', '31'), ('600', 'Enlarged prostate', '32'), ('607', 'ED/Libido', '33'), ('627', 'Menopause', '34'), ('6929', 'Dermatitis/Eczema', '35'), ('6960', 'Psoriatic arthritis', '36'), ('715', 'Arthritis/Osteoarthritis', '37'), ('722', 'degenerative disc disorder', '38'), ('7245', 'Back Pain', '39'), ('72885', 'Muscle Spasms', '40'), ('7291', 'Fibromyalgia', '41'), ('73390', 'osteopenia', '42'), ('7506', 'Hiatis Hernia', '43'), ('7804', 'Dizziness', '44'), ('7805', 'sleep', '45'), ('78051', 'Sleep apnea', '46'), ('78052', 'insomnia', '47'), ('78605', 'Difficulty breathing', '48'), ('7865', 'Chest pain', '49'), ('78841', 'Frequent Urination', '50'), ('8470', 'whiplash', '51'), ('O54', 'Herpes', '52'), ('V433', 'Aortic valve replacement', '53'), ('V450', 'Cardiac pace maker', '54');

--
-- ContactSpecialty Data
-- ----------------------------

INSERT INTO `ContactSpecialty` VALUES ('1', 'DERMATOLOGY', null), ('2', 'NEUROLOGY', null), ('3', 'PSYCHIATRY', null), ('5', 'OBSTETRICS & GYNAECOLOGY', null), ('6', 'OPHTHALMOLOGY', null), ('7', 'OTOLARYNGOLOGY', null), ('8', 'GENERAL SURGERY', null), ('9', 'NEUROSURGERY', null), ('10', 'ORTHOPAEDICS', null), ('11', 'PLASTIC SURGERY', null), ('12', 'CARDIO & THORACIC', null), ('13', 'UROLOGY', null), ('14', 'PAEDIATRICS', null), ('15', 'INTERNAL MEDICINE', null), ('16', 'RADIOLOGY', null), ('17', 'LABORATORY PROCEDURES', null), ('18', 'ANAESTHESIA', null), ('19', 'PAEDIATRIC CARDIOLOGY', null), ('20', 'PHYSICAL MEDICINE AND  REHABILITATION', null), ('21', 'PUBLIC HEALTH', null), ('22', 'PHARMACIST', null), ('23', 'OCCUPATIONAL MEDICINE', null), ('24', 'GERIATRIC MEDICINE', null), ('25', 'UNKNOWN', null), ('26', 'PROCEDURAL CARDIOLOGIST', null), ('28', 'EMERGENCY MEDICINE', null), ('29', 'MEDICAL MICROBIOLOGY', null), ('30', 'CHIROPRACTORS', null), ('31', 'NATUROPATHS', null), ('32', 'PHYSICAL THERAPISTS', null), ('33', 'NUCLEAR MEDICINE', null), ('34', 'OSTEOPATHY', null), ('35', 'ORTHOPTIC', null), ('37', 'ORAL SURGEONS', null), ('38', 'PODIATRISTS', null), ('39', 'OPTOMETRIST', null), ('40', 'DENTAL SURGEONS', null), ('41', 'ORAL MEDICINE', null), ('42', 'ORTHODONTISTS', null), ('43', 'MASSAGE PRACTITIONER', null), ('44', 'RHEUMATOLOGY', null), ('45', 'CLINICAL IMMUNIZATION AND ALLERGY', null), ('46', 'MEDICAL GENETICS', null), ('47', 'VASCULAR SURGERY', null), ('48', 'THORACIC SURGERY', null), ('49', 'FAMILY PHYSICIAN', null), ('50', 'ENDOCRINOLOGIST', null);

-- Insert into Specialty Table.
--

INSERT INTO specialty VALUES ('BC','00',' GENERAL PRACTITIONER');
INSERT INTO specialty VALUES ('BC','01',' DERMATOLOGY');
INSERT INTO specialty VALUES ('BC','02',' NEUROLOGY');
INSERT INTO specialty VALUES ('BC','03',' PSYCHIATRY');
INSERT INTO specialty VALUES ('BC','05',' OBSTETRICS & GYNAECOLOGY');
INSERT INTO specialty VALUES ('BC','06',' OPHTHALMOLOGY');
INSERT INTO specialty VALUES ('BC','07',' OTOLARYNGOLOGY');
INSERT INTO specialty VALUES ('BC','08',' GENERAL SURGERY');
INSERT INTO specialty VALUES ('BC','09',' NEUROSURGERY');
INSERT INTO specialty VALUES ('BC','10',' ORTHOPAEDICS');
INSERT INTO specialty VALUES ('BC','11',' PLASTIC SURGERY');
INSERT INTO specialty VALUES ('BC','12',' CARDIO & THORACIC');
INSERT INTO specialty VALUES ('BC','13',' UROLOGY');
INSERT INTO specialty VALUES ('BC','14',' PAEDIATRICS');
INSERT INTO specialty VALUES ('BC','15',' INTERNAL MEDICINE');
INSERT INTO specialty VALUES ('BC','16',' RADIOLOGY');
INSERT INTO specialty VALUES ('BC','17',' LABORATORY PROCEDURES');
INSERT INTO specialty VALUES ('BC','18',' ANAESTHESIA');
INSERT INTO specialty VALUES ('BC','19',' PAEDIATRIC CARDIOLOGY');
INSERT INTO specialty VALUES ('BC','20',' PHYSICAL MEDICINE AND  REHABILITATION');
INSERT INTO specialty VALUES ('BC','21',' PUBLIC HEALTH');
INSERT INTO specialty VALUES ('BC','23',' OCCUPATIONAL MEDICINE');
INSERT INTO specialty VALUES ('BC','24',' GERIATRIC MEDICINE          SUB-SPECIALTY OF INTERNAL MED');
INSERT INTO specialty VALUES ('BC','26',' PROCEDURAL CARDIOLOGIST');
INSERT INTO specialty VALUES ('BC','28',' EMERGENCY MEDICINE');
INSERT INTO specialty VALUES ('BC','29',' MEDICAL MICROBIOLOGY');
INSERT INTO specialty VALUES ('BC','30',' CHIROPRACTORS');
INSERT INTO specialty VALUES ('BC','31',' NATUROPATHS');
INSERT INTO specialty VALUES ('BC','32',' PHYSICAL THERAPISTS');
INSERT INTO specialty VALUES ('BC','33',' NUCLEAR MEDICINE');
INSERT INTO specialty VALUES ('BC','34',' OSTEOPATHY');
INSERT INTO specialty VALUES ('BC','35',' ORTHOPTIC');
INSERT INTO specialty VALUES ('BC','37',' ORAL SURGEONS');
INSERT INTO specialty VALUES ('BC','38',' PODIATRISTS');
INSERT INTO specialty VALUES ('BC','39',' OPTOMETRIST');
INSERT INTO specialty VALUES ('BC','40',' DENTAL SURGEONS');
INSERT INTO specialty VALUES ('BC','41',' ORAL MEDICINE');
INSERT INTO specialty VALUES ('BC','42',' ORTHODONTISTS');
INSERT INTO specialty VALUES ('BC','43',' MASSAGE PRACTITIONER');
INSERT INTO specialty VALUES ('BC','44',' RHEUMATOLOGY');
INSERT INTO specialty VALUES ('BC','45',' CLINICAL IMMUNIZATION AND ALLERGY');
INSERT INTO specialty VALUES ('BC','46',' MEDICAL GENETICS');
INSERT INTO specialty VALUES ('BC','47',' VASCULAR SURGERY');
INSERT INTO specialty VALUES ('BC','48',' THORACIC SURGERY');

--
-- Dumping data for table 'clinic'
--

INSERT INTO clinic VALUES (1234,'McMaster Hospital','Hamilton','Hamilton','L0R 4K3','555-555-5555','555-555-5555','444','A','Ontario','','');

--
-- Dumping data for table 'clinic_location'
--

INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('3642',1,'The Wellington Lodge');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('3831',1,'Maternity Centre of Hamilton');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('1994',1,'McMaster University Medical Center');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('1983',1,'Henderson General');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('1985',1,'Hamilton General');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('2003',1,'St. Joseph\"s Hospital');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('0000',1,'Not Applicable');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('1972',1,'Chedoke Hospital');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('3866',1,'Stonechurch Family Health Center');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('3226',1,'Stonechurch Family Health PCN');
INSERT INTO clinic_location (clinic_location_no,clinic_no,clinic_location_name) VALUES ('9999',1,'Home Visit');

--
-- Dumping data for table 'config_Immunization'
--

INSERT INTO config_Immunization VALUES (1,'Routine Infants & Children','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<immunizationSet headers=\"true\" name=\"Routine Infants &amp; Children\"><columnList><column name=\"2 months\"/><column name=\"4 months\"/><column name=\"6 months\"/><column name=\"12 months\"/><column name=\"18 months\"/><column name=\"4-6 years\"/><column name=\"14-16 years\"/></columnList><rowList><row name=\"DTP+IPV\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"5\"/><cell index=\"6\"/></row><row name=\"Hib\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"5\"/></row><row name=\"MMR\"><cell index=\"4\"/><cell index=\"6\"/></row><row name=\"Td\"><cell index=\"7\"/></row><row name=\"Hep B&#10;(3 doses)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/></row><row name=\"VariVax&#10;(chickenpox)\"><cell index=\"4\"/></row><row name=\"Prevnar&#10;(pneumococcus)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/></row><row name=\"Menjuvate&#10;(menningococcus)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"7\"/></row></rowList></immunizationSet>','2002-07-30','174',1);
INSERT INTO config_Immunization VALUES (2,'Late Infants & Children','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<immunizationSet headers=\"true\" name=\"Late Infants &amp; Children\"><columnList><column name=\"First visit\"/><column name=\"2 months later\"/><column name=\"2 months later\"/><column name=\"6-12 months later\"/><column name=\"4-6 years old\"/><column name=\"14-16 years old\"/></columnList><rowList><row name=\"DTP+IPV\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/></row><row name=\"Hib\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"4\"/></row><row name=\"MMR\"><cell index=\"1\"/></row><row name=\"Td\"><cell index=\"6\"/></row><row name=\"Hep B&#10;(3 doses)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"4\"/></row><row name=\"Varivax&#10;(chickenpox)\"><cell index=\"1\"/></row><row name=\"Prevnar&#10;(pneumococcus)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"4\"/></row><row name=\"Menjuvate&#10;(meningococcus)\"><cell index=\"1\"/><cell index=\"2\"/></row></rowList></immunizationSet>','2002-07-30','174',0);
INSERT INTO config_Immunization VALUES (3,'>7 year old children','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<immunizationSet headers=\"true\" name=\"&gt;7 year old children\"><columnList><column name=\"First visit\"/><column name=\"2 months later\"/><column name=\"6-12 months later\"/><column name=\"10 years later\"/></columnList><rowList><row name=\"dTap\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/></row><row name=\"IPV\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/></row><row name=\"MMR\"><cell index=\"1\"/></row><row name=\"Hep B\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/></row><row name=\"Varicella\"><cell index=\"1\"/></row><row name=\"Meningococcal&#10;Vaccine\"><cell index=\"1\"/></row></rowList></immunizationSet>','2002-07-30','174',0);
INSERT INTO config_Immunization VALUES (4,'Adult','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<immunizationSet headers=\"true\" name=\"Adult\"><columnList><column name=\"Td (Every 10 years)\"/><column name=\"Influenza (yearly)\"/><column name=\"Pneumococcal&#13;&lt;br&gt;(&gt;65 years + risks)\"/><column name=\"MMR(Adults born 1970 or later)\"/><column name=\"Other\"/><column name=\"Other\"/><column name=\"Other\"/></columnList><rowList><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row><row name=\"Date\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/><cell index=\"5\"/><cell index=\"6\"/><cell index=\"7\"/></row></rowList></immunizationSet>','2002-07-30','174',0);
INSERT INTO config_Immunization VALUES (5,'Routine Infants & Children','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<immunizationSet headers=\"true\" name=\"Routine Infants &amp; Children\"><columnList><column name=\"2 months\"/><column name=\"4 months\"/><column name=\"6 months\"/><column name=\"12 months\"/><column name=\"18 months\"/><column name=\"4-6 years\"/><column name=\"14-16 years\"/></columnList><rowList><row name=\"DTP+IPV\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"5\"/><cell index=\"6\"/></row><row name=\"Hib\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"5\"/></row><row name=\"MMR\"><cell index=\"4\"/><cell index=\"6\"/></row><row name=\"Td\"><cell index=\"7\"/></row><row name=\"Hep B (first visit,&#10;1 month, 6 months)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/></row><row name=\"VariVax&#10;(chickenpox)\"><cell index=\"4\"/></row><row name=\"Prevnar&#10;(pneumococcus)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"4\"/></row><row name=\"Menjugate or others&#10;(meningococcus)\"><cell index=\"1\"/><cell index=\"2\"/><cell index=\"3\"/><cell index=\"7\"/></row></rowList></immunizationSet>','2002-07-30','174',0);

--
-- Dumping data for table 'consultationServices'
--

INSERT INTO consultationServices VALUES (53,'Cardiology','1');
INSERT INTO consultationServices VALUES (54,'Dermatology','1');
INSERT INTO consultationServices VALUES (55,'Neurology','1');
INSERT INTO consultationServices VALUES (56,'Radiology','1');
INSERT INTO consultationServices VALUES (57,'SEE NOTES','1');
INSERT INTO consultationServices VALUES (58,'Referral Doctor','02');

--
-- Dumping data for table 'ctl_billingservice'
--

--
-- Dumping data for table 'ctl_billingservice_premium'
--

--
-- Dumping data for table 'ctl_diagcode'
--

--
-- Dumping data for table 'ctl_doctype'
--

INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','lab','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','consult','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','insurance','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','legal','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','oldchart','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','radiology','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','pathology','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','others','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('demographic','photo','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','resource','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','desktop','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','handout','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','forms','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','others','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','share','A');
INSERT INTO ctl_doctype (module,doctype,status) VALUES ('provider','photo','A');
INSERT INTO `ctl_doctype` (`module`, `doctype`, `status`, `id`) VALUES('provider','invoice letterhead','A',null);

--
-- Dumping data for table 'ctl_doc_class'
--

insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Abdomen X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Barium Enema");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Bone Densitometry");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Bone Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Brain Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Carotid Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Carotid Doppler Ultrasound");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Cervical Spine X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Chest X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Coronary Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","CT Scan Body");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","CT Scan Head");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Echocardiogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","ERCP X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Hysterosalpingogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","IVP");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Liver-Spleen Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Lumbar Spine X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Lung Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Mammogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. CT Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. MRI Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. Nuclear Scan");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. Ultrasound");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Misc. X-Ray");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","MRI Scan Body");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","MRI Scan Head");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Myelogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Myoview)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Other Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Retinal Angiography");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Retinal Tomograph");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Sestamibi");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Sonohistogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Stress Heart Scan (Thallium");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","UGI with Small Bowel");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Abdomen");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Breast");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Obstetrical");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Pelvis");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Ultrasound Thyroid");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Upper GI Series");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Imaging Report","Venous Doppler Ultrasound");

insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Ambulatory BP Monitoring");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Arterial Segmental Pressures (ABI)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Audiogram");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Bronchoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Colonoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Colposcopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Cystoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Dobutamine)");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","ECG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","EEG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","EGD-oscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","EMG");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Holter Monitor");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Loop Recorder");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Mantoux Test");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Misc. Diagnostic Test");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Pap Test Report");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Persantine");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Pulmonary Function Testing");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Sigmoidoscopy");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Sleep Study");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Stress Test (Exercise");
insert into ctl_doc_class (reportclass,subclass) values ("Diagnostic Test Report","Urodynamic Testing");

insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Echocardiography Bubble Study");
insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Pericardiocentesis");
insert into ctl_doc_class (reportclass,subclass) values ("Cardio Respiratory Report","Echocardiography Esophageal");

insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Authorization from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Consent from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Disability Report");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Letter from Insurance Company");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Letter from Lawyer");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Letter from Patient");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Letter from WSIB");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Living Will");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Miscellaneous Letter");
insert into ctl_doc_class (reportclass,subclass) values ("Other Letter","Power of Attorney for Health Care");

insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Allergy & Immunology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Anaesthesiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Audiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Cardiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Cardiovascular Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Chiropody / Podiatry");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Chiropractic");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Clinical Biochemistry");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Dentistry");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Dermatology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Diagnostic Radiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Dietitian");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Emergency Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Emergency Physician");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Endocrinology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Family Practice");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Gastroenterology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","General Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Genetics");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Geriatrics");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Hematology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Hospitalis");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Infectious Disease");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Internal Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Kinesiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Microbiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Midwifery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Naturopathy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Neonatology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Nephrology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Neurology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Neurosurgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Nuclear Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Nurse Practitioner");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Nursing");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Obstetrics & Gynecology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Occupational Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","On-Call Nurse");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","On-Call Physician");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Oncology / Chemotherapy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Ophthalmology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Optometry");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Oral Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Orthopedic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Osteopathy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Other Consultant ReportAnt");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Other Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Otolaryngology (ENT)");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Paediatrics");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Palliative Care");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Pathology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Pharmacology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Physical Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Physiotherapy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Plastic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Psychiatry");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Psychology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Respiratory Technology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Respirology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Rheumatology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Social Work");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Speech Therapy");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Sports Medicine");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Therapeutic Radiology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Thoracic Surgery");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Urgent Care/Walk-In Clinic Physician");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Uro-Gynecology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Urology");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportA","Vascular Surgery");

insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Admission History");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Consultant ReportAtion");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Discharge Summary");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Encounter Report");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Operative Report");
insert into ctl_doc_class (reportclass,subclass) values ("Consultant ReportB","Progress Report");


--
-- Dumping data for table 'ctl_document'
--

INSERT INTO ctl_document VALUES ('provider',999998,4953,'A');
INSERT INTO ctl_document VALUES ('provider',999998,4954,'H');
INSERT INTO ctl_document VALUES ('demographic',2147483647,4955,'A');

--
-- Dumping data for table 'ctl_frequency'
--
INSERT INTO ctl_frequency VALUES (1,'OD',1,1);
INSERT INTO ctl_frequency VALUES (2,'BID',2,2);
INSERT INTO ctl_frequency VALUES (3,'TID',3,3);
INSERT INTO ctl_frequency VALUES (4,'QID',4,4);
INSERT INTO ctl_frequency VALUES (5,'Q1H',24,24);
INSERT INTO ctl_frequency VALUES (6,'Q2H',12,12);
INSERT INTO ctl_frequency VALUES (7,'Q1-2H',12,24);
INSERT INTO ctl_frequency VALUES (8,'Q3-4H',6,8);
INSERT INTO ctl_frequency VALUES (9,'Q4H',6,6);
INSERT INTO ctl_frequency VALUES (10,'Q4-6H',4,6);
INSERT INTO ctl_frequency VALUES (11,'Q6H',4,4);
INSERT INTO ctl_frequency VALUES (12,'Q8H',3,3);
INSERT INTO ctl_frequency VALUES (13,'Q12H',2,2);
INSERT INTO ctl_frequency VALUES (14,'QAM',1,1);
INSERT INTO ctl_frequency VALUES (15,'QPM',1,1);
INSERT INTO ctl_frequency VALUES (16,'QHS',1,1);
INSERT INTO ctl_frequency VALUES (17,'Q1Week','1/7','1/7');
INSERT INTO ctl_frequency VALUES (18,'Q2Week','1/14','1/14');
INSERT INTO ctl_frequency VALUES (19,'Q1Month','1/30','1/30');
INSERT INTO ctl_frequency VALUES (20,'Q3Month','1/90','1/90');

--
-- Dumping data for table 'ctl_provider'
--

--
-- Dumping data for table 'ctl_specialinstructions'
--

INSERT INTO ctl_specialinstructions VALUES (1,'as needed');
INSERT INTO ctl_specialinstructions VALUES (2,'as needed for pain');
INSERT INTO ctl_specialinstructions VALUES (3,'on an empty stomach');
INSERT INTO ctl_specialinstructions VALUES (4,'until gone');
INSERT INTO ctl_specialinstructions VALUES (5,'before meals');
INSERT INTO ctl_specialinstructions VALUES (6,'after meals');
INSERT INTO ctl_specialinstructions VALUES (7,'with meals');
INSERT INTO ctl_specialinstructions VALUES (8,'before meals and at bedtime');
INSERT INTO ctl_specialinstructions VALUES (9,'as directed');
INSERT INTO ctl_specialinstructions VALUES (10,'in the morning');
INSERT INTO ctl_specialinstructions VALUES (11,'in the evening');
INSERT INTO ctl_specialinstructions VALUES (12,'at bedtime');
INSERT INTO ctl_specialinstructions VALUES (13,'as needed for pain or itching');
INSERT INTO ctl_specialinstructions VALUES (14,'as needed for fever');
INSERT INTO ctl_specialinstructions VALUES (15,'as needed for wheezing');
INSERT INTO ctl_specialinstructions VALUES (16,'while awake');
INSERT INTO ctl_specialinstructions VALUES (17,'1 hour before or 2 hours after');
INSERT INTO ctl_specialinstructions VALUES (18,'with food');
INSERT INTO ctl_specialinstructions VALUES (19,'Apply to affected areas');
INSERT INTO ctl_specialinstructions VALUES (20,'Apply sparingly');
INSERT INTO ctl_specialinstructions VALUES (21,'Insert in left ear');
INSERT INTO ctl_specialinstructions VALUES (22,'Insert in right ear');
INSERT INTO ctl_specialinstructions VALUES (23,'Insert in both ears');
INSERT INTO ctl_specialinstructions VALUES (24,'Insert in left eye');
INSERT INTO ctl_specialinstructions VALUES (25,'Insert in right eye');
INSERT INTO ctl_specialinstructions VALUES (26,'Insert in both eyes');

--
-- Dumping data for table 'demographic'
--

-- It doesn't work and is not useful.
-- INSERT INTO demographic VALUES (1,'Mr.','TEST','PATIENT','','','ON','','905-','','','','1998','06','15','','','','AC','2003-06-04','','','999998','M','0001-01-01','0001-01-01',NULL,'ON','0001-01-01','<rdohip></rdohip><rd></rd>','','','','','','','','');

--
-- Dumping data for table 'demographicaccessory'
--

--
-- Dumping data for table 'demographiccust'
--

--
-- Dumping data for table 'demographicstudy'
--

--
-- Dumping data for table 'desannualreviewplan'
--

--
-- Dumping data for table 'desaprisk'
--

--
-- Dumping data for table 'diagnosticcode'
--

--
-- Dumping data for table 'diseases'
--

--
-- Dumping data for table 'document'
--

--
-- Dumping data for table 'drugs'
--

--
-- Dumping data for table 'dxresearch'
--

--
-- Dumping data for table 'eChart'
--

--
-- Dumping data for table 'eform'
--

INSERT INTO eform VALUES (1,'letter','','letter generator','2010-05-02','10:00:00',NULL,1,'<html><head>\r\n<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\r\n\r\n<title>Rich Text Letter</title>\r\n<style type=\"text/css\">\r\n.butn {width: 140px;}\r\n</style>\r\n\r\n<style type=\"text/css\" media=\"print\">\r\n.DoNotPrint {display: none;}\r\n\r\n</style>\r\n\r\n<script language=\"javascript\">\r\nvar needToConfirm = false;\r\n\r\n//keypress events trigger dirty flag for the iFrame and the subject line\r\ndocument.onkeyup=setDirtyFlag\r\n\r\n\r\nfunction setDirtyFlag() {\r\n	needToConfirm = true; \r\n}\r\n\r\nfunction releaseDirtyFlag() {\r\n	needToConfirm = false; //Call this function if dosent requires an alert.\r\n	//this could be called when save button is clicked\r\n}\r\n\r\n\r\nwindow.onbeforeunload = confirmExit;\r\n\r\nfunction confirmExit() {\r\n	if (needToConfirm)\r\n	return \"You have attempted to leave this page. If you have made any changes without clicking the Submit button, your changes will be lost. Are you sure you want to exit this page?\";\r\n}\r\n\r\n</script>\r\n\r\n\r\n\r\n</head><body onload=\"Start()\" bgcolor=\"FFFFFF\">\r\n\r\n\r\n<!-- START OF EDITCONTROL CODE --> \r\n\r\n<script language=\"javascript\" type=\"text/javascript\" src=\"${oscar_image_path}editControl.js\"></script>\r\n      \r\n<script language=\"javascript\">\r\n\r\n    //put any of the optional configuration variables that you want here\r\n    cfg_width = \'640\';                    //editor control width in pixels\r\n    cfg_height = \'520\';                   //editor control height in pixels\r\n    cfg_editorname = \'edit\';                //the handle for the editor                  \r\n    cfg_isrc = \'${oscar_image_path}\';         //location of the button icon files\r\n    cfg_filesrc = \'${oscar_image_path}\';         //location of the html files\r\n    cfg_template = \'blank.html\';	    //default style and content template\r\n    cfg_formattemplate = \'<option value=\"\">&mdash; template &mdash;</option>  <option value=\"blank\">blank</option>  <option value=\"consult\">consult</option> <option value=\"certificate\">work note</option> <option value=\"narcotic\">narcotic contract</option> <option value=\"MissedAppointment\">missed appt</option> <option value=\"custom\">custom</option></select>\';\r\n    //cfg_layout = \'[all]\';             //adjust the format of the buttons here\r\n    cfg_layout = \r\n\'<table style=\"background-color:#ccccff; width:640px\"><tr id=control1><td>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]</td></tr><tr id=control2><td>[select-block][select-face][select-size][select-template]|[image][clock][date][spell][help]</td></tr></table>[edit-area]\';\r\n    insertEditControl(); // Initialise the edit control and sets it at this point in the webpage\r\n\r\n    function Start() {\r\n        // set eventlistener for the iframe to flag changes in the text displayed \r\n	var agent=navigator.userAgent.toLowerCase(); //for non IE browsers\r\n        if ((agent.indexOf(\"msie\") == -1) || (agent.indexOf(\"opera\") != -1)){\r\n		document.getElementById(cfg_editorname).contentWindow.addEventListener(\'keypress\',setDirtyFlag, true);\r\n	}\r\n\r\n	if (document.getElementById(\'recent_rx\').value.length<1){\r\n		//document.RichTextLetter.RecentMedications.style.visibility=\"hidden\";\r\n		document.getElementById(\'RecentMedications\').style.display = \"none\";\r\n	}\r\n\r\n        // reformat values of multiline database values from \\n lines to <br>\r\n        htmlLine(\'label\');\r\n        htmlLine(\'reminders\');\r\n        htmlLine(\'ongoingconcerns\');\r\n        htmlLine(\'medical_history\');document.getElementById(\'allergies_des\').value\r\n        htmlLine(\'other_medications_history\');  //family history  ... don\'t ask\r\n        htmlLine(\'social_family_history\');  //social history\r\n        htmlLine(\'address\');\r\n        htmlLine(\'NameAddress\');\r\n        htmlLine(\'clinic_label\');\r\n        htmlLine(\'clinic_address\');\r\n        htmlLine(\'druglist_generic\');\r\n        htmlLine(\'druglist_trade\');\r\n        htmlLine(\'recent_rx\');\r\n\r\n	var gender=document.getElementById(\'sex\').value; \r\n	if (gender==\'F\'){\r\n		document.getElementById(\'he_she\').value=\'she\'; \r\n		document.getElementById(\'his_her\').value=\'her\';\r\n		document.getElementById(\'gender\').value=\'female\';\r\n	}\r\n	var mySplitResult = document.getElementById(\'referral_name\').value.toString().split(\',\'); \r\n	document.getElementById(\'referral_nameL\').value=mySplitResult[0];\r\n\r\n	document.getElementById(\'letterhead\').value= genericLetterhead();\r\n\r\n	\r\n        // set the HTML contents of this edit control from the value saved in Oscar (if any)\r\n	var contents=document.getElementById(\'Letter\').value\r\n	if (contents.length==0){\r\n		parseTemplate();\r\n	} else {\r\n		seteditControlContents(cfg_editorname,contents);\r\n	}\r\n    }\r\n \r\n    function htmlLine(theelement) { \r\n	var temp = new Array();\r\n	if (document.getElementById(theelement).value.length>0){\r\n		temp=document.getElementById(theelement).value.split(\'\\n\'); \r\n		contents=\'\';\r\n		var x;\r\n		for (x in temp) {\r\n			contents += temp[x]+\'<br>\';\r\n			}\r\n		document.getElementById(theelement).value=contents;\r\n		}\r\n    }\r\n\r\n    function genericLetterhead() {\r\n        // set the HTML contents of the letterhead\r\n	var address = \'<table border=0><tbody><tr><td><font size=6>\'+document.getElementById(\'clinic_name\').value+\'</font></td></tr><tr><td><font size=2>\'+ document.getElementById(\'clinic_addressLineFull\').value+ \' Fax: \'+document.getElementById(\'clinic_fax\').value+\' Phone: \'+document.getElementById(\'clinic_phone\').value+\'</font><hr></td></tr></tbody></table><br>\'\r\n	if ((document.getElementById(\'clinic_name\').value.toLowerCase()).indexOf(\'amily health team\',0)>-1){\r\n		address=fhtLetterhead();\r\n	}\r\n	return address;\r\n    }\r\n\r\n    function fhtLetterhead() {\r\n        // set the HTML contents of the letterhead using FHT colours\r\n	var address = document.getElementById(\'clinic_addressLineFull\').value+ \'<br>Fax:\'+document.getElementById(\'clinic_fax\').value+\' Phone:\'+document.getElementById(\'clinic_phone\').value ;\r\n	if (document.getElementById(\'doctor\').value.indexOf(\'zapski\')>0){address=\'293 Meridian Avenue, Haileybury, ON P0J 1K0<br> Tel 705-672-2442 Fax 705-672-2384\'};\r\n	address=\'<table style=\\\'text-align: right;\\\' border=\\\'0\\\'><tbody><tr style=\\\'font-style: italic; color: rgb(71, 127, 128);\\\'><td><font size=\\\'+2\\\'>\'+document.getElementById(\'clinic_name\').value+\'</font> <hr style=\\\'width: 100%; height: 3px; color: rgb(212, 118, 0); background-color: rgb(212, 118, 0);\\\'></td> </tr> <tr style=\\\'color: rgb(71, 127, 128);\\\'> <td><font size=\\\'+1\\\'>Family Health Team<br> &Eacute;quipe Sant&eacute; Familiale</font></td> </tr> <tr style=\\\'color: rgb(212, 118, 0); \\\'> <td><small>\'+address+\'</small></td> </tr> </tbody> </table>\';\r\n	return address;\r\n    }\r\n</script>\r\n\r\n<!-- END OF EDITCONTROL CODE -->\r\n\r\n\r\n<form method=\"post\" action=\"\" name=\"RichTextLetter\" >\r\n\r\n<!-- START OF DATABASE PLACEHOLDERS -->\r\n\r\n<input type=\"hidden\" name=\"clinic_name\" id=\"clinic_name\" oscarDB=clinic_name>\r\n<input type=\"hidden\" name=\"clinic_address\" id=\"clinic_address\" oscarDB=clinic_address>\r\n<input type=\"hidden\" name=\"clinic_addressLine\" id=\"clinic_addressLine\" oscarDB=clinic_addressLine>\r\n<input type=\"hidden\" name=\"clinic_addressLineFull\" id=\"clinic_addressLineFull\" oscarDB=clinic_addressLineFull>\r\n<input type=\"hidden\" name=\"clinic_label\" id=\"clinic_label\" oscarDB=clinic_label>\r\n<input type=\"hidden\" name=\"clinic_fax\" id=\"clinic_fax\" oscarDB=clinic_fax>\r\n<input type=\"hidden\" name=\"clinic_phone\" id=\"clinic_phone\" oscarDB=clinic_phone>\r\n<input type=\"hidden\" name=\"clinic_city\" id=\"clinic_city\" oscarDB=clinic_city>\r\n<input type=\"hidden\" name=\"clinic_province\" id=\"clinic_province\" oscarDB=clinic_province>\r\n<input type=\"hidden\" name=\"clinic_postal\" id=\"clinic_postal\" oscarDB=clinic_postal>\r\n\r\n<input type=\"hidden\" name=\"patient_name\" id=\"patient_name\" oscarDB=patient_name>\r\n<input type=\"hidden\" name=\"first_last_name\" id=\"first_last_name\" oscarDB=first_last_name>\r\n<input type=\"hidden\" name=\"patient_nameF\" id=\"patient_nameF\" oscarDB=patient_nameF >\r\n<input type=\"hidden\" name=\"patient_nameL\" id=\"patient_nameL\" oscarDB=patient_nameL >\r\n<input type=\"hidden\" name=\"label\" id=\"label\" oscarDB=label>\r\n<input type=\"hidden\" name=\"NameAddress\" id=\"NameAddress\" oscarDB=NameAddress>\r\n<input type=\"hidden\" name=\"address\" id=\"address\" oscarDB=address>\r\n<input type=\"hidden\" name=\"addressline\" id=\"addressline\" oscarDB=addressline>\r\n<input type=\"hidden\" name=\"phone\" id=\"phone\" oscarDB=phone>\r\n<input type=\"hidden\" name=\"phone2\" id=\"phone2\" oscarDB=phone2>\r\n<input type=\"hidden\" name=\"province\" id=\"province\" oscarDB=province>\r\n<input type=\"hidden\" name=\"city\" id=\"city\" oscarDB=city>\r\n<input type=\"hidden\" name=\"postal\" id=\"postal\" oscarDB=postal>\r\n<input type=\"hidden\" name=\"dob\" id=\"dob\" oscarDB=dob>\r\n<input type=\"hidden\" name=\"dobc\" id=\"dobc\" oscarDB=dobc>\r\n<input type=\"hidden\" name=\"dobc2\" id=\"dobc2\" oscarDB=dobc2>\r\n<input type=\"hidden\" name=\"hin\" id=\"hin\" oscarDB=hin>\r\n<input type=\"hidden\" name=\"hinc\" id=\"hinc\" oscarDB=hinc>\r\n<input type=\"hidden\" name=\"hinversion\" id=\"hinversion\" oscarDB=hinversion>\r\n<input type=\"hidden\" name=\"ageComplex\" id=\"ageComplex\" oscarDB=ageComplex >\r\n<input type=\"hidden\" name=\"age\" id=\"age\" oscarDB=age >\r\n<input type=\"hidden\" name=\"sex\" id=\"sex\" oscarDB=sex >\r\n<input type=\"hidden\" name=\"chartno\" id=\"chartno\" oscarDB=chartno >\r\n\r\n<input type=\"hidden\" name=\"medical_history\" id=\"medical_history\" oscarDB=medical_history>\r\n<input type=\"hidden\" name=\"recent_rx\" id=\"recent_rx\" oscarDB=recent_rx>\r\n<input type=\"hidden\" name=\"druglist_generic\" id=\"druglist_generic\" oscarDB=druglist_generic>\r\n<input type=\"hidden\" name=\"druglist_trade\" id=\"druglist_trade\" oscarDB=druglist_trade>\r\n<input type=\"hidden\" name=\"druglist_line\" id=\"druglist_line\" oscarDB=druglist_line>\r\n<input type=\"hidden\" name=\"social_family_history\" id=\"social_family_history\" oscarDB=social_family_history>\r\n<input type=\"hidden\" name=\"other_medications_history\" id=\"other_medications_history\" oscarDB=other_medications_history>\r\n<input type=\"hidden\" name=\"reminders\" id=\"reminders\" oscarDB=reminders>\r\n<input type=\"hidden\" name=\"ongoingconcerns\" id=\"ongoingconcerns\" oscarDB=ongoingconcerns >\r\n\r\n<input type=\"hidden\" name=\"provider_name_first_init\" id=\"provider_name_first_init\" oscarDB=provider_name_first_init >\r\n<input type=\"hidden\" name=\"current_user\" id=\"current_user\" oscarDB=current_user >\r\n<input type=\"hidden\" name=\"doctor_work_phone\" id=\"doctor_work_phone\" oscarDB=doctor_work_phone >\r\n<input type=\"hidden\" name=\"doctor\" id=\"doctor\" oscarDB=doctor >\r\n\r\n<input type=\"hidden\" name=\"today\" id=\"today\" oscarDB=today>\r\n\r\n<input type=\"hidden\" name=\"allergies_des\" id=\"allergies_des\" oscarDB=allergies_des >\r\n\r\n<!-- PLACE REFERRAL PLACEHOLDERS HERE WHEN BC APCONFIG FIXED -->\r\n<input type=\"hidden\" name=\"referral_name\" id=\"referral_name\" oscarDB=referral_name>\r\n<input type=\"hidden\" name=\"referral_address\" id=\"referral_address\" oscarDB=referral_address>\r\n<input type=\"hidden\" name=\"referral_phone\" id=\"referral_phone\" oscarDB=referral_phone>\r\n<input type=\"hidden\" name=\"referral_fax\" id=\"referral_fax\" oscarDB=referral_fax>\r\n\r\n<!-- END OF DATABASE PLACEHOLDERS -->\r\n\r\n\r\n<!-- START OF MEASUREMENTS PLACEHOLDERS -->\r\n\r\n<input type=\"hidden\" name=\"BP\" id=\"BP\" oscarDB=m$BP#value>\r\n<input type=\"hidden\" name=\"WT\" id=\"WT\" oscarDB=m$WT#value>\r\n<input type=\"hidden\" name=\"smoker\" id=\"smoker\" oscarDB=m$SMK#value>\r\n<input type=\"hidden\" name=\"dailySmokes\" id=\"dailySmokes\" oscarDB=m$NOSK#value>\r\n<input type=\"hidden\" name=\"A1C\" id=\"A1C\" oscarDB=m$A1C#value>\r\n\r\n<!-- END OF MEASUREMENTS PLACEHOLDERS -->\r\n\r\n\r\n<!-- START OF DERIVED PLACEHOLDERS -->\r\n\r\n<input type=\"hidden\" name=\"he_she\" id=\"he_she\" value=\"he\">\r\n<input type=\"hidden\" name=\"his_her\" id=\"his_her\" value=\"his\">\r\n<input type=\"hidden\" name=\"gender\" id=\"gender\" value=\"male\">\r\n<input type=\"hidden\" name=\"referral_nameL\" id=\"referral_nameL\" value=\"Referring Doctor\">\r\n<input type=\"hidden\" name=\"letterhead\" id=\"letterhead\" value=\"Letterhead\">\r\n\r\n<!-- END OF DERIVED PLACEHOLDERS -->\r\n\r\n\r\n<textarea name=\"Letter\" id=\"Letter\" style=\"width:600px; display: none;\"></textarea>\r\n\r\n<div class=\"DoNotPrint\" id=\"control3\" style=\"position:absolute; top:20px; left: 660px;\">\r\n<input type=\"button\" class=\"butn\" name=\"AddLetterhead\" id=\"AddLetterhead\" value=\"Letterhead\" \r\n	onclick=\"doHtml(document.getElementById(\'letterhead\').value);\">\r\n\r\n<br>\r\n<!--\r\n<input type=\"button\" class=\"butn\" name=\"certificate\" value=\"Work Note\" \r\n	onclick=\"document.RichTextLetter.AddLetterhead.click();\r\n 	doHtml(\'<p>\'+doDate()+\'<p>This is to certify that I have today examined <p>\');\r\n	document.RichTextLetter.AddLabel.click();\r\n	doHtml(\'In my opinion, \'+document.getElementById(\'he_she\').value+\' will be unfit for \'+document.getElementById(\'his_her\').value+\' normal work from today to * inclusive.\');\r\n	document.RichTextLetter.Closing.click();\">\r\n<br>\r\n\r\n<input type=\"button\" class=\"butn\" name=\"consult\" value=\"Consult Letter\" \r\n	onclick=\"  var ref=document.getElementById(\'referral_name\').value.toString(); var mySplitResult = ref.split(\',\');\r\n	var gender=document.getElementById(\'sex\').value; if (gender==\'M\'){gender=\'male\';}; if (gender==\'F\'){gender=\'female\';};\r\n	var years=document.getElementById(\'ageComplex\').value; if (years==\'\'){years=document.getElementById(\'age\').value + \'yo\';};\r\n	document.RichTextLetter.AddLetterhead.click();\r\n	doHtml(\'<p>\'+doDate()+\'<p>\');\r\n	document.RichTextLetter.AddReferral.click();\r\n	doHtml(\'<p>RE:&nbsp\');\r\n	document.RichTextLetter.AddLabel.click();\r\n	doHtml(\'<p>Dear Dr. \'+mySplitResult[0]+\'<p>Thank you for asking me to see this \'+years+ \' \' +gender);\r\n	document.RichTextLetter.Closing.click(); \">\r\n<br>\r\n-->\r\n<input type=\"button\" class=\"butn\" name=\"AddReferral\" id=\"AddReferral\" value=\"Referring Block\" \r\n	onclick=\"doHtml(document.getElementById(\'referral_name\').value+\'<br>\'+ document.getElementById(\'referral_address\').value +\'<br>CANADA<br> Tel: \'+ document.getElementById(\'referral_phone\').value+\'<br>Fax:  \'+document.getElementById(\'referral_fax\').value);\">\r\n\r\n<br>\r\n\r\n<input type=\"button\" class=\"butn\" name=\"AddLabel\" id=\"AddLabel\" value=\"Patient Block\" \r\n	onclick=\"doHtml(document.getElementById(\'label\').value);\">\r\n\r\n<br>\r\n\r\n<br>\r\n<input type=\"button\"  class=\"butn\" name=\"MedicalHistory\" value=\"Recent History\" width=30\r\n	onclick=\"var hist=parseText(document.getElementById(\'medical_history\').value); doHtml(hist);\">\r\n<br>\r\n<input type=\"button\"  class=\"butn\" name=\"AddMedicalHistory\" value=\"Full History\" width=30\r\n	onclick=\"doHtml(document.getElementById(\'medical_history\').value); \">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"RecentMedications\" id=\"RecentMedications\" value=\"Recent Prescriptions\"\r\n	onclick=\"doHtml(document.getElementById(\'recent_rx\').value);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"Medlist\" id=\"Medlist\" value=\"Medication List\"\r\n	onclick=\"doHtml(document.getElementById(\'druglist_trade\').value);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"Allergies\" id=\"Allergies\" value=\"Meds & Allergies\"\r\n	onclick=\"var allergy=document.getElementById(\'allergies_des\').value; if (allergy.length>0){allergy=\'<br>Allergies: \'+allergy};doHtml(\'Medications: \'+document.getElementById(\'druglist_line\').value+allergy);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"OtherMedicationsHistory\" value=\"Family History\"\r\n	onclick=\"var hist=parseText(document.getElementById(\'other_medications_history\').value); doHtml(hist);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"AddOtherMedicationsHistory\" value=\"Full Family Hx\"\r\n	onclick=\"doHtml(document.getElementById(\'other_medications_history\').value); \">\r\n\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"AddSocialFamilyHistory\" value=\"Social History\" \r\n	onclick=\"var hist=parseText(document.getElementById(\'social_family_history\').value); doHtml(hist);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"AddReminders\" value=\"Reminders\"\r\n	onclick=\"var hist=parseText(document.getElementById(\'reminders\').value); doHtml(hist);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"AddOngoingConcerns\" value=\"Ongoing Concerns\"\r\n	onclick=\"var hist=parseText(document.getElementById(\'ongoingconcerns\').value); doHtml(hist);\">\r\n<br>\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"Patient\" value=\"Patient Name\"\r\n	onclick=\" doHtml(document.getElementById(\'first_last_name\').value);\">\r\n\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"PatientAge\" value=\"Patient Age\"\r\n	onclick=\"var hist=document.getElementById(\'ageComplex\').value; if (hist==\'\'){hist=document.getElementById(\'age\').value;}; doHtml(hist);\">\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"label\" value=\"Patient Label\"\r\n	onclick=\"var hist=document.getElementById(\'label\').value; doHtml(hist);\">\r\n\r\n\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"PatientSex\" value=\"Patient Gender\"\r\n	onclick=\"doHtml(document.getElementById(\'sex\').value);\">\r\n<br>\r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"Closing\" value=\"Closing Salutation\" \r\n	onclick=\" doHtml(\'<p>Yours Sincerely<p>&nbsp;<p>\'+ document.getElementById(\'provider_name_first_init\').value+\', MD\');\">\r\n \r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"User\" value=\"Current User\"\r\n	onclick=\"var hist=document.getElementById(\'current_user\').value; doHtml(hist);\">\r\n \r\n<br>\r\n<input type=\"button\" class=\"butn\" name=\"Doctor\" value=\"Attending Doctor\"\r\n	onclick=\"var hist=document.getElementById(\'doctor\').value; doHtml(hist);\">\r\n<br>\r\n<br>\r\n\r\n\r\n<br>\r\n</div>\r\n\r\n\r\n<div class=\"DoNotPrint\" >\r\n<input onclick=\"viewsource(this.checked)\" type=\"checkbox\">\r\nHTML Source\r\n<input onclick=\"usecss(this.checked)\" type=\"checkbox\">\r\nUse CSS\r\n	<table><tr><td>\r\n		 Subject: <input name=\"subject\" id=\"subject\" size=\"40\" type=\"text\">\r\n		 <input value=\"Submit\" name=\"SubmitButton\" type=\"submit\" onclick=\"needToConfirm=false;document.getElementById(\'Letter\').value=editControlContents(\'edit\');  document.RichTextLetter.submit()\">\r\n		 <input value=\"Reset\" name=\"ResetButton\" type=\"reset\">\r\n		 <input value=\"Print\" name=\"PrintButton\" type=\"button\" onclick=\"document.getElementById(\'edit\').contentWindow.print();\">\r\n		 <input value=\"Print & Save\" name=\"PrintSaveButton\" type=\"button\" onclick=\"document.getElementById(\'edit\').contentWindow.print();needToConfirm=false;document.getElementById(\'Letter\').value=editControlContents(\'edit\');  setTimeout(\'document.RichTextLetter.submit()\',1000);\">\r\n	 </td></tr></table>\r\n </div>\r\n </form>\r\n\r\n</body></html>\r\n',0,0,NULL,NULL,0,0);
INSERT INTO eform VALUES (2,'Rich Text Letter',NULL,'Rich Text Letter Generator','2014-02-01','10:00:00',NULL,0,'<html><head>\r\n<meta http-equiv="content-type" content="text/html; charset=UTF-8">\r\n\r\n<title>Rich Text Letter</title>\r\n<style type="text/css">\r\n.butn {width: 140px;}\r\n</style>\r\n\r\n<style type="text/css" media="print">\r\n.DoNotPrint {display: none;}\r\n\r\n</style>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}jquery/jquery-1.4.2.js"></script>\r\n\r\n<script language="javascript">\r\nvar needToConfirm = false;\r\n\r\n//keypress events trigger dirty flag for the iFrame and the subject line\r\ndocument.onkeyup=setDirtyFlag\r\n\r\n\r\nfunction setDirtyFlag() {\r\n	needToConfirm = true; \r\n}\r\n\r\nfunction releaseDirtyFlag() {\r\n	needToConfirm = false; //Call this function if dosent requires an alert.\r\n	//this could be called when save button is clicked\r\n}\r\n\r\n\r\nwindow.onbeforeunload = confirmExit;\r\n\r\nfunction confirmExit() {\r\n	if (needToConfirm){\r\n	return "You have attempted to leave this page. If you have made any changes without clicking the Submit button, your changes will be lost. Are you sure you want to exit this page?";\r\n	}\r\n}\r\n\r\n\r\nvar loads=true;\r\n\r\nfunction maximize() {\r\n	window.resizeTo(1030, 865) ;\r\n	loads=false;\r\n}\r\n\r\nfunction saveRTL() {\r\n	needToConfirm=false;\r\n	var theRTL=editControlContents(''edit'');\r\n	var myNewString = theRTL.replace(/"/g, ''&quot;'');\r\n	document.getElementById(''Letter'').value=myNewString.replace(/''/g, "&#39;");\r\n}\r\n</script>\r\n\r\n<!-- START OF EDITCONTROL CODE --> \r\n\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/editControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/APCache.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/imageControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/faxControl.js"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/signatureControl.jsp"></script>\r\n<script language="javascript" type="text/javascript" src="${oscar_javascript_path}eforms/printControl.js"></script>\r\n\r\n<script language="javascript">\r\n	//put any of the optional configuration variables that you want here\r\n	cfg_width = ''840''; //editor control width in pixels\r\n	cfg_height = ''520''; //editor control height in pixels\r\n	cfg_editorname = ''edit''; //the handle for the editor                  \r\n	cfg_isrc = ''../eform/displayImage.do?imagefile=''; //location of the button icon files\r\n	cfg_filesrc = ''../eform/displayImage.do?imagefile=''; //location of the html files\r\n	cfg_template = ''blank.rtl''; //default style and content template\r\n	cfg_formattemplate = ''<option value=""> loading... </option></select>'';\r\n	//cfg_layout = ''[all]'';             //adjust the format of the buttons here\r\n	//cfg_layout = ''<table style="background-color:ccccff; width:840px"><tr id=control1><td>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]</td></tr><tr id=control2><td>[select-block][select-face][select-size][select-template]|[image][clock][date][spell][help]</td></tr></table>[edit-area]'';\r\n	cfg_layout = ''<table style="background-color:ccccff; width:840px"><tr id=control1><td align=center>[bold][italic][underlined][strike][subscript][superscript]|[left][center][full][right]|[unordered][ordered][rule]|[undo][redo]|[indent][outdent][select-all][clean]|[table]\\[text-colour][hilight]</td></tr><tr id=control2><td align=center>[select-block][select-face][select-size][select-template]|[image][link]|[clock][date][spell][cut][copy][paste][help]</td></tr></table>[edit-area]'';\r\n	insertEditControl(); // Initialise the edit control and sets it at this point in the webpage\r\n\r\n	\r\n	function gup(name, url)\r\n	{\r\n		if (url == null) { url = window.location.href; }\r\n		name = name.replace(/[\\[]/,"\\\\\\[").replace(/[\\]]/,"\\\\\\]");\r\n		var regexS = "[\\\\?&]"+name+"=([^&#]*)";\r\n		var regex = new RegExp(regexS);\r\n		var results = regex.exec(url);\r\n		if (results == null) { return ""; }\r\n		else { return results[1]; }\r\n	}\r\n	var demographicNo ="";\r\n\r\n	jQuery(document).ready(function(){\r\n		demographicNo = gup("demographic_no");\r\n		if (demographicNo == "") { demographicNo = gup("efmdemographic_no", jQuery("form").attr(''action'')); }\r\n		if (typeof signatureControl != "undefined") {\r\n			signatureControl.initialize({\r\n				sigHTML:"../signature_pad/tabletSignature.jsp?inWindow=true&saveToDB=true&demographicNo=",\r\n				demographicNo:demographicNo,\r\n				refreshImage: function (e) {\r\n					var html = "<img src=''"+e.storedImageUrl+"&r="+ Math.floor(Math.random()*1001) +"''></img>";\r\n					doHtml(html);		\r\n				},\r\n				signatureInput: "#signatureInput"	\r\n			});\r\n		}		\r\n	});\r\n		\r\n	var cache = createCache({\r\n		defaultCacheResponseHandler: function(type) {\r\n			if (checkKeyResponse(type)) {\r\n				doHtml(cache.get(type));\r\n			}			\r\n			\r\n		},\r\n		cacheResponseErrorHandler: function(xhr, error) {\r\n			alert("Please contact an administrator, an error has occurred.");			\r\n			\r\n		}\r\n	});	\r\n	\r\n	function checkKeyResponse(response) {		\r\n		if (cache.isEmpty(response)) {\r\n			alert("The requested value has no content.");\r\n			return false;\r\n		}\r\n		return true;\r\n	}\r\n	\r\n	function printKey (key) {\r\n		var value = cache.lookup(key); \r\n		if (value != null && checkKeyResponse(key)) { doHtml(cache.get(key)); } 		  \r\n	}\r\n	\r\n	function submitFaxButton() {\r\n		document.getElementById(''faxEForm'').value=true;\r\n		saveRTL();\r\n		setTimeout(''document.RichTextLetter.submit()'',1000);\r\n	}\r\n	\r\n	cache.addMapping({\r\n		name: "_SocialFamilyHistory",\r\n		values: ["social_family_history"],\r\n		storeInCacheHandler: function(key,value) {\r\n			cache.put(this.name, cache.get("social_family_history").replace(/(<br>)+/g,"<br>"));\r\n		},\r\n		cacheResponseHandler:function () {\r\n			if (checkKeyResponse(this.name)) {				\r\n				doHtml(cache.get(this.name));\r\n			}	\r\n		}\r\n	});\r\n	\r\n	\r\n	cache.addMapping({name: "template", cacheResponseHandler: populateTemplate});	\r\n	\r\n	cache.addMapping({\r\n		name: "_ClosingSalutation", \r\n		values: ["provider_name_first_init"],	\r\n		storeInCacheHandler: function (key,value) {\r\n			if (!cache.isEmpty("provider_name_first_init")) {\r\n				cache.put(this.name, "<p>Yours Sincerely<p>&nbsp;<p>" + cache.get("provider_name_first_init") + ", MD");\r\n			}\r\n		},\r\n		cacheResponseHandler:function () {\r\n			if (checkKeyResponse(this.name)) {				\r\n				doHtml(cache.get(this.name));\r\n			}	\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "_ReferringBlock", \r\n		values: ["referral_name", "referral_address", "referral_phone", "referral_fax"], 	\r\n		storeInCacheHandler: function (key, value) {\r\n			var text = \r\n				(!cache.isEmpty("referral_name") ? cache.get("referral_name") + "<br>" : "") \r\n			  + (!cache.isEmpty("referral_address") ? cache.get("referral_address") + "<br>" : "")\r\n			  + (!cache.isEmpty("referral_phone") ? "Tel: " + cache.get("referral_phone") + "<br>" : "")\r\n			  + (!cache.isEmpty("referral_fax") ? "Fax: " + cache.get("referral_fax") + "<br>" : "");\r\n			if (text == "") {\r\n				text = \r\n					(!cache.isEmpty("bc_referral_name") ? cache.get("bc_referral_name") + "<br>" : "") \r\n				  + (!cache.isEmpty("bc_referral_address") ? cache.get("bc_referral_address") + "<br>" : "")\r\n				  + (!cache.isEmpty("bc_referral_phone") ? "Tel: " + cache.get("bc_referral_phone") + "<br>" : "")\r\n				  + (!cache.isEmpty("bc_referral_fax") ? "Fax: " + cache.get("bc_referral_fax") + "<br>" : "");\r\n			}						 \r\n			cache.put(this.name, text)\r\n		},\r\n		cacheResponseHandler: function () {\r\n			if (checkKeyResponse(this.name)) {\r\n				doHtml(cache.get(this.name));\r\n			}\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "letterhead", \r\n		values: ["clinic_name", "clinic_fax", "clinic_phone", "clinic_addressLineFull", "doctor", "doctor_contact_phone", "doctor_contact_fax", "doctor_contact_addr"], \r\n		storeInCacheHandler: function (key, value) {\r\n			var text = genericLetterhead();\r\n			cache.put("letterhead", text);\r\n		},\r\n		cacheResponseHandler: function () {\r\n			if (checkKeyResponse(this.name)) {\r\n				doHtml(cache.get(this.name));\r\n			}\r\n		}\r\n	});\r\n	\r\n	cache.addMapping({\r\n		name: "referral_nameL", \r\n		values: ["referral_name"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n		if (!cache.isEmpty("referral_name")) {\r\n				var mySplitResult =  cache.get("referral_name").toString().split(",");\r\n				cache.put("referral_nameL", mySplitResult[0]);\r\n			} \r\n		}\r\n	});\r\n\r\n	cache.addMapping({\r\n		name: "medical_historyS", \r\n		values: ["medical_history"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n		if (!cache.isEmpty("medical_history")) {\r\n				var mySplitResult =  cache.get("medical_history").toString().split("]]-----");\r\n				cache.put("medical_historyS", mySplitResult.pop());\r\n			} \r\n		}\r\n	});\r\n\r\n	cache.addMapping({\r\n		name: "stamp", \r\n		values: ["stamp_name", "doctor"], \r\n		storeInCacheHandler: function(_key,_val) { \r\n				var imgsrc=pickStamp();\r\n				cache.put("stamp",imgsrc);\r\n		}\r\n	});\r\n\r\n	\r\n	cache.addMapping({\r\n		name: "complexAge", \r\n		values: ["complexAge"], \r\n		cacheResponseHandler: function() {\r\n			if (cache.isEmpty("complexAge")) { \r\n				printKey("age"); \r\n			}\r\n			else {\r\n				if (checkKeyResponse(this.name)) {\r\n					doHtml(cache.get(this.name));\r\n				}\r\n			}\r\n		}\r\n	});\r\n	\r\n	// Setting up many to one mapping for derived gender keys.\r\n	var genderKeys = ["he_she", "his_her", "gender"];	\r\n	var genderIndex;\r\n	for (genderIndex in genderKeys) {\r\n		cache.addMapping({ name: genderKeys[genderIndex], values: ["sex"]});\r\n	}\r\n	cache.addMapping({name: "sex", values: ["sex"], storeInCacheHandler: populateGenderInfo});\r\n	\r\n	function isGenderLookup(key) {\r\n		var y;\r\n		for (y in genderKeys) { if (genderKeys[y] == key) { return true; } }\r\n		return false;\r\n	}\r\n	\r\n	function populateGenderInfo(key, val){\r\n		if (val == ''F'') {\r\n			cache.put("sex", "F");\r\n			cache.put("he_she", "she");\r\n			cache.put("his_her", "her");\r\n			cache.put("gender", "female");				\r\n		}\r\n		else {\r\n			cache.put("sex", "M");\r\n			cache.put("he_she", "he");\r\n			cache.put("his_her", "him");\r\n			cache.put("gender", "male");				\r\n		}\r\n	}\r\n	\r\n	function Start() {\r\n		\r\n			$.ajax({\r\n				url : "efmformrtl_templates.jsp",\r\n				success : function(data) {\r\n					$("#template").html(data);\r\n					loadDefaultTemplate();\r\n				}\r\n			});\r\n	\r\n			$(".cacheInit").each(function() { \r\n				cache.put($(this).attr(''name''), $(this).val());\r\n				$(this).remove();				\r\n			});\r\n			\r\n			// set eventlistener for the iframe to flag changes in the text displayed \r\n			var agent = navigator.userAgent.toLowerCase(); //for non IE browsers\r\n			if ((agent.indexOf("msie") == -1) || (agent.indexOf("opera") != -1)) {\r\n				document.getElementById(cfg_editorname).contentWindow\r\n						.addEventListener(''keypress'', setDirtyFlag, true);\r\n			}\r\n				\r\n			// set the HTML contents of this edit control from the value saved in Oscar (if any)\r\n			var contents = document.getElementById(''Letter'').value\r\n			if (contents.length == 0) {\r\n				parseTemplate();\r\n			} else {\r\n				seteditControlContents(cfg_editorname, contents);\r\n				document.getElementById(cfg_editorname).contentWindow.document.designMode = ''on'';\r\n			}\r\n			maximize();\r\n	}\r\n\r\n	function htmlLine(text) {\r\n		return text.replace(/\\r?\\n/g,"<br>");\r\n	}\r\n\r\n	function genericLetterhead() {\r\n		// set the HTML contents of the letterhead\r\n		var address = ''<table border=0><tbody><tr><td><font size=6>''\r\n				+ cache.get(''clinic_name'')\r\n				+ ''</font></td></tr><tr><td><font size=2>''\r\n				+ cache.get(''doctor_contact_addr'')\r\n				+ '' Fax: '' + cache.get(''doctor_contact_fax'')\r\n				+ '' Phone: '' + cache.get(''doctor_contact_phone'')\r\n				+ ''</font><hr></td></tr></tbody></table><br>'';\r\n		if ( (cache.get(''clinic_name'').toLowerCase()).indexOf(''amily health team'',0)>-1){\r\n		address=fhtLetterhead(); }\r\n		if ( (cache.get(''clinic_name'').toLowerCase()).indexOf(''fht'',0)>-1){\r\n		address=fhtLetterhead(); }\r\n		return address;\r\n	}\r\n\r\n	function fhtLetterhead() {\r\n		// set the HTML contents of the letterhead using FHT colours\r\n		var address = cache.get(''clinic_addressLineFull'')\r\n				+ ''<br>Fax:'' + cache.get(''clinic_fax'')\r\n				+ '' Phone:'' + cache.get(''clinic_phone'');\r\n		if (cache.contains("doctor") && cache.get(''doctor'').indexOf(''zapski'') > 0) {\r\n			address = ''293 Meridian Avenue, Haileybury, ON P0J 1K0<br> Tel 705-672-2442&nbsp;&nbsp; Fax 866-945-5725'';\r\n		}\r\n		address = ''<table style=\\''text-align: right;\\'' border=\\''0\\''><tbody><tr style=\\''font-style: italic; color: rgb(71, 127, 128);\\''><td><font size=\\''+2\\''>''\r\n				+ cache.get(''clinic_name'')\r\n				+ ''</font> <hr style=\\''width: 100%; height: 3px; color: rgb(212, 118, 0); background-color: rgb(212, 118, 0);\\''></td> </tr> <tr style=\\''color: rgb(71, 127, 128);\\''> <td><font size=\\''+1\\''>Family Health Team<br>Equipe Sante Familiale</font></td> </tr> <tr style=\\''color: rgb(212, 118, 0); \\''> <td><small>''\r\n				+ address + ''</small></td> </tr> </tbody> </table>'';\r\n		return address;\r\n	}\r\n\r\n	function pickStamp() {\r\n		// set the HTML contents of the signature stamp\r\n		var mystamp =''<img src="../eform/displayImage.do?imagefile=stamp.png">'';\r\n		if (cache.contains("doctor")) {\r\n			if (cache.get(''doctor'').indexOf(''zapski'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=PHC.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''hurman'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=MCH.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''mith'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=PJS.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''loko'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=FAO.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''urrie'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=LNC.png" width="200" height="100" />'';\r\n				}\r\n			if (cache.get(''doctor'').indexOf(''cdermot'') > 0) {\r\n				mystamp = ''<img src="../eform/displayImage.do?imagefile=TMD.png" width="200" height="100" />'';\r\n				}\r\n		}\r\n		return mystamp;\r\n	}\r\n	var formIsRTL = true;\r\n\r\n</script>\r\n<!-- END OF EDITCONTROL CODE -->\r\n</head><body bgcolor="FFFFFF" onload="Start();">\r\n<form method="post" action="" name="RichTextLetter" ><textarea name="Letter" id="Letter" style="width:600px; display: none;"></textarea>\r\n\r\n<div class="DoNotPrint" id="control3" style="position:absolute; top:20px; left: 860px;">\r\n\r\n<!-- Letter Head -->\r\n<input type="button" class="butn" name="AddLetterhead" id="AddLetterhead" value="Letterhead" onclick="printKey(''letterhead'');">\r\n<br>\r\n\r\n<!-- Referring Block -->\r\n<input type="button" class="butn" name="AddReferral" id="AddReferral" value="Referring Block" onclick="printKey(''_ReferringBlock'');">\r\n<br>\r\n\r\n<!-- Patient Block -->\r\n<input type="button" class="butn" name="AddLabel" id="AddLabel" value="Patient Block" onclick="printKey(''label'');">\r\n<br>\r\n<br> \r\n\r\n<!-- Social History -->\r\n<input type="button" class="butn" name="AddSocialFamilyHistory" value="Social History" onclick="var hist=''_SocialFamilyHistory'';printKey(hist);">\r\n<br>\r\n\r\n<!--  Medical History -->\r\n<input type="button"  class="butn" name="AddMedicalHistory" value="Medical History" width=30 onclick="var hist=''medical_historyS'';printKey(hist);">\r\n<br>\r\n\r\n<!--  Ongoing Concerns -->\r\n<input type="button" class="butn" name="AddOngoingConcerns" value="Ongoing Concerns" onclick="var hist=''ongoingconcerns''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Reminders -->\r\n<input type="button" class="butn" name="AddReminders" value="Reminders"\r\n	onclick="var hist=''reminders''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Allergies -->\r\n<input type="button" class="butn" name="Allergies" id="Allergies" value="Allergies" onclick="printKey(''allergies_des'');">\r\n<br>\r\n\r\n<!-- Prescriptions -->\r\n<input type="button" class="butn" name="Medlist" id="Medlist" value="Prescriptions"	onclick="printKey(''druglist_trade'');">\r\n<br>\r\n\r\n<!-- Other Medications -->\r\n<input type="button" class="butn" name="OtherMedicationsHistory" value="Other Medications" onclick="printKey(''other_medications_history''); ">\r\n\r\n<br>\r\n\r\n<!-- Risk Factors -->\r\n<input type="button" class="butn" name="RiskFactors" value="Risk Factors" onclick="printKey(''riskfactors''); ">\r\n<br>\r\n\r\n<!-- Family History -->\r\n<input type="button" class="butn" name="FamilyHistory" value="Family History" onclick="printKey(''family_history''); ">\r\n<br>\r\n<br>\r\n\r\n<!-- Patient Name --> \r\n<input type="button" class="butn" name="Patient" value="Patient Name" onclick="printKey(''first_last_name'');">\r\n<br>\r\n\r\n<!-- Patient Age -->\r\n<input type="button" class="butn" name="PatientAge" value="Patient Age" onclick="var hist=''ageComplex''; printKey(hist);">\r\n\r\n<br>\r\n\r\n<!-- Patient Label -->\r\n<input type="button" class="butn" name="label" value="Patient Label" onclick="hist=''label'';printKey(hist);">\r\n<br>\r\n\r\n<input type="button" class="butn" name="PatientSex" value="Patient Gender" onclick="printKey(''gender'');">\r\n<br>\r\n<br>\r\n\r\n<!-- Closing Salutation -->\r\n<input type="button" class="butn" name="Closing" value="Closing Salutation" onclick="printKey(''_ClosingSalutation'');">\r\n<br>\r\n\r\n<!-- Signature Stamp -->\r\n<input type="button" class="butn" name="stamp" value="Stamp" onclick="printKey(''stamp'');">\r\n<br>\r\n<!--  Current User -->\r\n<input type="button" class="butn" name="User" value="Current User" onclick="var hist=''current_user''; printKey(hist);">\r\n<br>\r\n\r\n<!-- Attending Doctor -->\r\n<input type="button" class="butn" name="Doctor" value="Doctor (MRP)" onclick="var hist=''doctor''; printKey(hist);">\r\n<br>\r\n<br>\r\n\r\n</div>\r\n\r\n\r\n<div class="DoNotPrint" >\r\n<input onclick="viewsource(this.checked)" type="checkbox">\r\nHTML Source\r\n<input onclick="usecss(this.checked)" type="checkbox">\r\nUse CSS	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Subject: <input name="subject" id="subject" size="40" type="text">		 \r\n\r\n<table><tr id=sig>\r\n<td> <div id="signatureInput">&nbsp;</div></td>\r\n<td> <div id="faxControl">&nbsp;</div></td>\r\n</tr></table>\r\n \r\n \r\n<input value="Submit" name="SubmitButton" type="submit" onclick="saveRTL();  document.RichTextLetter.submit()">\r\n<input value="Print" name="PrintSaveButton" type="button" onclick="document.getElementById(''edit'').contentWindow.print();saveRTL();  setTimeout(''document.RichTextLetter.submit()'',1000);">\r\n<input value="Reset" name="ResetButton" type="reset">\r\n<input value="Print" name="PrintButton" type="button" onclick="document.getElementById(''edit'').contentWindow.print();">\r\n\r\n\r\n    	</div>\r\n\r\n</form>\r\n\r\n</body></html>',0,0,NULL,NULL,0,0);

--
-- Dumping data for table 'eform_data'
--

--
-- Dumping data for table 'eforms'
--

--
-- Dumping data for table 'eforms_data'
--

--
-- Dumping data for table 'encounter'
--

--
-- Dumping data for table 'encounterForm'
--

INSERT INTO encounterForm VALUES ('Annual','../form/formannual.jsp?demographic_no=','formAnnual', '0');
INSERT INTO encounterForm VALUES ('Annual V2','../form/formannualV2.jsp?demographic_no=','formAnnualV2', '0');
INSERT INTO encounterForm VALUES ('ALPHA','../form/formalpha.jsp?demographic_no=','formAlpha', '0');
INSERT INTO encounterForm VALUES ('Rourke','../form/formrourke.jsp?demographic_no=','formRourke', '0');
INSERT INTO encounterForm VALUES ('Rourke2006','../form/formrourke2006.jsp?demographic_no=','formRourke2006', '0');
INSERT INTO encounterForm VALUES ('T2Diabetes','../form/formtype2diabete.jsp?demographic_no=','formType2Diabetes', '0');
INSERT INTO encounterForm VALUES ('Mental Health','../form/formmentalhealth.jsp?demographic_no=','formMentalHealth', '0');
INSERT INTO encounterForm VALUES ('PeriMenopausal','../form/formperimenopausal.jsp?demographic_no=','formPeriMenopausal', '0');
INSERT INTO encounterForm VALUES ('Lab Req','../form/formlabreq.jsp?demographic_no=','formLabReq', '0');
INSERT INTO encounterForm VALUES ('MMSE','../form/formmmse.jsp?demographic_no=','formMMSE', '0');
INSERT INTO encounterForm VALUES ('Pall. Care','../form/formpalliativecare.jsp?demographic_no=','formPalliativeCare', '0');
INSERT INTO encounterForm VALUES ('AR','../form/formar.jsp?demographic_no=','formAR', '0');
INSERT INTO encounterForm VALUES ('ADF','../form/formadf.jsp?demographic_no=','formAdf', '0');
INSERT INTO encounterForm VALUES ('ADFv2', '../form/formadfv2.jsp?demographic_no=', 'formAdfV2', '0');
INSERT INTO encounterForm VALUES ('ImmunAllergies', '../form/formimmunallergy.jsp?demographic_no=', 'formImmunAllergy', '0');
INSERT INTO encounterForm VALUES ('Falls History', '../form/formfalls.jsp?demographic_no=','formFalls',0);
INSERT INTO encounterForm VALUES ('Risk Assessment', '../form/formselfadministered.jsp?demographic_no=','formSelfAdministered',0);
INSERT INTO encounterForm VALUES ('Intake Information', '../form/formintakeinfo.jsp?demographic_no=','formIntakeInfo',0);
INSERT INTO encounterForm VALUES ('SF36', '../form/formSF36.jsp?demographic_no=','formSF36',0);
INSERT INTO encounterForm VALUES ('2 Minute Walk', '../form/form2minwalk.jsp?demographic_no=','form2MinWalk',0);
INSERT INTO encounterForm VALUES ('Self Management', '../form/formselfmanagement.jsp?demographic_no=','formSelfManagement',0);
INSERT INTO encounterForm VALUES ('Self Efficacy', '../form/formselfefficacy.jsp?demographic_no=','formSelfEfficacy',0);
INSERT INTO encounterForm VALUES ('HOME FAST', '../form/formhomefalls.jsp?demographic_no=','formHomeFalls',0);
INSERT INTO encounterForm VALUES ('Cost Questionnaire', '../form/formcostquestionnaire.jsp?demographic_no=','formCostQuestionnaire',0);
INSERT INTO encounterForm VALUES ('FDI Function', '../form/formlatelifeFDIfunction.jsp?demographic_no=','formLateLifeFDIFunction',0); 
INSERT INTO encounterForm VALUES ('FDI Disability', '../form/formlatelifeFDIdisability.jsp?demographic_no=','formLateLifeFDIDisability',0);
INSERT INTO encounterForm VALUES ('CESD', '../form/formCESD.jsp?demographic_no=','formCESD',0);
INSERT INTO encounterForm VALUES ('Caregiver', '../form/formcaregiver.jsp?demographic_no=','formCaregiver',0);
INSERT INTO encounterForm VALUES ('Grip Strength', '../form/formgripstrength.jsp?demographic_no=','formGripStrength',0);
INSERT INTO encounterForm VALUES ('Treatment Preference', '../form/formtreatmentpref.jsp?demographic_no=','formTreatmentPref',0);
INSERT INTO encounterForm VALUES ('Caregiver - SF36', '../form/formSF36caregiver.jsp?demographic_no=','formSF36Caregiver',0);
INSERT INTO encounterForm VALUES ('Vascular Tracker', '../form/SetupForm.do?formName=VTForm&demographic_no=','formVTForm',0);
INSERT INTO encounterForm VALUES ('Growth 0-36m', '../form/formGrowth0_36.jsp?demographic_no=','formGrowth0_36',0);
INSERT INTO encounterForm VALUES ('Letterhead', '../form/formConsultant.jsp?demographic_no=', 'formConsult', 0);
INSERT INTO `encounterForm`(`form_name`,`form_value`,`form_table`,`hidden`) VALUES ('CHF','../form/formchf.jsp?demographic_no=','formchf',0);
INSERT INTO `encounterForm`(`form_name`,`form_value`,`form_table`,`hidden`) VALUES ('Health Passport', '../form/formbchp.jsp?demographic_no=', 'formBCHP', 0);
insert into encounterForm values ('ON AR Enhanced','../form/formonarenhanced.jsp?demographic_no=','formONAREnhancedRecord',0);
INSERT INTO `encounterForm` (`form_name`, `form_value`, `form_table`, `hidden`) VALUES ('HMP Form','../form/HSFOForm2.do?demographic_no=','form_hsfo2_visit',1);
INSERT INTO encounterForm VALUES ('Student Intake Hx','../form/formIntakeHx.jsp?demographic_no=','formIntakeHx', '0');
insert into encounterForm (`form_name`, `form_value`, `form_table`, `hidden`) values('Patient Encounter Worksheet','../form/patientEncounterWorksheet.jsp?demographic_no=','',0);

--
-- Dumping data for table 'encountertemplate'
--

INSERT INTO encountertemplate VALUES ('SOAP','0001-01-01 00:00:00','Subject:\n\nObject: \n\nAssessment: \n\nPlan:\n','<table');
INSERT INTO encountertemplate VALUES ('BILIARY COLIC','0001-01-01 00:00:00','1. Inquiry re at least three of following?\r\npain, description\r\nAND\r\nlocation\r\nfood intolerance\r\nrecurrence\r\nfever\r\n2. Abdominal exam?\r\n3. Chest exam?\r\n4. Heart rate AND rhythm?\r\n5. Blood pressure?\r\n6. CBC?\r\n7. SGOT, serum bilirubin, alkaline phosphatase?\r\n8. Gall bladder X-ray OR ultrasound?\r\n9. Advice re low-fat diet?\r\n10. One follow-up within 1 month?\r\n11. IF recurrent (2nd or greater episode), referral?','Unknow');
INSERT INTO encountertemplate VALUES ('PULMONARY EMPHYSEMA','0001-01-01 00:00:00','1. Inquiry re chest symptoms each visit, at least two of the following?\r\ncough\r\nsputum\r\nwheezing\r\ndyspnea\r\n2. CPE at least once in 2 years?\r\n3. ECG on chart (3 minute)? \r\n4. Chest X-ray at least once in 2 years?\r\n5. IF smoker, advice re smoking?\r\n6. Follow-up at least once yearly?','Unknow');
INSERT INTO encountertemplate VALUES ('OTITIS EXTERNA','0001-01-01 00:00:00','1. Inquiry re symptoms?\r\n2. Ear exam?\r\n3. Evidence of \"normal drum\"?','Unknow');
INSERT INTO encountertemplate VALUES ('URI','0001-01-01 00:00:00','1. Complaint of at least one of the following?\r\nnasal discharge\r\nsore throat\r\nmalaise\r\ncold\r\n2. Duration of symptoms noted?\r\n3. IF cough in history, chest exam?\r\n4. IF patient\r\n5. IF sore throat in history, throat exam?\r\n6. IF narcotic antitussives prescribed, cough in history?\r\n7. IF antibiotics prescribed, was there history of secondary infection (coloured phlegm, or fever > 38 for 3 days or more)\r\nOR\r\nhigh risk (cardiac valvular disease or chronic pulmonary disease)?','Unknow');
INSERT INTO encountertemplate VALUES ('HERPETIC ULCER (EYE)','0001-01-01 00:00:00','1. Inquiry re pain in eye?\r\n2. Red eye noted?\r\n3. Dendrite shaped ulceration noted?\r\n4. Fluorescein staining positive?\r\n5. Referral to ophthalmologist?\r\n6. Steroids used locally?','Unknow');
INSERT INTO encountertemplate VALUES ('INFLUENZA','0001-01-01 00:00:00','1. Inquiry re three of the following?\r\nmyalgia\r\nfever\r\ncough\r\nphlegm type\r\nmalaise\r\n2. Inquiry re duration of symptoms?\r\n3. ENT exam?\r\n4. IF coughing, chest exam?\r\n5. IF antibiotics prescribed, was there history of secondary infection (coloured phlegm, or fever > 38 for 3 days or more)\r\nOR\r\nhigh risk (cardiac valvular disease or chronic pulmonary disease)?','Unknow');
INSERT INTO encountertemplate VALUES ('PEPTIC ULCER','0001-01-01 00:00:00','1. Inquiry re epigastric pain?\r\n2. Inquiry re past history of similar symptoms?\r\n3. Inquiry re relief from antacid or milk?\r\n4. Abdominal exam?\r\n5. UGI series\r\nOR\r\ngastroscopy done?\r\n6. UGI series\r\nOR\r\ngastroscopy demonstrates ulcer crater\r\nAND/OR scarring?\r\n7. IF GASTRIC ulcer demonstrated by UGI series\r\nOR\r\ngastroscopy, procedure repeated within 6 weeks?\r\n8. Instruction re diet?\r\n9. IF smoker, advice re smoking?\r\n10. Instruction re alcohol?\r\n11. Counselling re stress factors?\r\n12. Were any of the following drugs used?\r\noral steroids\r\nnonsteroidal anti-inflammatories\r\nASA\r\ncolchicine\r\n13. Follow-up at least every 6 weeks until asymptomatic\r\nOR\r\nhealing demonstrated by UGI series\r\nOR\r\ngastroscopy?','Unknow');
INSERT INTO encountertemplate VALUES ('ALLERGIC REACTION','0001-01-01 00:00:00','1. Inquiry re type OR description of reaction?\r\n2. Inquiry re site of reaction?\r\n3. Inquiry re severity of reaction?\r\n4. Inquiry re possible causes (eg. food, medications, bites, inhalation)?\r\n5. Examination of affected area(s)?\r\n6. IF \"severe\" reaction, heart rate AND rymthm?\r\n7. Blood pressure?\r\n8. Chest exam?\r\n9. Discussion re allergies OR on chart (3 minute)?\r\n10. IF patient has specific drug allergy, bracelet?\r\n11. IF patient has specific drug allergy, is this recorded in a\r\nconsistent area of the chart?','Unknow');
INSERT INTO encountertemplate VALUES ('HEAD INJURY','0001-01-01 00:00:00','These questions apply only to the INITIAL\r\nPRESENTATION of a head injury.\r\n1. Description of injury?\r\n2. Level of consciousness since injury noted?\r\n3. Cause of injury noted?\r\n4. Mechanism of injury noted?\r\n5. Head and neck exam?\r\n6. ENT exam?\r\n7. Cranial nerves?\r\n8. Neurological exam?\r\n9. Pulse and blood pressure?\r\n10. Level of consciousness and orientation at time of exam?\r\n11. IF depressed consciousness, skull X-ray\r\nOR CAT scan OR referral?\r\n12. IF neck pain\r\nOR tenderness, cervical spine X-ray?\r\n13. Narcotics prescribed (including codeine)?\r\n14. IF sent home, instructions to family or friend re\r\nobservation for change in level of consciousness (i.e.\r\nhead injury sheet)?\r\n15. IF penetrating wound\r\nOR\r\ndeteriorating (i.e. change in sensorium), immediate referral?\r\n16. IF recurrent, discussion re safety measures (eg. helmets)?\r\n17. IF recurrent in child -abuse considered?','Unknow');
INSERT INTO encountertemplate VALUES ('ORCHITIS AND EPIDIDY','0001-01-01 00:00:00','1. Inquiry re location of pain?\r\n2. Inquiry re swelling of testes?\r\n3. Examination of testicles?\r\n4. Comment re tenderness?\r\n5. WBC?\r\n6. Urinalysis?\r\n7. Urine C & S?\r\n8. Support to scrotum?\r\n9. IF epididymitis, antibiotics used?\r\n10. IF antibiotics used, amount AND duration noted?\r\n11. Follow-up within one week?','Unknow');
INSERT INTO encountertemplate VALUES ('DIAPER RASH','0001-01-01 00:00:00','1. Inquiry re duration?\r\n2. Description of rash?\r\n3. IF monilia, comment on mouth?\r\n4. IF monilia, topical antifungal used?\r\n5. Discussion re cleaning at diaper changing?\r\n6. IF \"severe\", follow-up within 1 month?\r\n7. Were fluorinated steroids used?\r\n8. IF thrush also present, oral mycostatin used?','Unknow');
INSERT INTO encountertemplate VALUES ('PROSTATITIS CHRONIC','0001-01-01 00:00:00','1. Inquiry re at least 3 of following?\r\ndysuria\r\nfrequency\r\nperineal pain\r\npainful sexual activity\r\nurethral discharge\r\nlow back pain\r\nnocturia\r\n2. Abdominal exam?\r\n3. Rectal exam?\r\n4. Description of prostate (size and consistency)?\r\n5. Urine C & S?\r\n6. Septra\r\nOR\r\ntetracycline\r\nOR\r\nampicillin\r\nOR\r\nerythromycin used?\r\n7. Antibiotic used for at least 2 weeks?\r\n8. One follow-up?\r\n9. IF symptoms continue beyond one month\r\nOR\r\npyuria for more than one month\r\nOR\r\nbacteriuria for more than one month, consultation and/or referral?','Unknow');
INSERT INTO encountertemplate VALUES ('DYSMENORRHEA','0001-01-01 00:00:00','1. Menstrual history?\r\n2. Inquiry re urinary symptoms?\r\n3. Inquiry re painful periods?\r\n4. IF sexually active, pelvic exam with comment on cervix?\r\n5. Abdominal exam?\r\n6. IF vaginal discharge present, C & S?\r\n7. Follow-up once within 4 months?','Unknow');
INSERT INTO encountertemplate VALUES ('UTI','0001-01-01 00:00:00','1. Inquiry re duration of symptoms?\r\n2. Inquiry re first or recurring episode?\r\n3. Inquiry re at least two of following?\r\nfrequency\r\ndysuria\r\nhematuria\r\nfever\r\n4. Abdominal exam?\r\n5. Presence/absence of flank OR CVA tenderness noted?\r\n6. IF more than 2 infections within one year in female, vaginal exam?\r\n7. Urinalysis AND micro?\r\n8. Urine C & S?\r\n9. IF 3rd or more occurrence (3 minute) in female, IVP?\r\n10. IF 2nd or more occurrence (3 minute) in males, IVP?\r\n11. IF child AND 2nd or more occurrence in chart, voiding cysto-urethrogram?\r\n12. IF antibiotic used, was it one of penicillins, erythromycins, sulfonamides, cephalosporins, Septra/Bactrim, or tetracyclines?\r\n13. IF tetracycline used, was patient\r\n14. IF child\r\nOR\r\ndiscussion re causes of UTI\'s?\r\n15. IF condition persists without definitive diagnosis for more than 3 months, referral?\r\n16. One follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('KNEE INJURIES','0001-01-01 00:00:00','1. Description of how injury happened?\r\n2. Duration of discomfort?\r\n3. Presence/absence of locking or collapse?\r\n4. Presence/absence of swelling?\r\n5. Comment on function?\r\n6. Comment on stability of ligaments?\r\n7. IF effusion persists for more than 72 hours, joint aspiration OR referral?\r\n8. IF locking OR instability, referral?','Unknow');
INSERT INTO encountertemplate VALUES ('DEAFNESS','0001-01-01 00:00:00','1. Inquiry re duration of hearing loss?\r\n2. Inquiry re trauma OR infection OR industrial exposure, on chart (3 minute)?\r\n3. Comment on ear drums at least once per year?\r\n4. Audiogram OR referral to ENT on chart (3 minute)?','Unknow');
INSERT INTO encountertemplate VALUES ('SYPHILIS','0001-01-01 00:00:00','1. Inquiry re exposure?\r\n2. IF skin lesion present, inquiry re duration?\r\n3. IF primary syphilis, presence/absence of chancre noted?\r\n4. IF secondary syphilis, presence/absence of rash noted?\r\n5. Presence/absence of lymphadenopathy noted?\r\n6. VDRL OR STS?\r\n7. IF VDRL OR STS negative, repeated within 2 months?\r\n8. Swab for C & S for gonorrhea?\r\n9. IF antibiotic used, was it one of the penicillins, erythromycins, tetracyclines or spectinomycin?\r\n10. Notification of public health authorities?\r\n11. One follow-up within 2 months?','Unknow');
INSERT INTO encountertemplate VALUES ('INGUINAL HERNIA','0001-01-01 00:00:00','1. Inquiry re presence/absence of vomiting?\r\n2. Inquiry re at least two of following?\r\ninguinal bulge\r\nduration\r\npain\r\n3. Description of inguinal mass including side?\r\n4. Reducible or not noted?\r\n5. IF not reducible AND painful, referral to surgeon within 24 hours?','Unknow');
INSERT INTO encountertemplate VALUES ('DEPRESSION','0001-01-01 00:00:00','1. Inquiry re medications/drugs taken?\r\n2. Inquiry re duration of problem?\r\n3. Inquiry re suicidal thoughts OR statement that depression is mild or minor?\r\n4. IF physical complaints noted, evidence of examination of affected area?\r\n5. CPE within 2 years?\r\n6. Comment on mood OR appearance OR affect?\r\n7. IF antidepressant given, follow-up within 2 weeks?\r\n8. IF first prescription for antidepressant, was duration noted AND was duration\r\n9. IF no antidepressants given, follow-up within 1 month?\r\n10. IF \"suicidal\", referral OR hospitalization?\r\n11. Discussion re stress factors?\r\n12. Were barbiturates prescribed?','Unknow');
INSERT INTO encountertemplate VALUES ('LARYNGITIS OR TRACHE','0001-01-01 00:00:00','1. Duration of symptoms?\r\n2. Presence/absence of cough noted?\r\n3. Throat exam?\r\n4. Chest exam?','Unknow');
INSERT INTO encountertemplate VALUES ('GONORRHEA','0001-01-01 00:00:00','1. Inquiry re time since exposure?\r\n2. Inquiry re sexual contacts?\r\n3. Inquiry re symptoms (discharge, dysuria)?\r\n4. Inquiry re sexual preferences and habits?\r\n5. Genital exam?\r\n6. IF oral sex noted, throat exam?\r\n7. IF anal sex noted, rectal exam?\r\n8. C & S, genital?\r\n9. IF indicated by history, C & S oral AND/OR rectal?\r\n10. Positive culture AND/OR gramstain?\r\n11. Antibiotics according to recommendations of Dept of Health (see list)?\r\n12. One follow-up with repeat cultures within 1 month?\r\n13. Counselling re prevention?\r\n14. Presence/absence of penicillin allergy noted on chart (3 minute)?\r\n15. Refer to Public Health Dept. OR insure follow-up of sexual contacts?\r\n16. VDRL in 6 weeks and 3 months?','Unknow');
INSERT INTO encountertemplate VALUES ('CORONARY ARTERY DISE','0001-01-01 00:00:00','1. Comment on one of the following with each visit?\r\nangina\r\nshortness of breath\r\nankle edema\r\n2. Yearly comment on pain OR nitroglycerines taken?\r\n3. Yearly comment on exercise tolerance?\r\n4. Blood pressure of each visit for this diagnosis (at least 75%)?\r\n5. One CPE by family physician in 2 years?\r\n6. ECG on chart within 2 years?\r\n7. Drug list every 12 months?\r\n8. Dosage of prescribed drugs every 12 months (at least 75%)?\r\n9. IF obesity noted, advice re weight loss?\r\n10. Follow-up at least twice per year?\r\n11. IF congestive heart failure AND use of non-steroidal anti-inflammatory agents OR beta-blockers OR calcium channel blockers, was there a justification statement?','Unknow');
INSERT INTO encountertemplate VALUES ('OSTEOPOROSIS','0001-01-01 00:00:00','\r\n1. Inquiry re presence/absence of pain?\r\n2. Inquiry re dietary history?\r\n3. Inquiry re menopause date?\r\n4. IF pain present, examination of area?\r\n5. Comment re kyphosis?\r\n6. X-ray OR bone density OR cortical thickness?\r\n7. Confirmation of osteoporosis by any of tests in question 6?\r\n8. Increased calcium intake (supplements or dietary)?','Unknow');
INSERT INTO encountertemplate VALUES ('ALCOHOLISM','0001-01-01 00:00:00','1. Alcohol intake, amount per day (when drinking)?\r\n2. Duration of problem?\r\n3. Inquiry re time missed from work?\r\n4. Blood pressure yearly?\r\n5. Chest exam yearly?\r\n6. Yearly comment re condition of skin?\r\n7. Abdominal exam yearly?\r\n8. CPE OR CNS examination within past two years?\r\n9. CBC yearly?\r\n10. Three of following done AT LEAST ONCE; serum protein(AG AND total), SGOT, SGPT, alkaline phosphatase,\r\nprothrombin time, bilirubin?\r\n11. Time since last drink before this office visit noted?\r\n12. Initiate education of patient (AA, Alanon, OR note that counselling was done)?\r\n13. Counselling OR referral to alcohol treatment agency for family member(s)?\r\n14. IF first diagnosis within past 2 years, follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('LACERATIONS','0001-01-01 00:00:00','1. Inquiry re how laceration occurred?\r\n2. Time between injury and visit?\r\n3. Description of wound?\r\n4. IF hand or wrist, comment on function?\r\n5. IF tendons severed, referral?\r\n6. Debridement (washing)?\r\n7. IF sutured, one follow-up?\r\n8. IF no tetanus toxoid within 10 years, injection given?','Unknow');
INSERT INTO encountertemplate VALUES ('STREP THROAT','0001-01-01 00:00:00','1. Inquiry re sore throat?\r\n2. Inquiry re presence/absence of fever?\r\n3. Inquiry re cough?\r\n4. Throat exam?\r\n5. Presence/absence of cervical lymphadenopathy noted?\r\n6. Presence/absence of pharyngeal exudate noted?\r\n7. IF fever OR lymphadenopathy OR exudate OR enlarged tonsils, throat swab C & S?\r\n8. Antibiotic used AND was it one of the penicillins, erythromycins, or cephalosporins?\r\n9. IF antibiotic used AND patient antibiotic used for at least 7 days?\r\n10. One follow-up within two weeks?\r\n11. IF positive strep culture, antibiotics used?','Unknow');
INSERT INTO encountertemplate VALUES ('OTITIS MEDIA -SEROUS','0001-01-01 00:00:00','1. Inquiry re at least two of following; hearing, pain, recurrent URI?\r\n2. Comment re fluid in middle ear OR retracted ear drum?\r\n3. Comment re nose AND throat?\r\n4. IF third episode or more, audiometry OR referral?\r\n5. IF physical findings OR hearing test are abnormal, follow-up until resolved OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('HEMORRHOIDS','0001-01-01 00:00:00','1. Inquiry re pain?\r\n2. Inquiry re bleeding?\r\n3. Rectal exam?\r\n4. IF rectal bleeding, sigmoidoscopic exam on chart (3 minute)?\r\n5. IF patient >40 and no definitive diagnosis for rectal bleeding found on sigmoidoscopic, barium enema?\r\n6. Description of site and location of hemorrhoids?\r\n7. One or more of the following used? dietary (high fibre, avoid constipation) suppositories sitz baths surgical ligation and banding I & D if acute thrombosed hemorrhoids\r\n8. IF symptoms unchanged for more than 3 months, referral?\r\n9. Discussion re high fibre diet and stool softeners?\r\n10. One follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('INFECTIOUS NOMONUCLE','0001-01-01 00:00:00','1. Inquiry re at least two of following?\r\nsore throat\r\nfever\r\nmalaise\r\nlymphadenopathy\r\nabdominal pain\r\n2. Presence/absence of fever noted?\r\n3. Throat exam?\r\n4. Presence/absence of lymphadenopathy noted?\r\n5. Presence/absence of hepatosplenomegaly noted?\r\n6. WBC AND diff.?\r\n7. Mono screen?\r\n8. Positive mono test OR abnormal WBC\'s?\r\n9. Advice re reduced activity?\r\n10. Ampicillin used?\r\n11. IF splenomegaly present, follow-up within 2 weeks?\r\n12. IF splenomegaly not present, follow-up within 4 weeks?\r\n13. IF splenomegaly present, advice re avoidance of contact sports or activities?','Unknow');
INSERT INTO encountertemplate VALUES ('HYPERTHYROIDISM, NEW','0001-01-01 00:00:00','1. Inquiry re one or more of following?\r\nweight loss palpitations\r\ntremulousness restlessness\r\nmuscular weakness\r\nfatigue\r\n2. Thyroid exam?\r\n3. Pulse?\r\n4. Examination of eyes?\r\n5. T4 OR T3 OR resin uptake OR other thyroid tests?\r\n6. T4 AND T3 AND uptake elevated?\r\n7. Follow-up every 6 months?\r\n8. Euthyroid within 6 months OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('TRANSIENT CEREBRAL I','0001-01-01 00:00:00','1. Inquiry re frequency?\r\n2. Inquiry re duration of each episode?\r\n3. Description of symptoms?\r\n4. Neurological exam with description of deficit?\r\n5. Blood pressure?\r\n6. Cardiovascular examination?\r\n7. Presence/absence of bruits in neck?\r\n8. ECG?\r\n9. IF male > 55 years, ASA prescribed as initial medication OR justification noted?\r\n10. IF ASA used, duration AND dosage recorded?\r\n11. IF two or more episodes, referral OR admission to hospital?\r\n12. IF smoker, advice re smoking?\r\n13. One follow-up within one month?\r\n14. Cause (eg. embolus, thrombosis) noted within one month OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('IMPETIGO or PYODERMA','0001-01-01 00:00:00','1. Site noted?\r\n2. IF oral antibiotic used, was it one of penicillins, erythromycins, sulfonamides, tetracyclines, or cephalosporins?\r\n3. IF tetracycline used, was patient','Unknow');
INSERT INTO encountertemplate VALUES ('BASAL AND SQUAMOUS C','0001-01-01 00:00:00','1. Inquiry re duration of lesion?\r\n2. Location of lesion noted?\r\n3. Size of lesion noted?\r\n4. Surgical pathology biopsy OR referral?\r\n5. IF not referred, pathology report positive?\r\n6. Excision OR dessication OR cryosurgery OR referral?\r\n7. IF not referred, follow-up within 1 month?','Unknow');
INSERT INTO encountertemplate VALUES ('CONJUNCTIVITIS','0001-01-01 00:00:00','1. Inquiry re itching OR discharge?\r\n2. Inquiry re duration?\r\n3. Description of conjunctiva?\r\n4. IF ophthalmic steroids used, was cornea stained with fluorescein?','Unknow');
INSERT INTO encountertemplate VALUES ('TRIGEMINAL NEURALGIA','0001-01-01 00:00:00','1. Inquiry re severity of pain?\r\n2. Inquiry re duration of pain?\r\n3. Inquiry re facial pain?\r\n4. Inquiry re initiating stimuli?\r\n5. Neurological exam?\r\n6. IF Tegretol given, liver function tests within 6 weeks?\r\n7. Discussion re natural history of disease OR reassurance?\r\n8. One follow-up within 3 months?','Unknow');
INSERT INTO encountertemplate VALUES ('GLOMERULONEPHRITIS','0001-01-01 00:00:00','1. Inquiry re urination on each visit?\r\n2. Blood pressure?\r\n3. Weight?\r\n4. Urinalysis, routine AND micro yearly?\r\n5. Creatinine yearly?\r\n6. BUN yearly?\r\n7. Creatinine clearance yearly?\r\n8. Serum proteins yearly?\r\n9. Hemoglobin yearly?\r\n10. One of following?\r\nproteinemia reports on chart\r\ngranular casts\r\nrenal biopsy report on chart\r\n11. Follow-up at least yearly?','Unknow');
INSERT INTO encountertemplate VALUES ('SPONTANEOUS ABORTION','0001-01-01 00:00:00','1. Date of LMP noted?\r\n2. Duration of LMP noted?\r\n3. Uterine cramps noted?\r\n4. Amount AND duration of vaginal bleeding?\r\n5. Passage of tissue?\r\n6. Pelvic exam with comment re cervix open or closed?\r\n7. Blood pressure?\r\n8. Pulse?\r\n9. Presence/absence of fever?\r\n10. Pregnancy test?\r\n11. Hemoglobin?\r\n12. Hematocrit?\r\n13. Rh factors?\r\n14. IF tissue available, specimen sent to lab?\r\n15. IF indicated by Rh factors, RHOGAM/Rh immune globulin?\r\n16. One follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('CERVIX, CARCINOMA IN','0001-01-01 00:00:00','1. Inquiry re vaginal discharge, within one month?\r\n2. Inquiry re presence/absence of vaginal spotting, within one month?\r\n3. Description of cervix, within one month?\r\n4. Positive Pap smear?\r\n5. IF class IV smear or worse, referral within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('ECTOPIC PREGNANCY','0001-01-01 00:00:00','1. Inquiry re presence/absence of lower abdominal pain?\r\n2. Inquiry re date of last menstrual period?\r\n3. Inquiry re presence/absence of vaginal bleeding?\r\n4. Pelvic exam?\r\n5. Blood pressure AND pulse?\r\n6. IF purulent vaginal discharge, C & S?\r\n7. Abdominal exam?\r\n8. Pregnancy test?\r\n9. IF pregnancy test negative, Beta-HCG?\r\n10. IF not referred or admitted, pelvic ultrasound?\r\n11. Positive pregnancy test OR positive Beta-HCG?\r\n12. Referral OR admission?','Unknow');
INSERT INTO encountertemplate VALUES ('DIZZINESS, NYD','0001-01-01 00:00:00','1. Inquiry re details of episode?\r\n2. Inquiry re duration of episode?\r\n3. Inquiry re presence/absence of precipitating factors?\r\n4. Inquiry re presence/absence of medications?\r\n5. Blood pressure?\r\n6. Heart rate AND rhythm?\r\n7. Ear exam?\r\n8. Comment on Rhomberg OR reflexes OR nystagmus?\r\n9. IF on diuretics, electrolytes tested?\r\n10. IF problem persists, on second visit blood sugar AND CBC?\r\n11. IF heart irregular, ECG OR Holter monitor?\r\n12. IF condition persists for more than 3 months AND specific diagnosis is made, referral?','Unknow');
INSERT INTO encountertemplate VALUES ('UMBILICAL HERNIA','0001-01-01 00:00:00','*** Patient under 1 year old\r\n1. Well baby care visits?\r\n2. IF surgery done, was justification noted (eg. thin skin, too large, pain, ulceration)?','Unknow');
INSERT INTO encountertemplate VALUES ('DIABETES MELLITUS, T','0001-01-01 00:00:00','1. Inquiry re family history of diabetes on chart (3 minute)?\r\n2. Duration of disease OR starting date on chart (3 minute)?\r\n3. Inquiry re one of following on each visit? \r\nurine sugars blood sugars dietary management patient feels well or ill\r\n4. Weight recorded (at least 75% of visits)?\r\n5. Urine glucose each visit?\r\n6. Comment re cardiovascular system AND blood pressure yearly?\r\n7. Examination of fundi yearly?\r\n8. IF on oral hypoglycemics, at least one blood sugar recorded yearly?\r\n9. IF on insulin, at least two blood sugars recorded yearly?\r\n10. BUN OR creatinine on chart (3 minute)?\r\n11. IF diabetes first diagnosed within past 2 years, evidence of 2 fasting blood sugars > 8.8 mmol/L OR random sugar > 13.8 mmol/L prior to treatment?\r\n12. Diabetic diet - caloric intake noted on chart (3 minute)?\r\n13. Evidence of dietary counselling by a health professional on chart (3 minute)?\r\n14. IF newly diagnosed, follow-up within one month?\r\n15. IF on diet alone, follow-up at least once yearly?\r\n16. IF on oral hypoglycemic OR insulin, follow-up at least twice yearly?\r\n17. Inquiry re sexual dysfunction on chart (3 minute)?\r\n18. IF acetohexamide or chlorpropramide used (Glyburide and Diabeta are OK), was BUN > 9 mmol/L OR was creatinine > 140 mmol/L?\r\n19. IF no ketones in serum or urine, was dietary therapy tried prior to starting oral hypoglycemic?','Unknow');
INSERT INTO encountertemplate VALUES ('HAY FEVER','0001-01-01 00:00:00','1. Inquiry re seasonal complaint?\r\n2. Inquiry re precipitating factors (e.g. ragweed, grass etc.)?\r\n3. One of following present; sneezing, rhinorrhea, nasal congestion?\r\n4. Examination of nose?\r\n5. Seasonal occurrence of nasal congestion?\r\n6. Discussion re air conditioning OR air filters?\r\n7. Discussion re avoidance of plants and pollen specific to patient?','Unknow');
INSERT INTO encountertemplate VALUES ('FIBROMYOSITIS','0001-01-01 00:00:00','1. Inquiry re pain, description AND location?\r\n2. Inquiry re aggrravating AND/OR relieving factors?\r\n3. Inquiry re duration?\r\n4. Inquiry re sleep patterns?\r\n5. Inquiry re symptoms of fatigue AND/OR possible depression?\r\n6. Description of areas of pain?\r\n7. IF trigger point(s) noted, location(s)\r\n8. Hemoglobin AND sed rate within 6 months?\r\n9. Oral steroids used?\r\n10. Discussion re stress factors OR on chart (3 minute)?','Unknow');
INSERT INTO encountertemplate VALUES ('DIVERTICULITIS','0001-01-01 00:00:00','1. Inquiry re abdominal pain?\r\n2. Inquiry re at least one of following?\r\nconstipation\r\ndiarrhea\r\nrectal bleeding\r\nregularity\r\n3. Inquiry re food intolerances?\r\n4. Abdominal exam?\r\n5. Rectal exam?\r\n6. Stool for occult blood OR within 1 year?\r\n7. Barium enema on chart (3 minute)?','Unknow');
INSERT INTO encountertemplate VALUES ('SYNCOPE, NYD','0001-01-01 00:00:00','1. Inquiry re three of the following?\r\nrecurrent or initial episode predisposing factors (stress, pain, hyperventilation) description of event medications taken any associated injuries\r\n2. Inquiry re duration of unconsciousness? \r\n3. Neurological comments (eg. reflexes, pupils, movements)?\r\n4. Blood pressure?\r\n5. Presence/absence of hyperventilation noted?\r\n6. IF > 55 years, ECG?\r\n7. IF 2nd or more episode, blood sugar?\r\n8. IF 2nd or more episode, CBC?\r\n9. IF 2nd or more episode, EEG?\r\n10. IF 2nd or more episode, follow-up within 1 month OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('IMPOTENCE','0001-01-01 00:00:00','1. Inquiry re impotence, constant or intermittant?\r\n2. Inquiry re alcohol use?\r\n3. Inquiry re D.M., or systemic disease?\r\n4. Inquiry re emotional problems?\r\n5. Inquiry re nocturnal erections?\r\n6. Inquiry re medications?\r\n7. Genital exam?\r\n8. Blood pressure?\r\n9. Abdominal exam?\r\n10. Neurological exam?\r\n11. Exam of pulses?\r\n12. Urinalysis?\r\n13. Fasting blood sugar?\r\n14. One follow-up?\r\n15. IF problem persists for > 3 months, referral?\r\n16. Sexual counselling with partner?','Unknow');
INSERT INTO encountertemplate VALUES ('ANXIETY','0001-01-01 00:00:00','1. Statement re symptoms?\r\n2. Inquiry re duration of symptoms?\r\n3. Inquiry re precipitating factors?\r\n4. IF physical complaints noted, evidence of examination of affected area?\r\n5. IF anxiolytic agents used, amount and duration recorded?\r\n6. IF first prescription for medication, follow-up within 2 weeks?\r\n7. Counselling OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('HEMATURIA','0001-01-01 00:00:00','1. Inquiry re first or recurrent episode?\r\n2. Inquiry re frequency of hematuria?\r\n3. Presence/absence of flank pain noted?\r\n4. Presence/absence of dysuria OR frequency noted?\r\n5. Microscopic or gross hematuria noted?\r\n6. Presence/absence of colicky pain noted?\r\n7. Abdominal exam?\r\n8. Flank percussion, findings noted?\r\n9. IF male, rectal AND genital exam?\r\n10. IF female AND 2nd or more episode within 1 year, pelvic exam?\r\n11. Urinalysis AND micro?\r\n12. Urine C & S?\r\n13. BUN AND/OR Creatinine?\r\n14. IF 2nd episode within 2 years, IVP OR referral?\r\n15. IF > 60 years AND source not identified, referral?','Unknow');
INSERT INTO encountertemplate VALUES ('MENORRHAGIA','0001-01-01 00:00:00','1. Inquiry re bleeding pattern, duration AND amount?\r\n2. IF\r\n3. Pelvic exam on initial visit OR when bleeding stops?\r\n4. Hb?\r\n5. Pap smear on initial visit OR when bleeding stops?\r\n6. Cause established OR referral within 3 months of initial visit for problem?','Unknow');
INSERT INTO encountertemplate VALUES ('PROSTATE CANCER','0001-01-01 00:00:00','1. Inquiry re urinary symptoms?\r\n2. Rectal exam at least yearly?\r\n3. Serum acid phosphatase yearly?\r\n4. IF new or changed urinary symptoms, C & S AND urinalysis?\r\n5. Pathology report positive?\r\n6. Follow-up every 6 months?','Unknow');
INSERT INTO encountertemplate VALUES ('VAGINITIS, VULVITIS','0001-01-01 00:00:00','1. Inquiry re at least one of following; vaginal itch (pruritis) vulvar irritation, vaginal odour?\r\n2. Presence/absence of vaginal discharge?\r\n3. Vaginal exam?\r\n4. Vaginal AND/OR cervical C & S OR office examination of discharge in saline or KOH?\r\n5. IF Monilia (Candida) AND topical agent used, was it mystatin OR miconazole OR cotrimoxazole?\r\n6. IF Trichomonas AND systemic or topical agent used, was it metronidazole?\r\n7. IF Gardnerella AND systemic or topical agent used, was it metronidazole OR sulfonamide OR tetracycline?\r\n8. IF Trichomonas, discussion re simultaneous treatment of sexual partner?','Unknow');
INSERT INTO encountertemplate VALUES ('INFERTILITY, FEMALE','0001-01-01 00:00:00','1. Parity noted?\r\n2. Infertility for more than 2 years?\r\n3. Inquiry re medication history?\r\n4. Inquiry re menstrual history?\r\n5. Pelvic exam?\r\n6. Examination of breasts?\r\n7. CPE within 2 years after initial diagnosis?\r\n8. Pap smear AND/OR referral?\r\n9. Semen analysis (sexual partner/husband) \r\nOR referral?\r\n10. BS within 6 months after initial diagnosis OR referral?\r\n11. T3 AND/OR T4 within 6 months after initial diagnosis OR referral?\r\n12. Plan of action noted OR referral?','Unknow');
INSERT INTO encountertemplate VALUES ('LARYNGITIS','0001-01-01 00:00:00','1. Inquiry re duration?\r\n2. Inquiry re smoking (or on chart) (3 minute)?\r\n3. Inquiry re specific cause (eg. shouting, occupation)?\r\n4. Exam of pharynx?\r\n5. IF persistant for more than 6 weeks, laryngoscopic exam OR referral?\r\n6. IF smoker, advice re smoking?','Unknow');
INSERT INTO encountertemplate VALUES ('DDD, CERVICAL','0001-01-01 00:00:00','1. Inquiry re neck pain?\r\n2. Inquiry re presence/absence of trauma?\r\n3. Inquiry re one of following?\r\npain referred to shoulder and arm muscle weakness of forearm paresthesia\r\n4. Comment on reflexes in arms?\r\n5. Comment on presence/absence of weakness in upper extremity muscles?\r\n6. Comment re range of movement of neck OR within 1 year?\r\n7. Cervical spine X-ray on chart (3 minute)?\r\n8. Positive X-ray diagnosis?\r\n9. IF first visit for this episode, follow-up within 6 weeks?\r\n10. Cervical collar used continuously for more than 1 month?','Unknow');
INSERT INTO encountertemplate VALUES ('FRACTURES','0001-01-01 00:00:00','1. Description of accident?\r\n2. Time since accident noted?\r\n3. Place of accident (eg. work related)?\r\n4. Description of fracture including presence/absence of deformity?\r\n5. Presence/absence of swelling?\r\n6. Comment re involvement of neurovascular structures?\r\n7. X-ray of fracture site?\r\n8. IF displaced, evidence of reduction OR referral?\r\n9. Immobilization?\r\n10. IF cast applied to extremity, follow-up within 48 hours?\r\n11. One follow-up?','Unknow');
INSERT INTO encountertemplate VALUES ('ARTHRITIS','0001-01-01 00:00:00','(less than one month - multiple joints)\r\n1. Inquiry re duration of symptoms?\r\n2. Location of joint pains noted?\r\n3. Description of nature OR severity of pain?\r\n4. Inquiry re aggravating OR precipitating factors?\r\n5. Description of inflammation OR swelling?\r\n6. Description of range of movement?\r\n7. On OR before second visit for same problem, CBC?\r\n8. On OR before second visit for same problem, ESR?\r\n9. On OR before second visit for same problem, anti-nuclear factor (i.e. ANF, ANA)?\r\n10. On OR before second visit for same problem, rheumatoid arthritis factor (i.e. RF, RA)?\r\n11. Were systemic steroids prescribed?\r\n12. Advice re rest OR restrict movement of joint?\r\n13. Follow-up within 2 weeks?\r\n14. IF within 1 year of visit for peptic ulcer disease, were anti-inflammatory agents prescribed?','Unknow');
INSERT INTO encountertemplate VALUES ('STASIS DERMATITIS','0001-01-01 00:00:00','1. Comment on location?\r\n2. Inquiry re duration?\r\n3. Presence/absence of varicose veins noted?\r\n4. Description of lesions?\r\n5. One follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('LICE AND SCABIES','0001-01-01 00:00:00','1. Inquiry re itching or pruritis?\r\n2. Location noted?\r\n3. IF scabies, description of skin lesion(s)?\r\n4. Gamma Benzene Hexachloride Lotion OR Shampoo?\r\n5. IF used?\r\n6. Instructions re washing clothing and bed linen?\r\n7. IF scabies, entire family treated?','Unknow');
INSERT INTO encountertemplate VALUES ('BLEPHARITIS','0001-01-01 00:00:00','1. Inquiry re symptoms?\r\n2. Duration of symptoms noted?\r\n3. Fluorinated steroids used?\r\n4. Advice re eye care?','Unknow');
INSERT INTO encountertemplate VALUES ('URETHRITIS, NYD','0001-01-01 00:00:00','1. Inquiry re urinary symptoms?\r\n2. Inquiry re sexual contacts OR injury?\r\n3. Comment re presence/absence of urethral discharge?\r\n4. Genital exam?\r\n5. Urinalysis AND micro?\r\n6. Urine C & S?\r\n7. IF discharge present, urethral swab C & S?\r\n8. VDRL OR STS?\r\n9. IF antibiotic used, was it one of the penicillins, erythromycins, sulfonamides, Septra/Bactrim, cephalosporins, or tetracyclines?\r\n10. One follow-up within 2 weeks?\r\n11. IF urine C & S OR urethral swab C & S still positive after antibiotic treatment, antibiotic changed?\r\n12. IF urine C & S OR urethral swab C & S positive, discussion re notification of sexual partner(s)?\r\n13. IF urethral swab C & S positive for gonorrhea or chlamydia, notification of public health authorities?','Unknow');
INSERT INTO encountertemplate VALUES ('TONSILLITIS, ACUTE','0001-01-01 00:00:00','1. Inquiry re sore throat?\r\n2. Description of tonsils?\r\n3. IF erythromycin, cephalosporin or sulfa used?\r\n4. IF > 4 years and cephalosporin, or sulfa used?\r\n5. IF >= 13 years, was penicillin, erythromycin,\r\ncephalosporin, sulfa, or tetracycline used?\r\n6. IF tetracycline used, was patient','Unknow');
INSERT INTO encountertemplate VALUES ('CYSTITIS, RECURRENT','0001-01-01 00:00:00','1. Comment re any urinary symptoms?\r\n2. Urinalysis or dipstick?\r\n3. Urine for C & S?\r\n4. BUN on chart (3 minute)?\r\n5. IF 3 or more episodes within 3 months, urine culture for TB (acid-fast bacilli)?\r\n6. At least one urinary symptom present OR positive culture?\r\n7. IF culture done, do antibiotics reflect culture \r\nsensitivities?\r\n8. IF child with 2 or more UTI, IVP AND voiding cystogram, OR referral?\r\n9. IF adult female with 3 or more culture proven UTI within\r\n2 years, IVP AND (BUN or Creatinine) OR referral?\r\n10. IF abnormal IVP OR (BUN or Creatinine), referral?','Unknow');
INSERT INTO encountertemplate VALUES ('BRONCHOPNEUMONIA','0001-01-01 00:00:00','1. One of more of following?\r\ncough\r\ndyspnea (shortness of breath)\r\nfever\r\n2. Duration of symptoms noted?\r\n3. Chest exam?\r\n4. Rales in chest?\r\n5. Chest X-ray within one day of diagnosis?\r\n6. IF patient has not improved within 7 days, chest X-ray?\r\n7. IF initial X-ray is positive, repeat within 30 days?\r\n8. Positive X-ray OR rales on examination?\r\n9. IF antibiotics used, dose AND duration recorded, (2/3 of visits)?\r\n10. IF X-ray indicates mycopolasma pneumonia, tetracycline or erythromycin used?\r\n11. Follow-up within one week?','Unknow');
INSERT INTO encountertemplate VALUES ('HEMATOMA,','0001-01-01 00:00:00','1. Inquiry re history of trauma, type noted?\r\n2. Inquiry re spontaneous or traumatic?\r\n3. IF spontaneous, inquiry re previous episodes?\r\n4. IF spontaneous, inquiry re family history of bleeding?\r\n5. Description of size?\r\n6. Description of location?\r\n7. IF spontaneous, CBC, platelets, PT, PTT done?\r\n8. IF seen within 48 hours of onset, ice recommended?\r\n9. IF spontaneous OR recurrent, one follow-up?\r\n10. IF child (other recent or old trauma)?\r\n11. IF AND history of repeated trauma (3 or more within 2 years) skeletal survey?','Unknow');
INSERT INTO encountertemplate VALUES ('KIDNEY OBSTRUCTION','0001-01-01 00:00:00','1. Inquiry re pain? \r\n2. Abdominal exam? \r\n3. Blood pressure? \r\n4. BUN OR creatinine? \r\n5. Urinalysis AND C & S? \r\n6. IVP shows blockage? \r\n7. Referral within 1 week?','Unknow');
INSERT INTO encountertemplate VALUES ('ARRHYTHMIA (CARDIAC)','0001-01-01 00:00:00','1. Inquiry re frequency of chest discomfort or pain? \r\n2. Inquiry re duration of chest discomfort or pain? \r\n3. Inquiry re frequency of palpitations? \r\n4. Inquiry re duration of palpitations? \r\n5. Inquiry re precipitating factors (coffee, tea, alcohol)? \r\n6. Inquiry re medications taken prior to occurrence? \r\n7. Blood pressure? \r\n8. Cardiac rate AND rhythm? \r\n9. Chest exam? \r\n10. ECG on first visit for this problem? \r\n11. IF on digoxin OR diuretics, electrolytes? \r\n12. IF on digoxin AND new arrhythmia present, digoxin level? \r\n13. ECG OR description of irregularity on chart? \r\n14. IF arrhythmia present at time of examination, treatment with medication OR reassurance OR referral? \r\n15. Advice re precipitating factors (eg. coffee, tea, alcohol, stress factors)? \r\n16. IF paroxysmal atrial tachycardia, inquiry re stress factors? \r\n17. Follow-up until specific diagnosis made OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('NASOPHARYNGITIS OR U','0001-01-01 00:00:00','1. Complaint of at least one of the following? \r\nnasal discharge\r\nsore throat\r\nmalaise \r\ncold \r\n2. Duration of symptoms noted? \r\n3. IF cough in history, chest exam?\r\n4. IF patient \r\n5. IF sore throat in history, throat exam?\r\n6. IF narcotic antitussives prescribed, cough in history? \r\n7. IF antibiotics prescribed, was there history of secondary infection (coloured phlegm, or fever > 38 for 3 days or more)OR\r\nhigh risk (cardiac valvular disease or chronic pulmonary disease)? ','Unknow');
INSERT INTO encountertemplate VALUES ('MIGRAINE EQUIVALENTS','0001-01-01 00:00:00','1. Inquiry re presence/absence of aura? \r\n2. IF aura present, inquiry re type of aura? \r\n3. Inquiry re location of pain? \r\n4. Inquiry re change in headaches?\r\n5. Neurological exam within last year? \r\n6. Blood pressure within last year? \r\n7. IF medication prescribed, dosage noted? \r\n8. IF medication prescribed, duration noted?','Unknow');
INSERT INTO encountertemplate VALUES ('CHRONIC PROSTATITIS','0001-01-01 00:00:00','1. Inquiry re at least 3 of following? \r\ndysuria\r\nfrequency\r\nperineal pain\r\npainful sexual activity\r\nurethral discharge \r\nlow back pain\r\nnocturia \r\n2. Abdominal exam? \r\n3. Rectal exam?\r\n4. Description of prostate (size and consistency)? \r\n5. Urine C & S? \r\n6. Septra OR tetracycline OR ampicillin OR\r\nerythromycin used? \r\n7. Antibiotic used for at least 2 weeks?\r\n8. One follow-up? \r\n9. IF symptoms continue beyond one month OR\r\npyuria for more than one month OR  bacteriuria for more than one month, consultation and/or referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('URINARY TRACT INFECT','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. Inquiry re first or recurring episode? \r\n3. Inquiry re at least two of following?\r\nfrequency \r\ndysuria \r\nhematuria\r\nfever \r\n4. Abdominal exam?\r\n5. Presence/absence of flank OR CVA tenderness noted? \r\n6. IF more than 2 infections within one year in female, vaginal exam? \r\n7. Urinalysis AND micro? \r\n8. Urine C & S? \r\n9. IF 3rd or more occurrence (3 minute) in female, IVP? \r\n10. IF 2nd or more occurrence (3 minute) in males, IVP? \r\n11. IF child AND 2nd or more occurrence in chart, voiding cysto-urethrogram? \r\n12. IF antibiotic used, was it one of penicillins, erythromycins, sulfonamides, cephalosporins, Septra/Bactrim, or tetracyclines? \r\n13. IF tetracycline used, was patient\r\n14. IF child OR discussion re causes of UTI\'s? \r\n15. IF condition persists without definitive diagnosis for more than 3 months, referral? \r\n16. One follow-up within one month?','Unknow');
INSERT INTO encountertemplate VALUES ('CONGESTIVE HEART FAI','0001-01-01 00:00:00','1. Inquiry re at least two of following? \r\nshortness of breath \r\nswollen ankles \r\nparoxysmal nocturnal dyspnea \r\nexercise intolerance \r\n2. Current medication list? [HA2 ]> \r\n3. Chest exam? \r\n4. Weight recorded on at least 50% of visits? \r\n5. Blood pressure? \r\n6. Comment on ankles OR jugular venous pressure (J.V.P. or J.V.D.)? \r\n7. Heart rate AND rhythm? \r\n8. ECG within 1 year prior OR within 2 weeks after first diagnosis? \r\n9. IF on diuretics, electrolytes done on 50% of visits? \r\n10. BUN done on 50% of visits? \r\n11. Hemoglobin OR indices (hematocrit, MCV, MCHC) done on 50% of visits? \r\n12. Diuretics prescribed? \r\n13. IF on diuretics, amount and duration recorded? \r\n14. Rest recommended? \r\n15. Advice re diet (eg. low salt)? \r\n16. Follow-up weekly until physician notes \"improved\" or \"stable\"? \r\n17. Narcotics prescribed? ','Unknow');
INSERT INTO encountertemplate VALUES ('CLUSTER HEADACHE','0001-01-01 00:00:00','1. History of attacks in clusters? \r\n2. Attacks acute AND short duration AND recurring several times in 24 hours? \r\n3. Description of headache including two of following? \r\nfacial flushing OR sweating \r\nunilateral lacrimation \r\nnasal congestion \r\n4. Blood pressure? \r\n5. Neurological exam, including note re cranial nerves? \r\n6. One follow-up within 6 months? \r\n7. IF on medication, one follow-up within one month? ','Unknow');
INSERT INTO encountertemplate VALUES ('DYSPLASIA OF CERVIX','0001-01-01 00:00:00','1. Pap smear at least yearly? \r\n2. IF uterus not removed, yearly follow-up? \r\n3. IF present less than one year, record of follow-up time? ','Unknow');
INSERT INTO encountertemplate VALUES ('HEADACHE NYD','0001-01-01 00:00:00','1. Inquiry re at least 6 of the following? \r\nseverity \r\nfrequency \r\nlocation \r\nprecipitating factors \r\nmedication history \r\nduration \r\nassociated symptoms (eg. dizziness, blurred vision) \r\nprevious history of headaches \r\nhistory of head injury \r\nhistory of seizures \r\n2. Neurological exam? \r\n3. Blood pressure? \r\n4. One follow-up within 3 months OR referral? \r\n5. IF no definitive diagnosis within 3 months, referral? \r\n6. Inquiry re stress factors? ','Unknow');
INSERT INTO encountertemplate VALUES ('HERPETIC ULCER, VULV','0001-01-01 00:00:00','1. Inquiry re history of vaginal complaint? \r\n2. Description of lesion? \r\n3. Location of lesion? \r\n4. Viral culture (scraping)? \r\n5. IF initial culture negative AND patient pregnant OR lesion persists, repeat culture? \r\n6. Positive viral culture? \r\n7. IF present at labour, Caesarian section OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('THREATENED ABORTION','0001-01-01 00:00:00','1. Date of LMP noted? \r\n2. Amount of vaginal bleeding? \r\n3. Duration of vaginal bleeding? \r\n4. Uterine cramps? \r\n5. IF heavy bleeding OR continued spotting for one week, pelvic exam? \r\n6. Pregnancy test? \r\n7. Hemoglobin? \r\n8. Hematocrit? \r\n9. Rh factors? \r\n10. Blood type? \r\n11. Positive pregnancy test? \r\n12. Bedrest advised? \r\n13. Progesterone or estrogen used? \r\n14. Follow-up at least once weekly while bleeding? ','Unknow');
INSERT INTO encountertemplate VALUES ('DERMATOPHYTOSIS - RI','0001-01-01 00:00:00','1. Site noted? \r\n2. Extent noted? \r\n3. IF griseofulvin prescribed, skin scraping for C & S? \r\n4. Topical antifungal agent used? \r\n5. IF griseofulvin used, were topical antifungal agents tried for 1 month first? \r\n6. One follow-up within 3 weeks? \r\n7. IF griseofulvin used, CBC within 3 months? ','Unknow');
INSERT INTO encountertemplate VALUES ('AMMENORRHEA - PRIMAR','0001-01-01 00:00:00','1. Family history? \r\n2. Growth history? \r\n3. Sexual development history (secondary sexual characteristics)? \r\n4. No period by age 17? \r\n5. Description of breasts, pubic and axillary hair? \r\n6. Pelvic exam OR referral? \r\n7. Referral by age 18? ','Unknow');
INSERT INTO encountertemplate VALUES ('PARAPHIMOSIS','0001-01-01 00:00:00','1. Inquiry re pain? \r\n2. Foreskin not reducible by patient? \r\n3. Description of penis? \r\n4. Reduction attempted by physician? \r\n5. Counselling on care of penis? \r\n6. IF physician unable to reduce, follow-up or referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('LOW BACK PAIN, NOS,','0001-01-01 00:00:00','1. Inquiry re duration AND location of pain? \r\n2. Inquiry re presence/absence of one of following? \r\nparesthesia \r\nsensory aberrations \r\nradiation of pain \r\n3. Inquiry re presence/absence of trauma? \r\n4. Inquiry re previous episode(s)? \r\n5. Movement of back (flexion OR extension OR lateral flexion OR rotation) noted? \r\n6. Note on reflexes (one of knee OR ankle)? \r\n7. Note on straight leg raising? \r\n8. IF pain persists for more than 1 month, lumbar spine AP AND lateral X-rays? \r\n9. IF narcotic analgesic (except codeine compounds, 30 mg. codeine max.) used, justification statement? \r\n10. Back exercises AND/OR back care instructions? \r\n11. One follow-up? \r\n12. IF still continuously painful after 3 months, consultation OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('NOSEBLEED, ANTERIOR','0001-01-01 00:00:00','1. Inquiry re frequency? \r\n2. Inquiry re duration? \r\n3. Some estimate of blood loss noted? \r\n4. Examination of nose? \r\n5. IF active bleeding at time of visit, blood pressure recorded? \r\n6. IF > 60 years, hemoglobin? \r\n7. IF recurrent nose bleeder (2 episodes within 6 months), CBC AND platelet count AND PT AND PTT on chart? \r\n8. One of the following? \r\ngross bleeding \r\nphysical evidence of bleeding vessel on examination \r\n9. IF packing performed, follow-up within 2 days? \r\n10. IF Hg \r\n11. IF 3 nosebleeds within past 2 days AND not actively bleeding, cautery OR prescription of ointment? \r\n12. IF acute nosebleed, packing AND/OR cautery AND/OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('MUMPS','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. Swelling in parotid area noted? \r\n3. IF male > 11 years, testicular exam? \r\n4. IF analgesic used, was it ASA or acetaminophen? ','Unknow');
INSERT INTO encountertemplate VALUES ('HYPERLIPIDEMIA','0001-01-01 00:00:00','1. Family history OR on chart (3 minute)? \r\n2. Cardiovascular exam? \r\n3. Weight recorded? \r\n4. Abdominal exam? \r\n5. Comment re xanthomas? \r\n6. Blood sugar OR glucose tolerance test? \r\n7. Lipids? \r\n8. Lipid level above lab normal? \r\n9. Discussion re diet? \r\n10. IF obesity noted, discussion re weight reduction? \r\n11. Discussion re alcohol AND/OR exercise? ','Unknow');
INSERT INTO encountertemplate VALUES ('BRONCHITIS,ACUTE','0001-01-01 00:00:00','1. Comment re cough? \r\n2. Comment re sputum? \r\n3. Chest exam? \r\n4. Temperature recorded? \r\n5. IF antibiotics used, dose AND duration recorded (2/3 of the time)? \r\n6. IF smoker, advice re smoking? \r\n7. IF narcotic syrup used, was it prescribed more than once within 30 days? ','Unknow');
INSERT INTO encountertemplate VALUES ('INFANTILE COLIC','0001-01-01 00:00:00','1. Inquiry re at least two of following? \r\nvomiting\r\nbowel movements\r\nburping\r\npassing gas\r\nfluid intake\r\n2. Inquiry re timing of crying? \r\n3. Weight recorded with initial diagnosis? \r\n4. Comment on appearance of baby? \r\n5. Evidence of evening crying after feeding? \r\n6. At least one follow-up with comment on colic status? \r\n7. Evidence of some support for parent(s) by one of following?\r\npublic health nurse\r\nreassurance and/or discussion by family doctor\r\ninvolvement of family members','Unknow');
INSERT INTO encountertemplate VALUES ('DIABETES MELLITUS, J','0001-01-01 00:00:00','1. At least every 6 months, comment re one of following? \r\npolyuria \r\npolydipsia \r\nweight loss \r\n2. Description of fundi at least once yearly OR evidence on chart (3 minute) that patient is followed by an opthalmologist? \r\n3. Yearly fasting blood sugar? \r\n4. Urinalysis on at least 75% of visits? \r\n5. Insulin dosage noted at least once yearly? \r\n6. Evidence that home monitoring of urine glucose OR blood glucose is occurring? \r\n7. Evidence of dietary counselling (CDA diet or diabetic education centre referral) on chart (3 minute)? \r\n8. Follow-up at least twice yearly? \r\n9. Evidence of discussion re effects on normal life (eg. family, friends, activities) at least once yearly? ','Unknow');
INSERT INTO encountertemplate VALUES ('PSORIASIS','0001-01-01 00:00:00','1. Inquiry re duration of lesions OR on chart (3 minute)?\r\n2. Description of lesions (scaly, size, psoriatic etc.) OR on chart (3 minute)? \r\n3. Location of lesions noted OR on chart (3 minute)? \r\n4. IF systemic steroids used, consultant\'s note? \r\n5. IF antimitotic agents used, consultant\'s note? \r\n6. Counselling OR on chart (3 minute)? ','Unknow');
INSERT INTO encountertemplate VALUES ('DEGENERATIVE ARTHRIT','0001-01-01 00:00:00','1. Were oral corticosteroids (steroids) used? ','Unknow');
INSERT INTO encountertemplate VALUES ('ACNE VULGARIS','0001-01-01 00:00:00','1. IF on antibiotics (systemic or topical), duration noted on at least 50% of visits? \r\n2. IF on antibiotics (systemic or topical), type noted of visits? \r\n3. Radiation used in treatment by family physicians? \r\n4. Discussion re causes and treatment with patient? \r\n5. IF systemic antibiotics prescribed, follow-up within 6 weeks? \r\n6. IF \"controlled\", follow-up once per year? \r\n7. IF \"severe\" (failure to respond to treatment by family physician within 6 months), referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('GASTROENTERITIS AND','0001-01-01 00:00:00','1. Inquiry re presence/absence of vomiting? \r\n2. IF vomiting, frequency AND amount noted? \r\n3. Inquiry re frequency AND consistency AND mucus of stools? \r\n4. Inquiry re presence/absence of blood in stools? \r\n5. Inquiry re duration of symptoms? \r\n6. Inquiry re travel history? \r\n7. Abdominal exam? \r\n8. IF child \r\n9. Comment re presence/absence of dehydration? \r\n10. IF failure to respond in 2 days, CBC? \r\n11. IF failure to respond in 2 days, stool cultures? \r\n12. IF failure to respond in 2 days, electrolytes? \r\n13. IF failure to respond in 2 days, stool for occult blood? \r\n14. IF gastroenteritis, presence of diarrhea and vomiting recorded? \r\n15. Antispasmodics OR narcotic antidiarrheals OR antibiotics used? \r\n16. Discussion re avoidance of citrus juices and milk? ','Unknow');
INSERT INTO encountertemplate VALUES ('EPICONDYLITIS','0001-01-01 00:00:00','1. Inquiry re duration? \r\n2. Inquiry re causes? \r\n3. Palpation, findings noted? \r\n4. Localized pain present? \r\n5. Tenderness on palpation of site present? \r\n6. Advice re avoidance of activity that caused or precipitated problem? ','Unknow');
INSERT INTO encountertemplate VALUES ('HYPERTENSION (ANY AG','0001-01-01 00:00:00','1. Inquiry re family history of stroke, M.I. OR on chart (3 minute)? \r\n2. Medications taken listed at least twice in 2 year period? \r\n3. One blood pressure per visit (at least 75%)? \r\n4. Yearly comment on heart AND lungs AND fundi AND weight?\r\n5. ECG, on chart (3 minute)? \r\n6. Urinalysis, on chart (3 minute)? \r\n7. IF smoker, advice re smoking? \r\n8. IF obesity noted, advice re weight loss? \r\n9. IF on medication, at least 2 visits per year? \r\n10. IF patient AND diastolic B.P. > 105 on three consecutive visits, referral? \r\n11. Inquiry re stress factors at least once? \r\n12. Inquiry re alcohol intake at least once? \r\n13. IF oral contraceptives used, was justification noted? \r\n14. IF sympathomimetics used, was justification noted?\r\n15. IF diastolic B.P. > 105 on two consecutive occasions, treated with medication? ','Unknow');
INSERT INTO encountertemplate VALUES ('BREAST LUMP','0001-01-01 00:00:00','1. Inquiry duration? \r\n2. Inquiry re presence/absence of pain? \r\n3. Inquiry re changes relative to menstrual cycle? \r\n4. Size of lump noted? \r\n5. Location of lump noted, specific description OR diagram?\r\n6. Presence/absence of axillary nodes? \r\n7. Referral OR follow-up visit within 4 weeks? \r\n8. IF not previously referred AND lump has not changed OR is larger, one of following done? \r\nreferral \r\naspiration \r\nmammogram \r\nexcision ','Unknow');
INSERT INTO encountertemplate VALUES ('IRON DEFICIENCY ANEM','0001-01-01 00:00:00','** NOTE **\r\nNon-pregnant, new presentation \r\n1. Inquiry re bleeding from bowel? \r\n2. Inquiry re bleeding from other sources (eg. nose, vagina) \r\n3. Inquiry re diet? \r\n4. Hemoglobin OR hematocrit? \r\n5. Indices MCV AND MCHC OR smear? \r\n6. Two of following? \r\nSerum ferritin \r\nserum iron \r\ntotal iron binding capacity \r\n7. Stool for occult blood? \r\n8. Serum ferritin OR hemoglobin microcytic smear OR low indices (MCV and MCHC)? \r\n9. Oral iron prescribed? \r\n10. IF poor diet noted, diet counselling? \r\n11. One follow-up within 6 weeks? \r\n12. IF injectable iron used, justification statement?','Unknow');
INSERT INTO encountertemplate VALUES ('SCARLET FEVER','0001-01-01 00:00:00','','Unknow');
INSERT INTO encountertemplate VALUES ('ATROPHIC VAGINITIS','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\ndyspareunia \r\ndysuria \r\nspotting \r\nvaginal itch \r\n2. Description of vulva AND/OR vagina? \r\n3. Pap smear for karyopyknotic index? \r\n4. IF dysuria, urinalysis AND micro? \r\n5. Vaginal C & S? \r\n6. IF topical agent used, was it Premarin/conjugated estrogen OR dienestrol cream? \r\n7. IF oral estrogen therapy used, follow-up within one year? \r\n8. IF sexual dysfunction OR dyspareunia identified, counselling? ','Unknow');
INSERT INTO encountertemplate VALUES ('IRRITABLE BOWEL','0001-01-01 00:00:00','1. Inquiry re bowel activity OR cramps, once per 6 months? \r\n2. Abdominal exam once per year? \r\n3. Stool for occult blood once per year? \r\n4. Sigmoidoscopic exam OR on chart (3 minute)? \r\n5. UGI series with small bowel follow through OR on chart (3 minute)? \r\n6. Presence of constipation OR diarrhea OR cramps? \r\n7. Barium enema OR on chart (3 minute)? \r\n8. Follow-up at least once within 6 months? \r\n9. Discussion of stress factors? \r\n10. Narcotics used? ','Unknow');
INSERT INTO encountertemplate VALUES ('ANKYLOSING SPONDYLIT','0001-01-01 00:00:00','1. Inquiry re presence/absence of pain? \r\n2. Inquiry re history of stiffness? \r\n3. Yearly comment re stiffness? \r\n4. Yearly comment re range of movement? \r\n5. Yearly comment re presence/absence of deformity? \r\n6. HLA-B27 positive AND X-ray report positive OR consultant\'s report positive? \r\n7. Oral corticosteroids started in primary care? \r\n8. IF on any medication, follow-up yearly? ','Unknow');
INSERT INTO encountertemplate VALUES ('OTITIS MEDIA ACUTE','0001-01-01 00:00:00','1. Description of symptoms? \r\n2. Duration of symptoms? \r\n3. Examination of ears? \r\n4. Comment re one of the following? \r\nred drum \r\nbulging drum \r\nloss of light reflex \r\n5. IF tetracycline or chloramphenicol used, was patient\r\n6. Antibiotics prescribed for at least 10 days? \r\n7. IF > 4 years AND antibiotic used, was it ampicillin, penicillin or erythromycin? \r\n8. IF AND antibiotic used, was it penicillin, amoxicillin, sulfa or erythromycin? \r\n9. One follow-up within 4 weeks of episode with statement of patient\'s condition? ','Unknow');
INSERT INTO encountertemplate VALUES ('ANIMAL BITES','0001-01-01 00:00:00','1. Inquiry re what kind of animal? \r\n2. Inquiry re animal provoked or not? \r\n3. Description of wound? \r\n4. IF no tetanus toxoid within 10 years, injection given? \r\n5. IF animal unprovoked, comment re rabies risk? ','Unknow');
INSERT INTO encountertemplate VALUES ('STY','0001-01-01 00:00:00','1. Painful or swollen eyelid? ','Unknow');
INSERT INTO encountertemplate VALUES ('MYOCARDIAL INFARCTIO','0001-01-01 00:00:00','** NOTE **\r\nQUESTIONS 1 THROUGH 6 SHOULD BE PRESENT ON AT LEAST 75% OF VISITS. \r\n1. Inquiry re chest pain relating to activity? \r\n2. Inquiry re palpitations? \r\n3. Inquiry re dyspnea? \r\n4. Blood pressure? \r\n5. Chest auscultation? \r\n6. Cardiac auscultation (sounds AND rhythm AND murmurs)? \r\n7. Lipids (cholesterol AND triglycerides) within one year of hospital discharge? \r\n8. IF new abnormal rhythm noted, ECG within 2 days? \r\n9. Current medications recorded (name AND dosage)? \r\n10. Inquiry re risk factors (eg. diet, blood pressure, smoking, obesity)? \r\n11. One follow-up by family doctor or specialist within 4 weeks of discharge? \r\n12. Following initial visit after discharge, follow-up at least every 3 months for one year? ','Unknow');
INSERT INTO encountertemplate VALUES ('RHEUMATIC HEAR DISEA','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\ndyspnea on exertion \r\neffort intolerance \r\nchest pain \r\nfatique \r\n2. Description of cardiac sounds, rhythm, murmurs? \r\n3. Blood pressure? \r\n4. IF available in the community, echocardiography? \r\n5. Chest X-ray on chart? \r\n6. IF prophylactic antibiotic used, was it one of the penicillins, cephalosporins, sulfonamides, erythromycins? \r\n7. Advice re antibiotic coverage for instrumentation procedures (eg. dental surgery, urology, gynecology)? ','Unknow');
INSERT INTO encountertemplate VALUES ('FATIGUE, NYD (> 15 Y','0001-01-01 00:00:00','1. Inquiry re duration? \r\n2. Inquiry re relation to physical activity? \r\n3. Inquiry re presence/absence of diurnal variation? \r\n4. Inquiry re personal habits (alcohol, drugs)? \r\n5. Inquiry re stress factors? \r\n6. Inquiry re symptoms of depression (early morning wakening, feeling of worthlessness, weight loss, suicidal thoughts)? \r\n7. IF no positive findings in questions 4, 5 and/or 6 above, general assessment within 6 months? \r\n8. Hemoglobin? \r\n9. Urinalysis and micro? \r\n10. IF mention of abnormal thyroid, T4 OR TSH done? \r\n11. IF patient is on diuretics, electrolyte levels recorded? \r\n12. Drug treatment started before definitive diagnosis? \r\n13. One follow-up within 6 weeks? ','Unknow');
INSERT INTO encountertemplate VALUES ('PROSTATITIS','0001-01-01 00:00:00','1. Inquiry re dysuria? \r\n2. Inquiry re pain? \r\n3. Prostate tender? \r\n4. Urinalysis? \r\n5. Urine C & S? \r\n6. Antibiotics used AND amount noted? \r\n7. Antibiotics used AND duration noted? \r\n8. Counselling re at least one of coffee, alcohol, smoking, spices? \r\n9. Follow-up within 2 weeks? ','Unknow');
INSERT INTO encountertemplate VALUES ('KERATITIS, INFLAMMAT','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\nphotophobia \r\npain in eye \r\nocular discharge \r\ntearing of eye \r\n2. Duration of symptoms? \r\n3. Description of cornea? \r\n4. Fluoroscein staining? \r\n5. Corticosteroid eye drops used? \r\n6. Follow-up within 48 hours? \r\n7. IF not improved within 48 hours, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('MONOARTICULAR ARTHRI','0001-01-01 00:00:00','** NOTE **\r\nOne large joint; ankle, knee, hip, wrist, elbow, shoulder. \r\n1. Inquiry re pain? \r\n2. Site noted? \r\n3. Inquiry re duration of symptoms? \r\n4. Inquiry re presence/absence of trauma? \r\n5. Description of joint? \r\n6. Temperature recorded OR history of fever? \r\n7. One large severely painful joint with abnormalities upon examination? \r\n8. Definitive diagnosis on chart within 3 days OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('DEGENERATIVE DISC DI','0001-01-01 00:00:00','1. Inquiry re low back pain, at least one of following? \r\nduration \r\nlocation \r\nradiation \r\n2. Comment on movement of back, at least one of following? \r\nflexion \r\nextension \r\nlateral flexion \r\nrotation \r\n3. Lumbar X-ray (3 views) on chart (3 minute)? \r\n4. Positive X-ray of lumbar spine on chart (3 minute)? \r\n5. IF narcotic analgesic used, justification statement? \r\n6. Back exercises AND/OR back care instructions? ','Unknow');
INSERT INTO encountertemplate VALUES ('PERFORATION TYMPANIC','0001-01-01 00:00:00','1. Inquiry re cause? \r\n2. Inquiry re pain? \r\n3. Inquiry re discharge? \r\n4. Location of perforation? \r\n5. Size of perforation? \r\n6. Follow-up until resolved or referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('ARTHRITIS, RHEUMATOI','0001-01-01 00:00:00','** NOTE **\r\nThese questions apply only to PREVIOUSLY DIAGNOSED rheumatoid arthritis. \r\n1. Inquiry re pain? \r\n2. Inqiury re stiffness? \r\n3. Inquiry re fatigue? \r\n4. Yearly comment re swollen joints? \r\n5. Yearly comment re limitation of movement? \r\n6. Follow-up at least once per year? \r\n7. IF patient on NSAIDS OR chloroquine OR penicillamine OR methotrexate OR gold (myochrysine), follow-up at least 2 times per year? \r\n8. Note at least once yearly re how patient coping? \r\n9. Was methotrexate OR gold OR oral corticosteroids started by family doctor (search back 3 months only)? \r\n10. IF taking chloroquine, ophthamological consultation AND evidence of opthalmological follow-up yearly? ','Unknow');
INSERT INTO encountertemplate VALUES ('PINWORMS','0001-01-01 00:00:00','1. Inquiry re pruritis of anus or vulva? \r\n2. Examination for eggs AND/OR worms on anus? \r\n3. Pinworm test? \r\n4. Pyrvinium pamovate OR Vanquin used? \r\n5. Whole household treated simultaneously? \r\n6. Positive eggs OR positive worms OR positive pinworm test? ','Unknow');
INSERT INTO encountertemplate VALUES ('RUBELLA','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\nfatigue enlarged glands \r\nmalaise rhinitis\r\nmyalgia conjunctivitis\r\nfever abdominal pain\r\nsore throat ear pain\r\n2. Inquiry re duration of symptom? \r\n3. Rash noted? \r\n4. Presence of posterior auricular nodes noted? ','Unknow');
INSERT INTO encountertemplate VALUES ('CELLULITIS','0001-01-01 00:00:00','1. Inquiry re duration? \r\n2. Site of lesion noted? \r\n3. Extent/size of lesion noted? \r\n4. Temperature recorded? \r\n*** NOTE *** \r\nIF the lesion is larger than 5 inches in diameter OR this is the third or more episode, then questions 5 through 8 apply. \r\n5. IF above, WBC on chart? \r\n6. IF above, urinalysis on chart? \r\n7. IF above, C & S of lesion? \r\n8. IF above, fasting blood sugar within one year? \r\n9. Antibiotics used for at least 7 days? \r\n10. IF antibiotic used, type recorded? \r\n11. IF antibiotic used, amount recorded? \r\n12. Follow-up within 7 days? ','Unknow');
INSERT INTO encountertemplate VALUES ('LYMPHADENOPATHY NYD','0001-01-01 00:00:00','1. Inquiry re location of enlarged glands? \r\n2. Inquiry re duration? \r\n3. Description of node(s)? \r\n4. IF in axilla OR groin, comment on extremity? \r\n5. IF in neck, ear and throat exam? \r\n6. IF non-neck node AND no obvious cause noted, CBC?\r\n7. IF neck node AND no obvious cause noted, infectious monocucleosis screen (Monospot)? \r\n8. IF lesion persists for one month or more at the same size, chest X-ray? \r\n9. IF lesion persists for two months or longer, biopsy of node OR referral? \r\n10. Antibiotic used AND it was one of the penicillins, erythromycins, sulfonamides, cephalosporins, OR tetracyclines? \r\n11. IF the patient was\r\n12. IF no infectious cause noted, one follow-up? ','Unknow');
INSERT INTO encountertemplate VALUES ('BURSITIS','0001-01-01 00:00:00','1. Inquiry re pain OR swelling? \r\n2. Inquiry re location? \r\n3. Inquiry re duration? \r\n4. Description of site of lesion (eg. redness, swelling, fluctuation)? \r\n5. IF infected OR if aspirated OR if I&D done, specimen sent for C&S? \r\n6. IF NSAID prescribed, one follow-up within 1 month? ','Unknow');
INSERT INTO encountertemplate VALUES ('PREGNANCY, DELIVERY,','0001-01-01 00:00:00','1. Ontario antenatal records I AND II? \r\n2. Urinalysis with each visit? \r\n3. Hemoglobin each trimester? \r\n4. IF urinalysis positive for glucose on 2 occasions, blood sugar OR glucose tolerance test OR referral? \r\n5. IF dipstick urinalysis positive, lab report of urinalysis and micro? \r\n6. IF hemoglobin\r\n7. IF any drugs used (except pencillins, vitamins, iron, or antinauseants), comment re teratogenicity? \r\n8. IF blood sugar elevated, discussion of diet with patient? \r\n9. Follow-up monthly for first 7 months, every 2 weeks during the 8th month, and then weekly until delivered? \r\n10. IF X-rays done, pregnancy related OR justification statement? ','Unknow');
INSERT INTO encountertemplate VALUES ('RECTAL BLEEDING','0001-01-01 00:00:00','1. Inquiry re at least 2 of following? \r\namount \r\ntype of bleeding \r\nduration of bleeding \r\n2. Inquiry re bowel habits? \r\n3. Abdominal exam? \r\n4. Rectal exam? \r\n5. Hemoglobin within 1 week? \r\n6. Proctoscopic exam within 1 week? \r\n7. Sigmoidoscopic exam OR referral within 2 weeks? \r\n8. IF > 30, barium enema AND air contrast within 1 month? \r\n9. IF 30 years old or less AND no cause found on sigmoidoscopic, barium enema AND air contrast within 1 month? \r\n10. Barium enema AND/OR sigmoidoscopic exam within 1 month? \r\n11. IF no diagnosis established after 1 month, referral/consultation OR statement of justification? \r\n12. IF not hemorrhoids, one follow-up? ','Unknow');
INSERT INTO encountertemplate VALUES ('URTICARIA','0001-01-01 00:00:00','1. Inquiry re duration of rash? \r\n2. Inquiry re location of rash? \r\n3. Inquiry re possible cause (eg. diet, stress, medications)? \r\n4. Description of lesion? \r\n5. IF life threatening (eg. laryngeal edema, circulatory collapse), epinephrine used? \r\n6. IF systemic steroids used, duration \r\n7. IF recurrent (4 or more occasions), consultation OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('FOLLICULITIS','0001-01-01 00:00:00','1. Inquiry re first or recurrent episode? \r\n2. Description of eruption? \r\n3. Location noted? \r\n4. IF recurrent (3 or more episodes in 1 year), C & S? \r\n5. IF recurrent (3 or more episodes in 1 year), serum glucose OR fasting blood sugar OR glucose tolerance test? \r\n6. IF oral antibiotics used, dosage AND duration recorded? \r\n7. IF folliculitis on face AND male > 16, advice re shaving? \r\n8. IF oral antibiotics used, one follow-up?','Unknow');
INSERT INTO encountertemplate VALUES ('CHEST PAIN-NYD (>18','0001-01-01 00:00:00','1. Location of pain noted? \r\n2. Duration of pain noted? \r\n3. Response to exercise OR posture noted? \r\n4. History of cough OR response to breathing noted? \r\n5. Response to time of eating OR type of food noted? \r\n6. Chest exam? \r\n7. Blood pressure? \r\n8. Heart rate AND rhythm? \r\n9. Presence/absence of chest wall tenderness noted? \r\n10. IF exercise related AND chest not tender, ECG done within 3 days? \r\n11. IF rales OR rhonchi OR dullness in chest, X-ray ordered within 3 days? \r\n12. Statement that there is no cardiac cause OR ECG normal, on this visit OR follow-up within one month? ','Unknow');
INSERT INTO encountertemplate VALUES ('FOREIGN BODY IN NOSE','0001-01-01 00:00:00','1. Inquiry re how foreign body got into nose? \r\n2. Inquiry re which side is affected? \r\n3. IF removed, description of foreign body? \r\n4. IF foreign body not removed, referral to ENT specialist within 24 hours? ','Unknow');
INSERT INTO encountertemplate VALUES ('SKIN ABSCESS','0001-01-01 00:00:00','1. Inquiry re location? \r\n2. Inquiry re recurrent or first attack? \r\n3. Description of size? \r\n4. Presence/absence of fluctuation? \r\n5. Presence/absence of lymphangitis? \r\n6. C & S of pus? \r\n7. IF recurrent, fasting serum glucose? \r\n8. Demonstration of pus? \r\n9. I & D? \r\n10. One follow-up within 10 days? ','Unknow');
INSERT INTO encountertemplate VALUES ('STOMATITIS, MONILIAL','0001-01-01 00:00:00','1. Inquiry re location AND duration of oral lesions? \r\n2. IF adult, inquiry re underlying cause (eg. antibiotics diabetes) OR this information on chart (3 minute)? \r\n3. Presence/absence of plaques in mouth noted? \r\n4. IF lesions unresolved within 2 weeks after therapy started, C & S of lesions for monilia? \r\n5. White plaques in mouth? \r\n6. Local antimonilial agent? \r\n7. Antibiotic used? \r\n8. Follow-up within 2 weeks? ','Unknow');
INSERT INTO encountertemplate VALUES ('BRONCHITIS,CHRONIC','0001-01-01 00:00:00','1. Occupation on chart (3 minute)? \r\n2. Smoking history on chart (3 minute)? \r\n3. Cough productive OR note re presence/absence of change in amount of sputum? \r\n4. Chest breath sounds? \r\n5. CPE at least every two years with detailed description of chest (3 of 6 respiratory signs)? \r\n6. Chest X-ray (2 views) within 3 years? \r\n7. IF patient fails to improve after 21 days continuous medication, chest X-ray? \r\n8. Does patient produce sputum 6 months of the year? \r\n9. IF antibiotics used, dose AND durtion recorded (2/3 of visits)? \r\n10. IF smoker, advice re smoking? \r\n11. Follow-up twice per year? \r\n12. Sedatives, hypnotics, narcotics or antihistamines used? ','Unknow');
INSERT INTO encountertemplate VALUES ('HYPERTENSION, < 75 Y','0001-01-01 00:00:00','1. Inquiry re family history of stroke AND/OR M.I., OR on chart (3 minute)? \r\n2. Right and left arm blood pressure at least once on chart? \r\n3. One blood pressure per visit (at least 75%)? \r\n4. Yearly comment on heart AND lungs AND fundi AND weight? \r\n5. ECG, on chart (3 minute)? \r\n6. Urinalysis, on chart (3 minute)? \r\n7. Were there at least two readings with diastolic greater than 90 OR one reading greater than 105 before drug therapy was started? \r\n8. IF diastolic B.P. > 105 or systolic > 200, first line antihypertensive used (thiazides AND/OR beta blockers)? \r\n9. IF second line antihypertensive were used, were first line antihypertensives tried for at least 3 months? \r\n10. IF smoker, advice re smoking? \r\n11. IF obesity noted, advice re weight loss (eg. diet or exercise)? \r\n12. IF patient on drugs, at least 2 visits per year? \r\n13. IF patient has persistant B.P. > 105, referral after 6 months? \r\n14. Inquiry re stress factors? \r\n15. IF oral contraceptives used, justification noted? \r\n16. IF sympathomimetics used, justification noted? ','Unknow');
INSERT INTO encountertemplate VALUES ('PROSTATE, BENIGN HYP','0001-01-01 00:00:00','1. Inquiry re urinary symptoms, at least one of following?\r\nnocturia \r\nfrequency\r\nstream\r\nurgency \r\n2. Description of prostate? \r\n3. Urinalysis? \r\n4. C & S? \r\n5. IF bladder distended, drained slowly? \r\n6. IF catheterized or obstructed, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('PYELONEPHRITIS, CHRO','0001-01-01 00:00:00','1. Inquiry re 3 of following OR on chart (3 minute)? \r\nurinary frequency \r\nurinary urgency \r\nburning on urination (dysuria) \r\nlumbar back pain \r\nfever \r\nchills \r\n2. Blood pressure at least yearly? \r\n3. Urinalysis AND micro at least once in 2 years? \r\n4. Urine C&S at least once in 2 years? \r\n5. Urine C&S for acid-fast bacilli on chart (3 minute)? \r\n6. BUN OR creatinine at least once in 2 years? \r\n7. IVP on chart (3 minute)? \r\n8. Follow-up at least yearly? ','Unknow');
INSERT INTO encountertemplate VALUES ('IRITIS','0001-01-01 00:00:00','1. Inquiry re at least one of following; blurred vision, painful eye, red eye, photophobia? \r\n2. Description of eye? \r\n3. Referral OR phone consultation? ','Unknow');
INSERT INTO encountertemplate VALUES ('REFLUX ESOPHAGITIS','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. At least two of following present? \r\nheartburn with bending over recumbency \r\nwater brash \r\nintolerance to rich or spicy foods \r\nintolerance to alcohol \r\ndysphagia \r\nbelching \r\n3. Abdominal exam? \r\n4. IF dysphagia present, endoscopy? \r\n5. IF UGI series performed, reflux demonstrated? \r\n6. Advice re elevation of head of bed? \r\n7. Advice re diet (eg. avoid rich foods, spices, alcohol, coffee, tea, late meals, large meals,)? \r\n8. IF obesity noted, advice re weight reduction? ','Unknow');
INSERT INTO encountertemplate VALUES ('STOMATITIS, HERPETIC','0001-01-01 00:00:00','1. Inquiry re pain in mouth? \r\n2. Oral ulcerations noted? ','Unknow');
INSERT INTO encountertemplate VALUES ('ASTHMA','0001-01-01 00:00:00','1. Inquiry re previous episodes? \r\n2. Inquiry re family history OR on chart (3 minute)? \r\n3. Inquiry re occupational history OR on chart (3 minute)? \r\n4. Drugs used for asthma recorded? \r\n5. Amount and duration of asthma drugs recorded? \r\n6. Inquiry re allergies OR on chart (3 minute)? \r\n7. Inquiry re duration of current episode? \r\n8. Description of breathing (eg. wheezing, respiratory distress)? \r\n9. Description of breath sounds? \r\n10. IF steroids used in acute attack, was dosage decreased within 10 days? \r\n11. Wheezing present in history or physical exam? \r\n12. IF smoker, advice re smoking? \r\n13. Advice re avoidance of allergens? \r\n14. Advice re avoidance of precipitating factors? \r\n15. IF on medication for an acute episode, follow-up weekly? \r\n16. Was beta-blocker prescribed? \r\n17. Were parasympathomimetics prescribed? ','Unknow');
INSERT INTO encountertemplate VALUES ('PELVIC INFLAMMATORY','0001-01-01 00:00:00','1. Inquiry re pelvic pain AND vaginal discharge? \r\n2. Inquiry re previous PID OR venereal disease? \r\n3. Inquiry re menstrual history? \r\n4. Pelvic exam with comment re cervical discharge? \r\n5. Comment re adnexal examination? \r\n6. Comment re pelvic tenderness (cervical excitation)? \r\n7. Presence/absence of fever noted? \r\n8. WBC? \r\n9. Urinalysis? \r\n10. Micro? \r\n11. VDRL? \r\n12. Cervical/vaginal C & S? \r\n13. IF bleeding, pregnancy test? \r\n14. Antibiotic used? \r\n15. Follow-up within 10 days? ','Unknow');
INSERT INTO encountertemplate VALUES ('HERPES ZOSTER','0001-01-01 00:00:00','1. Description of lesions? \r\n2. Location of lesions noted? \r\n3. IF lesions on forehead OR physician notes \"ophthalmic distribution\", examination of cornea OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('THYROID NODULE','0001-01-01 00:00:00','1. Inquiry re location? \r\n2. Inquiry re duration? \r\n3. Inquiry re one of following? \r\npalpitations \r\ntremor \r\nweight loss \r\n4. Description of size of lesion? \r\n5. Comment on location (midline or lateral)? \r\n6. Referral OR thyroid function tests (T3 and T4 and TSH) AND I-131 uptake/thyroid scan OR ultrasound? \r\n7. Follow-up within 2 months, OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('PERITONISILLAR ABSCE','0001-01-01 00:00:00','1. Inquiry re sore throat? \r\n2. Inquiry re swallowing difficulties? \r\n3. Throat exam? \r\n4. Description of mass? \r\n5. Referral or hospitalization? ','Unknow');
INSERT INTO encountertemplate VALUES ('FEBRILE CONVULSION,','0001-01-01 00:00:00','1. Description of convulsion? \r\n2. Total time for convulsion noted? \r\n3. Inquiry re previous history of convulsions? \r\n4. Inquiry re fever in preceding 24 hours? \r\n5. Inquiry re illness in preceding 24 hours? \r\n6. Temperature recorded? \r\n7. Presence/absence of neck stiffness noted? \r\n8. ENT exam? \r\n9. Chest exam? \r\n10. Fever within previous 24 hours? \r\n11. IF temperature > 38 C (100.4 F), antipyretics OR instructions re sponging? \r\n12. IF bacterial cause of fever identified, antibiotics used? \r\n13. Discussion re fever therapy (fluids OR sponging OR antipyretics)? \r\n14. Follow-up within two weeks? \r\n15. IF convulsion lasts more than 20 minutes, admission OR immediate referral? \r\n16. Tetracycline used? ','Unknow');
INSERT INTO encountertemplate VALUES ('NOCTURNAL ENURESIS','0001-01-01 00:00:00','** NOTE**\r\nAudit only for patients at least 4 years old. \r\n1. Inquiry re family history of enuresis? \r\n2. Inquiry re frequency of bedwetting? \r\n3. Inquiry re remissions and exacerbations? \r\n4. Genital exam, once on chart (3 minute)? \r\n5. Urinalysis, once on chart (3 minute)? \r\n6. Urine C & S, once on chart (3 minute)? \r\n7. IF urine culture positive, IVP OR ultrasound? \r\n8. IF recurrent positive urine culture, voiding cystogram OR referral? \r\n9. History of bedwetting on chart? \r\n10. Follow-up at least once? \r\n11. Family counselling, parents and child? \r\n12. Management plan on chart involving at least one of following? \r\nmedications \r\ncounselling \r\ndry-night record ','Unknow');
INSERT INTO encountertemplate VALUES ('TENSION HEADACHE','0001-01-01 00:00:00','1. Inquiry re at least five of following? \r\nlocation of pain \r\nduration \r\ntime of onset \r\nfrequency \r\nassociated symptoms (nausea) \r\nfamily history \r\npsychosocial factors \r\n2. Blood pressure within last year? \r\n3. CPE, including neurological exam within last year? \r\n4. Headache is stress related? \r\n5. Stress factors identified AND counselling done? \r\n6. IF on medication, follow-up at least every 3 months? \r\n7. IF narcotic analgesic prescribed, dose AND duration recorded? ','Unknow');
INSERT INTO encountertemplate VALUES ('FAMILY PLANNING - FE','0001-01-01 00:00:00','1. Inquiry re 3 of following? \r\npregnancies \r\nabortions \r\nmenstrual history \r\ngynecological surgery \r\nhistory of PID \r\nsmoking history (# of cigarettes per day) \r\nthrombophlebitis \r\nheadaches (migraines) OR\r\nno risk factors statement? \r\n2. Blood pressure recorded? \r\n3. Pelvic exam? \r\n4. Pap smear within one year of starting birth control? \r\n5. IF on birth control pill or IUD, pap smear yearly? \r\n6. IF on birth control pill, OR justification statement? \r\n7. IF patient smokes more than 15 cigarettes per day OR is > 35 years old, oral contraceptives used? \r\n8. IF history of PID OR nulliparous, IUD used? \r\n9. IF smoker, advice re smoking? \r\n10. Breast self-examination (BSE) noted? \r\n11. Discussion of all methods of contraception on chart? ','Unknow');
INSERT INTO encountertemplate VALUES ('INTERMITTENT CLAUDIC','0001-01-01 00:00:00','1. Inquiry re duration of pain? \r\n2. Inquiry re current smoking status? \r\n3. Presence/absence of pulses in legs? \r\n4. Blood pressure? \r\n5. Comment on abdomen OR aneurysm? \r\n6. Comment on legs, warmth OR hair growth OR colour? \r\n7. CPE within 12 months before OR 6 months after presentation?\r\n8. Cholesterol OR triglycerides? \r\n9. Blood sugar? \r\n10. Pain in legs with exercise or walking, relieved by rest? \r\n11. IF smoker, advice re smoking? \r\n12. Discussion re foot care? ','Unknow');
INSERT INTO encountertemplate VALUES ('CYSTITIS','0001-01-01 00:00:00','1. Inquiry re urinary symptoms, one or more of following? \r\nurgency \r\nfrequency \r\ndysuria \r\nhematuria \r\n2. Inquiry re duration of symptoms? \r\n3. Urine dip for protein AND blood OR urinalysis OR urine culture? \r\n4. Positive culture OR two of following present? \r\nurgency \r\nfrequency \r\ndysuria \r\nhematuria \r\n5. Antibiotic used AND was it one of the sulfas, ampicillin, Septra/Bactrim, or tetracycline? \r\n6. One follow-up AND repeat urinalysis OR\r\nculture? \r\n7. Was a negative culture on chart at end of treatment? \r\n8. Was streptomycin or chloromycetin used? ','Unknow');
INSERT INTO encountertemplate VALUES ('HYPOTHROIDISM','0001-01-01 00:00:00','1. Inquiry re previous thyroid treatment? \r\n2. Inquiry re at least one of following? \r\nsensitivity to cold chronic fatigue \r\nmental dullness menses\r\ngeneralized weakness constipation\r\n3. Thyroid exam? \r\n4. Reflex exam? \r\n5. Comment re at least one of the following? \r\ndry skin voice change\r\nmyxedema lethargy\r\n6. T4 done? \r\n7. T4 repeat, every second dosage change? \r\n8. One of; low T4, low T3, or low uptake, or high TSH? \r\n9. IF newly diagnosed (within last two years), extracts used? \r\n10. IF lab test normal? \r\n11. IF >= 60, follow-up every 2 weeks until euthyroid or lab tests normal? \r\n12. Euthyroid within 6 months OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('ABDOMINAL PAIN, NOS','0001-01-01 00:00:00','1. Inquiry re type of pain? \r\n2. Inquiry re duration of pain? \r\n3. Inquiry re location of pain? \r\n4. Inquiry re presence/absence of specific food intolerances? \r\n5. Inquiry re presence/absence of GI symptoms? \r\n6. Inquiry re presence/absence of fever? \r\n7. IF female, inquiry re menstrual history? \r\n8. Chest exam? \r\n9. Abdominal exam? \r\n10. Presence/absence of tenderness noted? \r\n11. IF female AND pelvic pain or lower left or right quadrant pain, pelvic exam? \r\n12. IF male AND pelvic pain or lower left or right quadrant pain, rectal exam? \r\n13. Urinalysis AND micro? \r\n14. IF abnormal urine, C & S? \r\n15. IF 2nd episode of abdominal pain NOS, C & S? ','Unknow');
INSERT INTO encountertemplate VALUES ('NASOPHARYNGITIS, CHR','0001-01-01 00:00:00','1. History of one of following? \r\nnasal spray \r\nnasal stuffiness \r\npost-nasal drip \r\ncigarette smoking \r\nexposure to dust or fumes \r\n2. Description of nasal mucosa? \r\n3. Advice re irritants (stop smoking, avoid dust and fumes)? ','Unknow');
INSERT INTO encountertemplate VALUES ('EPILEPSY','0001-01-01 00:00:00','1. Type AND description of seizures? \r\n2. Frequency of seizures noted? \r\n3. Time of occurrence of seizures noted (eg. day, night, at work, at school, etc.)? \r\n4. Inquiry re precipitating factors on chart (3 minute)? \r\n5. Inquiry re family history of seizure disorder on chart (3 minute)? \r\n6. Neurological exam on chart (3 minute)? \r\n7. EEG on chart (3 minute)? \r\n8. IF neurological exam \"abnormal\", CAT scan on chart (3 minute) OR referral? \r\n9. Description of seizure by witness on chart (3 minute)? \r\n10. Names of drug(s) AND dosage? \r\n11. Discussion re dangerous activities (eg. driving car, working with machinery, etc.) on chart (3 minute)? \r\n12. Discussion re precipitating factors? \r\n13. IF seizures persist (more than one per week), referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('THERAPEUTIC ABORTION','0001-01-01 00:00:00','1. Obstetrical history? \r\n2. Date of LMP noted? \r\n3. Parity noted? \r\n4. Pelvic exam, findings noted OR referral? \r\n5. Estimate of size of uterus OR weeks of preqnancy OR referral? \r\n6. Rh factor? \r\n7. Pregnancy test done? \r\n8. Pregnancy test positive? \r\n9. IF indicated by Rh factors, RHOGAM/Rh immune globulin? \r\n10. Admit for D & C OR referral? \r\n11. Family planning OR birth control counselling? \r\n12. One follow-up within 6 weeks after abortion? ','Unknow');
INSERT INTO encountertemplate VALUES ('MENOPAUSAL SYNDROME','0001-01-01 00:00:00','1. Inquiry re menstrual history (all of: cycle, flow, LMP)? \r\n2. Inquiry re hot flashes/flushes? \r\n3. CPE within one year after initial diagnosis? \r\n4. Pap smear within one year after initial diagnosis? \r\n5. IF Premarin OR conjugated estrogens used, cyclical use OR progestational agent added (5 days per 3 months)? \r\n6. Discussion re post-menopausal sexual problems (i.e. lack of lubrication) on chart (3 minute)? ','Unknow');
INSERT INTO encountertemplate VALUES ('HYPERTHYROIDISM, TRE','0001-01-01 00:00:00','1. Inquiry re at least one of the following at each visit? \r\nenergy \r\nweight \r\nheat sensitivity \r\n2. If new patient to practice within past 2 years, inquiry re duration of disease? \r\n3. If new patient to practice within past 2 years, examination of thyroid and eyes noted? \r\n4. Heart rate OR pulse at each visit? \r\n5. T3 RIA OR TSH yearly? \r\n6. At least one abnormal thyroid test on chart (3 minute), TSH down OR T4 up? \r\n7. Follow-up yearly? ','Unknow');
INSERT INTO encountertemplate VALUES ('ANEMIA, NYD','0001-01-01 00:00:00','*** NOTE ***\r\nNon-pregnant \r\n1. Inquiry re blood loss? \r\n2. Inquiry re diet? \r\n3. CPE within 6 months? \r\n4. On presenting visit, at least three of following? \r\nblood pressure \r\npulse \r\nabdominal exam \r\nrectal exam \r\n5. Hb OR hematocrit? \r\n6. IF no history of blood loss as cause, blood smear for indices? \r\n7. IF black patient, sickle cell screen? \r\n8. IF male over 19, hemoglobin OR IF female over 17, hemoglobin \r\n9. IF macrocytic indices OR smear (pancytopenia, macro-ovalocytosis, hypersegmentation of neutrophils), folate AND B12 test? \r\n10. IF microcytic indices OR smear microcytic, hypochromic) AND no obvious cause for bleeding, stool for occult blood? \r\n11. IF melena stool OR occult blood positive, barium enema? \r\n12. IF GI symptoms OR upper GI bleeding, UGI series? \r\n13. IF vitamin B12 injections given, documented B12 deficiency?\r\n14. IF iron therapy used, documented iron deficiency? \r\nSerum ferritin OR hemoglobin microcytic smear OR low indices (MCV and MCHC)? ','Unknow');
INSERT INTO encountertemplate VALUES ('PYELONEPHRITIS, ACUT','0001-01-01 00:00:00','1. Inquiry re at least 3 of following? \r\nurinary frequency \r\nurinary urgency \r\nburning on urination (dysuria) \r\nlumbar back pain \r\nfever \r\nchills \r\n2. Presence/absence of fever noted? \r\n3. Presence/absence of lumbar (CVA) tenderness? \r\n4. Urinalysis AND micro? \r\n5. WBC? \r\n6. Urine C&S prior to treatment? \r\n7. Urine C&S positive? \r\n8. Antibiotic used AND was it one of penicillins, sulfonamides, Septra/Bactrim, cephalosporins, or tetracyclines? \r\n9. IF tetracycline used, was patient \r\n10. Was antibiotic used for 7 days or more initially? \r\n11. IF lab report indicates that organism not sensitive to initial antibiotic used, was antibiotic changed OR did physician indicate \"patient better\"? \r\n12. One follow-up within two weeks? \r\n13. Repeat urine C&S after treatment? ','Unknow');
INSERT INTO encountertemplate VALUES ('PITYRIASIS ROSEA','0001-01-01 00:00:00','1. Inquiry re duration of rash? \r\n2. Inquiry re herald patch? \r\n3. Description of distribution? \r\n4. VDRL? \r\n5. Oral steroids used? \r\n6. Counselling re duration? ','Unknow');
INSERT INTO encountertemplate VALUES ('VIRAL WARTS (VERRUCA','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. Site(s) noted? \r\n3. Number noted? \r\n4. Electrodessication (cautery) of plantar warts on weight-bearing surfaces? \r\n5. Surgery on plantar warts? ','Unknow');
INSERT INTO encountertemplate VALUES ('HERPANGINA','0001-01-01 00:00:00','1. Inquiry re duration of sore throat? \r\n2. Examination of throat? \r\n3. Antibiotics used?','Unknow');
INSERT INTO encountertemplate VALUES ('GASTRITIS - HYPERACI','0001-01-01 00:00:00','1. Inquiry re location of abdominal pain? \r\n2. Inquiry re duration of abdominal pain? \r\n3. Inquiry re type of abdominal pain? \r\n4. Inquiry re aggravating causes (eg. food, smoking, alcohol, stress, drugs (ASA))? \r\n5. Inquiry re vomiting OR hematemesis? \r\n6. Abdominal exam? \r\n7. Advice re avoidance of aggravating factors (eg. smoking, spices, alcohol, etc.)? \r\n8. Discussion of stress factors? \r\n9. NSAIDs OR ASA OR cortisone used? ','Unknow');
INSERT INTO encountertemplate VALUES ('CONSTIPATION, RECURR','0001-01-01 00:00:00','*** NOTE ***\r\nPatient over 30 years old with a prior history of constipation. \r\n1. Inquiry re change in bowel movement? \r\n2. Inquiry re diet? \r\n3. Inquiry re drugs? \r\n4. Abdominal exam? \r\n5. Rectal exam? \r\n6. Stool for occult blood? \r\n7. IF less than 3 months duration, barium enema? \r\n8. Infrequent AND/OR difficult bowel movements? \r\n9. Instructions re increase in roughage OR fibre OR bran? \r\n10. One follow-up OR specific diagnostic statement within 3 months? ','Unknow');
INSERT INTO encountertemplate VALUES ('OBESITY','0001-01-01 00:00:00','1. Inquiry re duration of obesity? \r\n2. Weight recorded? \r\n3. Height recorded? \r\n4. Height AND weight recorded? \r\n5. Anorexiants OR thyroid drugs (if hypothyroidism not diagnosed) OR diuretics used? \r\n6. Diet counselling OR nutritional counselling (physician or dietician)? \r\n7. IF treatment given, follow-up within 6 weeks? ','Unknow');
INSERT INTO encountertemplate VALUES ('SPRAIN OR STRAIN, NY','0001-01-01 00:00:00','1. Inquiry re how injury happened? \r\n2. Inquiry re location of injury? \r\n3. Time of injury? \r\n4. Presence/absence of swelling? \r\n5. Presence/absence of tenderness? \r\n6. Presence/absence of hematoma? \r\n7. IF sports related, advice re prevention of further episodes? ','Unknow');
INSERT INTO encountertemplate VALUES ('CEREBRAL CONCUSSION','0001-01-01 00:00:00','1. Type of trauma described? \r\n2. Comment re severity of injury? \r\n3. Time since injury? \r\n4. Presence/absence of change in sensorium since injury? \r\n5. History of loss of consciousness? \r\n6. Neurological exam? \r\n7. Examination of site of injury? \r\n8. Skull X-ray? \r\n9. IF patient not admitted, head injury routine sheet OR instructions? \r\n10. Narcotics or sedatives used? \r\n11. Admission to hospital OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('CHICKEN POX','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. Description of rash? \r\n3. Blister OR papular OR vesicular rash? \r\n4. ASA used? ','Unknow');
INSERT INTO encountertemplate VALUES ('PLEURISY','0001-01-01 00:00:00','1. Inquiry re duration of symptoms? \r\n2. Inquiry re location of pain? \r\n3. Presence/absence of fever noted? \r\n4. Presence/absence of cough noted? \r\n5. Inquiry whether chest pain worse with deep breathing (pleuritic)? \r\n6. Chest exam? \r\n7. Throat exam? \r\n8. CVS exam? \r\n9. Blood pressure? \r\n10. Temperature recorded? \r\n11. IF temperature elevated OR sputum, CBC? \r\n12. IF sputum, C & S? \r\n13. IF rales AND/OR rhonchi present, chest X-ray? \r\n14. IF antibiotics prescribed, C & S of sputum done before use of antibiotic? \r\n15. IF smoker, advice re smoking? \r\n16. Cause stated OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('LOBAR PNEUMONIA','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\ncough \r\ndyspnea \r\nchest pain \r\nfever \r\n2. Description of breath sounds? \r\n3. Comment re dullness OR consolidation? \r\n4. Sputum C & S? \r\n5. WBC? \r\n6. Chest X-ray, 2 views? \r\n7. IF X-ray positive, follow-up X-ray within 30 days? \r\n8. Positive culture AND positive X-ray OR consolidation on examination? \r\n9. Oral penicillin or erythromycin or cephalosporin given? \r\n10. Dosage recorded? \r\n11. Amount recorded? \r\n12. Follow-up in 1 week? ','Unknow');
INSERT INTO encountertemplate VALUES ('PYODERMA (INC. IMPET','0001-01-01 00:00:00','1. Site noted? \r\n2. IF oral antibiotic used, was it one of penicillins, erythromycins, sulfonamides, tetracyclines, or cephalosporins? \r\n3. IF tetracycline used, was patient ','Unknow');
INSERT INTO encountertemplate VALUES ('LACERATIONS OF SKIN','0001-01-01 00:00:00','** NOTE **\r\nFOR QUESTIONS 1 THROUGH 7, PHYSICIAN MUST FULFILL CONDITION FOR EACH EPISODE OF LACERATION. \r\n1. Inquiry re how laceration occurred? \r\n2. Time between injury and visit? \r\n3. Description of wound? \r\n4. IF hand or wrist, comment on function? \r\n5. IF tendons severed, referral? \r\n6. Debridement (washing)? \r\n7. IF sutured, one follow-up? \r\n8. IF no tetanus toxoid within 10 years, injection given? ','Unknow');
INSERT INTO encountertemplate VALUES ('SEROUS OTITIS MEDIA','0001-01-01 00:00:00','1. Inquiry re at least two of following; hearing, pain, recurrent URI? \r\n2. Comment re fluid in middle ear OR retracted ear drum? \r\n3. Comment re nose AND throat? \r\n4. IF third episode or more, audiometry OR referral? \r\n5. IF physical findings OR hearing test are abnormal, follow-up until resolved OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('DIABETES MELLITUS, A','0001-01-01 00:00:00','1. Inquiry re family history of diabetes on chart (3 minute)? \r\n2. Duration of disease OR starting date on chart (3 minute)? \r\n3. Inquiry re one of following on each visit? \r\nurine sugars \r\nblood sugars \r\ndietary management \r\npatient feels well or ill \r\n4. Weight recorded (at least 75% of visits)? \r\n5. Urine glucose each visit? \r\n6. Comment re cardiovascular system AND blood pressure yearly? \r\n7. Examination of fundi yearly? \r\n8. IF on oral hypoglycemics, at least one blood sugar recorded yearly? \r\n9. IF on insulin, at least two blood sugars recorded yearly? \r\n10. BUN OR creatinine on chart (3 minute)? \r\n11. IF diabetes first diagnosed within past 2 years, evidence of 2 fasting blood sugars > 8.8 mmol/L OR random sugar > 13.8 mmol/L prior to treatment? \r\n12. Diabetic diet - caloric intake noted on chart (3 minute)? \r\n13. Evidence of dietary counselling by a health professional on chart (3 minute)? \r\n14. IF newly diagnosed, follow-up within one month? \r\n15. IF on diet alone, follow-up at least once yearly? \r\n16. IF on oral hypoglycemic OR insulin, follow-up at least twice yearly? \r\n17. Inquiry re sexual dysfunction on chart (3 minute)? \r\n18. IF acetohexamide or chlorpropramide used (Glyburide and Diabeta are OK), was BUN > 9 mmol/L OR was creatinine > 140 mmol/L? \r\n19. IF no ketones in serum or urine, was dietary therapy tried prior to starting oral hypoglycemic? ','Unknow');
INSERT INTO encountertemplate VALUES ('PROSTATE, CANCER OF','0001-01-01 00:00:00','1. Inquiry re urinary symptoms? \r\n2. Rectal exam at least yearly? \r\n3. Serum acid phosphatase yearly? \r\n4. IF new or changed urinary symptoms, C & S AND urinalysis?\r\n5. Pathology report positive? \r\n6. Follow-up every 6 months? ','Unknow');
INSERT INTO encountertemplate VALUES ('WELL BABY CARE','0001-01-01 00:00:00','1. Inquiry re food/diet? \r\n2. Inquiry re coping/parenting skills? \r\n3. Weight recorded at each visit? \r\n4. Comment re normal/abnormal developmental milestones? \r\n5. Length recorded 3 or more times per year? \r\n6. Head circumference recorded 3 or more times in first year of life? \r\n7. Three doses of DPTP by age 8 months OR justification of alternate course? \r\n8. IF age 1 to 2, MMR at 12-15 months, DPTP at 17-19 months OR justification of alternate course? \r\n9. IF > 1 year old, at least 3 visits in first year? \r\n10. IF > 2 years old, at least 3 visits in second year? \r\n11. IF parenting problems identified, counselling OR referral? \r\n12. MMR given before 12 months of age? ','Unknow');
INSERT INTO encountertemplate VALUES ('ARTHRITIS, NYD OR NO','0001-01-01 00:00:00','** NOTE **\r\nless than one month - multiple joints \r\n1. Inquiry re duration of symptoms? \r\n2. Location of joint pains noted? \r\n3. Description of nature OR severity of pain? \r\n4. Inquiry re aggravating OR precipitating factors? \r\n5. Description of inflammation OR swelling? \r\n6. Description of range of movement? \r\n7. On OR before second visit for same problem, CBC? \r\n8. On OR before second visit for same problem, ESR? \r\n9. On OR before second visit for same problem, anti-nuclear factor (i.e. ANF, ANA)? \r\n10. On OR before second visit for same problem, rheumatoid arthritis factor (i.e. RF, RA)? \r\n11. Were systemic steroids prescribed? \r\n12. Advice re rest OR restrict movement of joint? \r\n13. Follow-up within 2 weeks? \r\n14. IF within 1 year of visit for peptic ulcer disease, were anti-inflammatory agents prescribed? ','Unknow');
INSERT INTO encountertemplate VALUES ('HEMATOMA,SUBCUTANEOU','0001-01-01 00:00:00','1. Inquiry re history of trauma, type noted? \r\n2. Inquiry re spontaneous or traumatic? \r\n3. IF spontaneous, inquiry re previous episodes? \r\n4. IF spontaneous, inquiry re family history of bleeding? \r\n5. Description of size? \r\n6. Description of location? \r\n7. IF spontaneous, CBC, platelets, PT, PTT done? \r\n8. IF seen within 48 hours of onset, ice recommended? \r\n9. IF spontaneous OR recurrent, one follow-up? \r\n10. IF child (other recent or old trauma)? \r\n11. IF AND history of repeated trauma (3 or more within 2 years) skeletal survey? ','Unknow');
INSERT INTO encountertemplate VALUES ('CONTACT DERMATITIS','0001-01-01 00:00:00','** NOTE **\r\nIncludes poison ivy. \r\n1. Inquiry re duration? \r\n2. Inquiry re itching? \r\n3. Inquiry re exposure to irritants? \r\n4. Location of rash noted? \r\n5. IF oral prednisone used, no more than 7 days? \r\n6. IF oral prednisone used, one follow-up visit or phone call? ','Unknow');
INSERT INTO encountertemplate VALUES ('SECONDARY AMMENORRHE','0001-01-01 00:00:00','1. Menstrual history? \r\n2. Duration of problem? \r\n3. Description re onset of problem? \r\n4. History of medications (including oral contraceptives)? \r\n5. Inquiry re changes in diet? \r\n6. Inquiry re stress factors AND/OR athletics? \r\n7. Pelvic exam? \r\n8. Abdominal exam? \r\n9. Pregnancy test? \r\n10. CBC? \r\n11. Thyroid function (at least one of TSH,T3,T4)? \r\n12. Fasting blood sugar on chart (3 minute)? \r\n13. IF more than 3 months duration, prolactin? \r\n14. Pelvic ultrasound on chart (3 minute)? \r\n15. IF more than 3 months duration, X-ray of pituitary? \r\n16. Follow-up until cause found or referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('DIARRHEA, MULTIPLE V','0001-01-01 00:00:00','1. Inquiry re frequency? \r\n2. Inquiry re duration? \r\n3. Inquiry re diet? \r\n4. Inquiry re medications? \r\n5. Inquiry re travel? \r\n6. Inquiry re blood in stool? \r\n7. Inquiry re fever? \r\n8. Inquiry re weight loss? \r\n9. Inquiry re nausea OR abdominal cramps OR pain? \r\n10. Abdominal exam? \r\n11. Rectal exam? \r\n12. Weight noted at least once? \r\n13. Stool for C & S? \r\n14. Stool for ova and parasites? \r\n15. CBC? \r\n16. ESR? \r\n17. Sigmoidoscopy OR colonoscopy OR referral? \r\n18. Barium enema? \r\n19. IF barium enema negative, UGI series with small bowel follow through? \r\n20. IF not improved within 6 months OR specific diagnosis not noted on chart, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('NASAL POLYP','0001-01-01 00:00:00','1. Inquiry re nasal symptoms? \r\n2. Inquiry re history of asthma or ASA allergy (or on chart) (3 minute)? \r\n3. Description of polyp? \r\n4. IF no improvement after 6 weeks, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('SCOLIOSIS','0001-01-01 00:00:00','** NOTE **\r\nAudit only presenting visit. \r\n1. Inquiry re how condition found? \r\n2. Description of location (eg. thoracic, lumbar)? \r\n3. Description of extent (degree of angulation)? \r\n4. X-ray of affected area(s) of spine within 3 months of initial diagnosis? \r\n5. X-ray confirms diagnosis of scoliosis? \r\n6. IF 9 to 16 years old AND severe (angulation greater than or equal to 15 degrees, consultation OR referral? \r\n7. IF not referred, follow-up within 3 months? ','Unknow');
INSERT INTO encountertemplate VALUES ('MOUTH LESION','0001-01-01 00:00:00','1. Inquiry re location? \r\n2. Inquiry re duration? [HA2 ]> \r\n3. Description of lesion? \r\n4. IF lesion described as \"ulcer\" or \"plaque\" AND lesion not healed in 2 months, investigation OR referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('GLAUCOMA','0001-01-01 00:00:00','1. Inquiry re vision at each visit? \r\n** NOTE **\r\nIF patient is followed by an ophthalmologist, Questions 2 through 8 are NOT APPLICABLE. \r\n2. Inquiry re compliance with medications? \r\n3. Fundi, yearly statement re optic cup? \r\n4. Visual fields recorded yearly? \r\n5. Intraocular pressure yearly? \r\n6. Medications, dosage recorded? \r\n7. High intraocular pressure ( >30 mm Hg ) recorded on chart (3 minute)? \r\n8. Follow-up yearly? \r\n9. IF ocular pressures are not improved after one month of treatment, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('TONSILLITIS, CHRONIC','0001-01-01 00:00:00','1. Inquiry re recurrent sore throat? \r\n2. Description of tonsils? \r\n3. Presence/absence of cervical glands noted? \r\n4. IF antibiotic used, was it one of the penicillins, erythromycins, cephalosporins, or tetracyclines? \r\n5. IF tonsillectomy OR referral, were there 4 or more episodes within 2 years OR peritonsillar abscess (quinsy) OR unilateral emlargement OR demonstrated hearing loss? \r\n6. IF tetracycline used, was patient ','Unknow');
INSERT INTO encountertemplate VALUES ('ECZEMA, CONTACT DERM','0001-01-01 00:00:00','1. Inquiry re duration? \r\n2. Presence/absence of family history of eczema OR on chart (3 minute)? \r\n3. IF over 5 years, inquiry re stress factors? \r\n4. Description of lesion? \r\n5. Location and extent? \r\n6. Topical steroids used? \r\n7. Systemic steroids initiated by family doctor? \r\n8. Discussion re prognosis of disease? \r\n9. One follow-up? \r\n10. IF acute AND failure to respond within 6 weeks, referral?\r\n11. IF child, occulsive dressing for 8 hours or more per 24 hours? \r\n12. IF systemic corticosteroids used, was it for more than 3 months? \r\n13. Fluorinated steroids used on face? ','Unknow');
INSERT INTO encountertemplate VALUES ('FIBROCYSTIC DISEASE','0001-01-01 00:00:00','1. Inquiry re at least 2 of the following? \r\nbreast pain \r\nrelationship of lump to periods \r\nrecurrency of problem \r\nlocation of lump \r\n2. Description of both breasts? \r\n3. Statement of location and size of lumps? \r\n4. Axillary exam? \r\n5. IF lesion diagnosed as non-cystic, mammography? \r\n6. IF suspected cyst, aspiration OR referral? \r\n7. Yearly breast examination by physician following initial diagnosis? \r\n8. Cysts diagnosed by examination OR by mammography? \r\n9. BSE on chart every two years? \r\n10. IF discrete lump persists after aspiration, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('PHARYNGITIS','0001-01-01 00:00:00','1. Inquiry re sore throat? \r\n2. Inquiry re duration? \r\n3. Examination of pharynx? \r\n4. IF white membrane OR lot of exudate noted, mono test AND C & S? \r\n5. Red (inflamed, injected) throat? \r\n6. IF AND positive strep culture, amoxil or ampicillin or erythromycin given for at least 7 days? \r\n7. IF 5 years or older AND positive strep culture, penicillin or erythromycin for at least 7 days? ','Unknow');
INSERT INTO encountertemplate VALUES ('SINUSITIS','0001-01-01 00:00:00','1. Inquiry re pain in the face and/or head? \r\n2. Inquiry re nasal blockage? \r\n3. Presence/absence of fever noted? \r\n4. Presence/absence of tenderness over sinuses noted? \r\n5. IF recurrent (3 or more visits), X-ray of sinuses? \r\n6. Tenderness over sinuses OR positive X-ray of sinuses? \r\n7. IF antibiotic used, was it one of the penicillins, sulfonamides, erythromycins, cephalosporins, or tetracycline?\r\n8. IF tetracycline used, was patient ','Unknow');
INSERT INTO encountertemplate VALUES ('GOUT','0001-01-01 00:00:00','1. Inquiry re at least one of following? \r\nsevere joint pain \r\nhistory of swelling \r\nhistory of inflammation \r\nmonoarticular joint \r\n2. List of drugs being used OR on chart (3 minute)? \r\n3. Presence/absence of swelling of involved joint? \r\n4. Presence/absence of inflammation of involved joint? \r\n5. IF joint aspiration done, report for uric acid crystals? \r\n6. Serum uric acid? \r\n7. One of the following? \r\nserum uric acid greater than lab normal \r\nuric acid crystals in joint aspirate \r\nX-ray diagnosis \r\n8. ASA used? \r\n9. NSAIDs OR Colchicine used? \r\n10. IF thiazides used, statement of justification? \r\n11. IF 3 or more episodes OR uric acid greater than lab normal, recommendation for prophylaxis treatment? \r\n12. Advice re avoidance of precipitating factors (eg. alcohol, high purine foods) \r\n13. IF flare-up occurs AND allopurinal OR uricosurics are being used, Colchicine used? ','Unknow');
INSERT INTO encountertemplate VALUES ('NASAL INJURY','0001-01-01 00:00:00','1. Description of accident? \r\n2. Description of nose including 2 of following; swelling, amount of bleeding, deformity, lacerations? \r\n3. IF deformity found on examination, X-ray of nasal bones? \r\n4. IF deformity found on examination, referral to ENT specialist? \r\n5. IF unable to control bleeding, referral? ','Unknow');
INSERT INTO encountertemplate VALUES ('VENEREAL WARTS (COND','0001-01-01 00:00:00','1. Inquiry re duration of lesions? \r\n2. Description of size AND extent of lesions? \r\n3. VDRL OR syphilis screen? ','Unknow');

--
-- Dumping data for table 'encounterWindow'
--

--
-- Dumping data for table 'favorites'
--

--
-- Dumping data for table 'form'
--

--
-- Dumping data for table 'formAR'
--

--
-- Dumping data for table 'formAlpha'
--

--
-- Dumping data for table 'formAnnual'
--

--
-- Dumping data for table 'formLabReq'
--

--
-- Dumping data for table 'formMMSE'
--

--
-- Dumping data for table 'formMentalHealth'
--

--
-- Dumping data for table 'formPalliativeCare'
--

--
-- Dumping data for table 'formPeriMenopausal'
--

--
-- Dumping data for table 'formRourke'
--

--
-- Dumping data for table 'formType2Diabetes'
--

--
-- Dumping data for table 'groupMembers_tbl'
--


--
-- Dumping data for table 'groups_tbl'
--

INSERT INTO groups_tbl VALUES (17,0,'doc');

--
-- Dumping data for table 'ichppccode'
--

INSERT INTO ichppccode VALUES ('000','831','Dislocated Shoulder');
INSERT INTO ichppccode VALUES ('204','669','Complicated Delivery');
INSERT INTO ichppccode VALUES ('288','781','Pain Or Stiffness In Joint');
INSERT INTO ichppccode VALUES ('053','269','Feeding Problem Baby Or Elderly');
INSERT INTO ichppccode VALUES ('235','739','Cervical Spine Syndromes');
INSERT INTO ichppccode VALUES ('376','629','Non-Specific Abnormal Pap Smear');
INSERT INTO ichppccode VALUES ('246','739','Other Musculoskel, Connectiv Diseas (DDD)');
INSERT INTO ichppccode VALUES ('105','389','Deafness, Partial or Complete/ Hearing problem');
INSERT INTO ichppccode VALUES ('231','715','Arthritis NEC/Diff Conn Tiss Dis,polymyalgia rheumatica, PMR');
INSERT INTO ichppccode VALUES ('026','112','Moniliasis, Urogenital, Proven');
INSERT INTO ichppccode VALUES ('363.2','909','Elder abuse');
INSERT INTO ichppccode VALUES ('006','033','Whooping Cough');
INSERT INTO ichppccode VALUES ('109','410','Acute MI');
INSERT INTO ichppccode VALUES ('251','743','Blocked Tear Duct');
INSERT INTO ichppccode VALUES ('303','807','Fractured Ribs');
INSERT INTO ichppccode VALUES ('330','930','Foreign Body In Eye');
INSERT INTO ichppccode VALUES ('293','799','Weight Loss');
INSERT INTO ichppccode VALUES ('329','930','Foreign Body In Tissues');
INSERT INTO ichppccode VALUES ('277','289','Hepatomegaly/Splenomegaly');
INSERT INTO ichppccode VALUES ('099','379','Other Eye Diseases, Vision Problem');
INSERT INTO ichppccode VALUES ('147','519','Other Respiratory, Atelectasis');
INSERT INTO ichppccode VALUES ('083.3','304','Prescription drug dependence');
INSERT INTO ichppccode VALUES ('194','629','Other disorders of female genital organs');
INSERT INTO ichppccode VALUES ('110','413','Acute coronary insufficiency, Angina Pectoris(CAD, Ischemic Heart)/IHD');
INSERT INTO ichppccode VALUES ('202.2','644','Preterm Labour');
INSERT INTO ichppccode VALUES ('334','977','Overdose, Poisoning, Accidental Ingestion');
INSERT INTO ichppccode VALUES ('016','072','Mumps');
INSERT INTO ichppccode VALUES ('142','491','Chronic Bronchitis');
INSERT INTO ichppccode VALUES ('157','553','Other Hernias');
INSERT INTO ichppccode VALUES ('184','752','Cervical Hyperplasia');
INSERT INTO ichppccode VALUES ('115','427','Ectopic Beats, All Types');
INSERT INTO ichppccode VALUES ('149.1','529','Thrush');
INSERT INTO ichppccode VALUES ('043','218','Fibroids, Benign Neoplasm Uterus');
INSERT INTO ichppccode VALUES ('001','002','Typhoid & Paratyphoid Fevers');
INSERT INTO ichppccode VALUES ('340','896','Prophylactic Immunization');
INSERT INTO ichppccode VALUES ('335','989','Adverse Effects Of Other Chemicals');
INSERT INTO ichppccode VALUES ('146','680','Boil In Nose');
INSERT INTO ichppccode VALUES ('339','136','Contact/Carrier, Infec/Parasit Dis');
INSERT INTO ichppccode VALUES ('058','280','Iron Deficiency Anemia');
INSERT INTO ichppccode VALUES ('111','429','Disease Heart Valve Non-Rheum,NOS,NYD');
INSERT INTO ichppccode VALUES ('053.1','269','Feeding Problem Baby');
INSERT INTO ichppccode VALUES ('012','055','Measles');
INSERT INTO ichppccode VALUES ('312','718','Acute Damage Knee Meniscus');
INSERT INTO ichppccode VALUES ('073.2','300','Obsessive-compulsive disorder');
INSERT INTO ichppccode VALUES ('074.1','315','Learning disorder');
INSERT INTO ichppccode VALUES ('363.3','909','Child abuse');
INSERT INTO ichppccode VALUES ('348','799','Letter, Forms, Prescription WO Exam');
INSERT INTO ichppccode VALUES ('054','274','Gout');
INSERT INTO ichppccode VALUES ('249','608','Undescended Testicle');
INSERT INTO ichppccode VALUES ('070.2','300','Panic Disorder');
INSERT INTO ichppccode VALUES ('273','787','(DO NOT USE) Anorexia');
INSERT INTO ichppccode VALUES ('200','635','Therapeutic Abortion');
INSERT INTO ichppccode VALUES ('213','691','Eczema And Allergic Dermatitis');
INSERT INTO ichppccode VALUES ('354','799','Advice & Health Instruction');
INSERT INTO ichppccode VALUES ('190','626','Excessive Menstruation');
INSERT INTO ichppccode VALUES ('189','626','Amenorrhea, Absent, Scanty, Rare Menstruation');
INSERT INTO ichppccode VALUES ('338.1','917','CPX/Physical, Annual Health Adult/Teen, Well Visit');
INSERT INTO ichppccode VALUES ('204.4','653','Cephalo-pelvic disproportion');
INSERT INTO ichppccode VALUES ('204.11','645','Post Dates');
INSERT INTO ichppccode VALUES ('332.1','959','Motor Vehicle Accident, MVA');
INSERT INTO ichppccode VALUES ('002','009','Diarrhea/Presumed Infect.Intest Dis');
INSERT INTO ichppccode VALUES ('015','070','Infectious Hepatitis');
INSERT INTO ichppccode VALUES ('156','552','Hiatus/Diaphragmatic Hernia');
INSERT INTO ichppccode VALUES ('218','698','Pruritis And Related Conditions');
INSERT INTO ichppccode VALUES ('120','401','Hypertension - Uncomplicated (HTN)');
INSERT INTO ichppccode VALUES ('119','796','Elevated Blood Pressure (BP)');
INSERT INTO ichppccode VALUES ('067','295','Schizophrenia');
INSERT INTO ichppccode VALUES ('057','259','AIDS,HIV,Other Endocr,Nutritn,Metabol Disord,Jaundice,Dehydration,immunity disorders');
INSERT INTO ichppccode VALUES ('094','367','Myopia,Astigmatism,Other Refrac Dis');
INSERT INTO ichppccode VALUES ('038','201','Hodgkins Disease,Lymphoma,Leukemia');
INSERT INTO ichppccode VALUES ('104','386','Labyrinthitis, Meniere&#146;s Disease');
INSERT INTO ichppccode VALUES ('245','735','Hammer Toe');
INSERT INTO ichppccode VALUES ('297','797','Senility Without Psychosis');
INSERT INTO ichppccode VALUES ('307','815','Fractured Metacarpals');
INSERT INTO ichppccode VALUES ('350','650','Diagnosing Pregnancy');
INSERT INTO ichppccode VALUES ('349','799','Referral WO Exam Or Interview');
INSERT INTO ichppccode VALUES ('021','136','Malaria');
INSERT INTO ichppccode VALUES ('162','565','Anal Fissure/Fistula/Abscess');
INSERT INTO ichppccode VALUES ('082.1','304','Smokestop');
INSERT INTO ichppccode VALUES ('353.1','609','Circumcision');
INSERT INTO ichppccode VALUES ('338','650','Well baby, Newborn care, Postnatal care, Postpartum care');
INSERT INTO ichppccode VALUES ('198','646','Urinary Infection, Pregnancy& Postpartum');
INSERT INTO ichppccode VALUES ('074.2','315','Attention deficit disorder, ADD, ADHD');
INSERT INTO ichppccode VALUES ('017','075','Infectious Mononucleosis');
INSERT INTO ichppccode VALUES ('027','131','Trichomonas, Urogenital, Proven');
INSERT INTO ichppccode VALUES ('262','785','Chest Pain');
INSERT INTO ichppccode VALUES ('116','785','Heart Murmur NEC, NYD');
INSERT INTO ichppccode VALUES ('219','700','Corns, Calluses');
INSERT INTO ichppccode VALUES ('220','706','Sebaceous Cyst');
INSERT INTO ichppccode VALUES ('121','402','Hypertension - Target Organ Invl (HTN)');
INSERT INTO ichppccode VALUES ('167','579','Other Digestive Sys. Dis. NEC, Dysphagia');
INSERT INTO ichppccode VALUES ('068','296','(DO NOT USE) Manic Depressive Psychosis');
INSERT INTO ichppccode VALUES ('163','564','Proctitis, Rectal & Anal Pain NOS');
INSERT INTO ichppccode VALUES ('252','759','Congenital Anomalies, hip diplasia');
INSERT INTO ichppccode VALUES ('064','288','Abnormal White Cell Count');
INSERT INTO ichppccode VALUES ('153','536','Other Stomach & Duoden Dis/Disord');
INSERT INTO ichppccode VALUES ('317','845','Sprain/Strain Ankle, Foot, Toes');
INSERT INTO ichppccode VALUES ('215','691','Diaper Rash');
INSERT INTO ichppccode VALUES ('022','097','Syphilis, All Sites And Stages');
INSERT INTO ichppccode VALUES ('125','440','Arteriosclerosis, Atherosclerosis');
INSERT INTO ichppccode VALUES ('318','845','Sprain/Strain Foot, Toes');
INSERT INTO ichppccode VALUES ('266','785','Enlarged Lymph Nodes, Not Infected');
INSERT INTO ichppccode VALUES ('149.2','529','Sore Throat');
INSERT INTO ichppccode VALUES ('369','909','(DO NOT USE) Other Problems Of Social Adjustment');
INSERT INTO ichppccode VALUES ('370','906','Legal Problem');
INSERT INTO ichppccode VALUES ('197.1','640','Bleeding, threatened abortion, hemorrhage in early pregnancy');
INSERT INTO ichppccode VALUES ('294.1','315','Developmental delay');
INSERT INTO ichppccode VALUES ('053.2','269','Elderly Feeding Problem');
INSERT INTO ichppccode VALUES ('202.3','644','Post-term Labour');
INSERT INTO ichppccode VALUES ('209','683','Lymphadenitis, Acute');
INSERT INTO ichppccode VALUES ('210','684','Impetigo');
INSERT INTO ichppccode VALUES ('298','791','Abnormal Urine Test NEC');
INSERT INTO ichppccode VALUES ('308','816','Fractured Phalanges - Foot/Hand');
INSERT INTO ichppccode VALUES ('313','839','Other Dislocations');
INSERT INTO ichppccode VALUES ('131','447','Postural Hypotension');
INSERT INTO ichppccode VALUES ('351','799','Prenatal Care');
INSERT INTO ichppccode VALUES ('078','313','(DO NOT USE) Behaviour Disorders, Child/Adolesce, ADD, ADHD');
INSERT INTO ichppccode VALUES ('172','597','Urethritis');
INSERT INTO ichppccode VALUES ('068.1','296','Bi-polar/bipolar affective disorder');
INSERT INTO ichppccode VALUES ('323','879','Laceration/Open Wound/Traum Amputat/Needlestick injury');
INSERT INTO ichppccode VALUES ('355','799','Problems External To Patient');
INSERT INTO ichppccode VALUES ('135','463','Tonsilitis And Quinsy');
INSERT INTO ichppccode VALUES ('032','151','Malig Neopl G.I. Tract, Colon Cancer');
INSERT INTO ichppccode VALUES ('086.1','301','Self esteem problem');
INSERT INTO ichppccode VALUES ('074','315','(DO NOT USE) Specified Delays In Development');
INSERT INTO ichppccode VALUES ('036','180','Malig Neoplasm Female Genital Tract');
INSERT INTO ichppccode VALUES ('256','780','Vertigo & Giddiness, Dizzy');
INSERT INTO ichppccode VALUES ('359','898','(DO NOT USE) Marital/Relationship Problem');
INSERT INTO ichppccode VALUES ('360','899','(DO NOT USE) Parent/Child Problem, Child Abuse');
INSERT INTO ichppccode VALUES ('224','706','Acne, Sebaceous Cyst');
INSERT INTO ichppccode VALUES ('077.3','309','Adolescent adjustment');
INSERT INTO ichppccode VALUES ('194.1','640','Bleeding, threatened abor., hemorrhage in early pregnacy');
INSERT INTO ichppccode VALUES ('078.2','313','Discipline, Temper Tantrums, Conduct Disorder');
INSERT INTO ichppccode VALUES ('199','642','Pre-eclampsia, eclampsia, toxaemia, Gestational Hypertension, Toxemias of Pregnancy & Puerperium');
INSERT INTO ichppccode VALUES ('204.5','660','Obstructed labour');
INSERT INTO ichppccode VALUES ('080.1','303','Alcohol Abuse');
INSERT INTO ichppccode VALUES ('204.1','651','Multiple Pregnancy');
INSERT INTO ichppccode VALUES ('354.1','895','Sexual Health');
INSERT INTO ichppccode VALUES ('204.9','656','Decreased fetal movement');
INSERT INTO ichppccode VALUES ('168','580','Glumerulonephritis, Acute & Chronic');
INSERT INTO ichppccode VALUES ('042','217','Benign Neoplasm Breast');
INSERT INTO ichppccode VALUES ('286','781','Leg Pain');
INSERT INTO ichppccode VALUES ('221','703','Ingrown Toenail/Nail Diseases/Paronychia');
INSERT INTO ichppccode VALUES ('070.1','300','Post-traumatic stress disorder');
INSERT INTO ichppccode VALUES ('132','459','Other Periph. Vessel Dis, Aneurysm, CVD');
INSERT INTO ichppccode VALUES ('084','301','Personality Disorders');
INSERT INTO ichppccode VALUES ('069','298','(DO NOT USE) Psychosis, Other/NOS Excl Alcoholic');
INSERT INTO ichppccode VALUES ('070','300','Anxiety');
INSERT INTO ichppccode VALUES ('079','302','Sexual Dysfunction');
INSERT INTO ichppccode VALUES ('080','303','Alcoholism & Alcohol Problem');
INSERT INTO ichppccode VALUES ('234','781','Muscle Pain/Myalgia/Fibromyalgia');
INSERT INTO ichppccode VALUES ('375','790','Hematological Abnormality NEC');
INSERT INTO ichppccode VALUES ('361','900','(DO NOT USE) Aged Parent Or In-Law Problem');
INSERT INTO ichppccode VALUES ('136','474','Chronic Infection Tonsils/Adenoids');
INSERT INTO ichppccode VALUES ('187','627','Menopausal Symptoms/Menopause,post menopausal bleeding');
INSERT INTO ichppccode VALUES ('033','162','Malignant Neopl Respiratory Tract, lung cancer');
INSERT INTO ichppccode VALUES ('319','847','Sprain/Strain Neck, Low Back,Coccyx');
INSERT INTO ichppccode VALUES ('037','188','Malig Neop Urinary & Male Genital');
INSERT INTO ichppccode VALUES ('178','604','Orchitis & Epididymitis');
INSERT INTO ichppccode VALUES ('320','847','Sprain/Strain Vertebral Excl Neck');
INSERT INTO ichppccode VALUES ('037.1','185','Prostate cancer');
INSERT INTO ichppccode VALUES ('075','307','Sleep Disorders, Insomnia');
INSERT INTO ichppccode VALUES ('333','959','Other Injuries & Trauma, Fall, Soft Tissue Injury');
INSERT INTO ichppccode VALUES ('314','840','Sprain/Strain Shoulder And Arm');
INSERT INTO ichppccode VALUES ('356','897','Financial Stress');
INSERT INTO ichppccode VALUES ('183','615','PID, Pelvic Inflammatory Disease, Acute Or Chronic Endometritis (PID)');
INSERT INTO ichppccode VALUES ('204.6','662','Prolonged labour');
INSERT INTO ichppccode VALUES ('078.3','313','Behaviour Problem, Conduct Disorder');
INSERT INTO ichppccode VALUES ('052','269','Avitamin & Nutritional Disorder NEC');
INSERT INTO ichppccode VALUES ('365','903','Illegitimacy');
INSERT INTO ichppccode VALUES ('141','511','Pleurisy All Types Excl Tubercul');
INSERT INTO ichppccode VALUES ('173','593','Orthostatic Albuminuria');
INSERT INTO ichppccode VALUES ('366','904','(DO NOT USE) Social Maladjustment');
INSERT INTO ichppccode VALUES ('276','787','Hematemesis/Melena');
INSERT INTO ichppccode VALUES ('225','707','Chronic Skin Ulcer');
INSERT INTO ichppccode VALUES ('240','737','Scoliosis, Kyphosis, Lordosis');
INSERT INTO ichppccode VALUES ('369.1','909','Other social problem');
INSERT INTO ichppccode VALUES ('239','724','Lumbar Strain, Sciatica, back pain with radiation');
INSERT INTO ichppccode VALUES ('088','332','Parkinsonism');
INSERT INTO ichppccode VALUES ('177','603','Hydrocele');
INSERT INTO ichppccode VALUES ('229','715','Osteoarthritis & Allied Conditions');
INSERT INTO ichppccode VALUES ('230','716','Traumatic Arthritis');
INSERT INTO ichppccode VALUES ('267','786','Epistaxis');
INSERT INTO ichppccode VALUES ('371','909','Problems NEC In Codes 008- To V629');
INSERT INTO ichppccode VALUES ('126','447','Other Disorders Of Arteries/claudication');
INSERT INTO ichppccode VALUES ('005','511','Pleural Effusion NOS');
INSERT INTO ichppccode VALUES ('328','949','Burns & Scalds - All Degrees');
INSERT INTO ichppccode VALUES ('999','','Other');
INSERT INTO ichppccode VALUES ('197.2','640','Antepartum bleeding');
INSERT INTO ichppccode VALUES ('041','216','Mole, Pigmented Nevus');
INSERT INTO ichppccode VALUES ('083','304','Drug Addiction, Dependence');
INSERT INTO ichppccode VALUES ('130','455','Hemorrhoids');
INSERT INTO ichppccode VALUES ('134','461','Sinusitis, Acute & Chronic');
INSERT INTO ichppccode VALUES ('217','696','Psoriasis');
INSERT INTO ichppccode VALUES ('374','625','Non-Psych Vaginismus & Dyspareunia');
INSERT INTO ichppccode VALUES ('275','787','Heartburn/dyspepsia');
INSERT INTO ichppccode VALUES ('082','304','Tobacco Abuse/Smoking Cessation');
INSERT INTO ichppccode VALUES ('182','611','Other Breast Diseases(gynecomastia)');
INSERT INTO ichppccode VALUES ('363','909','(DO NOT USE) Other Family Problems');
INSERT INTO ichppccode VALUES ('066','290','Dementia/organic psychosis');
INSERT INTO ichppccode VALUES ('364','902','Education Problem');
INSERT INTO ichppccode VALUES ('259','300','Disturbance Of Sensation/Numbness');
INSERT INTO ichppccode VALUES ('233','727','Other Bursitis & Synovitis, Tendonitis');
INSERT INTO ichppccode VALUES ('129','454','Varicose Veins - Legs, venous stasis');
INSERT INTO ichppccode VALUES ('176','601','Prostatitis & Seminal Vesiculitis');
INSERT INTO ichppccode VALUES ('279','787','Abdominal Pain');
INSERT INTO ichppccode VALUES ('316','844','Sprain/Strain Knee, Leg');
INSERT INTO ichppccode VALUES ('280','786','Dysuria');
INSERT INTO ichppccode VALUES ('327','919','Bruise, Contusion, Crushing');
INSERT INTO ichppccode VALUES ('118','429','Other Heart Diseases NEC,cardiomyopathy');
INSERT INTO ichppccode VALUES ('004','010','TB skin test conv.Tuberculosis infection, primary');
INSERT INTO ichppccode VALUES ('181','610','Chronic cystic breast disease, fibrocystic breast disease, cyst breast benign');
INSERT INTO ichppccode VALUES ('186','618','Cystocele,Rectocele,Uterine Prolaps');
INSERT INTO ichppccode VALUES ('223','799','Pompholyx & Sweat Gland Disease NEC');
INSERT INTO ichppccode VALUES ('029','132','Lice, Head Or Body, Pediculosis');
INSERT INTO ichppccode VALUES ('030','133','Scabies & Other Acariasis');
INSERT INTO ichppccode VALUES ('144','493','Asthma');
INSERT INTO ichppccode VALUES ('311','829','Other Fractures');
INSERT INTO ichppccode VALUES ('326','919','Abrasion, Scratch, Blister');
INSERT INTO ichppccode VALUES ('353','799','Med/Surg Procedure WO Diagnosis');
INSERT INTO ichppccode VALUES ('128','451','Phlebitis, Thrombophlebitis (DVT)');
INSERT INTO ichppccode VALUES ('171','592','Urinary Calculus/ kidney stone');
INSERT INTO ichppccode VALUES ('322','850','Head injury, concussion, intracranial injury');
INSERT INTO ichppccode VALUES ('212','690','Seborrhoeic Dermatitis');
INSERT INTO ichppccode VALUES ('368','909','Phase-Of-Life Problem NEC');
INSERT INTO ichppccode VALUES ('238','781','Back Pain (backache)W/O Radiation');
INSERT INTO ichppccode VALUES ('072','300','Depression');
INSERT INTO ichppccode VALUES ('204.3','652','Unusual position of fetus, malpresentation');
INSERT INTO ichppccode VALUES ('093','373','Stye, Chalazion');
INSERT INTO ichppccode VALUES ('197','641','Abruptio Placenta, Placenta Praevia');
INSERT INTO ichppccode VALUES ('244','718','Chronic Internal Knee Derangement');
INSERT INTO ichppccode VALUES ('248','754','Congenital Anomalies Of Lower Limb');
INSERT INTO ichppccode VALUES ('103','381','Eustachian Block Or Catarrh');
INSERT INTO ichppccode VALUES ('025','112','Moniliasis Excl Urogenital');
INSERT INTO ichppccode VALUES ('347','895','General Contraceptive Guidance');
INSERT INTO ichppccode VALUES ('056','272','Lipid Metabolism Disorders/Hypercholesterolemia/Hyperlipidemia');
INSERT INTO ichppccode VALUES ('084.1','301','(DO NOT USE) Substance/alcohol abuse, not tobacco');
INSERT INTO ichppccode VALUES ('207','680','Boil/Cellulitis Incl Finger/Toe/Paronychia');
INSERT INTO ichppccode VALUES ('145','477','Hay Fever, allergic rhinitis, allergies');
INSERT INTO ichppccode VALUES ('046','239','Neoplasm Nyd As Benign Or Malignant');
INSERT INTO ichppccode VALUES ('124','436','CVA, Stroke');
INSERT INTO ichppccode VALUES ('161','564','Constipation');
INSERT INTO ichppccode VALUES ('291','780','Fever - Undetermined Cause');
INSERT INTO ichppccode VALUES ('301','802','Skull/Facial Fractures');
INSERT INTO ichppccode VALUES ('343','895','Sterilization, Male/Female');
INSERT INTO ichppccode VALUES ('265','785','Edema');
INSERT INTO ichppccode VALUES ('149','529','Glossitis/Mouth Disease');
INSERT INTO ichppccode VALUES ('150','530','Esophageal Disorder(GERD/esophagitis),Reflux');
INSERT INTO ichppccode VALUES ('363.5','901','Sibling Rivalry');
INSERT INTO ichppccode VALUES ('083.2','304','Legal Drug Addiction, Dependence');
INSERT INTO ichppccode VALUES ('337','994','Adverse Effects Of Physical Factors');
INSERT INTO ichppccode VALUES ('014','057','Viral Xanthems');
INSERT INTO ichppccode VALUES ('155','550','Inguinal Hernia W/WO Obstruction');
INSERT INTO ichppccode VALUES ('073.1','300','Phobia');
INSERT INTO ichppccode VALUES ('078.1','313','Behavioural problem/conduct disorder');
INSERT INTO ichppccode VALUES ('363.1','909','Family Violence');
INSERT INTO ichppccode VALUES ('607','180','(DO NOT USE) Other Male Genital Organ Diseases');
INSERT INTO ichppccode VALUES ('031','136','Sepsis/Other Infect/Parasutic Diseases NEC/STD/fungus/coxsackie');
INSERT INTO ichppccode VALUES ('071','300','Hysterical & Hypochondriac Disorder');
INSERT INTO ichppccode VALUES ('013','056','Rubella');
INSERT INTO ichppccode VALUES ('039','199','Other Malignant Neoplasms NEC');
INSERT INTO ichppccode VALUES ('040','214','Lipoma, Any Site');
INSERT INTO ichppccode VALUES ('055','278','Obesity');
INSERT INTO ichppccode VALUES ('097','365','Glaucoma');
INSERT INTO ichppccode VALUES ('158','562','Diverticular Disease Of Intestine');
INSERT INTO ichppccode VALUES ('195','628','Female Infertility');
INSERT INTO ichppccode VALUES ('205','675','Mastitis & Lactation Disorders');
INSERT INTO ichppccode VALUES ('378','998','Other Adverse Effects NEC');
INSERT INTO ichppccode VALUES ('362.1','901','Couple problem');
INSERT INTO ichppccode VALUES ('318.1','845','Heel pain, plantar fasciitis');
INSERT INTO ichppccode VALUES ('222','704','Alopecia,folliculitis');
INSERT INTO ichppccode VALUES ('107','388','Tinnitus/Ear Pain/Otalgia');
INSERT INTO ichppccode VALUES ('341','799','Observ/Care Pt On Medicat (HRT, medication rev)');
INSERT INTO ichppccode VALUES ('008','045','Polio & CNS Enteroviral Diseases');
INSERT INTO ichppccode VALUES ('138','466','Bronchitis & Bronchiolitis, Acute');
INSERT INTO ichppccode VALUES ('304','810','Fractured Clavicle');
INSERT INTO ichppccode VALUES ('294','783','Lack Of Expected Physiolog Develop');
INSERT INTO ichppccode VALUES ('346','895','Other Contraceptive Methods(IUD)');
INSERT INTO ichppccode VALUES ('024','117','Dermatophytosis & Dermatomycosis, fungal infection/Tinea');
INSERT INTO ichppccode VALUES ('123','435','Transient Cerebral Ischemia/TIA');
INSERT INTO ichppccode VALUES ('050.1','251','Glucose Intolerance');
INSERT INTO ichppccode VALUES ('363.4','899','Family of Origin Issues');
INSERT INTO ichppccode VALUES ('360.3','899','Adult Child of Alcoholic');
INSERT INTO ichppccode VALUES ('083.1','304','Illegal Drug Addiction, Dependence');
INSERT INTO ichppccode VALUES ('258','780','Headache Except Tension And Migrain');
INSERT INTO ichppccode VALUES ('247','746','Congenital Anomaly Heart & Circulat');
INSERT INTO ichppccode VALUES ('336','998','Surgery & Medical Care Complication');
INSERT INTO ichppccode VALUES ('102','381','Acute & Chronic Serous Otitis Media');
INSERT INTO ichppccode VALUES ('096','366','Cataract');
INSERT INTO ichppccode VALUES ('106','388','Wax In Ear');
INSERT INTO ichppccode VALUES ('092','372','Conjunctivitis & Ophthalmia');
INSERT INTO ichppccode VALUES ('289','781','Swelling Or Effusion Of Joint');
INSERT INTO ichppccode VALUES ('300','788','Sign, Symptom, Ill Defined Cond NEC');
INSERT INTO ichppccode VALUES ('290','780','Excessive Sweating, Night Sweats');
INSERT INTO ichppccode VALUES ('003','349','Other Diseases Of CNS (CP), Neuralgia');
INSERT INTO ichppccode VALUES ('367.1','905','Unemployment/Work stress');
INSERT INTO ichppccode VALUES ('034','173','Malig Neo Skin/Subcutaneous Tissue');
INSERT INTO ichppccode VALUES ('175','600','Benign Prostatic Hypertrophy/BPH');
INSERT INTO ichppccode VALUES ('072.1','300','Dysthymia');
INSERT INTO ichppccode VALUES ('076','307','Tension Headaches');
INSERT INTO ichppccode VALUES ('112','428','Congestive Heart Failure (CHF)');
INSERT INTO ichppccode VALUES ('191','625','Dysmenorrhea');
INSERT INTO ichppccode VALUES ('243','733','Osteoporosis');
INSERT INTO ichppccode VALUES ('060','282','Hereditary Hemolytic Anemias');
INSERT INTO ichppccode VALUES ('148','521','Dental Disorders');
INSERT INTO ichppccode VALUES ('342','799','Observ/Care Other Hi Risk Patient');
INSERT INTO ichppccode VALUES ('059','281','Pernicious & Other Deficienc Anemia (B12 deficiency)');
INSERT INTO ichppccode VALUES ('053.3','269','Breast Feeding Difficulties');
INSERT INTO ichppccode VALUES ('201','634','Complete/Incomplete Abortion, Miscarriage');
INSERT INTO ichppccode VALUES ('154','540','Appendicitis, All Types');
INSERT INTO ichppccode VALUES ('196','633','Ectopic Pregnancy');
INSERT INTO ichppccode VALUES ('206','669','Other Complication Of Puerperium');
INSERT INTO ichppccode VALUES ('299','796','Other Unexplained Abnormal Results');
INSERT INTO ichppccode VALUES ('309','821','Fractured Femur');
INSERT INTO ichppccode VALUES ('310','823','Fractured Tibia/Fibula');
INSERT INTO ichppccode VALUES ('377','977','Allergy To Medications');
INSERT INTO ichppccode VALUES ('263','786','Palpitations');
INSERT INTO ichppccode VALUES ('077.1','309','Grief reaction/bereavement');
INSERT INTO ichppccode VALUES ('362','901','Separation/divorce');
INSERT INTO ichppccode VALUES ('325','989','Insect Bites / Bee Stings');
INSERT INTO ichppccode VALUES ('257','799','Disturbance Of Speech, hoarseness');
INSERT INTO ichppccode VALUES ('278','787','Flatulence, Bloating, Eructation');
INSERT INTO ichppccode VALUES ('242','732','Osteochondritis');
INSERT INTO ichppccode VALUES ('295','780','Fatigue, Malaise, Tiredness');
INSERT INTO ichppccode VALUES ('305','812','Fractured Humerus');
INSERT INTO ichppccode VALUES ('086.2','307','Eating disorder');
INSERT INTO ichppccode VALUES ('281.1','788','Toilet Training Problems');
INSERT INTO ichppccode VALUES ('169','590','Pyelonephritis & Pyelitis,Acute/Chr');
INSERT INTO ichppccode VALUES ('044','228','Hemangioma & Lymphangioma');
INSERT INTO ichppccode VALUES ('143','492','Emphysema & COPD');
INSERT INTO ichppccode VALUES ('321','848','Other Sprains And Strains');
INSERT INTO ichppccode VALUES ('232','739','Shoulder Syndromes');
INSERT INTO ichppccode VALUES ('081','291','Acute Alcoholic Intoxication');
INSERT INTO ichppccode VALUES ('170','595','Cystitis & UTI (Urinary Tract Infection)');
INSERT INTO ichppccode VALUES ('028','127','Oxyuriasis, Pinworms, Helminthiasis');
INSERT INTO ichppccode VALUES ('065','289','Blood/Blood Forming Organ Disor NEC');
INSERT INTO ichppccode VALUES ('117','429','Pulmonary Heart Disease');
INSERT INTO ichppccode VALUES ('159','787','Irrit Bowel Syndr IBS /Intest Disor NEC');
INSERT INTO ichppccode VALUES ('160','556','Ulcerative Colitis, Crohn&#146;s, Inflammatory Bowel');
INSERT INTO ichppccode VALUES ('185','616','Vaginitis NOS, Vulvitis, Yeast Vaginitis');
INSERT INTO ichppccode VALUES ('352','799','Postnatal Care/Postpartum Care');
INSERT INTO ichppccode VALUES ('366.1','904','Cultural adjustment');
INSERT INTO ichppccode VALUES ('315','842','Sprain/Strain Wrist, Hand, Fingers');
INSERT INTO ichppccode VALUES ('237','715','Osteoarthritis Of Spine');
INSERT INTO ichppccode VALUES ('061','285','Anemia, Other/Unspecified');
INSERT INTO ichppccode VALUES ('018','372','Viral Conjunctivitis');
INSERT INTO ichppccode VALUES ('211','686','Pyoderma,Pyogenic Granuloma');
INSERT INTO ichppccode VALUES ('253','763','All Perinatal Conditions');
INSERT INTO ichppccode VALUES ('086','298','Other Psychiatric Disorder');
INSERT INTO ichppccode VALUES ('049','244','Hypothyroidism, Myxedema, Cretinism');
INSERT INTO ichppccode VALUES ('023','098','Gonococcal Infections');
INSERT INTO ichppccode VALUES ('127','415','Pulmonary Embolism & Infarction');
INSERT INTO ichppccode VALUES ('164','569','Rectal Bleeding');
INSERT INTO ichppccode VALUES ('373','599','Hematuria NOS');
INSERT INTO ichppccode VALUES ('179','605','Phimosis & Paraphimosis');
INSERT INTO ichppccode VALUES ('268','786','Hemoptysis');
INSERT INTO ichppccode VALUES ('180','608','Other Male Genital Organ Diseases');
INSERT INTO ichppccode VALUES ('216','696','Pityriasis Rosea');
INSERT INTO ichppccode VALUES ('331','930','Foreign Body Entering Thru Orifice');
INSERT INTO ichppccode VALUES ('086.3','301','Sexual identity problem');
INSERT INTO ichppccode VALUES ('357','909','Housing/Placement Problem');
INSERT INTO ichppccode VALUES ('050','250','Diabetes Mellitus, NIDDM, IDDM');
INSERT INTO ichppccode VALUES ('007','034','Strep Thr, Scarlet Fev, Erysipelas');
INSERT INTO ichppccode VALUES ('367','905','(DO NOT USE) Occupational Problems');
INSERT INTO ichppccode VALUES ('226','708','Allergic Urticaria, hives');
INSERT INTO ichppccode VALUES ('338.2','917','Well child 2-15 years');
INSERT INTO ichppccode VALUES ('204.2','656','Small/Large for Dates');
INSERT INTO ichppccode VALUES ('204.7','664','Perineal lacerations');
INSERT INTO ichppccode VALUES ('274','643','Nausea and/or vomiting, hyperemesis gravidarum');
INSERT INTO ichppccode VALUES ('133','460','Common Cold, Acute URI, Pharyngitis, URTI');
INSERT INTO ichppccode VALUES ('202.4','645','Prolonged pregnancy');
INSERT INTO ichppccode VALUES ('011','054','Herpes Simplex, All Sites');
INSERT INTO ichppccode VALUES ('372','099','Non-Specific Urethritis');
INSERT INTO ichppccode VALUES ('345','895','Intrauterine Devices');
INSERT INTO ichppccode VALUES ('035','174','Malignant Neoplasm Breast');
INSERT INTO ichppccode VALUES ('140','486','Pneumonia');
INSERT INTO ichppccode VALUES ('139','487','Influenza');
INSERT INTO ichppccode VALUES ('051','790','Abnormal Unexplained Biochem Test');
INSERT INTO ichppccode VALUES ('332','959','Late Effect Of Trauma');
INSERT INTO ichppccode VALUES ('358','909','Caregiver Stress');
INSERT INTO ichppccode VALUES ('152','531','Other Peptic Ulcer, H Pylori, PUD');
INSERT INTO ichppccode VALUES ('165','571','Cirrhosis & Other Liver Diseases');
INSERT INTO ichppccode VALUES ('228','714','RH Arthritis, Still&#146;s Disease, Polymyalgia Rheumatica');
INSERT INTO ichppccode VALUES ('264','780','Syncope, Faint, Blackout');
INSERT INTO ichppccode VALUES ('201.1','656','Decreased Fetal Movement, Fetal Distress');
INSERT INTO ichppccode VALUES ('086.4','299','Autism');
INSERT INTO ichppccode VALUES ('204.8','666','Postpartum Hemorrhage, PPH');
INSERT INTO ichppccode VALUES ('214','692','Contact Dermatitis');
INSERT INTO ichppccode VALUES ('063','289','Lymphadenitis, Chronic/Nonspecific');
INSERT INTO ichppccode VALUES ('254','780','Convulsions');
INSERT INTO ichppccode VALUES ('270','786','Cough');
INSERT INTO ichppccode VALUES ('306','813','Fractured Radius/Ulna');
INSERT INTO ichppccode VALUES ('296','229','Mass & Localized Swelling NOS/NYD');
INSERT INTO ichppccode VALUES ('227','709','Other Skin/Subcutaneous Tiss Diseas (Actinic Keratosis)');
INSERT INTO ichppccode VALUES ('269','786','Dyspnea/SOB');
INSERT INTO ichppccode VALUES ('089','345','Epilepsy/Seizure, All Types');
INSERT INTO ichppccode VALUES ('090','346','Migraine Headaches');
INSERT INTO ichppccode VALUES ('283','788','Frequency Of Urination');
INSERT INTO ichppccode VALUES ('241','727','Ganglion Of Joint & Tendon');
INSERT INTO ichppccode VALUES ('100','380','Otitis Externa/OE');
INSERT INTO ichppccode VALUES ('047','240','Nontoxic Goiter & Nodule');
INSERT INTO ichppccode VALUES ('188','625','Premenstrual Tension Syndrome (PMS)');
INSERT INTO ichppccode VALUES ('045','229','Other Benign Neoplasms NEC');
INSERT INTO ichppccode VALUES ('087','340','Multiple Sclerosis/MS');
INSERT INTO ichppccode VALUES ('020.1','799','Sexually transmitted disease, STD');
INSERT INTO ichppccode VALUES ('110.1','412','Post MI, Old Myocardial infarction, chronic coronary artery disease');
INSERT INTO ichppccode VALUES ('204.10','658','Premature rupture of membrane');
INSERT INTO ichppccode VALUES ('101','382','Acute Otitis Media/OM');
INSERT INTO ichppccode VALUES ('048','242','Thyrotoxicosis W/WO Goiter,Hyperthyroidism');
INSERT INTO ichppccode VALUES ('091','343','Other Neurological Disorders/Carpal Tunnel Syndrome/Trigeminal Neuralgia');
INSERT INTO ichppccode VALUES ('062','286','Purpura,Hemorrhag & Coagulat Defect');
INSERT INTO ichppccode VALUES ('202','646','Other Complications Of Pregnancy');
INSERT INTO ichppccode VALUES ('281','788','Enuresis, Incontinence');
INSERT INTO ichppccode VALUES ('166','575','Cholecystitis/Gallbladder Disease');
INSERT INTO ichppccode VALUES ('077','309','(DO NOT USE) Adjustment Reaction, grief');
INSERT INTO ichppccode VALUES ('071.1','300','Somatoform/psychosomatic disturbance');
INSERT INTO ichppccode VALUES ('137','464','Laryngitis&Tracheitis, Acute, Croup');
INSERT INTO ichppccode VALUES ('009','052','Chickenpox');
INSERT INTO ichppccode VALUES ('174','598','Other Urinary System Diseases NEC/ RENAL FAILURE');
INSERT INTO ichppccode VALUES ('085','319','(DO NOT USE) Mental Retardation');
INSERT INTO ichppccode VALUES ('019','078','Warts, All Sites');
INSERT INTO ichppccode VALUES ('020','079','Viral Infection NOS');
INSERT INTO ichppccode VALUES ('098','369','Blindness');
INSERT INTO ichppccode VALUES ('108','390','Rheumatic Fever/Heart Disease');
INSERT INTO ichppccode VALUES ('010','053','Herpes Zoster, Shingles');
INSERT INTO ichppccode VALUES ('202.1','644','False Labour, Threatened Labour');
INSERT INTO ichppccode VALUES ('114','427','Paroxysmal Tachycardia');
INSERT INTO ichppccode VALUES ('255','781','Abnormal Involuntary Movement(tremor)');
INSERT INTO ichppccode VALUES ('113','427','Atrial Fibrillation or Flutter');
INSERT INTO ichppccode VALUES ('151','532','Duodenal Ulcer/Gastritis/Gastroenteritis');
INSERT INTO ichppccode VALUES ('344','895','Contraceptive Advice, Family Plan,contraception/BCP');
INSERT INTO ichppccode VALUES ('193','626','Disorders Of Menstruation, DUB');
INSERT INTO ichppccode VALUES ('085.1','319','Developmental delay');
INSERT INTO ichppccode VALUES ('302','805','Fracture Vertebral Column');
INSERT INTO ichppccode VALUES ('292','691','Rash & Other Non Spec. Skin Erupt.');
INSERT INTO ichppccode VALUES ('360.1','899','Parent/child problem');
INSERT INTO ichppccode VALUES ('073','300','(DO NOT USE) Neurosis, Other/Unspecified');
INSERT INTO ichppccode VALUES ('077.2','309','Coping with physical illness');
INSERT INTO ichppccode VALUES ('086.5','300','Self mutilation');
INSERT INTO ichppccode VALUES ('203','650','Uncomplicated Pregnancy, normal delivery');

--
-- Dumping data for table 'immunizations'
--

--
-- Dumping data for table 'measurementType'
--

INSERT INTO `measurementType` (`type`, `typeDisplayName`, `typeDescription`, `measuringInstruction`, `validation`, `createDate`) VALUES
( '02', 'Oxygen Saturatifn', 'Oxygen Saturation', 'percent', '4', '2013-02-01 00:00:00'),
( '24UA', '24 hour urine albumin', '24 hour urine albumin', 'mg/24h (nnn.n) Range:0-500 Interval:12mo.', '14', '2013-02-01 00:00:00'),
( '24UR', '24-hr Urine cr clearance & albuminuria', 'Renal 24-hr Urine cr clearance & albuminuria', 'q 6-12 months, unit mg', '3', '2013-02-01 00:00:00'),
( '5DAA', '5 Day Adherence if on ART', '5 Day Adherence if on ART', 'number', '4', '2013-02-01 00:00:00'),
( 'A1C', 'A1C', 'A1C', 'Range:0.040-0.200', '3', '2013-02-01 00:00:00'),
( 'AACP', 'Asthma Action Plan ', 'Asthma Action Plan ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ABO', 'Blood Group', 'ABO RhD blood type group', 'Blood Type', '11', '2014-05-09 00:00:00'),
( 'ACOS', 'Asthma Coping Strategies', 'Asthma Coping Strategies', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ACR', 'Alb creat ratio', 'ACR', 'in mg/mmol', '5', '2013-02-01 00:00:00'),
( 'ACS', 'Acute Conronary Syndrome', 'Acute Conronary Syndrome', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AEDR', 'Asthma Education Referral', 'Asthma Education Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AELV', 'Exacerbations since last visit requiring clincal evaluation', 'Exacerbations since last visit requiring clincal evaluation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AENC', 'Asthma Environmental Control', 'Asthma Environmental Control', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AFP', 'AFP', 'Alpha Fetoprotein', 'ug/L Range under 7', '5', '2014-05-09 00:00:00'),
( 'AHGM', 'Anit-hypoglycemic Medication', 'Anit-hypoglycemic Medication', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'AIDU', 'Active Intravenous Drug Use', 'Active Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ALB', 'Albumin', 'Serum Albumin', 'g/L Range 35-50', '5', '2014-05-09 00:00:00'),
( 'ALC', 'Alcohol', 'Alcohol', 'Yes/No/X', '12', '2013-02-01 00:00:00'),
( 'ALP', 'ALP', 'Alkaline Phosphatase', 'U/L Range 50-300', '14', '2014-05-09 00:00:00'),
( 'ALPA', 'Asthma Limits Physical Activity', 'Asthma Limits Physical Activity', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ALT', 'ALT', 'ALT', 'in U/L', '5', '2013-02-01 00:00:00'),
( 'ANA', 'ANA', 'Antinuclear Antibodies', 'result', '17', '2014-05-09 00:00:00'),
( 'Ang', 'Angina', 'Angina', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ANR', 'Asthma Needs Reliever   ', 'Asthma Needs Reliever   ', 'frequency per week', '14', '2013-02-01 00:00:00'),
( 'ANSY', 'Asthma Night Time Symtoms', 'Asthma Night Time Symtoms', 'frequency per week', '14', '2013-02-01 00:00:00'),
( 'AORA', 'ACE-I OR ARB', 'ACE-I OR ARB', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'APOB', 'APO B', 'Apolipoprotein B', 'g/L Range 0.5-1.2', '14', '2014-05-09 00:00:00'),
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
( 'BILI', 'Bilirubin', 'Total Bilirubin', 'umol/L Range under 20', '14', '2014-05-09 00:00:00'),
( 'BMED', 'Blood Pressure Medication Changes', 'BP Med Changes', 'Changed', '7', '2013-02-01 00:00:00'),
( 'BMI', 'Body Mass Index', 'BMI', 'BMI', '4', '2013-02-01 00:00:00'),
( 'BP', 'BP', 'Blood Pressure', 'BP Tru', '6', '2013-02-01 00:00:00'),
( 'BP', 'BP', 'Blood Pressure', 'supine', '6', '2013-02-01 00:00:00'),
( 'BP', 'BP', 'Blood Pressure', 'standing position', '6', '2013-02-01 00:00:00'),
( 'BP', 'BP', 'Blood Pressure', 'sitting position', '6', '2013-02-01 00:00:00'),
( 'BPII', 'BPI Pain Interference', 'BPI Pain Interference', 'null', '2', '2013-07-25 13:00:00'),
( 'BPIS', 'BPI Pain Severity', 'BPI Pain Severity', 'null', '2', '2013-07-25 00:00:00'),
( 'BTFT', 'Brush teeth with fluoride toothpaste', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'BUN', 'BUN', 'Blood Urea Nitrogen', 'mmol/L Range 2-9', '14', '2014-05-09 00:00:00'),
( 'C125', 'CA 125', 'CA 125', 'kU/L Range under 36', '14', '2014-05-09 00:00:00'),
( 'C153', 'CA 15-3', 'CA 15-3', 'kU/L Range under 23', '14', '2014-05-09 00:00:00'),
( 'C199', 'CA 19-9', 'CA 19-9', 'kU/L Range under 27', '14', '2014-05-09 00:00:00'),
( 'C3', 'C3', 'Complement component 3', 'umol/L', '14', '2014-05-09 00:00:00'),
( 'CA', 'Calcium', 'Calcium', 'mmol/L', '14', '2014-05-09 00:00:00'),
( 'CASA', 'Consider ASA', 'Consider ASA', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CAVD', 'Calcium and Vitamin D', 'NULL', 'Review', '16', '2014-01-23 13:00:00'),
( 'CD4', 'CD4', 'CD4', 'in x10e9/l', '14', '2013-02-01 00:00:00'),
( 'CD4P', 'CD4 Percent', 'CD4 Percent', 'in %', '4', '2013-02-01 00:00:00'),
( 'CDMP', 'Attended CDM Self Management Program', 'Attended CDM Self Management Program', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEA', 'CEA', 'CEA', 'umol/L', '14', '2014-05-09 00:00:00'),
( 'CEDE', 'Education Exercise', 'Education Exercise', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDM', 'Education Patient Meds', 'Education Patient Meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDS', 'Education Salt fluid ', 'Education Salt fluid ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CEDW', 'Education Daily Weight Monitoring', 'Education Daily Weight Monitoring', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CERV', 'ER visits for HF', 'ER visits for HF', 'integer', '2', '2013-02-01 00:00:00'),
( 'CGSD', 'Collaborative Goal Setting', 'Collaborative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CHLM', 'CHLM', 'Chlamydia', 'test result', '17', '2014-05-09 00:00:00'),
( 'CIMF', 'Child Immunization recall', 'Child Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'CK', 'CK', 'Creatinine Kinase', 'U/L', '14', '2014-05-09 00:00:00'),
( 'Clpl', 'Chloride', 'Chloride', 'mmol/L Range 98-106', '5', '2014-05-09 00:00:00'),
( 'CMBS', 'Coombs', 'Coombs', 'test result', '17', '2014-05-09 00:00:00'),
( 'CMVI', 'CMV IgG', 'CMV IgG', 'Positive', '7', '2013-02-01 00:00:00'),
( 'CODC', 'COD Classification', 'COD Classification', 'null', '11', '2013-02-01 00:00:00'),
( 'COGA', 'Cognitive Assessment', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'COPE', 'Provide COP Education Materials ', 'Provide COP Education Materials ', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COPM', 'Review COP Med use and Side effects', 'Review COP Med use and Side effects', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COPS', 'COP Specialist Referral', 'COP Specialist Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'COUM', 'Warfarin Weekly Dose', 'WarfarinDose', 'Total mg Warfarin per week', '5', '2013-02-01 00:00:00'),
( 'CRCL', 'Creatinine Clearance', 'Creatinine Clearance', 'in ml/h', '5', '2013-02-01 00:00:00'),
( 'CRP', 'CRP', 'C reactive protein', 'mg/L', '14', '2014-05-09 00:00:00'),
( 'CVD', 'CVD', 'Cerebrovascular disease', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'CXR', 'CXR', 'CXR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DARB', 'ACE AARB', 'ACE AARB', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DEPR', 'Depression', 'Depression', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DESM', 'Dental Exam Every 6 Months', 'Dental Exam Every 6 Months', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DiaC', 'Diabetes Counseling Given', 'Diabetes Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIER', 'Diet and Exercise', 'Diet and Exercise', 'Reviewed', '7', '2013-02-01 00:00:00'),
( 'DIET', 'Diet', 'Diet', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIFB', 'Impaired FB', 'Impaired FB', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIG', 'Digoxin', 'Digoxin Level', 'nmol/L Range 1-2.6', '14', '2014-05-09 00:00:00'),
( 'DIGT', 'Impaired GT', 'Impaired Glucose Tolerance', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DIL', 'Dilantin', 'Dilantin (Phenytoin) level', 'umol/L Range 40-80', '14', '2014-05-09 00:00:00'),
( 'DILY', 'Dentist in the last year', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'DM', 'DM', 'Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DMED', 'Diabetes Medication Changes', 'DM Med Changes', 'Changed', '7', '2013-02-01 00:00:00'),
( 'DMME', 'Diabetes Education', 'Diabetes Education', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'DMOE', 'Daily Morphine Equivalent', 'Daily Morphine Equivalent', 'null', '11', '2014-11-27 13:00:00'),
( 'DMSM', 'Diabetes Self Management Goals', 'Diabetes Self Management Goals', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'DNFS', 'DN4 Questionnaire', 'DN4 Questionnaire', 'null', '2', '2013-05-07 00:00:00'),
( 'DOLE', 'Date of last Exacerbation', 'Date of last Exacerbation', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00'),
( 'DpSc', 'Depression Screen', 'Feeling Sad, blue or depressed for 2 weeks or more', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DRCO', 'Drug Coverage', 'Drug Coverage', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DRPW', 'Drinks per Week', 'Drinks per Week', 'Number of Drinks per week', '5', '2013-02-01 00:00:00'),
( 'DT1', 'Type I', 'Diabetes Type 1', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DT2', 'Type II', 'Diabetes Type 2', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'DTYP', 'Diabetes Type', 'Diabetes Type', '1 or 2', '10', '2013-02-01 00:00:00'),
( 'ECG', 'ECG', 'ECG', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ECHK', 'Do you have your eyes regularly checked?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'EDC', 'EDC', 'Expected Date of Confinement', 'yyyy-mm-dd', '13', '2013-02-01 00:00:00'),
( 'EDDD', 'Education Diabetes', 'Education Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDF', 'EDF', 'Erectile Dysfunction', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDGI', 'Autonomic Neuropathy', 'Autonomic Neuropathy', 'Present', '7', '2013-02-01 00:00:00'),
( 'EDND', 'Education Nutrition Diabetes', 'Education Nutrition Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EDNL', 'Education Nutrition Lipids', 'Education Nutrition Lipids', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'EGFR', 'EGFR', 'EGFR', 'in ml/min', '4', '2013-02-01 00:00:00'),
( 'ENA', 'ENA', 'Extractable Nuclear Antigens', 'result', '11', '2014-05-09 00:00:00'),
( 'EPR', 'Exacerbation plan in place or reviewed', 'Exacerbation plan in place or reviewed', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ESR', 'ESR', 'Erythrocyte sedimentation rate', 'mm/h Range under 20', '14', '2014-05-09 00:00:00'),
( 'EXE', 'Exercise', 'Exercise', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'ExeC', 'Exercise Counseling Given', 'Exercise Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Exer', 'Exercise', 'Exercise', '[min/week 0-1200]', '14', '2013-02-01 00:00:00'),
( 'EYEE', 'Dilated Eye Exam', 'Eye Exam', 'Exam Done', '7', '2013-02-01 00:00:00'),
( 'FAHS', 'Risk of Falling', 'Risk of Falling', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FAMR', 'Family/Relationships', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'FAS', 'Folic Acid supplementation', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FBPC', '2 hr PC BG', '2 hr PC BG', 'in mmol/L', '3', '2013-02-01 00:00:00'),
( 'FBS', 'FBS', 'Glucose FBS', 'FBS', '3', '2013-02-01 00:00:00'),
( 'FEET', 'FEET', 'Feet Check skin', 'sensation (Yes/No)', '7', '2013-02-01 00:00:00'),
( 'FEET', 'FEET', 'Feet Check skin', 'vibration (Yes/No)', '7', '2013-02-01 00:00:00'),
( 'FEET', 'FEET', 'Feet Check skin', 'reflexes (Yes/No)', '7', '2013-02-01 00:00:00'),
( 'FEET', 'FEET', 'Feet Check skin', 'pulses (Yes/No)', '7', '2013-02-01 00:00:00'),
( 'FEET', 'FEET', 'Feet Check skin', 'infection (Yes/No)', '7', '2013-02-01 00:00:00'),
( 'Fer', 'Ferritin', 'Ferritin', 'ug/L Range 15-180', '14', '2014-05-09 00:00:00'),
( 'FEV1', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', 'Forced Expiratory Volume 1 Second', '14', '2013-02-01 00:00:00'),
( 'FGLC', 'Fasting Glucose meter , lab comparison', 'Fasting glucose meter, lab comparison', 'Within 20 percent', '7', '2013-02-01 00:00:00'),
( 'FICO', 'Financial Concerns', 'Financial Concerns', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'FIT', 'FIT', 'Fecal Immunochemical Test', 'result', '17', '2014-05-09 00:00:00'),
( 'FLOS', 'Floss', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'FLUF', 'Flu Recall', 'Flu Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'FOBF', 'FOBT prevention recall', 'FOBT Immunization Follow up', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'FOBT', 'FOBT', 'Fecal Occult Blood', 'result', '17', '2014-05-09 00:00:00'),
( 'FRAM', 'Framingham 10 year CAD', 'Framingham 10 year CAD', 'percent', '11', '2013-02-01 00:00:00'),
( 'FT3', 'FT3', 'Free T3', 'pmol/L Range 4-8', '14', '2014-05-09 00:00:00'),
( 'FT4', 'FT4', 'Free T4', 'pmol/L Range 11-22', '14', '2014-05-09 00:00:00'),
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
( 'FUPP', 'Assessment/Follow-up plans', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'G', 'Gravida', 'Gravida', 'Gravida', '3', '2013-02-01 00:00:00'),
( 'G6PD', 'G6PD', 'G6PD', 'Positive', '7', '2013-02-01 00:00:00'),
( 'GBS', 'GBS', 'Group B Strep', 'test result', '17', '2014-05-09 00:00:00'),
( 'GC', 'Gonococcus', 'Gonococcus', 'test result', '17', '2014-05-09 00:00:00'),
( 'GGT', 'GGT', 'Gamma-glutamyl transferase', 'U/L Range 10-58', '14', '2014-05-09 00:00:00'),
( 'GCT', '50g Glucose Challenge', '1h 50g Glucose Challenge', 'mmol/L Range under 7.8', '4', '2014-05-09 00:00:00'),
( 'GT1', '75g OGTT 1h', '1h 75g Glucose Tolerance Test', 'mmol/L Range under 10.6', '4', '2014-05-09 00:00:00'),
( 'GT2', '75g OGTT 2h', '2h 75g Glucose Tolerance Test', 'mmol/L Range under 9', '4', '2014-05-09 00:00:00'),
( 'Hb', 'Hb', 'Hb', 'in g/L', '5', '2013-02-01 00:00:00'),
( 'HCON', 'Do you have any hearing concerns?', 'NULL', 'Yes/No', '7', '2013-12-20 13:00:00'),
( 'HBEB', 'AntiHBeAg', 'AntiHBeAg', 'result', '17', '2014-05-09 00:00:00'),
( 'HBEG', 'HBeAg', 'HBeAg', 'result', '17', '2014-05-09 00:00:00'),
( 'HBVD', 'HBV DNA', 'HBV DNA', 'result', '17', '2014-05-09 00:00:00'),
( 'HCO3', 'Bicarbonate', 'Bicarbonate', 'mmol/L Range 20-29', '4', '2014-05-09 00:00:00'),
( 'Hchl', 'Hypercholesterolemia', 'Hypercholesterolemia', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HDL', 'HDL', 'High Density Lipid', 'in mmol/L (n.n) Range:0.4-4.0', '2', '2013-02-01 00:00:00'),
( 'HEAD', 'Head circumference', 'Head circumference', 'in cm (nnn) Range:30-70 Interval:2mo.', '4', '2013-02-01 00:00:00'),
( 'HFCG', 'HF Collaorative Goal Setting', 'HF Collaorative Goal Setting', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFCS', 'HF Self Management Challenge', 'HF Self Management Challenge', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMD', 'HF Mod Risk Factor Diabetes', 'HF Mod Risk Factor Diabetes', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMH', 'HF Mod Risk Factor Hyperlipidemia', 'HF Mod Risk Factor Hyperlipidemia', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMO', 'HF Mod Risk Factor Overweight', 'HF Mod Risk Factor Overweight', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMS', 'HF Mod Risk Factor Smoking', 'HF Mod Risk Factor Smoking', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HFMT', 'HF Mod Risk Factor Hypertension', 'HF Mod Risk Factor Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HIP', 'Hip Circ.', 'Hip Circumference', 'at 2 cm above navel', '14', '2013-02-01 00:00:00'),
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
( 'HPNP', 'Hearing protection/Noise control programs', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'HPYL', 'H Pylori', 'H Pylori', 'result', '17', '2014-05-09 00:00:00'),
( 'HR', 'P', 'Heart Rate', 'in bpm (nnn) Range:40-180', '5', '2013-02-01 00:00:00'),
( 'HRMS', 'Review med use and side effects', 'HTN Review of Medication use and side effects', 'null', '11', '2013-02-01 00:00:00'),
( 'HSMC', 'Self Management Challenges', 'HTN Self Management Challenges', 'null', '11', '2013-02-01 00:00:00'),
( 'HSMG', 'Self Management Goal', 'HTN Self Management Goal', 'null', '11', '2013-02-01 00:00:00'),
( 'HT', 'HT', 'Height', 'in cm', '5', '2013-02-01 00:00:00'),
( 'HTN', 'HTN', 'Hypertension', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'HYPE', 'Hypoglycemic Episodes', 'Number of Hypoglycemic Episodes', 'since last visit', '3', '2013-02-01 00:00:00'),
( 'HYPM', 'Hypoglycemic Management', 'Hypoglycemic Management', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'IART', 'Currently On ART', 'Currently On ART', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'IBPL', 'Income below poverty line', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'iDia', 'Eye Exam: Diabetic Retinopathy', 'Diabetic Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iEx', 'Eye Exam: Significant Pathology', 'Significant Pathology', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iHyp', 'Eye Exam: Hypertensive Retinopathy', 'Hypertensive Retinopathy', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'INR', 'INR', 'INR', 'INR Blood Work', '5', '2013-02-01 00:00:00'),
( 'INSL', 'Insulin', 'Insulin', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iOth', 'Eye Exam: Other Vascular Abnomality', 'Other Vascular Abnormality', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'iPTH', 'iPTH', 'intact Parathyroid Hormone', 'pmol/L Range 1-6', '14', '2014-05-09 00:00:00'),
( 'iRef', 'Eye Exam: Refferal Made', 'Refferal Made', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'KEEL', 'Keele Score', 'Keele Score', 'null', '2', '2013-05-07 00:00:00'),
( 'JVPE', 'JPV Elevation', 'JPV Elevation', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Kpl', 'Potassium', 'Potassium', 'in mmol/L', '2', '2013-02-01 00:00:00'),
( 'LcCt', 'Locus of Control Screen', 'Feeling lack of control over daily life', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LDL', 'LDL', 'Low Density Lipid', 'monitor every 1-3 year', '2', '2013-02-01 00:00:00'),
( 'LEFP', 'LEFS Pain', 'Lower Extremity Functional Scale - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'LETH', 'Lethargy', 'Lethargic', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LHAD', 'Lung Related Hospital Admission', 'Lung Related Hospital Admission', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'LITH', 'Lithium', 'Lithium', 'mmol/L Range 0.6-0.8', '14', '2014-05-09 00:00:00'),
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
( 'MCV', 'MCV', 'Mean corpuscular volume', 'fL Range 82-98', '14', '2014-05-09 00:00:00'),
( 'MedA', 'Medication adherence access barriers', 'Difficulty affording meds or getting refills on time', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedG', 'Medication adherence general problem', 'Any missed days or doses of meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedN', 'Medication adherence negative beliefs', 'Concerns about side effects or medication is not working', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MedR', 'Medication adherence recall barriers', 'Difficulty remembering to take meds', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'MG', 'Mg', 'Magnesium', 'mmol/L Range 0.7-1.2', '14', '2014-05-09 00:00:00'),
( 'MI', 'MI', 'MI', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'Napl', 'Sodium', 'Sodium', 'in mmol/L', '5', '2013-02-01 00:00:00'),
( 'NDIP', 'CMCC NDI Pain', 'CMCC Neck Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'NDIS', 'CMCC NDI Score', 'CMCC Neck Disability Index - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'NERF', 'Neuropathic Features?', 'Neuropathic Features?', 'null', '15', '2013-05-07 00:00:00'),
( 'NOSK', 'Number of Cigarettes per day', 'Smoking', 'Cigarettes per day', '5', '2013-02-01 00:00:00'),
( 'NOVS', 'Need for nocturnal ventilated support', 'Need for nocturnal ventilated support', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'NtrC', 'Diet/Nutrition Counseling Given', 'Diet/Nutrition Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'NYHA', 'NYHA Functional Capacity Classification', 'NYHA Functional Capacity Classification', 'Class 1-4', '9', '2013-02-01 00:00:00'),
( 'OPAE', 'Opioid Adverse Effects', 'Opioid Adverse Effects', 'null', '17', '2014-11-27 13:00:00'),
( 'OPAB', 'Opioid Aberrant Behaviour', 'Opioid Aberrant Behaviour', 'null', '17', '2014-11-27 13:00:00'),
( 'OPUS', 'Opioid Urine Drug Screen', 'Opioid Urine Drug Screen', 'null', '17', '2014-11-27 13:00:00'),
( 'ORSK', 'Opioid Risk', 'NULL', 'Score 0-26', '3', '2013-12-27 13:00:00'),
( 'OSWP', 'Oswestry BDI Pain', 'Oswestry Back Disability Index - Pain', 'number', '5', '2013-02-01 00:00:00'),
( 'OSWS', 'Oswestry BDI Score', 'Oswestry Back Disability Index - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'OTCO', 'Other Concerns', 'Other Concerns', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'OthC', 'Other Counseling Given', 'Other Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'OUTR', 'Outside Spirometry Referral', 'Outside Spirometry Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'P', 'Para', 'Para', 'Para', '3', '2013-02-01 00:00:00'),
( 'PANE', 'Painful Neuropathy', 'Painful Neuropathy', 'Present', '7', '2013-02-01 00:00:00'),
( 'PAPF', 'Pap Recall', 'Pap Recall Documentation', 'Patient Contacted by Letter or Phone', '11', '2013-02-01 00:00:00'),
( 'PB19', 'Parvovirus', 'Parvovirus B19', 'result', '11', '2014-05-09 00:00:00'),
( 'PEDE', 'Pitting Edema', 'Pitting Edema', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PEFR', 'PEFR value', 'PEFR value', 'null', '14', '2013-02-01 00:00:00'),
( 'PHIN', 'Pharmacological Intolerance', 'Pharmacological Intolerance', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PHOS', 'Phosphate', 'Phosphate', 'mmol/L Range 0.8-1.4', '14', '2014-05-09 00:00:00'),
( 'PHQS', 'PHQ4 Depression Anxiety Score', 'PHQ4 Depression Anxiety Score', 'null', '3', '2013-05-07 00:00:00'),
( 'PIDU', 'Previous Intravenous Drug Use', 'Previous Intravenous Drug Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PLT', 'Platelets', 'Platelets', 'x10 9/L Range 150-400', '14', '2014-05-09 00:00:00'),
( 'PPD', 'PPD', 'PPD', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PROT', 'Protein', 'Total Protein Serum', 'g/L Range 60-80', '14', '2014-05-09 00:00:00'),
( 'PRRF', 'Pulmonary Rehabilitation Referral', 'Pulmonary Rehabilitation Referral', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PSA', 'PSA', 'Prostatic specific antigen', 'ug/L Range under 5', '14', '2014-05-09 00:00:00'),
( 'PSPA', 'Patient Sets physical Activity Goal', 'Patient Sets physical Activity Goal', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PSQS', 'PSQ3 Sleep Score', 'PSQ3 Sleep Score', 'null', '5', '2013-05-07 00:00:00'),
( 'PSSC', 'Psychosocial Screening', 'Psychosocial Screening', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PsyC', 'Psychosocial Counseling Given', 'Psychosocial Counseling Given', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PTSD', 'PC PTSD Trauma Score', 'PC PTSD Trauma Score', 'null', '2', '2013-05-07 00:00:00'),
( 'PVD', 'PVD', 'Peripheral vascular disease', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'PXAM', 'Physical Exam', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'QDSH', 'QuickDASH Score', 'Disabilities of the Arm, Shoulder and Hand - Score', 'number', '5', '2013-02-01 00:00:00'),
( 'RABG', 'Recommend ABG', 'Recommend ABG', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'REBG', 'Review Blood Glucose Records', 'Review Glucose Records', 'Reviewed', '7', '2013-02-01 00:00:00'),
( 'RESP', 'RR', 'Respiratory Rate', 'Breaths per minute', '4', '2013-02-01 00:00:00'),
( 'RETI', 'Retinopathy', 'null', 'Discussed', '7', '2013-02-01 00:00:00'),
( 'RF', 'RF', 'Rheumatoid Factor', 'result', '17', '2014-05-09 00:00:00'),
( 'Rh', 'Rh', 'RhD blood type group', 'result', '11', '2014-05-09 00:00:00'),
( 'RPHR', 'Review PHR', 'Review PHR', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'RPPT', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Review Pathophysiology, Prognosis, Treatment with Patient', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'RUB', 'Rubella', 'Rubella titre', 'titre', '11', '2014-05-09 00:00:00'),
( 'RVTN', 'Revascularization', 'Revascularization', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SBLT', 'Seat belts', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SCR', 'Serum Creatinine', 'Creatinine', 'in umol/L', '14', '2013-02-01 00:00:00'),
( 'SDET', 'Smoke detector that works', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SDUS', 'Street Drug Use', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SEXF', 'Sexual Function', 'Sexual Function', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SEXH', 'Sexual History', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'SHAB', 'Sleep Habits', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
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
( 'SSXC', 'Safe Sex Counselling', 'NULL', 'Review', '16', '2014-01-23 13:00:00'),
( 'STIS', 'STI Screening', 'Sexual Transmitted Infections', 'Review', '16', '2014-01-23 13:00:00'),
( 'STRE', 'Stress Testing', 'Stress Testing', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'StSc', 'Stress Screen', 'Several periods of irritability, feeling filled with anxiety, or difficulty sleeping b/c of stress', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SUAB', 'Substance Use', 'Substance Use', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'SUNP', 'Sun protection', 'NULL', 'Yes/No', '7', '2013-10-25 13:00:00'),
( 'SUO2', 'Need for supplemental oxygen', 'Need for supplemental oxygen', 'Yes/No', '7', '2013-02-01 00:00:00'),
( 'TCHD', 'TC/HDL', 'LIPIDS TD/HDL', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00'),
( 'TCHL', 'Total Cholestorol', 'Total Cholestorol', 'in mmol/L (nn.n) Range:2.0-12.0', '2', '2013-02-01 00:00:00'),
( 'TEMP', 'Temp', 'Temperature', 'degrees celcius', '3', '2013-02-01 00:00:00'),
( 'TG', 'TG', 'LIPIDS TG', 'monitor every 1-3 year', '3', '2013-02-01 00:00:00'),
( 'TOXP', 'Toxoplasma IgG', 'Toxoplasma IgG', 'Positive', '7', '2013-02-01 00:00:00'),
( 'TRIG', 'Triglycerides', 'Triglycerides', 'in mmol/L (nn.n) Range:0.0-12.0', '3', '2013-02-01 00:00:00'),
( 'TSAT', 'Transferrin Saturation', 'Transferrin Saturation', 'percent Range 20-50', '4', '2014-05-09 00:00:00'),
( 'TSH', 'TSH', 'Thyroid Stimulating Hormone', 'null', '4', '2013-02-01 00:00:00'),
( 'TUG', 'Timed Up and Go', 'Timed Up and Go', 'Number of Seconds', '14', '2013-02-01 00:00:00'),
( 'UAIP', 'Update AIDS defining illness in PMH', 'Update AIDS defining illness in PMH', 'Changed', '7', '2013-02-01 00:00:00'),
( 'UDUS', 'Update Drug Use', 'Update Drug Use', 'Changed', '7', '2013-02-01 00:00:00'),
( 'UHTP', 'Update HIV Test History in PMH', 'Update HIV Test History in PMH', 'Changed', '7', '2013-02-01 00:00:00'),
( 'URBH', 'Update Risk Behaviours', 'Update Risk Behaviours', 'Changed', '7', '2013-02-01 00:00:00'),
( 'URIC', 'Uric Acid', 'Uric Acid', 'umol/L Range 230-530', '14', '2014-05-09 00:00:00'),
( 'USSH', 'Update Sexual Identity in Social History', 'Update Sexual Identity in Social History', 'Changed', '7', '2013-02-01 00:00:00'),
( 'VB12', 'Vit B12', 'Vitamin B12', 'Range >0 pmol/l', '14', '2013-02-01 00:00:00'),
( 'VDRL', 'VDRL', 'VDRL', 'Positive', '7', '2013-02-01 00:00:00'),
( 'VLOA', 'Viral Load', 'Viral Load', 'in x10e9/L', '14', '2013-02-01 00:00:00'),
( 'VZV', 'Zoster', 'Varicella Zoster', 'result', '17', '2014-05-09 00:00:00'),
( 'WAIS', 'Waist', 'Waist', 'Waist Circum in cm', '5', '2013-02-01 00:00:00'),
( 'WBC', 'WBC', 'White Cell Count', 'x10 9/L Range 4-11', '14', '2014-05-09 00:00:00'),
( 'WHR', 'Waist:Hip', 'Waist Hip Ratio', 'Range:0.5-2 Interval:3mo.', '2', '2013-02-01 00:00:00'),
( 'WKED', 'Work/Education', 'NULL', 'Review', '16', '2013-12-30 13:00:00'),
( 'WT', 'WT', 'Weight', 'in kg', '5', '2013-02-01 00:00:00');

insert into measurementType VALUES(null, 'PRGT','PRGT','Pregnancy Test','Pregnancy Test','19',now());
insert into measurementType VALUES(null, 'UDIP','UDIP','Urine Dip Test','Urine Dip Test','19',now());
insert into measurementType VALUES(null, 'GLMT','GLMT','Glucose Monitor Test','Glucose Monitor Test','19',now());
insert into measurementType VALUES(null, 'LECM','LECM','Left Eye Check Up Measurement','Left Eye Check Up Measurement','11',now());
insert into measurementType VALUES(null, 'RECM','RECM','Right Eye Check Up Measurement','Right Eye Check Up Measurement','11',now()); 
--
-- Dumping data for table 'measurementCSSLocation'
--


--
-- Dumping data for table 'measurementGroup'
--

--
-- Dumping data for table 'measurementGroupStyle'
--

--
-- Dumping data for table 'messagelisttbl'
--

--
-- Dumping data for table 'messagetbl'
--

--
-- Dumping data for table 'mygroup'
--

INSERT INTO mygroup VALUES ('IT Support','88888','Support','IT',NULL,NULL);

--
-- Dumping data for table 'oscarcommlocations'
--

INSERT INTO oscarcommlocations VALUES (145,'Oscar Users',NULL,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<addressBook><group><group desc=\"doc\" id=\"17\"><address desc=\"Chan, David\" id=\"174\"/><address desc=\"oscardoc, doctor\" id=\"999998\"/></group><group desc=\"receptionist\" id=\"18\"><address desc=\"oscarrep, receptionist\" id=\"999999\"/><address desc=\"Support, IT\" id=\"88888\"/></group><group desc=\"admin\" id=\"19\"><address desc=\"oscaradmin, admin\" id=\"999997\"/></group><address desc=\"Chan, David\" id=\"174\"/><address desc=\"oscaradmin, admin\" id=\"999997\"/><address desc=\"oscardoc, doctor\" id=\"999998\"/><address desc=\"oscarrep, receptionist\" id=\"999999\"/><address desc=\"Support, IT\" id=\"88888\"/></group></addressBook>',NULL);


insert into ProviderPreference (providerNo, startHour, endHour, everyMin, myGroupNo, colourTemplate, defaultCaisiPmm, defaultNewOscarCme, printQrCodeOnPrescriptions, lastUpdated, appointmentScreenLinkNameDisplayLength,defaultDoNotDeleteBilling,eRxEnabled,eRxTrainingMode) values ('999998','8','18','15','.default','deepblue','disabled','disabled', 0, now(), 3,0,0,0);

--
-- Dumping data for table 'provider'
--

INSERT INTO provider VALUES ('999998','oscardoc','doctor','doctor',null,'','','','0001-01-01','','','','','','','','1','','','','','','','','',now(),'0001-01-01');

--
-- Dumping data for table 'quickList'
--
INSERT INTO quickList VALUES(1,"default", "999997", "000","ichppc");
INSERT INTO quickList VALUES(2,"default", "999997", "204","ichppc");
INSERT INTO quickList VALUES(3,"default", "999997", "288","ichppc");
INSERT INTO quickList VALUES(4,"default", "999997", "053","ichppc");
INSERT INTO quickList VALUES(5,"default", "999997", "235","ichppc");
INSERT INTO quickList VALUES(6,"List1", "999998", "235","ichppc");
INSERT INTO quickList VALUES(7,"List1", "999998", "376","ichppc");
INSERT INTO quickList VALUES(8,"List1", "999998", "246","ichppc");
INSERT INTO quickList VALUES(9,"List1", "999998", "105","ichppc");
INSERT INTO quickList VALUES(10,"List1", "999998", "231","ichppc");

--
-- Dumping data for table 'radetail'
--

--
-- Dumping data for table 'raheader'
--

--
-- Dumping data for table 'recycle_bin'
--

--
-- Dumping data for table 'recyclebin'
--

--
-- Dumping data for table 'remoteAttachments'
--

--
-- Dumping data for table 'reportagesex'
--

--
-- Dumping data for table 'reportprovider'
--

INSERT INTO reportprovider VALUES (1,'174','Docs','billingreport','A');

--
-- Dumping data for table 'reporttemp'
--

--
-- Dumping data for table 'rschedule'
--

--
-- Dumping data for table 'scheduledate'
--

--
-- Dumping data for table 'scheduledaytemplate'
--

--
-- Dumping data for table 'scheduleholiday'
--

INSERT INTO scheduleholiday VALUES ('2002-03-29','Good Friday');
INSERT INTO scheduleholiday VALUES ('2002-04-01','Easter Monday - Hospital');
INSERT INTO scheduleholiday VALUES ('2002-07-01','Canada Day');
INSERT INTO scheduleholiday VALUES ('2002-05-20','Victoria Day');
INSERT INTO scheduleholiday VALUES ('2002-08-05','Civic Day');
INSERT INTO scheduleholiday VALUES ('2002-09-02','Labour Day');
INSERT INTO scheduleholiday VALUES ('2002-10-14','Thanksgiving Day');
INSERT INTO scheduleholiday VALUES ('2002-11-11','2nd Monday in November - Hospital');
INSERT INTO scheduleholiday VALUES ('2002-02-11','2nd Monday in February - Hospital');
INSERT INTO scheduleholiday VALUES ('2002-01-01','New Year\'s Day');
INSERT INTO scheduleholiday VALUES ('2002-12-25','Christmas Day');
INSERT INTO scheduleholiday VALUES ('2002-12-26','Boxing Day');
INSERT INTO scheduleholiday VALUES ('2003-01-01','New Year\'s Day');
INSERT INTO scheduleholiday VALUES ('2003-02-10','2nd Monday in February - Hospital');
INSERT INTO scheduleholiday VALUES ('2003-04-18','Good Friday');
INSERT INTO scheduleholiday VALUES ('2003-04-21','Easter Monday - Hospital');
INSERT INTO scheduleholiday VALUES ('2002-12-27','In Lieu of Day Before Christmas - University');
INSERT INTO scheduleholiday VALUES ('2002-12-30','Floating Holiday - University');
INSERT INTO scheduleholiday VALUES ('2002-12-31','Floating Holiday - University');
INSERT INTO scheduleholiday VALUES ('2003-05-19','Victoria Day');
INSERT INTO scheduleholiday VALUES ('2003-07-01','Canada Day');
INSERT INTO scheduleholiday VALUES ('2003-08-04','Civic Day');
INSERT INTO scheduleholiday VALUES ('2003-09-01','Labour Day');
INSERT INTO scheduleholiday VALUES ('2003-10-13','Thanksgiving Day');
INSERT INTO scheduleholiday VALUES ('2003-11-10','2nd Monday in November - Hospital');
INSERT INTO scheduleholiday VALUES ('2003-12-25','Christmas Day');
INSERT INTO scheduleholiday VALUES ('2003-12-26','Boxing Day');
INSERT INTO scheduleholiday VALUES ('2004-01-01','New Year\'s Day');

--
-- Dumping data for table 'scheduletemplate'
--

--
-- Dumping data for table 'scheduletemplatecode'
--

INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('A','Academic','',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('B','Behavioral Science','15','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('2','30 Minute Appointment','30','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('3','45 Minute Appointment','45','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('P','Phone time','15','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('M','Monitoring','','EED2EE','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('6','60 Minute Appointment','60','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('C','Chart Audit Rounds','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('R','Rounds','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('E','Study Leave','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('V','Vacation','15','FFF68F','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('G','PBSG Rounds','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('H','Hospital Rounds','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('d','Drug Rep (Chief)','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('U','Urgent','15',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('a','Administrative Work','15','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('t','Travel','',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('m','Meeting','',NULL,'N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('1','15 Minute Appointment','15','#BFEFFF','N',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('s','Same Day','15','FFF68F','Day',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('S','Same Day - R1','30','FFF68F','Day',1);
INSERT INTO scheduletemplatecode (code,description,duration,color,confirm,bookinglimit) VALUES ('W','Same Week','15','FFF68F','Wk','1');

--
-- Dumping data for table 'security'
--

INSERT INTO security(security_no,user_name,password,provider_no,pin,forcePasswordReset,storageVersion) VALUES (128,'oscardoc','-51-282443-97-5-9410489-60-1021-45-127-12435464-32','999998','1117',1,1);
--
-- Dumping data for table 'serviceSpecialists'
--

--
-- Dumping data for table 'specialistsJavascript'
--

INSERT INTO specialistsJavascript VALUES (1,'1','function makeSpecialistslist(dec){\n if(dec==\'1\') \n{K(-1,\"----Choose a Service-------\");D(-1,\"--------Choose a Specialist-----\");}\nelse\n{K(-1,\"----All Services-------\");D(-1,\"--------All Specialists-----\");}\nK(53,\"Cardiology\");\nD(53,\"297\",\"ss4444\",\"ssss, sss ssss\",\"sss\",\"sssss\");\n\nK(54,\"Dermatology\");\n\nK(55,\"Neurology\");\n\nK(56,\"Radiology\");\n\nK(57,\"SEE NOTES\");\n\n\n}\n');

--
-- Dumping data for table 'study'
--

--
-- Dumping data for table 'studydata'
--


--
-- Dumping data for table 'tickler'
--


--
-- Dumping data for table 'tmpdiagnosticcode'
--

--
-- Dumping data for table 'validations'
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
(14,'Numeric Value greater than or equal to 0',NULL,0,0,0,0,1,NULL,0),
(15, 'Yes/No/Maybe', 'YES|yes|Yes|Y|NO|no|No|N|MAYBE|maybe|Maybe', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(16, 'Review', 'REVIEWED|reviewed|Reviewed', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(17,'pos or neg', 'pos|neg|positive|negative', NULL, NULL, NULL, NULL, NULL, NULL, NULL);


insert into `secRole` values(1, 'receptionist', 'receptionist');
insert into `secRole` values(2, 'doctor', 'doctor');
insert into `secRole` values(3, 'admin', 'admin');
insert into `secRole` values(4, 'locum', 'locum');
insert into `secRole` values(5, 'nurse', 'nurse');
insert into `secRole` values(6, 'Vaccine Provider', 'Vaccine Provider');
insert into `secRole` values(null, 'external', 'External');
insert into `secRole` values(null, 'er_clerk', 'ER Clerk');
insert into `secRole` (role_name, description) values('psychiatrist', 'psychiatrist');
insert into `secRole` (role_name, description) values('RN', 'Registered Nurse');
insert into `secRole` (role_name, description) values('RPN', 'Registered Practical Nurse');
insert into `secRole` (role_name, description) values('Nurse Manager', 'Nurse Manager');
insert into `secRole` (role_name, description) values('Clinical Social Worker','Clinical Social Worker');
insert into `secRole` (role_name, description) values('Clinical Case Manager','Clinical Case Manager');
insert into `secRole` (role_name, description) values('Medical Secretary', 'Medical Secretary');
insert into `secRole` (role_name, description) values('Clinical Assistant', 'Clinical Assistant');
insert into `secRole` (role_name, description) values('secretary', 'secretary');
insert into `secRole` (role_name, description) values('counsellor', 'counsellor');
insert into `secRole` (role_name, description) values('Case Manager', 'Case Manager');
insert into `secRole` (role_name, description) values('Housing Worker', 'Housing Worker');
insert into `secRole` (role_name, description) values('Support Worker', 'Support Worker');
insert into `secRole` (role_name, description) values('Client Service Worker', 'Client Service Worker');
insert into `secRole` (role_name, description) values('CAISI ADMIN', 'CAISI ADMIN');
insert into `secRole` (role_name, description) values('Recreation Therapist', 'Recreation Therapist');
insert into `secRole` (role_name, description) values('property staff','property staff');
insert into `secRole` (role_name, description) values('Support Counsellor','Support Counsellor');
insert into `secRole` (role_name, description) values('Counselling Intern', 'Counselling Intern');
insert into `secRole` (role_name, description) values('Field Note Admin', 'Field Note Admin');
INSERT INTO `secRole` (`role_name`, `description` ) VALUES ('student', 'Student (OSCAR Learning)');
INSERT INTO `secRole` (`role_name`, `description` ) VALUES ('moderator', 'Moderator (OSCAR Learning)');


insert into `secUserRole` (`provider_no`,`role_name`,`orgcd`,`activeyn`,lastUpdateDate) values('999998', 'doctor', 'R0000001',1,now());
insert into `secUserRole` (`provider_no`,`role_name`,`orgcd`,`activeyn`,lastUpdateDate) values('999998', 'admin', 'R0000001',1,now());
insert into `secUserRole` (`provider_no`,`role_name`,`orgcd`,`activeyn`,lastUpdateDate) values('999997', 'receptionist', 'R0000001',1,now());

insert into `secPrivilege` values(1, 'x', 'All rights.');
insert into `secPrivilege` values(2, 'r', 'Read');
insert into `secPrivilege` values(3, 'w', 'Write');
insert into `secPrivilege` values(4, 'd', 'Delete');
insert into `secPrivilege` values(5, 'o', 'No rights.');
insert into `secPrivilege` values(6, 'u', 'Update');

insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_appointment', 'Appointment',0);
insert into `secObjectName`  (`objectName`,`description`,`orgapplicable`) values('_admin','Administration',0);
insert into `secObjectName`  (`objectName`,`description`,`orgapplicable`) values('_eChart', 'Encounter', 0);
insert into `secObjectName`  (`objectName`,`description`,`orgapplicable`) values('_demographic', 'Client Demographic Info', 0);
insert into `secObjectName`  (`objectName`) values('_appointment.doctorLink');
insert into `secObjectName` (`objectName`) values('_eChart.verifyButton');
insert into `secObjectName` (`objectName`) values('_billing');
insert into `secObjectName` (`objectName`) values('_tasks');
insert into `secObjectName` (`objectName`) values('_formMentalHealth');
insert into `secObjectName` (`objectName`) values ('_admin.userAdmin');
insert into `secObjectName` (`objectName`) values ('_admin.schedule');
insert into `secObjectName` (`objectName`) values ('_admin.billing');
insert into `secObjectName` (`objectName`) values ('_admin.resource');
insert into `secObjectName` (`objectName`) values ('_admin.reporting');
insert into `secObjectName` (`objectName`) values ('_admin.backup');
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_admin.measurements', 'access to customize measurements',0);
insert into `secObjectName` (`objectName`) values ('_admin.messenger');
insert into `secObjectName` (`objectName`) values ('_admin.eform');
insert into `secObjectName` (`objectName`) values ('_admin.encounter');
insert into `secObjectName` (`objectName`) values ('_admin.misc');
insert into `secObjectName` (`objectName`) values ('_admin.pmm');
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_masterlink', 'Client Master Record',0);
insert into `secObjectName` (`objectName`) values ('_rx');
insert into `secObjectName` (`objectName`) values ('_merge');
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_flowsheet','Flow Sheet',0);

insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_casemgmt.issues','Access to Case Management Issues',0);
insert into `secObjectName` (`objectName`,`description`,`orgapplicable`) values('_casemgmt.notes','Permissions for Case Management Notes',0);

INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`) VALUES ('_team_schedule_only', 'Restrict schedule to only login provider and his team', '0');
INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`) VALUES ('_team_billing_only', 'Restrict billing access to only login provider and his team', '0');
INSERT INTO `secObjectName` (`objectName`, `description`, `orgapplicable`) VALUES ('_admin.fax', 'Configure & Manage Faxes', '0');

insert into `secObjectName` (`objectName`) values ('_newCasemgmt.preventions');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.viewTickler');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.DxRegistry');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.forms');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.eForms');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.documents');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.labResult');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.oscarMsg');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.measurements');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.consultations');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.allergies');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.prescriptions');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.otherMeds');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.riskFactors');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.familyHistory');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.decisionSupportAlerts');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.medicalHistory');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.calculators');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.templates');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.cpp');
insert into `secObjectName` (`objectName`) values ('_eform.doctor');

insert into `secObjectName` (`objectName`) values ('_resource');
insert into `secObjectName` (`objectName`) values ('_search');
insert into `secObjectName` (`objectName`) values ('_report');
insert into `secObjectName` (`objectName`) values ('_msg');
insert into `secObjectName` (`objectName`) values ('_con');
insert into `secObjectName` (`objectName`) values ('_pmm_agencyList');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.apptHistory');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.doctorName');

insert into `secObjectName` (`objectName`) values ('_day');
insert into `secObjectName` (`objectName`) values ('_month');
insert into `secObjectName` (`objectName`) values ('_pref');
insert into `secObjectName` (`objectName`) values ('_edoc');
insert into `secObjectName` (`objectName`) values ('_tickler');
insert into `secObjectName` (`objectName`) values ('_pmm_client.BedRoomReservation');
insert into `secObjectName` (`objectName`) values('_pmm.functionalCentre');
insert into `secObjectName` (`objectName`) values ('_pmm_editProgram.vacancies');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.pregnancy');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.episode');
insert into `secObjectName` (`objectName`) values ('_newCasemgmt.photo');

insert into `secObjectName` (`objectName`) values ('_hrm');
insert into `secObjectName` (`objectName`) values ('_eform');
insert into `secObjectName` (`objectName`) values ('_form');
insert into `secObjectName` (`objectName`) values ('_measurement');
insert into `secObjectName` (`objectName`) values ('_lab');
insert into `secObjectName` (`objectName`) values ('_prevention');
insert into `secObjectName` (`objectName`) values ('_dxresearch');
insert into `secObjectName` (`objectName`) values ('_allergy');
insert into `secObjectName` (`objectName`) values ('_eyeform');
insert into `secObjectName` (`objectName`) values ('_appDefinition');
insert into `secObjectName` (`objectName`) values ('_phr');

insert into `secObjectName` (`objectName`) values ('_pmm');
insert into `secObjectName` (`objectName`) values ('_pmm.editProgram.schedules');
insert into `secObjectName` (`objectName`) values ('_admin.consult');
insert into `secObjectName` (`objectName`) values ('_admin.document');

insert into `secObjectName`  (`objectName`,`description`,`orgapplicable`) values('_demographicExport', 'Export Demographic', 0);

insert into `secObjectName` (`objectName`) values ('_dashboardManager');
insert into `secObjectName` (`objectName`) values ('_dashboardDisplay');
insert into `secObjectName` (`objectName`) values ('_dashboardDrilldown');

insert into `secObjPrivilege` values('receptionist', '_appointment', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist', '_demographic', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist', '_billing', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist','_masterLink','x',0,999998);
insert into `secObjPrivilege` values('receptionist', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist', '_casemgmt.notes', 'x', 0, '999998');
insert into `secObjPrivilege` values('receptionist','_pref','x',0,'999998');


insert into `secObjPrivilege` values('doctor','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_appointment.doctorLink','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_billing','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_rx','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_admin.reporting','o',0,999998);

insert into `secObjPrivilege` values('doctor','_admin.facilityMessage','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.systemMessage','o',0,999998);

insert into `secObjPrivilege` values('doctor','_admin.provider','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.security','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.securityLogReport','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.unlockAccount','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.cookieRevolver','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.caisi','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.lookupFieldEditor','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.issueEditor','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.caisiRoles','o',0,999998);
insert into `secObjPrivilege` values('doctor','_admin.userCreatedForms','o',0,999998);

insert into `secObjPrivilege` values('doctor','_pmm.clientSearch','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.newClient','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.mergeRecords','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.caseManagement','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.agencyInformation','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.manageFacilities','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.staffList','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.programList','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.addProgram','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.globalRoleAccess','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.caisiRoles','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm.functionalCentre','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.general','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.staff','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.functionUser','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.teams','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.clients','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.queue','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.access','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.bedCheck','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.clientStatus','x',0,999998);
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.serviceRestrictions','x',0,999998);

insert into `secObjPrivilege` values('doctor','_newCasemgmt.preventions','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.viewTickler','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.DxRegistry','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.forms','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.eForms','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.documents','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.labResult','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.oscarMsg','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.measurements','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.consultations','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.allergies','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.prescriptions','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.otherMeds','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.riskFactors','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.familyHistory','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.decisionSupportAlerts','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.medicalHistory','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.calculators','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.templates','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.cpp','x',0,'999998');

insert into `secObjPrivilege` values('doctor','_eform.doctor','x',0,'999998');

insert into `secObjPrivilege` values('doctor','_resource','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_search','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_report','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_msg','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_con','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm_agencyList','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.apptHistory','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.doctorName','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_day','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_month','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pref','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_edoc','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_tickler','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm_client.BedRoomReservation','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm_editProgram.vacancies','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.pregnancy','o',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.episode','o',0,'999998');
insert into `secObjPrivilege` values('doctor','_newCasemgmt.photo','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_hrm','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_eform','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_form','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_measurement','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_lab','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_prevention','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_dxresearch','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_allergy','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_eyeform','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_phr','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_admin.document','x',0,'999998');
insert into `secObjPrivilege` values('doctor','_pmm','x',0,'999998');

insert into `secObjPrivilege` values('admin', '_admin', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin','_masterLink','x',0,999998);
insert into `secObjPrivilege` values('admin', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin', '_casemgmt.notes', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin','_admin.caisi','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.cookieRevolver','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.facilityMessage','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.issueEditor','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.lookupFieldEditor','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.provider','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.reporting','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.security','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.securityLogReport','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.systemMessage','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.unlockAccount','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.fax','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.userCreatedForms','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.measurements','x',0,'999998');
insert into `secObjPrivilege` values('admin','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('admin','_appointment.doctorLink','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.addProgram','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.globalRoleAccess','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.manageFacilities','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.programList','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.staffList','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.access','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.bedCheck','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.clients','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.clientStatus','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.functionUser','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.general','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.queue','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.serviceRestrictions','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.staff','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_editProgram.teams','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm.editor','x',0,999998);
insert into `secObjPrivilege` values('admin','_pmm.functionalCentre','x',0,999998);
insert into `secObjPrivilege` values('admin','_newCasemgmt.preventions','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.viewTickler','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.DxRegistry','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.forms','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.eForms','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.documents','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.labResult','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.oscarMsg','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.measurements','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.consultations','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.allergies','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.prescriptions','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.otherMeds','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.riskFactors','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.familyHistory','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.decisionSupportAlerts','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.medicalHistory','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.calculators','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.templates','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.cpp','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.pmm','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pref','x',0,'999998');


insert into `secObjPrivilege` values('admin','_resource','x',0,'999998');
insert into `secObjPrivilege` values('admin','_search','x',0,'999998');
insert into `secObjPrivilege` values('admin','_report','x',0,'999998');
insert into `secObjPrivilege` values('admin','_msg','x',0,'999998');
insert into `secObjPrivilege` values('admin','_con','x',0,'999998');
insert into `secObjPrivilege` values('admin','_pmm_agencyList','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.apptHistory','x',0,'999998');
insert into `secObjPrivilege` values('admin','_newCasemgmt.doctorName','x',0,'999998');

insert into `secObjPrivilege` values('doctor','_admin.traceability','x',0,'999998');
insert into `secObjPrivilege` values('admin','_admin.traceability','x',0,'999998');
insert into `secObjPrivilege` values('admin','_appDefinition','x',0,'999998');
insert into `secObjPrivilege` values('admin','_demographicExport','x',0,'999998');
insert into `secObjPrivilege` values('admin', '_pmm.editProgram.schedules', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin', '_admin.consult', 'x', 0, '999998');
insert into `secObjPrivilege` values('admin', '_admin.document', 'x', 0, '999998');




-- for defaultqueue
insert into queue values(1,'default');
insert into secObjectName values('_queue.1','default',0);

-- for role locum
insert into `secObjPrivilege` values('locum', '_appointment', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_eChart', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_appointment.doctorLink', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_eChart.verifyButton', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_demographic', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_billing', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_tasks', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum','_masterLink','x',0,999998);
insert into `secObjPrivilege` values('locum','_rx','x',0,999998);
insert into `secObjPrivilege` values('locum', '_casemgmt.issues', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum', '_casemgmt.notes', 'x', 0, '999998');
insert into `secObjPrivilege` values('locum','_pref','x',0,'999998');

-- for role nurse
insert into `secObjPrivilege` values('nurse','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_phr','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('nurse','_pref','x',0,'999998');



insert into `secObjPrivilege` values('psychiatrist','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_appointment.doctorLink','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_billing','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_rx','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('psychiatrist','_pref','x',0,'999998');

insert into `secObjPrivilege` values('RN','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('RN','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('RN','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('RN','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('RN','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('RN','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('RN','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('RN','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('RN','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('RN','_pref','x',0,'999998');



insert into `secObjPrivilege` values('RPN','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('RPN','_pref','x',0,'999998');




insert into `secObjPrivilege` values('Nurse Manager','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Nurse Manager','_pref','x',0,'999998');


insert into `secObjPrivilege` values('Clinical Social Worker','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Social Worker','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Clinical Case Manager','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Case Manager','_pref','x',0,'999998');

insert into `secObjPrivilege` values('counsellor','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('counsellor','_pref','x',0,'999998');


insert into `secObjPrivilege` values('Case Manager','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Case Manager','_pref','x',0,'999998');


insert into `secObjPrivilege` values('Housing Worker','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Housing Worker','_pref','x',0,'999998');


insert into `secObjPrivilege` values('Medical Secretary','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Medical Secretary','_pref','x',0,'999998');


insert into `secObjPrivilege` values('Clinical Assistant','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Clinical Assistant','_pref','x',0,'999998');


insert into `secObjPrivilege` values('secretary','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.caisiRoles','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('secretary','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Support Worker','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Support Worker','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Client Service Worker','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Client Service Worker','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Recreation Therapist','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Recreation Therapist','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Counselling Intern','_appointment','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_casemgmt.issues','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_casemgmt.notes','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_demographic','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_eChart','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_eChart.verifyButton','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_flowsheet','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_masterLink','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.agencyInformation','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.caseManagement','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.clientSearch','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.mergeRecords','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm.newClient','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_tasks','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pmm','x',0,'999998');
insert into `secObjPrivilege` values('Counselling Intern','_pref','x',0,'999998');

insert into `secObjPrivilege` values('Field Note Admin','_admin.fieldnote','x',0,'999998');

insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree) values('R0000001','Shelter Management Information System',1,10,'R000001');

insert into lst_orgcd (code, description, activeyn, orderbyindex,codetree) values('O0000020','Salvation Army',1,30,'R0000001O0000020');

INSERT INTO `country_codes` VALUES (1,'AFGHANISTAN','AF','en'),(2,'LAND ISLANDS','AX','en'),(3,'ALBANIA','AL','en'),(4,'ALGERIA','DZ','en'),(5,'AMERICAN SAMOA','AS','en'),(6,'ANDORRA','AD','en'),(7,'ANGOLA','AO','en'),(8,'ANGUILLA','AI','en'),(9,'ANTARCTICA','AQ','en'),(10,'ANTIGUA AND BARBUDA','AG','en'),(11,'ARGENTINA','AR','en'),(12,'ARMENIA','AM','en'),(13,'ARUBA','AW','en'),(14,'AUSTRALIA','AU','en'),(15,'AUSTRIA','AT','en'),(16,'AZERBAIJAN','AZ','en'),(17,'BAHAMAS','BS','en'),(18,'BAHRAIN','BH','en'),(19,'BANGLADESH','BD','en'),(20,'BARBADOS','BB','en'),(21,'BELARUS','BY','en'),(22,'BELGIUM','BE','en'),(23,'BELIZE','BZ','en'),(24,'BENIN','BJ','en'),(25,'BERMUDA','BM','en'),(26,'BHUTAN','BT','en'),(27,'BOLIVIA','BO','en'),(28,'BOSNIA AND HERZEGOVINA','BA','en'),(29,'BOTSWANA','BW','en'),(30,'BOUVET ISLAND','BV','en'),(31,'BRAZIL','BR','en'),(32,'BRITISH INDIAN OCEAN TERRITORY','IO','en'),(33,'BRUNEI DARUSSALAM','BN','en'),(34,'BULGARIA','BG','en'),(35,'BURKINA FASO','BF','en'),(36,'BURUNDI','BI','en'),(37,'CAMBODIA','KH','en'),(38,'CAMEROON','CM','en'),(39,'CANADA','CA','en'),(40,'CAPE VERDE','CV','en'),(41,'CAYMAN ISLANDS','KY','en'),(42,'CENTRAL AFRICAN REPUBLIC','CF','en'),(43,'CHAD','TD','en'),(44,'CHILE','CL','en'),(45,'CHINA','CN','en'),(46,'CHRISTMAS ISLAND','CX','en'),(47,'COCOS (KEELING) ISLANDS','CC','en'),(48,'COLOMBIA','CO','en'),(49,'COMOROS','KM','en'),(50,'CONGO','CG','en'),(51,'CONGO, THE DEMOCRATIC REPUBLIC OF THE','CD','en'),(52,'COOK ISLANDS','CK','en'),(53,'COSTA RICA','CR','en'),(54,'CïTE D IVOIRE','CI','en'),(55,'CROATIA','HR','en'),(56,'CUBA','CU','en'),(57,'CYPRUS','CY','en'),(58,'CZECH REPUBLIC','CZ','en'),(59,'DENMARK','DK','en'),(60,'DJIBOUTI','DJ','en'),(61,'DOMINICA','DM','en'),(62,'DOMINICAN REPUBLIC','DO','en'),(63,'ECUADOR','EC','en'),(64,'EGYPT','EG','en'),(65,'EL SALVADOR','SV','en'),(66,'EQUATORIAL GUINEA','GQ','en'),(67,'ERITREA','ER','en'),(68,'ESTONIA','EE','en'),(69,'ETHIOPIA','ET','en'),(70,'FALKLAND ISLANDS (MALVINAS)','FK','en'),(71,'FAROE ISLANDS','FO','en'),(72,'FIJI','FJ','en'),(73,'FINLAND','FI','en'),(74,'FRANCE','FR','en'),(75,'FRENCH GUIANA','GF','en'),(76,'FRENCH POLYNESIA','PF','en'),(77,'FRENCH SOUTHERN TERRITORIES','TF','en'),(78,'GABON','GA','en'),(79,'GAMBIA','GM','en'),(80,'GEORGIA','GE','en'),(81,'GERMANY','DE','en'),(82,'GHANA','GH','en'),(83,'GIBRALTAR','GI','en'),(84,'GREECE','GR','en'),(85,'GREENLAND','GL','en'),(86,'GRENADA','GD','en'),(87,'GUADELOUPE','GP','en'),(88,'GUAM','GU','en'),(89,'GUATEMALA','GT','en'),(90,'GUERNSEY','GG','en'),(91,'GUINEA','GN','en'),(92,'GUINEA-BISSAU','GW','en'),(93,'GUYANA','GY','en'),(94,'HAITI','HT','en'),(95,'HEARD ISLAND AND MCDONALD ISLANDS','HM','en'),(96,'HOLY SEE (VATICAN CITY STATE)','VA','en'),(97,'HONDURAS','HN','en'),(98,'HONG KONG','HK','en'),(99,'HUNGARY','HU','en'),(100,'ICELAND','IS','en'),(101,'INDIA','IN','en'),(102,'INDONESIA','ID','en'),(103,'IRAN, ISLAMIC REPUBLIC OF','IR','en'),(104,'IRAQ','IQ','en'),(105,'IRELAND','IE','en'),(106,'ISLE OF MAN','IM','en'),(107,'ISRAEL','IL','en'),(108,'ITALY','IT','en'),(109,'JAMAICA','JM','en'),(110,'JAPAN','JP','en'),(111,'JERSEY','JE','en'),(112,'JORDAN','JO','en'),(113,'KAZAKHSTAN','KZ','en'),(114,'KENYA','KE','en'),(115,'KIRIBATI','KI','en'),(116,'KOREA, DEMOCRATIC PEOPLES REPUBLIC OF','KP','en'),(117,'KOREA, REPUBLIC OF','KR','en'),(118,'KUWAIT','KW','en'),(119,'KYRGYZSTAN','KG','en'),(120,'LAO PEOPLES DEMOCRATIC REPUBLIC','LA','en'),(121,'LATVIA','LV','en'),(122,'LEBANON','LB','en'),(123,'LESOTHO','LS','en'),(124,'LIBERIA','LR','en'),(125,'LIBYAN ARAB JAMAHIRIYA','LY','en'),(126,'LIECHTENSTEIN','LI','en'),(127,'LITHUANIA','LT','en'),(128,'LUXEMBOURG','LU','en'),(129,'MACAO','MO','en'),(130,'MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF','MK','en'),(131,'MADAGASCAR','MG','en'),(132,'MALAWI','MW','en'),(133,'MALAYSIA','MY','en'),(134,'MALDIVES','MV','en'),(135,'MALI','ML','en'),(136,'MALTA','MT','en'),(137,'MARSHALL ISLANDS','MH','en'),(138,'MARTINIQUE','MQ','en'),(139,'MAURITANIA','MR','en'),(140,'MAURITIUS','MU','en'),(141,'MAYOTTE','YT','en'),(142,'MEXICO','MX','en'),(143,'MICRONESIA, FEDERATED STATES OF','FM','en'),(144,'MOLDOVA','MD','en'),(145,'MONACO','MC','en'),(146,'MONGOLIA','MN','en'),(147,'MONTENEGRO','ME','en'),(148,'MONTSERRAT','MS','en'),(149,'MOROCCO','MA','en'),(150,'MOZAMBIQUE','MZ','en'),(151,'MYANMAR','MM','en'),(152,'NAMIBIA','NA','en'),(153,'NAURU','NR','en'),(154,'NEPAL','NP','en'),(155,'NETHERLANDS','NL','en'),(156,'NETHERLANDS ANTILLES','AN','en'),(157,'NEW CALEDONIA','NC','en'),(158,'NEW ZEALAND','NZ','en'),(159,'NICARAGUA','NI','en'),(160,'NIGER','NE','en'),(161,'NIGERIA','NG','en'),(162,'NIUE','NU','en'),(163,'NORFOLK ISLAND','NF','en'),(164,'NORTHERN MARIANA ISLANDS','MP','en'),(165,'NORWAY','NO','en'),(166,'OMAN','OM','en'),(167,'PAKISTAN','PK','en'),(168,'PALAU','PW','en'),(169,'PALESTINIAN TERRITORY, OCCUPIED','PS','en'),(170,'PANAMA','PA','en'),(171,'PAPUA NEW GUINEA','PG','en'),(172,'PARAGUAY','PY','en'),(173,'PERU','PE','en'),(174,'PHILIPPINES','PH','en'),(175,'PITCAIRN','PN','en'),(176,'POLAND','PL','en'),(177,'PORTUGAL','PT','en'),(178,'PUERTO RICO','PR','en'),(179,'QATAR','QA','en'),(180,'RƒUNION','RE','en'),(181,'ROMANIA','RO','en'),(182,'RUSSIAN FEDERATION','RU','en'),(183,'RWANDA','RW','en'),(184,'SAINT BARTHƒLEMY','BL','en'),(185,'SAINT HELENA','SH','en'),(186,'SAINT KITTS AND NEVIS','KN','en'),(187,'SAINT LUCIA','LC','en'),(188,'SAINT MARTIN','MF','en'),(189,'SAINT PIERRE AND MIQUELON','PM','en'),(190,'SAINT VINCENT AND THE GRENADINES','VC','en'),(191,'SAMOA','WS','en'),(192,'SAN MARINO','SM','en'),(193,'SAO TOME AND PRINCIPE','ST','en'),(194,'SAUDI ARABIA','SA','en'),(195,'SENEGAL','SN','en'),(196,'SERBIA','RS','en'),(197,'SEYCHELLES','SC','en'),(198,'SIERRA LEONE','SL','en'),(199,'SINGAPORE','SG','en'),(200,'SLOVAKIA','SK','en'),(201,'SLOVENIA','SI','en'),(202,'SOLOMON ISLANDS','SB','en'),(203,'SOMALIA','SO','en'),(204,'SOUTH AFRICA','ZA','en'),(205,'SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS','GS','en'),(206,'SPAIN','ES','en'),(207,'SRI LANKA','LK','en'),(208,'SUDAN','SD','en'),(209,'SURINAME','SR','en'),(210,'SVALBARD AND JAN MAYEN','SJ','en'),(211,'SWAZILAND','SZ','en'),(212,'SWEDEN','SE','en'),(213,'SWITZERLAND','CH','en'),(214,'SYRIAN ARAB REPUBLIC','SY','en'),(215,'TAIWAN, PROVINCE OF CHINA','TW','en'),(216,'TAJIKISTAN','TJ','en'),(217,'TANZANIA, UNITED REPUBLIC OF','TZ','en'),(218,'THAILAND','TH','en'),(219,'TIMOR-LESTE','TL','en'),(220,'TOGO','TG','en'),(221,'TOKELAU','TK','en'),(222,'TONGA','TO','en'),(223,'TRINIDAD AND TOBAGO','TT','en'),(224,'TUNISIA','TN','en'),(225,'TURKEY','TR','en'),(226,'TURKMENISTAN','TM','en'),(227,'TURKS AND CAICOS ISLANDS','TC','en'),(228,'TUVALU','TV','en'),(229,'UGANDA','UG','en'),(230,'UKRAINE','UA','en'),(231,'UNITED ARAB EMIRATES','AE','en'),(232,'UNITED KINGDOM','GB','en'),(233,'UNITED STATES','US','en'),(234,'UNITED STATES MINOR OUTLYING ISLANDS','UM','en'),(235,'URUGUAY','UY','en'),(236,'UZBEKISTAN','UZ','en'),(237,'VANUATU','VU','en'),(238,'VATICAN CITY STATE','VA','en'),(239,'VENEZUELA','VE','en'),(240,'VIET NAM','VN','en'),(241,'VIRGIN ISLANDS, BRITISH','VG','en'),(242,'VIRGIN ISLANDS, U.S.','VI','en'),(243,'WALLIS AND FUTUNA','WF','en'),(244,'WESTERN SAHARA','EH','en'),(245,'YEMEN','YE','en'),(246,'ZAMBIA','ZM','en'),(247,'ZIMBABWE','ZW','en');


INSERT INTO `appointment_status` VALUES 
(1,'t','To Do','#FDFEC7','starbill.gif',1,0,0,'TODO'),
(2,'T','Daysheet Printed','#FDFEC7','todo.gif',1,0,0,'DSPrt'),
(3,'H','Here','#00ee00','here.gif',1,1,0,'HERE'),
(4,'P','Picked','#FFBBFF','picked.gif',1,1,0,'PICK'),
(5,'E','Empty Room','#FFFF33','empty.gif',1,1,0,'EmpRm'),
(11,'N','No Show','#cccccc','noshow.gif',1,0,0,'NOSHO'),
(12,'C','Cancelled','#999999','cancel.gif',1,0,0,'CAN'),
(13,'B','Billed','#3ea4e1','billed.gif',1,0,0,'BILL'),
(6,'a','Customized 1','#897DF8','1.gif',1,1,0,'CUST1'),
(7,'b','Customized 2','#897DF8','2.gif',1,1,0,'CUST2'),
(8,'c','Customized 3','#897DF8','3.gif',0,1,0,'CUST3'),
(9,'d','Customized 4','#897DF8','4.gif',1,1,0,'CUST4'),
(10,'e','Customized 5','#897DF8','5.gif',1,1,0,'CUST5');

insert into gstControl set gstPercent = 5;

-- create security objects for multi-office control
insert into `secObjectName` 
	(objectName, `description`, orgapplicable)
	values
	('_team_access_privacy', 'restrict access to only the same team of a provider', 0);

insert into `secObjectName` 
	(objectName, `description`, orgapplicable)
	values
	('_site_access_privacy', 'restrict access to only the assigned sites of a provider', 0);

-- create new roles for multi-office control
insert into `secRole` 
	(role_no, role_name, `description`)
SELECT 
	 (SELECT MAX(role_no) from `secRole`)  +  1 AS role_no
	,'Site Manager' AS role_name
	,'Site Manager' AS `description`;

insert into `secRole` 
	(role_no, role_name, `description`)
SELECT 
	 (SELECT MAX(role_no) from `secRole`)  +  1 AS role_no
	,'Partner Doctor' AS role_name
	,'Partner Doctor' AS `description`;


insert into issue (code,description,role,update_date,priority,type,sortOrderId) values ('PastOcularHistory','Past Ocular History','nurse',now(),NULL,'system',0);
insert into issue (code,description,role,update_date,priority,type,sortOrderId) values ('DiagnosticNotes','Diagnostic Notes','nurse',now(),NULL,'system',0);
insert into issue (code,description,role,update_date,priority,type,sortOrderId) values ('OcularMedication','Ocular Medication','nurse',now(),NULL,'system',0);
insert into issue (code,description,role,update_date,priority,type,sortOrderId) values ('PatientLog','Patient Log','nurse',now(),NULL,'system',0);
insert into issue (`code`,`description`,`role`,`update_date`,type,sortOrderId) Values('CurrentHistory','Current History', 'nurse', now(),'system',0);

INSERT INTO `issue` (`code`, `description`, `role`, `update_date`, `priority`, `type`,sortOrderId)
VALUES
        ('eyeformFollowUp', 'Follow-Up Item for Eyeform', 'nurse', NOW(), NULL, 'system',0),
        ('eyeformCurrentIssue', 'Current Presenting Issue Item for Eyeform', 'nurse', NOW(), NULL, 'system',0),
        ('eyeformPlan', 'Plan Item for Eyeform', 'nurse', NOW(), NULL, 'system',0),
        ('eyeformImpression', 'Impression History Item for Eyeform', 'nurse', NOW(), NULL, 'system',0),
        ('eyeformProblem', 'Problem List Item for Eyeform', 'nurse', NOW(), NULL, 'system',0);


insert into HRMCategory values (null, 'General Oscar Lab', 'DEFAULT');
insert into HRMCategory values (null, 'Oscar HRM Category CT:ABDW' ,'CT:ABDW');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP5' ,'RAD:CSP5');
insert into HRMCategory values (null, 'Oscar HRM Category NM:THYSAN' ,'NM:THYSAN');
insert into HRMCategory values (null, 'Oscar HRM Category NM:BLDPOL' ,'NM:BLDPOL');
insert into HRMCategory values (null, 'Oscar HRM Category US:ABDC' ,'US:ABDC');
insert into HRMCategory values (null, 'Oscar HRM Category US:PELVLT' ,'US:PELVLT');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD' ,'RAD:ABD');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CXR2' ,'RAD:CXR2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ABD2' ,'RAD:ABD2');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ANKB' ,'RAD:ANKB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:CSP' ,'RAD:CSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:TSP' ,'RAD:TSP');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:LSP4ER' ,'RAD:LSP4ER');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:DIGB' ,'RAD:DIGB');
insert into HRMCategory values (null, 'Oscar HRM Category RAD:ELBB' ,'RAD:ELBB');
insert into HRMCategory values (null, 'Oscar HRM Category MAM:MAMMOB' ,'MAM:MAMMOB');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:ECHO' ,'ECHO:ECHO');
insert into HRMCategory values (null, 'Oscar HRM Category ECHOWL:ECH0520' ,'ECHOWL:ECH0520');
insert into HRMCategory values (null, 'Oscar HRM Category ECHO:MDAB' ,'ECHO:MDAB');

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health",".","Lakeridge Health Oshawa");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","MHB","Lakeridge Health Bowmanville");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","NDP","Lakeridge Health Port Perry");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","OE.LHC","Lakeridge Health ");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Lakeridge Health","RAD.OSG","Lakeridge Health");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVA","Rouge Valley Ajax and Pickering");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RVC","Rouge Valley Centenary");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","RAD.APG","Rouge Valley");
	
INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","PRH","Peterborough Regional Health Centre");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","NHC","Northumberland Hills Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","CMH","Campbellford Memorial Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Peterborough Regional Health Centre","RAD.PRH","Peterborough/Northumberland/Campbellford");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","GRA","The Scarborough Hospital - Birchmount Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","SCS","The Scarborough Hospital - General Campus");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("The Scarborough Hospitals","RAD.SCS","The Scarborough Hospital");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Ontario Shores","WHA","Ontario Shores");

INSERT INTO HL7HandlerMSHMapping
 (hospital_site,facility,facility_name) VALUES ("Rouge Valley Health System","APG","Rouge Valley Ajax and Pickering");
 
 INSERT INTO `clinic_nbr` 
VALUES
	(NULL, '22','R .  M .  A .', 'A'),
	(NULL, '33','AFP Ham Surgery RMA', 'A'),
	(NULL, '98','Bill Directs', 'A');


INSERT INTO billing_payment_type (id, payment_type) VALUES (1,'CASH');
INSERT INTO billing_payment_type (id, payment_type) VALUES (2,'CHEQUE');
INSERT INTO billing_payment_type (id, payment_type) VALUES (3,'VISA');
INSERT INTO billing_payment_type (id, payment_type) VALUES (4,'MASTERCARD');
INSERT INTO billing_payment_type (id, payment_type) VALUES (5,'AMEX');
INSERT INTO billing_payment_type (id, payment_type) VALUES (6,'ELECTRONIC');
INSERT INTO billing_payment_type (id, payment_type) VALUES (7,'DEBIT');

INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (1,'Agency','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (2,'Age','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (3,'Area','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (4,'Serious and Persistent Mental Illness','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (5,'Serious and Persistent Mental Illness Diagnosis','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (6,'Serious and Persistent Mental Illness Hospitalization','number','0',1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (7,'Type of Program','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (8,'Referral Source','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (9,'Legal History','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (10,'Residence','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (11,'Other Health Issues','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (12,'Language','select_multiple',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (13,'Gender','select_one',NULL,1,1,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (14,'Gender','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (15,'Homeless','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (16,'Mental health diagnosis','select_multiple',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (17,'Housing type','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (18,'Referral source','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (19,'Support level','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (20,'Geographic location','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (21,'Age category','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (22,'Current involvement with Criminal Justice system','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (23,'SHPPSU criteria','select_one',NULL,1,2,0);
INSERT INTO `criteria_type` (`CRITERIA_TYPE_ID`,`FIELD_NAME`,`FIELD_TYPE`,`DEFAULT_VALUE`,`ACTIVE`,`WL_PROGRAM_ID`,`CAN_BE_ADHOC`) VALUES (24,'Accessible unit','select_one',NULL,1,2,0);

/*
-- Query: SELECT * FROM test.criteria_type_option
LIMIT 0, 1000

-- Date: 2012-04-14 15:35
*/
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (1,2,1,'Youth – 14 – 22',NULL,14,22);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (2,2,2,'Youth  - 16-24',NULL,16,24);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (3,3,1,'North York','North York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (4,3,2,'Scarborough','Scarborough',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (5,3,3,'East York','East York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (6,3,4,'Old City of York','Old City of York',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (7,3,5,'North Etobicoke','North Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (8,3,6,'South Etobicoke','South Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (9,3,7,'Downtown Toronto','Downtown Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (10,3,8,'East of Yonge','East of Yonge',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (11,3,9,'West of Yonge','West of Yonge',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (12,3,10,'Toronto','Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (13,2,3,'16 Years of age or older',NULL,16,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (14,2,4,'18 years of age or older',NULL,18,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (15,4,1,'Formal Diagnosis','Formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (16,4,2,'No formal Diagnosis','No formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (19,5,1,'Test diagnosis 1','Test diagnosis 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (20,5,2,'Test diagnosis 2','Test diagnosis 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (21,7,1,'Long-Term Case Management','Long-Term Case Management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (22,7,2,'Short-term case management','Short-term case management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (23,7,3,'Emergency Department Diversion Program','Emergency Department Diversion Program',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (24,7,4,'Assertive Community Treatment Team','Assertive Community Treatment Team',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (25,7,5,'Mental Health Outreach Program','Mental Health Outreach Program',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (26,7,6,'Language Specific Service (Across Boundaries, CRCT, WRAP, Pathways, Passages)','Language Specific Service',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (27,7,7,'Youth Programs','Youth Programs',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (28,7,8,'Early Intervention Programs','Early Intervention Programs',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (29,7,9,'Mental Health Prevention Program (short-term case management)','Mental Health Prevention Program (short-term case management)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (30,7,10,'Seniors Case Management','Seniors Case Management',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (31,7,11,'TCAT (Addictions case management)','TCAT (Addictions case management)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (32,7,12,'CATCH','CATCH',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (33,7,13,'CATCH - ED','CATCH - ED',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (34,1,1,'Across Boundaries','Across Boundaries',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (35,1,2,'Bayview Community Services','Bayview Community Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (36,8,1,'Organizational Referral Source','Organizational Referral Source',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (37,8,2,'Accredited Professional (i.e. private psychiatrist, family doctor etc)','Accredited Professional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (38,8,3,' Self',' Self',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (39,8,4,'Family/Friend','Family/Friend',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (40,8,5,'Hospital (List of all hospitals)','Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (41,8,6,'Ontario Review Board','Ontario Review Board',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (42,8,7,'Alternative Access Route (i.e. internal referral, pre-existing agreement, alternate access route)','Alternative Access Route',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (43,9,1,'Test legal history 1','Test legal history 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (44,9,2,'Test legal history 2','Test legal history 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (45,10,1,'Housed','Housed',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (46,10,2,'Homeless','Homeless',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (47,10,3,'Transitional','Transitional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (48,11,1,'Concurrent Disorder','Concurrent Disorder',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (49,11,2,'Dual Diagnosis','Dual Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (50,11,3,'Acquired brain injury','Acquired brain injury',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (51,11,4,'Psycho-geriatric  issues','Psycho-geriatric  issues',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (52,12,1,'English','English',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (53,12,2,'French','French',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (54,12,3,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (55,13,1,'Male','Male',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (56,13,2,'Female','Female',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (57,14,1,'Male','Male',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (58,14,2,'Female','Female',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (59,15,1,'Homeless','Homeless',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (60,15,2,'At risk','At risk',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (61,15,3,'Housed','Housed',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (62,16,1,'Formal Diagnosis','Formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (63,16,2,'No formal Diagnosis','No formal Diagnosis',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (65,17,1,'Shared','Shared',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (66,17,2,'Independent','Independent',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (67,18,1,'Organizational Referral Source','Organizational Referral Source',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (68,18,2,'Accredited Professional (i.e. private psychiatrist, family doctor etc)','Accredited Professional',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (69,19,1,'Test level 1','Test level 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (70,19,2,'Test level 2','Test level 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (75,22,1,'Test involvement 1','Test involvement 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (76,22,2,'Test involvement 2','Test involvement 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (77,23,1,'Test SHPPSU criteria 1','Test SHPPSU criteria 1',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (78,23,2,'Test SHPPSU criteria 2','Test SHPPSU criteria 2',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (79,24,1,'Accessible unit required','Accessible unit required',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (80,24,2,'Accessible unit not required','Accessible unit not required',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (82,1,3,'CMHA (Toronto East)','CMHA (Toronto East)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (83,1,4,'CMHA (Toronto West)','CMHA (Toronto West)',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (84,1,5,'COTA Health','COTA Health',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (85,1,6,'Community Resource Connections of Toronto','Community Resource Connections of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (86,1,7,'Griffin Centre & Community Support Network','Griffin Centre & Community Support Network',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (87,1,8,'North York General Hospital','North York General Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (88,1,9,'Reconnect Mental Health Services','Reconnect Mental Health Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (89,1,10,'Saint Elizabeth Health Care','Saint Elizabeth Health Care',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (90,1,11,'Scarborough Hospital','Scarborough Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (91,1,12,'Sunnybrook Hospital','Sunnybrook Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (92,1,13,'Toronto North Support Services','Toronto North Support Services',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (93,13,3,'Transgender','Transgender',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (94,13,4,'Transsexual','Transsexual',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (95,13,5,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (96,14,3,'Transgender','Transgender',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (97,14,4,'Transsexual','Transsexual',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (98,14,5,'Other','Other',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (99,18,3,' Self',' Self',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (100,18,4,'Family/Friend','Family/Friend',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (101,18,5,'Hospital (List of all hospitals)','Hospital',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (102,18,6,'Ontario Review Board','Ontario Review Board',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (103,18,7,'Alternative Access Route (i.e. internal referral, pre-existing agreement, alternate access route)','Alternative Access Route',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (104,20,1,'West End of Toronto (Bathurst to Islington, Lawrence to Lakeshore) ','West End of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (105,20,1,'East End of Toronto (Don Valley to Victoria Park, Lawrence to Lakeshore)','East End of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (106,20,1,'Downtown Core of Toronto (Bathurst to Don Valley, Lawrence to Lakeshore)','Downtown Core of Toronto',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (107,20,1,'North York East (North of Lawrence, East of Yonge to Victoria Park) ','North York East',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (108,20,1,'North York West (North of Lawrence, West of Yonge to Islington)','North York West',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (109,20,1,'Etobicoke (West of Islington) ','Etobicoke',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (110,20,1,'Scarborough (East of Victoria Park)','Scarborough',NULL,NULL);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (111,21,1,'Youth – 14 – 22',NULL,14,22);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (112,21,2,'Youth  - 16-24',NULL,16,24);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (113,21,3,'16 Years of age or older',NULL,16,120);
INSERT INTO `criteria_type_option` (`OPTION_ID`,`CRITERIA_TYPE_ID`,`DISPLAY_ORDER_NUMBER`,`OPTION_LABEL`,`OPTION_VALUE`,`RANGE_START_VALUE`,`RANGE_END_VALUE`) VALUES (114,21,4,'18 years of age or older',NULL,18,120);

insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised Test Results", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see INFO", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC see MD", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Rx", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Advised RTC for immunization", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Declined treatment", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Don't call", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Letter sent", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg on ans. mach. to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Msg with roomate to call clinic", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Phone - No Answer", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Notified. Patient is asymptomatic.", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription given", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Prescription phoned in to:", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Referral Booked", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Re-Booked for followup", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Returned for Lab Work", now(), "1");
insert into tickler_text_suggest (creator, suggested_text, create_date, active) values ("-1", "Telephone Busy", now(), "1");

INSERT INTO secObjectName VALUES('_caseload.DisplayMode',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Age',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Sex',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastAppt',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.NextAppt',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.ApptsLYTD',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Lab',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Doc',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Tickler',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Msg',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.BMI',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.BP',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.WT',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.SMK',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.A1C',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.ACR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.SCR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LDL',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.HDL',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.TCHD',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.EGFR',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.EYEE',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastEncounterDate',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.LastEncounterType',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.CashAdmissionDate',NULL,0);
INSERT INTO secObjectName VALUES('_caseload.Access1AdmissionDate',NULL,0);

insert into `secObjectName`  (`objectName`,`description`,`orgapplicable`) values ('_admin.traceability', 'Right to generate trace and run traceability report',0);



INSERT INTO secObjPrivilege VALUES('doctor','_caseload.DisplayMode','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Age','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Sex','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastAppt','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.NextAppt','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.ApptsLYTD','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Lab','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Doc','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Tickler','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Msg','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.BMI','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.BP','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.WT','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.SMK','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.A1C','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.ACR','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LDL','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.HDL','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.TCHD','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.EGFR','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.EYEE','x',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastEncounterDate','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.LastEncounterType','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.CashAdmissionDate','o',0,'999998');
INSERT INTO secObjPrivilege VALUES('doctor','_caseload.Access1AdmissionDate','o',0,'999998');

INSERT INTO `OscarCode` VALUES (1,'CKDSCREEN','Ckd Screening');

insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Mother','Mother',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Father','Father',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Parent','Parent',true,'Son','Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Wife','Wife',true,'Husband','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Husband','Husband',true,'Partner','Wife');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Partner','Partner',true,'Partner','Partner');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Brother','Brother',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Sister','Sister',true,'Brother','Sister');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Son','Son',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Daughter','Daughter',true,'Father','Mother');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Aunt','Aunt',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Uncle','Uncle',true,'Nephew','Niece');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Nephew','Nephew',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Niece','Niece',true,'Uncle','Aunt');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandFather','GrandFather',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('GrandMother','GrandMother',true,'GrandSon','GrandDaughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Parent','Foster Parent',true,'Foster Son','Foster Daughter');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Son','Foster Son',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active,maleInverse,femaleInverse) values ('Foster Daughter','Foster Daughter',true,'Foster Parent','Foster Parent');
insert into CtlRelationships (value,label,active) values ('Guardian','Guardian',true);
insert into CtlRelationships (value,label,active) values ('Next of Kin','Next of kin',true);
insert into CtlRelationships (value,label,active) values ('Administrative Staff','Administrative Staff',true);
insert into CtlRelationships (value,label,active) values ('Care Giver','Care Giver',true);
insert into CtlRelationships (value,label,active) values ('Power of Attorney','Power of Attorney',true);
insert into CtlRelationships (value,label,active) values ('Insurance','Insurance',true);
insert into CtlRelationships (value,label,active) values ('Guarantor','Guarantor',true);
insert into CtlRelationships (value,label,active) values ('Other','Other',true);

INSERT INTO LookupList(name, description, categoryId, active, createdBy, dateCreated) VALUES('reasonCode', 'Reason Code', null, 1, 'oscar', CURRENT_TIMESTAMP);

SET @lookupListId:=LAST_INSERT_ID();

INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Contraception'                 , 'Contraception'                 , 1 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Counselling'                   , 'Counselling'                   , 2 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'ECP'                           , 'ECP'                           , 3 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Follow-Up'                     , 'Follow-Up'                     , 4 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Genital Warts Treatment'       , 'Genital Warts Treatment'       , 5 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'HIV Testing'                   , 'HIV Testing'                   , 6 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Immunization'                  , 'Immunization'                  , 7 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'IUD Removal'                   , 'IUD Removal'                   , 8 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Needle Exchange'               , 'Needle Exchange'               , 9 , 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'PAP Test'                      , 'PAP Test'                      , 10, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Pregnancy Test'                , 'Pregnancy Test'                , 11, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Repeat PAP Test'               , 'Repeat PAP Test'               , 12, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Results'                       , 'Results'                       , 13, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'STI Exam'                      , 'STI Exam'                      , 14, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'STI Prescription/Treatment'    , 'STI Prescription/Treatment'    , 15, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Therapeutic Abortion Follow-Up', 'Therapeutic Abortion Follow-Up', 16, 1, 'oscar', CURRENT_TIMESTAMP);
INSERT INTO LookupListItem(lookupListId, `value`, label, displayOrder, active, createdBy, dateCreated) VALUES(@lookupListId, 'Others'						 , 'Others'						   , 99, 1, 'oscar', CURRENT_TIMESTAMP);

insert into issue (code,description,role,update_date,type,sortOrderId) values ('TicklerNote','Tickler Note', 'nurse',now(),'system', 0);

insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Hematology','Hema',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','Biochemistry','Bio',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('lab','ECG','ECG',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','Ultrasound','US',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','MRI','MRI',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','CT-SCAN','Scan',NULL);
insert into documentDescriptionTemplate (doctype,description,descriptionShortcut,provider_no) values ('radiology','X-Ray','XRay',NULL);

insert into `secObjectName` (`objectName`) values('_rx.dispense');
insert into `secObjPrivilege` values('doctor','_rx.dispense','x',0,'999998');


insert into ProductLocation (name) values ('Default');

INSERT INTO `consentType` VALUES ('1', 'integrator_patient_consent', 'Sunshiner frailty network', 'Patient Permissions for Integrator enabled sharing of: Chart notes, RXes, eforms, allergies, documents (e.g.photos) Discussed with patient (and/or their representative) and they have consented to integrator enabled sharing of their information with Sunshiners Frailty Network', '1');

insert into EncounterType (value,global) VALUES ('face to face encounter with client',1),('telephone encounter with client',1),('email encounter with client',1),('encounter without client',1),('group face to face encounter',0),('group telephone encounter',0),('group encounter with client',0),('group encounter without group',0);

INSERT INTO `tickler_category` VALUES ('1', 'To Call In', 'Call this patient in for a follow-up visit', b'1'), ('2', 'Reminder Note', 'Send a reminder note to this patient', b'1'), ('3', 'Follow-up Billing', 'Follow-up Additional Billing', b'1');

insert into `secObjectName` (`objectName`) values ('_newCasemgmt.clearTempNotes');
insert into `secObjPrivilege` values('admin','_newCasemgmt.clearTempNotes','x',0,'999998');

INSERT INTO `LookupList` (`listTitle`,`name`, description, categoryId, active, createdBy, dateCreated) VALUES('Consultation Request Appointment Instructions List', 'consultApptInst', 'Select list for the consultation appointment instruction select list', NULL, '1', 'oscar', NOW() );
INSERT INTO `LookupListItem` (lookupListId, value, label, displayOrder, active, createdBy, dateCreated)( 
SELECT id, UUID(), 'Please reply to sending facility by fax or phone with appointment','1', '1','oscar', NOW() FROM `LookupList` WHERE `name` = "consultApptInst" );

insert into scheduletemplate Values('Public','P:OnCallClinic','Weekends/Holidays','________________________________________CCCCCCCCCCCCCCCC________________________________________');

insert into scheduletemplatecode Values(null,'C','On Call Clinic','15','green','Onc',1);

insert into OscarJobType Values(null,'OSCAR ON CALL CLINIC', 'Notifies MRP if patient seen during on-call clinic','org.oscarehr.jobs.OscarOnCallClinic',false,now());

insert into OscarJob Values(null,'OSCAR On-Call Clinic',null,(select id from OscarJobType where name = 'OSCAR ON CALL CLINIC'),'0 0 4 * * *','999998',false,now());

INSERT INTO `OscarJobType` VALUES (null,'OSCAR MSG REVIEW','Sends OSCAR Messages to Residents Supervisors when charts need to be reviewed','org.oscarehr.jobs.OscarMsgReviewSender',0,now());
INSERT INTO `OscarJob` VALUES (null,'OSCAR Message Review','',(select id from OscarJobType where name = 'OSCAR MSG REVIEW') ,'0 0/30 * * * *','999998',0,now());

INSERT INTO  `secObjectName` (`objectName`) VALUES ('_caisi.documentationWarning ') ON DUPLICATE KEY UPDATE objectName='_caisi.documentationWarning ' ;

insert into `secObjectName` (`objectName`) values ('_admin.auditLogPurge');
