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
	errorPage="../errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
<!--
function start(){
  this.focus();
}
//-->
</script>
</head>

<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD MYGROUP RECORDS</font></th>
	</tr>
</table>
<%
  int rowsAffected=0, datano=0;
  String[] param = new String[4];
  param[0] = request.getParameter("mygroup_no");

	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	  StringBuffer strbuf = new StringBuffer(e.nextElement().toString());
	  if (strbuf.toString().indexOf("data")==-1) continue;
	  datano = Integer.parseInt(request.getParameter(strbuf.toString()));
	  param[1] = request.getParameter("provider_no" + datano);
	  param[2] = request.getParameter("last_name" + datano);
	  param[3] = request.getParameter("first_name" + datano);
      rowsAffected = oscarSuperManager.update("receptionistDao", request.getParameter("dboperation"), param);
  }

  if (rowsAffected == 1) {
%>
<p>
<h1>Successful Addition of a Group Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
</script>
<%
  } else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>