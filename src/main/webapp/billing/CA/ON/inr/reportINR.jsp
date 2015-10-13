<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
String user_no = (String) session.getAttribute("user");
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.*, java.sql.*, java.net.*" %>
<%@ include file="../../../../admin/dbconnection.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingInr" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingInrDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%
	ProviderDao providerDao= SpringUtils.getBean(ProviderDao.class);
	BillingInrDao billingInrDao = SpringUtils.getBean(BillingInrDao.class);

%>

<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation" %>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
  String nowTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
 String clinicview = oscarVariables.getProperty("clinic_view");
   String Clinic_no = oscarVariables.getProperty("clinic_no");


  %>

<html>
<head>
<title><bean:message key="admin.admin.btnINRBatchBilling"/></title>
<script language="JavaScript">
<!--
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}

function jumpMenu(targ,selObj,restore){
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
var remote=null;

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


//-->
</script>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<h3><bean:message key="admin.admin.btnINRBatchBilling"/></h3>

<div class="container-fluid well">
<button class="btn" type='button' name='print' value='Print' onClick='window.print()'><i class="icon icon-print"></i> Print</button>

<% String providerview=request.getParameter("provider_no")==null?"":request.getParameter("provider_no");

 %>
	<form name="serviceform" method="post"
		action="<%=oscarVariables.getProperty("isNewONbilling","").equals("true")? "onGenINRbilling.jsp":"genINRbilling.jsp" %>">
	Select provider 
	<select name="provider" onChange="jumpMenu('parent',this,0)" class="span2">
		<option value="#">Select Provider</option>
		<option value="reportINR.jsp?provider_no=all"
				<%=providerview.equals("all")?"selected":""%>><b>All
			Provider</b></option>
			<% String proFirst="";
           String proLast="";
           String proName="";
            String proName1="";
           String proOHIP="";
          String specialty_code;
String billinggroup_no;
ArrayList providerArray = new ArrayList();
String[] providerArr = new String[2];
           int Count = 0;
           

for(Provider p:providerDao.getActiveProviders()) {
	if(p.getOhipNo() == null || p.getOhipNo().isEmpty())
		continue;
	
 proFirst = p.getFirstName();
 proLast = p.getLastName();
proName = proFirst + " " + proLast;
 proOHIP = p.getProviderNo();

providerArr[0] = proOHIP;
providerArr[1] = proName;
providerArray.add(providerArr);
 %>
			<option value="reportINR.jsp?provider_no=<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%

 }
//
  %>
	</select>
	Clinic Location:
	<input type="hidden" name="billcenter" value="G">
	<select name="xml_location" datafld='xml_location' class="span4">
		<% ResultSet rsclinic = null;
            String clinic_location="", clinic_code="";
            List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
            for(ClinicLocation clinicLocation:clinicLocations) {
           	 clinic_location = clinicLocation.getClinicLocationName();
                clinic_code = clinicLocation.getClinicLocationNo();
            %>
		<option value="<%=clinic_code%>"
			<%=clinicview.equals(clinic_code)?"selected":""%>><%=clinic_location%></option>
		<%
            }
            %>
	</select>
	<input type="hidden" name="verCode" value="V03"> 
	<input type="hidden" name="curUser" value="<%=user_no%>"> 
	<input type="hidden" name="curDate" value="<%=nowDate%>"> 
	<input type="hidden" name="curTime" value="<%=nowTime%>">
	
	<table class="table table-striped  table-condensed">

			<tr>
				<td>Selection</td>
				<td>Demographic</td>
				<td>Provider</td>
				<td>Service</td>
				<td>Amount</td>
				<td>Diagnostic</td>
				<td>Last Bill Date</td>

			</tr>
			<% String demono="",demo_name="",demo_dob="",demo_hin="", billinginr_no="", provider_no="";
    String provider_ohip_no="", provider_rma_no="", diagnostic_code="", service_code="", billing_amount="";
    String billing_unit="", billdate="", billstatus="";
    int colorCount = 0;
       String color="";
  	int Count1 = 0;

       if (providerview.compareTo("all")==0){
	   for(BillingInr b:billingInrDao.findCurrentByProviderNo("%")) {

      billinginr_no = b.getId().toString();
      demono = String.valueOf(b.getDemographicNo());
      demo_name = b.getDemographicName();
      demo_hin = b.getHin();
      demo_dob = b.getDob();
      provider_no = b.getProviderNo();

      Provider p = providerDao.getProvider(provider_no);
      if(p != null) {
    	  proFirst = p.getFirstName();
    	  proLast = p.getLastName();
    	  proName1 = proFirst + " " + proLast;
      }
       
      provider_ohip_no = b.getProviderOhipNo();
      provider_rma_no = b.getProviderRmaNo();
      diagnostic_code = b.getDiagnosticCode();
      service_code = b.getServiceCode();
      billing_amount = b.getBillingAmount();
      billing_unit = b.getBillingUnit();
      billdate = ConversionUtils.toDateString(b.getCreateDateTime());
      billstatus = b.getStatus();
      if (colorCount == 0){
    	      colorCount = 1;
    	      color = "#FFFFFF";
    	      } else {
    	      colorCount = 0;
	      color="#EEEEFF";
	      }
  Count1 = Count1 + 1;
    %>
			<tr>
				<td width="12%" height="16"><input type="checkbox"
					name="inrbilling<%=billinginr_no%>"></td>
				<td width="22%" height="16"><a href="#"
					onClick='rs("billinginrupdate","updateINRbilling.jsp?demono=<%=demono%>&billinginr_no=<%=billinginr_no%>&servicecode=<%=service_code%>&billingamount=<%=billing_amount%>&dxcode=<%=diagnostic_code%>&demo_name=<%=URLEncoder.encode(demo_name)%>&provider_name=<%=URLEncoder.encode(proName1)%>","380","300","0")'><%=demo_name%></a></td>
				<td width="22%" height="16"><%=proName1%></td>
				<td width="12%" height="16"><%=service_code%></td>
				<td width="12%" height="16"><%=billing_amount%></td>
				<td width="10%" height="16"><%=diagnostic_code%></td>
				<td width="10%" height="16">
				<% if (billstatus.compareTo("A")==0) {%><%=billdate.substring(0,10)%>
				<%}else{%>Not Available<%}%>
				</td>
			</tr>
			<%}
  } else{

	  for(BillingInr b:billingInrDao.findCurrentByProviderNo(providerview)) {
      
		     billinginr_no = b.getId().toString();
		      demono = String.valueOf(b.getDemographicNo());
		      demo_name = b.getDemographicName();
		      demo_hin = b.getHin();
		      demo_dob = b.getDob();
		      provider_no = b.getProviderNo();

		      Provider p = providerDao.getProvider(provider_no);
		      if(p != null) {
		    	  proFirst = p.getFirstName();
		    	  proLast = p.getLastName();
		    	  proName1 = proFirst + " " + proLast;
		      }
		       
		      provider_ohip_no = b.getProviderOhipNo();
		      provider_rma_no = b.getProviderRmaNo();
		      diagnostic_code = b.getDiagnosticCode();
		      service_code = b.getServiceCode();
		      billing_amount = b.getBillingAmount();
		      billing_unit = b.getBillingUnit();
		      billdate = ConversionUtils.toDateString(b.getCreateDateTime());
		      billstatus = b.getStatus();
		      
       
        if (colorCount == 0){
      	      colorCount = 1;
      	      color = "#FFFFFF";
      	      } else {
      	      colorCount = 0;
  	      color="#EEEEFF";
  	      }

  Count1 = Count1 + 1;
  %>
	<tr>
		<td width="12%" height="16"><input type="checkbox"
			name="inrbilling<%=billinginr_no%>"></td>
		<td width="22%" height="16"><a href="#"
			onClick='rs("billinginrupdate","updateINRbilling.jsp?demono=<%=demono%>&billinginr_no=<%=billinginr_no%>&servicecode=<%=service_code%>&billingamount=<%=billing_amount%>&dxcode=<%=diagnostic_code%>&demo_name=<%=URLEncoder.encode(demo_name)%>&provider_name=<%=URLEncoder.encode(proName1)%>","380","300","0")'><%=demo_name%></a></td>
		<td width="22%" height="16"><%=proName1%></td>
		<td width="12%" height="16"><%=service_code%></td>
		<td width="12%" height="16"><%=billing_amount%></td>
		<td width="10%" height="16"><%=diagnostic_code%></td>
		<td width="10%" height="16">
		<% if (billstatus.compareTo("A")==0) {%><%=billdate.substring(0,10)%>
		<%}else{%>Not Available<%}%>
		</td>
	</tr>
	<%}}%>
	<% if ( Count1 == 0) { %>
	<tr>
		<td colspan=7>No Match Found</td>
		<%} else {%>
	</tr>
	<tr>
		<td>
			<a href="#"
				onClick='rs("billingcalendar","../billingCalendarPopup.jsp?year=<%=curYear%>&month=<%=curMonth%>&type=service","380","300","0")'>Service
			Date:</a>
			<input type="text" name="xml_appointment_date"
				value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>"
				size="12" datafld='xml_appointment_date'>
		</td>
		<td colspan=7>
			<input type="submit" name="submit" value="Generate INR Batch Billing"> 
			<input type="hidden" name="rowCount" value="<%=Count1%>"> 
			<input type="hidden" name="clinic_no" value="<%=Clinic_no%>">
			<input type="hidden" name="visittype" value="00">
		</td>
 	</tr>
<%
}
%>
</table>
 </form>
</div>
</body>
</html>
