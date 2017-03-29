<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="oscar.util.DateUtils"%>
<%@page import="oscar.OscarProperties"%>
<%
String user_no="";
user_no = (String) session.getAttribute("user");
%>
<%@ include file="../../../casemgmt/taglibs.jsp" %>
<%@ page import="java.util.*, java.sql.*, java.net.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.ClinicLocationDao, org.oscarehr.PMmodule.dao.ProviderDao, org.oscarehr.common.dao.BatchBillingDAO, org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.ClinicLocation, org.oscarehr.common.model.Provider, org.oscarehr.common.model.BatchBilling, org.oscarehr.common.model.Demographic" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%
	ClinicLocationDao clinicLocationDao = (ClinicLocationDao)SpringUtils.getBean("clinicLocationDao");
%>
<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
	String nowTime = now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
   	String clinicview = OscarProperties.getInstance().getProperty("clinic_view");
   	String Clinic_no = OscarProperties.getInstance().getProperty("clinic_no");
   	String servicecode = request.getParameter("service_code");

   	BatchBillingDAO batchBillingDAO = (BatchBillingDAO) SpringUtils.getBean("batchBillingDAO");
   	String providerview=request.getParameter("provider_no")==null?"":request.getParameter("provider_no");
   	List<BatchBilling>batchBillings = null;
   	
   	if( !providerview.equalsIgnoreCase("#") && !providerview.equalsIgnoreCase("") ) {
   		if (providerview.compareTo("all")==0){
			if( servicecode.equals("all") ) {
				batchBillings = batchBillingDAO.findAll();
			}
			else{
				batchBillings = batchBillingDAO.findByServiceCode(servicecode);
			}
   		}
   		else {
	   		if( servicecode.equals("all") ) {
		   		batchBillings = batchBillingDAO.findByProvider(providerview.trim());
	   		}
	   		else {
		   		batchBillings = batchBillingDAO.findByProvider(providerview.trim(), servicecode);
	   		}
   		}
   	}

  %>

<html>
<head>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">

<title><bean:message key="admin.admin.btnBatchBilling"/></title>
<script type="text/javascript">
<!--

function askFirst(method) {
	if( confirm('<bean:message key="billing.batchbilling.msgConfirmDelete"/>')) {
		setMethod(method);
	}
	
	return false;
}

function setMethod(val) {
	var element = document.getElementById("method");
	element.value = val;
	document.forms["serviceform"].submit();
}


var selected = false;
function selectAll() {
	var checkboxes = document.getElementsByName("bill");

	for( var idx = 0; idx < checkboxes.length; ++idx ) {
		if(selected) {
			checkboxes[idx].checked = false;			
		}
		else {
			checkboxes[idx].checked = true;			
		}
	}
	
	selected = !selected;
}

function jumpMenu(targ,provider){
  var servicecode = document.getElementById("service_code");
  var service = servicecode.options[servicecode.selectedIndex].value;
  var providerNo = provider.options[provider.selectedIndex].value;
  
  if( providerNo != "#" ) {
  	eval(targ+".location='batchBilling.jsp?provider_no="+providerNo+"&service_code=" + service + "'");
  }
  
}

