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

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
%>
<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      //this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }   
    //-->
</script>
</head>
<body onload="start()" background="../images/gray_bg.jpg"
	bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD AN APPOINTMENT RECORD</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
    String[] param =new String[16];
	  param[0]=request.getParameter("provider_no");
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
	  param[13]=request.getParameter("createdatetime");
	  param[14]=request.getParameter("creator");
	  param[15]=request.getParameter("remarks");
	  int[] intparam=new int [1];
	  if(!(request.getParameter("demographic_no").equals(""))) intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
	  else intparam[0]=0;
  int rowsAffected = apptMainBean.queryExecuteUpdate(param,intparam,request.getParameter("dboperation"));
  if (rowsAffected ==1) {
    System.out.println("***************************************** -------- ");
%>
<p>
<h1>Successful Addition of an appointment Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
     	self.opener.refresh();
</script> <%
  }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
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