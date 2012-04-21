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
<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script></head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.providerdelete.title" /></font></th>
	</tr>
</table>
<%
  //if action is good, then congratulations
  int rowsAffected = apptMainBean.queryExecuteUpdate(request.getParameter("keyword"), request.getParameter("dboperation"));
  if (rowsAffected ==1) {
	LogAction.addLog((String)session.getAttribute("user"), LogConst.DELETE, "adminDeleteUser",
			request.getParameter("keyword"), request.getRemoteAddr());
%>
<p>
<h2><bean:message key="admin.providerdelete.msgDeletionSuccess" />:
<%= request.getParameter("keyword") %>.</h2>
<%  
  } else {
%>
<h1><bean:message key="admin.providerdelete.msgDeletionFailure" />:
<%= request.getParameter("keyword") %>. <%  
  }
%>
<p></p>
<%@ include file="footerhtm.jsp"%>
</center>
</body>
</html:html>
