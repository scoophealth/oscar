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
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("a.appointment_date") ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="patientBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="myGroupBean" class="java.util.Vector" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
//{"search_noshowappt", "select provider_no, last_name, first_name, chart_no from appointment where provider_no = ? order by "+orderby }, 
{"search_noshowappt", "select a.appointment_no, a.appointment_date,a.name, a.provider_no, a.start_time, a.end_time, d.last_name, d.first_name from appointment a, demographic d where a.status = 'N' and a.provider_no = ? and a.appointment_date >= ? and a.appointment_date<= ? and a.demographic_no=d.demographic_no  order by "+orderby }, 
{"searchmygroupall", "select * from mygroup where mygroup_no= ? order by last_name"}, 
  };
  String[][] responseTargets=new String[][] {  };
  patientBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<html>
<head>
<title>PATIENT NO SHOW LIST </title>
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
  GregorianCalendar now=new GregorianCalendar();
  String createtime = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) +" "+now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) ;
  now.add(now.DATE, 1);
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  ResultSet rsdemo = null ;
  boolean bodd = false;
  boolean bGroup = false;
  String sdate = request.getParameter("sdate")!=null?request.getParameter("sdate"):(curYear+"-"+curMonth+"-"+curDay) ;
  String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"175" ;
  
  //initial myGroupBean if neccessary
  if(provider_no.startsWith("_grp_")) {
    bGroup = true;
	  rsdemo = patientBean.queryResults(provider_no.substring(5), "searchmygroupall");
    while (rsdemo.next()) { 
	    myGroupBean.add(rsdemo.getString("provider_no"));
    }
  }
%>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor=<%=deepcolor%>><th><font face="Helvetica">PATIENT NO SHOW LIST</font></th>
    <th width="10%" nowrap>
      <input type="button" name="Button" value="Print" onClick="window.print()"><input type="button" name="Button" value=" Exit " onClick="window.close()"></th></tr>
</table>
<%
  boolean bFistL = true; //first line in a table for TH
  String strTemp = "";
  String [] param = new String[3];
  param[1] = sdate;
  param[2] = createtime;
  int pnum = bGroup?myGroupBean.size():1 ;
//  System.out.println(myGroupBean.size());
  for(int i=0; i<pnum; i++) {
    param[0] = bGroup?((String) myGroupBean.get(i)):provider_no;
	  rsdemo = patientBean.queryResults(param, "search_noshowappt");
    while (rsdemo.next()) { 
      bodd = bodd?false:true;
	    if(!strTemp.equals(rsdemo.getString("provider_no")) ) { //new provider for a new table
	      strTemp = rsdemo.getString("provider_no") ;
	      bFistL = true;
	      out.println("</table> <p>") ;
	    }
	    if(bFistL) {
	      bFistL = false;
        bodd = false ;
%>
<table width="480" border="0" cellspacing="1" cellpadding="0" ><tr> 
<td><%=providerBean.getProperty(rsdemo.getString("provider_no")) %>  </td>
<td align="right"></td>
</tr></table>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="1" cellpadding="0" > 
<tr bgcolor=<%=deepcolor%> align="center">
<TH width="20%"><b><a href="reportnoshowapptlist.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&orderby=a.appointment_date">Appt Date</a></b></TH>
<TH width="20%"><b><a href="reportnoshowapptlist.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&orderby=a.start_time">Start Time</a> </b></TH>
<TH width="20%"><b><a href="reportnoshowapptlist.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&orderby=a.end_time">End Time</a> </b></TH>
<TH width="10%"><b><a href="reportnoshowapptlist.jsp?provider_no=<%=provider_no%>&sdate=<%=sdate%>&orderby=a.name">Patient's Name</a></b></TH>
<TH width="30%"><b>Comments</b></TH>
</tr>
<%
    }
%> 
<tr bgcolor="<%=bodd?weakcolor:"white"%>">
      <td align="center"><a href=# onClick="popupPage(300,700,'../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&appointment_no=<%=rsdemo.getString("a.appointment_no")%>&provider_no=<%=curUser_no%>&year=<%=MyDateFormat.getYearFromStandardDate(rsdemo.getString("a.appointment_date"))%>&month=<%=MyDateFormat.getMonthFromStandardDate(rsdemo.getString("a.appointment_date"))%>&day=<%=MyDateFormat.getDayFromStandardDate(rsdemo.getString("a.appointment_date"))%>&start_time=<%=rsdemo.getString("a.start_time")%>&demographic_no=');return false;" >
      <%=rsdemo.getString("a.appointment_date")%></a></td>
      <td align="center"><%=rsdemo.getString("a.start_time")%></td>
      <td align="center"><%=rsdemo.getString("a.end_time")%></td>
      <td align="center"><%=rsdemo.getString("a.name")%></td>
      <td>&nbsp;No Show</td>
</tr>
<%
  }
  }
  patientBean.closePstmtConn();
%> 

</table>
</body>
</html>