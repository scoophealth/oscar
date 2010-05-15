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

<%@ page import="java.sql.*, java.util.*" errorPage="errorpage.jsp"%>
<jsp:useBean id="mainBean" class="oscar.ProviderMainBean"
	scope="session" />
<%
  ResultSet rs = mainBean.queryResults(request.getParameter("keyword"));
  while (rs.next()) {
    // the cursor of ResultSet only goes through once from top
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>OSCAR Project</title>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--

    function onsub() {
      // do nothing at the moment
      // check input data in the future 
    }

    //-->
    </script>
</head>

<body bgproperties="fixed">
<center>
<table cellspacing="0" cellpadding="6" width="640" border="0">
	<tr>
		<td valign=top colspan=8 bgcolor="#FFFF66"><span class="titleweb">Calendar
		Page!</span></td>
	</tr>
	<tr>
		<td colspan="8">
		<hr color="blue">
		</td>
	</tr>
	<form method="post" action="../provider/providercontrol.jsp"
		name="updatearecord" onsubmit="return onsub()">
	<tr>
		<td align="right" valign=top width="3%">
		<div align="center">Sun</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Mon</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Tue</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Wed</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Thu</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Fri</div>
		</td>
		<td valign=top width="2%">
		<div align="center">Sat</div>
		</td>
		<td valign=top width="2%" rowspan="2"></td>
	</tr>
	</form>
	<tr>
		<td align="right" valign=top width="3%">
		<div align="center"></div>
		</td>
		<td valign=top width="2%">
		<div align="center">1</div>
		</td>
		<td valign=top width="2%">
		<div align="center"></div>
		</td>
		<td valign=top width="2%">
		<div align="center"></div>
		</td>
		<td valign=top width="2%">
		<div align="center"></div>
		</td>
		<td valign=top width="2%">
		<div align="center"></div>
		</td>
		<td valign=top width="2%">
		<div align="center"></div>
		</td>
	</tr>
</table>
<%
  }
  mainBean.closePstmtConn();
%>

<hr color="blue" width="640">
<p align="center"><font size="+2"><a href="../main.jsp">Home</a>
<a href="../logout.jsp">Logout</a></font></p>
<p align="center"><font size="-2"><i>No Copyright </i></font></p>
</center>
</body>
</html>
