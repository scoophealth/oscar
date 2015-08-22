--
-- Table structure for table 'contactspecialty'
--
CREATE TABLE `ContactSpecialty` (
  `id` int(11) NOT NULL,
  `specialty` varchar(50) NOT NULL,
  `description` varchar(140),
  PRIMARY KEY (`id`)
);
--
-- Insert into Specialty Table.
--
--
-- ContactSpecialty Data
--
INSERT INTO `ContactSpecialty` VALUES ('1', 'DERMATOLOGY', null), ('2', 'NEUROLOGY', null), ('3', 'PSYCHIATRY', null), ('5', 'OBSTETRICS & GYNAECOLOGY', null), ('6', 'OPHTHALMOLOGY', null), ('7', 'OTOLARYNGOLOGY', null), ('8', 'GENERAL SURGERY', null), ('9', 'NEUROSURGERY', null), ('10', 'ORTHOPAEDICS', null), ('11', 'PLASTIC SURGERY', null), ('12', 'CARDIO & THORACIC', null), ('13', 'UROLOGY', null), ('14', 'PAEDIATRICS', null), ('15', 'INTERNAL MEDICINE', null), ('16', 'RADIOLOGY', null), ('17', 'LABORATORY PROCEDURES', null), ('18', 'ANAESTHESIA', null), ('19', 'PAEDIATRIC CARDIOLOGY', null), ('20', 'PHYSICAL MEDICINE AND  REHABILITATION', null), ('21', 'PUBLIC HEALTH', null), ('22', 'PHARMACIST', null), ('23', 'OCCUPATIONAL MEDICINE', null), ('24', 'GERIATRIC MEDICINE', null), ('25', 'UNKNOWN', null), ('26', 'PROCEDURAL CARDIOLOGIST', null), ('28', 'EMERGENCY MEDICINE', null), ('29', 'MEDICAL MICROBIOLOGY', null), ('30', 'CHIROPRACTORS', null), ('31', 'NATUROPATHS', null), ('32', 'PHYSICAL THERAPISTS', null), ('33', 'NUCLEAR MEDICINE', null), ('34', 'OSTEOPATHY', null), ('35', 'ORTHOPTIC', null), ('37', 'ORAL SURGEONS', null), ('38', 'PODIATRISTS', null), ('39', 'OPTOMETRIST', null), ('40', 'DENTAL SURGEONS', null), ('41', 'ORAL MEDICINE', null), ('42', 'ORTHODONTISTS', null), ('43', 'MASSAGE PRACTITIONER', null), ('44', 'RHEUMATOLOGY', null), ('45', 'CLINICAL IMMUNIZATION AND ALLERGY', null), ('46', 'MEDICAL GENETICS', null), ('47', 'VASCULAR SURGERY', null), ('48', 'THORACIC SURGERY', null), ('49', 'FAMILY PHYSICIAN', null), ('50', 'ENDOCRINOLOGIST', null);
