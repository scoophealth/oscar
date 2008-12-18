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
  //  if (session.getAttribute("user") == null) {
  //    response.sendRedirect("../logout.jsp");
  //  }String curUser_no = (String)session.getAttribute("user");
%>
<%@ page errorPage="../errorpage.jsp"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>REPORT PHCP</title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css">
<script language="JavaScript">
        <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
//-->
        
      </script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> PHCP </font></th>
		<td><input type="button" name="Button" value="Cancel"
			onClick="window.close()"></td>
		</th>
	</tr>
</table>
<table width="20%" border="0" bgcolor="ivory" cellspacing="0"
	cellpadding="1">
	<tr bgcolor="silver">
		<th bgcolor="silver" width="10%" nowrap><b>Setting</b></th>
	</tr>
	<tr>
		<td><a href="reportonbilledvisitprovider.jsp"> Provider list
		</a></td>
	</tr>
	<tr>
		<td><a href="reportonbilleddxgrp.jsp"> Dx group list </a></td>
	</tr>
</table>
</body>
</html>
