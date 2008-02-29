
CREATE OR REPLACE VIEW V_LOOKUP_TABLE AS
select 'BTY' prefix, to_char(BED_TYPE_ID) code, NAME description, '' parentcode,1 active,rownum lineID
    from BED_TYPE
union
select 'BED' prefix, to_char(BED_ID) code, NAME description, to_char(ROOM_ID) parentcode, active,rownum lineID
    from BED
union
select 'ORG' prefix, to_char(id), description,  null parentcode,1, rownum lineID
    from V_LK_ORG
union
select 'ROL', role_name,role_name,  null parentcode, 1,rownum
       from SECROLE
union
select 'ROC', to_char(role_id), name,  null parentcode,1,rownum
       from CAISI_ROLE
union
select 'QGV',QGVIEWCODE,DESCRIPTION,GROUPCODE,1,rownum
       FROM REPORT_QGVIEWSUMMARY
union
SELECT 'RPG',to_char(ID),DESCRIPTION,null,1,orderbyindex
       FROM REPORT_LK_REPORTGROUP
union
SELECT 'LKT',TABLEID,DESCRIPTION,to_char(MODULEID),ACTIVEYN,rownum
       FROM APP_LOOKUPTABLE
union
SELECT 'MOD',to_char(module_Id),description, null, 1,rownum
from APP_MODULE
union
SELECT 'ISS',to_char(ISSUE_ID),DESCRIPTION,ROLE,1,rowNum
FROM ISSUE
Union
SELECT 'ISG',to_char(id),Name,null,1, rownum
from ISSUEGROUP
/
