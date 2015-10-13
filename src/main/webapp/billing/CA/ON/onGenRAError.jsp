<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<title>Billing Reconcilliation</title>
</head>
<% 
String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="";

proNo = request.getParameter("proNo")!=null? request.getParameter("proNo") : "";
raNo = request.getParameter("rano");
if (raNo == null || raNo.compareTo("") == 0) return;
%>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<form action="onGenRAError.jsp">
	<tr class="myDarkGreen">
		<th align='LEFT'><font color="#FFFFFF"> Billing
		Reconcilliation - Error Report</font></th>
		<th align='RIGHT'><select name="proNo">
			<option value="all" <%=proNo.equals("all")?"selected":""%>>All
			Providers</option>

			<%   
//
BillingRAPrep obj = new BillingRAPrep();
List aL = obj.getProviderListFromRAReport(raNo);
for(int i=0; i<aL.size(); i++) {
	Properties prop = (Properties) aL.get(i);
	pohipno = prop.getProperty("providerohip_no", "");
	plast = prop.getProperty("last_name", "");
	pfirst = prop.getProperty("first_name", "");
%>
			<option value="<%=pohipno%>" <%=proNo.equals(pohipno)?"selected":""%>><%=plast%>,<%=pfirst%></option>
			<%
}
%>
		</select><input type=submit name='submit' value='Generate'> <input
			type="hidden" name="rano" value="<%=raNo%>"> <input
			type='button' name='print' value='Print' onClick='window.print()'>
		<input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
	</form>
</table>


<% 
if (proNo.compareTo("") == 0 || proNo.compareTo("all") == 0){ 
%>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr class="myYellow">
		<th width="10%">Billing No</th>
		<th width="15%">Demographic</th>
		<th width="10%">Service Date</th>
		<th width="10%">Service Code</th>
		<th width="15%">Count</th>
		<th width="15%">Claim</th>
		<th width="15%">Pay</th>
		<th>Error</th>
	</tr>

	<%

%>
	<tr>
		<td><%=account%></td>
		<td><%=demoLast%></td>
		<td><%=servicedate%></td>
		<td><%=servicecode%></td>
		<td><%=serviceno%></td>
		<td align=right><%=amountsubmit%></td>
		<td align=right><%=amountpay%></td>
		<td align=right><%=explain%></td>
	</tr>

	<%

} else {
%>

	<table width="100%" border="0" cellspacing="1" cellpadding="0"
		class="myIvory">
		<tr class="myYellow">
			<th width="10%">Billing No</th>
			<th width="25%">Demographic</th>
			<th width="10%">Service Date</th>
			<th width="10%">Service Code</th>
			<th width="10%">Count</th>
			<th width="15%">Claim</th>
			<th width="15%">Pay</th>
			<th>Error</th>
		</tr>

		<%//	
	aL = obj.getRAErrorReport(raNo, proNo, new String[] {"I2"});
	for(int i=0; i<aL.size(); i++) {
		Properties prop = (Properties) aL.get(i);
		account = prop.getProperty("account", "");
		demoLast = prop.getProperty("demoLast", "");
		servicecode = prop.getProperty("servicecode", "");
		servicedate = prop.getProperty("servicedate", "");
		serviceno = prop.getProperty("serviceno", "");
		explain = prop.getProperty("explain", "");
		amountsubmit = prop.getProperty("amountsubmit", "");
		amountpay = prop.getProperty("amountpay", "");
%>
		<tr <%=i%2==0? "class='myGreen'" : "" %>>
			<td align="center"><%=account%></td>
			<td><%=demoLast%></td>
			<td align="center"><%=servicedate%></td>
			<td align="center"><%=servicecode%></td>
			<td align="center"><%=serviceno%></td>
			<td align="right"><%=amountsubmit%></td>
			<td align="right"><%=amountpay%></td>
			<td align="right"><%=explain%></td>
		</tr>

		<%
	}
%>

	</table>

	<%
}
%>

</body>
</html>
