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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ include file="/taglibs.jsp"%>
<%@ page
	import="org.caisi.model.*, org.oscarehr.common.model.*, org.oscarehr.PMmodule.model.*,org.springframework.context.*,org.springframework.web.context.support.*"%>
<%@ page import="java.util.Date"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tasks"
	rights="r" reverse="<%=true%>">
	<%response.sendRedirect("noRights.html");%>
</security:oscarSec>

<%
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);
%>
<c:if test="${requestScope.from ne 'CaseMgmt'}">
	<html>
	<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<title>TicklerPlus</title>
	</head>
	<body>
</c:if>

<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<table width="100%" border="1" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr>

		<th>Demographic Name</th>
		<th>Provider Name</th>
		<th>Date</th>
		<th>Priority</th>
		<th>Task Assigned To</th>
		<th>Status</th>
		<th>Message</th>
	</tr>

	<tr>
		<%int index = 0;
			String bgcolor;
			String view_image;

			%>
		<c:forEach var="tickler" items="${ticklers}">
			<tr align="center">
				<%
					String demographic_name = "";
					String provider_name = "";
					String assignee_name = "";
					String status = "Active";
					String late_status = "b";			
					Tickler temp = (Tickler) pageContext.getAttribute("tickler");
					if (temp != null) {
						Demographic demographic = temp.getDemographic();
						if (demographic != null) {
							demographic_name = demographic.getLastName() + ","
									+ demographic.getFirstName();
						}
						Provider provider = temp.getProvider();
						if (provider != null) {
							provider_name = provider.getLastName() + ","
									+ provider.getFirstName();
						}
						Provider assignee = temp.getAssignee();
						if (assignee != null) {
							assignee_name = assignee.getLastName() + ","
									+ assignee.getFirstName();
						}
						status = "Active";
						if(temp.getStatus().equals(Tickler.STATUS.C))
							status="Completed";
						if(temp.getStatus().equals(Tickler.STATUS.D))
							status="Deleted";
						
						
						// add by PINE_SOFT
						// get system date
						Date sysdate = new java.util.Date();
						Date service_date = temp.getServiceDate();

						if (!sysdate.before(service_date)) {
							late_status = "a";
						}
					}
				%>

				<td><%=demographic_name%></td>
				<td><%=provider_name%></td>
				<td><c:out value="${tickler.serviceDateWeb}" /></td>
				<td><c:out value="${tickler.priority}" /></td>
				<td><%=assignee_name%></td>
				<td><%=status%></td>
				<td><c:out value="${tickler.message}" /></td>
			</tr>
		</c:forEach>
	</tr>

</table>

</body>
</html>
