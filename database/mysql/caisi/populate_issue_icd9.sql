--
-- Populate icd9 codes/desc into issue table
--
INSERT INTO issue (code, description, role, update_date, type)
	 SELECT icd9.icd9, icd9.description, 'doctor', now(), 'icd9' FROM icd9;
