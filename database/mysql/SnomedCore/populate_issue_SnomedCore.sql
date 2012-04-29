--
-- Populate SnomedCore codes/desc into issue table
--
INSERT INTO issue (code, description, role, update_date, type)
	 SELECT SnomedCore.SnomedCore, SnomedCore.description, 'doctor', now(), 'SnomedCore' FROM SnomedCore;
