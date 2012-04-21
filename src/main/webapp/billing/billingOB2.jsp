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
    response.sendRedirect("../logout.htm");
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

%>
<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing History</title>
</head>
<% String billNo = request.getParameter("billing_no");
 ResultSet rslocation = null;
 ResultSet rsPatient = null;
 String proFirst="", proLast="", proRMA="", proOHIPNO="", crFirst="Not", crLast="Available", apptFirst="Not", apptLast="Available", asstFirst="Not", asstLast="Available";
 String UpdateDate = "";
 String DemoNo = "";
 String DemoName = "";
 String DemoAddress = "";
 String DemoCity="";
 String DemoProvince="";
 String DemoPostal="";
 String DemoDOB="";
 String DemoSex="";
 String hin="";
 String location="";
 String BillLocation="";
 String BillDate="";
 String proNO="";
 String asstProvider_no="";
  String apptProvider_no="";
 String BillType="";
 String BillTotal="";
 String visitdate="";
 String visittype="";
 String creator = "";
 String creatdate = "";
 rslocation = null;
 rslocation = apptMainBean.queryResults(billNo, "search_bill");
 while(rslocation.next()){
 DemoNo = rslocation.getString("demographic_no");
 DemoName = rslocation.getString("demographic_name");
 UpdateDate = rslocation.getString("update_date");
 hin = rslocation.getString("hin");
 location = rslocation.getString("clinic_ref_code");
 // BillDate = rslocation.getString("billing_date");
 BillType = rslocation.getString("status");
 proNO= rslocation.getString("provider_no");
  BillTotal = rslocation.getString("total");
  visitdate = rslocation.getString("visitdate");
  visittype = rslocation.getString("visittype");
  apptProvider_no = rslocation.getString("apptProvider_no");
    asstProvider_no = rslocation.getString("asstProvider_no");
    creator = rslocation.getString("creator");
   }


 ClinicLocation clinicLocation = clinicLocationDao.searchBillLocation(1,location);
 if(clinicLocation!=null) {
	 BillLocation = clinicLocation.getClinicLocationName();
 }

 rsPatient = null;
 rsPatient = apptMainBean.queryResults(DemoNo, "search_demographic_details");
 while(rsPatient.next()){
 DemoSex = rsPatient.getString("sex");
 DemoAddress = rsPatient.getString("address");
 DemoCity = rsPatient.getString("city");
 DemoProvince = rsPatient.getString("province");
 DemoPostal = rsPatient.getString("postal");
 DemoDOB = rsPatient.getString("year_of_birth") + "-" + rsPatient.getString("month_of_birth") + "-" + rsPatient.getString("date_of_birth");

   }

   ResultSet rsprovider = null;


     rsprovider = null;
    rsprovider = apptMainBean.queryResults(proNO, "search_provider_name");
    while(rsprovider.next()){
    proFirst = rsprovider.getString("first_name");
    proLast = rsprovider.getString("last_name");
     proOHIPNO = rsprovider.getString("ohip_no");
    proRMA = rsprovider.getString("rma_no");
     }
       rsprovider = null;
      rsprovider = apptMainBean.queryResults(apptProvider_no, "search_provider_name");
      while(rsprovider.next()){
     apptFirst = rsprovider.getString("first_name");
     apptLast = rsprovider.getString("last_name");

     }
       rsprovider = null;
      rsprovider = apptMainBean.queryResults(asstProvider_no, "search_provider_name");
      while(rsprovider.next()){
    asstFirst = rsprovider.getString("first_name");
     asstLast = rsprovider.getString("last_name");
  }

         rsprovider = null;
        rsprovider = apptMainBean.queryResults(creator, "search_provider_name");
        while(rsprovider.next()){
   crFirst = rsprovider.getString("first_name");
       crLast = rsprovider.getString("last_name");
  }
       ResultSet rsBillRec2 = null;
     rsBillRec2 = null;
 rsBillRec2 = apptMainBean.queryResults(billNo, "search_bill_record");
 while(rsBillRec2.next()){
 BillDate = rsBillRec2.getString("appointment_date");
}
 %>


