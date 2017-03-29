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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.dao.AdmissionDao"%>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreAdmissionDao"%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.OcanSubmissionLog"%>
<%@page import="java.util.List"%>
<%@page import="oscar.util.CBIUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>

<%!
CBIUtil cbiUtil = new CBIUtil();
%>

<%
List<OcanSubmissionLog> submissionLogList = null;
   		 
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
Integer currentFacilityId=loggedInInfo.getCurrentFacility().getId();
 			
String dateStr = "";
if(request.getParameter("date")!=null && request.getParameter("date").trim().length()>0)
{
	dateStr = request.getParameter("date").trim();
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	Date dateObj = dateFormat.parse(dateStr);
	
	submissionLogList = cbiUtil.getCbiSubmissionLogRecords(dateObj);
}

int totalCount = 0, successCount = 0, failureCount = 0;
if(submissionLogList!=null)
{
	totalCount = submissionLogList.size();
	for(OcanSubmissionLog ocanSubmissionLog : submissionLogList)
	{
		if(ocanSubmissionLog.getResultMessage()!=null && ocanSubmissionLog.getResultMessage().contains("success") )
			successCount++;
		else
			failureCount++;
	}
}

%>

<table class="table_main_details" width="100%" height="100%" border="1" bordercolor="#C0CBDB">
	<tr>
		<td class="summary">
			<span>Date : <input type="text" id="txt_date" value="<%=dateStr%>" readonly="readonly"></span>
			<span>Attempted : <font style="bold" color="blue"> <%=totalCount %></font></span>
			<span>Successful : <font style="bold" color="green"> <%=successCount %></font></span>
			<span>Unsuccessful : <font style="bold" color="red"> <%=failureCount %></font></span>
		</td>
	</tr>
	<tr>
		<td class="filter">
			Filter: <span><input type="radio" name="filter" id="filter" value="all" checked="checked" onclick="onclick_filter('all');">All</span>
			<span><input type="radio" name="filter" id="filter" value="successful" onclick="onclick_filter('true');">Successful</span>
			<span><input type="radio" name="filter" id="filter" value="failure" onclick="onclick_filter('false');">Failure</span>
		</td>
	</tr>
	<tr>
		<td style="padding-left: 1px !important;">
			<table class="table_records" width="100%" height="100%" border="1" bordercolor="#C0CBDB">
				<thead>
					<tr>	
						<th>Client ID</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Date of Admission</th>
						<th>Functional Centre</th>
						<th>Last Modified Time</th>
						<th>Status</th>
						<th>Upload Time</th>
						<th>Failure</th>
					</tr>
				</thead>
				<tbody>
					<!-- <tr>
						<td align="center">1</td>
						<td align="center">first 1</td>
						<td align="center">last 1</td>
						<td align="center">1-1-1</td>
						<td align="center">1</td>
						<td align="center">1-1-1</td>
						<td align="center">Successful</td>
						<td align="center">1-1-1</td>
						<td align="center">&nbsp;</td>
					</tr> -->
					<%
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(submissionLogList!=null && submissionLogList.size()>0)
					{
						AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
						
						for(OcanSubmissionLog ocanSubmissionLog : submissionLogList)
						{
							if(ocanSubmissionLog!=null)
							{
								//get form record based on the submission id 
								OcanStaffForm ocanStaffForm = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, ocanSubmissionLog.getId());
								
								if(ocanStaffForm!=null)
								{	
									String admissionDate = "", functionalCentre = "";
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");									
									if(ocanStaffForm.getAdmissionDate()!=null)
									{
										admissionDate = formatter.format(ocanStaffForm.getAdmissionDate());
									}
									
									FunctionalCentreAdmissionDao fc_admissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
									FunctionalCentreAdmission fc_admission = fc_admissionDao.find(ocanStaffForm.getAdmissionId());
									if(fc_admission!=null && fc_admission.getFunctionalCentreId()!=null && fc_admission.getFunctionalCentreId().trim().length()>0) {
										functionalCentre = fc_admission.getFunctionalCentreId();
									}
									
									String result = ocanSubmissionLog.getResult();
									String cls = "";
									if(result.equalsIgnoreCase("failure"))
										cls = "error";
									%>
									<tr class="<%=cls%>" onclick="onclick_submission('<%=ocanSubmissionLog.getId()%>');">
										<td align="center"><%=ocanStaffForm.getClientId() %></td>
										<td align="center"><%=ocanStaffForm.getFirstName() %></td>
										<td align="center"><%=ocanStaffForm.getLastName() %></td>
										<td align="center"><%=admissionDate %></td>
										<td align="center"><%=functionalCentre %></td>
										<td align="center"><%=dateFormat.format(ocanStaffForm.getCreated()) %></td>
										<td align="center"><%=result %></td>
										<td align="center"><%=dateFormat.format(ocanSubmissionLog.getSubmitDateTime()) %></td>
										<td align="center"><%=ocanSubmissionLog.getResultMessage() %></td>
									</tr>
									<%
								}
							}
						}
					}
					%>
				</tbody>
			</table>
		</td>
	</tr>
</table>
