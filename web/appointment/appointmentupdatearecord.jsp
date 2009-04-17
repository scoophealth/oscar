<%@ page import="java.sql.*, java.util.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
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
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="appointment.appointmentupdatearecord.msgMainLabel" /></font></th>
	</tr>
</table>
<%
  String creator = (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");
  String createdatetime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss");

  int rowsAffected = 0;
  if (request.getParameter("buttoncancel")!=null && (request.getParameter("buttoncancel").equals("Cancel Appt") || request.getParameter("buttoncancel").equals("No Show"))) {
	  String[] param = new String[4];
	  param[0]=request.getParameter("buttoncancel").equals("Cancel Appt")?"C":"N";
	  param[1]=creator;  //request.getParameter("creator");
	  param[2]=createdatetime;
	  param[3]=request.getParameter("appointment_no");
	  rowsAffected = oscarSuperManager.update("appointmentDao", "updatestatusc", param);

  } else {
	  String[] param = new String[17];
	  if (request.getParameter("demographic_no")!=null && !(request.getParameter("demographic_no").equals(""))) {
		  param[0] = request.getParameter("demographic_no");
	  } else param[0]="0";
	  param[1]=request.getParameter("appointment_date");
	  param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	  param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	  param[4]=request.getParameter("keyword");
	  param[5]=request.getParameter("notes");
	  param[6]=request.getParameter("reason");
	  param[7]=request.getParameter("location");
	  param[8]=request.getParameter("resources");
	  param[9]=request.getParameter("type");
	  param[10]=request.getParameter("style");
	  param[11]=request.getParameter("billing");
	  param[12]=request.getParameter("status");
	  param[13]=createdatetime;  //request.getParameter("createdatetime");
	  param[14]=creator;  //request.getParameter("creator");
	  param[15]=request.getParameter("remarks");
	  param[16]=request.getParameter("appointment_no");
	  rowsAffected = oscarSuperManager.update("appointmentDao", request.getParameter("dboperation"), param);
  }
  if (rowsAffected == 1) {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateSuccess" /></h1>
</p>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script> <%  
  } else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentupdatearecord.msgUpdateFailure" /></h1>
</p>
<%  
  }
%>
<p></p>
<hr width="90%"/>
<form>
<input type="button" value="<bean:message key="global.btnClose"/>" onClick="closeit()">
</form>
</center>
</body>
</html:html>
