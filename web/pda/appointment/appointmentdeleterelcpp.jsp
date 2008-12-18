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
      this.focus();
    }
    function closeit() {
    	self.opener.refresh();
      self.close();
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
		DELETE RELATIVE APPOINTMENT RECORDS</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
    String[] param =new String[1];
	  param[0]=request.getParameter("cpp_id");
	  
  int rowsAffected = apptMainBean.queryExecuteUpdate(param,request.getParameter("dboperation"));
  if (rowsAffected >=1) {
%>
<p>
<h1>Successful Deletion of relative appointment Record(s).</h1>
</p>
<script LANGUAGE="JavaScript">
     	self.opener.refresh();
      self.close();
</script> <%  
  } else {
%>
<p>
<h1>Sorry, no deletion happened.</h1>
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