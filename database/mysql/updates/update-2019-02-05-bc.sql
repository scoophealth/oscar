ALTER TABLE professionalSpecialists ADD COLUMN cdxId VARCHAR(10);
ALTER TABLE professionalSpecialists ADD COLUMN cdxCapable BIT(1) NOT NULL DEFAULT 0;
