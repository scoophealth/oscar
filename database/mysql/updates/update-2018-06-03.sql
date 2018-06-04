INSERT INTO `consentType` VALUES (\N,'dhir_non_ispa_consent','DHIR NON-ISPA Consent','Patient consents to submitting immunization data not converted by ISPA to DHIR',1);

INSERT INTO `OscarJobType` VALUES (\N,'CanadianVaccineCatalogueUpdater','Updates the local copy of the data','org.oscarehr.integration.born.CanadianVaccineCatalogueJob',1,now()),(\N,'BORN FHIR','','org.oscarehr.integration.born.BORNFhirJob',1,now());

INSERT INTO `OscarJob` VALUES (\N,'CanadianVaccineCatalogueUpdater','Updates the CVC data',(select id from OscarJobType where name='CanadianVaccineCatalogueUpdater'),'0 * 0 * * *','999998',1,now()),(\N,'BORN FHIR','',(select id from OscarJobType where name='BORN FHIR'),'0 * * * * *','999998',1,now());
