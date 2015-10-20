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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
String curUser_no = (String) session.getAttribute("user");

%>

<%@ page import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"	errorPage="../errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ include file="../../../admin/dbconnection.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@page import="org.oscarehr.common.model.BillingService" %>

<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Summary</title>
<script type="text/javascript"
	src="../../../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../../../share/javascript/nifty.js"></script>
<link rel="stylesheet" type="text/css"
	href="../../../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css"
	href="../../../share/css/niftyPrint.css" media="print" />

<script type="text/javascript">
window.onload=function(){
  if(!NiftyCheck())
    return;
Rounded("div.dxBox","top","transparent","#CCCCFF","small border #CCCCFF");
Rounded("div.dxBox","bottom","transparent","#EEEEFF","small border #CCCCFF");
}
</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="JavaScript">
<!--
function validate_form(){
	// do some validation
	document.body.style.cursor = "wait";
	document.forms.form1.Submit.disabled = true;
	return true;
}
//-->
</SCRIPT>
</head>

<%
String errorMsg="", errorFlag = "";
String warningMsg="";
String proNO = request.getParameter("xml_provider");
String temp2=null, content="", location2="", desc3="",desc2="", scode2=null, scode3=null, dsfee="", fee2="",fee3="", flag="", flag1="", flag2="";
String pValue="", pCode="", pDesc="", pPerc="", pUnit = "";
String eValue="", eCode="", eDesc="", ePerc="", eUnit = "";
String xValue="", xCode="", xDesc="", xPerc="", xUnit = "";
String eFlag = "", xFlag="";

if (proNO.compareTo("000000") == 0) {
	errorFlag = "1";
	errorMsg = errorMsg + "Error: Please select a valid Provider. <br>";
}
%>

<%
String dxString = request.getParameter("xml_diagnostic_detail");
String dx = dxString.length()>2 ? dxString.substring(0,3) : " ";
// add perc. setting
boolean bPercS = false;
bPercS = "".equals(oscarVariables.getProperty("billing_pCode",""))? false : true;
boolean bDbCode = false;
String billing_pCode = "";
String billing_E078A = oscarVariables.getProperty("billing_pCode_E078A","");
if(!"".equals(billing_E078A)) {
    String dTemp [] = billing_E078A.split("@");
    billing_E078A = dx.matches(dTemp[2]) ? dTemp[0] : " ";
}
if(bPercS) {
    billing_pCode = oscarVariables.getProperty("billing_pCode","");
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		String t=e.nextElement().toString();
		if( t.startsWith("xml_") ) {
			t = t.substring(4).toUpperCase();
			if("E411A".equals(t) || "E411A".equals(request.getParameter("xml_other1").toUpperCase())
			        || "E411A".equals(request.getParameter("xml_other2").toUpperCase())
			        || "E411A".equals(request.getParameter("xml_other3").toUpperCase())) {
			    billing_pCode = oscarVariables.getProperty("billing_pCode_E411A","");
			    break;
			}
			if(!"".equals(billing_E078A)) {
				if("E078A".equals(t) || "E078A".equals(request.getParameter("xml_other1").toUpperCase())
				        || "E078A".equals(request.getParameter("xml_other2").toUpperCase())
				        || "E078A".equals(request.getParameter("xml_other3").toUpperCase())) {
				    billing_pCode = billing_E078A;
				    if(" ".equals(billing_E078A)) {
						errorFlag = "1";
					    errorMsg += "Error: Diagnostic code error for E078A<br>" ;
				    }
				    break;
				}
			}
		}
	}
}

int counter = 0;
String numCode="";
String diagcode="";
String diagnostic_code = request.getParameter("xml_diagnostic_code");
String diagnostic_detail = request.getParameter("xml_diagnostic_detail");

if (diagnostic_detail == null || diagnostic_detail.compareTo("") == 0) {
	if (diagnostic_code.substring(0,3).compareTo("000") == 0){
		diagnostic_code = "000|Other code";
		diagcode = diagnostic_code.substring(0,3);

		for(int i=0;i<diagcode.length();i++) {
			String c = diagcode.substring(i,i+1);
			if(c.hashCode()>=48 && c.hashCode()<=58)	numCode += c;
		}

		if (numCode.length() < 3) {
			diagnostic_code = "000|Other code";
			diagcode="000";
		}
	} else {
		diagcode = diagnostic_code.substring(0,3);
	}

} else {
	diagnostic_code = request.getParameter("xml_diagnostic_detail");
	diagcode = diagnostic_detail.substring(0,3);
	numCode = "";

	for(int i=0;i<diagcode.length();i++) {
		String c = diagcode.substring(i,i+1);
		if(c.hashCode()>=48 && c.hashCode()<=58)	numCode += c;
	}
	if (numCode.length() < 3) {
		diagnostic_code = "000|Other code";
		diagcode="000";
	}
}
if (diagcode.compareTo("000") == 0) {
	errorFlag = "1";
	errorMsg = errorMsg + "Error: Please enter a valid Diagnostic Code. <br>";
}


String demoname=request.getParameter("demographic_name");
GregorianCalendar now=new GregorianCalendar();
String demoNO = request.getParameter("demographic_no");
String r_doctor="", r_doctor_ohip="" ;
String r_status=request.getParameter("xml_referral");
String demoFirst="", demoLast="", demoHIN="", demoDOB="", demoDOBYY="", demoDOBMM="", demoDOBDD="", demoSex="", demoHCTYPE="";

