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
 String BillLocationNo="";
 String BillDate="";
 String Provider="";
 String BillType="";
 String BillTotal="";
 String visitdate="";
 String visittype="";
 String BillDTNo="";
 String HCTYPE="";
 String HCSex="";
 String r_doctor_ohip="";
 String r_doctor="";
 String m_review="";
  String specialty="";
 String r_status="";
  String roster_status="";
 int rowCount = 0;
 int rowReCount = 0;
  ResultSet rslocation = null;
 ResultSet rsPatient = null;

%>
<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>


<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>
<%
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>oscarBilling Correction</title>
<script language="JavaScript">
<!--
		function setfocus() {
		  document.form1.billing_no.focus();
		  document.form1.billing_no.select();
		}
		function rs(n,u,w,h,x) {
		  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
		  remote=window.open(u,n,args);
		  if (remote != null) {
		    if (remote.opener == null)
		      remote.opener = self;
		  }
	 	  if (x == 1) { return remote; }
		}



	 	var awnd=null;
		function ScriptAttach() {
		  f0 = escape(document.serviceform.xml_diagnostic_detail.value);
		  f1 = document.serviceform.xml_dig_search1.value;
		 // f2 = escape(document.serviceform.elements["File2Data"].value);
		 // fname = escape(document.Compose.elements["FName"].value);
		  awnd=rs('att','billingDigSearch.jsp?name='+f0 + '&search=' + f1,600,600,1);
		  awnd.focus();
}
        //-->

        function validateNum(el){
   var val = el.value;
   var tval = ""+val;
   if (isNaN(val)){
      alert("Item value must be numeric.");
      el.select();
      el.focus();
      return false;
   }
   if ( val >= 99999.99 ){
     alert("Item value must be below $100000");
     el.select();
     el.focus();
     return false;
   }
   decLen = tval.indexOf(".");
   if (decLen != -1  &&   ( tval.length - decLen ) > 3  ){
      alert("Item value has a maximum of 2 decimal places");
      el.select();
      el.focus();
      return false;
   }
   return true;
}
    </script>
</head>



<body bgcolor="#FFFFFF" text="#000000" topmargin="5" leftmargin="0"
	rightmargin="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing - Correction</font></font></b></font></p>
		</td>
	</tr>
</table>
<% int bFlag = 0;	String billNo = request.getParameter("billing_no");         if (billNo.compareTo("") == 0 || billNo == null) {         bFlag = 0;         } else {         bFlag =1;         %>
<%@ include file="billingDataRetrieve.jspf"%>
<% } %>
<table width="100%" border="0" bgcolor="#FFFFFF">
	<form name="form1" method="post" action="billingCorrection.jsp">
	<tr>
		<td width="20%" align="left"><b><font
			face="Arial, Helvetica, sans-serif" size="2" color="#000000">Enter
		Invoice No </font></b></td>
		<td width="20%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="text" name="billing_no"
			value="<%=billNo%>" maxsize="10"></td>
		<td width="60%" align="left"><b><font
			face="Arial, Helvetica, sans-serif" size="2" color="#000000"><b>Last
		update: <%=UpdateDate%></font></b></td>
	</tr>
	</form>
</table>

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


