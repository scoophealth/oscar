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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.web.*"%>

<%@ include file="/taglibs.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	String submissionId = request.getParameter("submissionId");
	OcanSubmissionLog log = null;
	
	if(submissionId != null) {
		log = OcanReportUIBean.getOcanSubmissionLog(loggedInInfo.getCurrentFacility().getId(), submissionId);	
	}
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
%>

	<div class="page-header">
		<h4>OCAN IAR Detail Report</h4>
	</div>

	<h5>IAR Submission</h5>
	<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>				
			<th>Submission Id</th>
			<th>Submission Date</th>
			<th>Transaction ID</th>
			<th>Result</th>
			<th>Result Message</th>
		</tr>
	</thead>		
		<tr>
			<td><%=log.getId() %></td>
			<td><%=formatter.format(log.getSubmitDateTime())%></td>
			<td><%=log.getTransactionId()%></td>
			<td><%=log.getResult() %></td>
			<td><%=log.getResultMessage()%></td>
		</tr>			
	</table>	

	<h5>Records</h5>
	<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<td>Form Id</td>
			<td>Date Started</td>
			<td>Date Completed</td>
			<td>Client</td>
			<td>Provider</td>			
		</tr>
	</thead>
		<%for(OcanStaffForm form:log.getRecords()) { %>
			<tr>
				<td><%=form.getId()%></td>
				<td><%=formatter2.format(form.getStartDate()) %></td>
				<td><%=formatter2.format(form.getCompletionDate()) %></td>
				<td><%=form.getClientId() %></td>
				<td><%=form.getProviderName()%></td>
			</tr>
		<%} %>
	</table>
