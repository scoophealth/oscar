<!DOCTYPE html>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.ISO36612Dao"%>
<%@ page import="java.util.*,oscar.oscarReport.data.*, java.util.Properties, oscar.oscarBilling.ca.on.administration.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">

<%
	ISO36612Dao iso = SpringUtils.getBean(ISO36612Dao.class);
	boolean result = iso.reloadTable();

%>

<script type="text/javascript">
</script>

<head>
<title><bean:message key="admin.admin.manageGSTControl"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>
<body onload="loadData()">

<h3>ISO 3661-2 Reload Manager</h3>

<%if(result) { %>
<p>Reloaded your ISO3661-2 (Province/Country codes) into DB</p>

<% } else { %>
<p>Error reloading your ISO3661-2 (Province/Country codes) into DB. Please check logs</p>

<% } %>

</body>
</html:html>
