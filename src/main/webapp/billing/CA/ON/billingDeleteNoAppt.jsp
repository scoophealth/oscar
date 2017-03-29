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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
String curUser_no = (String) session.getAttribute("user");
%>

<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
<!--
function start(){
	this.focus();
}
//-->
</script>
</head>

<body onload="start()">
<center>

<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">DELETE
		A BILLING RECORD</font></th>
	</tr>
</table>

<%
String billCode = request.getParameter("billCode");
if (billCode.substring(0,1).compareTo("B") == 0) {
%>

<h1>Sorry, cannot delete billed items.</h1>
<form><input type="button" value="Back to previous page"
	onClick="history.go(-1);return false;"></form>

<%
} else{
	int rowsAffected=0;
	OscarProperties props = OscarProperties.getInstance();
	if(props.getProperty("isNewONbilling", "").equals("true")) {
		BillingCorrectionPrep dbObj = new BillingCorrectionPrep();
		rowsAffected = dbObj.deleteBilling(request.getParameter("billing_no"),"D", curUser_no)? 1 : 0;
	} else {
		 Billing b = billingDao.find(Integer.parseInt(request.getParameter("billing_no")));
		   if(b != null) {
			   b.setStatus("D");
			   billingDao.merge(b);
		   }
	}
%>

<h1>Successful Addition of a billing Record.</h1>
<script LANGUAGE="JavaScript">
self.close();
self.opener.refresh();
</script> <%
	if(!props.getProperty("isNewONbilling", "").equals("true")) {
	}
}
%>

<p></p>
<hr width="90%"></hr>
<input type="button" value="Close this window" onClick="window.close()">

</center>
</body>
</html>
