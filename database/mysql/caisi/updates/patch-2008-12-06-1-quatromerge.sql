alter table property
add lastUpdateDate date;

alter table Facility
add lastUpdateUser varchar(6),
add lastUpdateDate date;

create table secSite (
        siteId varchar(36) not null,
        description varchar(80),
        `active` int(1),
        `siteKey` int(10),
        `createdBy` varchar(30),
        createdDate date,
        lastAccessed date,
        primary key(siteId)
);

alter table security
add lastUpdateUser varchar(6),
add lastUpdateDate date;

alter table secRole
add isActive int(1) default 1,
add displayOrder int(10) default 0,
add lastUpdateUser varchar(6),
add lastUpdateDate date;

update secRole set isActive=1;
update secRole set displayOrder=0;

alter table secUserRole
add lastUpdateUser varchar(6),
add lastUpdateDate date;

alter table secObjectName
add note text;



alter table lst_orgcd
add fullCode varchar(80),
add lastUpdateUser varchar(6),
add lastUpdateDate date,
add codeCsv varchar(80);

create or replace view v_user_access as
select a.provider_no, c.code orgcd, c.codecsv orgcdcsv, b.objectname,d.orgapplicable, max(b.privilege) privilege from 
secUserRole a, secObjPrivilege b, lst_orgcd c, secObjectName d,secRole e where a.role_name=e.role_name and a.role_name 
= b.roleusergroup and a.orgcd = c.code and b.objectname=d.objectname and a.activeyn=1 and c.activeyn=1 and 
e.isactive=1 group by a.provider_no, c.code,c.codecsv, b.objectname,d.orgapplicable;
