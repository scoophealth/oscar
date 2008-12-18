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
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>CPP: the following records</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
<!--
//-->
</SCRIPT>
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		DELETE CPP RECORD</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
  int []intparam=new int[]{Integer.parseInt(request.getParameter("cpp_id"))};
  int rowsAffected = apptMainBean.queryExecuteUpdate(intparam, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
<h2>Delete a CPP Record Successfully !
<p><%= request.getParameter("cpp_id") %>
</h2>
<a
	href="../appointment/appointmentcontrol.jsp?cpp_id=<%=request.getParameter("cpp_id")%>&displaymode=delrelativecpp&dboperation=del_relative_cpp">Delete
the relative appointment records as well. </a> <%  
  } else {
%>
<h1>Sorry, fail to delete !!! <%= request.getParameter("cpp_id") %>.
<%  
  }
  apptMainBean.closePstmtConn();
%>
<p></p>
<%@ include file="footer.htm"%>
</center>
</body>
</html>
