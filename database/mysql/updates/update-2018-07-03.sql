INSERT INTO `consentType` VALUES (\N,'dhir_ispa_consent','DHIR ISPA Vaccines','Patient consents to submitting immunization data covered by ISPA to DHIR',1);
update consentType set name='DHIR non-ISPA Vaccines',description='Patient consents to submitting immunization data not covered by ISPA to DHIR' where type='dhir_non_ispa_consent';

alter table DHIRSubmissionLog add clientRequestId varchar(100);
alter table DHIRSubmissionLog add clientResponseId varchar(100);
