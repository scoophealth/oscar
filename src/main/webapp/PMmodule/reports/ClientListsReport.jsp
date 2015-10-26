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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.apache.commons.lang.time.*"%>
<%@ include file="/taglibs.jsp"%>

<div class="h4">
<h5>Client Lists Report Results</h5>
</div>
<div class="axial"><html:form
	action="/PMmodule/Reports/ClientListsReport">

	<table border="0" cellspacing="2" cellpadding="3">
		<tr>
			<th>Last name, First name</th>
			<th>Date of birth</th>
			<th>Program</th>
		</tr>
		<%
				Map<String, DemographicDao.ClientListsReportResults> reportResults=(Map<String, DemographicDao.ClientListsReportResults>)request.getAttribute("reportResults");
				for (DemographicDao.ClientListsReportResults clientListsReportResults : reportResults.values())
				{
					%>
		<tr>
			<td><%=clientListsReportResults.lastName+", "+clientListsReportResults.firstName%></td>
			<td><%=DateFormatUtils.ISO_DATE_FORMAT.format(clientListsReportResults.dateOfBirth)%></td>
			<td><%=clientListsReportResults.programName%></td>
		</tr>
		<%
				}
			%>
	</table>

	<html:submit value="go back to form" />

</html:form></div>
