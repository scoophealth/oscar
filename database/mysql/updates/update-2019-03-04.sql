ALTER TABLE drugs modify route varchar(50) default 'PO';
ALTER TABLE drugs modify dispense_interval varchar(100);
ALTER TABLE drugs MODIFY past_med boolean;
ALTER TABLE drugs MODIFY long_term boolean;
ALTER TABLE drugs MODIFY patient_compliance boolean;
ALTER TABLE drugs add protocol varchar(255);
ALTER TABLE drugs add priorRxProtocol varchar(255);
ALTER TABLE drugs ADD COLUMN pharmacyId int(11);
