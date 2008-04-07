create or replace view v_user_access as
select a.provider_no, c.codetree orgcd, b.objectname,d.orgapplicable, max(b.privilege) privilege
from secuserrole a, secobjprivilege b, lst_orgcd c, secobjectname d
where a.role_name = b.roleusergroup
and a.orgcd = c.code and b.objectname=d.objectname
and a.activeyn=1 and c.activeyn=1
group by a.provider_no, c.codetree, b.objectname,d.orgapplicable
