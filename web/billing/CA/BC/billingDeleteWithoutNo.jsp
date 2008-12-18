<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
if(session.getValue("user") == null)
    response.sendRedirect(".././../logout.htm");
String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>
<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jsp"%>
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

String apptNo = request.getParameter("appointment_no");
ResultSet rsprovider = null;
// String proNO = request.getParameter("xml_provider");

rsprovider = null;
rsprovider = apptMainBean.queryResults(apptNo, "search_bill_beforedelete");
ArrayList<String> billCodeList = new ArrayList();
boolean cannotDelete = false;
while(rsprovider.next()){
    String billCode = rsprovider.getString("status");
    if (billCode.substring(0,1).equals("B")){
        cannotDelete = true;
    }
    billCodeList.add(rsprovider.getString("billing_no"));
}
if (cannotDelete) {
%>
<p>
<h1>Sorry, cannot delete billed items.</h1>
</p>
<form><input type="button" value="Back to previous page"
	onClick="window.close()"></form>
<% } else{
    
    boolean updateApptStatus = false;
    for (String billNo:billCodeList){
       int rowsAffected=0;
       rowsAffected = apptMainBean.queryExecuteUpdate(billNo,"delete_bill");
       apptMainBean.queryExecuteUpdate(billNo,"delete_bill_master");
       if (rowsAffected == 1){
          updateApptStatus = true;    
       }
    }
   
    if (updateApptStatus) {
        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String unbillStatus = as.unbillStatus(request.getParameter("status"));
        String[] param1 =new String[2];
        param1[0]=unbillStatus;
        param1[1]=request.getParameter("appointment_no");
        apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
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
    apptMainBean.closePstmtConn();
}
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>