Demographic d = demographicDao.getDemographic(demoNO);
if(d != null) {
	demoFirst = d.getFirstName();
	demoLast = d.getLastName();
	demoHIN = d.getHin() + d.getVer();
	demoSex = d.getSex();
	if (demoSex.compareTo("M")==0) demoSex ="1";
	if (demoSex.compareTo("F")==0) demoSex ="2";

	demoHCTYPE = d.getHcType()==null?"":d.getHcType();
	if (demoHCTYPE.compareTo("") == 0 || demoHCTYPE == null || demoHCTYPE.length() <2) {
		demoHCTYPE="ON";
	}else{
		demoHCTYPE=demoHCTYPE.substring(0,2).toUpperCase();
	}
	demoDOBYY = d.getYearOfBirth();
	demoDOBMM = d.getMonthOfBirth();
	demoDOBDD = d.getDateOfBirth();

	if (d.getFamilyDoctor() == null){
		r_doctor = "N/A"; r_doctor_ohip="000000";
	}else{
		r_doctor=SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rd")==null ? "" : SxmlMisc.getXmlContent(d.getFamilyDoctor(), "rd");
		r_doctor_ohip=SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rdohip")==null ? "" : SxmlMisc.getXmlContent(d.getFamilyDoctor(), "rdohip");
	}

	demoDOBMM = demoDOBMM.length() == 1 ? ("0" + demoDOBMM) : demoDOBMM;
	demoDOBDD = demoDOBDD.length() == 1 ? ("0" + demoDOBDD) : demoDOBDD;
	demoDOB = demoDOBYY + demoDOBMM + demoDOBDD;

	if (d.getHin() == null ) {
		errorFlag = "1";
		errorMsg = errorMsg + "Error: The patient does not have a valid HIN. <br>";
	} else if (d.getHin().equals("")) {
		warningMsg += "Warning: The patient does not have a valid HIN. <br>";
	}
	if (r_doctor_ohip != null && r_doctor_ohip.length()>0 && r_doctor_ohip.length() != 6) {
		warningMsg += "Warning: the referral doctor's no is wrong. <br>";
	}
	if (demoDOB.length() != 8) {
		errorFlag = "1";
		errorMsg = errorMsg + "Error: The patient does not have a valid DOB. <br>";
	}

}


if (r_doctor==null || r_status == null || r_doctor.equals("")) r_doctor="N/A";
if (r_doctor_ohip==null || r_status == null || r_doctor_ohip.equals("")) r_doctor_ohip="000000";


String proFirst="", proLast="", proRMA="", proOHIPNO="", crFirst="Not", crLast="Available", apptFirst="Not", apptLast="Available", asstFirst="Not", asstLast="Available";

Provider p = providerDao.getProvider(proNO);
if(p != null) {
	proFirst = p.getFirstName();
	proLast = p.getLastName();
	proOHIPNO = p.getOhipNo();
	proRMA = p.getRmaNo();
}

p = providerDao.getProvider(request.getParameter("apptProvider_no"));
if(p != null) {
	apptFirst = p.getFirstName();
	apptLast = p.getLastName();
}

p = providerDao.getProvider(request.getParameter("asstProvider_no"));
if(p != null) {
	asstFirst = p.getFirstName();
	asstLast = p.getLastName();
}

p = providerDao.getProvider(curUser_no);
if(p != null) {
	crFirst = p.getFirstName();
	crLast = p.getLastName();
}


String visitdate = request.getParameter("xml_vdate");
String billtype = request.getParameter("xml_billtype");
String visittype = request.getParameter("xml_visittype");
if (visittype.substring(0,2).compareTo("00") == 0) {
	visitdate = request.getParameter("xml_appointment_date");
} else {
	visitdate = request.getParameter("xml_vdate");
	if (visitdate.compareTo("") == 0 || visitdate == null){
		visitdate=request.getParameter("xml_appointment_date");
	}else{
		//visitdate = visitdate;
	}
}
%>

<body bgcolor="#FFFFFF" text="#000000">
<form name="form1" method="post" action="billingSave.jsp"
	onsubmit="return(validate_form());">
<table width="100%" border="0">
	<tr>
		<td><b>Generic Billing Form for General Practice</b></td>
	</tr>
	<tr bgcolor="#CCCCFF">
		<td><b><u>Billing Summary</u></b></td>
	</tr>
</table>

<table width="80%" border="0" cellspacing="2" cellpadding="1">
	<tr bgcolor="#EEEEFF">
		<td width="54%">Patient : <b><%=request.getParameter("demographic_name")%></b></td>
		<td width="46%">Health# : <b><%=demoHIN%></b></td>
	</tr>
	<tr>
		<td width="54%">Billing Type: <b><%=billtype%></b></td>
		<td width="46%">Service Date: <b><%=request.getParameter("xml_appointment_date")%></b></td>
	</tr>
	<%
String local_desc="";
String location1 = request.getParameter("xml_location");
String visitLocation = clinicLocationDao.searchVisitLocation(location1);
if(visitLocation!=null) {
	 local_desc = visitLocation;
}
%>
	<tr bgcolor="#EEEEFF">
		<td width="54%">Visit Type Code: <b><%=request.getParameter("xml_visittype")%></b></td>
		<td width="46%">Billing Physician: <b><%=proFirst%> <%=proLast%></b></td>
	</tr>
	<tr>
		<td width="54%">Visit Location: <b><%=local_desc%></b></td>
		<td width="46%">Admission Date: <b><%=visitdate%></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%">Appointment Physician: <b> <%=apptFirst%> <%=apptLast%>
		</b></td>
		<td width="46%">Secordary Physician: <b><%=asstFirst%> <%=asstLast%>
		</b></td>
	</tr>
	<tr>
		<td width="54%">Referral Physician: <b> <%=r_doctor%> </b></td>
		<td width="46%">Referral Physician Number: <b><%=r_doctor_ohip%>
		</b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%">Creator: <b><%=crFirst%> <%=crLast%> </b></td>
		<td width="46%">Creation Date:<b> <%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%></b></td>
	</tr>
</table>

<%
BigDecimal otherfee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal otherfee02 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal otherfee03 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal pValue1= new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal pValue1PerUnit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal otherunit = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal xPercent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
BigDecimal xPercentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal percent = new BigDecimal(0).setScale(4, BigDecimal.ROUND_HALF_UP);
BigDecimal percentPremium = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigSubTotal = new BigDecimal(0).setScale(2);

String temp=null, location="", desc="", scode="", fee="";//default is not null
String otherdesc1="", otherdbcode1="", otherfee1="", otherperc1="";
String otherdesc2="", otherdbcode2="", otherfee2="", otherperc2="";
String otherdesc3="", otherdbcode3="", otherfee3="", otherperc3="";
int otherflag1 = 0, otherflag2=0, otherflag3=0;
String recunit="";
String otherstr="", otherstr2="", otherstr3="", othercode1="", othercode2="", othercode3="", othercode1unit="", othercode2unit="", othercode3unit="";

