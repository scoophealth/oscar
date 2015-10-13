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
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingInr" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingInrDao" %>
<%
	BillingInrDao billingInrDao= SpringUtils.getBean(BillingInrDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%
BillingInr bi = new BillingInr();
bi.setDemographicNo(Integer.parseInt(request.getParameter("demoid").trim()));
bi.setDemographicName(request.getParameter("demo_name"));
bi.setHin(request.getParameter("demo_hin"));
bi.setDob(request.getParameter("demo_dob"));
bi.setProviderNo(request.getParameter("provider_no"));
bi.setProviderOhipNo(request.getParameter("provider_ohip_no"));
bi.setProviderRmaNo(request.getParameter("provider_rma_no"));
bi.setCreator(request.getParameter("doccreator"));
bi.setDiagnosticCode(request.getParameter("diag_code"));
bi.setServiceCode(request.getParameter("service_code"));
bi.setServiceDesc(request.getParameter("service_desc"));
bi.setBillingAmount(request.getParameter("service_amount"));
bi.setBillingUnit(request.getParameter("service_unit"));
bi.setCreateDateTime(new java.util.Date());
bi.setStatus("N");

billingInrDao.persist(bi);
int rowsAffected = 1;

if (rowsAffected ==1) {
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script> <%
}  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%
}
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
