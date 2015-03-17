
-- ----------------------------
--  Table structure for `Icd9Synonym`
-- ----------------------------
CREATE TABLE `Icd9Synonym` (
  `dxCode` varchar(10) NOT NULL,
  `patientFriendly` varchar(250) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);



INSERT INTO `Icd9Synonym` VALUES ('172', 'Skin Cancer', '1'), ('173', 'basal cell carcinoma', '2'), ('2429', 'Hyperthyroid', '3'), ('2449', 'Hypothyroid', '4'), ('2564', 'polycystic ovarian synd
rome', '5'), ('2720', 'Hypercholesterolemia', '6'), ('2722', 'Mixed hyperlipidemia', '7'), ('2724', 'Cholesterol', '8'), ('274', 'Gout', '9'), ('2768', 'hypokalemia', '10'), ('2778', 'Retinitis pigment
osa', '11'), ('2901', 'Dementia', '12'), ('2963', 'Depression/Mood', '13'), ('2967', 'Bipolar', '14'), ('3000', 'Anxiety', '15'), ('3003', 'OCD', '16'), ('30981', 'PTSD', '17'), ('3339', 'Restless leg 
syndrome', '18'), ('3540', 'carpal tunnel syndrome', '19'), ('356', 'Neuropathy/Neuropathic pain', '20'), ('401', 'Hypertension', '21'), ('4140', 'CAD', '22'), ('4273', 'Atrial Fibrilation', '23'), ('4
53', 'Deep vein thrombosis', '24'), ('4781', 'Nasal congestion', '25'), ('4912', 'COPD', '26'), ('530', 'Barret\'s esophagus', '27'), ('53081', 'GERD/Reflux', '28'), ('555', 'Cholitis/Crohn\'s', '29'),
 ('5718', 'Fatty liver', '30'), ('59651', 'Overactive bladder', '31'), ('600', 'Enlarged prostate', '32'), ('607', 'ED/Libido', '33'), ('627', 'Menopause', '34'), ('6929', 'Dermatitis/Eczema', '35'), (
'6960', 'Psoriatic arthritis', '36'), ('715', 'Arthritis/Osteoarthritis', '37'), ('722', 'degenerative disc disorder', '38'), ('7245', 'Back Pain', '39'), ('72885', 'Muscle Spasms', '40'), ('7291', 'Fi
bromyalgia', '41'), ('73390', 'osteopenia', '42'), ('7506', 'Hiatis Hernia', '43'), ('7804', 'Dizziness', '44'), ('7805', 'sleep', '45'), ('78051', 'Sleep apnea', '46'), ('78052', 'insomnia', '47'), ('
78605', 'Difficulty breathing', '48'), ('7865', 'Chest pain', '49'), ('78841', 'Frequent Urination', '50'), ('8470', 'whiplash', '51'), ('O54', 'Herpes', '52'), ('V433', 'Aortic valve replacement', '53
'), ('V450', 'Cardiac pace maker', '54');
