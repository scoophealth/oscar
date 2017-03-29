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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.common.model.BillingOnItemPayment" %>
<%@page import="org.oscarehr.common.model.BillingONPayment" %>
<%@page import="org.oscarehr.common.model.BillingPaymentType" %>
<%@page import="org.oscarehr.common.dao.BillingPaymentTypeDao" %>

<%@page import="org.oscarehr.util.SpringUtils" %>

<%
BillingONPayment billPayment = (BillingONPayment)request.getAttribute("billPayment");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page
	import="java.util.*, oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*,oscar.oscarProvider.data.*,java.math.* ,oscar.oscarBilling.ca.on.administration.*"%>

<html>
<head>
<title>View payment details</title>
</head>
<body>
	
	<label>View payment details:</label>
	<hr>
	<p />
	<table width="100%" border="0">
		<tbody>
			<tr>
				<th>Patient Name</th>
				<th>Invoice # </th>				
				<th>Service Code</th>
				<th>Payment</th>
				<th>Discount</th>
				<th>Refund Credit / Overpayment</th>
				<th>Refund / Write off</th>
			</tr>
			<logic:present name="itemDataList" scope="request">
				<logic:iterate id="itemData" name="itemDataList" indexId="idx">
				<tr align="center">
					<td><bean:write name="itemData" property="patientName"/></td>
					<td><bean:write name="itemData" property="ch1_id"/></td>					
					<td><bean:write name="itemData" property="service_code"/></td>
					<td><bean:write name="itemData" property="paid"/></td>
					<td><bean:write name="itemData" property="discount"/></td>
					<td><bean:write name="itemData" property="credit"/></td>
					<td><bean:write name="itemData" property="refund"/></td>
				</tr>
				</logic:iterate>
			</logic:present>
		</tbody>
	</table>
	<hr />
	<%if (billPayment != null) {
		String payType = "";
		BillingPaymentTypeDao billPayTypeDao = (BillingPaymentTypeDao)SpringUtils.getBean(BillingPaymentTypeDao.class);
		if (billPayTypeDao != null) {
			BillingPaymentType payTypeTmp = billPayTypeDao.find(billPayment.getPaymentTypeId());
			if (payTypeTmp != null) {
				payType = payTypeTmp.getPaymentType();
			}
		}
	%>
	<table width="100%" border="0">
		<tr align="right">
			<td width="86%">Date:</td>
			<td><%=billPayment.getPaymentDateFormatted() %></td>
		</tr>
		<tr align="right">
			<td width="86%">Payment type:</td>
			<td><%=payType %></td>
		</tr>
		<tr align="right">
			<td width="86%">Payment:</td>
			<td><%=billPayment.getTotal_payment() %></td>
		</tr>
		<tr align="right">
			<td width="86%">Discount:</td>
			<td><%=billPayment.getTotal_discount() %></td>
		</tr>
		<tr align="right">
			<td width="86%">Refund Credit / Overpayment:</td>
			<td><%=billPayment.getTotal_credit()%></td>
		</tr>
		<tr align="right">
			<td width="86%">Refund / Write off:</td>
			<td><%=billPayment.getTotal_refund() %></td>
		</tr>
	</table>
	<%} %>
</body>
</html>
