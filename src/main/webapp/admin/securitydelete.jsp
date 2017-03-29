<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
     String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
        objectName="_admin,_admin.userAdmin" rights="w"
        reverse="<%=true%>">
        <%authed=false; %>
        <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Security" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.managers.SecurityManager" %>
<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script></head>
<link rel="stylesheet" href="../web.css" />
<body topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="admin.securitydelete.description" /></font></th>
	</tr>
</table>
<%
	int rowsAffected=0;
	Security s = securityManager.find(loggedInInfo, Integer.parseInt(request.getParameter("keyword")));
	if(s != null) {
		securityManager.remove(loggedInInfo, s.getId());
		rowsAffected=1;
		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.DELETE, LogConst.CON_SECURITY,
        		request.getParameter("keyword"), request.getRemoteAddr());
	}

	if (rowsAffected ==1) {
%>
<p>
<h2><bean:message key="admin.securitydelete.msgDeletionSuccess" />:
<%= request.getParameter("keyword") %>.</h2>
<%
  } else {
%>
<h1><bean:message key="admin.securitydelete.msgDeletionFailure" />:
<%= request.getParameter("keyword") %>.</h1>
<%
  }
%>
<p></p>

</center>
</body>
</html:html>
