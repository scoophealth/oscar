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
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Provider: the following records</title>
</head>
<link rel="stylesheet" href="../web.css" />
<body background="../images/gray_bg.jpg" bgproperties="fixed">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		PROVIDERS</font></th>
	</tr>
</table>
<table border="0" cellspacing="1" cellpadding="2">
<%
  //if action is good, then give me the result
	  String[] param = new String[13];
	  param[0]=request.getParameter("last_name");
	  param[1]=request.getParameter("first_name");
	  param[2]=request.getParameter("specialty");
	  param[3]=request.getParameter("team");
	  param[4]=request.getParameter("sex");
	  param[5]=request.getParameter("dob");
	  param[6]=request.getParameter("address");
	  param[7]=request.getParameter("phone");
	  param[8]=request.getParameter("ohip_no");
	  param[9]=request.getParameter("rma_no");
	  param[10]=request.getParameter("hso_no");
	  param[11]=request.getParameter("status");
	  param[12]=request.getParameter("provider_no");

	  oscarSuperManager.update("providerDao", request.getParameter("dboperation"), param);
	  List<Map> resultList = oscarSuperManager.find("providerDao", "search_detail", new Object[] {request.getParameter("provider_no")});
	  for (Map provider : resultList) {
	  // the cursor only goes through once from top
%>
	<tr bgcolor="#66FF66">
		<td><b>provider_no:</b></td>
		<td colspan="3"><span style="color: black"><%=provider.get("provider_no") %></span></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>last_name:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("last_name") %></td>
		<td bgcolor="#C4D9E7"><b>first_name:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("first_name") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>specialty:</b></td>
		<td bgcolor="#C4D9E7"><span style="color: blue"><%=provider.get("specialty") %></span></td>
		<td bgcolor="#C4D9E7"><b>team:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("team") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>sex:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("sex") %></td>
		<td bgcolor="#C4D9E7"><b>dob:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("dob") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>address:</b></td>
		<td colspan="3" bgcolor="#C4D9E7"><%=provider.get("address") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>phone:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("phone") %></td>
		<td bgcolor="#C4D9E7"><b>ohip_no:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("ohip_no") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>rma_no:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("rma_no") %></td>
		<td bgcolor="#C4D9E7"><b>hso_no:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("hso_no") %></td>
	</tr>
	<tr>
		<td bgcolor="#C4D9E7"><b>status:</b></td>
		<td bgcolor="#C4D9E7"><%=provider.get("status") %></td>
		<td bgcolor="#C4D9E7"></td>
		<td bgcolor="#C4D9E7"></td>
	</tr>
<%
	  }
%>
</table>
<p></p>
<%@ include file="footer.htm"%></center>
</body>
</html>
