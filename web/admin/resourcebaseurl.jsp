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
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="baseurlBean" class="oscar.AppointmentMainBean" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
    {"delete_baseurl", "delete from property where name = ?"}, 

    {"add_baseurl", "insert into property (name,value) values('resource_baseurl',?)"}, 
    {"add_resource", "insert into property (name,value) values('resource',?)"}, 
  };
  String[][] responseTargets=new String[][] {  };
  baseurlBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<%
  if(request.getParameter("submit")!=null && request.getParameter("submit").equals(" Save ") ) {

	  int rowsAffected = baseurlBean.queryExecuteUpdate("resource_baseurl", "delete_baseurl");
	  int rowsAffected1 = baseurlBean.queryExecuteUpdate("resource", "delete_baseurl");

    rowsAffected = baseurlBean.queryExecuteUpdate(request.getParameter("resource_baseurl"), "add_baseurl");
    rowsAffected1 = baseurlBean.queryExecuteUpdate(request.getParameter("resource_baseurl"), "add_resource");
    out.println("<script language=\"JavaScript\"><!--");
    out.println("self.close();");
    out.println("//--></SCRIPT>");
    baseurlBean.closePstmtConn();  
  }
%>

<html>
<head>
<title>BASE URL</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.baseurl.resource_baseurl.focus();
  document.baseurl.resource_baseurl.select();
}
//-->
</script>
</head>
<body bgcolor="ivory" onLoad="setfocus()"  topmargin="0" leftmargin="0" rightmargin="0">
  <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
		<tr BGCOLOR="#CCCCFF">
   	  <th>RESOURCE BASE URL</th>
		</tr>
  </table>

  <table width="100%" border="0" cellspacing="0" cellpadding="2" >
  <form method="post" name="baseurl" action="resourcebaseurl.jsp">
    <tr> 
      <td>&nbsp;</td>
    </tr>
    <tr bgcolor="#EEEEFF">
    <td><p>
      &nbsp;Base URL: <font size='-2'>(e.g. http://oscar.mcmaster.ca)</a><br>
      &nbsp;<input type="text" name="resource_baseurl" value="" size='30'>
    </td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td align="center" bgcolor="#CCCCFF"> 
      <input type="submit" name="submit" value=" Save "><input type="button" name="Cancel" value=" Exit " onClick="window.close()">
      </td>
    </tr>
  </form>
  </table>

</body>
</html>