<br>
<form name="serviceform" method="post"
	action="billingCorrectionValid.jsp"><input type="hidden"
	name="xml_billing_no" value="<%=billNo%>"><input type="hidden"
	name="update_date" value="<%=UpdateDate%>">
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td height="21" colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Patient
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Patient Name: <%=DemoName%> <input type="hidden"
			name="demo_name" value="<%=DemoName%>"> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Health# : <input type="text" style="font-size: 80%;"
			name="hin" value="<%=hin%>"> <input type="hidden"
			name="xml_hin" value="<%=hin%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b>Sex:
		<%=DemoSex%> <input type="hidden" name="demo_sex" value="<%=DemoSex%>">
		<input type="hidden" name="hc_sex" value="<%=HCSex%>"> </b></font></td>
		<td><font size="2"><b><font
			face="Arial, Helvetica, sans-serif">D.O.B. : <input
			type="hidden" name="xml_dob" value="<%=DemoDOB%>"> <input
			type="text" style="font-size: 80%;" name="dob" value="<%=DemoDOB%>">
		</font></b></font></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Address:
		<%=DemoAddress%> <input type="hidden" name="demo_address"
			value="<%=DemoAddress%>"> </font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">City:
		<%=DemoCity%> <input type="hidden" name="demo_city"
			value="<%=DemoCity%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Province:
		<%=DemoProvince%> <input type="hidden" name="demo_province"
			value="<%=DemoProvince%>"> </font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Postal
		Code: <%=DemoPostal%> <input type="hidden" name="demo_postal"
			value="<%=DemoPostal%>"> </font></b></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><strong>Referral
		Doctor: <%=r_doctor%> <input type="hidden" name="rd"
			value="<%=r_doctor%>"></strong></font></td>
		<td><font size="2" face="Arial, Helvetica, sans-serif"><strong>Referral
		Doctor #: <%=r_doctor_ohip%> <input type="hidden" name="rdohip"
			value="<%=r_doctor_ohip%>"></strong></font></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td width="320"><font face="Arial, Helvetica, sans-serif"><strong>Additional
		Information</strong></font></td>
		<td width="270"><font face="Arial, Helvetica, sans-serif"><strong>Specialty
		</strong></font> <select name="specialty" style="font-size: 80%;">
			<option value="none">None</option>
			<option value="flu" <%=specialty.equals("flu")?"selected":""%>>FLU</option></td>

	</tr>
	<tr>
		<td width="320"><strong><font size="2"
			face="Arial, Helvetica, sans-serif">HC-Type: <select
			name="hc_type" style="font-size: 80%;">
			<option value="ON" <%=HCTYPE.equals("ON")?"selected":""%>>ON-Ontario</option>
			<option value="AB" <%=HCTYPE.equals("AB")?"selected":""%>>AB-Alberta</option>
			<option value="BC" <%=HCTYPE.equals("BC")?"selected":""%>>BC-British
			Columbia</option>
			<option value="MB" <%=HCTYPE.equals("MB")?"selected":""%>>MB-Manitoba</option>
			<option value="NF" <%=HCTYPE.equals("NF")?"selected":""%>>NF-Newfoundland</option>
			<option value="NB" <%=HCTYPE.equals("NB")?"selected":""%>>NB-New
			Brunswick</option>
			<option value="YT" <%=HCTYPE.equals("YT")?"selected":""%>>YT-Yukon</option>
			<option value="NS" <%=HCTYPE.equals("NS")?"selected":""%>>NS-Nova
			Scotia</option>
			<option value="PE" <%=HCTYPE.equals("PE")?"selected":""%>>PE-Prince
			Edward Island</option>
			<option value="SK" <%=HCTYPE.equals("SK")?"selected":""%>>SK-Saskatchewan</option>
		</select> </font></strong></td>
		<td width="270"><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Manual Review: <input
			type="checkbox" name="m_review" value="checkbox"
			<%=m_review.equals("checked")?"checked":""%>> </font></strong></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Referral Doctor: <input
			type="checkbox" name="referral" value="checkbox"
			<%=r_status.equals("checked")?"checked":""%>> </font></strong></td>
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Roster Status: <input
			type="hidden" name="roster" value="<%=roster_status%>"><%=roster_status%>
		</font></strong></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Billing
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Type: <input type="hidden" name="xml_status"
			value="<%=BillType%>"> <select style="font-size: 80%;"
			name="status">
			<option value="">--- Select Bill Type ---</option>
			<option value="H" <%=BillType.equals("H")?"selected":""%>>H
			| Capitated</option>
			<option value="O" <%=BillType.equals("O")?"selected":""%>>O
			| Bill OHIP</option>
			<option value="P" <%=BillType.equals("P")?"selected":""%>>P
			| Bill Patient</option>
			<option value="N" <%=BillType.equals("N")?"selected":""%>>N
			| Do Not Bill</option>
			<option value="W" <%=BillType.equals("W")?"selected":""%>>W
			| Bill Worker's Compensation Board</option>
			<option value="B" <%=BillType.equals("B")?"selected":""%>>B
			| Submitted OHIP</option>
			<option value="S" <%=BillType.equals("S")?"selected":""%>>S
			| Settled/Paid by OHIP</option>
			<option value="X" <%=BillType.equals("X")?"selected":""%>>X
			| Bad Debt</option>
			<option value="D" <%=BillType.equals("D")?"selected":""%>>D
			| Deleted Bill</option>

		</select> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><a href="#"
			onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=service","380","300","0")'>Billing
		Date: </a><input type="text" style="font-size: 80%;"
			name="xml_appointment_date" value="<%=BillDate%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit: <input type="hidden"
			name="xml_clinic_ref_code" value="<%=location%>"><select
			style="font-size: 80%;" name="clinic_ref_code">
			<option value="">--- Select Visit Location ---</option>
			<%  rslocation = null;
			List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
            for(ClinicLocation clinicLocation:clinicLocations) {
           	 	BillLocation = clinicLocation.getClinicLocationName();
           	 	BillLocationNo = clinicLocation.getClinicLocationNo();
 %>
			<option value="<%=BillLocationNo%>"
				<%=location.equals(BillLocationNo)?"selected":""%>><%=BillLocationNo%>
			| <%=BillLocation%></option>

			<% } %>
		</font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Physician#: <select style="font-size: 80%;"
			name="provider_no">
			<option value="">--- Select Provider ---</option>
			<%
