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
   String billCode = " ";
   String apptNo = request.getParameter("appointment_no");
   ResultSet rsprovider = null;  
// String proNO = request.getParameter("xml_provider");
String billNo ="";
  rsprovider = null;
 rsprovider = apptMainBean.queryResults(apptNo, "search_bill_beforedelete");
 while(rsprovider.next()){
 billCode = rsprovider.getString("status");
 billNo = rsprovider.getString("billing_no");
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
	  List billStatus = dbObj.getBillingNoStatusByAppt(apptNo);
	  //delete the bill
	  if(billStatus!=null && ((billStatus.size() == 0) || (billStatus.size()>1 && ((String)billStatus.get(billStatus.size()-1)).startsWith("B")))){
		  out.println("Sorry, cannot delete billed items.");
	  } else if(billStatus!=null) {
                for( int idx = 0; idx < billStatus.size(); idx += 2) {
                    if( !((String)billStatus.get(idx)).equals("D") ) {
                        rowsAffected = dbObj.deleteBilling((String)billStatus.get(idx),"D", curUser_no)? 1 : 0;
                    }
                }
	  }
	  
  } else {
	  rowsAffected = apptMainBean.queryExecuteUpdate(billNo,"delete_bill");
  }   
       
       //       }

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));
  
  if (rowsAffected ==1) {
    //apptMainBean.closePstmtConn();//change the status to billed {"updateapptstatus", "update appointment set status=? where appointment_no=? //provider_no=? and appointment_date=? and start_time=?"},
  oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
String unbillStatus = as.unbillStatus(request.getParameter("status"));
  String[] param1 =new String[2];
	  param1[0]=unbillStatus;
	  param1[1]=request.getParameter("appointment_no");
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
   rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
// rsdemo = null;
 //  rsdemo = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_billing_no");
 //  while (rsdemo.next()) {    
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
      //self.top.location = 'providercontrol.jsp?appointment_no=<%// =request.getParameter("appointment_no")%>&demographic_no=<%// =Integer.parseInt(request.getParameter("demographic_no"))%>&curProvider_no=<%// =curUser_no%>&username=<%// = userfirstname+" "+userlastname %>&appointment_date=<%// =request.getParameter("appointment_date")%>&start_time=<%// =request.getParameter("start_time")%>&status=B&displaymode=encounter&dboperation=search_demograph&template=';
    //  self.opener.document.encounter.encounterattachment.value +="<billing>../billing/billingOB2.jsp?billing_no=<%// =rsdemo.getString("billing_no")%>^dboperation=search_bill^hotclick=0</billing>"; //providercontrol.jsp?billing_no=<%// =rsdemo.getString("billing_no")%>^displaymode=vary^displaymodevariable=<%// =URLEncoder.encode("../billing/")%>billing<%// =request.getParameter("billing_name")%>.jsp^dboperation=search_bill^hotclick=0</billing>";
     // self.opener.document.encounter.attachmentdisplay.value +="Billing "; //:<%=request.getParameter("billing_name")%> ";
     	self.opener.refresh();
</script> <%
  //  break; //get only one billing_no
  //  }//end of while
 }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
  }
  //apptMainBean.closePstmtConn(); //this call does not exist
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
