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

<%@ page import="java.sql.*, java.util.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	self.opener.refresh();
      close();
    }   
    //-->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		UPDATE AN APPOINTMENT RECORD</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
    int[] intparam=new int [1];
	  if(!(request.getParameter("demographic_no").equals(""))) intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
	  else intparam[0]=0;
  
    String[] param =new String[14];
	  param[0]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	  param[1]=request.getParameter("keyword");
	  param[2]=request.getParameter("notes");
	  param[3]=request.getParameter("reason");
	  param[4]=request.getParameter("location");
	  param[5]=request.getParameter("resources");
	  param[6]=request.getParameter("type");
	  param[7]=request.getParameter("style");
	  param[8]=request.getParameter("billing");
	  param[9]=request.getParameter("status");
	  param[10]=request.getParameter("createdatetime");
	  param[11]=request.getParameter("creator");
	  param[12]=request.getParameter("remarks");
	  param[13]=request.getParameter("appointment_no");
/*	  param[13]=request.getParameter("provider_no");
	  param[14]=request.getParameter("appointment_date");
	  param[15]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
*/
  int rowsAffected = apptMainBean.queryExecuteUpdate(intparam,param, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
<p>
<h1>Successful Update of an Appointment Record.</h1>
</p>
<script LANGUAGE="JavaScript">
     	self.opener.refresh();
      self.close();
</script> <%  
  } else {
%>
<p>
<h1>Sorry, update has failed.</h1>
</p>
<%  
  }
  apptMainBean.closePstmtConn();
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="closeit()"></form>
</center>
</body>
</html>