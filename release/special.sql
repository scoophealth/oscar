-- Special Payment codes for Primary Care Models eg FHN, FHO etc

INSERT INTO `cssStyles` (`id`, `name`, `style`, `status`) VALUES (2, 'Special Payment', 'font-weight:bold;', 'A');

-- palliative care
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='K023A';
-- prenatal
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P003A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P004A';
-- housecall
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='A901A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='A902A';
-- serious mental illness
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='Q021A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='Q020A';
-- hospital special payment codes
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='A933A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C002A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C003A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C004A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C005A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C006A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C007A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C008A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C009A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C010A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C121A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C122A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C123A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C124A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C142A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C143A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C777A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C905A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='C933A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='H001A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='E082A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='E083A';
-- OB special payment codes
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P006A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P009A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P018A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P020A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P038A';
UPDATE `billingservice` SET `displaystyle`=2 WHERE `service_code`='P041A';

