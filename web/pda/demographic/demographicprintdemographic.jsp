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
    response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="../appointment/errorpage.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">

<script language="JavaScript">
<!--


//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<%
  int left = Integer.parseInt(request.getParameter("left"));
  int top = Integer.parseInt(request.getParameter("top"));
  int height = Integer.parseInt(request.getParameter("height"));
  int gap = Integer.parseInt(request.getParameter("gap"));
  int b1=0,b2=0,b3=0;
  if(request.getParameter("label1checkbox")!=null && request.getParameter("label1checkbox").compareTo("checked")==0) b1=Integer.parseInt(request.getParameter("label1no"));
  if(request.getParameter("label2checkbox")!=null && request.getParameter("label2checkbox").compareTo("checked")==0) b2=Integer.parseInt(request.getParameter("label2no"));
  if(request.getParameter("label3checkbox")!=null && request.getParameter("label3checkbox").compareTo("checked")==0) b3=Integer.parseInt(request.getParameter("label3no"));
  
  for (int i=0; i<b1; i++) {
%>
<div ID="blockDiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=left%>px; top:<%=top+i*(height+gap/2)%>px; width:400px; height:100px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("label1")%></td>
	</tr>
</table>
</div>

<%
  }
  for (int i=0; i<b2; i++) {
%>

<div ID="blockDiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=left%>px; top:<%=top+b1*(height+gap)+i*(height+gap/2)%>px; width:400px; height:100px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("label2")%></td>
	</tr>
</table>
</div>
<%
  }
  for (int i=0; i<b3; i++) {
%>

<div ID="blockDiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=left%>px; top:<%=top+(b1+b2)*(height+gap)+i*(height+gap/2)%>px; width:400px; height:100px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("label3")%></td>
	</tr>
</table>
</div>
<%
  }
%>
<div ID="blockDiv1"
	STYLE="position: absolute; visibility: visible; z-index: 2; left: 620px; top: 0px; width: 70px; height: 20px;">
<a href=# onClick="window.print();"><img src="../images/print.gif"
	width="16" height="16" border="0">Print</a></div>
<div ID="blockDiv1"
	STYLE="position: absolute; visibility: visible; z-index: 2; left: 620px; top: 22px; width: 70px; height: 20px;">
<a href=# onClick="history.go(-1);return false;"><img
	src="../images/previous.gif" border="0"> Back</a></div>

</body>
</html>