--
-- Populate icd10 codes/desc into issue table
--
INSERT INTO issue (code, description, role, update_date)
	 SELECT icd10.icd10, icd10.description, 'doctor', now() FROM icd10;

