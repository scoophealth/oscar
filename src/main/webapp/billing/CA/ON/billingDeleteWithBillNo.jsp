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
<%
  if(session.getAttribute("user") == null)
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

%>
<%@ page import="java.sql.*, java.util.*,oscar.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    
    function start(){
      this.focus();
    }
    function closeit() {
     
    }   
    
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
   	String billCode = " ";
   	String apptNo = request.getParameter("appointment_no");
   	ResultSet rsprovider = null;  
	// String proNO = request.getParameter("xml_provider");
	String billNo ="";
  	rsprovider = null;
  	
  	billNo = request.getParameter("billNo_old");
  	billCode = request.getParameter("billStatus_old");  	
  	if(billNo==null || billNo.equals("")) {
 	rsprovider = apptMainBean.queryResults(apptNo, "search_bill_beforedelete");
	 	while(rsprovider.next()){
	 	billCode = rsprovider.getString("status");
	 	billNo = rsprovider.getString("billing_no");
	 	}
  	}
  	
   	if (billCode.substring(0,1).compareTo("B") == 0) {
   %>
<p>
<h1>Sorry, cannot delete billed items.</h1>
</p>
<form><input type="button" value="Back to previous page"
	onClick="window.close()"></form>
<% }
   else{
     
   
  int rowsAffected=0;
  OscarProperties props = OscarProperties.getInstance();
  if(props.getProperty("isNewONbilling", "").equals("true")) {
	  //search bill status
	  BillingCorrectionPrep dbObj = new BillingCorrectionPrep();
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
	  
  } else {
	  rowsAffected = apptMainBean.queryExecuteUpdate(billNo,"delete_bill");
  }   
 
  if (rowsAffected ==1) {
	  //still need to be able to edit the billing, so don't need to change appointment's status here.
	  //So comment out all 6 lines below
	  
    // oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
	//String unbillStatus = as.unbillStatus(request.getParameter("billStatus_old"));
	//String[] param1 =new String[2];
	//  param1[0]=unbillStatus;
	//  param1[1]=request.getParameter("appointment_no");
	//rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
  
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
  
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
