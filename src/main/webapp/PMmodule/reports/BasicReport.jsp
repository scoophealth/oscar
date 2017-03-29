
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
	border-right: 1px solid #333;
	background-color: #ddd;
	color: #ddd;
	border-left: 1px solid #fff;
}

.message {
	color: red;
	background-color: white;
}

.error {
	color: red;
	background-color: white;
}
</style>


<div class="tabs" id="tabs">
<table cellpadding="3" cellspacing="0" border="0">

	<tr>
		<th title="Programs">Program Statistics</th>
	</tr>
</table>
</div>

<table class="b" width="100%">
	<tr>
		<th style="color: black">Description</th>
		<th style="color: black">Value</th>
	</tr>
	<c:forEach var="stat" items="${programStatistics}">
		<tr>
			<td><c:out value="${stat.key}" /></td>
			<td><c:out value="${stat.value}" /></td>
		</tr>
	</c:forEach>
</table>
