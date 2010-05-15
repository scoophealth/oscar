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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.sql.*, java.util.*,oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Provider: the following records</title>
</head>
<link rel="stylesheet" href="../web.css" />
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		PROVIDERS</font></th>
	</tr>
</table>
<%
  //if action is good, then give me the result
    String[] param =new String[16];
	  param[0]=request.getParameter("last_name");
	  param[1]=request.getParameter("first_name");
	  param[2]=request.getParameter("provider_type");
	  param[3]=request.getParameter("specialty");
	  param[4]=request.getParameter("team");
	  param[5]=request.getParameter("sex");
	  param[6]=request.getParameter("dob");
	  param[7]=request.getParameter("address");
	  param[8]=request.getParameter("phone");
	  param[9]=request.getParameter("ohip_no");
	  param[10]=request.getParameter("rma_no");
	  param[11]=request.getParameter("billing_no");
	  param[12]=request.getParameter("hso_no");
	  param[13]=request.getParameter("status");
	  param[14]=SxmlMisc.createXmlDataString(request,"xml_p");
	  param[15]=request.getParameter("provider_no");
  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation"));
  if (rowsAffected ==1) {
%>
<p>
<h2>Update a Provider Record Successfully ! <a
	href="admincontrol.jsp?keyword=<%=request.getParameter("provider_no")%>&displaymode=Provider_Update&dboperation=provider_search_detail"><%= request.getParameter("provider_no") %></a>
</h2>
<%  
  } else {
%>
<h1>Sorry, fail to update !!! <%= request.getParameter("provider_no") %>.
<%  
  }
  apptMainBean.closePstmtConn(); 
%>
<p></p>
<%@ include file="footer2.htm"%>
</center>
</body>
</html>
