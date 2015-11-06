<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%// start
			
			String curUser_no, userfirstname, userlastname;
			curUser_no = (String) session.getAttribute("user");

			String UpdateDate = "";
			String DemoNo = "";
			String DemoName = "";
			String DemoAddress = "";
			String DemoCity = "";
			String DemoProvince = "";
			String DemoPostal = "";
			String DemoDOB = "";
			String DemoSex = "";
			String hin = "";
			String location = "";
			String BillLocation = "";
			String BillLocationNo = "";
			String BillDate = "";
			String Provider = "";
			String BillType = "";
			String payProgram = "";
			String BillTotal = "";
			String visitdate = "";
			String visittype = "";
			String BillDTNo = "";
			String HCTYPE = "";
			String HCSex = "";
			String r_doctor_ohip = "";
			String r_doctor = "";
			String r_doctor_ohip_s = "";
			String r_doctor_s = "";
			String m_review = "";
			String specialty = "";
			String r_status = "";
			String roster_status = "";
			String comment = "";
			int rowCount = 0;
			int rowReCount = 0;
			ResultSet rslocation = null;
			ResultSet rsPatient = null;

			%>

<%@ page import="java.math.*,java.util.*,java.sql.*,oscar.*,java.net.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ClinicNbr"%>
<%@page import="org.oscarehr.common.dao.ClinicNbrDao"%>

