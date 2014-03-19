/* Sharing Center Tables */
CREATE TABLE IF NOT EXISTS `sharing_affinity_domain` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `oid` VARCHAR(255),
    `permission` VARCHAR(10),
    `name` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `sharing_actor` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `oid` VARCHAR(255),
    `name` VARCHAR(255),
    `actor_type` VARCHAR(255),
    `secure` TINYINT(1),
    `endpoint` VARCHAR(255),
    `id_facility_name` VARCHAR(255),
    `id_application_id` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_policy_definition` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `display_name` VARCHAR(255),
    `code` VARCHAR(255),
    `code_system` VARCHAR(255),
    `policy_doc_url` VARCHAR(255),
    `ack_duration` DOUBLE,
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_pd_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_acl_definition` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `role` VARCHAR(255),
    `permission` VARCHAR(255),
    `action_outcome` VARCHAR(255),
    `policy_fk` INT,
    CONSTRAINT `fk_sharing_policy` FOREIGN KEY (`policy_fk`) REFERENCES `sharing_policy_definition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_value_set` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `value_set_id` VARCHAR(255),
    `description` VARCHAR(255),
    `attribute` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_vs_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_code_mapping` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `attribute` VARCHAR(255),
    `description` VARCHAR(255),
    `domain_fk` INT,
    CONSTRAINT `fk_sharing_cm_domain` FOREIGN KEY (`domain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_code_value` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `code_system` VARCHAR(255),
    `code` VARCHAR(255),
    `display_name` VARCHAR(255),
    `code_system_name` VARCHAR(255),
    `mapping_fk` INT,
    CONSTRAINT `fk_sharing_mapping` FOREIGN KEY (`mapping_fk`) REFERENCES `sharing_code_mapping` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_clinic_info` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `org_oid` VARCHAR(255),
    `name` VARCHAR(255),
    `application_name` VARCHAR(255),
    `facility_name` VARCHAR(255),
    `universal_id` VARCHAR(255),
    `namespace` VARCHAR(255),
    `source_id` VARCHAR(255)
);
# Ensure there is a record in the sharing_clinic_info table
INSERT INTO sharing_clinic_info (org_oid, name, application_name, facility_name, universal_id, namespace, source_id)
SELECT * FROM (SELECT '0.1.2.3.4', 'Clinic Name', 'OSCAR_App', 'OSCAR_Facility', '1.0.1', 'OSCAR', '1.0.2') AS temp_set
WHERE NOT EXISTS (SELECT * FROM sharing_clinic_info);

CREATE TABLE IF NOT EXISTS `sharing_infrastructure` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `alias` VARCHAR(255),
    `common_name` VARCHAR(255),
    `organizational_unit` VARCHAR(255),
    `organization` VARCHAR(255),
    `locality` VARCHAR(255),
    `state` VARCHAR(255),
    `country` VARCHAR(255),
    `public_key` TEXT,
    `private_key` TEXT
);

CREATE TABLE IF NOT EXISTS `sharing_patient_document` (
  `patientDocumentId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `demographic_no` INT,
  `uniqueDocumentId` VARCHAR(255),
  `repositoryUniqueId` VARCHAR(45),
  `creationTime` DATETIME,
  `isDownloaded` TINYINT(1),
  `affinityDomain` VARCHAR(255),
  `title` VARCHAR(255),
  `mimetype` VARCHAR(255),
  `author` VARCHAR(255),
  `affinityDomain_fk` INT,
  CONSTRAINT `fk_sharing_pat_doc_affinityDomain` FOREIGN KEY (`affinityDomain_fk`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_patient_network` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `demographic_no` INT,
    `affinity_domain` INT,
    `sharing_enabled` TINYINT(1),
    `sharing_key` VARCHAR(255),
    `date_enabled` Date,
    CONSTRAINT `fk_sharing_pat_netwk_affinity_domain` FOREIGN KEY (`affinity_domain`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_mapping_code` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `attribute` VARCHAR(255),
    `code` VARCHAR(255),
    `code_system` VARCHAR(255),
    `code_system_name` VARCHAR(255),
    `display_name` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS `sharing_mapping_site` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `facility_type_code` INT,
    `practice_setting_code` INT,
    CONSTRAINT `fk_sharing_mapping_site_facility_type_code` FOREIGN KEY (`facility_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_site_practice_setting_code` FOREIGN KEY (`practice_setting_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_mapping_edoc` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `doc_type` VARCHAR(255) NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_edoc_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_edoc_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_mapping_eform` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `eform_id` INT NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_eform_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_eform_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_mapping_misc` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `affinity_domain` INT NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `source` VARCHAR(255) NOT NULL,
    `class_code` INT,
    `type_code` INT,
    `format_code` INT,
    `content_type_code` INT,
    `event_code_list` INT,
    `folder_code_list` INT,
    CONSTRAINT `fk_sharing_mapping_misc_class_code` FOREIGN KEY (`class_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_type_code` FOREIGN KEY (`type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_format_code` FOREIGN KEY (`format_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_content_type_code` FOREIGN KEY (`content_type_code`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_event_code_list` FOREIGN KEY (`event_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT `fk_sharing_mapping_misc_folder_code_list` FOREIGN KEY (`folder_code_list`) REFERENCES `sharing_mapping_code` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_document_export` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `document_type` VARCHAR(10) NOT NULL,
    `document` BLOB NOT NULL,
    `demographic_no` INT NOT NUlL,
    CONSTRAINT `fk_sharing_document_export_demographic_no` FOREIGN KEY (`demographic_no`) REFERENCES `demographic` (`demographic_no`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `sharing_patient_policy_consent` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `demographic_no` INT NOT NULL,
  `affinity_domain_id` INT NOT NULL,
  `consent_date` DATETIME NOT NULL,
  `policy_id` INT NOT NULL,
  CONSTRAINT `fk_sharing_consent_policy_definition` FOREIGN KEY (`policy_id`) REFERENCES `sharing_policy_definition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_sharing_consent_affinity_domain` FOREIGN KEY (`affinity_domain_id`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `sharing_exported_doc` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `affinity_domain` INT NOT NULL,
  `demographic_no` INT NOT NULL,
  `local_doc_id` INT,
  `document_type` VARCHAR(10) NOT NULL,
  `document_uid` VARCHAR(255) NOT NULL,
  `document_uuid` VARCHAR(255) NOT NULL,
  `date_exported` DATETIME NOT NULL,
  CONSTRAINT `fk_sharing_exported_doc_domain` FOREIGN KEY (`affinity_domain`) REFERENCES `sharing_affinity_domain` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
);