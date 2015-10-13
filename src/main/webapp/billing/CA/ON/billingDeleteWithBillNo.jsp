<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

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
<%@ page import="java.sql.*, java.util.*,oscar.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ include file="../../../admin/dbconnection.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>

<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    function start(){
      this.focus();
    }
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER">
		<font face="Helvetica" color="#FFFFFF">BILLING RECORD</font>
		</th>
	</tr>
</table>
<%
   	String billCode = " ";
   	String apptNo = request.getParameter("appointment_no");
   	String billNo ="";
   	
  	billNo = request.getParameter("billNo_old");
  	billCode = request.getParameter("billStatus_old");  	
  	if(billNo==null || billNo.equals("")  || billNo.equals("null")) {
  		for(Billing b:billingDao.findByAppointmentNo(Integer.parseInt(apptNo))) {
  		   billCode = b.getStatus();
  		   billNo = b.getId().toString();
  	   }
  	}
  	
  	
   	if (billCode.substring(0,1).compareTo("B") == 0) {
   %>

<h1>Sorry, cannot delete billed items.</h1>

<% }else{
   
  int rowsAffected=0;
  OscarProperties props = OscarProperties.getInstance();
  if(props.getProperty("isNewONbilling", "").equals("true")) {
	  //search bill status
	  BillingCorrectionPrep dbObj = new BillingCorrectionPrep();
	  if(billNo != null && !billNo.equals("null")) {
		  List billStatus = dbObj.getBillingNoStatusByBillNo(billNo);
		  //delete the bill
		  if(billStatus!=null && ((billStatus.size() == 0) || (billStatus.size()>1 && ((String)billStatus.get(billStatus.size()-1)).startsWith("B")))){
			  out.println("Sorry, cannot delete billed items.");
		  } else if(billStatus!=null) {
	                for( int idx = 0; idx < billStatus.size(); idx += 2) {
	                    if( !((String)billStatus.get(idx+1)).equals("D") ) {
	                        rowsAffected = dbObj.deleteBilling((String)billStatus.get(idx),"D", curUser_no)? 1 : 0;
	                    }
	                }
		  }
	  }
	  
  } else {
	  Billing b = billingDao.find(Integer.parseInt(billNo));
	  if(b != null) {
		  b.setStatus("D");
		  billingDao.merge(b);
		  rowsAffected=1;
	  }
  }   
 
  if (rowsAffected ==1) {
%>

<h1>Successful Deletion of a billing Record.</h1>

<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script> <%
  
  } else if(rowsAffected == 0 && billNo != null && !"null".equals(billNo)) {
%>

<h1>Sorry, deletion has failed. </h1>

<%  
  }
  
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window" onClick="window.close()"></form>
</center>
</body>
</html>
