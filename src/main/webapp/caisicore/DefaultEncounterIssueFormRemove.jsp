
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="java.util.Calendar"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<title>Default Issue Editor</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/validation.js"></script>
</head>

<body>
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	bgcolor="#CCCCFF">
	<tr class="subject">
		<th colspan="4">CAISI</th>
	</tr>

	<tr>
		<td class="searchTitle" colspan="4">Default Issue Editor</td>
	</tr>
</table>

<br />
<form action="<%=request.getContextPath()%>/DefaultEncounterIssue.do">
	<input type="hidden" name="method" value="remove" />
	<table width="100%" border="0" cellpadding="0" cellspacing="1"
		bgcolor="#C0C0C0">
		<tr>
			<td class="filedValue" colspan="4">Remove default issues&nbsp;</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td class="filedValue" colspan="3"><input type="text" name="issueNames"/></td>
			<td class="fieldValue" width="80%"><html:submit>Remove</html:submit>
			<input type="button" value="Cancel"
				onclick="location.href='DefaultEncounterIssue.do'" /></td>
		</tr>
		<tr>
			<td class="filedValue" colspan="4"><small><b>Tips:</b><br/>Enter default issue codes separated by commas, then click on save. The issues listed here will be assigned to each client record when an encounter is opened for that client for the first time.<small></td>
		</tr>
	</table>
</form>
</body>
</html>
