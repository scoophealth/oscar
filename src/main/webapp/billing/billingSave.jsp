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
<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
%>
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

<%@ include file="dbBilling.jspf"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
%>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
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
Billing b = new Billing();
b.setClinicNo(Integer.parseInt(request.getParameter("clinic_no")));
b.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
b.setProviderNo(request.getParameter("provider_no"));
b.setAppointmentNo(Integer.parseInt(request.getParameter("appointment_no")));
b.setOrganizationSpecCode(request.getParameter("ohip_version"));
b.setDemographicName(request.getParameter("demographic_name"));
b.setHin(request.getParameter("hin"));
b.setUpdateDate(new java.util.Date());
b.setUpdateTime(new java.util.Date());
b.setBillingDate(MyDateFormat.getSysDate(request.getParameter("appointment_date")));
b.setBillingTime(MyDateFormat.getSysTime(request.getParameter("start_time")));
b.setClinicRefCode(request.getParameter("clinic_ref_code"));
b.setContent(request.getParameter("content"));
b.setTotal(request.getParameter("total"));
b.setStatus(request.getParameter("billtype"));
b.setDob(request.getParameter("demographic_dob"));
b.setVisitDate(MyDateFormat.getSysDate(request.getParameter("visitdate")));
b.setVisitType(request.getParameter("visittype"));
b.setProviderOhipNo(request.getParameter("pohip_no"));
b.setProviderRmaNo(request.getParameter("prma_no"));
b.setApptProviderNo(request.getParameter("apptProvider_no"));
b.setAsstProviderNo(request.getParameter("asstProvider_no"));
b.setCreator(request.getParameter("user_no"));
billingDao.persist(b);
int rowsAffected=1;

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

    	   BillingDetail bd = new BillingDetail();
    	   bd.setBillingNo(Integer.parseInt(billNo));
    	   bd.setServiceCode(request.getParameter("billrec"+i));
    	   bd.setServiceDesc(request.getParameter("billrecdesc"+i));
    	   bd.setBillingAmount(request.getParameter("pricerec"+i));
    	   bd.setDiagnosticCode(request.getParameter("diagcode"));
    	   bd.setAppointmentDate(oscar.MyDateFormat.getSysDate(request.getParameter("appointment_date")));
    	   bd.setStatus(request.getParameter("billtype"));
    	   bd.setBillingUnit(request.getParameter("billrecunit"+i));
    	   billingDetailDao.persist(bd);
    }

//	  int[] demo_no = new int[1]; demo_no[0]=Integer.parseInt(request.getParameter("demographic_no")); int rowsAffected = apptMainBean.queryExecuteUpdate(demo_no,param,request.getParameter("dboperation"));

    if (rowsAffected ==1) {
    //change the status to billed {"updateapptstatus", "update appointment set status=? where appointment_no=? //provider_no=? and appointment_date=? and start_time=?"},
        rsdemo = apptMainBean.queryResults(request.getParameter("appointment_no"), "searchapptstatus");
        String apptCurStatus = rsdemo.next()?rsdemo.getString("status"):"T";

        oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
        String billStatus = as.billStatus(apptCurStatus);
        String[] param1 =new String[3];
	    param1[0]=billStatus;
	    param1[1]=(String)session.getAttribute("user");
	    param1[2]=request.getParameter("appointment_no");
//	  param1[1]=request.getParameter("apptProvider_no"); param1[2]=request.getParameter("appointment_date"); param1[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
		Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
    	appointmentArchiveDao.archiveAppointment(appt);
        rowsAffected = apptMainBean.queryExecuteUpdate(param1,"updateapptstatus");
        rsdemo = null;
        rsdemo = apptMainBean.queryResults(request.getParameter("demographic_no"), "search_billing_no");
        while (rsdemo.next()) {
%>
<p>
<h1>Successful Addition of a billing Record.</h1>

<%
// not sure what this variable is suppose to be, this page has never compiled since 2004 when some one did a faulty merge
int nBillNo=0;
%>
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

<%
    }
%>
<p></p>
<hr width="90%">
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
