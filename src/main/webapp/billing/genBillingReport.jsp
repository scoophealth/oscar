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

<%    
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1="0";
  String strLimit2="10";
 // if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
 // if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Summary</title>
<script language="JavaScript">
<!--



  
function onUnbilled(url) {
  if(confirm("You are about to delete the previous billing, are you sure?")) {
  self.location.href = url;
  }
}
//-->
</SCRIPT>
</head>

<body bgcolor="#FFFFFF" text="#000000">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">Billing
		Report by Appointment</font></th>
	</tr>
</table>
<% String apptProvider="";
   ResultSet rsprov = null;
   
   rsprov = apptMainBean.queryResults(request.getParameter("provider_no"), "search_provider_name");
    while (rsprov.next()) {
   apptProvider=rsprov.getString("last_name")+","+rsprov.getString("first_name");
   }
   //
   %>
<H4>Billing Report for <%=apptProvider%></H4>
<br>


<form><input type="button" value="Print Report"
	onClick="window.print()"></form>
<table width="100%" border="2" bgcolor="#ffffff">
	<tr bgcolor="#339999">
		<TH align="center" width="20%"><b>APPOINTMENT DATE</b></TH>
		<TH align="center" width="10%"><b>TIME</b></TH>
		<TH align="center" width="20%"><b>DEMOGRAPHIC</b></TH>
		<TH align="center" width="20%"><b>PROVIDER</b></TH>
		<TH align="center" width="10%"><b>COMMENTS</b></TH>
		<TH align="center" width="20%"><b>ACTION</b></TH>
	</tr>
	<%
  String billProvider="";
  ResultSet rs=null ;
  rs = apptMainBean.queryResults(request.getParameter("provider_no"), "search_bill_mismatch");
 
  boolean bodd=false;
  int nItems=0;
  String billType="", billCode = ""; 
  if(rs==null) {
    out.println("failed!!!"); 
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       billProvider = rs.getString("provider_no");
     
      
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<td width="20%" align="center" height="25"><%=rs.getString("appointment_date")%></td>
		<td align="center" width="10%" height="25"><%=rs.getString("start_time")%></td>
		<td align="center" width="20%" height="25"><%=rs.getString("d.last_name")+","+rs.getString("d.first_name")%></td>
		<td align="center" width="20%" height="25"><%=rs.getString("p.last_name")+","+rs.getString("p.first_name")%></td>
		<% if (billProvider.compareTo(request.getParameter("provider_no"))==0) { %>
		<td align="center" width="10%" height="25"></td>
		<% } else { %>
		<td align="center" width="10%" height="25">MISMATCH</td>
		<% } %>
		<td align="center" width="20%" height="25"><a href=#
			onClick="popupPage(600,800, '../billing/billingOB2.jsp?billing_no=<%=rs.getString("billing_no")%>&dboperation=search_bill&hotclick=0')">View</a><!--|<a href=# onclick="onUnbilled('../billing/billingDelete.jsp?appointment_no=<%=rs.getString("appointment_no")%>&billing_no=<%=rs.getString("billing_no")%>&billCode=<%=billCode%>&dboperation=delete_bill&hotclick=0')">-$</a>--></td>

	</tr>
	<%
    }
  }
  
%>

</table>
<form><input type="button" value="Close this window"
	onClick="window.close()"><input type="button"
	value="Print Report" onClick="window.print()"></form>

<br>
<!--
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
<a href="../billing/genBillingReport.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last Page</a> |
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a href="../billing/genBillingReport.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>"> Next Page</a>
<%
}
%>
-->
<p><%@ include file="../demographic/zfooterbackclose.jsp"%>
</p>

</body>
</html>
