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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>APPOINTMENT HISTORY</title>
<link rel="stylesheet" href="../web.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="JavaScript">
<!--

//-->
</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body background="../images/gray_bg.jpg" bgproperties="fixed">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">APPOINTMENT
		HISTORY </font></th>
	</tr>
</table>

<!--%@ include file="zcppfulltitlesearch.htm" %-->

<table width="95%" border="0">
	<tr>
		<td align="left"><i>Results for Demographic</i> :<%=request.getParameter("last_name")%>,<%=request.getParameter("first_name")%>
		(<%=request.getParameter("demographic_no")%>)</td>
	</tr>
</table>
<hr>
<CENTER>
<table width="100%" border="2" bgcolor="#ffffff">
	<tr bgcolor="#339999">
		<TH align="center" width="20%"><b>APPT DATE</b></TH>
		<TH align="center" width="10%"><b>FROM</b></TH>
		<TH align="center" width="10%"><b>TO</b></TH>
		<TH align="center" width="20%"><b>PROVIDER</b></TH>
		<TH align="center" width="10%"><b>COMMENTS</b></TH>
	</tr>
	<%
  ResultSet rs=null ;
  rs = apptMainBean.queryResults(Integer.parseInt(request.getParameter("demographic_no")), request.getParameter("dboperation"));

  boolean bodd=false;
  int nItems=0;
  
  if(rs==null) {
    out.println("failed!!!");
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<td width="20%" align="center" height="25"><%=rs.getString("appointment_date")%></td>
		<td align="center" width="20%" height="25"><%=rs.getString("start_time")%></td>
		<td align="center" width="20%" height="25"><%=rs.getString("end_time")%></td>
		<td align="center" width="10%" height="25"><%=rs.getString("last_name")+","+rs.getString("first_name")%></td>
		<td align="center" width="10%" height="25"></td>
	</tr>
	<%
    }
  }
  apptMainBean.closePstmtConn();
  
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
}
%>
<p><%@ include file="zfooterbackclose.htm"%>
</center>
</body>
</html>