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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.List"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Program History</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>

<display:table cellspacing="2" cellpadding="9" id="ps"
	name="programSignatures" export="false" pagesize="0"
	requestURI="/PMmodule/ProgramManager.do">

	<display:column property="providerName" style="white-space: nowrap;"
		sortable="true" title="Provider Name"></display:column>
	<display:column property="caisiRoleName" style="white-space: nowrap;"
		sortable="true" title="Role"></display:column>
	<display:column property="updateDate" style="white-space: nowrap;"
		sortable="true" title="Date"></display:column>
</display:table>

</body>
</html:html>
<!--  
<input type="button" value="Back" onClick="history.go(-1)"/>
-->
</br>
<input type="button" value="Close" onClick="self.close()" />
<!--
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td>&nbsp;</td>
		<td>Provider Name</td>
		<td>Role</td>
		<td>Date</td>
	</tr>
	
	
	<c:forEach var="ps" items="${programSignatures}">
	<tr class="b">
		<td><c:out value="${ps.providerName}"/></td>
		<td><c:out value="${ps.caisiRoleName}" /></td>
		<td><c:out value="${ps.updateDate}" /></td>	
	</tr>
	</c:forEach>
</table>
-->
