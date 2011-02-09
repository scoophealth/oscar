# Adds two missing columns for the BC NewBorn form
# Only need if you have this form loaded.


alter table formBCNewBorn add column pg1_bloodRh varchar(6);
alter table formBCNewBorn add column pg1_delRomDonorMilk  tinyint(1);

