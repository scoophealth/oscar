--QuatroShelter 
alter table FACILITY add org_id number(10);
alter table FACILITY add sector_id number(10);
update FACILITY set org_id=0, sector_id=0; 
alter table FACILITY  modify org_id number(10) NOT NULL;
alter table FACILITY  modify sector_id number(10) NOT NULL;
