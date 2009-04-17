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
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Single Prescribe</title>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}
//-->
</script>
</head>
<body onload="start()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		A PRESCRIBE RECORD</font></th>
	</tr>
</table>
</center>
<%
   List<Map> resultList = oscarSuperManager.find("providerDao", request.getParameter("dboperation"), new Object[] {request.getParameter("prescribe_no")});
   Map prescribe = resultList.get(0);
%>
<font size="-1"><%=prescribe.get("prescribe_date")%> <%=prescribe.get("prescribe_time")%></font>
<br>
<hr>
<xml id="xml_list">
<encounter>
<xml_content>
<%=prescribe.get("content")%>
</xml_content>
</encounter>
</xml>

<p><!-- open and read from database--> <%
  //System.out.println("kkkkkkkkkkkkkkkkkkkkk"+request.getParameter("template"));
  out.println("<table datasrc='#xml_list' border='0'><tr><td><font color='blue'>Content:</font></td></tr><tr><td><div datafld='xml_content'></td></tr></table>");
%>

<center>
<form><input type="button" value="Close this window"
	onClick="self.close()"></form>
</center>
</body>
</html>