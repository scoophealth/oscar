<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean" %>
<%@page import="org.oscarehr.casemgmt.web.GroupNoteAction" %>

<%
	String demographicNo = request.getParameter("demographicNo");
	String programId = (String) request.getSession().getAttribute("case_program_id");
	
	String[] ids = request.getParameterValues("group_client_id");
	
	String totalAnonymous = request.getParameter("num_anonymous");
	String frmName = "caseManagementEntryForm" + demographicNo;
	CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute(frmName);

	if(cform != null) {
		cform.setGroupNote(true);
		cform.setGroupNoteClientIds(ids);
		cform.setGroupNoteTotalAnonymous(Integer.parseInt(totalAnonymous));
		session.setAttribute(frmName,cform);		
		GroupNoteAction.saveGroupNote(cform,programId);
	}
%>
<html>
<head>
<title></title>
<script>
window.close();
</script>
</head>
<body>
Saving...
</body>
</html>