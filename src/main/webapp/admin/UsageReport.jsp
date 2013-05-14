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

<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="oscar.util.*" %>
<%@page import="java.util.Date"%>
<%@page import="org.oscarehr.casemgmt.dao.CaseManagementNoteDAO" %>
<%@page import="org.oscarehr.common.dao.BillingDao" %>
<%@page import="org.oscarehr.common.model.Drug" %>
<%@page import="org.oscarehr.common.dao.DrugDao" %>
<%@page import="org.oscarehr.common.dao.ProviderInboxRoutingDao" %>
<%@page import="org.oscarehr.common.model.ProviderInboxItem" %>
<%@page import="org.oscarehr.casemgmt.dao.TicklerDAO" %>
<%@page import="org.caisi.model.CustomFilter" %>
<%@page import="org.oscarehr.common.dao.DocumentDao" %>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao" %>
<%
	BillingONCHeader1Dao billingONCHeader1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);
String providerNo = request.getParameter("providerNo");

OscarAppointmentDao appointmentDao          	=(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
CaseManagementNoteDAO caseManagementNoteDao 	=(CaseManagementNoteDAO)SpringUtils.getBean("caseManagementNoteDAO");
BillingDao billingDAO 							=(BillingDao)SpringUtils.getBean("billingDao");
DrugDao drugDao 								= (DrugDao) SpringUtils.getBean("drugDao");
ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) SpringUtils.getBean("providerInboxRoutingDAO");
TicklerDAO ticklerDAO 							= (TicklerDAO) SpringUtils.getBean("ticklerDAO");
DocumentDao documentDao							=(DocumentDao) SpringUtils.getBean("documentDao");
ProviderDao providerDao 						= (ProviderDao)SpringUtils.getBean("providerDao");

