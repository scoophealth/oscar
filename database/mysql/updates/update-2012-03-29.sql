ALTER TABLE consultationRequests ADD COLUMN `signature_img` VARCHAR(20);
ALTER TABLE consultationRequests
ADD COLUMN letterheadName VARCHAR(6),
ADD COLUMN letterheadAddress TEXT,
ADD COLUMN letterheadPhone VARCHAR(50),
ADD COLUMN letterheadFax VARCHAR(50);
ALTER TABLE consultationRequests MODIFY letterheadName varchar(20);