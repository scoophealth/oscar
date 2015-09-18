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
      String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="org.apache.commons.lang.StringUtils,org.apache.commons.lang.StringEscapeUtils,java.util.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarDB.*,java.sql.*,oscar.oscarDemographic.data.*,oscar.eform.*,org.oscarehr.common.model.Provider,org.oscarehr.managers.ProviderManager2,org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
	ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
%>

<jsp:useBean id="reportMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%  if(!reportMainBean.getBDoConfigure()) { %>
<%@ include file="/report/reportMainBeanConn.jspf"%>
<% }  %>


<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<%  //This could be done alot better.
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");

  String startStr = "";
  String endStr   = "";
  String[] providerNo = null;
  String formId = "8";

  
  if (request.getParameter("startDate") != null && request.getParameter("endDate") != null && request.getParameter("formId") != null &&  request.getParameter("providerIds") != null) {

      String startDate =     request.getParameter("startDate");
      String endDate  =      request.getParameter("endDate");
      formId = request.getParameter("formId");
      providerNo = request.getParameterValues("providerIds");

      java.util.Date sDate = UtilDateUtilities.StringToDate(startDate);
      java.util.Date eDate = UtilDateUtilities.StringToDate(endDate);



      startStr = UtilDateUtilities.DateToString(sDate);
      endStr   = UtilDateUtilities.DateToString(eDate);
  }else{
     Calendar cal = Calendar.getInstance();
     int daysTillMonday = cal.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
     java.util.Date endDate = cal.getTime();
     cal.add(Calendar.DATE,-daysTillMonday);
     java.util.Date startDate = cal.getTime();

     startStr = UtilDateUtilities.DateToString(startDate);
     endStr   = UtilDateUtilities.DateToString(endDate);
  }
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSIS Report</title>
<style>
td.OSISdata{
	text-align:center;
}
</style>
<style type="text/css" media="print">
.noprint {
	display: none;
}

table.ele {
	width: 450pt;
	margin-left: 0pt;
}
</style>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1"  />

<script title="text/javascript" src="../share/calendar/calendar.js"></script>
<script title="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script title="text/javascript" src="../share/calendar/calendar-setup.js"></script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<table class="MainTable noprint" id="scrollNumber1"
	name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Report</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>OSIS</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<form action="OSISReport.jsp">
		<fieldset>
		<legend>OSIS Report</legend> 
		<label>Start Date:</label> 
		<input type="text" size="9" id="startDate" name="startDate" value="<%=startStr%>" />
		<a id="startdate"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> 
		
		<label style="margin-left:20px;">End Date:</label>
		<input type="text" size="9" id="endDate" name="endDate" value="<%=endStr%>" /> 
		<a id="enddate"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0"/></a>
		
                <label style="margin-left:20px;">Form Id:</label><input type="text" size="3" name="formId" value="<%=formId%>"/>
		<br><br>
		<label>Providers to include:</label>
		<br>
		<select multiple="multiple" name="providerIds" size="15">
		<%
		// null for both active and inactive because the report might be for a provider
		// who's just left the current reporting period
		List<Provider> providers = providerManager.getProviders(loggedInInfo, null);

		for(Provider provider: providers){
			//skip (system,system)
			if(provider.getProviderNo().equals(Provider.SYSTEM_PROVIDER_NO)) continue;
		%>
			<option value="<%=provider.getProviderNo()%>"
		<%	
			if(providerNo != null){
				if(Arrays.asList(providerNo).contains(provider.getProviderNo())) 
					out.println("selected");
			}
		%>
		
			><%=StringEscapeUtils.escapeHtml(provider.getFormattedName())%></option>
		<%
		}
		%>
		</select>

		<input type="submit" value="Run Report" /></fieldset>
		</form>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

