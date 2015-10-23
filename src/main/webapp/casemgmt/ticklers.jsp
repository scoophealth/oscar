
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



<%@ include file="/casemgmt/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_tickler");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page
	import="java.util.Date,org.caisi.model.*,org.oscarehr.casemgmt.model.*, org.oscarehr.common.model.*,
org.oscarehr.PMmodule.model.*,org.springframework.context.*,org.springframework.web.context.support.*"%>

<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr class="title">
		<th>Provider Name</th>
		<th>Date</th>
		<th>Priority</th>
		<th>Task Assigned To</th>
		<th>Status</th>
		<th>Message</th>
	</tr>

	<tr>
		<%int index=0; 
			String bgcolor;
			String view_image;
		%>
		<c:forEach var="tickler" items="${ticklers}">
			<%
				if(index++%2!=0) {
					bgcolor="white";
					view_image="details.gif";
				} else {
					bgcolor="#EEEEFF";
					view_image="details2.gif";
				}
			%>
			<tr bgcolor="<%=bgcolor %>" align="center">
				<%
					String provider_name="";
							String assignee_name="";
							String status = "Active";
							String late_status = "b";
							
							Tickler temp = (Tickler)pageContext.getAttribute("tickler");
							if(temp != null) {
								Provider provider = temp.getProvider();
								if(provider != null) {
									provider_name = provider.getLastName() + "," + provider.getFirstName();
								}
								Provider assignee = temp.getAssignee();
								if(assignee != null) {
									assignee_name = assignee.getLastName() + "," + assignee.getFirstName();
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
				<%
					String style = "";
				    if ("a".equals(late_status)) {
				    	style="color:red;";
				    }
				%>
				<td style="<%=style%>"><%=provider_name %></td>
				<td style="<%=style%>"><fmt:formatDate
					pattern="MM/dd/yy : hh:mm a" value="${tickler.serviceDate}" /></td>
				<td style="<%=style%>"><c:out value="${tickler.priority}" /></td>
				<td style="<%=style%>"><%=assignee_name %></td>
				<td style="<%=style%>"><%=status %></td>
				<td style="<%=style%>" align="left"><c:out escapeXml="false"
					value="${tickler.message}" /></td>
			</tr>
		</c:forEach>
	</tr>
	<tr>
		<td colspan="9"><%=((java.util.List)request.getAttribute("ticklers")).size() %>
		ticklers found.</td>
	</tr>
</table>
