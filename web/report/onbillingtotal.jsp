
<%
	// this page is only for correcting total fields with no point
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page
	import="java.util.*, java.sql.*,java.io.*, oscar.util.*, java.text.*, java.net.*,sun.misc.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />

<%@ include file="../admin/dbconnection.jsp"%>
<% 
  String [][] dbQueries=new String[][] { 
{"search_billing", "select total, billing_no, billing_date from billing where total not like ? order by billing_no desc" }, 
{"update_billingtotal", "update billing set total = ? where billing_no = ?" }, 
  };
  daySheetBean.doConfigure(dbQueries);
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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Total - ON</title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
</head>
<body>
..................................................
<br>
<%
	ResultSet rsdemo = null ;
	int rowsAffected = 0; 
	rsdemo = daySheetBean.queryResults("%.%", "search_billing");
	while (rsdemo.next()) { 
		if (rsdemo.getString("total")!=null ) {
			String billingNo = rsdemo.getString("billing_no");
			String billingDate = rsdemo.getString("billing_date");
			String total = rsdemo.getString("total");
			daySheetBean.queryExecuteUpdate(new String[] { new java.math.BigDecimal(total).movePointLeft(2).toString(), billingNo}, "update_billingtotal");
			out.println( billingNo + " | " + billingDate+ " | " + total + " -> " + new java.math.BigDecimal(total).movePointLeft(2).toString() + "<br>");
			out.flush();
			rowsAffected++;
		}

	}
	out.println("total: " + rowsAffected);
	daySheetBean.closePstmtConn();
%>

<p>
<h1>done.</h1>
</body>
</html>