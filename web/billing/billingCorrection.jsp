
<%  
if(session.getValue("user") == null) response.sendRedirect("../../../logout.htm");
String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
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

<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jsp"%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.billingCorrection.title" /></title>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
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
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<body bgcolor="#FFFFFF" text="#000000" topmargin="5" leftmargin="0"
	rightmargin="0" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<th height="40" width="10%"></th>
		<th width="90%" align="left"><font
			face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF" size="4"><bean:message
			key="billing.billingCorrection.msgCorrection" /></font></th>
	</tr>
</table>

<% 
boolean bFlag = false;	
String billNo = request.getParameter("billing_no");         
if (billNo.compareTo("") == 0 || billNo == null) {
	bFlag = false;         
} else {         
	bFlag =true;         
%>
<%@ include file="billingDataRetrieve.jsp"%>
<% } %>

<table width="100%" border="0" bgcolor="#FFFFFF">
	<form name="form1" method="post" action="billingCorrection.jsp">
	<tr>
		<th width="20%" align="left"><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message
			key="billing.billingCorrection.formInvoiceNo" /> </font></th>
		<th width="20%"><input type="text" name="billing_no"
			value="<%=billNo%>" maxsize="10"></th>
		<th width="50%" align="left"><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message
			key="billing.billingCorrection.msgLastUpdate" />: <%=UpdateDate%></font></th>
		<th><input type="submit" name="submit" value="Search">
		</td>
		</th>
	</tr>
	</form>
</table>

<form name="serviceform" method="post"
	action="billingCorrectionValid.jsp"><input type="hidden"
	name="xml_billing_no" value="<%=billNo%>"> <input type="hidden"
	name="update_date" value="<%=UpdateDate%>">

<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<th align="left" colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif" size="3"><bean:message
			key="billing.billingCorrection.msgPatientInformation" /></font></th>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgPatientName" />: <%=DemoName%> <input
			type="hidden" name="demo_name" value="<%=DemoName%>"> </font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formHealth" />: <input type="text"
			style="font-size: 80%;" name="hin" value="<%=hin%>"> <input
			type="hidden" name="xml_hin" value="<%=hin%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b><bean:message
			key="billing.billingCorrection.msgSex" />: <%=DemoSex%> <input
			type="hidden" name="demo_sex" value="<%=DemoSex%>"> <input
			type="hidden" name="hc_sex" value="<%=HCSex%>"> </b></font></td>
		<td><font size="2"><b><font
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.formDOB" />: <input type="hidden"
			name="xml_dob" value="<%=DemoDOB%>"> <input type="text"
			style="font-size: 80%;" name="dob" value="<%=DemoDOB%>"> </font></b></font></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgAddress" />: <%=DemoAddress%> <input
			type="hidden" name="demo_address" value="<%=DemoAddress%>"> </font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgCity" />: <%=DemoCity%> <input
			type="hidden" name="demo_city" value="<%=DemoCity%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgProvince" />: <%=DemoProvince%> <input
			type="hidden" name="demo_province" value="<%=DemoProvince%>">
		</font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgPostalCode" />: <%=DemoPostal%> <input
			type="hidden" name="demo_postal" value="<%=DemoPostal%>"> </font></b></td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><strong><bean:message
			key="billing.billingCorrection.msgDoctor" />: <%=r_doctor%> <input
			type="hidden" name="rd" value="<%=r_doctor%>"></strong></font></td>
		<td><font size="2" face="Arial, Helvetica, sans-serif"><strong><bean:message
			key="billing.billingCorrection.msgDoctorNo" />: <%=r_doctor_ohip%> <input
			type="hidden" name="rdohip" value="<%=r_doctor_ohip%>"></strong></font></td>
	</tr>
</table>

