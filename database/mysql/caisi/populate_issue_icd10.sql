--
-- Populate icd10 codes/desc into issue table
--
INSERT INTO issue (code, description, role, update_date,type)
	 SELECT icd10.icd10, icd10.description, 'doctor', now(), 'icd10' FROM icd10;

