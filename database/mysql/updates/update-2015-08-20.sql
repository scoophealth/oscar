ALTER TABLE DemographicContact ADD mrp tinyint(1);
UPDATE DemographicContact SET mrp = 0 WHERE mrp IS NULL OR mrp > "";