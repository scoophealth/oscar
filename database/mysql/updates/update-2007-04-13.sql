#custom_instructions indicates whether prescription was written/pasted rather
#than configured with form controls

alter table drugs add column dosage text;
alter table favorites add column dosage text;
alter table drugs add column custom_instructions boolean default false;
alter table favorites add column custom_instructions boolean default false;
