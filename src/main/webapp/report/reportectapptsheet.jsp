<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

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
  

  String orderby = request.getParameter("orderby")!=null?request.getParameter("orderby"):("appointment_date") ;
  String demographic_no = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"0" ;
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
  String strLimit1="0";
  String strLimit2="10";  
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");  
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<jsp:useBean id="daySheetBean" class="oscar.AppointmentMainBean"
	scope="page" />
<% 
  String [][] dbQueries=new String[][] { 
{"search_appt","select appointment_no, appointment_date,start_time, end_time, reason from appointment where demographic_no=? order by ? desc limit ? offset ?" }, 
  };
  daySheetBean.doConfigure(dbQueries);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ENCOUNTER APPT SHEET</title>
<link rel="stylesheet" href="../web.css">
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

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#CCCCFF">
		<th align=CENTER NOWRAP><font face="Helvetica">APPOINTMENT</font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="Print" onClick="window.print()"><input type="button"
			name="Button" value=" Exit " onClick="window.close()"></th>
	</tr>
</table>

<table width="480" border="0" cellspacing="1" cellpadding="0">
	<tr>
		<td></td>
		<td align="right"></td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="#ffffff" cellspacing="1"
	cellpadding="2">
	<tr bgcolor="#CCCCFF" align="center">
		<TH width="15%"><b><a
			href="reportectapptsheet.jsp?demographic_no=<%=demographic_no%>&orderby=appointment_date">Appt
		Date</a></b></TH>
		<TH width="10%"><b><a
			href="reportectapptsheet.jsp?demographic_no=<%=demographic_no%>&orderby=start_time">Start
		Time</a> </b></TH>
		<TH width="10%"><b><a
			href="reportectapptsheet.jsp?demographic_no=<%=demographic_no%>&orderby=end_time">End
		Time</a> </b></TH>
		<TH width="65%"><b>Reason</b></TH>
	</tr>
	<%
  ResultSet rsdemo = null ;
  boolean bodd = false;
  int nItems=0;
  
  String[] param =new String[2];
  param[0]=demographic_no; 
  param[1]=orderby; 
  int[] itemp1 = new int[2];
  itemp1[1] = Integer.parseInt(strLimit1);
  itemp1[0] = Integer.parseInt(strLimit2);
  rsdemo = daySheetBean.queryResults(param,itemp1, "search_appt");
  while (rsdemo.next()) { 
    bodd = bodd?false:true;
	nItems++;
%>
	<tr bgcolor="<%=bodd?"#EEEEFF":"white"%>">
		<td align="center"><%=rsdemo.getString("appointment_date")%></a></td>
		<td align="center"><%=rsdemo.getString("start_time")%></td>
		<td align="center"><%=rsdemo.getString("end_time")%></td>
		<td><%=rsdemo.getString("reason")%></td>
	</tr>
	<%
  }
%>

</table>
<br>
<CENTER>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="reportectapptsheet.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="reportectapptsheet.jsp?demographic_no=<%=demographic_no%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
  }
%>
</CENTER>
</body>
</html>
