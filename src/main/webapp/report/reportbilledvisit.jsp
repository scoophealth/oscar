<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  //reportbilledvisit.jsp?sdate=2003-04-01&edate=2003-12-31
  
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("b.billing_date") ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, oscar.oscarDB.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PATIENT NO SHOW LIST</title>
<meta http-equiv=Expires content=-1>
<!--link rel="stylesheet" href="../web.css" -->
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}

  

//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<%
//busy ... busy ... busy ..................................................<br>
//display splash-msg first
out.flush();

// get total patientNum/rosterNR/rosterRO/sexF/sexM/
String sdate = request.getParameter("sdate")!=null?request.getParameter("sdate") : "2003-04-01" ;
String edate = request.getParameter("edate")!=null?request.getParameter("edate") : "2003-12-31" ;
Properties props = new Properties();
DBPreparedHandler db = new DBPreparedHandler();

// get total patTotal
DBPreparedHandlerParam[] params = new DBPreparedHandlerParam[2];
params[0] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(sdate));
params[1] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(edate));
String sql = "select count(distinct(b.demographic_no)) from billing b where  b.billing_date>=? and b.billing_date<=? and b.status!='D'";
ResultSet rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patTotal", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - Total: " + props.getProperty("patTotal") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get patRosterNR
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.roster_status='NR' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patRosterNR", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - patRosterNR: " + props.getProperty("patRosterNR") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get patRosterRO
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_b.billing_date>=? and b.billing_date<=?>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.roster_status='RO' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patRosterRO", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - patRosterRO: " + props.getProperty("patRosterRO") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get patSexF
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.sex='F' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patSexF", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - patSexF: " + props.getProperty("patSexF") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get patSexM
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.sex='M' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patSexM", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - patSexM: " + props.getProperty("patSexM") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat0_1
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 ";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat0_1", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat0_1: " + props.getProperty("pat0_1") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat2_11
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat2_11", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat2_11: " + props.getProperty("pat2_11") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat12_20
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat12_20", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat12_20: " + props.getProperty("pat12_20") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat21_34
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat21_34", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat21_34: " + props.getProperty("pat21_34") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat35_50
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat35_50", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat35_50: " + props.getProperty("pat35_50") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat51_64
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat51_64", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat51_64: " + props.getProperty("pat51_64") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat65_70
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat65_70", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat65_70: " + props.getProperty("pat65_70") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat71_
sql = "select count(distinct(b.demographic_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("pat71_", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - pat71_: " + props.getProperty("pat71_") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

///////////////////////////////////////////////////////////////////////////////////////////////

// get total visTotal
sql = "select count(distinct(b.billing_no)) from billing b where  b.billing_date>=? and b.billing_date<=? and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visTotal", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Vis - visTotal: " + props.getProperty("visTotal") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get visRosterNR
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.roster_status='NR' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visRosterNR", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - visRosterNR: " + props.getProperty("visRosterNR") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get visRosterRO
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.roster_status='RO' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visRosterRO", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - visRosterRO: " + props.getProperty("visRosterRO") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get visSexF
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.sex='F' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visSexF", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - visSexF: " + props.getProperty("visSexF") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get visSexM
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and d.sex='M' and b.status!='D'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visSexM", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - visSexM: " + props.getProperty("visSexM") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis0_1
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 ";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis0_1", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis0_1: " + props.getProperty("vis0_1") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis2_11
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis2_11", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis2_11: " + props.getProperty("vis2_11") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis12_20
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis12_20", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis12_20: " + props.getProperty("vis12_20") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis21_34
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis21_34", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis21_34: " + props.getProperty("vis21_34") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis35_50
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis35_50", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis35_50: " + props.getProperty("vis35_50") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis51_64
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis51_64", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis51_64: " + props.getProperty("vis51_64") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis65_70
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 " + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis65_70", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis65_70: " + props.getProperty("vis65_70") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis71_
sql = "select count(distinct(b.billing_no)) from billing b, demographic d where  b.billing_date>=? and b.billing_date<=? and b.demographic_no=d.demographic_no and b.status!='D'" + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 " ;
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("vis71_", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - vis71_: " + props.getProperty("vis71_") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/



rs.close();
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor=<%=deepcolor%>>
		<th><font face="Helvetica">PATIENT VISIT LIST </font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="Print" onClick="window.print()"><input type="button"
			name="Button" value=" Exit " onClick="window.close()"></th>
	</tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td>Period: (<%=sdate%> ~ <%=edate%>)</td>
	</tr>
</table>

<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1"
	cellpadding="0">
	<tr bgcolor=<%=deepcolor%> align="center">
		<TH width="10%">.</TH>
		<TH width="6%">Total</TH>
		<TH width="6%">RosterNR</TH>
		<TH width="6%">RosterRO</TH>
		<TH width="6%">Sex F</TH>
		<TH width="6%">Sex M</TH>
		<TH width="6%">Sex U</TH>

		<TH width="6%">0-1</TH>
		<TH width="6%">2-11</TH>
		<TH width="6%">12-20</TH>
		<TH width="6%">21-34</TH>
		<TH width="6%">35-50</TH>
		<TH width="6%">51-64</TH>
		<TH width="6%">65-70</TH>
		<TH width="6%">71+</TH>
	</tr>

	<tr bgcolor="<%=weakcolor%>">
		<td align="center">Patient</td>
		<td align="center"><%=props.getProperty("patTotal")%></td>
		<td align="center"><%=props.getProperty("patRosterNR")%></td>
		<td align="center"><%=props.getProperty("patRosterRO")%></td>
		<td align="center"><%=props.getProperty("patSexF")%></td>
		<td align="center"><%=props.getProperty("patSexM")%></td>
		<td align="center">0</td>
		<td align="center"><%=props.getProperty("pat0_1")%></td>
		<td align="center"><%=props.getProperty("pat2_11")%></td>
		<td align="center"><%=props.getProperty("pat12_20")%></td>
		<td align="center"><%=props.getProperty("pat21_34")%></td>
		<td align="center"><%=props.getProperty("pat35_50")%></td>
		<td align="center"><%=props.getProperty("pat51_64")%></td>
		<td align="center"><%=props.getProperty("pat65_70")%></td>
		<td align="center"><%=props.getProperty("pat71_")%></td>
	</tr>
	<tr>
		<td align="center">Visit</td>
		<td align="center"><%=props.getProperty("visTotal")%></td>
		<td align="center"><%=props.getProperty("visRosterNR")%></td>
		<td align="center"><%=props.getProperty("visRosterRO")%></td>
		<td align="center"><%=props.getProperty("visSexF")%></td>
		<td align="center"><%=props.getProperty("visSexM")%></td>
		<td align="center">0</td>
		<td align="center"><%=props.getProperty("vis0_1")%></td>
		<td align="center"><%=props.getProperty("vis2_11")%></td>
		<td align="center"><%=props.getProperty("vis12_20")%></td>
		<td align="center"><%=props.getProperty("vis21_34")%></td>
		<td align="center"><%=props.getProperty("vis35_50")%></td>
		<td align="center"><%=props.getProperty("vis51_64")%></td>
		<td align="center"><%=props.getProperty("vis65_70")%></td>
		<td align="center"><%=props.getProperty("vis71_")%></td>
	</tr>

</table>
</body>
</html>
