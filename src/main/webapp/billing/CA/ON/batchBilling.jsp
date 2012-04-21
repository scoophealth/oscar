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

<%@page import="oscar.util.DateUtils"%>
<%@page import="oscar.OscarProperties"%>
<%
if(session.getAttribute("user") == null)    response.sendRedirect("../../../../logout.jsp");
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
<link rel="stylesheet" href="billing.css">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="<c:out value="${ctx}"/>/share/calendar/calendar.css" title="win2k-cold-1">

  <!-- main calendar program -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="<c:out value="${ctx}"/>/share/calendar/calendar-setup.js"></script>

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

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0" onLoad="init()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"><bean:message key="admin.admin.btnBatchBilling"/></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

<% 
ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
 %>
<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="serviceform" method="post" action="BatchBill.do">	
	<input type="hidden" id="method" name="method" value="">	
	<tr>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><bean:message key="billing.batchbilling.msgProvider"/></font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider"
			onChange="jumpMenu('parent',this)">
			<option value="#"><b><bean:message key="billing.batchbilling.msgProvider"/></b></option>
			<option value="all"
				<%=providerview.equals("all")?"selected":""%>><bean:message key="billing.batchbilling.msgAllProvider"/></option>
			<% List<Provider>providers = providerDao.getBillableProviders();
			String proFirst, proLast, proNo;
			for(Provider p: providers){
				proFirst = p.getFirstName();
				proLast = p.getLastName();
				proNo = p.getProviderNo();
// billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
// specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");
	

 %>
			<option value="<%=proNo%>"
				<%=providerview.equals(proNo)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%

 }
//
  %>
		</select> </font></b></td>
		<td width="254"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message key="billing.batchbilling.serviceCode"/>:</font></b>			
		</td>
		<td width="254">
			<select id="service_code" name="service_code">
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
		</td>
		<td width="254"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message key="billing.batchbilling.msgClinicLocation"/>:</font></b>			
		</td>		
		<td width="277"><select name="clinic_view">
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
		</select><font color="#003366"> <input type="hidden" name="verCode"
			value="V03"> <input type="hidden" name="curUser"
			value="<%=user_no%>"> <input type="hidden" name="curDate"
			value="<%=nowDate%>"> <input type="hidden" name="curTime"
			value="<%=nowTime%>"> </font></td>
	</tr>
	<tr>
		<td colspan=6>
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#CCCCFF">
				<td width="12%" height="16"><input type="checkbox" onclick="selectAll();"></td>
				<td colspan="6" height="16">&nbsp;</td>
			</tr>
			<tr bgcolor="#CCCCFF">
				<td width="12%" height="16"><bean:message key="billing.batchbilling.msgSelection"/></td>
				<td width="22%" height="16"><bean:message key="billing.batchbilling.msgDemographic"/></td>
				<td width="22%" height="16"><bean:message key="billing.batchbilling.msgProviderTitle"/></td>
				<td width="12%" height="16"><bean:message key="billing.batchbilling.msgService"/></td>
				<td width="12%" height="16"><bean:message key="billing.batchbilling.msgAmount"/></td>
				<td width="10%" height="16"><bean:message key="billing.batchbilling.msgDiagnostic"/></td>
				<td width="10%" height="16"><bean:message key="billing.batchbilling.msgLastBillDate"/></td>

			</tr>
	<% 
	   if( batchBillings != null && batchBillings.size() > 0) {
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
				<tr bgcolor="<%=color%>">
					<td width="12%" height="16"><input type="checkbox"
						name="bill" value="<%=service_code + ";" + diagnostic_code + ";" + batchBilling.getDemographicNo() + ";" + batchBilling.getBillingProviderNo()%>"></td>
					<td width="22%" height="16"><%=demo_name%></a></td>
					<td width="22%" height="16"><%=proName1%></td>
					<td width="12%" height="16"><%=service_code%></td>
					<td width="12%" height="16"><%=billing_amount%></td>
					<td width="10%" height="16"><%=diagnostic_code%></td>
					<td width="10%" height="16"><%=billdate%></td>
				</tr>
				<%}
	  			 if ( Count1 == 0) { %>
				<tr bgcolor="#FFFFFF">
					<td colspan=7><bean:message key="billing.batchbilling.msgNoMatch"/></td>
				
				  <%} else {%>
	
				<tr bgcolor="#FFFFFF">
					<td colspan="2"><bean:message key="billing.batchbilling.serviceDate"/>
						<input type="text" name="BillDate" id="BillDate" readonly value="<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>">
						<img src="<c:out value="${ctx}/images/cal.gif" />" id="billDate_cal" alt="calendar">				
					<td colspan=5><input type="button" onclick="return setMethod('doBatchBill');"
						value="<bean:message key="billing.batchbilling.btnSubmit"/>">&nbsp;&nbsp;
						<input type="button" onclick="return askFirst('remove');" value="<bean:message key="billing.batchbilling.btnRemove"/>"> 					
					</td>
	   			</td>
	   		
	<%
				}
	   }
%>
</table>
    </td>
    </tr>
  </form>
</table>

</body>
</html>
