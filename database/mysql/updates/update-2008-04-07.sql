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

  role_name VARCHAR(30) not null,

  orgcd VARCHAR(80) default 'R0000001',

  activeyn    int(1),

  primary key (id)

);

insert into secUserRole (provider_no, role_name, orgcd, activeyn)

select a.provider_no,a.role_name,'R0000001',1 from secUserRole_tmp a;

drop table secUserRole_tmp;

-- add descriptions to the object list

alter table secObjectName add description varchar(60);
alter table secObjectName add orgapplicable tinyint(1);
update secObjectName set orgapplicable = 0;

--create or replace view v_user_access as
--select a.provider_no, c.codetree orgcd, b.objectName,d.orgapplicable, max(b.privilege) privilege
--from secUserRole a, secObjPrivilege b, lst_orgcd c, secObjectName d
--where a.role_name = b.roleUserGroup
--and a.orgcd = c.code and b.objectName=d.objectName
--and a.activeyn=1 and c.activeyn=1
--group by a.provider_no, c.codetree, b.objectName,d.orgapplicable
--;
