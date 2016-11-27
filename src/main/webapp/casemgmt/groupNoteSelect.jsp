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

<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.dao.GroupNoteDao"%>
<%@page import="org.oscarehr.common.model.GroupNoteLink"%>
<%@page import="org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean" %>

<%
	ProgramManager programManager = (ProgramManager)SpringUtils.getBean("programManager");
	AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");
	GroupNoteDao groupNoteLinkDao = (GroupNoteDao)SpringUtils.getBean("groupNoteDao"); 
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	List<Admission> admissions = admissionManager.getCurrentAdmissionsByProgramId(request.getParameter("programId"));
	List<Demographic> demographics = demographicDao.getActiveDemographicByProgramId(Integer.parseInt((String)request.getParameter("programId")));
	
	String demographicNo = request.getParameter("demographicNo");
	
	String frmName = "caseManagementEntryForm" + demographicNo;
	CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean)session.getAttribute(frmName);
	String noteId = cform.getNoteId();
	boolean isUpdate=false;
%>
<html>

	<head>
		<title>Group Note - Select Clients</title>
	</head>
	<body>
	
	<%if(cform.getNoteId() == null) { %>
		<h4>In order to use this page, you must first save this note and then click on edit for this note then click on group icon to select client members of this group. Please try again.</h4>
		<input type="button" onclick="window.close();" value="Close Window" />
	<% } else { %>

	<%if(noteId!=null&& noteId.length()>0) { 
		List<GroupNoteLink> currentLinks = groupNoteLinkDao.findLinksByNoteId(Integer.parseInt(noteId));
		if(currentLinks.size()>0) {
			isUpdate=true;
	%>
	<h5>Current Group Links to this Note</h5>
	
	<table border="1" width="80%">
		<tr>
			<td>Name</td>
			<td>Anonymous</td>
		</tr>
		
	<%
		for(GroupNoteLink link:currentLinks) {
			Demographic demographic = demographicDao.getDemographic(String.valueOf(link.getDemographicNo()));
	%>
	<!-- current links? -->
	
		<tr>
			<td><%=demographic.getFormattedName() %></td>
			<td><%=link.isAnonymous() %></td>
		</tr>
	
	<% } %>
	</table>
	<br/><br/>
	<% } }%>
	
	<h5>Select clients for group note</h5>
	<form action="groupNoteSelectAction.jsp">
		<input type="hidden" name="demographicNo" value="<%=demographicNo%>"/>
	<table>
	
<%  
	for(Demographic demographic : demographics ) {		
	
%>
	<tr>
		<td><input type="checkbox" name="group_client_id" value="<%=demographic.getDemographicNo()%>"/></td>
		<td><%=demographic.getFormattedName()%></td>
	</tr>
<%
	}
%>
	</table>
	<br/>
	<input type="text" name="num_anonymous" value="0" size="3"/>&nbsp;
	Anonymous Clients
	
	<br/><br/>
	
	<script>
		function confirmGroupNote() {
			var update=<%=isUpdate%>;
			if(update==true) {
				return confirm('This will cause all previously associated clients to be disassociated with group note, and all previously associated anonymous clients to be set inactive');
			}
			return true;
		}
	</script>
	
	<input type="button" value="cancel" onclick="window.close();"/> &nbsp;&nbsp; <input type="submit" value="Enter note into selected clients" onclick="return confirmGroupNote();"/>
	<input type="hidden" name="programId" value="<%=request.getParameter("programId")%>"/>
	</form>
	<% } %>
	</body>

</html>
