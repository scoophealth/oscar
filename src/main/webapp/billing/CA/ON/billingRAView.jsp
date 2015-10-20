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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.util.*,oscar.oscarProvider.data.*,oscar.oscarBilling.ca.on.data.*"%>

<%
String billingNo = request.getParameter("billing_no");

RAData raData = new RAData();
ArrayList aList = raData.getRAData(billingNo);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>JSP Page</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<div class="tableListing">
<table>
	<tr>
		<th>RA HEADER #</th>
		<th>OHIP #</th>
		<th>SERVICE CODE</th>
		<th>SERVICE COUNT</th>
		<th>HIN</th>
		<th>AMOUNT CLAIMED</th>
		<th>AMOUNT PAYED</th>
		<th>SERVICE DATE</th>
		<th>ERROR CODE</th>
		<th>BILL TYPE</th>
	</tr>
	<% for (int i = 0 ; i < aList.size(); i++) { 
       Hashtable h = (Hashtable) aList.get(i);           
       %>
	<tr>
		<td><%=h.get("raheader_no")%></td>
		<td><%=h.get("providerohip_no")%></a></td>
		<td><%=h.get("service_code")%></td>
		<td><%=h.get("service_count")%></td>
		<td><%=h.get("hin")%></td>
		<td><%=h.get("amountclaim")%></td>
		<td><%=h.get("amountpay")%></td>
		<td><%=h.get("service_date")%></td>
		<td><%=h.get("error_code")%></td>
		<td><%=h.get("billtype")%></td>
	</tr>
	<% } %>
	<table>
		</div>
</body>
</html>
