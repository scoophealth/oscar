<%--  
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
--%>


<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("d.last_name") ;
  int age = Integer.parseInt(request.getParameter("age"));
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="patientBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Vector" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
//{"search_patient", "select provider_no, last_name, first_name, chart_no from demographic where provider_no = ? order by "+orderby }, 
{"search_patient", "select distinct(d.demographic_no), d.last_name, d.first_name, d.sex, d.chart_no, d.patient_status, a.appointment_date, d.address, d.city, d.province, d.postal, DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d') as dob from demographic d, appointment a where d.provider_no = ? and d.demographic_no=a.demographic_no and (d.patient_status like 'AC' or d.patient_status like 'UHIP') and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'))) -(RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >? order by d.last_name, d.first_name, a.appointment_date desc" }, 
{"searchmygroupall", "select * from mygroup where mygroup_no= ? order by last_name"}, 
  };
  String[][] responseTargets=new String[][] {  };
  patientBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<html>
<head>
<title>PATIENT CHART LIST </title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
<link rel="stylesheet" href="../web.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=15";//360,680
  var popup=window.open(page, "apptday", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function refresh() {
  history.go(0);
}

//-->
</SCRIPT>
</head>
<% 
  String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"175" ;
  ResultSet rsdemo = null ;
  boolean bodd = false;
  boolean bGroup = false;
  
  //initial myGroupBean if neccessary
  if(provider_no.startsWith("_grp_")) {
    bGroup = true;
	  rsdemo = patientBean.queryResults(provider_no.substring(5), "searchmygroupall");
    while (rsdemo.next()) { 
	    myGroupBean.add(rsdemo.getString("provider_no"));
//System.out.println(rsdemo.getString("provider_no"));
    }
  }
%>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#CCCCFF"><th align=CENTER NOWRAP><font face="Helvetica">PATIENT CHART LIST</font></th>
    <th width="10%" nowrap>
      <input type="button" name="Button" value="Print" onClick="window.print()"><input type="button" name="Button" value=" Exit " onClick="window.close()"></th></tr>
</table>
<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String [] param = new String[1];
  int pnum = bGroup?myGroupBean.size():1 ;
  int dnoTemp = 0;

  for(int i=0; i<pnum; i++) {
    param[0]=bGroup?((String) myGroupBean.get(i)):provider_no;
//    param[1]=param[0];
//    param[2]=param[0];
//  System.out.println(param[0]);
	  rsdemo = patientBean.queryResults(param,(new int[]{age}), "search_patient");
    while (rsdemo.next()) { 
      if (rsdemo.getInt("d.demographic_no") != dnoTemp) dnoTemp = rsdemo.getInt("d.demographic_no");
	  else continue;
      bodd = bodd?false:true;
	    if(!strTemp.equals(param[0]) ) { //new provider for a new table
	      strTemp = param[0] ;
	      bFistL = true;
	      out.println("</table> <p>") ;
	    }
	    if(bFistL) {
	      bFistL = false;
          bodd = false ;
          dnoTemp = 0;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0" ><tr> 
<td><%=providerBean.getProperty(strTemp) %>  </td>
<td align="right"></td>
</tr></table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="1" cellpadding="2" > 
<tr bgcolor="#CCCCFF" align="center">
<TH width="12%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.last_name">Last Name</a></b></TH>
<TH width="12%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.first_name">First Name</a> </b></TH>
<TH width="2%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.sex">Sex</a> </b></TH>
<TH width="5%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.chart_no">Chart No</a> </b></TH>
<TH width="12%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.appointment_date">Appt. Date</a> </b></TH>
<TH width="20%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.address">Address</a> </b></TH>
<TH width="10%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.city">City,Province</a> </b></TH>
<TH width="10%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.postal">Postal</a> </b></TH>
<TH width="12%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=dob">DOB</a> </b></TH>
<TH width="2%"><b><a href="reportpatientchartlist.jsp?provider_no=<%=provider_no%>&orderby=d.patient_status">Status</a> </b></TH>
</tr>
<%
    }
%> 
<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
      <td><%=rsdemo.getString("d.last_name")%></td>
      <td><%=rsdemo.getString("d.first_name")%></td>
      <td><%=rsdemo.getString("d.sex")%></td>
      <td align="center"><%=rsdemo.getString("d.chart_no")%></td>
      <td align="center"><%=rsdemo.getString("a.appointment_date")%></td>
      <td align="center"><%=rsdemo.getString("d.address")%></td>
      <td align="center"><%=rsdemo.getString("d.city") + ", " + rsdemo.getString("d.province")%></td>
      <td align="center"><%=rsdemo.getString("d.postal")%></td>
      <td align="center"><%=rsdemo.getString("dob")%></td>
      <td align="center"><%=rsdemo.getString("d.patient_status")%></td>
</tr>
<%
  }
  }
  patientBean.closePstmtConn();
%> 

</table>
</body>
</html>