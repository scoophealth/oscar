<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.preferenceaddpreference.title" /></title>
<link rel="stylesheet" href="../web.css">
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.preferenceaddpreference.description" /></font></th>
	</tr>
</table>
<%
String[] param=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    param =new String[8];
}else {
	param =new String[7];
}
	  param[0]=request.getParameter("start_hour");
	  param[1]=request.getParameter("end_hour");
	  param[2]=request.getParameter("every_min");
	  param[3]=request.getParameter("mygroup_no");
	  param[4]=request.getParameter("default_servicetype");
	  param[5]=request.getParameter("color_template");
	if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  param[6]=request.getParameter("new_tickler_warning_window");
	  param[7]=request.getParameter("provider_no");
	} else {
	  param[6]=request.getParameter("provider_no");
	}
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, "preference_addupdate_record");
  if (rowsAffected >=1) { //Successful Update of a Preference Record.
    session.setAttribute("starthour", param[0]);
    session.setAttribute("endhour", param[1]);
    session.setAttribute("everymin", param[2]);
    session.setAttribute("groupno", param[3]);
    session.setAttribute("default_servicetype", param[4]);
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    session.setAttribute("newticklerwarningwindow", param[7]);
}
%>
<h1><bean:message key="admin.preferenceaddarecord.msgSuccessful" />
</h1>
<%  
  } else {
  //now try to add the new preference record
	param[0]=request.getParameter("provider_no");
	param[1]=request.getParameter("start_hour");
	param[2]=request.getParameter("end_hour");
	param[3]=request.getParameter("every_min");
	param[4]=request.getParameter("mygroup_no");
	param[5]=request.getParameter("default_servicetype");
	param[6]=request.getParameter("color_template");
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	param[7]=request.getParameter("new_tickler_warning_window");
}
  rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  if (rowsAffected ==1) { //Successful add of a Preference Record.
    session.setAttribute("starthour", param[1]);
    session.setAttribute("endhour", param[2]);
    session.setAttribute("everymin", param[3]);
    session.setAttribute("groupno", param[4]);
    session.setAttribute("default_servicetype", param[5]);
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    session.setAttribute("newticklerwarningwindow", param[7]);
}
%>
<h1><bean:message
	key="admin.preferenceaddpreference.msgAdditionSuccess" /></h1>
<%  
    } else {
%>
<p>
<h1><bean:message
	key="admin.preferenceaddpreference.msgAdditionFailure" /></h1>
</p>
<%  
    }
  }
  apptMainBean.closePstmtConn();
%> <!-- footer -->
<hr width="100%" color="navy">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"><bean:message
			key="global.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"><bean:message
			key="global.btnLogout" /><img src="../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html:html>
