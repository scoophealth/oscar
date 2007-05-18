<%
String invNo = request.getParameter("billingNo");
Billing3rdPartPrep privateObj = new Billing3rdPartPrep();
oscar.oscarRx.data.RxProviderData.Provider provider = new oscar.oscarRx.data.RxProviderData().getProvider((String) session.getAttribute("user"));
//Properties propClinic = privateObj.getLocalClinicAddr( (String) session.getAttribute("user"));
Properties prop3rdPart = privateObj.get3rdPartBillProp(invNo);
Properties prop3rdPayMethod = privateObj.get3rdPayMethod();

BillingCorrectionPrep billObj = new BillingCorrectionPrep();
List aL = billObj.getBillingRecordObj(invNo);
BillingClaimHeader1Data ch1Obj = (BillingClaimHeader1Data) aL.get(0);
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*, oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*,oscar.oscarProvider.data.*,java.math.*" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Invoice - <%=invNo %></title>
</head>
<body>

<table width="100%" border="0">
<tr>
<td>
    <%--
<b><%=propClinic.getProperty("clinic_name", "") %></b><br/>
<%=propClinic.getProperty("clinic_address", "") %><br/>
<%=propClinic.getProperty("clinic_city", "") %>,
<%=propClinic.getProperty("clinic_province", "") %><br/>
<%=propClinic.getProperty("clinic_postal", "") %><br/>
Tel.: <%=propClinic.getProperty("clinic_phone", "") %><br/>
--%>
<b><%= provider.getClinicName().replaceAll("\\(\\d{6}\\)","") %></b><br>
<%= provider.getClinicAddress() %><br>
<%= provider.getClinicCity() %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%= provider.getClinicPostal() %><br>
Tel: <%= provider.getClinicPhone() %><br>
Fax: <%= provider.getClinicFax() %><br>
</td>
<td align="right" valign="top">
<font size="+2"><b>Invoice - <%=invNo %></b></font><br/>
Date:<%=DateUtils.sumDate("yyyy-MM-dd HH:mm","0") %></td>
</tr></table>

<hr>
<table width="100%" border="0">
<tr><td width="50%" valign="top">Bill To<br/>
<pre><%=prop3rdPart.getProperty("billTo","") %>
</pre>
</td>
<td valign="top">Remit To<br/>
<pre><%=prop3rdPart.getProperty("remitTo","") %>
</pre>
</td>
</tr>
</table>

<table width="100%" border="0">
<tr><td>Patient: <%=ch1Obj.getDemographic_name() %> (<%=ch1Obj.getDemographic_no() %>)
<%=ch1Obj.getSex().equals("1")? "Male":"Female" %> DOB: <%=ch1Obj.getDob() %>
</td>
</tr>
</table>
<hr>

<table width="100%" border="0">
<tr><td><%=ch1Obj.getComment() %>
</td>
</tr>
</table>

<table width="100%" border="0" >
<tr><th>Service Date</th><th>Practitioner</th><th>Payee</th><th>Ref. Doctor</th>
</tr>
<tr align="center"><td><%=ch1Obj.getBilling_date() %></td>
<td><%=(new ProviderData()).getProviderName(ch1Obj.getProvider_no()) %></td>
<td><%=(new ProviderData()).getProviderName(ch1Obj.getProvider_no()) %></td>
<td><%=ch1Obj.getRef_num() %></td>
</tr>
</table>
<hr/>

<table width="100%" border="0">
<tr><th>Item #:</th><th>Description</th><th>Service Code</th><th>Qty</th><th>Dx</th><th>Amount</th></tr>
<% for(int i=1; i<aL.size(); i++) { 
	BillingItemData itemObj = (BillingItemData) aL.get(i);
	String serviceDesc = billObj.getBillingCodeDesc(itemObj.getService_code());
%>
<tr align="center"><td><%=itemObj.getId() %></td><td><%=serviceDesc %></td><td><%=itemObj.getService_code() %></td>
<td><%=itemObj.getSer_num() %></td><td><%=itemObj.getDx() %></td><td align="right"><%=itemObj.getFee() %></td></tr>
<% } %>
</table>

<hr/>
<table width="100%" border="0">
<tr align="right"><td width="86%">Total:</td><td><%=ch1Obj.getTotal() %></td></tr>
<tr align="right"><td>Payments:</td><td><%=prop3rdPart.getProperty("payment","0.00") %></td></tr>
<tr align="right"><td>Refunds:</td><td><%=prop3rdPart.getProperty("refund","0.00") %></td></tr>
<% 
BigDecimal bdBal = new BigDecimal(ch1Obj.getTotal()).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdPay = new BigDecimal(prop3rdPart.getProperty("payment","0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdRef = new BigDecimal(prop3rdPart.getProperty("refund","0.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
bdBal = bdBal.subtract(bdPay);
bdBal = bdBal.subtract(bdRef);
%>
<tr align="right"><td><b>Balance:</b></td><td><%=bdBal %></td></tr>
<tr align="right"><td>(<%=prop3rdPayMethod.getProperty(prop3rdPart.getProperty("payMethod",""), "") %>)</td><td></td></tr>
</table>

</body>
</html>