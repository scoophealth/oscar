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
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%
String invNo = request.getParameter("billingNo");
Billing3rdPartPrep privateObj = new Billing3rdPartPrep();
Properties propClinic = privateObj.getLocalClinicAddr();
Properties prop3rdPart = privateObj.get3rdPartBillProp(invNo);
Properties prop3rdPayMethod = privateObj.get3rdPayMethod();
Properties propGst = privateObj.getGst(invNo);
//int gstFlag = 0;
//if ( propGst.getProperty("gst", "") != "0.00" && propGst.getProperty("gst", "") != null ){
//    gstFlag = 1;
//}

BillingCorrectionPrep billObj = new BillingCorrectionPrep();
List aL = billObj.getBillingRecordObj(invNo);
BillingClaimHeader1Data ch1Obj = (BillingClaimHeader1Data) aL.get(0);
DemographicDao demoDAO = (DemographicDao)SpringUtils.getBean("demographicDao");
Demographic demo = demoDAO.getDemographic(ch1Obj.getDemographic_no());

Properties gstProp = new Properties();
GstControlAction db = new GstControlAction();
gstProp = db.readDatabase();

String percent = gstProp.getProperty("gstPercent", "");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page
	import="java.util.*, oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*,oscar.oscarProvider.data.*,java.math.* ,oscar.oscarBilling.ca.on.administration.*"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Invoice</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="invoice"/>
</head>
<body>

<table width="100%" border="0">
	<tr>
		<td><b><%=propClinic.getProperty("clinic_name", "") %></b><br />
		<%=propClinic.getProperty("clinic_address", "") %><br />
		<%=propClinic.getProperty("clinic_city", "") %>, <%=propClinic.getProperty("clinic_province", "") %><br />
		<%=propClinic.getProperty("clinic_postal", "") %><br />
		Tel.: <%=propClinic.getProperty("clinic_phone", "") %><br />

		</td>
		<td align="right" valign="top"><font size="+2"><b>Invoice
		- <%=invNo %></b></font><br />
		Date:<%=DateUtils.sumDate("yyyy-MM-dd HH:mm","0") %></td>
	</tr>
</table>

<hr>
<table width="100%" border="0">
	<tr>
		<td width="50%" valign="top">Bill To<br />
		<pre><%=prop3rdPart.getProperty("billTo","") %>
</pre></td>
		<td valign="top">Remit To<br />
		<pre><%=prop3rdPart.getProperty("remitTo","") %>
</pre></td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<td>Patient: <%=ch1Obj.getDemographic_name() %> (<%=ch1Obj.getDemographic_no() %>)
		<%=ch1Obj.getSex().equals("1")? "Male":"Female" %> DOB: <%=ch1Obj.getDob() %><br>
                Insurance No: <%=demo.getHin()%>
		</td>
	</tr>
</table>
<hr>

<table width="100%" border="0">
	<tr>
		<td><%=ch1Obj.getComment() %></td>
	</tr>
</table>

<table width="100%" border="0">
	<tr>
		<th>Service Date</th>
		<th>Practitioner</th>
		<th>Payee</th>
		<th>Ref. Doctor</th>
	</tr>
	<tr align="center">
		<td><%=ch1Obj.getBilling_date() %></td>
		<td><%=(new ProviderData()).getProviderName(ch1Obj.getProviderNo()) %></td>
<% Properties prop = oscar.OscarProperties.getInstance();
   String payee = prop.getProperty("PAYEE", "");
   payee = payee.trim();
   if( payee.length() > 0 ) {
%>
    <td><%=payee%></td>
<% } else { %>
    <td><%=(new ProviderData()).getProviderName(ch1Obj.getProviderNo()) %></td>
<% } %>
		<td><%=ch1Obj.getRef_num() %></td>
	</tr>
</table>
<hr />

<table width="100%" border="0">
	<tr>
		<th>Item #:</th>
		<th>Description</th>
		<th>Service Code</th>
		<th>Qty</th>
		<th>Dx</th>
		<th>Amount</th>
	</tr>
	<% for(int i=1; i<aL.size(); i++) { 
	BillingItemData itemObj = (BillingItemData) aL.get(i);
	String serviceDesc = billObj.getBillingCodeDesc(itemObj.getService_code());
%>
	<tr align="center">
		<td><%=itemObj.getId() %></td>
		<td><%=serviceDesc %></td>
		<td><%=itemObj.getService_code() %></td>
		<td><%=itemObj.getSer_num() %></td>
		<td><%=itemObj.getDx() %></td>
		<td align="right"><%=itemObj.getFee() %></td>
	</tr>
	<% } %>
</table>

<hr />
<% 
BigDecimal bdBal = new BigDecimal(ch1Obj.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdPay = new BigDecimal(prop3rdPart.getProperty("payment","0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdRef = new BigDecimal(prop3rdPart.getProperty("refund","0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
bdBal = bdBal.subtract(bdPay);
bdBal = bdBal.subtract(bdRef);
//BigDecimal bdGst = new BigDecimal(propGst.getProperty("gst", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
%>
<table width="100%" border="0">

	<tr align="right">
		<td width="86%">Total:</td>
		<td><%=ch1Obj.getTotal()%></td>
	</tr>
	<tr align="right">
		<td>Payments:</td>
		<td><%=prop3rdPart.getProperty("payment","0.00") %></td>
	</tr>
	<tr align="right">
		<td>Refunds:</td>
		<td><%=prop3rdPart.getProperty("refund","0.00") %></td>
	</tr>

	<tr align="right">
		<td><b>Balance:</b></td>
		<td><%=bdBal %></td>
	</tr>
	<tr align="right">
		<td>(<%=prop3rdPayMethod.getProperty(prop3rdPart.getProperty("payMethod",""), "") %>)</td>
		<td></td>
	</tr>
</table>

</body>
</html>
