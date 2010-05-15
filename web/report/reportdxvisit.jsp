
<%
  //reportdxvisit.jsp?sdate=2003-04-01&edate=2003-12-31
  //
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("b.billing_date") ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, oscar.oscarDB.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
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
<title>PATIENT LIST</title>
<meta http-equiv="Cache-Control" content="no-cache">
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
Vector vServiceCode = new Vector();
Vector vServiceDesc = new Vector();
Vector vBillingDx = new Vector();
Properties props = new Properties();
DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

// get nurse name list
String sql = "select provider_no, last_name, first_name from provider where provider_type like 'nurse%'";
ResultSet rs = db.GetSQL(sql); 
while (rs.next()) { 
	vNurse.add(db.getString(rs,"last_name") + ", " + db.getString(rs,"first_name"));
	vNurseNo.add(db.getString(rs,"provider_no"));
}

// get dx code list
sql = "select ichppccode, description, diagnostic_code from ichppccode order by ichppccode";
rs = db.GetSQL(sql); 
while (rs.next()) { 
	vServiceCode.add(db.getString(rs,"ichppccode"));
	vServiceDesc.add(db.getString(rs,"description"));
	vBillingDx.add(db.getString(rs,"diagnostic_code"));
}

for (int i = 0; i < vServiceCode.size(); i++) {
	// get total pat
	sql = "select count(distinct(x.demographic_no)) from dxresearch x, demographic d where x.dxresearch_code='" + vServiceCode.get(i) + "' and x.demographic_no=d.demographic_no  and x.update_date>='" + sdate + "' and x.update_date<='" + edate + "' and x.status!='D' ";
	rs = db.GetSQL(sql); 
	while (rs.next()) { 
		props.setProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i), db.getString(rs,"count(distinct(x.demographic_no))"));
	}

	// get total vis
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd where b.billing_no=bd.billing_no and bd.diagnostic_code='" + vBillingDx.get(i) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'";
	rs = db.GetSQL(sql); 
	while (rs.next()) { 
		props.setProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i), db.getString(rs,"count(distinct(b.billing_no))"));
	}


	// get pat num for Nurses
	for (int j = 0; j < vNurseNo.size(); j++) {
		sql = "select count(distinct(x.demographic_no)) from dxresearch x, billing b, billingdetail bd where x.status!='D' and x.dxresearch_code='" + vServiceCode.get(i) + "' and x.demographic_no=b.demographic_no  and b.billing_no=bd.billing_no and bd.diagnostic_code='" + vBillingDx.get(i) + "' and b.creator='" + vNurseNo.get(j) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'" ;
		//sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd  where b.billing_no=bd.billing_no and bd.diagnostic_code='" + vServiceCode.get(i) + "' and b.creator='" + vNurseNo.get(j) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'"  ;
		rs = db.GetSQL(sql); 
		while (rs.next()) { 
			props.setProperty(vServiceCode.get(i) + "patNurse" + j + vServiceDesc.get(i), db.getString(rs,"count(distinct(x.demographic_no))"));
		}

		sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd where b.billing_no=bd.billing_no and bd.diagnostic_code='" + vBillingDx.get(i) + "' and b.creator='" + vNurseNo.get(j) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'" ;
		//sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd  where b.billing_no=bd.billing_no and bd.diagnostic_code='" + vServiceCode.get(i) + "' and b.creator='" + vNurseNo.get(j) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'"  ;
		rs = db.GetSQL(sql); 
		while (rs.next()) { 
			props.setProperty(vServiceCode.get(i) + "visNurse" + j + vServiceDesc.get(i), db.getString(rs,"count(distinct(b.billing_no))"));
		}
/*
		out.println("<hr>");
		out.println("Pat - patNurse (" + vNurse.get(j) + "): " + props.getProperty("patNurse" + j + vServiceDesc.get(i)) + "<br>");
		out.println("busy ... busy ... busy ..................................................<br>");
		out.flush();

		sql = "select count(distinct(b.appointment_no)) from billing b, billingdetail bd  where b.billing_no=bd.billing_no and bd.diagnostic_code='" + vServiceCode.get(i) + "' and b.creator='" + vNurseNo.get(j) + "' and b.billing_date>='" + sdate + "' and b.billing_date<='" + edate + "' and b.status!='D' and bd.status!='D'"  ;
		rs = db.GetSQL(sql); 
		while (rs.next()) { 
			props.setProperty(vServiceCode.get(i) + "visNurse" + j + vServiceDesc.get(i), db.getString(rs,"count(distinct(b.appointment_no))"));
		}
/*
		out.println("<hr>");
		out.println("Vis - patNurse (" + vNurse.get(j) + "): " + props.getProperty("visNurse" + j + vServiceDesc.get(i)) + "<br>");
		out.println("busy ... busy ... busy ..................................................<br>");
		out.flush();
*/

	}

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
		<TH width="5%">DxCode</TH>
		<TH width="10%">Desc</TH>
		<TH width="6%">Pat_Total</TH>
		<TH width="6%">Vis_Total</TH>

		<!--TH width="6%">Pat_Phys</TH>
<TH width="6%">Vis_Phys</TH>

<TH width="6%">Pat_Res</TH>
<TH width="6%">Vis_Res</TH-->

		<% for (int i = 0; i < vNurseNo.size(); i++) { %>
		<TH width="6%"><%="pat_"+vNurse.get(i)%></TH>
		<TH width="6%"><%="vis_"+vNurse.get(i)%></TH>
		<% } %>
	</tr>

	<% for (int i = 0; i < vServiceCode.size(); i++) { %>
	<tr bgcolor="<%= i%2==0?weakcolor:"white"%>">
		<td align="center"><%=vServiceCode.get(i)%></td>
		<td><%=vServiceDesc.get(i)%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i))%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i))%></td>

		<!--td align="center"><%--=props.getProperty(vServiceCode.get(i) + "patPhy" + vServiceDesc.get(i))--%></td>
      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "visPhy" + vServiceDesc.get(i))--%></td>

      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "patRes" + vServiceDesc.get(i))--%></td>
      <td align="center"><%--=props.getProperty(vServiceCode.get(i) + "visRes" + vServiceDesc.get(i))--%></td-->

		<% for (int j = 0; j < vNurseNo.size(); j++) { %>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "patNurse" + j  + vServiceDesc.get(i))%></td>
		<td align="center"><%=props.getProperty(vServiceCode.get(i) + "visNurse" + j  + vServiceDesc.get(i))%></td>
		<% } %>
	</tr>
	<% } %>


</table>
</body>
</html>