<body bgcolor="#FFFFFF" text="#000000">
<SCRIPT Language="Javascript">
function printBill(){
if (window.print) {
    window.print() ;
} else {
    var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
document.body.insertAdjacentHTML('beforeEnd', WebBrowser);
    WebBrowser1.ExecWB(6, 2);//Use a 1 vs. a 2 for a prompting dialog box    WebBrowser1.outerHTML = "";
}
}
</script>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF">Biiling Summary </font></th>
		<th align='RIGHT'><input type='button' name='close' value='Done'
			onClick='window.close()'></th>
	</tr>
</table>
<br>
<table width="600" border="1">
	<tr bgcolor="#CCCCCC">
		<td colspan="2" height="21"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Patient
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Patient Name: <%=DemoName%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Health# : <%=hin%></font></b></td>
	</tr>
	<tr>
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b>Sex:
		<%=DemoSex%></b></font></td>
		<td><font size="2"><b><font
			face="Arial, Helvetica, sans-serif">D.O.B. : <%=DemoDOB%></font></b></font></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Address:
		<%=DemoAddress%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">City:
		<%=DemoCity%></font></b></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Province:
		<%=DemoProvince%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Postal
		Code: <%=DemoPostal%></font></b></td>
	</tr>
</table>
<table width="600" border="1">
	<tr bgcolor="#CCCCCC">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Billing
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Type: <%=BillType%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Date: <%=BillDate%></font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Type: <%=visittype%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Date: <%=visitdate%></font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Location: <%=BillLocation%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Physician#: <%=proFirst%> <%=proLast%> </font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Appointment Physician: <%=apptFirst%> <%=apptLast%> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Secordary Physician: <%=asstFirst%> <%=asstLast%> </font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Creator: <%=crFirst%> <%=crLast%> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Update Date: <%=UpdateDate%></font></b></td>
	</tr>

</table>
<table width="600" border="1">
	<tr>
		<td width="22%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Service Code</font></b></td>
		<td width="58%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Description</font></b></td>
		<td width="6%">
		<div align="right"><b><font size="2"
			face="Arial, Helvetica, sans-serif">#Unit</font></b></div>
		</td>
		<td width="14%">
		<div align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2">$ Fee</font></b></div>
		</td>
	</tr>
	<%
    String serviceCode = "";
    String serviceDesc = "";
    String billAmount = "";
    String diagCode = "";
 String billUnit="";
 ResultSet rsBillRec = null;
     rsBillRec = null;
 rsBillRec = apptMainBean.queryResults(billNo, "search_bill_record");
 while(rsBillRec.next()){
billUnit = rsBillRec.getString("billingunit");
 serviceCode = rsBillRec.getString("service_code");
 serviceDesc = rsBillRec.getString("service_desc");
 billAmount = rsBillRec.getString("billing_amount");
 diagCode = rsBillRec.getString("diagnostic_code");


 %>

	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=serviceCode%></font></td>

		<td width="58%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=serviceDesc%></font></td>
		<td width="6%">
		<div align="right"><font size="2"
			face="Arial, Helvetica, sans-serif"><%=billUnit%></font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=billAmount.substring(0,billAmount.length()-2) + "." + billAmount.substring(billAmount.length()-2)%></font></div>
		</td>
	</tr>
	<%
}
 String diagDesc = "";
   ResultSet rsDiagCode = null;
     rsDiagCode = null;
 	List<DiagnosticCode> results = diagnosticCodeDao.searchCode(diagCode);
 	for(DiagnosticCode result:results) {
 		diagDesc = result.getDescription();
 	}

    %>

	<tr bgcolor="#CCCCCC">
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><b>Diagnostic Code</b></font></td>

	</tr>
	<tr>
		<td width="22%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=diagCode%></font></td>
		<td colspan="3">
		<div align="left"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=diagDesc%></font></div>
		</td>

	</tr>
	<tr>
		<td width="22%">&nbsp;</td>
		<td colspan="2">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2">Total: </font></div>
		</td>
		<td width="14%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=BillTotal%></font></div>
		</td>
	</tr>
</table>
<p></p>
<p></p>
<p><SCRIPT Language="Javascript">
var NS = (navigator.appName == "Netscape");
var VERSION = parseInt(navigator.appVersion);
if (VERSION > 3) {
    document.write('<form><input type=button value="Print Billing History" name="Print" onClick="printBill()"></form>');
}
</script></p>

</body>
</html>
