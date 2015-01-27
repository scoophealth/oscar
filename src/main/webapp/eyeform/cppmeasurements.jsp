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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.Collection" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.casemgmt.dao.IssueDAO" %>
<%@ page import="org.oscarehr.casemgmt.model.Issue" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNote" %>
<%@ page import="org.oscarehr.casemgmt.web.CaseManagementViewAction" %>
<%@page import="oscar.OscarProperties"%>
<%
	//we need the issue ids
	IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	Issue currentHistory = issueDao.findIssueByCode("CurrentHistory");
	Issue familyHistory = issueDao.findIssueByCode("FamHistory");
	Issue diagnosticNotes = issueDao.findIssueByCode("DiagnosticNotes");
	Issue pastOcularHistory = issueDao.findIssueByCode("PastOcularHistory");
	Issue medicalHistory = issueDao.findIssueByCode("MedHistory");
	Issue ocularMeds = issueDao.findIssueByCode("OcularMedication");
	Issue patientLog = issueDao.findIssueByCode("PatientLog");
	Issue otherMeds = issueDao.findIssueByCode("OMeds");
	Issue misc = issueDao.findIssueByCode("Misc");

	int appointmentNo = 0;
	if(request.getParameter("appointmentNo")!=null && !"null".equalsIgnoreCase(request.getParameter("appointmentNo")))
		appointmentNo = Integer.parseInt(request.getParameter("appointmentNo"));
	String demographicNo = request.getParameter("demographicNo");

	CaseManagementNote currentHistoryNote = CaseManagementViewAction.getLatestCppNote(demographicNo,currentHistory.getId(),appointmentNo,true);
	CaseManagementNote familyHistoryNote = CaseManagementViewAction.getLatestCppNote(demographicNo,familyHistory.getId(),appointmentNo,false);
	CaseManagementNote diagnosticNotesNote = CaseManagementViewAction.getLatestCppNote(demographicNo,diagnosticNotes.getId(),appointmentNo,false);
	CaseManagementNote pastOcularHistoryNote = CaseManagementViewAction.getLatestCppNote(demographicNo,pastOcularHistory.getId(),appointmentNo,false);
	CaseManagementNote medicalHistoryNote = CaseManagementViewAction.getLatestCppNote(demographicNo,medicalHistory.getId(),appointmentNo,false);
	CaseManagementNote ocularMedsNote = CaseManagementViewAction.getLatestCppNote(demographicNo,ocularMeds.getId(),appointmentNo,false);
	CaseManagementNote otherMedsNote = CaseManagementViewAction.getLatestCppNote(demographicNo,otherMeds.getId(),appointmentNo,false);
	CaseManagementNote patientLogNote = CaseManagementViewAction.getLatestCppNote(demographicNo,patientLog.getId(),appointmentNo,false);
	CaseManagementNote miscNote = CaseManagementViewAction.getLatestCppNote(demographicNo,misc.getId(),appointmentNo,false);

	java.util.Properties oscarVariables = OscarProperties.getInstance();


%>
<%!
	public String getNoteValue(CaseManagementNote note) {
		if(note == null) {
			return new String();
		}
		return note.getNote();
	}

	public long getNoteId(CaseManagementNote note) {
		if(note == null) {
			return 0;
		}
		return note.getId();
	}
%>

