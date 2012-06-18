alter table formONAREnhanced add pg1_GeneticD1 tinyint(1) default NULL;
alter table formONAREnhanced add pg1_GeneticD2 tinyint(1) default NULL;
alter table formONAREnhanced change pg1_geneticD pg1_geneticD varchar(20) default NULL;

alter table Episode add notes text;

