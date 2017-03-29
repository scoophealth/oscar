<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>

<%@ include file="/casemgmt/taglibs.jsp" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
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

<%@ page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic, org.oscarehr.PMmodule.dao.ProviderDao, org.oscarehr.util.LoggedInInfo, org.oscarehr.util.SpringUtils, oscar.OscarProperties, org.oscarehr.common.dao.OscarAppointmentDao, org.oscarehr.common.model.Appointment, org.oscarehr.util.MiscUtils, oscar.SxmlMisc, org.oscarehr.common.dao.ProfessionalSpecialistDao"  %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProgramDao" %>

<%
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
String providerNo=loggedInInfo.getLoggedInProviderNo();
OscarProperties properties = OscarProperties.getInstance();

// This is here because the "case_program_id" session attribute is only set during the echart open routine.
// It's required for CaseManagementManager.filterNotes.  Since all of our Eyeform customers use 10016, this works (for now).
// Should get program id from program when name equals 'OSCAR'.
ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
Program p = programDao.getProgramByName("OSCAR");
if(p != null) 	 
	request.getSession().setAttribute("case_program_id", String.valueOf(p.getId()));
else
	request.getSession().setAttribute("case_program_id", "0");  //not sure if it should be 0..

String appointmentNo = "";
String appointmentReason = "";

OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");
try {
	Appointment appointment = null;
	if (request.getParameter("appointment_no") != null) {
		appointment = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
	} else {
		appointment = appointmentDao.findDemoAppointmentToday(Integer.parseInt(request.getParameter("demographic_no")));
	}

	if (appointment != null) {
		appointmentNo = appointment.getId().toString();
		appointmentReason = appointment.getReason();
	}
} catch (Exception e) {
	MiscUtils.getLogger().error("[eyeform] Couldn't get appointment data from database", e);
}


DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
Demographic d = demographicDao.getDemographicById(Integer.parseInt(request.getParameter("demographic_no")));

ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");

String rdOhip = null;
String rdName = null;

try {
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");

	rdOhip = SxmlMisc.getXmlContent(d.getFamilyDoctor(),"rdohip").trim();
	rdName = professionalSpecialistDao.getByReferralNo(rdOhip).getFormattedName();
} catch (Exception e) {
	MiscUtils.getLogger().error("Couldn't evaluate XML string for family doctor (" + d.getFamilyDoctor() + ")", e);
}
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath() %>/share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="<%=request.getContextPath() %>/share/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/share/calendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/share/calendar/calendar-setup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/share/javascript/jquery/jquery.autogrow-textarea.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/share/javascript/jquery/jquery-ui-1.8.15.custom.draggable.slider.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/share/css/jquery-ui-1.8.15.custom.draggable.slider.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/eyeform.css" />
<script type="text/javascript">
var ctx = "<%=request.getContextPath() %>";
var demographicNo = "<%=request.getParameter("demographic_no") %>";
var reason = "<%=appointmentReason%>";
var appointmentNo = "<%=appointmentNo %>";
var providerNo = "<%=providerNo %>";
var clinicNo = "<%=properties.getProperty("clinic_no", "").trim() %>";
</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/eyeform.js"></script>

