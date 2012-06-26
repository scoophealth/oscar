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
<% 
    if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");

OscarProperties props = OscarProperties.getInstance();
if(props.getProperty("isNewONbilling", "").equals("true")) {
%>
<jsp:forward page="onGenRAError.jsp" />
<% } %>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="billing.css">
<title>Billing Reconcilliation</title>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">
		Billing Reconcilliation - Error Report</font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

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

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<form action="genRAError.jsp">
	<tr bgcolor="#333333">
		<th><input type="hidden" name="rano" value="<%=raNo%>"> <select
			name="proNo">
			<option value="all" <%=proNo.equals("all")?"selected":""%>>All
			Providers</option>

			<%   
ResultSet rsdemo3 = null;
ResultSet rsdemo2 = null;
ResultSet rsdemo = null;
rsdemo = apptMainBean.queryResults(raNo, "search_raprovider");
while (rsdemo.next()) {   
	pohipno = rsdemo.getString("providerohip_no");
	plast = rsdemo.getString("last_name");
	pfirst = rsdemo.getString("first_name");
%>
			<option value="<%=pohipno%>" <%=proNo.equals(pohipno)?"selected":""%>><%=plast%>,<%=pfirst%></option>
			<%
}
%>
		</select><input type=submit name=submit value=Generate></th>
	</tr>
	</form>
</table>


<% 
if (proNo.compareTo("") == 0 || proNo.compareTo("all") == 0){ 
%>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<form>
	<tr>
		<td width="10%" height="16">Billing No</td>
		<td width="15%" height="16">Demographic</td>
		<td width="10%" height="16">Service Date</td>
		<td width="10%" height="16">Service Code</td>
		<td width="15%" height="16">Count</td>
		<td width="15%" height="16" align=right>Claim</td>
		<td width="15%" height="16" align=right>Pay</td>
		<td width="10%" height="16" align=right>Error</td>
	</tr>

	<%
/*
	String[] param = new String[3];
	param[0] = raNo;
	param[1] = "I2";
	param[2] = "%";

	String[] param0 = new String[2];
	rsdemo2 = null;
	rsdemo = apptMainBean.queryResults(param, "search_raerror");
	while (rsdemo.next()) {   
		account = rsdemo.getString("billing_no");
		param0[0]=raNo;
		param0[1]=account;
		demoLast = "";
		rsdemo3 =apptMainBean.queryResults(param0[1],"search_bill_short");
		while (rsdemo3.next()) {
			demoLast = rsdemo3.getString("demographic_name");
		}
		rsdemo2 = apptMainBean.queryResults(param0,"search_rabillno");
		while (rsdemo2.next()) {   
			servicecode = rsdemo2.getString("service_code");
			servicedate = rsdemo2.getString("service_date");
			serviceno = rsdemo2.getString("service_count");
			explain = rsdemo2.getString("error_code");
			amountsubmit = rsdemo2.getString("amountclaim");
			amountpay = rsdemo2.getString("amountpay");
			if (explain == null || explain.compareTo("") == 0){
				explain = "**";
			}    
*/
%>
	<tr>
		<td width="10%" height="16"><%=account%></td>
		<td width="10%" height="16"><%=demoLast%></td>
		<td width="10%" height="16"><%=servicedate%></td>
		<td width="10%" height="16"><%=servicecode%></td>
		<td width="15%" height="16"><%=serviceno%></td>
		<td width="15%" height="16" align=right><%=amountsubmit%></td>
		<td width="15%" height="16" align=right><%=amountpay%></td>
		<td width="10%" height="16" align=right><%=explain%></td>
	</tr>

	<%
//		}
//	} 
} else {
%>

	<table width="100%" border="1" cellspacing="0" cellpadding="0"
		bgcolor="#EFEFEF">
		<form>
		<tr>
			<td width="10%" height="16">Billing No</td>
			<td width="15%" height="16">Demographic</td>
			<td width="10%" height="16">Service Date</td>
			<td width="10%" height="16">Service Code</td>
			<td width="15%" height="16">Count</td>
			<td width="15%" height="16" align=right>Claim</td>
			<td width="15%" height="16" align=right>Pay</td>
			<td width="10%" height="16" align=right>Error</td>
		</tr>

		<%
	String[] param0 = new String[2];
	String[] param = new String[3];
	param[0] = raNo;
	param[1] = "I2";
	param[2] = proNo+"%";
	rsdemo2 = null;
	rsdemo3 = null;
	rsdemo = apptMainBean.queryResults(param, "search_raerror");
	while (rsdemo.next()) {   
		account = rsdemo.getString("billing_no");
		param0[0]=raNo;
		param0[1]=account;
		demoLast = "";
		rsdemo3 =apptMainBean.queryResults(param0[1],"search_bill_short");
		while (rsdemo3.next()) {
			demoLast = rsdemo3.getString("demographic_name");
		}

		rsdemo2 = apptMainBean.queryResults(param0,"search_rabillno");
		while (rsdemo2.next()) {   
			servicecode = rsdemo2.getString("service_code");
			servicedate = rsdemo2.getString("service_date");
			serviceno = rsdemo2.getString("service_count");
			explain = rsdemo2.getString("error_code");
			amountsubmit = rsdemo2.getString("amountclaim");
			amountpay = rsdemo2.getString("amountpay");
			if (explain == null || explain.compareTo("") == 0){
				explain = "**";
			}      
%>
		<tr>
			<td height="16"><%=account%></td>
			<td height="16"><%=demoLast%></td>
			<td height="16"><%=servicedate%></td>
			<td height="16"><%=servicecode%></td>
			<td height="16"><%=serviceno%></td>
			<td height="16" align=right><%=amountsubmit%></td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right><%=explain%></td>
		</tr>

		<%
		}
	}
%>

	</table>

	<%
}
%>

</body>
</html>
