--
--  unDemo.sql removes fake data from an oscar database placed there by demo.sql
--  Copyright Peter Hutten-Czapski 2012-2015 released under the GPL v2
--

SELECT @id:=demographic_no FROM demographic d WHERE  d.last_name="PATIENT" AND d.first_name ="TEST" AND d.year_of_birth ="1967" LIMIT 1;
SELECT @id2:=demographic_no FROM demographic d WHERE  d.last_name="MACDONALD" AND d.first_name ="JOHN ALEXANDER" AND d.year_of_birth ="1815" LIMIT 1;

DELETE FROM `preventions` WHERE demographic_no IN (@id, @id2);
DELETE FROM `drugs` WHERE demographic_no IN (@id, @id2);
DELETE FROM `measurements` WHERE demographicNo IN (@id, @id2);
DELETE FROM `dxresearch` WHERE demographic_no IN (@id, @id2);
DELETE FROM `drugs` WHERE demographic_no IN (@id, @id2);
DELETE FROM `admission` WHERE client_id IN (@id, @id2);
DELETE FROM `demographic` WHERE demographic_no IN (@id, @id2);
DELETE FROM `appointment` WHERE demographic_no IN (@id, @id2);
DELETE FROM `appointmentArchive` WHERE demographic_no IN (@id, @id2);
DELETE FROM `patientLabRouting` WHERE demographic_no IN (@id, @id2);
DELETE FROM `casemgmt_note` WHERE demographic_no IN (@id, @id2);
DELETE FROM `casemgmt_cpp` WHERE demographic_no IN (@id, @id2);
DELETE FROM `casemgmt_issue` WHERE demographic_no IN (@id, @id2);
DELETE FROM `eChart` WHERE demographicNo IN (@id, @id2);
DELETE FROM `client_image` WHERE demographic_no IN (@id, @id2);

DELETE FROM `hl7TextInfo` WHERE `requesting_client`="DR. NORMAN BETHUNE";
DELETE FROM `hl7TextInfo` WHERE `requesting_client`="DR. OSCARDOC";
DELETE FROM `hl7TextMessage` WHERE `lab_id`<10;
DELETE FROM `measurementsExt` WHERE `measurement_id`<17;
DELETE FROM `patientLabRouting` WHERE `lab_no`<10;
DELETE FROM `providerLabRouting` WHERE `lab_no`<10;
DELETE FROM `professionalSpecialists` WHERE `fName`="Sam" AND `lName`="Spade";
DELETE FROM `serviceSpecialists` WHERE `specId` <2;

DELETE FROM `messagelisttbl` WHERE `messagelisttbl`.`id` = 1;
DELETE FROM `messagetbl` WHERE `messagetbl`.`messageid` = 1;