<%GregorianCalendar now = new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH) + 1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.billingCorrection.title" /></title>
<link rel="stylesheet" type="text/css" href="billingON.css" />
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../../../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>
<script language="JavaScript">
<!--
function setfocus() {
	//document.form1.billing_no.focus();
	//document.form1.billing_no.select();
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
function referralScriptAttach2(elementName, name2) {
     var d = elementName;
     t0 = escape("document.forms[1].elements[\'"+d+"\'].value");
     t1 = escape("document.forms[1].elements[\'"+name2+"\'].value");
     awnd=rs('att',('searchRefDoc.jsp?param='+t0+'&param2='+t1),600,600,1);
     awnd.focus();
}
function scScriptAttach(nameF) {
	f0 = document.forms[1].elements[nameF].value;
    f1 = escape("document.forms[1].elements[\'"+nameF+ "\'].value");
	awnd=rs('att','billingCodeSearch.jsp?name='+f0 + '&search=&name1=&name2=&nameF='+f1,600,600,1);
	awnd.focus();
}

function validateNum(el){
   var val = el.value;
   var tval = ""+val;
   if (isNaN(val)){   
      alert("Item value must be numeric.");
      el.select();
      el.focus();
      return false;
   }
   if ( val > 999.99 ){
     alert("Item value must be below $1000");
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

function validateAllItems(){   
   if (!validateNum(document.getElementById("billingamount0")) ){
      return false;
   }   
   if (!validateNum(document.getElementById("billingamount1")) ){
      return false;
   }   
   if (!validateNum(document.getElementById("billingamount2")) ){
      return false;
   }   
   if (!validateNum(document.getElementById("billingamount3")) ){
      return false;
   }   
   if (!validateNum(document.getElementById("billingamount4")) ){
      return false;
   }   
   if (!validateNum(document.getElementById("billingamount5")) ){
      return false;
   }   
   return true;
}
function popupPage(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "billcorrection", windowprops);
    if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
    	popup.focus();
    }
}
//-->
</script>
</head>

<body bgcolor="ivory" text="#000000" topmargin="0" leftmargin="0"
	rightmargin="0" onLoad="setfocus()">
<!--  <table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<th height="40" width="10%"></th>
		<th width="90%" align="left"><font face="Verdana, Arial" color="#FFFFFF" size="4"><bean:message
			key="billing.billingCorrection.msgCorrection" /></font></th>
	</tr>
</table>
-->
<%//
				BillingCorrectionPrep obj = new BillingCorrectionPrep();
				List recordObj = null;
				BillingClaimHeader1Data ch1Obj = new BillingClaimHeader1Data();
				BillingItemData itemObj = null;

				// bFlag - fill in data?
				boolean bFlag = false;
				String billNo = request.getParameter("billing_no").trim();
				if (billNo != null && billNo.length() > 0) {
					bFlag = true;
				}

				if (bFlag) {
					recordObj = obj.getBillingRecordObj(billNo);
					if (recordObj != null && recordObj.size() > 0) {
						ch1Obj = (BillingClaimHeader1Data) recordObj.get(0);

						UpdateDate = ch1Obj.getUpdate_datetime(); //.substring(0,10);
						DemoNo = ch1Obj.getDemographic_no();
						DemoName = ch1Obj.getDemographic_name();
						DemoAddress = "";
						DemoCity = "";
						DemoProvince = "";
						DemoPostal = "";
						DemoDOB = ch1Obj.getDob();
						DemoSex = ch1Obj.getSex().equals("1") ? "M" : "F";
						hin = ch1Obj.getHin() + ch1Obj.getVer();
						location = ch1Obj.getFacilty_num();
						BillLocation = "";
						BillLocationNo = ch1Obj.getFacilty_num();
						BillDate = ch1Obj.getBilling_date();
						Provider = ch1Obj.getProviderNo();
						BillType = ch1Obj.getStatus();
						payProgram = ch1Obj.getPay_program();
						BillTotal = ch1Obj.getTotal();
						visitdate = ch1Obj.getAdmission_date();
						visittype = ch1Obj.getVisittype();
						BillDTNo = "";
						HCTYPE = ch1Obj.getProvince();
						HCSex = ch1Obj.getSex();
						r_doctor_ohip = ch1Obj.getRef_num();
						r_doctor = "";
						r_doctor_ohip_s = "";
						r_doctor_s = "";
						m_review = ch1Obj.getMan_review();
						specialty = "";
						r_status = "";
						roster_status = "";
						comment = ch1Obj.getComment();
					}
				}
%>

<table width="100%" border="0" class="myYellow">
	<form name="form1" method="post" action="billingONDisplay.jsp">
	<tr>
		<th width="30%" align="left"><bean:message
			key="billing.billingCorrection.formInvoiceNo" /></th>
		<th width="10%"><input type="text" name="billing_no"
			value="<%=nullToEmpty(ch1Obj.getId()) %>" maxsize="10"></th>
		<th width="50%" align="left"><bean:message
			key="billing.billingCorrection.msgLastUpdate" />: <%=nullToEmpty(ch1Obj.getUpdate_datetime())%></th>
		<th><input type="button" name="submit" value="Exit"
			onClick="window.close();" /></th>
	</tr>
	</form>
</table>

<!-- RA error -->
<% 
if(bFlag) { 
	//billNo = "44071";
	List lReject = obj.getBillingRejectList(billNo);
	List lError = obj.getBillingExplanatoryList(billNo);
	lError.addAll(lReject);
	JdbcBillingErrorCodeImpl errorObj = new JdbcBillingErrorCodeImpl();
%>
<table width="100%" border="0" class="myIvory">
	<% for(int i=0; i<lError.size(); i++) { 
	String codeNo = (String) lError.get(i);
	if("".equals(codeNo)) continue;
	String codeDesc = errorObj.getCodeDesc((String)lError.get(i));
	codeDesc = codeDesc == null ? "Unknown" : codeDesc;
%>
	<tr>
		<th width="10%"><b><%=codeNo %></b></th>
		<td align="left"><%=codeDesc %></td>
	</tr>
	<% } %>
</table>
<% } %>

<form name="serviceform" method="post"
	action=""
	onsubmit="return validateAllItems()"><input type="hidden"
	name="xml_billing_no" value="<%=billNo%>" /> <input type="hidden"
	name="update_date" value="<%=UpdateDate%>" />

<table width="600" border="0">
	<tr class="myGreen">
		<th align="left" colspan="2"><b><bean:message
			key="billing.billingCorrection.msgPatientInformation" /></b></th>
	</tr>
	<tr>
		<td width="54%"><b><bean:message
			key="billing.billingCorrection.msgPatientName" />: <a href=#
			onclick="popupPage(720,860,'../../../demographic/demographiccontrol.jsp?demographic_no=<%=DemoNo %>&displaymode=edit&dboperation=search_detail');return false;">
		<%=DemoName%></a> <input type="hidden" name="demo_name"
			value="<%=DemoName%>"> </b></td>
		<td width="46%"><b><bean:message
			key="billing.billingCorrection.formHealth" />: <%=hin%> <input
			type="hidden" name="xml_hin" value="<%=hin%>"> </b></td>
	</tr>
	<tr>
		<td><b><bean:message key="billing.billingCorrection.msgSex" />:
		<%=DemoSex%> <input type="hidden" name="demo_sex" value="<%=DemoSex%>">
		<input type="hidden" name="hc_sex" value="<%=HCSex%>"> </b></td>
		<td><b><bean:message key="billing.billingCorrection.formDOB" />:
		<input type="hidden" name="xml_dob" value="<%=DemoDOB%>"> <%=DemoDOB%>
		</b></td>
	</tr>
	<tr>
		<td><strong><bean:message
			key="billing.billingCorrection.msgDoctor" />: <input type="text"
			name="rd" value="<%=r_doctor%>" size=20 readonly></strong></td>
		<td><strong><bean:message
			key="billing.billingCorrection.msgDoctorNo" />: <input type="text"
			name="rdohip" value="<%=r_doctor_ohip%>" size=8 readonly /></strong> <a
			href="javascript:referralScriptAttach2('rdohip','rd')">Search</a></td>
	</tr>
</table>

<table width="600" border="0">
	<tr class="myGreen">
		<td colspan=2><strong><bean:message
			key="billing.billingCorrection.msgAditInfo" /></strong></td>
		<!--  td width="270"><strong><bean:message
			key="billing.billingCorrection.formSpecialty" /> </strong> <select name="specialty" style="font-size:80%;">
			<option value="none"><bean:message key="billing.billingCorrection.formNone" /></option>
			<option value="flu" <%=specialty.equals("flu")?"selected":""%>><bean:message key="billing.billingCorrection.formFlu" /></option></td>-->
	</tr>
	<tr class="myIvory">
		<td width="320"><strong><bean:message
			key="billing.billingCorrection.formHCType" />:</strong> <select
			name="hc_type" style="font-size: 80%;">
			<option value="ON" <%=HCTYPE.equals("ON")?"selected":""%>>ON-Ontario</option>
			<option value="AB" <%=HCTYPE.equals("AB")?"selected":""%>>AB-Alberta</option>
			<option value="BC" <%=HCTYPE.equals("BC")?"selected":""%>>BC-British
			Columbia</option>
			<option value="MB" <%=HCTYPE.equals("MB")?"selected":""%>>MB-Manitoba</option>
			<option value="NL" <%=HCTYPE.equals("NL")?"selected":""%>>NL-Newfoundland</option>
			<option value="NB" <%=HCTYPE.equals("NB")?"selected":""%>>NB-New
			Brunswick</option>
			<option value="YT" <%=HCTYPE.equals("YT")?"selected":""%>>YT-Yukon</option>
			<option value="NS" <%=HCTYPE.equals("NS")?"selected":""%>>NS-Nova
			Scotia</option>
			<option value="PE" <%=HCTYPE.equals("PE")?"selected":""%>>PE-Prince
			Edward Island</option>
			<option value="SK" <%=HCTYPE.equals("SK")?"selected":""%>>SK-Saskatchewan</option>
		</select></td>
		<td width="270"><strong><bean:message
			key="billing.billingCorrection.formManualReview" />: <input
			type="checkbox" name="m_review" value="Y"
			<%=m_review.equals("Y")?"checked":""%>> </strong></td>
	</tr>
	<!--  tr bgcolor="#EEEEFF">
		<td><strong><bean:message key="billing.billingCorrection.msgDoctor" />:
		<input type="checkbox" name="referral" value="checkbox" <%=r_status.equals("checked")?"checked":""%>> </strong></td>
		<td><strong><bean:message key="billing.billingCorrection.msgRoster" />:
		<input type="hidden" name="roster" value="<%=roster_status%>"><%=roster_status%> </strong></td>
	</tr>-->
</table>

<table width="600" border="0">
	<tr class="myGreen">
		<td><b><bean:message
			key="billing.billingCorrection.msgBillingInf" /></b></td>
		<td width="46%"><bean:message
			key="billing.billingCorrection.btnBillingDate" /><img
			src="../../../images/cal.gif" id="xml_appointment_date_cal" />: <input
			type="text" id="xml_appointment_date" name="xml_appointment_date"
			value="<%=BillDate%>" size=10 /></td>
	</tr>
	<tr>
		<td width="54%"><b><bean:message
			key="billing.billingCorrection.formBillingType" />: </b> <input
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
		<td width="46%"><b> Pay Program:</b> <input type="hidden"
			name="xml_payProgram" value="<%=BillDate%>" /><select
			style="font-size: 80%;" name="payProgram">
			<%for (int i = 0; i < BillingDataHlp.getVecPaymentType().size(); i = i + 2) {

					%>
			<option value="<%=BillingDataHlp.getVecPaymentType().get(i) %>"
				<%=payProgram.equals((String)BillingDataHlp.getVecPaymentType().get(i))? "selected":"" %>><%=BillingDataHlp.getVecPaymentType().get(i + 1)%></option>
			<%}

				%>
		</select></td>
	</tr>
	<tr class="myGreen">
		<td width="54%"><b><%if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %> Clinic Nbr <% } else { %> <bean:message key="billing.billingCorrection.formVisitType"/> <% } %>:</b> <input type="hidden"
			name="xml_clinic_ref_code" value="<%=location%>"> <select
			name="clinic_ref_code">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectLocation" /></option>
			<%//
				List lLocation = obj.getFacilty_num();
				for (int i = 0; i < lLocation.size(); i = i + 2) {
					BillLocationNo = (String) lLocation.get(i);
					BillLocation = (String) lLocation.get(i + 1);
%>
			<option value="<%=BillLocationNo%>"
				<%=location.equals(BillLocationNo)?"selected":""%>><%=BillLocationNo%>
			| <%=BillLocation%></option>

			<%}

				%>
		</select></td>
		<td width="46%"><b><bean:message
			key="billing.billingCorrection.formBillingPhysician" />: </b> <select
			style="font-size: 80%;" name="provider_no">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectProvider" /></option>
			<%List<String> pList = (new JdbcBillingPageUtil()).getCurProviderStr();
				for (int i = 0; i < pList.size(); i++) {
					String temp[] = ( pList.get(i)).split("\\|");

					%>
			<option value="<%=temp[0]%>"
				<%=Provider.equals(temp[0])?"selected":""%>><%=temp[0]%> |
			<%=temp[1]%>, <%=temp[2]%></option>
			<%}

				%>
		</select> <input type="hidden" name="xml_provider_no" value="<%=Provider%>"></td>
	</tr>
	<tr>
		<td width="54%"><b> <bean:message
			key="billing.billingCorrection.formVisitType" />: </b> <input
			type="hidden" name="xml_visittype" value="<%=visittype%>"> <select
			style="font-size: 80%;" name="visittype">
			<option value=""><bean:message
				key="billing.billingCorrection.msgSelectVisitType" /></option>
				 <% if (OscarProperties.getInstance().getBooleanProperty("rma_enabled", "true")) { %>
                   <% 
			    ClinicNbrDao cnDao = (ClinicNbrDao) SpringUtils.getBean("clinicNbrDao"); 
				ArrayList<ClinicNbr> nbrs = cnDao.findAll();
               	for (ClinicNbr clinic : nbrs) {
					String valueString = String.format("%s | %s", clinic.getNbrValue(), clinic.getNbrString());
					%>			    
				<option value="<%=valueString%>" <%=visittype.startsWith(clinic.getNbrValue())?"selected":""%>><%=valueString%></option>
		 		<%	} %>
                    <% } else { %>
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
				<% } %>
		</select></td>
		<td width="46%"><b> <input type="hidden" name="xml_visitdate"
			value="<%=visitdate%>" /> <bean:message
			key="billing.billingCorrection.btnAdmissionDate" /><img
			src="../../../images/cal.gif" id="xml_vdate_cal" />: <input
			type="text" id="xml_vdate" name="xml_vdate" value="<%=visitdate%>"
			size=10 /></b></td>
	</tr>
