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
<security:oscarSec roleName="<%=roleName2$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed2) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="org.apache.commons.lang.StringUtils,org.apache.commons.lang.StringEscapeUtils,java.util.*,oscar.oscarReport.data.*,oscar.util.*,oscar.oscarDB.*,java.sql.*,oscar.oscarDemographic.data.*,oscar.eform.*,org.oscarehr.common.model.Provider,org.oscarehr.managers.ProviderManager2,org.oscarehr.util.SpringUtils"%>
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
<title>CDS Report</title>

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
				<td>CDS</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<form action="CDSOneTimeConsultReport.jsp">
		<fieldset>
		<legend>CDS Report</legend> 
		<label>Start Date:</label> <input type="text" size="9" id="startDate" name="startDate"	value="<%=startStr%>" /> 
		<a id="startdate"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0" /></a> 

		<label>End Date:</label> <input type="text" size="9" id="endDate" name="endDate" value="<%=endStr%>" /> 
       		<a id="enddate"><img title="Calendar" src="../images/cal.gif" alt="Calendar" border="0"/></a>
		
		<label>Form Id:</label><input type="text" size="3" name="formId" value="<%=formId%>"/>
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

<%

	if (providerNo != null && !formId.equals("")){
		int[] count;
%>
	
	<p><strong>Note:</strong><br>This report is only for the One-Time Consults</p>
	<table width="60%" border="1">
	<tr>
		<th width="10%">CDS Category ID</th>
		<th width="30%">CDS Category Description</th>
		<th width="10%">#</th>
	</tr>
	<tr>
		<td colspan="4"><strong>Gender</strong></td>
	</tr>
	<tr>
		<td>008-01</td>
		<td>Male</td>
		<%count = OneTimeConsultUtil.getCDSReport("008-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>008-02</td>
		<td>Female</td>
		<%count = OneTimeConsultUtil.getCDSReport("008-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>008-03</td>
		<td>Other</td>
		<%count = OneTimeConsultUtil.getCDSReport("008-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>		
	</tr>
	<tr>
		<td>008-04</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("008-04",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>		
	</tr>
	<tr>
		<td colspan="4"><strong>Age</strong></td>
	</tr>
	<tr>
		<td>009-01</td>
		<td>0-15</td>
		<%count = OneTimeConsultUtil.getCDSReport("0-15",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-02</td>
		<td>16-17</td>
		<%count = OneTimeConsultUtil.getCDSReport("16-17",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-03</td>
		<td>18-24</td>
		<%count = OneTimeConsultUtil.getCDSReport("18-24",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-04</td>
		<td>25-34</td>
		<%count = OneTimeConsultUtil.getCDSReport("25-34",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-05</td>
		<td>35-44</td>
		<%count = OneTimeConsultUtil.getCDSReport("35-44",providerNo,formId,startStr,endStr);%>
		<td><%=count[1]%></td>
	</tr>
	<tr>
		<td>009-06</td>
		<td>45-54</td>
		<%count = OneTimeConsultUtil.getCDSReport("45-54",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-07</td>
		<td>55-64</td>
		<%count = OneTimeConsultUtil.getCDSReport("55-64",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-08</td>
		<td>65-74</td>
		<%count = OneTimeConsultUtil.getCDSReport("65-74",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>	
	</tr>
	<tr>
		<td>009-09</td>
		<td>75-84</td>
		<%count = OneTimeConsultUtil.getCDSReport("75-84",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-10</td>
		<td>85 and over</td>
		<%count = OneTimeConsultUtil.getCDSReport("85 and over",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>009-11</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("Unknown",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td colspan="4"><strong>Aboriginal Status</strong></td>
	</tr>
	<tr>
		<td>011-01</td>
		<td>Aboriginal</td>
		<%count = OneTimeConsultUtil.getCDSReport("011-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>011-02</td>
		<td>Non-aboriginal</td>
		<%count = OneTimeConsultUtil.getCDSReport("011-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>011-03</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("011-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td colspan="4"><strong>Community Treatment Order</strong></td>
	</tr>
	<tr>
		<td>015-01</td>
		<td>Issued CTO</td>
		<%count = OneTimeConsultUtil.getCDSReport("015-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>015-02</td>
		<td>No CTO</td>
		<%count = OneTimeConsultUtil.getCDSReport("015-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>015-03</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("015-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td colspan="4"><strong>Diagnostic Categories</strong></td>
	</tr>
	<tr>
		<td>016-01</td>
		<td>Adjustment Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-02</td>
		<td>Anxiety Disorder</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-03</td>
		<td>Delirium, Dementia, and Amnestic and Cognitive Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-04</td>
		<td>Disorder of Childhood/Adolescence</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-04",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-05</td>
		<td>Dissociative Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-05",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-06</td>
		<td>Eating Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-06",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-07</td>
		<td>Factitious Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-07",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-08</td>
		<td>Impulse Control Disorders not elsewhere classified</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-08",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-09</td>
		<td>Mental Disorders due to General Medical Conditions</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-09",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-10</td>
		<td>Mood Disorder</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-10",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-11</td>
		<td>Personality Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-11",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-12</td>
		<td>Schizophrenia and other Psychotic Disorder</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-12",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-13</td>
		<td>Sexual and Gender Identity Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-13",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-14</td>
		<td>Sleep Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-14",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-15</td>
		<td>Substance Related Disorders</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-15",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-16</td>
		<td>Developmental Handicap</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-16",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-17</td>
		<td>Developmental Handicap</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-17",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>016-18</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("016-18",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td colspan="4"><strong>Presenting Issues</strong></td>
	</tr>
	<tr>
		<td>017-01</td>
		<td>Threat to others/attempted suicide</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-02</td>
		<td>Specific symptom of Serious Mental Illness</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-03</td>
		<td>Physical / Sexual Abuse</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-04</td>
		<td>Educational</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-04",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-05</td>
		<td>Occupational/Employment/Vocational</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-05",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-06</td>
		<td>Housing</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-06",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-07</td>
		<td>Financial</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-07",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-08</td>
		<td>Legal</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-08",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-09</td>
		<td>Problems with Relationships</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-09",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-10</td>
		<td>Problems with substance abuse / addictions</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-10",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>017-11</td>
		<td>Activities of daily living</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-11",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>		
	</tr>
	<tr>
		<td>017-12</td>
		<td>Other</td>
		<%count = OneTimeConsultUtil.getCDSReport("017-12",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td colspan="4"><strong>Source of Referral</strong></td>
	</tr>
	<tr>
		<td>018-01</td>
		<td>General Hospital</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>018-02</td>
		<td>Psychiatric Hospital</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>			
	</tr>
	<tr>
		<td>018-03</td>
		<td>Other Institution</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>				
	</tr>
	<tr>
		<td>018-04</td>
		<td>CMH&amp;A - Case Management</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-04",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>				
	</tr>
	<tr>
		<td>018-05</td>
		<td>CMH&amp;A - ACT Teams</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-05",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>				
	</tr>
	<tr>
		<td>018-06</td>
		<td>CMH&amp;A - Counseling and Treatment</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-06",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>				
	</tr>
	<tr>
		<td>018-08</td>
		<td>CMH&amp;A - Early Intervention</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-08",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>				
	</tr>
	<tr>
		<td>018-09</td>
		<td>CMH&amp;A - Crisis Intervention</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-09",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>		
	</tr>
	<tr>
		<td>018-10</td>
		<td>CMH&amp;A - Supports within Housing</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-10",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>		
	</tr>
	<tr>
		<td>018-11</td>
		<td>CMH&amp;A - Short Term Residential Crisis Support Beds</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-11",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-12</td>
		<td>CMH&amp;A - Information and Referral</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-12",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-13</td>
		<td>CMH&amp;A - Other community mental health and addiction services</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-13",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-14</td>
		<td>Other community agencies</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-14",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-15</td>
		<td>Family Physicians</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-15",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-16</td>
		<td>Psychiatrists</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-16",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-17</td>
		<td>Mental Health Worker</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-17",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-18</td>
		<td>Criminal Justice System (CJS) - Police</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-18",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-19</td>
		<td>Criminal Justice System (CJS) - Courts</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-19",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-20</td>
		<td>Criminal Justice System (CJS) - Correctional Facilities</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-20",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-21</td>
		<td>Criminal Justice System (CJS) - Probation/Parole Officers</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-21",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-22</td>
		<td>Criminal Justice System (CJS) - Short Term Residentail Safe Beds</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-22",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-23</td>
		<td>Criminal Justice System (CJS) - Source breakdown not available</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-23",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-24</td>
		<td>Self, Family or friend</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-24",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>018-25</td>
		<td>Other</td>
		<%count = OneTimeConsultUtil.getCDSReport("018-25",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td colspan="4"><strong>Highest Level of Education</strong></td>
	</tr>
	<tr>
		<td>29a-01</td>
		<td>No formal schooling</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-01",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-02</td>
		<td>Some Elementary/Junior High School</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-02",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-03</td>
		<td>Elementary/Junior High School</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-03",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-04</td>
		<td>Some Secondary/High School</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-04",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-05</td>
		<td>Secondary/High School</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-05",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-06</td>
		<td>Some College/University</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-06",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-07</td>
		<td>College/University</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-07",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>
	<tr>
		<td>29a-08</td>
		<td>Unknown</td>
		<%count = OneTimeConsultUtil.getCDSReport("29a-08",providerNo,formId,startStr,endStr);%>
		<td><%=count[1] %></td>
	</tr>	
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