othercode1 = request.getParameter("xml_other1");
othercode1unit = request.getParameter("xml_other1_unit");
othercode2 = request.getParameter("xml_other2");
othercode2unit = request.getParameter("xml_other2_unit");
othercode3 = request.getParameter("xml_other3");
othercode3unit = request.getParameter("xml_other3_unit");

if (othercode1.compareTo("") == 0 || othercode1 == null || othercode1.length() < 5) {
	otherflag1 = 0;
	othercode1 = "00000";
} else {
	if (othercode1unit.compareTo("") == 0 || othercode1unit == null) {
		othercode1unit = "1";
	}

	for(BillingService bs: billingServiceDao.getBillingCodeAttr(othercode1.substring(0,5))) {
		otherdesc1 = bs.getDescription();
		otherdbcode1 = bs.getServiceCode();
		otherfee1 = bs.getValue();
		otherperc1 = bs.getPercentage();
	}
	

	if (otherdesc1.compareTo("") == 0 || otherdesc1 == null ) {
		otherflag1 = 0;
	} else {
	//  otherflag1 = 1;
		if (otherfee1.compareTo(".00") == 0) {
			otherunit = new BigDecimal(Double.parseDouble(othercode1unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc1)).setScale(4, BigDecimal.ROUND_HALF_UP);
			BigDecimal otherperc01 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);

			if (scode.compareTo("E400B") == 0 || scode.compareTo("E401B") == 0 || scode.compareTo("E410A") == 0 || scode.compareTo("E409A") == 0
			        || scode.compareTo("E070A") == 0 || scode.compareTo("E071A") == 0 || otherdbcode1.compareTo("Q012A") == 0 || scode.compareTo("Q016A") == 0) {
				eCode = otherdbcode1;
				eDesc = otherdesc1;
				ePerc = otherperc01.toString();
				eValue =otherfee1;
				eUnit = othercode1unit;
				eFlag = "1";
			} else {
				xCode = otherdbcode1;
				xDesc = otherdesc1;
				xPerc = otherperc01.toString();
				xValue = otherfee1;
				xUnit = othercode1unit;
				xFlag = "1";
			}
		}else{
			otherflag1 = 1;
			otherunit = new BigDecimal(Double.parseDouble(othercode1unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee1)).setScale(2, BigDecimal.ROUND_HALF_UP);
			otherfee = bdotherFee.multiply(otherunit).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigTotal = BigTotal.add(otherfee);
			otherstr = otherfee.toString();
			StringBuffer sotherBuffer = new StringBuffer(otherstr);
			int f = otherstr.indexOf('.');
			sotherBuffer.deleteCharAt(f);
			sotherBuffer.insert(f,"");
			otherstr = sotherBuffer.toString();

			if(bPercS) {
			    bDbCode = bPercS ? otherdbcode1.matches(billing_pCode) : false;
			    if(bDbCode) {
					pValue1 = pValue1.add(otherfee);
					pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
					pCode =otherdbcode1;
					pDesc = otherdesc1;
					pValue = pValue1.toString();

					if (!eCode.equals(""))	eFlag = "1";
			    }
			}
			else if (otherdbcode1.compareTo("A001A")==0 || otherdbcode1.compareTo("A003A")==0 ||
                            otherdbcode1.compareTo("A004A")==0 || otherdbcode1.compareTo("A007A")==0 ||
                            otherdbcode1.compareTo("A008A")==0 || otherdbcode1.compareTo("A888A")==0 ||
                            otherdbcode1.compareTo("Z777A")==0 || otherdbcode1.compareTo("P029A")==0 ||
                            otherdbcode1.compareTo("P028A")==0 || otherdbcode1.compareTo("Z776A")==0 ||
                            otherdbcode1.compareTo("P042A")==0 || otherdbcode1.compareTo("S768A")==0 ||
                            otherdbcode1.compareTo("S756A")==0 || otherdbcode1.compareTo("S757A")==0 ||
                            otherdbcode1.compareTo("S784A")==0 || otherdbcode1.compareTo("S745A")==0 ||
                            otherdbcode1.compareTo("P010A")==0 || otherdbcode1.compareTo("P009A")==0 ||
                            otherdbcode1.compareTo("P006A")==0 || otherdbcode1.compareTo("P011A")==0 ||
                            otherdbcode1.compareTo("P041A")==0 || otherdbcode1.compareTo("P018A")==0 ||
                            otherdbcode1.compareTo("P038A")==0 || otherdbcode1.compareTo("P020A")==0 ||
                            otherdbcode1.compareTo("P031A")==0 || otherdbcode1.compareTo("Z552A")==0 ||
                            otherdbcode1.compareTo("P022A")==0 || otherdbcode1.compareTo("P023A")==0 ||
                            otherdbcode1.compareTo("P030A")==0 ||
                            otherdbcode1.compareTo("K005A")==0 ||
                            otherdbcode1.compareTo("K030A")==0 ||
                            otherdbcode1.compareTo("K017A")==0 ||
                            otherdbcode1.compareTo("P005A")==0 ||
                            otherdbcode1.compareTo("P004A")==0 ||
                            otherdbcode1.compareTo("Z716A")==0 || otherdbcode1.startsWith("S") ||
                            (otherdbcode1.endsWith("B") && !otherdbcode1.endsWith("C988B") && !otherdbcode1.endsWith("C998B") && !otherdbcode1.endsWith("C999B") ) ){
				pValue1 = pValue1.add(otherfee);
				pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
				pCode =otherdbcode1;
				pDesc = otherdesc1;
				pValue = pValue1.toString();

				if (!eCode.equals(""))	eFlag = "1";
			}
		}
	}
}