</table>

<table width="600" border="0" cellspacing="1" cellpadding="0">
	<tr class="myYellow">
		<td width="30%" colspan=2><b><bean:message
			key="billing.billingCorrection.formServiceCode" /></b></td>
		<th width="50%"><b><bean:message
			key="billing.billingCorrection.formDescription" /></b></th>
		<th width="3%"><b><bean:message
			key="billing.billingCorrection.formUnit" /></b></th>
		<th width="13%" align="right"><b><bean:message
			key="billing.billingCorrection.formFee" /></b></th>
		<th><font size="-1">Settle</font></th>
	</tr>
	<%//
				String serviceCode = "";
				String serviceDesc = "";
				String billAmount = "";
				String diagCode = "";
				String diagDesc = "";
				String billingunit = "";
				String itemStatus = "";

				if (bFlag) {
					if (recordObj.size() > 1) {
						for (int i = 1; i < recordObj.size(); i++) {
							itemObj = (BillingItemData) recordObj.get(i);

							serviceCode = itemObj.getService_code();
							serviceDesc = obj.getBillingCodeDesc(serviceCode);
							billAmount = itemObj.getFee();
							diagCode = itemObj.getDx();
							billingunit = itemObj.getSer_num();
							rowCount = rowCount + 1;
							itemStatus = itemObj.getStatus().equals("S") ? "checked" : "";
							%>

	<tr>
		<th width="25%"><input type="hidden"
			name="xml_service_code<%=rowCount%>" value="<%=serviceCode%>">
		<input type="text" style="width: 100%"
			name="servicecode<%=rowCount-1%>" value="<%=serviceCode%>"></th>
		<td><a href=# onClick="scScriptAttach('servicecode<%=i-1%>')">Search</a></td>
		<th><font size="-1"><%=serviceDesc%></th>
		<th><input type="hidden" name="xml_billing_unit<%=rowCount%>"
			value="<%=billingunit%>"> <input type="text"
			style="width: 100%" name="billingunit<%=rowCount-1%>"
			value="<%=billingunit%>" size="5" maxlength="5"></th>
		<th align="right"><input type="hidden"
			name="xml_billing_amount<%=rowCount%>" value="<%=billAmount%>">
		<input type="text" style="width: 100%" size="5" maxlength="6"
			id="billingamount<%=rowCount-1%>" name="billingamount<%=rowCount-1%>"
			value="<%=billAmount%>" onchange="javascript:validateNum(this)"></th>
		<td align="center"><input type="checkbox"
			name="itemStatus<%=rowCount-1%>" id="itemStatus<%=rowCount-1%>"
			value="S" <%=itemStatus %>></td>
	</tr>
	<%//
						}
					}

					//for (int i = rowCount; i < 10; i++) {
					rowCount++;
						%>
	<tr>
		<td><input type="text" style="width: 100%"
			name="servicecode<%=rowCount-1%>" value=""></td>
		<td><a href=#
			onClick="scScriptAttach('servicecode<%=rowCount-1%>')">Search</a></td>
		<td>&nbsp;</td>
		<td><input type="text" style="width: 100%"
			name="billingunit<%=rowCount-1%>" value="" size="5" maxlength="5"></td>
		<td align="right"><input type="text" style="width: 100%"
			name="billingamount<%=rowCount-1%>" id="billingamount<%=rowCount-1%>"
			value="" size="5" maxlength="5"></td>
	</tr>

	<%//}

				}
%>

	<tr class="myGreen">
		<td colspan="5"><b> <bean:message
			key="billing.billingCorrection.formDiagnosticCode" /></b></td>
	</tr>
	<tr>
		<td colspan="4"><input type="hidden" name="xml_diagnostic_code"
			value="<%=diagCode%>"> <input type="text"
			style="font-size: 80%;" name="xml_diagnostic_detail"
			value="<%=diagCode%>" size="50"> <input type="hidden"
			name="xml_dig_search1"> <a href="javascript:ScriptAttach()"><bean:message
			key="billing.billingCorrection.btnDXSearch" /></a></td>
	</tr>
	<tr>
		<td colspan="4"><input type="button" name="submit" value="Exit"
			onClick="window.close();" /></td>
	</tr>
	<tr>
		<td colspan="4">Billing Notes:<br>
		<textarea name="comment" value="" cols=60 rows=4><%=comment %></textarea>
		</td>
	</tr>
</table>
</form>
</body>
<script type="text/javascript">
Calendar.setup( { inputField : "xml_appointment_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );
</script>
<%!String nullToEmpty(String str) {
		return (str == null ? "" : str);
	}

	%>

</html:html>
