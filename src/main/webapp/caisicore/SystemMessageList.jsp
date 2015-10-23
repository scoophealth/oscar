
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<title>System Messages</title>
</head>

<body>
<table border="0" cellspacing="0" cellpadding="1" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="4">CAISI</th>
	</tr>
	<tr>
		<td class="searchTitle" colspan="4">System Messages</td>
	</tr>
</table>

<br />

<%@ include file="messages.jsp"%>

<br />
<table width="100%" border="0" cellpadding="0" cellspacing="1"
	bgcolor="#C0C0C0">
	<tr class="title">
		<td></td>
		<td>Creation Date</td>
		<td>Expiry Date</td>
		<td>Message</td>
	</tr>

	<c:forEach var="msg" items="${ActiveMessages}" varStatus="status">
		<%String style="color:black;"; String bgcolor="white";%>
		<c:if test="${msg.expired}">
			<%style="color:red;"; %>
		</c:if>

		<c:if test="${status.count % 2 != 0}">
			<%bgcolor="#EEEEFF";%>
		</c:if>



		<tr style="<%=style %>" bgcolor="<%=bgcolor %>">
			<td valign="middle"><a
				href="SystemMessage.do?method=edit&id=<c:out value="${msg.id}"/>"><img
				border="0" src="images/edit.jpg" /></a></td>
			<td><c:out value="${msg.formattedCreationDate}" /></td>
			<td><c:out value="${msg.formattedExpiryDate}" /></td>
			<td><c:out value="${msg.message}" /></td>
		</tr>
	</c:forEach>
</table>

<br />

<table>
	<tr>
		<td><input type="button" value="Back"
			onclick="location.href='<%=request.getContextPath()%>/admin/admin.jsp'" /></td>
		<td><input type="button" value="Create New Message"
			onclick="location.href='SystemMessage.do?method=edit'" /></td>
	</tr>
</table>

</body>
</html>
