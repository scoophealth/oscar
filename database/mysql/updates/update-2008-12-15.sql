alter table demographic add title varchar(10) after demographic_no;
alter table demographic add preferred_lang varchar(20) after chart_no;

alter table drugs add long_term boolean;
alter table drugs add past_med boolean;
alter table drugs add patient_compliance tinyint(1);
alter table drugs add last_refill_date date after `repeat`;
alter table drugs add drug_form varchar(50) after route;
alter table favorites add drug_form varchar(50) after route;

