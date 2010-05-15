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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>



<%
if(session.getValue("user") == null) response.sendRedirect("../../../logout.htm");
String curUser_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
String content = (String) session.getAttribute("content");
session.setAttribute("content", ""); 
%>

<%@ page
	import="java.sql.*, java.util.*,java.net.*, oscar.util.*, oscar.oscarBilling.ca.on.data.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="../../../admin/dbconnection.jsp"%>
<%@ include file="dbBilling.jsp"%>

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
param[7] = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd");
param[8] = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
//param[7]=request.getParameter("billing_date");
//param[8]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("billing_time"));
param[9]=request.getParameter("appointment_date");
param[10]=request.getParameter("start_time");
param[11]=request.getParameter("clinic_ref_code");
param[12]=content; //request.getParameter("content");
param[13]=request.getParameter("total");
param[14]=request.getParameter("billtype");
param[15]=request.getParameter("demographic_dob"); 
param[16]=request.getParameter("visitdate"); 
param[17]=request.getParameter("visittype"); 
param[18]=request.getParameter("pohip_no"); 
param[19]=request.getParameter("prma_no"); 
param[20]=request.getParameter("apptProvider_no"); 
param[21]=request.getParameter("asstProvider_no"); 
param[22] = curUser_no;//request.getParameter("user_no"); 

int nBillNo = 0;
int nBillDetailNo = 0;
BillingONDataHelp billObj = new BillingONDataHelp();
//String sql = "insert into billing values('\\N',?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)";
String sql = "insert into billing(clinic_no, demographic_no, provider_no, appointment_no, organization_spec_code, demographic_name, hin, update_date, update_time, billing_date, billing_time, clinic_ref_code, content, total, status, dob, visitdate, visittype, provider_ohip_no, provider_rma_no, apptProvider_no, asstProvider_no, creator) values( " 
	+ param[0] + "," + param[1] + "," + UtilMisc.nullMySQLEscape(param[2]) + "," + param[3] + "," + UtilMisc.nullMySQLEscape(param[4]) + ","
	+ UtilMisc.nullMySQLEscape(param[5]) + "," + UtilMisc.nullMySQLEscape(param[6]) + "," + UtilMisc.nullMySQLEscape(param[7]) + "," + UtilMisc.nullMySQLEscape(param[8]) + "," + UtilMisc.nullMySQLEscape(param[9]) + ","
	+ UtilMisc.nullMySQLEscape(param[10]) + "," + UtilMisc.nullMySQLEscape(param[11]) + "," + UtilMisc.nullMySQLEscape(param[12]) + "," + UtilMisc.nullMySQLEscape(param[13]) + "," + UtilMisc.nullMySQLEscape(param[14]) + ","
	+ UtilMisc.nullMySQLEscape(param[15]) + "," + UtilMisc.nullMySQLEscape(param[16]) + "," + UtilMisc.nullMySQLEscape(param[17]) + "," + UtilMisc.nullMySQLEscape(param[18]) + "," + UtilMisc.nullMySQLEscape(param[19]) + ","
	+ UtilMisc.nullMySQLEscape(param[20]) + "," + UtilMisc.nullMySQLEscape(param[21]) + ",'" + param[22] + "')";
nBillNo = billObj.saveBillingRecord(sql);
//int rowsAffected = apptMainBean.queryExecuteUpdate(param,request.getParameter("dboperation"));
	     
//String billNo = null;
//String[] param4 = new String[2];
//param4[0] = request.getParameter("demographic_no");
//param4[1] = request.getParameter("appointment_no");

//if (rowsAffected == 1) {
if (nBillNo > 0) {
	//ResultSet rsdemo = apptMainBean.queryResults(param4, "search_billing_no_by_appt");
	//while (rsdemo.next()) {   
	//	billNo = rsdemo.getString("billing_no");
	//}
	   
	//int recordAffected=0;
	int recordCount = Integer.parseInt(request.getParameter("record"));
	for (int i=0; i<recordCount; i++){
		String[] param2 = new String[8];
		param2[0] = "" + nBillNo; // billNo;
		param2[1] = request.getParameter("billrec"+i);
		param2[2] = request.getParameter("billrecdesc"+i);
		param2[3] = request.getParameter("pricerec"+i);
		param2[4] = request.getParameter("diagcode");
		param2[5] = request.getParameter("appointment_date");
		param2[6] = request.getParameter("billtype");
		param2[7] = request.getParameter("billrecunit"+i);
 
		//insert into billingdetail values('\\N',?,?,?,?,?, ?,?,?)
    	sql = "insert into billingdetail(billing_no, service_code, service_desc, billing_amount, diagnostic_code, appointment_date, status, billingunit) values( " 
    	+ param2[0] + "," + UtilMisc.nullMySQLEscape(param2[1]) + "," + UtilMisc.nullMySQLEscape(param2[2]) + "," + UtilMisc.nullMySQLEscape(param2[3]) + "," + UtilMisc.nullMySQLEscape(param2[4]) + ","
    	+ UtilMisc.nullMySQLEscape(param2[5]) + "," + UtilMisc.nullMySQLEscape(param2[6]) + "," + UtilMisc.nullMySQLEscape(param2[7]) + ")";
		nBillDetailNo = 0;
    	nBillDetailNo = billObj.saveBillingRecord(sql);
    	if (nBillDetailNo == 0) {
    		// roll back
    		sql = "update billing set status='D' where billing_no = " + nBillNo;
			billObj.updateDBRecord(sql);
    		break;
    	}
    	//System.out.println(nBillNo + sql);
		//recordAffected = apptMainBean.queryExecuteUpdate(param2,"save_bill_record");
	}

//    if (rowsAffected ==1) {
    if (nBillDetailNo > 0) {
        ResultSet rsdemo = apptMainBean.queryResults(request.getParameter("appointment_no"), "searchapptstatus");
        String apptCurStatus = rsdemo.next()?rsdemo.getString("status"):"T";

        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String billStatus = as.billStatus(apptCurStatus);
        String[] param1 =new String[2];
	    param1[0]=billStatus;
	    param1[1]=request.getParameter("appointment_no");

        int rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
        //rsdemo = null;
        rsdemo.close();
        rsdemo = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_billing_no");
        while (rsdemo.next()) {    
%>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
	if (self.opener.document.caseManagementEntryForm) 
		self.opener.document.caseManagementEntryForm.elements["caseNote.billing_code"].value="<%=nBillNo%>";
	self.close();
	if (!self.opener.document.caseManagementEntryForm) self.opener.refresh();
</script> <%
            break; //get only one billing_no
        }//end of while
		apptMainBean.closePstmtConn();
    }  else {
%>
<p>
<h1>Sorry, billing has failed. Please do it again!</h1>
</p>
<%  
    }
}  else {
%>
<p>
<h1>Sorry, billing has failed. Please do it again!</h1>
</p>
<%  
}
%>
<p></p>
<hr width="90%"></hr>
<input type="button" value="Close this window" onClick="window.close()">

</center>
</body>
</html>
