<%@ page import="java.sql.*, java.util.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
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
  if(request.getParameter("buttoncancel")!=null && ( request.getParameter("buttoncancel").equals("Cancel Appt") || request.getParameter("buttoncancel").equals("No Show")) ) {
    String[] param1 =new String[4];
	  param1[0]=request.getParameter("buttoncancel").equals("Cancel Appt")?"C":"N";
	  param1[1]=creator;  //request.getParameter("creator");
	  param1[2]=createdatetime;
	  param1[3]=request.getParameter("appointment_no");
    rowsAffected = apptMainBean.queryExecuteUpdate(param1, "updatestatusc");
  } else {
    int[] intparam=new int [1];
	  if(!(request.getParameter("demographic_no").equals(""))) intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
	  else intparam[0]=0;
  
    String[] param =new String[16];
	  param[0]=request.getParameter("appointment_date");
	  param[1]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	  param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	  param[3]=request.getParameter("keyword");
	  param[4]=request.getParameter("notes");
	  param[5]=request.getParameter("reason");
	  param[6]=request.getParameter("location");
	  param[7]=request.getParameter("resources");
	  param[8]=request.getParameter("type");
	  param[9]=request.getParameter("style");
	  param[10]=request.getParameter("billing");
	  param[11]=request.getParameter("status");
	  param[12]=createdatetime;  //request.getParameter("createdatetime");
	  param[13]=creator;  //request.getParameter("creator");
	  param[14]=request.getParameter("remarks");
	  param[15]=request.getParameter("appointment_no");
    rowsAffected = apptMainBean.queryExecuteUpdate(intparam,param, request.getParameter("dboperation"));
  }
  if (rowsAffected ==1) {
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
  apptMainBean.closePstmtConn();
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button"
	value="<bean:message key="global.btnClose"/>" onClick="closeit()">
</form>
</center>
</body>
</html:html>