if (othercode2.compareTo("") == 0 || othercode2 == null || othercode2.length() < 5) {
	otherflag2 = 0;
	othercode2 = "00000";
} else {
	if (othercode2unit.compareTo("") == 0 || othercode2unit == null) {
		othercode2unit = "1";
	}

	for(BillingService bs: billingServiceDao.getBillingCodeAttr(othercode1.substring(0,5))) {
		otherdesc2 = bs.getDescription();
		otherdbcode2 = bs.getServiceCode();
		otherfee2 = bs.getValue();
		otherperc2 = bs.getPercentage();
	}

	if (otherdesc2.compareTo("") == 0 || otherdesc2 == null) {
		otherflag2 = 0;
	} else {
		if (otherfee2.compareTo(".00") == 0) {
			otherunit = new BigDecimal(Double.parseDouble(othercode2unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc2)).setScale(4, BigDecimal.ROUND_HALF_UP);
			BigDecimal otherperc02 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);

			if (scode.compareTo("E400B") == 0 || scode.compareTo("E401B") == 0 || scode.compareTo("E410A") == 0 || scode.compareTo("E409A") == 0
			        || scode.compareTo("E070A") == 0 || scode.compareTo("E071A") == 0 || otherdbcode1.compareTo("Q012A") == 0 || scode.compareTo("Q016A") == 0) {
				eCode = otherdbcode2;
				eDesc = otherdesc2;
				ePerc = otherperc02.toString();
				eValue =otherfee2;
				eUnit = othercode2unit;
				eFlag = "1";
			}else{
				xCode = otherdbcode2;
				xDesc = otherdesc2;
				xPerc = otherperc02.toString();
				xValue = otherfee2;
				xUnit = othercode2unit;
				xFlag = "1";
			}
		}else{
			otherflag2 = 1;
			BigDecimal otherunit2 = new BigDecimal(Double.parseDouble(othercode2unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee2)).setScale(2, BigDecimal.ROUND_HALF_UP);
			otherfee02 = bdotherFee.multiply(otherunit2).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigTotal = BigTotal.add(otherfee02);
			otherstr2 = otherfee02.toString();
			StringBuffer sotherBuffer = new StringBuffer(otherstr2);
			int f = otherstr2.indexOf('.');
			sotherBuffer.deleteCharAt(f);
			sotherBuffer.insert(f,"");
			otherstr2 = sotherBuffer.toString();

		    bDbCode = bPercS ? otherdbcode2.matches(billing_pCode) : false;
			if(bPercS) {
			    if(bDbCode) {
					pValue1 = pValue1.add(otherfee02);
					pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
					pCode =otherdbcode2;
					pDesc = otherdesc2;
					pValue = pValue1.toString();

					if (!eCode.equals(""))	eFlag = "1";
			    }
			} else
			if (otherdbcode2.compareTo("A001A")==0 || otherdbcode2.compareTo("A003A")==0 ||
                            otherdbcode2.compareTo("A004A")==0 || otherdbcode2.compareTo("A007A")==0 ||
                            otherdbcode2.compareTo("A008A")==0 || otherdbcode2.compareTo("A888A")==0 ||
                            otherdbcode2.compareTo("Z777A")==0 || otherdbcode2.compareTo("P029A")==0 ||
                            otherdbcode2.compareTo("P028A")==0 || otherdbcode2.compareTo("Z776A")==0 ||
                            otherdbcode2.compareTo("P042A")==0 || otherdbcode2.compareTo("S768A")==0 ||
                            otherdbcode2.compareTo("S756A")==0 || otherdbcode2.compareTo("S757A")==0 ||
                            otherdbcode2.compareTo("S784A")==0 || otherdbcode2.compareTo("S745A")==0 ||
                            otherdbcode2.compareTo("P010A")==0 || otherdbcode2.compareTo("P009A")==0 ||
                            otherdbcode2.compareTo("P006A")==0 || otherdbcode2.compareTo("P011A")==0 ||
                            otherdbcode2.compareTo("P041A")==0 || otherdbcode2.compareTo("P018A")==0 ||
                            otherdbcode2.compareTo("P038A")==0 || otherdbcode2.compareTo("P020A")==0 ||
                            otherdbcode2.compareTo("P031A")==0 || otherdbcode2.compareTo("Z552A")==0 ||
                            otherdbcode2.compareTo("P022A")==0 || otherdbcode2.compareTo("P023A")==0 ||
                            otherdbcode2.compareTo("P030A")==0 ||
                            otherdbcode2.compareTo("K005A")==0 ||
                            otherdbcode2.compareTo("K030A")==0 ||
                            otherdbcode2.compareTo("K017A")==0 ||
                            otherdbcode2.compareTo("P005A")==0 ||
                            otherdbcode2.compareTo("P004A")==0 ||
                            otherdbcode2.compareTo("Z716A")==0 || otherdbcode2.startsWith("S") ||
                            (otherdbcode2.endsWith("B") && !otherdbcode2.endsWith("C988B") && !otherdbcode2.endsWith("C998B") && !otherdbcode2.endsWith("C999B") ) ){
				pValue1 = pValue1.add(otherfee02);
				pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
				pCode =otherdbcode2;
				pDesc = otherdesc2;
				pValue = pValue1.toString();

				if (!eCode.equals(""))	eFlag = "1";
			}
		}
	}
}

