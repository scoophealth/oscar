<%  
  //operation available to the client - dboperation
  String orderby="", whereClause="", limit="", limit1="", limit2="";
  if(request.getParameter("orderby")!=null) orderby="order by "+request.getParameter("orderby");
    if(request.getParameter("whereClause")!=null) whereClause=request.getParameter("whereclause");
  if(request.getParameter("limit1")!=null) limit1=request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) {
    limit2=request.getParameter("limit2");
    limit="limit "+limit2+" offset "+limit1;
  }
  
  
  String [][] dbQueries=new String[][] {
    {"search_provider_all_dt", "select * from provider where provider_type='doctor' and provider_no like ? order by last_name"},
	{"search_provider_dt", "select * from provider where provider_type='doctor' and ohip_no != '' and provider_no like ? order by last_name"},
    {"search_demographic_details", "select * from demographic where demographic_no=?"},
     {"search_provider_details", "select * from provider where provider_no=?"},
     {"search_document_content", "select * from document where status<>'D' and document_no=?"},
    {"search_provider_name", "select * from provider where provider_no like ?"},
    {"search_visit_location", "select clinic_location_name from clinic_location where clinic_location_no=?"},
    {"save_document","insert into document (doctype, docdesc, docxml, docfilename, doccreator, updatedatetime, status) values(?,?,?,?,?,?,?)"},
     {"save_tickler","insert into tickler (demographic_no, message, status, update_date, service_date, creator, priority, task_assigned_to) values(?,?,?,?,?,?,?,?)"},
         {"update_tickler","update tickler set status=? where tickler_no=?"},
     {"search_tickler","select t.tickler_no, d.demographic_no, d.last_name,d.first_name, p.last_name as provider_last, p.first_name as provider_first, t.status,t.message,t.service_date, t.priority, p2.first_name AS assignedFirst, p2.last_name as assignedLast from tickler t LEFT JOIN provider p2 ON ( p2.provider_no=t.task_assigned_to), demographic d LEFT JOIN provider p ON ( p.provider_no=d.provider_no) where t.demographic_no=d.demographic_no and t.status=? and t.service_date >=? and t.service_date<=? and d.provider_no like ? order by t.service_date desc"},
     {"search_tickler_bydemo","select t.tickler_no, d.demographic_no,d.last_name,d.first_name, p.last_name as provider_last, p.first_name as provider_first, t.status,t.message,t.service_date, t.priority, p2.first_name AS assignedFirst, p2.last_name as assignedLast from tickler t LEFT JOIN provider p2 ON ( p2.provider_no=t.task_assigned_to), demographic d LEFT JOIN provider p ON ( p.provider_no=d.provider_no)  where t.demographic_no=d.demographic_no and t.status=? and t.service_date >=? and t.service_date<=? and t.demographic_no like ? order by t.service_date desc"},
   
    {"save_ctl_document","insert into ctl_document values(?,?,?,?)"},
    {"search_document", "select * from document where status <> 'D' and docfilename=? or doctype=? or docdesc like ?  or doccreator = ? " + orderby},
    {"match_document", "select distinct d.doccreator, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime from document d, ctl_document c where d.doctype <> 'share' and  d.status=c.status and d.status <> 'D' and c.document_no=d.document_no and c.module=? and c.module_id=? and d.doctype like ?"},
     {"share_document", "select distinct d.doccreator, d.status, d.docdesc, d.docfilename, d.doctype, d.document_no, d.updatedatetime from document d, ctl_document c where  d.status=c.status and d.status <> 'D' and c.document_no=d.document_no and c.module=?  and d.doctype='share'"},
    {"search_doctype_by_module", "select * from ctl_doctype where (status = 'A' or status='H') and module = ?"},
    {"search_doctype_by_type", "select * from ctl_doctype where (status = 'A' or status='H') and doctype = ?"},
    {"delete_document", "update document set status='D' and updatedatetime=? where document_no=?"},
 };
  
  //associate each operation with an output JSP file - displaymode
  String[][] responseTargets=new String[][] {
    {"day" , "appointmentprovideradminday.jsp"},
    {"month" , "appointmentprovideradminmonth.jsp"},
    {"addstatus" , "provideraddstatus.jsp"},
    {"updatepreference" , "providerupdatepreference.jsp"},
    {"displaymygroup" , "providerdisplaymygroup.jsp"},
    {"encounter" , "providerencounter.jsp"},
    {"prescribe" , "providerprescribe.jsp"},
    {"vary" , request.getParameter("displaymodevariable")==null?"":request.getParameter("displaymodevariable") },
    {"saveencounter" , "providersaveencounter.jsp"},
    {"savebill" , "providersavebill.jsp"},
    {"encounterhistory" , "providerencounterhistory.jsp"},
  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
