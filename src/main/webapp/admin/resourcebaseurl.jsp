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

<%

%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%
	UserPropertyDAO propertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
%>

<%
  if(request.getParameter("submit_form")!=null && request.getParameter("submit_form").equals(" Save ") ) {

	  UserProperty up = propertyDao.getProp("resource_baseurl");
	  if(up != null) {
		  propertyDao.delete(up);
	  }
	  up = propertyDao.getProp("resource");
	  if(up != null) {
		  propertyDao.delete(up);
	  }

	  propertyDao.saveProp("resource_baseurl", request.getParameter("resource_baseurl"));
	  propertyDao.saveProp("resource_baseurl", request.getParameter("resource"));
    out.println("<script language=\"JavaScript\"><!--");
    out.println("self.close();");
    out.println("//--></SCRIPT>");
  }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.resourcebaseurl.title" /></title>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.baseurl.resource_baseurl.focus();
  document.baseurl.resource_baseurl.select();
}
//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr BGCOLOR="#CCCCFF">
		<th><bean:message key="admin.resourcebaseurl.msgTitle" /></th>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="2">
	<form method="post" name="baseurl" action="resourcebaseurl.jsp">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td>
		<p>&nbsp;<bean:message key="admin.resourcebaseurl.formBaseUrl" /></a><br>
		&nbsp;<input type="text" name="resource_baseurl" value="" size='30'>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF"><input type="hidden"
			name="submit_form" value=' Save '> <input type="submit"
			name="confirmButton"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>">
		<input type="button" name="Cancel"
			value="<bean:message key="admin.resourcebaseurl.btnExit"/>"
			onClick="window.close()"></td>
	</tr>
	</form>
</table>

</body>
</html:html>