if (othercode3.compareTo("") == 0 || othercode3 == null || othercode3.length() < 5) {
	otherflag3 = 0;
	othercode3 = "00000";
} else {
	if (othercode3unit.compareTo("") == 0 || othercode3unit == null) {
		othercode3unit = "1";
	}
	
	for(BillingService bs: billingServiceDao.getBillingCodeAttr(othercode1.substring(0,5))) {
		otherdesc3 = bs.getDescription();
		otherdbcode3= bs.getServiceCode();
		otherfee3 = bs.getValue();
	}
	

	if (otherdesc3.compareTo("") == 0 || otherdesc3 == null ) {
		otherflag3 = 0;
	} else {
		if (otherfee3.compareTo(".00") == 0) {
			otherunit = new BigDecimal(Double.parseDouble(othercode3unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherperc3)).setScale(4, BigDecimal.ROUND_HALF_UP);
			BigDecimal otherperc03 = bdotherFee.multiply(otherunit).setScale(4, BigDecimal.ROUND_HALF_UP);

			if (scode.compareTo("E400B") == 0 || scode.compareTo("E401B") == 0 || scode.compareTo("E410A") == 0 || scode.compareTo("E409A") == 0
			        || scode.compareTo("E070A") == 0 || scode.compareTo("E071A") == 0 || otherdbcode1.compareTo("Q012A") == 0 || scode.compareTo("Q016A") == 0) {
				eCode = otherdbcode3;
				eDesc = otherdesc3;
				ePerc = otherperc03.toString();
				eValue =otherfee3;
				eUnit = othercode3unit;
				eFlag = "1";
			} else{
				xCode = otherdbcode3;
				xDesc = otherdesc3;
				xPerc = otherperc03.toString();
				xValue = otherfee3;
				xUnit = othercode3unit;
				xFlag = "1";
			}
		}else{
			otherflag3 = 1;
			BigDecimal otherunit3 = new BigDecimal(Double.parseDouble(othercode3unit)).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal bdotherFee = new BigDecimal(Double.parseDouble(otherfee3)).setScale(2, BigDecimal.ROUND_HALF_UP);
			otherfee03 = bdotherFee.multiply(otherunit3).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigTotal = BigTotal.add(otherfee03);
			otherstr3 = otherfee03.toString();
			StringBuffer sotherBuffer = new StringBuffer(otherstr3);
			int f = otherstr3.indexOf('.');
			sotherBuffer.deleteCharAt(f);
			sotherBuffer.insert(f,"");
			otherstr3 = sotherBuffer.toString();

		    bDbCode = bPercS ? otherdbcode3.matches(billing_pCode) : false;
			if(bPercS) {
			    if(bDbCode) {
					pValue1 = pValue1.add(otherfee03);
					pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
					pCode =otherdbcode3;
					pDesc = otherdesc3;
					pValue = pValue1.toString();

					if (!eCode.equals(""))	eFlag = "1";
			    }
			} else
			if (otherdbcode3.compareTo("A001A")==0 || otherdbcode3.compareTo("A003A")==0 ||
                            otherdbcode3.compareTo("A004A")==0 || otherdbcode3.compareTo("A007A")==0 ||
                            otherdbcode3.compareTo("A008A")==0 || otherdbcode3.compareTo("A888A")==0 ||
                            otherdbcode3.compareTo("Z777A")==0 || otherdbcode3.compareTo("P029A")==0 ||
                            otherdbcode3.compareTo("P028A")==0 || otherdbcode3.compareTo("Z776A")==0 ||
                            otherdbcode3.compareTo("P042A")==0 || otherdbcode3.compareTo("S768A")==0 ||
                            otherdbcode3.compareTo("S756A")==0 || otherdbcode3.compareTo("S757A")==0 ||
                            otherdbcode3.compareTo("S784A")==0 || otherdbcode3.compareTo("S745A")==0 ||
                            otherdbcode3.compareTo("P010A")==0 || otherdbcode3.compareTo("P009A")==0 ||
                            otherdbcode3.compareTo("P006A")==0 || otherdbcode3.compareTo("P011A")==0 ||
                            otherdbcode3.compareTo("P041A")==0 || otherdbcode3.compareTo("P018A")==0 ||
                            otherdbcode3.compareTo("P038A")==0 || otherdbcode3.compareTo("P020A")==0 ||
                            otherdbcode3.compareTo("P031A")==0 || otherdbcode3.compareTo("Z552A")==0 ||
                            otherdbcode3.compareTo("P022A")==0 || otherdbcode3.compareTo("P023A")==0 ||
                            otherdbcode3.compareTo("P030A")==0 ||
                            otherdbcode3.compareTo("K005A")==0 ||
                            otherdbcode3.compareTo("K030A")==0 ||
                            otherdbcode3.compareTo("P005A")==0 ||
                            otherdbcode3.compareTo("P004A")==0 ||
                            otherdbcode3.compareTo("K017A")==0 ||
                            otherdbcode3.compareTo("Z716A")==0 || otherdbcode3.startsWith("S") ||
                            (otherdbcode3.endsWith("B") && !otherdbcode3.endsWith("C988B") && !otherdbcode3.endsWith("C998B") && !otherdbcode3.endsWith("C999B") ) ){
				pValue1 = pValue1.add(otherfee03);
				pValue1PerUnit = pValue1PerUnit.add(bdotherFee);
				pCode =otherdbcode3;
				pDesc = otherdesc3;
				pValue = pValue1.toString();

				if (!eCode.equals(""))	eFlag = "1";
			}
		}
	}
}
%>

<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<th width="22%"><font size="2">Service Code</th>
		<th width="58%"><font size="2">Description</th>
		<th width="6%" align="right"><font size="2">#Unit</th>
		<th width="14%" align="right"><font size="2">$ Fee</th>
	</tr>

	<%
// get the list of checked codes
Properties checkedCode = new Properties();
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if( temp.startsWith("xml_") ) {
		checkedCode.setProperty(temp, "1") ;
	}
}

double dSFee = 0.00;
double dFee = 0.00;