<p>
<strong>Providers:</strong><br>
<%

	if (providerNo != null && !formId.equals("")){
        
        int[] count;
        
        for(Provider provider: providers){
        	if(Arrays.asList(providerNo).contains(provider.getProviderNo())){
				out.println(provider.getFormattedName() + "(" + provider.getProviderNo() + ")<br>");
        	}
        }
    
    %>
</p>
    

<p><strong>Note:</strong><br>O - Ongoing Clients<br>N - New Clients</p>
<table border="1">
	<tr>
		<td>&nbsp;</td>
		<th colspan="8">Number of Individuals<br>HOMELESS</th>
		<th colspan="8">Number of Individuals<br>HOUSED</th>
	</tr>
	<tr>
		<th>Age / Gender of Individuals</th>
		<th colspan="2">Male</th>
		<th colspan="2">Female</th>
		<th colspan="2">Transgender<br>(Self-Identified</th>
		<th colspan="2">Gender<br>Unknown</th>
		<th colspan="2">Male</th>
		<th colspan="2">Female</th>
		<th colspan="2">Transgender<br>(Self-Identified</th>
		<th colspan="2">Gender<br>Unknown</th>
	</tr>

	<tr>
		<th>&nbsp;</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		
	</tr>
	<tr>
		<td>15-21 years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","15-21","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>		
	</tr>

	<tr style="background-color:gainsboro;">
		<td>22-30 years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>		
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","22-30","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>

		
	<tr>
		<td>31-45 years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","31-45","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>


	
	<tr style="background-color:gainsboro;">
		<td>46-64 years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","46-64","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
		

	<tr>
		<td>65+ years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","65+","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>



	
	<tr style="background-color:gainsboro;">
		<td>unknown years</td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-01","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-02","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-03","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-04","CurrentlyHomeless","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-01","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-02","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-03","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","unknown","008-04","CurrentlyHoused","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	

	</table>
	<br><br>
	<table border="1">
	<tr>
		<th>Activity</th>
		<th>O</th>
		<th>N</th>
	</tr>
	<tr>
		<td ><strong>Assisted With Housing</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refAssistedWithHousing","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Assisted with Accessing Food</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refAssistedWithAccessingFood","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Assisted with Accessing Transportation</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refAssistedWithAccessTransport","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Assisted With Income Support</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refAssistedWithIncomeSupport","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Referred to Employment Services</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToEmploymentService","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Referred to Emergency Shelter</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToEmergShelter","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Referred to Mental Health Service</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToMentalHealthServi","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Referred to Addiction Service</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToAddictionService","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td><strong>Referred to Health Service</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToHealthService","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Referred to Housing Help Centre</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToHousingHelpCentre","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td><strong>Provided Heat/Cold Supplies</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refProvidedHeatColdSupplies","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>COAST Referral Made</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refReferredToCOAST","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Care Planning RE: Client/System Issue</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refCarePlanningClientSystemIss","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td ><strong>Education to Agency RE: Service for Clients</strong></td>
		<%count = OneTimeConsultUtil.getOSISReport("refEducationToAgency","","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	</table>


	<br><br>	
	<table border="1">
	
	<tr>
		<th>Other Demographic<br>Information of Individuals</th>
		<th colspan="2">Male</th>
		<th colspan="2">Female</th>
		<th colspan="2">Transgender<br>(Self-Identified)</th>
		<th colspan="2">Gender<br>Unknown</th>
		
		<th colspan="2">Male</th>
		<th colspan="2">Female</th>
		<th colspan="2">Transgender<br>(Self-Identified)</th>
		<th colspan="2">Gender<br>Unknown</th>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>
		<th>O</th>
		<th>N</th>		
	
	</tr>	
	<tr>
		<td>Aboriginal/Metis/First Nation/Inuit</td>		
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-01","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-02","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-03","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-04","CurrentlyHomeless","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-01","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-02","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-03","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		<%count = OneTimeConsultUtil.getOSISReport("","","","008-04","CurrentlyHoused","011-01",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>

	</table>

	<br><br>
	<table border="1">
	<tr>
		<th>Transitions from Homelessness to Housing:</th>
		<th colspan="2"># Individuals (Households)</th>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<th>O</th>
		<th>N</th>
	</tr>	
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from street to emergency shelter</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Street to Emergency Shelter","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td># Singles moved from street to temporary/transitional housing</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Street to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from street to supportive/supported housing</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Street to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td># Singles moved from street to regular/permanent housing</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Street to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from emergency shelter to temporary/transitional</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Emergency Shelter to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td># Singles moved from emergency shelter to supportive/supported</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Emergency Shelter to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from emergency shelter to regular/permanent</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Emergency Shelter to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td># Singles moved from hidden homelessness to temporary/transitional</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Hidden Homelessness to Temporary/Transitional Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td># Singles moved from hidden homelessness to supportive/supported</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Hidden Homelessness to Supportive/Supported Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	<tr>
		<td># Singles moved from hidden homelessness to regular/permanent</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Hidden Homelessness to Regular/Permanent Housing","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	
	<tr style="background-color:gainsboro;">
		<td>Other type of move that resulted in Single person moving from homelessness to housing (temporary or permanent)</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Other","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
		
	</tr>
	<tr>
		<td>Other type of service-street outreach provided</td>
		<%count = OneTimeConsultUtil.getOSISReport("","Other2","","","","",providerNo,formId,startStr,endStr);%>
		<td class="OSISdata"><%=count[0] %></td>
		<td class="OSISdata"><%=count[1] %></td>
	</tr>
	</table>

	</table>


</table>
<input type="button" onclick="window.print();" value="Print"
	class="noprint" />

<%}%>
<script type="text/javascript">
Calendar.setup( { inputField : "startDate", ifFormat : "%Y-%m-%d", showsTime : false, button : "startdate", singleClick : true, step : 1} );
Calendar.setup( { inputField : "endDate", ifFormat : "%Y-%m-%d", showsTime : false, button : "enddate", singleClick : true, step : 1} );
</script>
</body>
</html>
