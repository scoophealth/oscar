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

<%@page import="java.sql.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscar.billing.CA.BC.title" /></title>
<script type="javascript">
function refreshParent(){
	opener.window.location.href = opener.window.location.href;
}
</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>
<logic:equal name="receivePaymentActionForm" property="paymentReceived"
	value="true">
	<fieldset><legend><bean:message
		key="oscar.billing.CA.BC.received" /></legend>
	<div class="msgDisplay">
	<%
oscar.oscarBilling.ca.bc.pageUtil.ReceivePaymentActionForm frm = (oscar.oscarBilling.ca.bc.pageUtil.ReceivePaymentActionForm)request.getAttribute("receivePaymentActionForm");
	%> <%=java.text.NumberFormat.getCurrencyInstance().format(new Double(frm.getAmountReceived()))%>
	<bean:message key="oscar.billing.CA.BC.credit" /> &nbsp; <bean:message
		key="oscar.billing.CA.BC.invoice" /> <bean:write
		name="receivePaymentActionForm" property="billNo" /> &nbsp; <bean:message
		key="oscar.billing.CA.BC.lineNo" /> <bean:write
		name="receivePaymentActionForm" property="billingmasterNo" /></div>
	<div align="center">
	<button
		onclick="opener.window.location.reload();self.close();return false;">Close</button>
	</div>
	</fieldset>
</logic:equal>
<logic:notEqual name="receivePaymentActionForm"
	property="paymentReceived" value="true">
	<html:form action="/billing/CA/BC/receivePaymentAction">
		<html:hidden property="billingmasterNo" />
		<html:hidden property="billNo" />

		<fieldset><legend> <bean:message
			key="oscar.billing.CA.BC.title" /> </legend>
		<div class="msgDisplay">
		<p><bean:message key="oscar.billing.CA.BC.invoice" /> <bean:write
			name="receivePaymentActionForm" property="billNo" /></p>
		<p><bean:message key="oscar.billing.CA.BC.lineNo" /> <bean:write
			name="receivePaymentActionForm" property="billingmasterNo" /></p>
		</div>
		<p><label> <bean:message key="oscar.billing.CA.BC.amount" />
		<html:text maxlength="6" property="amountReceived" /><!--&nbsp;<html:checkbox property="isRefund" value="true"/>-->
		</label></p>
		<p><label> <bean:message key="oscar.billing.CA.BC.method" />
		<bean:define id="paymentMethodList" scope="request"
			name="receivePaymentActionForm" property="paymentMethodList" /> <html:select
			property="paymentMethod">
			<html:options name="receivePaymentActionForm"
				collection="paymentMethodList" property="id"
				labelProperty="paymentType" />
		</html:select> </label></p>
		<p><input type="submit" value="Submit" /></p>
		</fieldset>
	</html:form>
</logic:notEqual>
</body>
</html>
