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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
  String curUser_no = (String) session.getAttribute("user");  
%>
<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="oscar.oscarBilling.ca.bc.data.BillingmasterDAO" %>
<%@page import="oscar.entities.Billingmaster" %>

<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingmasterDAO billingMasterDao =  SpringUtils.getBean(BillingmasterDAO.class);
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
		DELETE A BILLING RECORD</font></th>
	</tr>
</table>
<%
   String billCode = request.getParameter("billCode");
   if (billCode.substring(0,1).compareTo("B") == 0) {
   %>
<p>
<h1>Sorry, cannot delete billed items.</h1>
</p>
<form><input type="button" value="Back to previous page"
	onClick="history.go(-1);return false;"></form>
<% }
   else{
   
	   Billing b = billingDao.find(Integer.parseInt(request.getParameter("billing_no")));
	   if(b != null) {
		   b.setStatus("D");
		   billingDao.merge(b);
	   }
	   
	   for(Billingmaster m: billingMasterDao.getBillingMasterByBillingNo(request.getParameter("billing_no"))) {
		   m.setBillingstatus("D");
		   billingMasterDao.update(m);
	   }
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
   	self.opener.refresh();
</script> <%
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
