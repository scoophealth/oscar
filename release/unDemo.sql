--
--  unDemo.sql removes fake data from an oscar database placed there by demo.sql
--  Copyright Peter Hutten-Czapski 2012 released under the GPL v2
--

SELECT @id:=demographic_no FROM demographic d WHERE  d.last_name="ANDERSON" AND d.first_name ="PAMELA" LIMIT 1;

DELETE FROM `preventions` WHERE demographic_no=@id;
DELETE FROM `drugs` WHERE demographic_no=@id;
DELETE FROM `measurements` WHERE demographicNo=@id;
DELETE FROM `dxresearch` WHERE demographic_no=@id;
DELETE FROM `drugs` WHERE demographic_no=@id;
DELETE FROM `admission` WHERE client_id=@id;
DELETE FROM `demographic` WHERE demographic_no=@id;
DELETE FROM `appointment` WHERE demographic_no=@id;
DELETE FROM `appointmentArchive` WHERE demographic_no=@id;
DELETE FROM `patientLabRouting` WHERE demographic_no=@id;
DELETE FROM `casemgmt_note` WHERE demographic_no=@id;
DELETE FROM `casemgmt_cpp` WHERE demographic_no=@id;
DELETE FROM `casemgmt_issue` WHERE demographic_no=@id;
DELETE FROM `eChart` WHERE demographicNo=@id;

DELETE FROM `hl7TextInfo` WHERE `requesting_client`="DR. NORMAN BETHUNE";
DELETE FROM `hl7TextInfo` WHERE `requesting_client`="DR. OSCARDOC";
DELETE FROM `hl7TextMessage` WHERE `lab_id`<10;
DELETE FROM `measurementsExt` WHERE `measurement_id`<17;
DELETE FROM `patientLabRouting` WHERE `lab_no`<10;
DELETE FROM `providerLabRouting` WHERE `lab_no`<10;
DELETE FROM `professionalSpecialists` WHERE `fName`="Sam" AND `lName`="Spade";

DELETE FROM `messagelisttbl` WHERE `messagelisttbl`.`id` = 1;
DELETE FROM `messagetbl` WHERE `messagetbl`.`messageid` = 1;

