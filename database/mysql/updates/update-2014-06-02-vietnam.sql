--
-- Add new validation "pos or neg" if not exists
--
insert into validations (name, regularExp) select * from (select "pos or neg","pos|neg|positive|negative") as tmp where not exists (select id from validations where name="pos or neg");

--
-- Update validation "No Validations" (work-around for oscar bug)
--
update validations set regularExp="" where name="No Validations";

--
-- Add new measurementTypes
--
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("OPID","Urine Test Opioids","Urine Toxicology Test: Opioids",
(select id from validations where name="pos or neg"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("MAMP","Urine Test Methamphetamine","Urine Toxicology Test: Methamphetamine",
(select id from validations where name="pos or neg"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("BZD","Urine Test Benzodiazepine","Urine Toxicology Test: Benzodiazepine",
(select id from validations where name="pos or neg"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("CANB","Urine Test Cannabis","Urine Toxicology Test: Cannabis",
(select id from validations where name="pos or neg"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("Uetc","Urine Test etc","Urine Toxicology Test: other substance - name in comment",
(select id from validations where name="pos or neg"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("RHU","Recent Heroin Use","Methadone Induction Phase Assessment: Recent Heroin Use Yes/No - Amount in comment",
(select id from validations where name="Yes/No"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("ORDU","Other Recent Drug Use","Methadone Induction Phase Assessment: Other Recent Drug Use Yes/No - Description in comment",
(select id from validations where name="Yes/No"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("MDD","Methadone Dose","Methadone Induction Phase Assessment: Methadone Dose - Dosing Date Number & Patient Comfort Level in comment",
(select id from validations where name="Numeric Value greater than or equal to 0"));
insert into measurementType (type,typeDisplayName,typeDescription,validation)
values ("GWS","General Withdrawal Symptoms","Methadone Induction Phase Assessment: General Withdrawal Symptoms - Doctor's detailed notes in comment",
(select id from validations where name="No Validations"));

