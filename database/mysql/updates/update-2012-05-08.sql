ALTER TABLE professionalSpecialists ADD COLUMN referralNo VARCHAR(6);
INSERT INTO professionalSpecialists (specId, fName, lName, phone, fax, specType, lastUpdated, address, referralNo)
(SELECT billingreferral_no+900000 AS specId, first_name AS fName, last_name AS lName, phone AS phone, fax AS fax, specialty AS specType, NOW() AS lastUpdated,
CONCAT(
IFNULL(CONCAT(address1, "\n"), ""),
IFNULL(CONCAT(address2, "\n"), ""),
IFNULL(CONCAT(city, " "), ""),
IFNULL(province, ""),
IFNULL(CONCAT("\n", postal), ""),
IFNULL(CONCAT("\n", country), "")
) AS address,
referral_no AS referralNo
 FROM billingreferral);

 UPDATE EyeformConsultationReport SET referralId=(referralId+900000);