<div id="divR1" style="width:100%;">
	<table style="border: 1px solid rgb(102, 102, 102);width:100%" bgcolor="#ccccff">
		<tbody>
			<tr>
				<td>
					<table id="rowOne" width="100%">
						<tbody>
							<tr>
							<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
								<td width="30%">
									<div class="fieldtitle"><b>Subjective</b></div>
								</td>
								<td width="30%">
									<div class="fieldtitle"><b>Ocular Diagnoses</b></div>
								</td>
								<td width="30%">
									<div class="fieldtitle"><b>Objective</b></div>
								</td>
							<%} else { %>
								<td>
									<div class="fieldtitle"><b>Current History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Family History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Diag. Notes</b></div>
								</td>
							<% } %>
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
									<div class="fieldcontent"><textarea name="cpp_currentHis" cpp="CurrentHistory" note_id="<%=getNoteId(currentHistoryNote)%>" issue_id="<%=currentHistory.getId() %>" class="examfieldwhite" tabindex="1" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(currentHistoryNote)%></textarea></div>
								</td>
								<td>
									<div class="fieldcontent"><textarea name="cpp_familyHis" cpp="FamHistory" note_id="<%=getNoteId(familyHistoryNote)%>" issue_id="<%=familyHistory.getId() %>" class="examfieldwhite" tabindex="2" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(familyHistoryNote)%></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_diagnostics" cpp="DiagnosticNotes" note_id="<%=getNoteId(diagnosticNotesNote)%>" issue_id="<%=diagnosticNotes.getId() %>" class="examfieldwhite" tabindex="3" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(diagnosticNotesNote) %></textarea></div>
								</td>								
							</tr>
						</tbody>
					</table>


					<table id="rowTwo" width="100%">
						<tbody>
							<tr>
							<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
								<td width="30%">
									<div class="fieldtitle"><b>Glaucoma Risk Factors</b></div>
								</td>
								<td width="30%">
									<div class="fieldtitle"><b>Past Ocular Hx</b></div>
								</td>
								<td width="30%">
									<div class="fieldtitle"><b>Medical History</b></div>
								</td>
							<% } else { %>
								<td>
									<div class="fieldtitle"><b>Past Ocular History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Medical History</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Patient Log</b></div>
								</td>
							<% } %>
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
							<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
								<td width="30%">
									<div class="fieldcontent"><textarea name="cpp_patientLog" cpp="PatientLog" note_id="<%=getNoteId(patientLogNote)%>" issue_id="<%=patientLog.getId() %>" class="examfieldwhite" tabindex="4" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(patientLogNote) %></textarea></div>
								</td>
								<td width="30%">
									<div class="fieldcontent"><textarea name="cpp_pastOcularHis" cpp="PastOcularHistory" note_id="<%=getNoteId(pastOcularHistoryNote)%>" issue_id="<%=pastOcularHistory.getId() %>" class="examfieldwhite" tabindex="5" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(pastOcularHistoryNote) %></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_medicalHis" cpp="MedHistory" note_id="<%=getNoteId(medicalHistoryNote)%>" issue_id="<%=medicalHistory.getId() %>" class="examfieldwhite" tabindex="6" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(medicalHistoryNote) %></textarea></div>
								</td>
							<% } else { %>
								<td>
									<div class="fieldcontent"><textarea name="cpp_pastOcularHis" cpp="PastOcularHistory" note_id="<%=getNoteId(pastOcularHistoryNote)%>" issue_id="<%=pastOcularHistory.getId() %>" class="examfieldwhite" tabindex="4" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(pastOcularHistoryNote) %></textarea></div>
								</td>
								<td>
									<div class="fieldcontent"><textarea name="cpp_medicalHis" cpp="MedHistory" note_id="<%=getNoteId(medicalHistoryNote)%>" issue_id="<%=medicalHistory.getId() %>" class="examfieldwhite" tabindex="5" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(medicalHistoryNote) %></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_patientLog" cpp="PatientLog" note_id="<%=getNoteId(patientLogNote)%>" issue_id="<%=patientLog.getId() %>" class="examfieldwhite" tabindex="6" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(patientLogNote) %></textarea></div>
								</td>
							<% } %>
							</tr>
						</tbody>
					</table>


					<table id="rowThree" width="100%">
						<tbody>
							<tr>
							<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
								<td width="30%">
									<div class="fieldtitle"><b>Drops Administered This Visit</b></div>
								</td>								
								<td width="30%">
									<div class="fieldtitle"><b>Misc</b></div>
								</td>
								<td width="30%">
									<div class="fieldtitle"><b>Systemic Meds</b></div>
								</td>
							<% } else { %>
								<td>
									<div class="fieldtitle"><b>Ocular Meds</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Other Meds</b></div>
								</td>
								<td>
									<div class="fieldtitle"><b>Misc</b></div>
								</td>
							<% } %>
								<td>
									<div style="font-size: 8pt; text-align: right; vertical-align: bottom;">
								        <a onmouseover="javascript:window.status='Minimize'; return true;" href="javascript:rowThreeX1();" title="tooltipsClose">X</a>|
								        <a onmouseover="javascript:window.status='Small Size'; return true;" href="javascript:rowThreeSmall1();" title="tooltipSmall">S</a>|
								        <a onmouseover="javascript:window.status='Medium Size'; return true;" href="javascript:rowThreeNormal1();" title="tooltipNormal">N</a> |
								        <a onmouseover="javascript:window.status='Large Size'; return true;" href="javascript:rowThreeLarge1();" title="tooltipLarge">L</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:rowThreeFull1();" title="tooltipFull">F</a> |
								        <a onmouseover="javascript:window.status='Full Size'; return true;" href="javascript:reset1();" title="tooltipReset">R</a>
    								</div>
								</td>
							</tr>

							<tr>
							<%  if (oscarVariables.getProperty("cme_js", "").equals("eyeform_DrJinapriya")) { %>
								<td width="30%">
									<div class="fieldcontent"><textarea name="cpp_ocularMeds" cpp="OcularMedication" note_id="<%=getNoteId(ocularMedsNote)%>" issue_id="<%=ocularMeds.getId() %>" class="examfieldwhite" tabindex="7" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(ocularMedsNote) %></textarea></div>
								</td>								
								<td width="30%">
									<div class="fieldcontent"><textarea name="cpp_misc" cpp="Misc" note_id="<%=getNoteId(miscNote)%>" issue_id="<%=misc.getId() %>" class="examfieldwhite" tabindex="8" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(miscNote) %></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_otherMeds" cpp="OMeds" note_id="<%=getNoteId(otherMedsNote)%>" issue_id="<%=otherMeds.getId() %>" class="examfieldwhite" tabindex="9" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(otherMedsNote)%></textarea></div>
								</td>
							<% } else { %>
								<td>
									<div class="fieldcontent"><textarea name="cpp_ocularMeds" cpp="OcularMedication" note_id="<%=getNoteId(ocularMedsNote)%>" issue_id="<%=ocularMeds.getId() %>" class="examfieldwhite" tabindex="7" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(ocularMedsNote) %></textarea></div>
								</td>
								<td>
									<div class="fieldcontent"><textarea name="cpp_otherMeds" cpp="OMeds" note_id="<%=getNoteId(otherMedsNote)%>" issue_id="<%=otherMeds.getId() %>" class="examfieldwhite" tabindex="8" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(otherMedsNote)%></textarea></div>
								</td>
								<td colspan="2">
									<div class="fieldcontent"><textarea name="cpp_misc" cpp="Misc" note_id="<%=getNoteId(miscNote)%>" issue_id="<%=misc.getId() %>" class="examfieldwhite" tabindex="9" cols="20" onchange="setSaveflag(true)" style="height: 60px; overflow: auto; width:100%"><%=getNoteValue(miscNote) %></textarea></div>
								</td>							
							<% } %>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</tbody>
	</table>
</div>