List<Provider> providers = providerDao.getActiveProviders();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
    <head>
     	<title>EMR Usage Report</title>
     	<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />

    </head>
    <body class="mainbody">
         <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Report</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>EMR Usage Report</td>
                            <td style="text-align: right;"  >
                                    <oscar:help keywords="1.6.11" key="app.top1"/> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
            	<td class="MainTableLeftColumn">&nbsp;</td>
            	<td class="MainTableRightColumn">


					<form action="UsageReport.jsp">
					<label>Provider:</label>
						<select name="providerNo">
						<%
						for(Provider provider:providers) {
							String selected = new String();
							if(providerNo != null && providerNo.equals(provider.getProviderNo())) {
								selected=" selected=\"selected\" ";
							}
							%><option value="<%=provider.getProviderNo()%>" <%=selected%>><%=provider.getFormattedName()%></option><%
						}
					%>
						</select>

					<label>Start Date</label><input type="text" name="startDate"   value="<%=request.getParameter("startDate")%>"/>
					<label>End Date</label><input type="text" name="endDate" value="<%=request.getParameter("endDate")%>"/>
					<input type="submit" value="Run Report"/>
					</form>


            		<%

					 if(providerNo != null){
						 DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
						 List<Demographic> demoList = demographicDao.getDemographicByProvider( providerNo);

						 int total = demoList.size();
						 int a0to19  = 0;
						 int a0to19m  = 0;
						 int a0to19f  = 0;

						 int a20to44 = 0;
						 int a20to44m = 0;
						 int a20to44f = 0;

						 int a45to64 = 0;
						 int a45to64m = 0;
						 int a45to64f = 0;

						 int a65to84 = 0;
						 int a65to84m = 0;
						 int a65to84f = 0;

						 int a85plus = 0;
						 int a85plusm = 0;
						 int a85plusf = 0;

						 for(Demographic demo:demoList){
							 int age = demo.getAgeInYears();
							 String sex = demo.getSex();

							 if(age <= 19){
								 a0to19++;
								 a0to19m = checkMale(sex,a0to19m);
								 a0to19f = checkFemale(sex,a0to19f);
							 }else if (age >= 20 && age <= 44){
								 a20to44++;
								 a20to44m = checkMale(sex,a20to44m);
								 a20to44f = checkFemale(sex,a20to44f);
							 }else if (age >= 45 && age <=64){
								 a45to64++;
								 a45to64m = checkMale(sex,a45to64m);
								 a45to64f = checkFemale(sex,a45to64f);
							 }else if (age >=65 && age <= 84){
								 a65to84++;
								 a65to84m = checkMale(sex,a65to84m);
								 a65to84f = checkFemale(sex,a65to84f);
							 }else{
								 a85plus++;
								 a85plusm = checkMale(sex,a85plusm);
								 a85plusf = checkFemale(sex,a85plusf);
							 }
						 }


						 Date startDate = null;
						 Date endDate   = null;

						 try{
						 	startDate = UtilDateUtilities.StringToDate(request.getParameter("startDate"));
						 	endDate   = UtilDateUtilities.StringToDate(request.getParameter("endDate"));
						 }catch(Exception e){
					 		startDate = null;
					 		endDate   = null;
						 }

						 int scheduledAppts       = appointmentDao.findByDateRangeAndProvider(startDate, endDate, providerNo).size();
						 int billing              = billingONCHeader1Dao.getNumberOfDemographicsWithInvoicesForProvider(providerNo,startDate, endDate,true);
						 int encounterNote        = caseManagementNoteDao.getNoteCountForProviderForDateRange(providerNo,startDate,endDate);
						 int problemList          = caseManagementNoteDao.getNoteCountForProviderForDateRangeWithIssueId(providerNo,startDate,endDate,"Concerns");
						 int storedDocuments      = documentDao.getNumberOfDocumentsAttachedToAProviderDemographics(providerNo, startDate, endDate);
						 int rxNewRenewals        = drugDao.getNumberOfDemographicsWithRxForProvider(providerNo,startDate, endDate,true);
						 int useOfRemindersAlerts = caseManagementNoteDao.getNoteCountForProviderForDateRangeWithIssueId(providerNo,startDate,endDate,"Reminders");
						 	CustomFilter customFilter = new CustomFilter();
						 	customFilter.setProvider(providerNo);
						 	customFilter.setStart_date(startDate);
						 	customFilter.setEnd_date(endDate);
						 	useOfRemindersAlerts += ticklerDAO.getTicklers(customFilter).size();

						 int labs                 = providerInboxRoutingDao.howManyDocumentsLinkedWithAProvider(providerNo);

					%>

					<div>
					<h2>Practice Profile</h2>

					Practice Size: <%=demoList.size() %>

					<h2>Age and Gender Distribution</h2>
					<table border=1 >
						<tr>
							<th>Age Group - Years</th>
							<th>Percentage</th>
							<th>Male</th>
							<th>Female</th>
						</tr>
						<tr>
							<td>0 -19</td>
							<td><%=divide(total,a0to19) %>%</td>
							<td><%=divide(a0to19,a0to19m) %>%</td>
							<td><%=divide(a0to19,a0to19f) %>%</td>
						</tr>
						<tr>
							<td>20-44</td>
							<td><%=divide(total,a20to44) %>%</td>
							<td><%=divide(a20to44,a20to44m) %>%</td>
							<td><%=divide(a20to44,a20to44f) %>%</td>
						</tr>
						<tr>
							<td>45-64</td>
							<td><%=divide(total,a45to64) %>%</td>
							<td><%=divide(a45to64,a45to64m) %>%</td>
							<td><%=divide(a45to64,a45to64f) %>%</td>
						</tr>
						<tr>
							<td>65-84</td>
							<td><%=divide(total,a65to84) %>%</td>
							<td><%=divide(a65to84,a65to84m) %>%</td>
							<td><%=divide(a65to84,a65to84f) %>%</td>
						</tr>
						<tr>
							<td>85+</td>
							<td><%=divide(total,a85plus) %>%</td>
							<td><%=divide(a85plus,a85plusm) %>%</td>
							<td><%=divide(a85plus,a85plusf) %>%</td>
						</tr>

					</table>

					<br><br><br>


					<table border=1>
						<tr>
							<th>Scheduled Appts</th>
							<th title="Bill for services  includes OHIP, WSIB, other Provincial plans, private insurance and uninsured (self pay, third parties) invoicing">Billing</th>
							<th title="Enter encounter notes for patients seen  progress note entry associated with a kept patient office visit">Encounter Note</th>
							<th title="Enter problem lists for patients seen  presence of CPP problem list entry. If an application allows for none in the CPP category of Problem List/Ongoing Problems this is an
					acceptable entry.">Problem List</th>
							<th title="Store documents not originated from an EMR  includes any scanned documents or external documents delivered through an electronic interface e.g. through Hospital Report
					Manager.">Stored Documents</th>
					 		<th>Rx new/renewals</th>
							<th title="Generate automated alerts or  reminders to support care delivery includes medication alerts (drug-drug, drug-allergy, drug-condition); preventive care and chronic disease
					management reminders">Use of reminders/alerts</th>
							<th title="Receive lab results electronically, directly into the EMR from private labs  includes electronic interfaces with hospital labs.">Labs</th>
						</tr>
						<tr>
							<td><%=scheduledAppts%></td>
							<td><%=billing%></td>
							<td><%=encounterNote%></td>
							<td><%=problemList%></td>
							<td><%=storedDocuments%></td>
							<td><%=rxNewRenewals%></td>
							<td><%=useOfRemindersAlerts%></td>
							<td><%=labs%></td>
						</tr>
					</table>
					</div>

					<%}%>

            	</td>
            </tr>
            <tr>
				<td class="MainTableBottomRowLeftColumn"></td>
				<td class="MainTableBottomRowRightColumn"></td>
			</tr>
         </table>
	</body>
</html>

<%!
String divide(int total, int count){
	double val = (float)count/total;
	if(Double.isNaN(val)){
		return "---";
	}
	return ""+val * 100;
}


int checkMale(String sex,int count){
	if("M".equalsIgnoreCase(sex)){
		return (1+count);
	}
	return count;
}

int checkFemale(String sex,int count){
	if("F".equalsIgnoreCase(sex)){
		return (1+count);
	}
	return count;
}
%>
