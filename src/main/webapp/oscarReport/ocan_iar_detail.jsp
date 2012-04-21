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

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	String submissionId = request.getParameter("submissionId");
	OcanSubmissionLog log = null;
	
	if(submissionId != null) {
		log = OcanReportUIBean.getOcanSubmissionLog(submissionId);	
	}
	
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
	
%>

<h1>OCAN IAR Detail Report</h1>

				
	<table class="borderedTableAndCells">
		<tr>
			<td colspan="2" align="center">IAR Submission</td>
		</tr>
		<tr>
			<td>Submission Id</td>
			<td><%=log.getId() %></td>
		</tr>
		
		<tr>
			<td>Submission Date</td>
			<td><%=formatter.format(log.getSubmitDateTime())%></td>
		</tr>			
		
		<tr>
			<td>Transaction ID</td>
			<td><%=log.getTransactionId()%></td>
		</tr>
		
		<tr>
			<td>Result</td>
			<td><%=log.getResult() %></td>
		</tr>
		
		<tr>
			<td>Result Message</td>
			<td><%=log.getResultMessage()%></td>
		</tr>
		
	</table>	

	<table class="borderedTableAndCells">
		<tr>
			<td colspan="5" align="center">Records</td>
		</tr>
		<tr>
			<td>Form Id</td>
			<td>Date Started</td>
			<td>Date Completed</td>
			<td>Client</td>
			<td>Provider</td>			
		</tr>
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
	<br/><br/>
	
	

<%@include file="/layouts/caisi_html_bottom.jspf"%>