<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td width="320"><font face="Arial, Helvetica, sans-serif"><strong><bean:message
			key="billing.billingCorrection.msgAditInfo" /></strong></font></td>
		<td width="270"><font face="Arial, Helvetica, sans-serif"><strong><bean:message
			key="billing.billingCorrection.formSpecialty" /> </strong></font> <select
			name="specialty" style="font-size: 80%;">
			<option value="none"><bean:message
				key="billing.billingCorrection.formNone" /></option>
			<option value="flu" <%=specialty.equals("flu")?"selected":""%>>
			<bean:message key="billing.billingCorrection.formFlu" /></option></td>
	</tr>
	<tr>
		<td width="320"><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.formHCType" />:</font></strong> <select name="hc_type"
			style="font-size: 80%;">
			<option value="ON" <%=HCTYPE.equals("ON")?"selected":""%>>ON-Ontario</option>
			<option value="AB" <%=HCTYPE.equals("AB")?"selected":""%>>AB-Alberta</option>
			<option value="BC" <%=HCTYPE.equals("BC")?"selected":""%>>BC-British
			Columbia</option>
			<option value="MB" <%=HCTYPE.equals("MB")?"selected":""%>>MB-Manitoba</option>
			<option value="NL" <%=HCTYPE.equals("NF")?"selected":""%>>NF-Newfoundland</option>
			<option value="NB" <%=HCTYPE.equals("NB")?"selected":""%>>NB-New
			Brunswick</option>
			<option value="YT" <%=HCTYPE.equals("YT")?"selected":""%>>YT-Yukon</option>
			<option value="NS" <%=HCTYPE.equals("NS")?"selected":""%>>NS-Nova
			Scotia</option>
			<option value="PE" <%=HCTYPE.equals("PE")?"selected":""%>>PE-Prince
			Edward Island</option>
			<option value="SK" <%=HCTYPE.equals("SK")?"selected":""%>>SK-Saskatchewan</option>
		</select></td>
		<td width="270"><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.formManualReview" />: <input
			type="checkbox" name="m_review" value="checkbox"
			<%=m_review.equals("checked")?"checked":""%>> </font></strong></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgDoctor" />: <input type="checkbox"
			name="referral" value="checkbox"
			<%=r_status.equals("checked")?"checked":""%>> </font></strong></td>
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgRoster" />: <input type="hidden"
			name="roster" value="<%=roster_status%>"><%=roster_status%> </font></strong></td>
	</tr>
</table>

<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3"><bean:message
			key="billing.billingCorrection.msgBillingInf" /></font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formBillingType" />: </font></b> <input
			type="hidden" name="xml_status" value="<%=BillType%>"> <select
			style="font-size: 80%;" name="status">
			<option value=""><bean:message
				key="billing.billingCorrection.formSelectBillType" /></option>
			<option value="H" <%=BillType.equals("H")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeH" /></option>
			<option value="O" <%=BillType.equals("O")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeO" /></option>
			<option value="P" <%=BillType.equals("P")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeP" /></option>
			<option value="N" <%=BillType.equals("N")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeN" /></option>
			<option value="W" <%=BillType.equals("W")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeW" /></option>
			<option value="B" <%=BillType.equals("B")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeB" /></option>
			<option value="S" <%=BillType.equals("S")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeS" /></option>
			<option value="X" <%=BillType.equals("X")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeX" /></option>
			<option value="D" <%=BillType.equals("D")?"selected":""%>><bean:message
				key="billing.billingCorrection.formBillTypeD" /></option>
		</select></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><a href="#"
			onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=service","380","300","0")'>
		<bean:message key="billing.billingCorrection.btnBillingDate" />: </a><input
			type="text" style="font-size: 80%;" name="xml_appointment_date"
			value="<%=BillDate%>"> </font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formVisit" />:</font></b> <input type="hidden"
			name="xml_clinic_ref_code" value="<%=location%>"> <select
			name="clinic_ref_code">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectLocation" /></option>
			<%  
rslocation = apptMainBean.queryResults("1", "search_clinic_location");
while(rslocation.next()){
	BillLocationNo = rslocation.getString("clinic_location_no");
	BillLocation = rslocation.getString("clinic_location_name");
%>
			<option value="<%=BillLocationNo%>"
				<%=location.equals(BillLocationNo)?"selected":""%>><%=BillLocationNo%>
			| <%=BillLocation%></option>

			<% } %>
		</select></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formBillingPhysician" />: </font></b> <select
			style="font-size: 80%;" name="provider_no">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectProvider" /></option>
			<% 
String proFirst="", proLast="", proOHIP="", proNo="";
int Count = 0;
ResultSet rslocal = apptMainBean.queryResults("%", "search_provider_dt");
while(rslocal.next()){
	proFirst = rslocal.getString("first_name");
	proLast = rslocal.getString("last_name");
	proOHIP = rslocal.getString("provider_no");
%>
			<option value="<%=proOHIP%>"
				<%=Provider.equals(proOHIP)?"selected":""%>><%=proOHIP%> |
			<%=proLast%>, <%=proFirst%></option>
			<% 
}
%>
		</select> <input type="hidden" name="xml_provider_no" value="<%=Provider%>">
		</td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"> <bean:message
			key="billing.billingCorrection.formVisitType" />: </font></b> <input
			type="hidden" name="xml_visittype" value="<%=visittype%>"> <select
			style="font-size: 80%;" name="visittype">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectVisitType" /></option>
			<option value="00" <%=visittype.equals("00")?"selected":""%>><bean:message
				key="billing.billingCorrection.formClinicVisit" /></option>
			<option value="01" <%=visittype.equals("01")?"selected":""%>><bean:message
				key="billing.billingCorrection.formOutpatientVisit" /></option>
			<option value="02" <%=visittype.equals("02")?"selected":""%>><bean:message
				key="billing.billingCorrection.formHospitalVisit" /></option>
			<option value="03" <%=visittype.equals("03")?"selected":""%>><bean:message
				key="billing.billingCorrection.formER" /></option>
			<option value="04" <%=visittype.equals("04")?"selected":""%>><bean:message
				key="billing.billingCorrection.formNursingHome" /></option>
			<option value="05" <%=visittype.equals("05")?"selected":""%>><bean:message
				key="billing.billingCorrection.formHomeVisit" /></option>
		</select></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="hidden" name="xml_visitdate"
			value="<%=visitdate%>"> <a href="#"
			onClick='rs("billingcalendar","billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=admission","380","300","0")'>
		<bean:message key="billing.billingCorrection.btnAdmissionDate" />:</a> <input
			type="text" style="font-size: 80%;" name="xml_vdate"
			value="<%=visitdate%>"></font></b></td>
	</tr>
