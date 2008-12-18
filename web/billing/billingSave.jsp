
<%      
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>
<%--  
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
--%>



<%
    if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
    String curUser_no,userfirstname,userlastname;
    curUser_no = (String) session.getAttribute("user");
    userfirstname = (String) session.getAttribute("userfirstname");
    userlastname = (String) session.getAttribute("userlastname");
%>

<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
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
}   
//-->
</script>
</head>

<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th><font face="Helvetica" color="#FFFFFF">ADD A BILLING
		RECORD</font></th>
	</tr>
</table>

<%
    String[] param =new String[23]; 
	  param[0]=request.getParameter("clinic_no");
	  param[1]=request.getParameter("demographic_no");
	  param[2]=request.getParameter("provider_no");
	  param[3]=request.getParameter("appointment_no");
	  param[4]=request.getParameter("ohip_version");
	  param[5]=request.getParameter("demographic_name");
	  param[6]=request.getParameter("hin");
	  param[7]=request.getParameter("billing_date");
	  param[8]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("billing_time"));
	  param[9]=request.getParameter("appointment_date");
	  param[10]=request.getParameter("start_time");
	  param[11]=request.getParameter("clinic_ref_code");
	  param[12]=request.getParameter("content");
	  param[13]=request.getParameter("total");
	  param[14]=request.getParameter("billtype");
	  param[15]=request.getParameter("demographic_dob"); 
	  param[16]=request.getParameter("visitdate"); 
	  param[17]=request.getParameter("visittype"); 
	  param[18]=request.getParameter("pohip_no"); 
	  param[19]=request.getParameter("prma_no"); 
	  param[20]=request.getParameter("apptProvider_no"); 
	  param[21]=request.getParameter("asstProvider_no"); 
	  param[22]=request.getParameter("user_no"); 
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,request.getParameter("dboperation"));
	     
	String billNo = null;
	String[] param4 = new String[2];
	  param4[0] = request.getParameter("demographic_no");
	  param4[1] = request.getParameter("appointment_no");
	ResultSet rsdemo = apptMainBean.queryResults(param4, "search_billing_no_by_appt");
    while (rsdemo.next()) {   
        billNo = rsdemo.getString("billing_no");
    }
   
    int recordAffected=0;
    int recordCount = Integer.parseInt(request.getParameter("record"));
    for (int i=0;i<recordCount;i++){
        String[] param2 = new String[8];
        param2[0] = billNo;
        param2[1] = request.getParameter("billrec"+i);
        param2[2] = request.getParameter("billrecdesc"+i);
        param2[3] = request.getParameter("pricerec"+i);
        param2[4] = request.getParameter("diagcode");
        param2[5] = request.getParameter("appointment_date");
        param2[6] = request.getParameter("billtype");
        param2[7] = request.getParameter("billrecunit"+i);
       
        recordAffected = apptMainBean.queryExecuteUpdate(param2,"save_bill_record");
    }

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));
  
    if (rowsAffected ==1) {
    //apptMainBean.closePstmtConn();//change the status to billed {"updateapptstatus", "update appointment set status=? where appointment_no=? //provider_no=? and appointment_date=? and start_time=?"},
        rsdemo = apptMainBean.queryResults(request.getParameter("appointment_no"), "searchapptstatus");
        String apptCurStatus = rsdemo.next()?rsdemo.getString("status"):"T";

        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String billStatus = as.billStatus(apptCurStatus);
        String[] param1 =new String[2];
	    param1[0]=billStatus;
	    param1[1]=request.getParameter("appointment_no");
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
        rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
        rsdemo = null;
        rsdemo = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_billing_no");
        while (rsdemo.next()) {    
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
    if (self.opener.document.inputForm)
		self.opener.document.inputForm.elements["caseNote.billing_code"].value="<%=nBillNo%>";
	self.close();
	if (!self.opener.document.inputForm) self.opener.refresh();
</script> <%
            break; //get only one billing_no
        }//end of while
    }  else {
%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%  
    }
    apptMainBean.closePstmtConn();
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