for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if( temp.indexOf("xml_")==-1 ) continue;
	//////////////////////////////////////////////////////////////////////////////////////
	if( temp.startsWith("desc_xml_") || temp.startsWith("price_xml_") || temp.startsWith("perc_xml_") ) {
		if (checkedCode.getProperty(temp.substring(temp.indexOf("_")+1) , "").equals("1") ) {
			content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp))+ "</" +temp+ ">";
		}
	} else {
		content+="<" +temp+ ">" +SxmlMisc.replaceHTMLContent(request.getParameter(temp).trim())+ "</" +temp+ ">";
	}
	//////////////////////////////////////////////////////////////////////////////////////

	fee = request.getParameter("price_" + temp);
	desc = request.getParameter("desc_" + temp);
	scode = temp.substring(4).toUpperCase();
	if (scode.substring(scode.length()-1).compareTo("A") != 0){
		//scode = scode;
	}

	if (fee == null) {
		fee = "";
	}else{
		if (fee.compareTo(".00")==0) {
			if (scode.compareTo("E400B") == 0 || scode.compareTo("E401B") == 0 || scode.compareTo("E410A") == 0 || scode.compareTo("E409A") == 0
			        || scode.compareTo("E070A") == 0 || scode.compareTo("E071A") == 0 || otherdbcode1.compareTo("Q012A") == 0 || scode.compareTo("Q016A") == 0) {
				eCode = scode;
				eDesc = desc;
				ePerc = request.getParameter("perc_"+temp);
				eValue = fee;
				eUnit= "1";
				eFlag = "1";
			} else{
				xCode = scode;
				xDesc = desc;
				xPerc = request.getParameter("perc_"+temp);
				xValue = fee;
				xUnit = "1";
				xFlag = "1";
			}
		}else{
			dFee = Double.parseDouble(fee);
			BigDecimal bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigTotal = BigTotal.add(bdFee);

		    bDbCode = bPercS ? scode.matches(billing_pCode) : false;
			if(bPercS) {
			    if(bDbCode) {
					pValue1 = pValue1.add(bdFee);
					pValue1PerUnit = pValue1PerUnit.add(bdFee);
					pCode = scode;
					pDesc = desc;
					pValue = pValue1.toString();

					if (!eCode.equals(""))	eFlag = "1";
			    }
			} else
			if (scode.compareTo("A001A")==0 || scode.compareTo("A003A")==0 ||
                            scode.compareTo("A004A")==0 || scode.compareTo("A007A")==0 ||
                            scode.compareTo("A008A")==0 || scode.compareTo("A888A")==0 ||
                            scode.compareTo("Z777A")==0 || scode.compareTo("P029A")==0 ||
                            scode.compareTo("P028A")==0 || scode.compareTo("Z776A")==0 ||
                            scode.compareTo("P042A")==0 || scode.compareTo("S768A")==0 ||
                            scode.compareTo("S756A")==0 || scode.compareTo("S757A")==0 ||
                            scode.compareTo("S784A")==0 || scode.compareTo("S745A")==0 ||
                            scode.compareTo("P010A")==0 || scode.compareTo("P009A")==0 ||
                            scode.compareTo("P006A")==0 || scode.compareTo("P011A")==0 ||
                            scode.compareTo("P041A")==0 || scode.compareTo("P018A")==0 ||
                            scode.compareTo("P038A")==0 || scode.compareTo("P020A")==0 ||
                            scode.compareTo("P031A")==0 || scode.compareTo("Z552A")==0 ||
                            scode.compareTo("P022A")==0 || scode.compareTo("P023A")==0 ||
                            scode.compareTo("P030A")==0 ||
                            scode.compareTo("K005A")==0 ||
                            scode.compareTo("K030A")==0 ||
                            scode.compareTo("P005A")==0 ||
                            scode.compareTo("P004A")==0 ||
                            scode.compareTo("K017A")==0 ||
                            scode.compareTo("A903A")==0 || scode.compareTo("C903A")==0 ||
                            scode.compareTo("C003A")==0 || scode.compareTo("W102A")==0 ||
                            scode.compareTo("W109A")==0 || scode.compareTo("W903A")==0 ||
                            scode.compareTo("Z716A")==0 || scode.startsWith("S") ||
                            (scode.endsWith("B") && !scode.endsWith("C988B") && !scode.endsWith("C998B") && !scode.endsWith("C999B") ) ){
				pValue1 = pValue1.add(bdFee);
				pValue1PerUnit = pValue1PerUnit.add(bdFee);
				pCode = scode;
				pDesc = desc;
				pValue = pValue1.toString();

				if (!eCode.equals(""))	eFlag = "1";
			}

			desc = desc == null ? "" : desc;

			String str = fee.toString();
			StringBuffer sBuffer = new StringBuffer(str);
			int i = str.indexOf('.');
			sBuffer.deleteCharAt(i);
			sBuffer.insert(i,"");
			str = sBuffer.toString();
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=scode%></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=scode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=desc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="1">
		<td width="58%"><font size="2"><%=desc%></td>
		<td width="6%" align="right"><font size="2">1</td>
		<td width="14%" align="right"><font size="2"><%=fee%></td>
	</tr>
	<%
			counter = counter + 1;
		}
	}
}

if (otherflag1 == 1) {
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=otherdbcode1%></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode1%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=otherstr%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc1%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode1unit%>">
		<td width="58%"><font size="2"><%=otherdesc1%></td>
		<td width="6%" align="right"><font size="2"><%=othercode1unit%></td>
		<td width="14%" align="right"><font size="2"><%=otherfee%></td>
	</tr>
	<%
	counter = counter + 1;
}
if (otherflag2 == 1) {
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=otherdbcode2%></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode2%>">
		<input type="hidden" name="pricerec<%=counter%>"
			value="<%=otherstr2%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc2%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode2unit%>">
		<td width="58%"><font size="2"><%=otherdesc2%></td>
		<td width="6%">
		<div align="right"><font size="2"><%=othercode2unit%></div>
		</td>
		<td width="14%">
		<div align="right"><font size="2"><%=otherfee02%></div>
		</td>
	</tr>
	<%
	counter = counter + 1;
}
if (otherflag3 == 1) {
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=otherdbcode3%></td>
		<input type="hidden" name="billrec<%=counter%>"
			value="<%=otherdbcode3%>">
		<input type="hidden" name="pricerec<%=counter%>"
			value="<%=otherstr3%>">
		<input type="hidden" name="billrecdesc<%=counter%>"
			value="<%=otherdesc3%>">
		<input type="hidden" name="billrecunit<%=counter%>"
			value="<%=othercode3unit%>">
		<td width="58%"><font size="2"><%=otherdesc3%></td>
		<td width="6%">
		<div align="right"><font size="2"><%=othercode3unit%></div>
		</td>
		<td width="14%">
		<div align="right"><font size="2"><%=otherfee03%></div>
		</td>
	</tr>
	<%
	counter = counter + 1;
}