function init() {
	<%
	if( batchBillings != null && batchBillings.size() > 0 ) {
	%>		
		Calendar.setup({ inputField : "BillDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "billDate_cal", singleClick : true, step : 1 });
	<%
	}
	%>
}
//-->
</script>

<style>
@media print {
  .visible-print {
    display: inherit !important;
  }
  .hidden-print {
    display: none !important;
  }
}
</style>
</head>

<body>
<h3><bean:message key="admin.admin.btnBatchBilling"/></h3>

<div class="container-fluid">

<div class="row well well-condensed hidden-print">

<% 
ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
 %>
	<form name="serviceform" method="post" action="BatchBill.do" class="form-inline">	
	<input type="hidden" id="method" name="method" value="">	

<div class="span2">
	<bean:message key="billing.batchbilling.msgProvider"/><br>
		<select name="provider" class="span2"onChange="jumpMenu('window',this)">
			<option value="#"><b><bean:message key="billing.batchbilling.msgProvider"/></b></option>
			<option value="all"
				<%=providerview.equals("all")?"selected":""%>><bean:message key="billing.batchbilling.msgAllProvider"/></option>
			<% List<Provider>providers = providerDao.getBillableProviders();
			String proFirst, proLast, proNo;
			for(Provider p: providers){
				proFirst = p.getFirstName();
				proLast = p.getLastName();
				proNo = p.getProviderNo();
 %>
			<option value="<%=proNo%>"
				<%=providerview.equals(proNo)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%

 }
  %>
		</select>
</div>

<div class="span3">
		<bean:message key="billing.batchbilling.serviceCode"/>:			
			<select id="service_code" class="span3" name="service_code">
				<option value="all" <%=servicecode.equals("all")?"selected":""%>><bean:message key="billing.batchbilling.msgAllServiceCode"/></option>
		<%			
			List<String>serviceCodes = batchBillingDAO.findDistinctServiceCodes();
			for( String service : serviceCodes ) {
		%>
				<option value="<%=service%>" <%=servicecode.equals(service)?"selected":""%>><%=service%></option>
		<%
			}
		%>
			</select>
</div>

<div class="span4">
		<bean:message key="billing.batchbilling.msgClinicLocation"/>:			
		<select name="clinic_view" class="span4">
			<%
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
</div>
</div><!--row well-->
		

<div class="row">


		<input type="hidden" name="verCode"
			value="V03"> <input type="hidden" name="curUser"
			value="<%=user_no%>"> <input type="hidden" name="curDate"
			value="<%=nowDate%>"> <input type="hidden" name="curTime"
			value="<%=nowTime%>">
			

	<% 
	   if( batchBillings != null && batchBillings.size() > 0) {

%>

<button class="btn pull-right" type='button' name='print' value='Print' onClick='window.print()'><i class="icon icon-print"></i> Print</button>
		<br/><input type="checkbox" onclick="selectAll();"><br/><br/>
		
		<table class="table table-striped table-hover table-condensed">
		<thead>
			<tr>
				<th><bean:message key="billing.batchbilling.msgSelection"/></th>
				<th><bean:message key="billing.batchbilling.msgDemographic"/></th>
				<th><bean:message key="billing.batchbilling.msgProviderTitle"/></th>
				<th><bean:message key="billing.batchbilling.msgService"/></th>
				<th><bean:message key="billing.batchbilling.msgAmount"/></th>
				<th><bean:message key="billing.batchbilling.msgDiagnostic"/></th>
				<th><bean:message key="billing.batchbilling.msgLastBillDate"/></th>

			</tr>
		</thead>
		<tbody>

<%
		   DemographicDao demographicDAO = (DemographicDao) SpringUtils.getBean("demographicDao");
		   String demo_name="";
	       String diagnostic_code="", service_code="", billing_amount="";
	       String billdate="";
	       java.util.Date billDate;
	       int colorCount = 0;
	       String color="";
	       int Count1 = 0;
	       
	      Provider provider = null;
	      Demographic demographic = null;
	      String proName1;
	      for( BatchBilling batchBilling : batchBillings ){
		
	       provider = providerDao.getProvider(batchBilling.getBillingProviderNo());
	       proFirst = provider.getFirstName();
	       proLast = provider.getLastName();
		   proName1 = provider.getFullName();
		   
		   demographic = demographicDAO.getDemographic(String.valueOf(batchBilling.getDemographicNo()));
		   demo_name = demographic.getFormattedName();
		   service_code = batchBilling.getServiceCode();
		   billing_amount = batchBilling.getBillingAmount() == null ? "N/A" : batchBilling.getBillingAmount();
	       diagnostic_code = batchBilling.getDxcode();
	       billDate = batchBilling.getLastBilledDate();
	       if( billDate == null ) {
	    	   billdate = "N/A";
	       }
	       else {
	    	   billdate = DateUtils.format("yyyy-MM-dd", billDate, request.getLocale()); 	   
	       }
	       
		   
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
					<td><input type="checkbox"
						name="bill" value="<%=service_code + ";" + diagnostic_code + ";" + batchBilling.getDemographicNo() + ";" + batchBilling.getBillingProviderNo()%>"></td>
					<td><%=demo_name%></a></td>
					<td><%=proName1%></td>
					<td><%=service_code%></td>
					<td><%=billing_amount%></td>
					<td><%=diagnostic_code%></td>
					<td><%=billdate%></td>
				</tr>
				<%}
	  			 if ( Count1 == 0) { %>
				<tr>
					<td colspan=7><bean:message key="billing.batchbilling.msgNoMatch"/></td>
				</tr>
				  <%} else {%>
	
				<tr>
					<td colspan="7">
					<div class="span3">
					<bean:message key="billing.batchbilling.serviceDate"/>
					<div class="input-append">
						<input type="text" name="BillDate" id="BillDate" value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>" data-date-format="yyyy-m-d" style="width:90px"  autocomplete="off" readonly/>
						<span class="add-on"><i class="icon-calendar"></i></span>
					</div>
					</div>
					
					<div class="span4">
					<input type="button" class="btn btn-primary" onclick="return setMethod('doBatchBill');"	value="<bean:message key="billing.batchbilling.btnSubmit"/>">
					<input type="button" class="btn" onclick="return askFirst('remove');" value="<bean:message key="billing.batchbilling.btnRemove"/>"> 					
					</div>
					</td>
	   			</tr>
	   		
	<%
				}
	   }else if(servicecode!=null && providerview!=null && batchBillings == null){
%>
<tr><td>* Make selection above to generate batch billing</td></tr>
<%
}else{
%>
<tr><td>Nothing to report</td></tr>
<%}%>
</tbody>
</table>

  </form>
</div>

</div>

<script>
$(function (){ 
	$('#BillDate').datepicker();
}); 

$( document ).ready(function() {
parent.parent.resizeIframe($('html').height()+300);
});

</script>
</body>
</html>
