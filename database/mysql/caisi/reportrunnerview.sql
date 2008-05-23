-- Views for quatro report runner:
CREATE OR REPLACE VIEW v_lk_name AS
SELECT 0 grandParentID, 0 parentID, bed_type_id id, name description
FROM bed_type a;


CREATE OR REPLACE VIEW v_rep_bedlog AS
SELECT
a.client_id client_id,
  a.admission_date admission_date,
  a.discharge_date discharge_date,
  p.name program_name,
  p.descr program_description,
  b.bed_id bed_id,
  b.name bed_name,
  r.room_id room_id,
  r.name room_name,
  pc.name client_prog_st_name,
  d.last_name last_name,
  d.first_name first_name
FROM
	admission a left join program_clientstatus pc on a.program_id=pc.program_id  
	join program p on a.program_id= p.program_id 	
	join demographic d on a.client_id=d.demographic_no 
	join (room r join bed b on r.room_id=b.room_id) on a.program_id=r.program_id 
;


CREATE OR REPLACE VIEW v_user_report AS
SELECT a.reportno, a.title, a.description, a.orgapplicable, a.reporttype, a.dateoption, a.datepart,
       a.reportgroup,d.id reportgroupid, a.tablename, a.notes, c.provider_no,
       MAX(b.access_type) access_type, d.description reportgroupdesc, a.updatedby, a.updateddate
FROM  report a left join report_lk_reportgroup d on a.reportgroup=d.id 
		join (report_role b join secUserRole c on b.rolecode = c.role_name) on a.reportno = b.reportno 
GROUP BY a.reportno, a.title, a.description, a.orgapplicable, a.reporttype, a.dateoption, a.datepart, 
	a.reportgroup,d.id, a.tablename,a.notes, c.provider_no, d.description, a.updatedby, a.updateddate
;


create or replace view v_user_access as
select a.provider_no, c.codetree orgcd, b.objectName,d.orgapplicable, max(b.privilege) privilege
from secUserRole a, secObjPrivilege b, lst_orgcd c, secObjectName d
where a.role_name = b.roleUserGroup
and a.orgcd = c.code and b.objectName=d.objectName
and a.activeyn=1 and c.activeyn=1
group by a.provider_no, c.codetree, b.objectName,d.orgapplicable
;