<title><%=d.getLastName() %>, <%=d.getFirstName() %> (<%=d.getSex() %>) - Eyeform</title>
</head>
<body>
	<div id="leftBar">
		<div class="highlightBox">
			<div id="highlightSlider" class="contentItem"></div>
			<div id="highlightSliderLengthBox"><strong>Highlight:</strong> <span id="highlightSliderLength"></span></div>
		</div>
		<div class="smallBox macroBox">
			<span class="uiBarBtn macroBtn" id="openMacroBoxBtn">M</span>
			<input type="text" placeholder="macro" id="selectMacroExecuteBox">
			<div class="autocompleteList" id="macroAutocomplete" style="display: none;"></div>
		</div>
		<div class="smallBox boxTitleLink" id="allergies">
			<div class="title">
				Allergies
				<img src="<%=request.getContextPath() %>/images/icon-new-window.gif" />
				<span class="uiBarBtn"><span class="text addBtn" id="addAllergiesBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox" id="specsHistory">
			<div class="title">
				Specs History
				<span class="uiBarBtn"><span class="text addBtn" id="addSpecsHistoryBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox" id="procedures">
			<div class="title">
				Procedures
				<span class="uiBarBtn"><span class="text addBtn" id="addProcedureBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="labResults">
			<div class="title">
				Lab Results
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="diagrams">
			<div class="title">
				Diagrams/EForms
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
				<span class="uiBarBtn"><span class="text addBtn" id="addDiagramBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="documents">
			<div class="title">
				Documents
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="billing">
			<div class="title">
				Billing
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="tickler">
			<div class="title">
				Tickler
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
				<span class="uiBarBtn"><span class="text addBtn" id="addTicklerBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
		<div class="smallBox boxTitleLink" id="consultations">
			<div class="title">
				Consultations
				<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
				<span class="uiBarBtn"><span class="text addBtn" id="addConsultationBtn">+</span></span>
			</div>
			<div class="wrapper"><div class="content"></div></div>
		</div>
	</div>
	<div id="formContent">
		<div id="patientInfo">
			<span class="mrp">
			<span class="name"><%=d.getLastName().toUpperCase() %>, <%=d.getFirstName().toUpperCase() %> (<%=d.getSex() %>) (DOB: <%=d.getYearOfBirth() %>-<%=d.getMonthOfBirth() %>-<%=d.getDateOfBirth() %>, <%=d.getAgeInYears() %> years)</span>
				<strong>MRP</strong> <%=providerDao.getProviderName(d.getProviderNo()) %>
				<% if (rdName != null) { %>
				<strong>REFERRED BY</strong> <%=rdName %>
				<% } %>
			</span>
			<span class="appointmentSelectBtn">
				<span class="appointmentDate"><span class="appointmentDateText">Today</span>&#x25BC;</span>
				<span class="appointmentList"></span>
			</span>
			<span class="uiBarBtn"><span class="text rx3Btn">Rx3</span></span>
			<span class="uiBarBtn"><span class="text billingBtn">B</span></span>
			<span class="uiBarBtn"><span class="text masterRecBtn">M</span></span>
			<span class="uiBarBtn"><span class="text eChartBtn">E</span></span>

		</div>
		<div id="complaint">
			<span class="title">Today's Concern</span>
			<span class="content"></span>
			<input type="text" id="complaintBox" value="" />
		</div>
		<div class="formBoxes">
			<div class="smallBox boxTitleLink" id="planHistory">
				<div class="title">
					Previous Plans
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="diagnostics">
				<div class="title">
					Diagnostics
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
					<span class="uiBarBtn"><span class="text addBtn" id="addDiagnosticsBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="medicalOcularHistory">
				<div class="title">
					Medical History
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
					<span class="uiBarBtn"><span class="text addBtn" id="addHistoryBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
		</div>
		<div class="formBoxes">
			<div class="smallBox boxTitleLink" id="ocularMeds">
				<div class="title">
					Medications and Drops
					<img src="<%=request.getContextPath() %>/images/icon-new-window.gif" />
					<span class="uiBarBtn"><span class="text addBtn" id="addOcularMedsBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="familyMedicalOcularHistory">
				<div class="title">
					Family Medical/Ocular History
					<img src="<%=request.getContextPath() %>/images/icon-new-window.gif" />
					<span class="uiBarBtn"><span class="text addBtn" id="addFamilyHistoryBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="ocularHistory">
				<div class="title">
					Ocular History
					<img src="<%=request.getContextPath() %>/images/icon-new-window.gif" />
					<span class="uiBarBtn"><span class="text addBtn" id="addOcularHistoryBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>

		</div>
		<div class="formBoxes">
			<div class="smallBox boxTitleLink" id="reminders">
				<div class="title">
					<abbr title="(Reminders)">Notes</abbr>
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
					<span class="uiBarBtn"><span class="text addBtn" id="addReminderBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="patientLog">
				<div class="title">
					Patient Log
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
					<span class="uiBarBtn"><span class="text addBtn" id="addPatientLogBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
			<div class="smallBox boxTitleLink" id="eyedrops">
				<div class="title">
					Eye Drops/Ocular Medications
					<span class="newWindow"><img src="<%=request.getContextPath() %>/images/icon-new-window.gif" /></span>
					<span class="uiBarBtn"><span class="text addBtn" id="addEyedropBtn">+</span></span>
				</div>
				<div class="content">
					<ul></ul>
				</div>
			</div>
		</div>
		<div class="wideBox" id="currentIssueBox">
			<div class="title">
				Current Presenting Issue/History
				<span class="uiBarBtn"><span class="text smallerText" id="copyApptReasonBtn">Copy Appointment Reason</span></span>
				<span class="uiBarBtn"><span class="text smallerText" id="currentIssueShowHistoryBtn">History</span></span>
			</div>
			<div class="content" style="overflow: hidden;">
				<textarea style="width: 100%; height: 99%;" id="currentIssueAreaBox"></textarea>
			</div>
		</div>
		<div class="formBoxes">
			<div class="halfBox tallHalfBox" id="measurements">
				<div class="title">
					Measurements
					<span class="uiBarBtn"><span class="text smallerText" id="nextMeasurementsBtn">Next</span></span>
					<span class="uiBarBtn"><span class="text smallerText" id="prevMeasurementsBtn">Previous</span></span>
					<span class="uiBarBtn"><span class="text smallerText" id="showMeasurementsBtn">Modify</span></span>
				</div>
				<div class="content">
				</div>
			</div>
			<div class="halfBox tallHalfBox" id="impressionHistory">
				<div class="title">
					Impression History
				</div>
				<div class="content">
					<div class="historyList">
					</div>
				</div>
			</div>
		</div>
		<div class="wideBox" id="impressionBox">
			<div class="title">
				Impression
				<span class="uiBarBtn"><span class="text smallerText impressionBtn" id="stableBtn">Stable</span></span>
				<span class="uiBarBtn"><span class="text smallerText impressionBtn" id="noChangeBtn">No Change</span></span>
				<span class="uiBarBtn"><span class="text smallerText impressionBtn" id="copyLastBtn">Copy Last Impression</span></span>
			</div>
			<div class="content" style="overflow: hidden;">
				<textarea style="width: 100%; height: 99%;" id="impressionAreaBox"></textarea>
			</div>
		</div>
		<div class="formBoxes">
			<div class="halfBox" id="planHalfBox">
				<div class="title">
					Plan
				</div>
				<div class="content" style="height: auto; font-size: 12px; overflow-x: hidden;">
					<textarea style="width: 99%; height: 99%;" id="planBox"></textarea>
				</div>
				<div class="explanation">
					<strong>Sign, Save &amp; Exit</strong>: all users with the <strong>receptionist</strong> role will receive this message as a tickler.
				</div>
			</div>
			<div class="uiBtn billBtn floatRight" id="billBtn">Sign, Save &amp; Bill</div>
			<div class="uiBtn saveSignExitBtn floatRight" id="saveSignExitBtn">Sign, Save &amp; Exit</div>
			<div class="uiBtn saveBtn floatRight" id="saveBtn">Save &amp; Exit</div>
			<div class="floatRight" id="saveMsg" style="display: none; margin-right: 15px;">Saved</div>
			<span class="loaderImg floatRight" style="margin: 10px;"><img src="<%=request.getContextPath()%>/images/DMSLoader.gif" /></span>
		</div>
		<div class="formBoxes" style="display: inline-block;">
			<div class="halfBox" id="billingHalfBox">
				<div class="title">
					Bill Encounter
					<span class="uiBarBtn"><span class="text smallerText" id="clearBillingBtn">Clear</span></span>
				</div>
				<div class="content" style="height: auto; font-size: 12px;">
					<div>
						<div class="billInputItem">
							<input type="text" id="billDate" placeholder="date" /><img src="<%=request.getContextPath() %>/images/cal.gif" id="billDateBtn">
						</div>
						<div class="billInputItem">
							<input type="text" id="billCode" placeholder="billing code" />
							<div class="autocompleteList" id="billCodeAutocomplete" style="display: none;"></div>
						</div>
						<div class="billInputItem">
							<input type="text" id="billDxCode" placeholder="diagnostic code" />
							<div class="autocompleteList" id="billDxCodeAutocomplete" style="display: none;"></div>
						</div>
					</div>
					<div>
						For Provider: <select id="billProvider"></select>
					</div>
					<div id="billCodeMaximumError">
						You can only add a maximum of 10 separate billing codes per bill.
					</div>
					<div id="dxCodeMaximumError">
						You can only add a maximum of 3 diagnostic codes per bill.
					</div>
					<div id="dateError">
						The date entered is unreadable.  Please provide a date in the format <em>yyyy-mm-dd</em>.
					</div>
					<div>
						<span id="billDxCodeBox"></span>
						<span id="billCodeBox"></span>
					</div>
				</div>
			</div>
		</div>

	</div>

	<div id="measurementsBox" class="popoutBox measurementsBox" style="display: none;">
		<div class="boxContent">
			<div class="boxTitle">
				Measurements
				<div class="uiBarBtn" id="closeMeasurementsBtn"><span class="text">x</span></div>
				<div class="uiBarBtn nextMeasurementsBoxBtn" style="display: none;"><span class="text">Next</span></div>
				<div class="uiBarBtn prevMeasurementsBoxBtn" style="display: none;"><span class="text">Previous</span></div>
			</div>
			<div class="explanation" style="display: none;">The following measurements were recorded on <span class="measurementsTime"></span>. To modify, close this box and click "Modify" on the measurements box.</div>
			<div class="measurementsContent">
				<%@include file="/eyeform/exam.jsp" %>
			</div>
		</div>
	</div>

	<div id="newTicklerBox" class="popoutBox newTicklerBox" style="display: none;">
		<div class="boxContent">
			<div class="boxTitle">
				Add Tickler
				<div class="uiBarBtn uiCloseBtn" id="closeTicklerBoxBtn"><span class="text">x</span></div>
			</div>
			<input type="hidden" name="demographic_no" value="<%=request.getParameter("demographic_no") %>" />
			<table>
					<tr>
					<th>Message</th>
					<td><textarea rows=10 cols=40 id="newTicklerText" name="message"></textarea>
				</tr>
				<tr>
					<th>Recipient</th>
					<td>
						<select id="newTicklerRecipients" name="toProvider"></select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>This tickler message will be sent to the indicated provider and added to this patient's record.</td>
				</tr>
				<tr>
					<td></td>
					<td><div class="uiBtn" id="uiBoxSendTicklerBtn">Send Tickler</div><span class="loaderImg"><img src="<%=request.getContextPath()%>/images/DMSLoader.gif" /></span></td>
				</tr>
			</table>
		</div>

		<div class="arrow"></div>
	</div>

	<div class="popoutBox ticklerBox newTicklerBox openTicklerUiBox" style="display: none;">
		<div class="boxContent">
			<div class="fullBoxContent">
				<div class="messageContent"></div>
				<div class="details"><strong>Date</strong> <span class="messageDate"></span> | <strong>From</strong> <span class="messageFrom"></span> | <strong>To</strong> <span class="messageTo"></span></div>
			</div>
		</div>
		<div class="arrow" style="top: 20px;"></div>
	</div>

	<div class="popoutBox listBox" style="display: none;">
		<div class="boxContent">
			<div class="fullBoxContent">
			</div>
		</div>
		<div class="arrow" style="top: 1px;"></div>
	</div>

	<div id="newSpecsBox" class="popoutBox newSpecsBox" style="display: none;">
		<div class="boxContent">
			<div class="boxTitle">
				<span class="addEditSpecsTitle">Add Specs</span>
				<div class="uiBarBtn uiCloseBtn" id="closeNewSpecsBoxBtn"><span class="text">x</span></div>
			</div>
			<input type="hidden" name="specs.demographicNo" value="<%=request.getParameter("demographic_no") %>" />
			<input type="hidden" name="specs.appointmentNo" value="<%=request.getParameter("appointment_no") != null ? request.getParameter("appointment_no") : "" %>" />
			<table>
				<tr>
					<th>Date</th>
					<td><input type="text" name="specs.dateStr" id="specs.dateStr" /><img src="<%=request.getContextPath() %>/images/cal.gif" id="specs.dateCalBtn"></td>
				</tr>
				<tr>
					<th>Doctor's Name</th>
					<td><input type="text" name="specs.doctor" /></td>
				</tr>
				<tr>
					<th>Specs Type</th>
					<td><select name="specs.type"><option value="distance">distance</option><option value="bifocal">bifocal</option><option value="invisible bifocal">invisible bifocal</option><option value="reading">reading</option></select></td>
				</tr>
				<tr>
					<td colspan=2>
						<table id="newSpecsTable">
	   						<tr>
							   <td width="10%"></td>
							   <th width="18%">Sph</th>
							   <th width="18%">Cyl</th>
							   <th width="18%">Axis</th>
							   <th width="18%">Add</th>
							   <th width="18%">Prism</th>
						   </tr>

						   <tr>
							   <th width="10%">OD</th>
							   <td width="18%"><input type="text" value="" size="8" name="specs.odSph"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.odCyl"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.odAxis"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.odAdd"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.odPrism"></td>
						   </tr>

						   <tr>
							   <th width="10%">OS</th>
							   <td width="18%"><input type="text" value="" size="8" name="specs.osSph"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.osCyl"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.osAxis"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.osAdd"></td>
							   <td width="18%"><input type="text" value="" size="8" name="specs.osPrism"></td>
						   </tr>
		   				</table>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><div class="uiBtn" id="uiSaveNewSpecsBtn">Save</div><span class="loaderImg"><img src="<%=request.getContextPath()%>/images/DMSLoader.gif" /></span></td>
				</tr>
			</table>
		</div>

		<div class="arrow" style="top: 1px;"></div>
	</div>

	<div class="popoutBox newProcedureBox" id="newProcedureBox" style="display: none;">
		<div class="boxContent">
			<div class="boxTitle">
				<span class="addEditProcedureTitle">Add Procedure</span>
				<div class="uiBarBtn uiCloseBtn" id="closeNewProcedureBoxBtn"><span class="text">x</span></div>
			</div>
			<input type="hidden" name="proc.demographicNo" value="<%=request.getParameter("demographic_no") %>" />
			<input type="hidden" name="proc.appointmentNo" value="<%=request.getParameter("appointment_no") != null ? request.getParameter("appointment_no") : "" %>" />
			<input type="hidden" name="proc.id" value="0" />
			<table>
				<tr>
					<th>Date</th>
					<td><input type="text" name="proc.dateStr" id="newProcedureDate" /><img src="<%=request.getContextPath() %>/images/cal.gif" id="proc.dateCalBtn"></td>
				</tr>
				<tr>
					<th>Eye</th>
					<td><select name="proc.eye"><option value="OD">OD</option><option value="OS">OS</option><option value="OU">OU</option></select></td>
				</tr>
				<tr>
					<th>Procedure</th>
					<td><input type="text" name="proc.procedureName" /></td>
				</tr>
				<tr>
					<th>Doctor</th>
					<td><select id="addProcedureProviderList" name="proc.doctor"></select></td>
				</tr>
				<tr>
					<th>Location</th>
					<td><input type="text" name="proc.location" /></td>
				</tr>
				<tr>
					<th>Procedure Notes</th>
					<td><textarea cols=40 rows=5 name="proc.procedureNote"></textarea></td>
				</tr>
				<tr>
					<td></td>
					<td><div class="uiBtn" id="uiSaveNewProcedureBtn">Save</div><span class="loaderImg"><img src="<%=request.getContextPath()%>/images/DMSLoader.gif" /></span></td>
				</tr>
			</table>
		</div>

		<div class="arrow" style="top: 1px;"></div>
	</div>

	<div class="popoutBox macroListBox" id="macroListBox" style="display: none;">
		<div class="boxContent">
			<div class="boxTitle">
				Macros
				<div class="uiBarBtn uiCloseBtn" id="closeMacroBoxBtn"><span class="text">x</span></div>
			</div>
			<table>
				<tr>
					<td>
					<div class="macroControls">
							<span class="uiBarBtn" id="newMacroBtn"><span class="text smallerText">New</span></span>
							<span class="uiBarBtn" id="copyMacroBtn"><span class="text smallerText">Copy</span></span>
						</div>
						<select class="macroList" id="macroList" size="2"></select>

					</td>
					<td>
						<input type="hidden" id="macroIdField" name="macroIdField" value="" />
						<table>
							<tr>
								<th>Macro Name</th>
								<td><input type="text" id="macroNameBox" name="macroNameBox" /></td>
							</tr>
							<tr>
								<td colspan=2><hr /></td>
							<tr>
								<th>Impression Note</th>
								<td>
									<div class="macroControls">
										<span class="uiBarBtn" id="macroCopyLastImpressionBtn"><span class="text smallerText">Copy Last Impression</span></span>
									</div>
									<textarea cols=40 rows=5 id="macroImpressionBox" name="macroImpressionBox"></textarea>
								</td>
							</tr>
							<tr>
								<th>Plan</th>
								<td><input type="text" class="planInputBox" id="macroPlanBox" name="macroPlanBox" /></td>
							</tr>
							<tr style="display: none;">
								<th>Billing</th>
								<td>
									<div>
										<input type="text" id="macroBillingTextBox" placeholder="search" />
										<div class="autocompleteList" id="billingAutocomplete" style="display: none;"></div>
									</div>
									<div id="macroBillingItemBox"></div>
								</td>
							</tr>
							<tr>
								<td></td>
								<td><div class="uiBtn" id="macroSaveBtn">Save</div><span class="loaderImg"><img src="<%=request.getContextPath()%>/images/DMSLoader.gif" /></span></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>

		<div class="arrow" style="top: 1px;"></div>
	</div>
	<form name="caseManagementEntryForm"></form>

	<script type="text/javascript">
	var demographicName = "<%=d.getLastName().toUpperCase() %>, <%=d.getFirstName().toUpperCase() %>";
	</script>
</body>
</html>