</table>

<table width="600">
	<tr bgcolor="#CCCCFF">
		<td width="25%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formServiceCode" /></font></b></td>
		<td width="50%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formDescription" /></font></b></td>
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.formUnit" /></font></b></td>
		<td width="13%" align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message
			key="billing.billingCorrection.formFee" /></font></b></td>
	</tr>
	<%
String serviceCode = "";
String serviceDesc = "";
String billAmount = "";
String diagCode = "";
String diagDesc = "";
String billingunit="";

if (bFlag) {
	ResultSet rsBillRec = apptMainBean.queryResults(billNo, "search_bill_record");
	while(rsBillRec.next()){
		serviceCode = rsBillRec.getString("service_code");
		serviceDesc = rsBillRec.getString("service_desc");
		billAmount = rsBillRec.getString("billing_amount");
		diagCode = rsBillRec.getString("diagnostic_code");
		billingunit = rsBillRec.getString("billingunit");
		rowCount = rowCount + 1; 
%>

	<tr>
		<th><font face="Arial, Helvetica, sans-serif" size="2"> <input
			type="hidden" name="xml_service_code<%=rowCount%>"
			value="<%=serviceCode%>"> <input type="text"
			style="font-size: 80%;" name="servicecode<%=rowCount-1%>"
			value="<%=serviceCode%>"></font></th>
		<th><font face="Arial, Helvetica, sans-serif" size="1"><%=serviceDesc%></font>
		</th>
		<th><font face="Arial, Helvetica, sans-serif" size="2"> <input
			type="hidden" name="xml_billing_unit<%=rowCount%>"
			value="<%=billingunit%>"> <input type="text"
			style="font-size: 80%;" name="billingunit<%=rowCount-1%>"
			value="<%=billingunit%>" size="5" maxlength="5"></font></th>
		<th align="right"><input type="hidden"
			name="xml_billing_amount<%=rowCount%>"
			value="<%=billAmount.substring(0,billAmount.length()-2) + "." + billAmount.substring(billAmount.length()-2)%>">
		<input type="text" style="font-size: 80%;" size="5" maxlength="5"
			name="billingamount<%=rowCount-1%>"
			value="<%=billAmount.substring(0,billAmount.length()-2) + "." + billAmount.substring(billAmount.length()-2)%>">
		</th>
	</tr>
	<%
	}

	for (int i=rowCount; i<6; i++){ 
%>
	<tr>
		<td><input type="text" style="font-size: 80%;"
			name="servicecode<%=i%>" value=""></td>
		<td>&nbsp;</td>
		<td><input type="text" style="font-size: 80%;"
			name="billingunit<%=i%>" value="" size="5" maxlength="5"></td>
		<td align="right"><input type="text" style="font-size: 80%;"
			name="billingamount<%=i%>" value="" size="5" maxlength="5"></td>
	</tr>

	<%	
	}	

	ResultSet rsDiagCode = apptMainBean.queryResults(diagCode, "search_diagnostic_code");
	while(rsDiagCode.next()){
		diagDesc = rsDiagCode.getString("description");
	}
}
apptMainBean.closePstmtConn();
%>

	<tr bgcolor="#CCCCFF">
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><b> <bean:message
			key="billing.billingCorrection.formDiagnosticCode" /></b></font></td>
	</tr>
	<tr>
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"> <input type="hidden" name="xml_diagnostic_code"
			value="<%=diagCode%>"> <input type="text"
			style="font-size: 80%;" name="xml_diagnostic_detail"
			value="<%=diagCode%>" size="50"> <input type="hidden"
			name="xml_dig_search1"> <a href="javascript:ScriptAttach()"><bean:message
			key="billing.billingCorrection.btnDXSearch" /></a></font></td>
	</tr>
	<tr>
		<td colspan="4"><input type="submit" name="submit"
			value="<bean:message key="billing.billingCorrection.btnSubmit"/>"></td>
	</tr>
</table>
<form>
</body>
</html:html>
