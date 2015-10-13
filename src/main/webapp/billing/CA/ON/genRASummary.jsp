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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, java.net.*,oscar.*, oscar.util.*, oscar.MyDateFormat,oscar.oscarDB.*" errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>

<jsp:useBean id="billingLocalInvNoBean" class="java.util.Properties" scope="page" />

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.RaHeader" %>
<%@page import="org.oscarehr.common.dao.RaHeaderDao" %>
<%@page import="org.oscarehr.common.model.RaDetail" %>
<%@page import="org.oscarehr.common.dao.RaDetailDao" %>
<%@page import="org.oscarehr.common.dao.BillingDao"%>
<%@page import="org.oscarehr.common.model.Billing" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	RaHeaderDao dao = SpringUtils.getBean(RaHeaderDao.class);
	RaDetailDao raDetailDao = SpringUtils.getBean(RaDetailDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>


<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="billing.css">
<title>Billing Reconcilliation</title>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print(); return false;'></th>
		<th><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">
		Billing Reconcilliation - Payment Summary</font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>
<% 
String nowDate = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy/MM/dd"); 

String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="", proName="";
String sqlRACO="",sqlRAOB="", OBflag="0",COflag="0", amountOB="", amountCO="";
String demo_name ="",demo_hin="", demo_docname="",providerOhipNo = "";
int accountno=0 ;

raNo = request.getParameter("rano");

// set localClinic record bean
billingLocalInvNoBean = new Properties();
String localClinicNo = oscarVariables.getProperty("clinic_no");

RaHeader rh = dao.find(Integer.parseInt(raNo));
if(rh != null && !rh.getStatus().equals("D")) {
	filename = rh.getFilename();
}

filepath = oscarVariables.getProperty("DOCUMENT_DIR").trim(); //"/usr/local/OscarDocument/" + url +"/document/";
FileInputStream file = new FileInputStream(filepath + filename);
InputStreamReader reader = new InputStreamReader(file);
BufferedReader input = new BufferedReader(reader);
String nextline;

while ((nextline=input.readLine())!=null){
	header = nextline.substring(0,1);

	if (header.compareTo("H") == 0) { 
		headerCount = nextline.substring(2,3);

		if (headerCount.compareTo("4") == 0){
			account = nextline.substring(23,31);
			location = nextline.substring(69,73);

			String validnum = "0123456789- ";
			boolean valid = true;
			for (int i = 0; i < account.length(); i++) {
				char c = account.charAt(i);
				if (validnum.indexOf(c) == -1) {
					valid = false;
					break;
				}
			}

			if (account.trim().length() == 0) account = "0";

			if (valid){
				accountno= Integer.parseInt(account.trim());
				account = String.valueOf(accountno);
			}
			// add a bean
			if (location.equals(localClinicNo)) {
				billingLocalInvNoBean.setProperty(account, localClinicNo);
			}
		}
	}
}

// sqlOBfee = "select distinct billing_no from radetail where raheader_no='"+raNo+"' and (service_code='P006A' or service_code='P020A' or service_code='P022A' or service_code='P028A' or service_code='P023A' or service_code='P007A' or service_code='P008B' or service_code='P018B' or service_code='E502A' or service_code='C989A' or service_code='E409A' or service_code='E410A' or service_code='E411A' or service_code='H001A')";

ArrayList OBbilling_no = new ArrayList();
ArrayList CObilling_no = new ArrayList();

for(Integer i:raDetailDao.search_raob(Integer.parseInt(raNo))) {
	OBbilling_no.add(i.toString());
}

for(Integer i:raDetailDao.search_racolposcopy(Integer.parseInt(raNo))) {
	CObilling_no.add(i.toString());
}


Properties propProvierName = new Properties();

for(Provider p:providerDao.getActiveProviders()) {
	if(p.getOhipNo() != null) {
		propProvierName.setProperty( ("no_" + p.getProviderNo()), (p.getLastName() + "," + p.getFirstName()) );
		propProvierName.setProperty(p.getOhipNo(), (p.getLastName() + "," + p.getFirstName()) );
	}
}


BigDecimal bdCFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdPFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdOFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     
BigDecimal bdCOFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);     	     

