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

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<%
  if(session.getValue("user") == null && session.getValue("user").equals("admin") )  response.sendRedirect("../logout.jsp");
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
    {"provider_search_providerno", "select provider_no, first_name, last_name from provider order by ?"},
  };
  String[][] responseTargets=new String[][] {  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>Add a Login User</title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--

		function setfocus() {
		  document.searchprovider.user_name.focus();
		  document.searchprovider.user_name.select();
		}

    function onsub() {
      if(document.searchprovider.user_name.value=="" ||
		 document.searchprovider.password.value=="" 
		 ||document.searchprovider.provider_no.value=="" 
		) {
        alert("You forgot to input a keyword!");
        return false;
      } else return true;
      // do nothing at the moment
      // check input data in the future 
    }
    //-->
    </script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A LOGIN USER</font></th>
	</tr>
</table>
<table cellspacing="0" cellpadding="2" width="90%" border="0">
	<form method="post" action="admincontrol.jsp" name="searchprovider"
		onsubmit="return onsub()">
	<tr>
		<td>
		<div align="right">User Name <font color="red">:</font></div>
		</td>
		<td><input type="text" name="user_name"></td>
	</tr>
	<tr>
		<td>
		<div align="right">Password <font color="red">:</font></div>
		</td>
		<td><input type="text" name="password"></td>
	</tr>
	<tr>
		<td width="50%" align="right">Provider No.<font color="red">
		</font>:</td>
		<td><select name="provider_no">
			<option value="">---None---</option>
			<%
   ResultSet rsgroup = apptMainBean.queryResults("last_name", "provider_search_providerno");
 	 while (rsgroup.next()) { 
%>
			<option value="<%=rsgroup.getString("provider_no")%>"><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
			<%
 	 }
   apptMainBean.closePstmtConn();
 	 
%>
		</select> <!--input type="text" name="provider_no" --></td>
	</tr>
	<tr>
		<td colspan="2">
		<div align="center"><input type="hidden" name="dboperation"
			value="security_add_record"> <input type="hidden"
			name="displaymode" value="Security_Add_Record"> <input
			type="submit" name="subbutton" value="Add Record"></div>
		</td>
	</tr>
	</form>
</table>

<p></p>
<hr width="100%" color="orange">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"> Back to
		Admin Page.</a></td>
		<td align="right"><a href="../logout.jsp">Log Out <img
			src="../images/rightarrow.gif" border="0" width="25" height="20"
			align="absmiddle"></a></td>
	</tr>
</table>

</center>
</body>
</html>
