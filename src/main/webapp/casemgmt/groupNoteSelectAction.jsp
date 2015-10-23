<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean" %>
<%@page import="org.oscarehr.casemgmt.web.GroupNoteAction" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_casemgmt.notes" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_casemgmt.notes");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
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
		GroupNoteAction.saveGroupNote(loggedInInfo, cform,programId);
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
