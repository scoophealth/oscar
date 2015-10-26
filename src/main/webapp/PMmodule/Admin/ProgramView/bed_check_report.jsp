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
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>Bed Check Report</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<table width="100%">
	<tr>
		<td width="50%"><input type="button" value="Print"
			onclick="window.print()"></td>
		<td width="50%" align="right"><input type="button" value="Close"
			onclick="self.close()" /></td>
	</tr>
</table>
<br />
<display:table class="simple" name="reservedBeds" uid="reservedBed">
	<display:column property="roomName" title="Room"
		style="text-align: center" />
	<display:column property="name" title="Bed" style="text-align: center" />
	<display:column property="demographicName" title="Client"
		style="text-align: center" />
	<display:column property="familyId" title="Family Id" />
	<display:column property="statusName" title="Status"
		style="text-align: center" />
	<display:column title="Late Pass" style="text-align: center">
		<c:choose>
			<c:when test="${reservedBed.latePass}">
				<input type="checkbox" checked="checked" />
			</c:when>
			<c:otherwise>
				<input type="checkbox" />
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:column property="reservationStart" title="Since"
		format="{0, date, yyyy-MM-dd}" style="text-align: center" />
	<display:column property="reservationEnd" title="Until"
		format="{0, date, yyyy-MM-dd}" style="text-align: center" />
	<display:column title="Not Present" style="text-align: center">
		<input type="checkbox" />
	</display:column>
	<display:column title="Notes" style="width: 30%;text-align: center" />
</display:table>
</body>
</html:html>
