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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.preferenceupdate.title" /></title>
</head>
<link rel="stylesheet" href="../web.css" />
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.preferenceupdate.description" /></font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
  String[] param=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    param =new String[9];
}else{
	param =new String[7];
}
	  param[0]=request.getParameter("provider_no");
	  param[1]=request.getParameter("start_hour");
	  param[2]=request.getParameter("end_hour");
	  param[3]=request.getParameter("every_min");
	  param[4]=request.getParameter("mygroup_no");
	  param[5]=request.getParameter("default_servicetype");
	  param[6]=request.getParameter("color_template");
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  param[7]=request.getParameter("new_tickler_warning_window");
		param[8]=request.getParameter("default_pmm");
}	  
	  int[] nparam=new int[1];
	  nparam[0]=Integer.parseInt(request.getParameter("preference_no"));
  int rowsAffected = apptMainBean.queryExecuteUpdate(param,nparam, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
<p>
<h2><bean:message key="admin.preferenceupdate.msgUpdateSuccess" /><%=request.getParameter("provider_no")%>
</h2>
<%  
  } else {
%>
<h1><bean:message key="admin.preferenceupdate.msgUpdateFailure" /><%= request.getParameter("provider_no") %>.
<%  
  } 
%>
<p></p>
<%@ include file="footer2htm.jsp"%>
</center>
</body>
</html:html>