BigDecimal bdFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdHFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigCTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigPTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigOTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigCOTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigLTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigHTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal bdOBFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigOBTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
double dHFee = 0.00;        
double dFee = 0.00;
double dCOFee = 0.00; 
double dOBFee = 0.00; 
double dCFee = 0.00;       	
double dPFee = 0.00;       	       	
double dOFee = 0.00;

double dLocalHFee = 0.00;        
BigDecimal bdLocalHFee = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
BigDecimal BigLocalHTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
String localServiceDate = "";
       	
proNo = request.getParameter("proNo");
if (raNo.compareTo("") == 0 || raNo == null){
	flag = "0";
	return;
} else {
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#333333">
		<th align='CENTRE' nowrap>
		<form action="genRASummary.jsp"><input type="hidden"
			name="rano" value="<%=raNo%>"> <select name="proNo">
			<!--option value="all"  <%--=proNo.equals("all")?"selected":""--%>>All Providers</option-->

			<%
	
	for(Object[] res:raDetailDao.search_raprovider(Integer.parseInt(raNo))) {
		RaDetail rad = (RaDetail)res[0];
		Provider prov = (Provider)res[1];
		
		pohipno = rad.getProviderOhipNo();
		plast = prov.getLastName();
		pfirst = prov.getFirstName();
	
%>
			<option value="<%=pohipno%>""selected"><%=plast%>,<%=pfirst%></option>
			<%	} %>
		</select> <input type="submit" name="submit" value="Generate"> <a
			href="genRASummaryDetail.jsp?rano=<%=raNo%>&proNo=">Detail</a></form>
		</th>
	</tr>
</table>

<% 
	if (proNo == null || proNo.compareTo("") == 0 || proNo.compareTo("all") == 0){ 
%>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<form>
	<tr>
		<td width="7%" height="16">Billing No</td>
		<td width="14%" height="16">Provider</td>
		<td width="15%" height="16">Patient</td>
		<td width="7%" height="16">HIN</td>
		<td width="10%" height="16">Service Date</td>
		<td width="7%" height="16">Service Code</td>
		<!-- <td width="8%" height="16">Count</td> -->
		<td width="7%" height="16" align=right>Invoiced</td>
		<td width="7%" height="16" align=right>Paid</td>
		<td width="7%" height="16" align=right>Clinic Pay</td>
		<td width="7%" height="16" align=right>Hospital Pay</td>
		<td width="7%" height="16" align=right>OB</td>
		<td width="5%" height="16" align=right>Error</td>
	</tr>

	<%
	for(RaDetail rad:raDetailDao.search_rasummary_dt(Integer.parseInt(raNo),"%")) {
		account = String.valueOf(rad.getBillingNo());
		location = "";  
		demo_name = "";
		demo_docname = "";
		demo_hin = rad.getHin() != null? rad.getHin() : "";
		Billing b = billingDao.find(Integer.parseInt(account));
		if(b != null) {
			demo_name = b.getDemographicName();
			if (b.getHin() != null) {
				if (!(b.getHin()).startsWith(demo_hin)) {
					demo_hin = "";
					demo_name ="";
				}
			} else {
				demo_hin = "";
				demo_name ="";
			}
			location = b.getVisitType();
			localServiceDate = ConversionUtils.toDateString(b.getBillingDate());
			localServiceDate = localServiceDate.replaceAll("-*", "");
			demo_docname = propProvierName.getProperty(("no_" + b.getProviderNo()), "");
		}
        providerOhipNo = rad.getProviderOhipNo();
		proName = propProvierName.getProperty(providerOhipNo);
		servicecode = rad.getServiceCode();
		servicedate = rad.getServiceDate();
		serviceno = rad.getServiceCount();
		explain = rad.getErrorCode();
		amountsubmit =rad.getAmountClaim();
		amountpay = rad.getAmountPay();

		//OBflag="0";
		// get claim/pay amount
		dCFee = Double.parseDouble(amountsubmit);
		bdCFee = new BigDecimal(dCFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigCTotal = BigCTotal.add(bdCFee);

		dPFee = Double.parseDouble(amountpay);
		bdPFee = new BigDecimal(dPFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigPTotal = BigPTotal.add(bdPFee);

		OBflag="0";
		COflag="0";
		// set flag
		for (int i=0; i<OBbilling_no.size(); i++){
			sqlRAOB = (String)OBbilling_no.get(i);
			if(sqlRAOB.compareTo(account)==0) {
				OBflag = "1";
				break;
			}
		}
		for (int j=0; j<CObilling_no.size(); j++){
			sqlRACO = (String)CObilling_no.get(j);
			if(sqlRACO.compareTo(account)==0) {
				COflag = "1";
				break;
			}
		}
      	    
		if(OBflag.equals("1")) {
			amountOB=amountpay;
			dOBFee = Double.parseDouble(amountOB);
			bdOBFee = new BigDecimal(dOBFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigOBTotal = BigOBTotal.add(bdOBFee);
		} else {
			amountOB="N/A";
		} 

		if(COflag.equals("1")) {
			amountCO=amountpay;
			dCOFee = Double.parseDouble(amountCO);
			bdCOFee = new BigDecimal(dCOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigCOTotal = BigCOTotal.add(bdCOFee);
		} else {
			amountCO="N/A";
		} 


		if (explain.compareTo("") == 0 || explain == null){
			explain = "**";
		}

		if (location.compareTo("02") == 0) { // hospital
			dHFee = Double.parseDouble(amountpay);
			bdHFee = new BigDecimal(dHFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigHTotal = BigHTotal.add(bdHFee);

			// is local for hospital
			if (demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
				BigLocalHTotal = BigLocalHTotal.add(bdHFee);
			}
%>

	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>


	<%
		} else { // clinic && local clinic   providerOhipNo
                    
			if (location.compareTo("00") == 0 && wasBilledLocal(account,providerOhipNo,servicedate,servicecode)){ //billingLocalInvNoBean.getProperty(account, "").equals(localClinicNo)) {
				dFee = Double.parseDouble(amountpay);
				bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigTotal = BigTotal.add(bdFee);
%>
	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>

	<%
			} else { // other fee
				dOFee = Double.parseDouble(amountpay);
				bdOFee = new BigDecimal(dOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigOTotal = BigOTotal.add(bdOFee);
%>
	<tr>
		<td height="16"><%=account%></td>
		<td height="16"><%=demo_docname%></td>
		<td height="16"><%=demo_name%></td>
		<td height="16"><%=demo_hin%></td>
		<td height="16"><%=servicedate%></td>
		<td height="16"><%=servicecode%></td>
		<!-- <td width="8%" height="16"><%=serviceno%></td>-->
		<td height="16" align=right><%=amountsubmit%></td>
		<td height="16" align=right><%=amountpay%></td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right>N/A</td>
		<td height="16" align=right><%=amountOB%></td>
		<td height="16" align=right><%=explain%></td>
	</tr>
	<%
			}
		}
	}	 
} else { // raNo for all providers
%>

	<table width="100%" border="1" cellspacing="0" cellpadding="0"
		bgcolor="#EFEFEF">
		<tr>
			<td width="7%" height="16">Billing No</td>
			<td width="14%" height="16">Provider</td>
			<td width="15%" height="16">Patient</td>
			<td width="7%" height="16">HIN</td>
			<td width="10%" height="16">Service Date</td>
			<td width="7%" height="16">Service Code</td>
			<!-- <td width="8%" height="16">Count</td> -->
			<td width="7%" height="16" align=right>Invoiced</td>
			<td width="7%" height="16" align=right>Paid</td>
			<td width="7%" height="16" align=right>Clinic Pay</td>
			<td width="7%" height="16" align=right>Hospital Pay</td>
			<td width="7%" height="16" align=right>OB</td>
			<td width="5%" height="16" align=right>Error</td>
		</tr>

		<%
	for(RaDetail rad:raDetailDao.search_rasummary_dt(Integer.parseInt(raNo), proNo+"%")) {
		account = String.valueOf(rad.getBillingNo());
		location = "";
		demo_name ="";
		demo_docname = "";
		demo_hin = rad.getHin() != null? rad.getHin() : "";
		
		Billing b = billingDao.find(Integer.parseInt(account));
		
		if(b != null) {
			demo_name = b.getDemographicName();
			if (b.getHin() != null) {
				if (!(b.getHin()).startsWith(demo_hin)) {
					demo_hin = "";
					demo_name ="";
				}
			} else {
				demo_hin = "";
				demo_name ="";
			}
			location = b.getVisitType();
			localServiceDate = ConversionUtils.toDateString(b.getBillingDate());
			localServiceDate = localServiceDate.replaceAll("-*", "");
			demo_docname = propProvierName.getProperty(("no_" + b.getProviderNo()), "");
		}

        providerOhipNo = rad.getProviderOhipNo();
		proName = propProvierName.getProperty(providerOhipNo);
		servicecode = rad.getServiceCode();
		servicedate = rad.getServiceDate();
		serviceno = rad.getServiceCount();
		explain = rad.getErrorCode();
		amountsubmit = rad.getAmountClaim();
		amountpay = rad.getAmountPay();

		//k     location = rsdemo.getString("visittype");
		dCFee = Double.parseDouble(amountsubmit);
		bdCFee = new BigDecimal(dCFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigCTotal = BigCTotal.add(bdCFee);

		dPFee = Double.parseDouble(amountpay);
		bdPFee = new BigDecimal(dPFee).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigPTotal = BigPTotal.add(bdPFee);
		COflag="0";
		OBflag="0";

		for (int i=0; i<OBbilling_no.size(); i++){
			sqlRAOB = (String)OBbilling_no.get(i);
			if(sqlRAOB.compareTo(account)==0) {
				OBflag = "1";
				break;
			}
		}

		for (int j=0; j<CObilling_no.size(); j++){
			sqlRACO = (String)CObilling_no.get(j);
			if(sqlRACO.compareTo(account)==0) {
				COflag = "1";
				break;
			}
		}

		if(OBflag.equals("1")) {
			amountOB=amountpay;
			dOBFee = Double.parseDouble(amountOB);
			bdOBFee = new BigDecimal(dOBFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigOBTotal = BigOBTotal.add(bdOBFee);
		}else{
			amountOB="N/A";
		} 

		if(COflag.equals("1")) {
			amountCO=amountpay;
			dCOFee = Double.parseDouble(amountCO);
			bdCOFee = new BigDecimal(dCOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigCOTotal = BigCOTotal.add(bdCOFee);
		}else{
			amountCO="N/A";
		} 

		if (explain.compareTo("") == 0 || explain == null){
			explain = "**";
		}      

		if (location.compareTo("02") == 0) {
			dHFee = Double.parseDouble(amountpay);
			bdHFee = new BigDecimal(dHFee).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigHTotal = BigHTotal.add(bdHFee);
			
			// is local for hospital
			if (demo_hin.length() > 1 && servicedate.equals(localServiceDate)) {
				BigLocalHTotal = BigLocalHTotal.add(bdHFee);
			}
%>
		<tr>
			<td height="16"><%=account%></td>
			<td height="16"><%=demo_docname%></td>
			<td height="16"><%=demo_name%></td>
			<td height="16"><%=demo_hin%></td>
			<td height="16"><%=servicedate%></td>
			<td height="16"><%=servicecode%></td>
			<!--<td width="8%" height="16"><%=serviceno%></td>-->
			<td height="16" align=right><%=amountsubmit%></td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right>N/A</td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right><%=amountOB%></td>
			<td height="16" align=right><%=explain%></td>
		</tr>

		<%
		} else {     
			if (location.compareTo("00") == 0 && wasBilledLocal(account,providerOhipNo,servicedate,servicecode)){ //billingLocalInvNoBean.getProperty(account, "").equals(localClinicNo)) {
				dFee = Double.parseDouble(amountpay);
				bdFee = new BigDecimal(dFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigTotal = BigTotal.add(bdFee);
%>
		<tr>
			<td height="16"><%=account%></td>
			<td height="16"><%=demo_docname%></td>
			<td height="16"><%=demo_name%></td>
			<td height="16"><%=demo_hin%></td>
			<td height="16"><%=servicedate%></td>
			<td height="16"><%=servicecode%></td>
			<!-- <td width="8%" height="16"><%=serviceno%></td>-->
			<td height="16" align=right><%=amountsubmit%></td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right>N/A</td>
			<td height="16" align=right><%=amountOB%></td>
			<td height="16" align=right><%=explain%></td>
		</tr>

		<%
			} else{ 
				dOFee = Double.parseDouble(amountpay);
				bdOFee = new BigDecimal(dOFee).setScale(2, BigDecimal.ROUND_HALF_UP);
				BigOTotal = BigOTotal.add(bdOFee);
%>
		<tr>
			<td height="16"><%=account%></td>
			<td height="16"><%=demo_docname%></td>
			<td height="16"><%=demo_name%></td>
			<td height="16"><%=demo_hin%></td>
			<td height="16"><%=servicedate%></td>
			<td height="16"><%=servicecode%></td>
			<!-- <td width="8%" height="16"><%=serviceno%></td>-->
			<td height="16" align=right><%=amountsubmit%></td>
			<td height="16" align=right><%=amountpay%></td>
			<td height="16" align=right>N/A</td>
			<td height="16" align=right>N/A</td>
			<td height="16" align=right><%=amountOB%></td>
			<td height="16" align=right><%=explain%></td>
		</tr>

		<%
			}
		}

	}
}

}

BigLTotal = BigLTotal.add(BigTotal);
//BigLTotal = BigLTotal.add(BigHTotal);
BigLTotal = BigLTotal.add(BigLocalHTotal);
%>
		<tr bgcolor='#FFFF3E'>
			<td height="16"></td>
			<td height="16"></td>
			<td height="16"></td>
			<td height="16"></td>
			<td height="16"></td>
			<td height="16">Total</td>
			<td height="16" align=right><%=BigCTotal%></td>
			<td height="16" align=right><%=BigPTotal%><!-- <%=BigOTotal%>--></td>
			<td height="16" align=right><%=BigTotal%><!--<%=BigLTotal%>--></td>
			<td height="16" align=right><%=BigHTotal%></td>
			<td height="16" align=right><%=BigOBTotal%></td>
			<td height="16"></td>
		</tr>
	</table>

	<%
String transaction="", content="", balancefwd="", xtotal="", other_total="", ob_total=""; 
rh = dao.find(Integer.parseInt(raNo));
if(rh != null) {
	transaction= SxmlMisc.getXmlContent(rh.getContent(),"<xml_transaction>","</xml_transaction>");
	balancefwd= SxmlMisc.getXmlContent(rh.getContent(),"<xml_balancefwd>","</xml_balancefwd>");
}
content = content + "<xml_transaction>" + transaction + "</xml_transaction>" + "<xml_balancefwd>" + balancefwd + "</xml_balancefwd>";
content = content + "<xml_local>" + BigLTotal + "</xml_local>"+ "<xml_total>" + BigPTotal + "</xml_total>" + "<xml_other_total>" + BigOTotal + "</xml_other_total>" + "<xml_ob_total>" + BigOBTotal + "</xml_ob_total>" + "<xml_co_total>" + BigCOTotal + "</xml_co_total>";

int recordAffected=0;
 
	 RaHeader raHeader = dao.find(Integer.parseInt(raNo));
	 if(raHeader != null) {
		 raHeader.setContent(content);
		 dao.merge(raHeader);
		recordAffected++;
	 }
	 
file.close();
reader.close();
input.close();
%>

</body>
</html>
<%!
//Before the system used the location code coming back from the ministry to find out whether the billing was billed locally or not.
//
//This has now stopped so we have come up with a new method.
//For an invoice number. Does it exist for this physician with that date and billing code in the system.  If so its local if not its not!
//
//Not sure whether to check a if the code being billed for is applied to the same hin insurance number.  

boolean wasBilledLocal(String account,String provider,String billing_date, String code){
   boolean wasbilledlocal = false; 
   java.util.Date date = UtilDateUtilities.getDateFromString(billing_date, "yyyyMMdd");
   BillingDao dao = SpringUtils.getBean(BillingDao.class);
   wasbilledlocal = dao.findBillingsByManyThings(ConversionUtils.fromIntString(account), date, provider, code).size() >= 1;
   return wasbilledlocal;
}
%>
