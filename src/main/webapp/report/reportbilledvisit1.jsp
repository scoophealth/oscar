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
  //reportbilledvisit1.jsp?sdate=2002-04-15&edate=2003-03-31
  
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
Vector vNurse = new Vector();
Vector vNurseNo = new Vector();
Properties props = new Properties();
DBPreparedHandler db = new DBPreparedHandler();
DBPreparedHandlerParam[] params = new DBPreparedHandlerParam[2];
params[0] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(sdate));
params[1] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(edate));

// get nurse name list
String sql = "select provider_no, last_name, first_name from provider where provider_type like 'nurse%'";
ResultSet rs = db.queryResults(sql); 
while (rs.next()) { 
	vNurse.add(oscar.Misc.getString(rs,"last_name") + ", " + oscar.Misc.getString(rs,"first_name"));
	vNurseNo.add(oscar.Misc.getString(rs,"provider_no"));
}

// get total patPhys
sql = "select count(distinct(b.demographic_no)) from billing b, provider p where b.creator=p.provider_no  and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and p.provider_type='doctor'";
rs = db.queryResults(sql); 
while (rs.next()) { 
	props.setProperty("patPhys", oscar.Misc.getString(rs,1));
}
/*
out.println("<hr>");
out.println("Pat - PHYs: " + props.getProperty("patPhys") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/
// get patRes
sql = "select count(distinct(b.demographic_no)) from billing b, provider p where b.creator=p.provider_no  and b.billing_date>=? and b.billing_date<=? and b.status!='D' and p.provider_type='resident'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("patRes", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("Pat - Res: " + props.getProperty("patRes") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get pat num for Nurses
for (int i = 0; i < vNurseNo.size(); i++) {
	sql = "select count(distinct(b.demographic_no)) from billing b  where b.creator='" + vNurseNo.get(i) + "' and b.billing_date>=? and b.billing_date<=? and b.status!='D'";
	rs = db.queryResults(sql,params); 
	while (rs.next()) { 
		props.setProperty("patNurse" + i, oscar.Misc.getString(rs,1));
	}

/*
	out.println("<hr>");
	out.println("Pat - patNurse (" + vNurse.get(i) + "): " + props.getProperty("patRosterRO") + "<br>");
	out.println("busy ... busy ... busy ..................................................<br>");
	out.flush();
*/
}

//----------------------------------------------------------------------------------------------
// get visPhys
sql = "select count(distinct(b.billing_no)) from billing b, provider p where b.creator=p.provider_no  and b.billing_date>=? and b.billing_date<=? and b.status!='D' and p.provider_type='doctor'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visPhys", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - PHYs: " + props.getProperty("visPhys") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get visRes
sql = "select count(distinct(b.billing_no)) from billing b, provider p where b.creator=p.provider_no  and b.billing_date>=? and b.billing_date<=? and b.status!='D' and p.provider_type='resident'";
rs = db.queryResults(sql,params); 
while (rs.next()) { 
	props.setProperty("visRes", oscar.Misc.getString(rs,1));
}

/*
out.println("<hr>");
out.println("vis - Res: " + props.getProperty("visRes") + "<br>");
out.println("busy ... busy ... busy ..................................................<br>");
out.flush();
*/

// get vis num for Nurses
for (int i = 0; i < vNurseNo.size(); i++) {
	sql = "select count(distinct(b.billing_no)) from billing b  where b.creator='" + vNurseNo.get(i) + "' and b.billing_date>=? and b.billing_date<=? and b.status!='D'";
	rs = db.queryResults(sql,params); 
	while (rs.next()) { 
		props.setProperty("visNurse" + i, oscar.Misc.getString(rs,1));
	}

/*
	out.println("<hr>");
	out.println("vis - visNurse (" + vNurse.get(i) + "): " + props.getProperty("visRosterRO") + "<br>");
	out.println("busy ... busy ... busy ..................................................<br>");
	out.flush();
*/
}


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
		<TH width="6%">PHYs</TH>
		<TH width="6%">RES</TH>

		<% for (int i = 0; i < vNurseNo.size(); i++) { %>
		<TH width="6%"><%=vNurse.get(i)%></TH>
		<% } %>
	</tr>

	<tr bgcolor="<%=weakcolor%>">
		<td align="center">Patient</td>
		<td align="center"><%=props.getProperty("patPhys")%></td>
		<td align="center"><%=props.getProperty("patRes")%></td>
		<% for (int i = 0; i < vNurseNo.size(); i++) { %>
		<td align="center"><%=props.getProperty("patNurse"+i)%></td>
		<% } %>

	</tr>
	<tr>
		<td align="center">Visit</td>
		<td align="center"><%=props.getProperty("visPhys")%></td>
		<td align="center"><%=props.getProperty("visRes")%></td>
		<% for (int i = 0; i < vNurseNo.size(); i++) { %>
		<td align="center"><%=props.getProperty("visNurse"+i)%></td>
		<% } %>
	</tr>

</table>
</body>
</html>
