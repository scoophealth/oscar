CREATE TABLE `Consent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `demographic_no` int(10),
  `consent_type_id` int(10),
  `explicit` tinyint(1),
  `optout` tinyint(1),
  `last_entered_by` varchar(10),
  `consent_date` datetime,
  `optout_date` datetime,
  `edit_date` datetime,
  PRIMARY KEY (`id`)
);

CREATE TABLE `consentType` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `type` varchar(50),
  `name` varchar(50),
  `description` varchar(500),
  `active` tinyint(1),
  PRIMARY KEY (`id`)
);

INSERT INTO `consentType` VALUES ('1', 'integrator_patient_consent', 'Sunshiner frailty network', 'Patient Permissions for Integrator enabled sharing of: Chart notes, RXes, eforms, allergies, documents (e.g.photos) Discussed with patient (and/or their representative) and they have consented to integrator enabled sharing of their information with Sunshiners Frailty Network', '1');