// Retrieving Provider
String proFirst="", proLast="", proOHIP="", proNo="";
 int Count = 0;
 for(Provider p:providerDao.getActiveProviders()) {
	 if(p.getOhipNo() != null && !p.getOhipNo().isEmpty()) {

 proFirst = p.getFirstName();
 proLast = p.getLastName();
 proOHIP = p.getProviderNo();

%>
			<option value="<%=proOHIP%>"
				<%=Provider.equals(proOHIP)?"selected":""%>><%=proOHIP%> |
			<%=proLast%>, <%=proFirst%></option>
			<% } }


  %><input type="hidden" name="xml_provider_no" value="<%=Provider%>"></font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Type: <input type="hidden" name="xml_visittype"
			value="<%=visittype%>"> <select style="font-size: 80%;"
			name="visittype">
			<option value="">--- Select Visit Type ---</option>
			<option value="00" <%=visittype.equals("00")?"selected":""%>>00
			| Clinic Visit</option>
			<option value="01" <%=visittype.equals("01")?"selected":""%>>01
			| Outpatient Visit</option>
			<option value="02" <%=visittype.equals("02")?"selected":""%>>02
			| Hospital Visit</option>
			<option value="03" <%=visittype.equals("03")?"selected":""%>>03
			| ER</option>
			<option value="04" <%=visittype.equals("04")?"selected":""%>>04
			| Nursing Home</option>
			<option value="05" <%=visittype.equals("05")?"selected":""%>>05
			| Home Visit</option>
		</select></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="hidden" name="xml_visitdate"
			value="<%=visitdate%>"><a href="#"
			onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=admission","380","300","0")'>
		Admission Date:</a> <input type="text" style="font-size: 80%;"
			name="xml_vdate" value="<%=visitdate%>"></font></b></td>
	</tr>
</table>

<table width="600">
	<tr bgcolor="#CCCCFF">
		<td width="25%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Service Code</font></b></td>

		<td width="50%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Description</font></b></td>

		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Unit</font></b></td>

		<td width="13%">
		<div align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2">$ Fee</font></b></div>
		</td>
	</tr>
	<%
    String serviceCode = "";
    String serviceDesc = "";
    String billAmount = "";
    String diagCode = "";
    String billingunit="";
   
     List<BillingDetail> bds = billingDetailDao.findByBillingNo(Integer.parseInt(billNo));
 	for(BillingDetail bd:bds) {
 		if(bd.getStatus().equals("D")) continue;

 serviceCode = bd.getServiceCode();
 serviceDesc = bd.getServiceDesc();
 billAmount =bd.getBillingAmount();
 diagCode = bd.getDiagnosticCode();
 billingunit = bd.getBillingUnit();
 rowCount = rowCount + 1;
 %>

	<tr>
		<td width="25%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="hidden"
			name="xml_service_code<%=rowCount%>" value="<%=serviceCode%>"><input
			type="text" style="font-size: 80%;" name="servicecode<%=rowCount-1%>"
			value="<%=serviceCode%>"></font></td>

		<td width="50%"><font face="Arial, Helvetica, sans-serif"
			size="1"><%=serviceDesc%></font></td>
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="hidden"
			name="xml_billing_unit<%=rowCount%>" value="<%=billingunit%>"><input
			type="text" style="font-size: 80%;" name="billingunit<%=rowCount-1%>"
			value="<%=billingunit%>" size="5" maxlength="5"></font></td>
		<td width="13%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="hidden"
			name="xml_billing_amount<%=rowCount%>"
			value="<%=billAmount.substring(0,billAmount.length()-2) + "." + billAmount.substring(billAmount.length()-2)%>">
		<input type="text" style="font-size: 80%;" size="8" maxlength="8"
			name="billingamount<%=rowCount-1%>"
			value="<%=billAmount.substring(0,billAmount.length()-2) + "." + billAmount.substring(billAmount.length()-2)%>"
			onchange="javascript:validateNum(this)"> </font></div>
		</td>
	</tr>
	<%
}
%>
	<% if (rowCount < 5) { %>
	<% for (int i=rowCount; i<5; i++){ %>
	<tr>
		<td width="25%"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="text" style="font-size: 80%;"
			name="servicecode<%=i%>" value=""></font></td>
		<td width="50%"><font face="Arial, Helvetica, sans-serif"
			size="2">&nbsp;</font></td>
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="text" style="font-size: 80%;"
			name="billingunit<%=i%>" value="" size="5" maxlength="5"> </font></td>
		<td width="13%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="text" style="font-size: 80%;"
			name="billingamount<%=i%>" value="" size="8" maxlength="8"
			onchange="javascript:validateNum(this)"> </font></div>
		</td>
	</tr>
	<% }
} %>


	<%

 String diagDesc = "";
   ResultSet rsDiagCode = null;
     rsDiagCode = null;
 	List<DiagnosticCode> results = diagnosticCodeDao.searchCode(diagCode);
 	for(DiagnosticCode result:results) {
 		diagDesc = result.getDescription();
 	}
    %>


	<tr bgcolor="#CCCCFF">
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><b>Diagnostic Code</b></font></td>

	</tr>
	<tr>
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="hidden" name="xml_diagnostic_code"
			value="<%=diagCode%>"><input type="text"
			style="font-size: 80%;" name="xml_diagnostic_detail"
			value="<%=diagCode%>" size="50"><input type="hidden"
			name="xml_dig_search1"><a href="javascript:ScriptAttach()">DX
		Search</a></font></td>

	</tr>
	<tr>
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><input type="submit" name="submit" value="Submit"></font></td>
	</tr>
</table>
<form>
</body>
</html>
