--
-- Populate icd9 codes/desc into issue table
--
INSERT INTO issue (code, description, role, update_date)
	 SELECT icd9.icd9, icd9.description, 'doctor', now() FROM icd9;