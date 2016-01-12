ALTER TABLE scratch_pad ADD COLUMN status tinyint(1);
UPDATE scratch_pad set status=1;
