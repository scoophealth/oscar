<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*,java.io.*, oscar.util.*, java.text.*, java.net.*,sun.misc.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean" scope="page" />

<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"search_demographic", "select demographic_no, month_of_birth, date_of_birth from demographic order by ? " }, 
{"update_demographicmonth", "update demographic set month_of_birth = ? where demographic_no = ?" }, 
{"update_demographicdate", "update demographic set date_of_birth = ? where demographic_no = ?" }, 
  };
  String[][] responseTargets=new String[][] {  };
  daySheetBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<html>
<head>
<title>SCRAMBLE SHEET </title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
</head>
<body>
busy ... busy ... busy ..................................................<br>
<%
	ResultSet rsdemo = null ;
	int rowsAffected = 0; 
	rsdemo = daySheetBean.queryResults("demographic_no", "search_demographic");
	while (rsdemo.next()) { 
		if (rsdemo.getString("month_of_birth")!=null && rsdemo.getString("month_of_birth").length() == 1) {
			daySheetBean.queryExecuteUpdate(new String[] { "0"+rsdemo.getString("month_of_birth"), rsdemo.getString("demographic_no")}, "update_demographicmonth");
		}

		if (rsdemo.getString("date_of_birth")!=null && rsdemo.getString("date_of_birth").length() == 1) {
			daySheetBean.queryExecuteUpdate(new String[] { "0"+rsdemo.getString("date_of_birth"), rsdemo.getString("demographic_no")}, "update_demographicdate");
		}
	}

	daySheetBean.closePstmtConn();
%> 

<p><h1>done.</h1>
</body>
</html>