if (eFlag.compareTo("1")==0) {
	percent = new BigDecimal(Double.parseDouble(ePerc)).setScale(4, BigDecimal.ROUND_HALF_UP);
	percentPremium = percent.multiply(pValue1).setScale(2, BigDecimal.ROUND_HALF_UP);
	BigTotal = BigTotal.add(percentPremium);

	String str = percentPremium.toString();
	StringBuffer sBuffer = new StringBuffer(str);
	int i = str.indexOf('.');
	sBuffer.deleteCharAt(i);
	sBuffer.insert(i,"");
	str = sBuffer.toString();
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=eCode%></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=eCode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=eDesc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="<%=eUnit%>">
		<td width="58%"><font size="2"><%=eDesc%></td>
		<td width="6%">
		<div align="right"><font size="2"><%=eUnit%></div>
		</td>
		<td width="14%">
		<div align="right"><font size="2"><%=percentPremium%></div>
		</td>
	</tr>
	<%
	counter = counter + 1;
}

if (xFlag.compareTo("1")==0) {

	BigTotal = BigTotal.subtract(percentPremium);
	xPercent = new BigDecimal(Double.parseDouble(xPerc)).setScale(4, BigDecimal.ROUND_HALF_UP);
	xPercentPremium = xPercent.multiply(pValue1).setScale(2, BigDecimal.ROUND_HALF_UP);
	BigTotal = BigTotal.add(xPercentPremium);
	BigTotal = BigTotal.add(percentPremium);

	// add for unit based evening codes
	if (xCode.equals("E400B") || xCode.equals("E401B")) {
		// reset
		BigTotal = BigTotal.subtract(percentPremium);
		BigTotal = BigTotal.subtract(xPercentPremium);
		// calc xUnit
		xPercent = new BigDecimal(Double.parseDouble(xPerc)).setScale(4, BigDecimal.ROUND_HALF_UP);
		otherunit = new BigDecimal(Double.parseDouble(xUnit)).setScale(2, BigDecimal.ROUND_HALF_UP);
		xPercentPremium = pValue1PerUnit.multiply(otherunit).setScale(2, BigDecimal.ROUND_HALF_UP);
		xPercentPremium = xPercent.multiply(pValue1PerUnit).setScale(2, BigDecimal.ROUND_HALF_UP);

		BigTotal = BigTotal.add(xPercentPremium);
		BigTotal = BigTotal.add(percentPremium);
	}
	// end

	String str = xPercentPremium.toString();
	StringBuffer sBuffer = new StringBuffer(str);
	int i = str.indexOf('.');
	sBuffer.deleteCharAt(i);
	sBuffer.insert(i,"");
	str = sBuffer.toString();
%>
	<tr bgcolor="#EEEEFF">
		<td width="22%"><font size="2"><%=xCode%></td>
		<input type="hidden" name="billrec<%=counter%>" value="<%=xCode%>">
		<input type="hidden" name="pricerec<%=counter%>" value="<%=str%>">
		<input type="hidden" name="billrecdesc<%=counter%>" value="<%=xDesc%>">
		<input type="hidden" name="billrecunit<%=counter%>" value="<%=xUnit%>">
		<td width="58%"><font size="2"><%=xDesc%></td>
		<td width="6%">
		<div align="right"><font size="2"><%=xUnit%></div>
		</td>
		<td width="14%">
		<div align="right"><font size="2"><%=xPercentPremium%></div>
		</td>
	</tr>
	<%
	counter = counter + 1;
}

if (counter == 0) {
	errorFlag = "1";
	errorMsg = errorMsg + "Error: No Service/Procedure Code selected. <br>";
}

