-- Create table

create table lst_orgcd

(
  code         VARCHAR(8) not null,
  description  VARCHAR(240),
  activeyn     VARCHAR(1),
  orderbyindex int,
  codetree      VARCHAR(80),
  primary key (code) 
);

create index IDX_ORGCD_CODE on lst_orgcd (codetree);

-- add active yn flag in the role list

alter table secRole add column description varchar(60);

-- adding 3 new columns id,orgcd, activeyn to the secuserrole table,

-- id is added to allow one user multiple roles/org combinations

 

create table secUserRole_tmp as select * from secUserRole;

drop table secUserRole;

create table secUserRole(

  id  int(10) not null auto_increment,  

  provider_no VARCHAR(6) not null,

  role_no   VARCHAR(30) not null,

  orgcd       VARCHAR(80),

  activeyn    int(1),

  primary key (id)

);

insert into secUserRole (provider_no, role_no, orgcd, activeyn)

select a.provider_no,b.role_no,'R0000001',1 from secUserRole_tmp a, secRole b where a.role_no=b.role_name;

drop table secUserRole_tmp;

-- add descriptions to the object list

alter table secObjectName add description varchar(60);