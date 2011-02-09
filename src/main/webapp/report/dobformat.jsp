
<%
  
%>
<%@ page
	import="java.util.*, java.sql.*,java.io.*, oscar.util.*, java.text.*, java.net.*,sun.misc.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />

<% 
  String [][] dbQueries=new String[][] { 
{"search_demographic", "select demographic_no, month_of_birth, date_of_birth from demographic order by demographic_no" }, 
{"update_demographicmonth", "update demographic set month_of_birth = ? where demographic_no = ?" }, 
{"update_demographicdate", "update demographic set date_of_birth = ? where demographic_no = ?" }, 
  };
  String[][] responseTargets=new String[][] {  };
  daySheetBean.doConfigure(dbQueries,responseTargets);
%>
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
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>SCRAMBLE SHEET</title>
</head>
<body>
busy ... busy ... busy
..................................................
<br>
<%
	ResultSet rsdemo = null ;
	int rowsAffected = 0; 
	rsdemo = daySheetBean.queryResults("search_demographic");
	while (rsdemo.next()) { 
		if (rsdemo.getString("month_of_birth")!=null && rsdemo.getString("month_of_birth").length() == 1) {
			daySheetBean.queryExecuteUpdate(new String[] { "0"+rsdemo.getString("month_of_birth"), rsdemo.getString("demographic_no")}, "update_demographicmonth");
		}

		if (rsdemo.getString("date_of_birth")!=null && rsdemo.getString("date_of_birth").length() == 1) {
			daySheetBean.queryExecuteUpdate(new String[] { "0"+rsdemo.getString("date_of_birth"), rsdemo.getString("demographic_no")}, "update_demographicdate");
		}
	}

%>

<p>
<h1>done.</h1>
</body>
</html>