if (errorFlag.compareTo("1")==0){
%>

</table>

<p bgcolor="orange"><%=errorMsg%></p>
<% session.setAttribute("content", content); %>
<form><input type=button name=back value='Go Back and Change'
	onClick='javascript:location.href="billingOB.jsp?billForm=<%=request.getParameter("billForm")%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=request.getParameter("user_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("xml_appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=0"'>
</form>

<%
} else { %>

<tr bgcolor="#EEEEFF">
	<td width="22%"><font size="2"><b>Diagnostic Code</b></td>
	<td colspan="2"><font size="2"><%=diagnostic_code%></td>
	<td width="14%">
	<div align="right"><font size="2"></div>
	</td>
</tr>

<%
	if (request.getParameter("xml_research1") == null || request.getParameter("xml_research1").compareTo("") == 0) {
%>
<tr bgcolor="#EEEEFF">
	<td width="22%"><font size="2"></td>
	<td colspan="2">
	<div align="right"><font size="2">Total:</div>
	</td>
	<td width="14%">
	<div align="right"><font size="2"><%=BigTotal%></div>
	</td>
</tr>
</table>

<%
	} else {
%>

<tr bgcolor="#EEEEFF">
	<td width="22%"><font size="2"><b>Research Code</b></td>
	<td colspan="2"><font size="2"><%=request.getParameter("xml_research1")%>
	<%=request.getParameter("xml_research2").compareTo("")==0?"":", "+request.getParameter("xml_research2")%>
	<%=request.getParameter("xml_research3").compareTo("")==0?"":", "+request.getParameter("xml_research3")%></td>
	<td width="14%">
	<div align="right"><font size="2"></div>
	</td>
</tr>
<tr bgcolor="#EEEEFF">
	<td width="22%"><font size="2"></td>
	<td colspan="2">
	<div align="right"><font size="2">Total:</div>
	</td>
	<td width="14%">
	<div align="right"><font size="2"><%=BigTotal%></div>
	</td>
</tr>
</table>

<%	} %>
<p>
<%
	content = content + "<xml_providername>" +  proFirst + " " + proLast + "</xml_providername>";
	content = content + "<xml_apptprovidername>" + apptFirst + " " + apptLast + "</xml_apptprovidername>";
	content = content + "<xml_asstprovidername>" +  asstFirst + " " + asstLast + "</xml_asstprovidername>";
	content = content + "<xml_creator>" +  crFirst + " " + crLast + "</xml_creator>";
	content = content + "<rdohip>" + r_doctor_ohip+"</rdohip>" + "<rd>" + r_doctor + "</rd>";
	content = content + "<hctype>" + demoHCTYPE+"</hctype>" + "<demosex>" + demoSex + "</demosex>";
%> <input type="hidden" name="pohip_no" value="<%=proOHIPNO%>">
<input type="hidden" name="prma_no" value="<%=proRMA%>"> <input
	type="hidden" name="record" value="<%=counter%>"> <input
	type="hidden" name="diagcode" value="<%=diagcode%>"> <input
	type="hidden" name="visittype"
	value="<%=request.getParameter("xml_visittype").substring(0,2)%>">
<input type="hidden" name="billtype"
	value="<%=billtype.substring(0,1)%>"> <!--input type="hidden" name="content" value="<%--=content--%>"-->
<input type="hidden" name="provider_no" value="<%=proNO%>"> <input
	type="hidden" name="clinic_no"
	value="<%=request.getParameter("clinic_no")%>"> <input
	type="hidden" name="demographic_no"
	value="<%=request.getParameter("demographic_no")%>"> <input
	type="hidden" name="billing_name" value="OB2"> <!--input type="hidden" name="user_no" value="<%=request.getParameter("user_no")%>"-->
<input type="hidden" name="apptProvider_no"
	value="<%=request.getParameter("apptProvider_no")%>"> <input
	type="hidden" name="asstProvider_no"
	value="<%=request.getParameter("asstProvider_no")%>"> <!--input type="hidden" name="billing_date" value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>"-->
<!--input type="hidden" name="billing_time" value="<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)%>"-->
<input type="hidden" name="billingservice_code" value=""> 
<input
	type="hidden" name="appointment_date"
	value="<%=request.getParameter("xml_appointment_date")%>"> <input
	type="hidden" name="appointment_no"
	value="<%=request.getParameter("appointment_no")%>"> <input
	type="hidden" name="status" value="<%=request.getParameter("status")%>">
<input type="hidden" name="start_time"
	value="<%=request.getParameter("start_time")%>"> <input
	type="hidden" name="displaymode" value="savebill"> <input
	type="hidden" name="demographic_dob" value="<%=demoDOB%>"> <input
	type="hidden" name="demographic_name" value="<%=demoname%>"> <input
	type="hidden" name="hin" value="<%=demoHIN%>"> <input
	type="hidden" name="ohip_version" value="V03G"> <input
	type="hidden" name="total" value="<%=BigTotal%>"> <input
	type="hidden" name="clinic_ref_code" value="<%=location1%>"> <input
	type="hidden" name="visitdate" value="<%=visitdate%>"> <input
	type="submit" name="Submit" value="Confirm" onDblClick=""> <a
	href="billingOB.jsp?billForm=<%=request.getParameter("billForm")%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=request.getParameter("appointment_no")%>&demographic_name=<%=URLEncoder.encode(demoname)%>&demographic_no=<%=request.getParameter("demographic_no")%>&user_no=<%=request.getParameter("user_no")%>&providerview=<%=request.getParameter("apptProvider_no")%>&apptProvider_no=<%=request.getParameter("apptProvider_no")%>&appointment_date=<%=request.getParameter("xml_appointment_date")%>&status=<%=request.getParameter("status")%>&start_time=<%=request.getParameter("start_time")%>&bNewForm=0">edit</a>

<%
	if (warningMsg.length() > 0) {
%>

<p bgcolor="yellow"><%=warningMsg%></p>
<%
	}
	session.setAttribute("content", content);
}

if(bPercS) {
    billing_pCode = oscarVariables.getProperty("billing_pCode","");
}
%>
</form>

<script language="JavaScript">

function addToDiseaseRegistry(){
    if ( validateItems() ) {
	var url = "../../../oscarResearch/oscarDxResearch/dxResearch.do";
	var data = Form.serialize(dxForm);
	//alert ( data);
	new Ajax.Updater('dxListing',url, {method: 'post',postBody: data,asynchronous:true,onComplete: getNewCurrentDxCodeList});
    }else{
       alert("Error: Nothing was selected");
    }
}

function validateItems(form){
    var dxChecks;
    var ret = false;

    dxChecks = document.getElementsByName("xml_research");

     for( idx = 0; idx < dxChecks.length; ++idx ) {
        if( dxChecks[idx].checked ) {
            ret = true;
            break;
        }
     }
    return ret;
}


function getNewCurrentDxCodeList(origRequest){
   //alert("calling get NEW current Dx Code List");
   var url = "../../../oscarResearch/oscarDxResearch/currentCodeList.jsp";
   var ran_number=Math.round(Math.random()*1000000);
   var params = "demographicNo=<%=demoNO%>&rand="+ran_number;  //hack to get around ie caching the page
   //alert(params);
   new Ajax.Updater('dxFullListing',url, {method:'get',parameters:params,asynchronous:true});
   //alert(origRequest.responseText);
}


</script>


<oscar:oscarPropertiesCheck property="DX_QUICK_LIST_BILLING_REVIEW"
	value="yes">

	<div class="dxBox">
	<h3>&nbsp;Dx Quick Pick Add List</h3>
	<form id="dxForm"><input type="hidden" name="demographicNo"
		value="<%=demoNO%>" /> <input type="hidden" name="providerNo"
		value="<%=proNO%>" /> <input type="hidden" name="forward" value="" />
	<input type="hidden" name="forwardTo" value="codeList" />
	<div class="wrapper" id="dxListing"><jsp:include
		page="../../../oscarResearch/oscarDxResearch/quickCodeList.jsp">
		<jsp:param name="demographicNo" value="<%=demoNO%>" />
	</jsp:include></div>
	<input type="button" value="Add To Disease Registry"
		onclick="addToDiseaseRegistry()" /> <!--input type="button" value="check" onclick="getNewCurrentDxCodeList()"/>
<input type="button" value="check" onclick="validateItems()"/--></form>
	</div>


	<div class="dxBox">
	<h3>&nbsp;Current Patient Dx List <a href="#"
		onclick="Element.toggle('dxFullListing'); return false;"
		style="font-size: small;">show/hide</a></h3>
	<div class="wrapper" id="dxFullListing" style="display: none;"><jsp:include
		page="../../../oscarResearch/oscarDxResearch/currentCodeList.jsp">
		<jsp:param name="demographicNo" value="<%=demoNO%>" />
	</jsp:include></div>
	</div>
</oscar:oscarPropertiesCheck>

</body>
</html>
