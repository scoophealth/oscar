<%
	// this page is only for correcting total fields with no point
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page
	import="java.util.*, java.sql.*,java.io.*, oscar.util.*, java.text.*, java.net.*,sun.misc.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />

<% 
  String [][] dbQueries=new String[][] { 
{"search_billing", "select total, billing_no, billing_date from billing where total not like ? order by billing_no desc" }, 
{"update_billingtotal", "update billing set total = ? where billing_no = ?" }, 
  };
  daySheetBean.doConfigure(dbQueries);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Total - ON</title>
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
%>

<p>
<h1>done.</h1>
</body>
</html>
