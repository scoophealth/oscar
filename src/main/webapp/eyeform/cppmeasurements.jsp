<%@ page import="java.util.Collection" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.casemgmt.dao.IssueDAO" %>
<%@ page import="org.oscarehr.casemgmt.model.Issue" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@ page import="org.oscarehr.casemgmt.web.CaseManagementViewAction" %>
<%
	//we need the issue ids
	IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	Issue currentHistory = issueDao.findIssueByCode("CurrentHistory");
	Issue familyHistory = issueDao.findIssueByCode("FamHistory");
	Issue diagnosticNotes = issueDao.findIssueByCode("DiagnosticNotes");	
	Issue pastOcularHistory = issueDao.findIssueByCode("PastOcularHistory");
	Issue medicalHistory = issueDao.findIssueByCode("MedHistory");
	Issue ocularMeds = issueDao.findIssueByCode("OcularMedication");
	
	int appointmentNo = Integer.parseInt(request.getParameter("appointmentNo"));
	String demographicNo = request.getParameter("demographicNo");
	
	String currentHistoryValue = CaseManagementViewAction.getLatestCppNote(demographicNo,currentHistory.getId(),appointmentNo,true);
	String familyHistoryValue = CaseManagementViewAction.getLatestCppNote(demographicNo,familyHistory.getId(),appointmentNo,false);
	String diagnosticNotesValue = CaseManagementViewAction.getLatestCppNote(demographicNo,diagnosticNotes.getId(),appointmentNo,false);
	String pastOcularHistoryValue = CaseManagementViewAction.getLatestCppNote(demographicNo,pastOcularHistory.getId(),appointmentNo,false);
	String medicalHistoryValue = CaseManagementViewAction.getLatestCppNote(demographicNo,medicalHistory.getId(),appointmentNo,false);
	String ocularMedsValue = CaseManagementViewAction.getLatestCppNote(demographicNo,ocularMeds.getId(),appointmentNo,false);
%>

<div id="divR1" style="width:100%;">
	<table style="border: 1px solid rgb(102, 102, 102);width:100%" bgcolor="#ccccff">
		<tbody>
			<tr>
				<td>
					<table id="rowOne" width="100%">
						<tbody>
							<tr>
								<td>
									<div class="fieldtitle"><b>Current History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Family history</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Diagnostics Notes</b></div>
								</td>
								<td>
									<div style="font-size: 8pt; text-align: right; vertical-align: bottom;">
								        <a onmouseover="javascript:window.status='Minimize'; return true;" href="javascript:rowOneX1();" title="tooltipsClose">X</a>|
								        <a onmouseover="javascript:window.status='Small Size'; return true;" href="javascript:rowOneSmall1();" title="tooltipSmall">S</a>|
								        <a onmouseover="javascript:window.status='Medium Size'; return true;" href="javascript:rowOneNormal1();" title="tooltipNormal">N</a> |
								        <a onmouseover="javascript:window.status='Large Size'; return true;" href="javascript:rowOneLarge1();" title="tooltipLarge">L</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:rowOneFull1();" title="tooltipFull">F</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:reset1();" title="tooltipReset">R</a>
    								</div>
								</td>
							</tr>
							
							<tr>
								<td>
									<div class="fieldcontent"><textarea name="cpp_currentHis" cpp="CurrentHistory" issue_id="<%=currentHistory.getId() %>" class="examfieldwhite" tabindex="6" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=currentHistoryValue%></textarea></div>
								</td>
								<td>
									<div class="fieldcontent"><textarea name="cpp_familyHis" cpp="FamHistory" issue_id="<%=familyHistory.getId() %>" class="examfieldwhite" tabindex="7" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=familyHistoryValue %></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_diagnostics" cpp="DiagnosticNotes" issue_id="<%=diagnosticNotes.getId() %>" class="examfieldwhite" tabindex="8" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=diagnosticNotesValue %></textarea></div>
								</td>
							</tr>
						</tbody>
					</table>
					
					
					<table id="rowTwo" width="100%">
						<tbody>
							<tr>
								<td>
									<div class="fieldtitle"><b>Past Ocular History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Medical history</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Ocular Medications</b></div>
								</td>
								<td>
									<div style="font-size: 8pt; text-align: right; vertical-align: bottom;">
								        <a onmouseover="javascript:window.status='Minimize'; return true;" href="javascript:rowTwoX1();" title="tooltipsClose">X</a>|
								        <a onmouseover="javascript:window.status='Small Size'; return true;" href="javascript:rowTwoSmall1();" title="tooltipSmall">S</a>|
								        <a onmouseover="javascript:window.status='Medium Size'; return true;" href="javascript:rowTwoNormal1();" title="tooltipNormal">N</a> |
								        <a onmouseover="javascript:window.status='Large Size'; return true;" href="javascript:rowTwoLarge1();" title="tooltipLarge">L</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:rowTwoFull1();" title="tooltipFull">F</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:reset1();" title="tooltipReset">R</a>
    								</div>
								</td>
							</tr>
							
							<tr>
								<td>
									<div class="fieldcontent"><textarea name="cpp_pastOcularHis" cpp="PastOcularHistory" issue_id="<%=pastOcularHistory.getId() %>" class="examfieldwhite" tabindex="6" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=pastOcularHistoryValue %></textarea></div>
								</td>
								<td>
									<div class="fieldcontent"><textarea name="cpp_medicalHis" cpp="MedHistory" issue_id="<%=medicalHistory.getId() %>" class="examfieldwhite" tabindex="7" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=medicalHistoryValue %></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_ocularMeds" cpp="OcularMedication" issue_id="<%=ocularMeds.getId() %>" class="examfieldwhite" tabindex="8" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=ocularMedsValue %></textarea></div>
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>