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
  String strLimit1="0";
  String strLimit2="50";  
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*" errorPage="errorpage.jsp" %>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerNameBean" class="oscar.Dict" scope="page" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="reportMainBeanConn.jsp" %>  
<% } %>
 
<html>
<head>
<title> REPORT ACTIVE PATIENT </title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css" >
<script language="JavaScript">
<!--
function setfocus() {
//  document.titlesearch.keyword.focus();
//  document.titlesearch.keyword.select();
}
//-->
</SCRIPT>

</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="#486ebd"><th align=CENTER><font face="Helvetica" color="#FFFFFF">ACTIVE PATIENT LIST</font></th>
   <th align="right" width="10%" NOWRAP><input type="button" name="Button" value="Print" onClick="window.print()">
   <input type="button" name="Button" value="Cancel" onClick="window.close()"></th>
  </tr>
</table>

<CENTER><table width="100%" border="1" bgcolor="#ffffff" cellspacing="0" cellpadding="1"> 
<tr bgcolor="silver"> 
<TH align="center" width="12%" nowrap><b>Last Name</b></TH>
<TH align="center" width="12%"><b>First Name </b></TH>
<TH align="center" width="5%"><b>Chart No </b></TH>
<TH align="center" width="5%"><b>Age</b></TH>
<TH align="center" width="5%"><b>Sex</b></TH>
<TH align="center" width="10%"><b>HIN</b></TH>
<TH align="center" width="5%"><b>Ver</b></TH>
<TH align="center" width="16%"><b>MC Doc</b></TH>
<TH align="center" width="10%"><b>Date Joined</b></TH>
<TH align="center" width="15%"><b>Phone</b></TH>
</tr>
<%
  int age=0;
  ResultSet rs=null ;
  int[] itemp1 = new int[2];
  itemp1[1] = Integer.parseInt(strLimit1);
  itemp1[0] = Integer.parseInt(strLimit2);
  rs = reportMainBean.queryResults(itemp1, "search_demo_active");

  boolean bodd=false;
  int nItems=0;
  
  while (rs.next()) {
    bodd=bodd?false:true; //for the color of rows
    nItems++; 
    if(rs.getString("year_of_birth")!=null && rs.getString("month_of_birth")!=null && rs.getString("date_of_birth")!=null) age=MyDateFormat.getAge(Integer.parseInt(rs.getString("year_of_birth")),Integer.parseInt(rs.getString("month_of_birth")),Integer.parseInt(rs.getString("date_of_birth")));
%>
<tr bgcolor="<%=bodd?"ivory":"white"%>">
      <td nowrap><%=rs.getString("last_name")%></td>
      <td nowrap><%=rs.getString("first_name")%></td>
      <td align="center" ><%=rs.getString("chart_no")%> </td>
      <td align="center"><%=age%></td>
      <td align="center"><%=rs.getString("sex")%></td>
      <td><%=rs.getString("hin")%></td>
      <td align="center"><%=rs.getString("ver")%></td>
      <td><%=rs.getString("provider_no").length()>11?rs.getString("provider_no").substring(0,11):rs.getString("provider_no")%></td>
      <td><%=rs.getString("date_joined")%></td>
      <td><%=rs.getString("phone")%></td>
</tr>
<%
  }

if(reportMainBean.getBDoConfigure()) reportMainBean.setBDoConfigure();
  reportMainBean.closePstmtConn();
%> 

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
<a href="reportactivepatientlist.jsp?limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="reportactivepatientlist.jsp?limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> Next Page</a>
<%
  }
%>
</body>
</html>
