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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.dao.AdmissionDao"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.PMmodule.model.OcanSubmissionLog"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="oscar.util.CBIUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>

<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>

<%@page import="java.util.HashSet"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreAdmissionDao"%>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission"%>

<%@page import="org.oscarehr.common.model.CdsFormOption"%>
<%@page import="org.oscarehr.web.Cds4ReportUIBean"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
	CBIUtil cbiUtil = new CBIUtil();
	
 	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   	Integer currentFacilityId=loggedInInfo.getCurrentFacility().getId();	
   		 
	AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");
    ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
	FunctionalCentreAdmissionDao fc_admissionDao = (FunctionalCentreAdmissionDao) SpringUtils.getBean("functionalCentreAdmissionDao");
	
	int totalCount = 0, successCount = 0, failureCount = 0;
	
	SimpleDateFormat sdf=new SimpleDateFormat(DateFormatUtils.ISO_DATE_FORMAT.getPattern());
	Date startDate=sdf.parse(request.getParameter("startDate"));
	Date endDate=sdf.parse(request.getParameter("endDate"));
	
	String functionalCentreId=request.getParameter("functionalCentreId");
	
	// null for none selected, array of providerIds if selected
	String[] providerIdList=request.getParameterValues("providerIds");       
	String[] programIdListTemp=request.getParameterValues("programIds");
			
	// put proramId's in a Hash for quicker searches
	HashSet<Integer> programIdsToReportOn=null;
	if (programIdListTemp!=null && programIdListTemp.length>0) {
		programIdsToReportOn = new HashSet<Integer>();			
		for (String s: programIdListTemp) {
			s=StringUtils.trimToNull(s);
			if (s!=null) {
				programIdsToReportOn.add(new Integer(s));
			}
		}
	}
	
	// put providerId's in a Hash for quicker searches
	HashSet<String> providerIdsToReportOn=null;
	if (providerIdList!=null) {
		providerIdsToReportOn=new HashSet<String>();
		for (String s : providerIdList)	{
			providerIdsToReportOn.add(s);
		}
	}
	
	List<OcanSubmissionLog> allSubmissionLogList = cbiUtil.getCbiSubmissionLogRecords(startDate, endDate);
	List<OcanSubmissionLog> submissionLogListFilteredByProivders = new ArrayList<OcanSubmissionLog>();
	
	//Filter by provider
	if(providerIdsToReportOn!=null) { 
		if(allSubmissionLogList!=null) {		
			for(OcanSubmissionLog log : allSubmissionLogList) {
				OcanStaffForm form = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, log.getId()); //one cbi form has one submissionLog				
				if(form!=null && form.getProviderNo()!=null) {
					if (providerIdsToReportOn.contains(form.getProviderNo())) { 
						submissionLogListFilteredByProivders.add(log);
					}
				}				
			}
		}
	} else {
		submissionLogListFilteredByProivders = allSubmissionLogList;
	}
	
	//Filter by functional centre
	List<OcanSubmissionLog> submissionLogListFilteredByFunctionalCentre = new ArrayList<OcanSubmissionLog>();
	if(!StringUtils.isBlank(functionalCentreId)) {
		if(submissionLogListFilteredByProivders!=null) {
			for(OcanSubmissionLog log : submissionLogListFilteredByProivders) {
				OcanStaffForm form = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, log.getId());
				if(form!=null && form.getAdmissionId()!=null) {
					FunctionalCentreAdmission fc_admission = fc_admissionDao.find(form.getAdmissionId());
					if(fc_admission.getFunctionalCentreId()!=null && fc_admission.getFunctionalCentreId().trim().length()>0) {
						String fcId = fc_admission.getFunctionalCentreId();
						if(fcId!=null && functionalCentreId.equalsIgnoreCase(fcId)) {
							 submissionLogListFilteredByFunctionalCentre.add(log);
					  	}
					}					
				}
			}
		}
	} else {
		submissionLogListFilteredByFunctionalCentre = submissionLogListFilteredByProivders;
	}
	
	//Filter by program
	List<OcanSubmissionLog> submissionLogList = new ArrayList<OcanSubmissionLog>();
	if(programIdsToReportOn!=null) {
		if(submissionLogListFilteredByFunctionalCentre!=null) {
			for(OcanSubmissionLog log : submissionLogListFilteredByFunctionalCentre) {
				OcanStaffForm form = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, log.getId());
				if(form!=null) {					
					Admission admission = admissionDao.getAdmission(form.getAdmissionId());	
					if(admission!=null)	{
						Program program = admission.getProgram();
						if(program!=null) {
							 if(program.getId()!=null && programIdsToReportOn.contains(program.getId())) {
								 submissionLogList.add(log);
							}
						}
					}
				}
			}
		}
	} else {
		submissionLogList = submissionLogListFilteredByFunctionalCentre;
	}
				
		if(submissionLogList!=null)
		{			
			for(OcanSubmissionLog ocanSubmissionLog : submissionLogList)
			{
				OcanStaffForm form = cbiUtil.getCBIFormDataBySubmissionId(currentFacilityId, ocanSubmissionLog.getId()); //one cbi form has one submissionLog
				if(form!=null) {
					totalCount ++;
					if(ocanSubmissionLog.getResult()!=null && (ocanSubmissionLog.getResult().equalsIgnoreCase("success") || 
							ocanSubmissionLog.getResult().equalsIgnoreCase("true")))
						successCount++;
					else
						failureCount++;
				}
			}
		}
	



%>
<%@include file="/layouts/caisi_html_top.jspf"%>
<table class="borderedTableAndCells">
	<tr>
		<td class="summary">
			<span>Attempted : <font style="bold" color="blue"> <%=totalCount %></font></span>
		
			<span>Successful : <font style="bold" color="green"> <%=successCount %></font></span>
		
			<span>Unsuccessful : <font style="bold" color="red"> <%=failureCount %></font></span>		
		</td>
		
	</tr>
</table>
<table class="borderedTableAndCells">	
	<thead>
				<tr>
					<td>Client ID</td>
					<td>First Name</td>
					<td>Last Name</td>
					<td>Date of Admission</td>
					<td>Functional Centre</td>
					<td>Last Modified Time</td>
					<td>Status</td>
					<td>Upload Time</td>
					<td>Submission Result</td>
				</tr>
			</thead>
			<tbody>
					
					<%
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(submissionLogList!=null && submissionLogList.size()>0)
					{	
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
									
									FunctionalCentreAdmission fc_admission = fc_admissionDao.find(ocanStaffForm.getAdmissionId());
									if(fc_admission!=null && fc_admission.getFunctionalCentreId()!=null && fc_admission.getFunctionalCentreId().trim().length()>0) {
										functionalCentre = fc_admission.getFunctionalCentreId();
									}
																		
									String result = ocanSubmissionLog.getResult();
									String cls = "";
									if(result.equalsIgnoreCase("failure"))
										cls = "error";
									%>
									<tr>
										<td align="center"><a herf="../admin/cbiAdminFormDtl.jsp?submissionId=<%=ocanSubmissionLog.getId()%>"><%=ocanStaffForm.getClientId() %></a></td>
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
<%@include file="/layouts/caisi_html_bottom.jspf"%>
