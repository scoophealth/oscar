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
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";
  
  String strLimit1="0";
  String strLimit2="50";  
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  String startDate =null, endDate=null;
  if(request.getParameter("startDate")!=null) startDate = request.getParameter("startDate");  
  if(request.getParameter("endDate")!=null) endDate = request.getParameter("endDate");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*" errorPage="../errorpage.jsp" %>
<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerNameBean" class="java.util.Properties" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
{"select_formar", "select demographic_no, c_finalEDB, c_pName, pg1_age, c_gravida, c_term, pg1_homePhone, provider_no from formAR where c_finalEDB >= ? and c_finalEDB <= ? group by demographic_no order by ? desc limit ?, ?"  }, 
{"search_provider", "select provider_no, last_name, first_name from provider where provider_type='doctor' order by ?"}, 
  };
  reportMainBean.doConfigure(dbParams,dbQueries);
%>
 
<html>
<head>
<title> REPORT EDB </title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css" >
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
}
//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body  background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica">EDB LIST</font></th>
  </tr><tr>
   <td align="right" ><input type="button" name="Button" value="Print" onClick="window.print()">
   <input type="button" name="Button" value="Cancel" onClick="window.close()"></th>
  </tr>
</table>

<CENTER><table width="100%" border="0" bgcolor="silver" cellspacing="2" cellpadding="2"> 
<tr bgcolor='<%=deepcolor%>'> 
<TH align="center" width="10%" nowrap><b>EDB</b></TH>
<TH align="center" width="30%"><b>Patient's Name </b></TH>
<!--TH align="center" width="20%"><b>Demog' No </b></TH-->
<TH align="center" width="5%"><b>Age</b></TH>
<TH align="center" width="5%"><b>Gravida</b></TH>
<TH align="center" width="10%"><b>Term</b></TH>
<TH align="center" width="30%"><b>Phone</b></TH>
<TH align="center"><b>Provider</b></TH>
</tr>
<%
  ResultSet rs=null ;
  rs = reportMainBean.queryResults("last_name", "search_provider");
  while (rs.next()) { 
    providerNameBean.setProperty(rs.getString("provider_no"), new String( rs.getString("last_name")+","+rs.getString("first_name") ));
  }
    
  String[] param =new String[3];
  param[0]=startDate; //"0000-00-00"; 
  param[1]=endDate; //"0000-00-00"; 
  param[2]="c_finalEDB"; //"0000-00-00"; 
  int[] itemp1 = new int[2];
  itemp1[0] = Integer.parseInt(strLimit1);
  itemp1[1] = Integer.parseInt(strLimit2);
  boolean bodd=false;
  int nItems=0;
  rs = reportMainBean.queryResults(param,itemp1, "select_formar");
  while (rs.next()) {
    bodd=bodd?false:true; //for the color of rows
    nItems++; 
%>
<tr bgcolor="<%=bodd?weakcolor:"white"%>">
      <td align="center" nowrap><%=rs.getString("c_finalEDB")!=null?rs.getString("c_finalEDB").replace('-','/'):"0000/00/00"%></td>
      <td><%=rs.getString("c_pName")%></td>
      <!--td align="center" ><%=rs.getString("demographic_no")%> </td-->
      <td><%=rs.getString("pg1_age")%></td>
      <td><%=rs.getString("c_gravida")%></td>
      <td><%=rs.getString("c_term")%></td>
      <td nowrap><%=rs.getString("pg1_homePhone")%></td>
      <td><%=providerNameBean.setProperty(rs.getString("provider_no"), "")%></td>
</tr>
<%
  }
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
<a href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="reportnewedblist.jsp?startDate=<%=request.getParameter("startDate")%>&endDate=<%=request.getParameter("endDate")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> Next Page</a>
<%
}
%>
</body>
</html>