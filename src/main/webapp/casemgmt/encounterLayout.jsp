
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
<%@ page errorPage="/casemgmt/error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
	long loadPage = System.currentTimeMillis();
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<c:set var="ctx" value="${pageContext.request.contextPath}"
	scope="request" />
<link rel="stylesheet" href="<c:out value="${ctx}"/>/css/casemgmt.css"
	type="text/css">
<link rel="stylesheet" type="text/css"
	href="<c:out value="${ctx}"/>/css/print.css" media="print" />
<html:base />
<title>Case Management</title>
<%! String refresh = oscar.OscarProperties.getInstance().getProperty("refresh.encounterLayout.jsp", "-1"); %>
<%="-1".equals(refresh)?"":"<meta http-equiv=\"refresh\" content=\""+refresh+";\">"%>
</head>
<body>
<table border="0" width="100%" cellspacing="5">
	<tbody>
		<tr>
			<td width="22%" valign="top"><tiles:insert attribute="navigation" /></td>
			<td width="78%" valign="top"><tiles:insert attribute="body" /></td>
		</tr>
	</tbody>
</table>
</body>
</html:html>
