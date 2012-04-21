
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%@ include file="/taglibs.jsp"%>
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%
	DynaValidatorForm form = (DynaValidatorForm)request.getAttribute("intakeAForm");
	Formintakea intake = (Formintakea)form.get("intake");
%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<html:base />
<title>INTAKE A. RECEPTION ASSESSMENT</title>

<style>
.sortable {
	background-color: #555;
	color: #555;
}

.b th {
	border-right: 1px solid #333;
	background-color: #ddd;
	color: #ddd;
	border-left: 1px solid #fff;
}

.message {
	color: red;
	background-color: white;
}

.error {
	color: red;
	background-color: white;
}
</style>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/intakeA.css" />'>
<script language="JavaScript"
	src='<html:rewrite page="/js/IntakeA.js" />'></script>
</head>
<body>
<html:form action="/PMmodule/IntakeA.do">
	<html:hidden property="intake.demographicNo" />
	<html:hidden property="bean.clientId" />
	<input type="hidden" name="method" value="save" />

	<table width="100%" border="0">
		<tr>
			<td width="5%">&nbsp;</td>

			<td>
			<table width="95%" border="0">
				<tr>
					<td colspan="3" class="style63">INTAKE A. RECEPTION ASSESSMENT
					&nbsp;&nbsp;&nbsp; <input type="button" value="Back"
						onclick="javascript:history.back()"></td>

				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td class="style76">Assessment Date (YYYY/MM/DD): <br>
					<html:text property="intake.assessDate" /></td>
					<td class="style76">Assessment start time: <br>
					<input type="text" name="assessStartTime" value=""> am / pm</br>
					</td>
					<td class="style76"></td>
				</tr>
				<tr>
					<td class="style76">Date client entered agency (YYYY/MM/DD)<br>
					<input type="text" name="enterSeatonDate" value=""></td>
					<td class="style76"><html:checkbox
						property="intake.cboxNewclient" value="Y" />New Client</td>
					<td class="style76"><html:checkbox
						property="intake.cboxDateofreadmission" value="Y" />Re-admission.
					Give date of last admission (YYYY/MM/DD). <html:text
						property="intake.dateOfReadmission" /></td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">Notes for
					completing this intake:</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="left" class="style61">Reception
					Staff:&nbsp;&nbsp; <html:checkbox
						property="intake.cboxIsstatementread" value="Y" /> <br>
					1. Please complete every section of this intake. If you are unable
					to complete a section - use 'Comments' section to explain why
					(e.g., client refused to answer, interview cut short).<br>
					2. Before beginning this intake, please read this paragraph to the
					client:"We would like to ask you some questions to ensure that you
					are getting the health care that you need and want while at this
					agency. Any answers you give us will be kept private in your file.
					We will collect numbers from the answers that we get from everyone
					on a regular basis to try and make the system better at this agency
					and in the City but in this case we would not use your name. You
					don't have to answer these questions. Your stay at this agency will
					not change if you don't answer these questions"</td>
				</tr>
			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51">Identifying
					Data</td>
				</tr>
				<tr>
					<td align="left" class="style76">Client's Surname <html:text
						property="intake.clientSurname" /></td>
					<td align="left" class="style76">Client's First Name <html:text
						property="intake.clientFirstName" /></td>

					<td align="left" class="style76">Date of Birth<br>
					(MM/DD/YYYY)<br>
					<html:select property="intake.month">
						<html:option value="">&nbsp;</html:option>
						<%for(int x=1;x<13;x++){ 
				String value = String.valueOf(x);
				if(value.length()==1) { value= "0" + value;}
			%>
						<html:option value="<%=value%>" />
						<%} %>
					</html:select> <html:select property="intake.day">
						<html:option value="">&nbsp;</html:option>
						<%for(int x=1;x<32;x++){ 
				String value = String.valueOf(x);
				if(value.length()==1) { value= "0" + value;}
			%>
						<html:option value="<%=value%>" />
						<%} %>
					</html:select> <html:text property="intake.year" size="7" maxlength="4" /></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="left" class="style76">Language(s) Spoken:</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxSpeakenglish" value="Y" />English</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxSpeakfrench" value="Y" />French</td>
					<td align="left" class="style76" colspan="2"><html:checkbox
						property="intake.cboxSpeakother" value="Y" />Other: <html:text
						property="intake.speakOther" /></td>
				</tr>

				<tr>
					<td align="left" class="style76">Sex:</td>
					<td align="left" class="style76"><html:radio
						property="intake.radioSex" value="male" />Male</td>
					<td align="left" class="style76"><html:radio
						property="intake.radioSex" value="female" />Female</td>
					<td align="left" class="style76"><html:radio
						property="intake.radioSex" value="transgendered" />Transgendered</td>
					<td align="left" class="style76"><html:radio
						property="intake.radioSex" value="declined" />Declined</td>
				</tr>
			</table>
			<table width="95%" border="1">
				<tr>
					<td align="center" class="style51">1. Reason for Admission</td>
				</tr>
				<tr>
					<td align="left" class="style76">What is your reason for
					coming to this agency? (state briefly in client's own words if
					possible)</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:textarea
						property="intake.reasonToSeaton" cols="75"></html:textarea></td>
				</tr>
				<tr>
					<td align="left" class="style76">Have you ever stayed at this
					agency before? (give dates) <html:text
						property="intake.datesAtSeaton" size="55" /></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td align="center" colspan="2" class="style51">2. Assistance
					Required</td>
				</tr>
				<tr>
					<td align="left" colspan="2" class="style76">Do you require
					assistance with any of the following :</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinhealth" value="Y" /></td>
					<td align="left" class="style76">Physical or Mental Health,
					including medication</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinidentification" value="Y" /></td>
					<td align="left" class="style76">Obtaining Identification</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinaddictions" value="Y" /></td>
					<td align="left" class="style76">Addictions</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinhousing" value="Y" /></td>
					<td align="left" class="style76">Housing issues</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistineducation" value="Y" /></td>
					<td align="left" class="style76">Education Issues</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinemployment" value="Y" /></td>
					<td align="left" class="style76">Employment issues</td>
				</tr>

				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinfinance" value="Y" /></td>
					<td align="left" class="style76">Financial issues</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinlegal" value="Y" /></td>
					<td align="left" class="style76">Legal issues</td>
				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxAssistinimmigration" value="Y" /></td>
					<td align="left" class="style76">Immigration issues</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">3.
					Identification</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">What
					identification do you have?</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxNoid" value="Y" />No ID</td>

				</tr>
				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxSincard" value="Y" />SIN Card:<br>
					<html:text property="intake.sinNum" maxlength="18" /></td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxHealthcard" value="Y" />Ontario Health Card#
					(ver) <br>
					<html:text property="intake.healthCardNum" maxlength="18" /> <html:text
						property="intake.healthCardVer" maxlength="2" size="2" /> <br />
					Eff. Date<html:text property="intake.effDate" size="10"
						maxlength="10" value="2099-01-01" />&nbsp;(yyyy-mm-dd)</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxBirthcertificate" value="Y" />Birth
					Certificate<br>
					<html:text property="intake.birthCertificateNum" maxlength="18" />
					</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxCitzenshipcard" value="Y" />Citizenship Card<br>
					<html:text property="intake.citzenshipCardNum" maxlength="18" /></td>

				</tr>

				<tr>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxImmigrant" value="Y" />Landed Immigrant<br>
					<html:text property="intake.immigrantNum" maxlength="18" /></td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxRefugee" value="Y" />Convention Refugee<br>
					<html:text property="intake.refugeeNum" maxlength="18" /></td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxOtherid" value="Y" />Other (specify) <html:text
						property="intake.otherIdentification" maxlength="18" size="35" />
					</td>

				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">STAFF: Please
					photocopy and certify ID, and put copy in client file</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxIdfiled" value="Y" />ID copied and filed</td>
					<td align="left" class="style76"><html:checkbox
						property="intake.cboxIdnone" value="Y" />ID not available</td>

				</tr>
				<tr>
					<td colspan="4" align="left" valign="top" class="style76">
					Comments:<br>
					<html:textarea property="intake.commentsOnID" cols="75"></html:textarea>

					</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">4. ON-LINE
					CHECK</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">What has been
					your main source of income in the past 12 months (check one)</td>
				</tr>
				<tr>
					<td colspan="1" width="10%" align="left" class="style76"><html:checkbox
						property="intake.cboxOw" value="Y" />OW</td>
					<td colspan="1" width="12%" align="left" class="style76"><html:checkbox
						property="intake.cboxOdsp" value="Y" />ODSP</td>
					<td colspan="1" width="12%" align="left" class="style76"><html:checkbox
						property="intake.cboxWsib" value="Y" />WSIB</td>
					<td colspan="2" width="15%" align="left" class="style76"><html:checkbox
						property="intake.cboxEmployment" value="Y" />Employment</td>
					<td colspan="1" width="11%" align="left" class="style76"><html:checkbox
						property="intake.cboxEi" value="Y" />EI</td>
					<td colspan="1" width="11%" align="left" class="style76"><html:checkbox
						property="intake.cboxOas" value="Y" />OAS</td>
					<td colspan="1" width="10%" align="left" class="style76"><html:checkbox
						property="intake.cboxCpp" value="Y" />CPP</td>
					<td width="19%" colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxOtherincome" value="Y" />Other</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">OW/ODSP On-Line
					check completed by worker?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioOnlinecheck" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioOnlinecheck" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioActive" value="active" />Active</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioActive" value="inactive" />Inactive</td>
					<td colspan="5" align="left" class="style76"><html:checkbox
						property="intake.cboxNorecord" value="Y" />No Record</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76">Last issuance
					date (YYYY/MM/DD): <html:text property="intake.lastIssueDate" /></td>
					<td colspan="3" align="left" class="style76">Office: <html:text
						property="intake.office" /></td>
					<td colspan="2" align="left" class="style76">Worker#: <html:text
						property="intake.workerNum" /></td>
					<td colspan="2" align="left" class="style76">Amount Received:
					<br>
					$ <html:text property="intake.amtReceived" maxlength="9" size="10" />
					</td>

				</tr>
			</table>



			<table width="95%" border="1">
				<tr>
					<td colspan="9" align="center" class="style51">5. HEALTH</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Physical Health</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. Do you have a
					regular medical doctor or specialist?</td>
					<td width="11%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdoctor" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdoctor" value="no" />No</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdoctor" value="dontKnow" />Don't Know</td>

				</tr>
				<tr>
					<td colspan="9" class="style76">If yes, what is the name,
					address and phone # of your doctor or specialist?</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Name: <html:text
						property="intake.doctorName" /></td>
					<td colspan="5" align="left" class="style76">Phone #: <html:text
						property="intake.doctorPhone" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Ext. <html:text property="intake.doctorPhoneExt" /></td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Address: <html:text
						property="intake.doctorAddress" size="70" maxlength="150" /></td>
				</tr>

				<tr>
					<td colspan="7" align="left" class="style76">If yes, Would you
					be able to see this doctor again if you needed to see a doctor?</td>
					<td width="8%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioSeedoctor" value="yes" />Yes</td>
					<td width="15%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdoctor" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="7" align="left" class="style76">2. Do you have
					any health issue that we should know about in the event of an
					emergency?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHealthissue" value="yes" />Yes</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHealthissue" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">If yes, give details <br>
					<br>
					<html:textarea property="intake.healthIssueDetails" cols="75"></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan="9" class="style76">3. Do you have any of the
					following (read list and check all that apply):</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76"><html:checkbox
						property="intake.cboxHasdiabetes" value="Y" />Diabetes. If yes
					--></td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxInsulin" value="Y" />Insulin dependent?</td>
					<td colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxEpilepsy" value="Y" />Epilepsy</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxBleeding" value="Y" />Bleeding Disorder</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxHearingimpair" value="Y" />Hearing
					Impairment</td>
					<td width="19%" colspan="1" align="left" class="style76"><html:checkbox
						property="intake.cboxVisualimpair" value="Y" />Visual Impairment
					</td>
					<td colspan="6" align="left" class="style76"><html:checkbox
						property="intake.cboxMobilityimpair" value="Y" />Mobility
					impairment. Give details: <html:text
						property="intake.mobilityImpair" size="35" /></td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">4. Do you have
					any other health concern that you wish to share with us?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioOtherhealthconcern" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioOtherhealthconcern" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					details including duration of problem, any medications taken
					(include over the counter meds), outcome, etc <br>
					<br>
					<html:textarea property="intake.otherHealthConerns" cols="75"></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan="9" align="left" class="style76">Medications</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Are you
					presently taking any medications?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioTakemedication" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioTakemedication" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">Name(s) of
					Medication(s) <html:text property="intake.namesOfMedication"
						size="70" /></td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">2. Do you need
					help obtaining medication?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioHelpobtainmedication" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioHelpobtainmedication" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					details <br>
					<br>
					<html:textarea property="intake.helpObtainMedication" cols="75"></html:textarea>

					</td>
				</tr>

				<tr>
					<td colspan="5" align="left" class="style76">3. Are you
					allergic to any medications?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioAllergictomedication" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioAllergictomedication" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					medication names:<br>
					<br>
					<html:text property="intake.allergicToMedicationName" size="75" />
					<br>
					<br>
					If yes, give reaction (hives, rash, anaphylaxis=breathing problems,
					shortness of breath, itching swelling of mouth and throat) <html:textarea
						property="intake.reactionToMedication" cols="75"></html:textarea>

					</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">Mental Health</td>
				</tr>
				<tr>
					<td colspan="5" align="left" class="style76">1. Do you have
					any mental health concerns that you wish to share with us?</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioMentalhealthconcerns" value="yes" />Yes</td>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioMentalhealthconcerns" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="9" align="left" class="style76">If yes, give
					diagnosis (schizophrenia, depression etc, and date diagnosed)<br>
					<br>
					<html:textarea property="intake.mentalHealthConcerns" cols="75"></html:textarea>
					</td>
				</tr>

			</table>


			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">6. SURVEY
					MODULE - ACCESS TO HEALTH CARE</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style61">Please read the
					following statement to the client:"To better provide care at this
					agency, I'd like to ask you a few more questions about your access
					to health care" &nbsp;&nbsp; <html:checkbox
						property="intake.cboxIsstatement6read" value="Y" /></td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">1. Not counting
					when you were an overnight patient, in the past 12 months, how many
					times have you seen a general practitioner or family physician
					about your physical, emotional or mental health? <html:text
						property="intake.frequencyOfSeeingDoctor" /></td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">2. Where did the
					most recent contact take place? (Read list, Mark one only.)</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxVisitwalkinclinic" value="Y" />Walk-in
					clinic<br>
					<html:checkbox property="intake.cboxVisithealthcenter" value="Y" />Community
					health centre</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxVisitemergencyroom" value="Y" />Hospital
					emergency room<br>
					<html:checkbox property="intake.cboxVisitothers" value="Y" />Other
					(specify)</td>
					<td colspan="2" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxVisithealthoffice" value="Y" />Health
					Professionals office</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;If more than 1 contact
					and not in emergency room: <br>
					3. Would you be able to see this doctor again if you needed to see
					a doctor?</td>
					<td width="17%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioSeesamedoctor" value="yes" />Yes</td>
					<td width="23%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioSeesamedoctor" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">4. In the past 12
					months, how many times have you seen a physician in a hospital
					Emergency room? <html:text
						property="intake.frequencyOfSeeingEmergencyRoomDoctor" size="15" />
					</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">5. During the
					past 12 months, was there ever a time when you needed health care
					or advice but did not receive it?</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioDidnotreceivehealthcare" value="yes" />Yes<br>

					<html:radio property="intake.radioDidnotreceivehealthcare"
						value="dontKnow" />Don't Know</td>
					<td width="23%" colspan="1" align="left" class="style76"><<html:radio
						property="intake.radioDidnotreceivehealthcare" value="no" />No<br>
					<html:radio property="intake.radioDidnotreceivehealthcare"
						value="refuse" />Refuse<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; to answer</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">If yes, Thinking
					of the most recent time, what was the type of care that was needed?
					(Do not read list. Mark all that apply)</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxTreatphysicalhealth" value="Y" />Treatment of
					a physical<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;health problem <br>
					<html:checkbox property="intake.cboxTreatmentalhealth" value="Y" />Treatment
					of an emotional or<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mental health problem</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxRegularcheckup" value="Y" />A regular
					check-up<br>
					<html:checkbox property="intake.cboxTreatotherreasons" value="Y" />Any
					other reason (specify)<br>
					<html:text property="intake.treatOtherReasons" size="35" /></td>
					<td colspan="2" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxTreatinjury" value="Y" />Care of injury</td>
				</tr>
				<tr>
					<td colspan="6" align="left" class="style76">6. If you had a
					physical, emotional or mental health problem that you needed help
					with, where would you go for help? (Read list. Mark one only)</td>
				</tr>

				<tr>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxGotowalkinclinic" value="Y" />Walk-in clinic<br>
					<html:checkbox property="intake.cboxGotohealthcenter" value="Y" />Community
					health centre</td>
					<td colspan="2" align="left" class="style76"><html:checkbox
						property="intake.cboxGotoemergencyroom" value="Y" />Hospital
					emergency room<br>
					<html:checkbox property="intake.cboxGotoothers" value="Y" />Other
					(specify)<br>
					<html:text property="intake.goToOthers" size="35" /></td>
					<td colspan="2" align="left" valign="top" class="style76"><html:checkbox
						property="intake.cboxHealthoffice" value="Y" />Health
					Professionals office</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">7. Do you have an
					appointment to see a general practitioner or family doctor in the
					next 3 months?</td>
					<td width="17%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioAppmtseedoctorin3mths" value="yes" />Yes</td>
					<td width="23%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioAppmtseedoctorin3mths" value="no" />(A)No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">8. Do you feel
					you would benefit from having a regular doctor or do you need a
					regular doctor?</td>
					<td width="17%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioNeedregulardoctor" value="yes" />(B)Yes</td>
					<td width="23%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioNeedregulardoctor" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">9. Would you
					object to having an appointment with a regular doctor in the next 4
					weeks?</td>
					<td width="17%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioObjecttoregulardoctorin4wks" value="yes" />Yes
					</td>
					<td width="23%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioObjecttoregulardoctorin4wks" value="no" />(C)No
					</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">10. How would you
					rate your overall health?</td>
					<td width="19%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRateoverallhealth" value="poor" />Poor</td>
					<td width="19%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRateoverallhealth" value="fair" />Fair</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRateoverallhealth" value="good" />Good</td>
					<td colspan="1" align="left" class="style76"><html:radio
						property="intake.radioRateoverallhealth" value="excellent" />Excellent
					</td>
				</tr>

				<tr>
					<td colspan="6" align="left" class="style76">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Intake Staff: Has a
					"YES" been selected for an A, B, AND C question?<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; If yes, then ask: "Would
					you be willing to speak to a researcher about a study on accessing
					primary health care?"</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76"><html:radio
						property="intake.radioSpeaktoresearcher" value="yes" />Yes</td>
					<td colspan="4" align="left" class="style76"><html:radio
						property="intake.radioSpeaktoresearcher" value="no" />No</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">7. EMERGENCY
					CONTACT</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Please give the
					name/ address/ phone # of someone who may be contacted in the event
					of an emergency?</td>
				</tr>
				<tr>
					<td colspan="2" align="left" class="style76">Name: <html:text
						property="intake.contactName" /></td>
					<td colspan="2" width="50%" align="left" class="style76">
					Phone #: <html:text property="intake.contactPhone" /></td>

				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Address: <html:text
						property="intake.contactAddress" /></td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">What relationship
					is this person to you? (friend, sister, next-of-kin, etc.) <html:text
						property="intake.contactRelationship" /></td>
				</tr>
			</table>

			<table width="95%" border="0">
				<tr>
					<td class="style76">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;For
					Staff Use Only</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="4" align="center" class="style51">8. STAFF
					RATINGS AND IDENTIFIED ISSUES</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Based on my own
					observations and from information from other members on my team:</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I think this
					person currently has uncontrolled severe mental illness (like
					schizophrenia, bipolar disorder)</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasmentalillness" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasmentalillness" value="uncertain" />Uncertain
					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasmentalillness" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drinking</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdrinkingproblem" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasdrinkingproblem" value="uncertain" />Uncertain

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasdrinkingproblem" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from uncontrolled drug use</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasdrugproblem" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasdrugproblem" value="uncertain" />Uncertain

					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasdrugproblem" value="no" />No</td>
				</tr>


				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person has severe problems from an uncontrolled physical
					health problem</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHashealthproblem" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHashealthproblem" value="uncertain" />Uncertain
					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHashealthproblem" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioHasbehaviorproblem" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasbehaviorproblem" value="uncertain" />Uncertain
					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioHasbehaviorproblem" value="no" />No</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">I think this
					person will need our (Agency's) services for more than 60 days</td>
				</tr>

				<tr>
					<td colspan="1" width="22%" align="left" class="style76">&nbsp;

					</td>
					<td width="26%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioNeedseatonservice" value="yes" />Yes</td>
					<td width="28%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioNeedseatonservice" value="uncertain" />Uncertain
					</td>
					<td width="24%" colspan="1" align="left" valign="top"
						class="style76"><html:radio
						property="intake.radioNeedseatonservice" value="no" />No</td>
				</tr>

				<tr>
					<td colspan="4" align="left" class="style76">I am concerned
					that this person is handicapped or disabled by severe behaviour
					problems</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">PRINT AND SIGN
					NAME:</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">9. ORIENTATION
					TO AGENCY</td>
				</tr>

				<tr>
					<td colspan="1" width="25%" align="left" class="style76">Tour
					given?</td>
					<td width="10%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioSeatontour" value="yes" />Yes</td>
					<td colspan="4" align="left" valign="top" class="style76"><html:radio
						property="intake.radioSeatontour" value="no" />No (If no, give
					reason) <html:text property="intake.seatonNotToured" size="55" />
					</td>
				</tr>

				<tr>
					<td colspan="1" width="25%" align="left" class="style76">
					Information pamphlet issued?</td>
					<td width="10%" colspan="1" align="left" class="style76"><html:radio
						property="intake.radioPamphletissued" value="yes" />Yes</td>
					<td colspan="4" align="left" valign="top" class="style76"><html:radio
						property="intake.radioPamphletissued" value="no" />No (If no,
					give reason) <html:text property="intake.pamphletNotIssued"
						size="55" /></td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">10.
					COMMENTS/SUMMARY</td>
				</tr>
				<tr>
					<td colspan="6" align="center" class="style76"><html:textarea
						property="intake.summary" cols="95" rows="7"></html:textarea></td>
				</tr>

			</table>
			<table width="95%" border="1">
				<tr>
					<td colspan="6" align="center" class="style51">COMPLETED BY</td>
				</tr>
				<tr>
					<td colspan="4" align="left" class="style76">Completed by:
					(sign and print name)<br>
					<br>
					<html:text property="intake.completedBy" size="45" /></td>
					<td width="28%" colspan="1" align="center" class="style76">
					Time Assessment Completed:</td>
					<td width="22%" colspan="1" align="left" valign="middle"
						STYLE="text-align: right" class="style76"><html:text
						property="intake.assessCompleteTime" size="15" /> am/pm</td>
				</tr>
			</table>

			<table width="95%" border="1">
				<tr>
					<td colspan="3" align="center" class="style51"><!-- 11. TRIAGE- REFERRAL TO A PROGRAM	(Removed)
	 
	
	</td>
  </tr>
  <tr>	
  	<td colspan="3" align="left" class="style76">
		Based on my own observations (and from information from other members on my team) 
		I believe this client may be appropriate for one of the following programs:
	</td>
  </tr>
  
  <tr>
    <td width="36%"  colspan="1"  align="center" class="style76">
		Admission Criteria
	</td>
    <td width="33%" colspan="1"  align="center" class="style76">
		Exclusion Criteria:	
	</td>
    <td width="31%" colspan="1"  align="center" class="style76">
		Triage Contact #s:	
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<html:checkbox property="intake.cboxPamphletissued" value="Y" />Emergency Hostel Program
		<ul>
			<li>Homeless man</li>			
			<li>No other current resources</li>
			<li>No source of income</li>
			<li>Willing to follow case plan</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Violence or illicit drug use may result in client being barred from the Hostel</li>			
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5529 <br>
		(416) 338-3196 <br>
		(416) 338-3197<br><br>
		
		Steering Committee Member:<br>
		Maurice Jefferson (416) 392-5531	
		
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<html:checkbox property="intake.cboxHostel" value="Y" />Hostel - Fusion of Care Team
		<ul>
			<li>	Emergency Hostel Program client</li>			
			<li>	High mental or physical health needs</li>
		</ul>
	</td>
    <td colspan="1"  align="left" class="style76">&nbsp;
			
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 338-3311<br><br>
		
		Steering Committee Member:<br>
		Shawn Yoder (416) 392-6706		
	</td>
  </tr>	
  <tr>
    <td  colspan="1"  align="left"  valign="top" class="style76">
		<html:checkbox property="intake.cboxRotaryclub" value="Y" />Rotary Club of Toronto Infirmary
		<ul>
			<li>	Client is to be discharged from hospital and requires: <br>
			(i) Frequent physician or nursing follow-up and/or  <br>
			(ii) Rehabilitation to return to previous level of functioning.
			</li>			
		</ul>
		Client may be admitted to the Infirmary if he exhibits one or more of the following health conditions:
		<ul>
			<li>Requires follow-up care (post-operative care and wound care)</li>
			<li>Uncontrolled/poorly controlled chronic illness such as diabetes, cirrhosis, seizures, CHF and HIV</li>
			<li>Terminal illness that requires palliative care for comfort</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client unable to transfer safely (move from bed to chair unassisted)</li>
			<li>Client is incontinent and using a wheelchair </li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5598<br><br>
		
		Steering Committee Member:<br>
		Karen Smith (416) 392-5598
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxAnnexharm" value="Y" />Annex Harm Reduction Program
		<ul>
			<li>Chronic alcohol, substance use (including non-palatable substances)</li>
			<li>Severe uncontrolled physical illness </li>
			<li>Severe uncontrolled mental illness</li>
			<li>Severe uncontrolled behaviour problems</li>
			<li>Chronically homeless</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client wishing to maintain abstinence may not do well in this environment</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5519<br><br>
		
		Steering Committee Member: <br>
		Ken Mendonca (416) 392-5522
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxLongtermprogram" value="Y" />Long Term Program
		<ul>
			<li>Age 65+ - refer direct from Reception </li>
		</ul>
			OR
		<ul>
			<li>Age 50+</li>
			<li>Willing and able to pay rent </li>
		</ul>
			OR Any age and one of the following criteria:
		<ul>
			<li>On pension</li>
			<li>Physically or socially vulnerable</li>
			<li>Mental health issues</li>
			<li>Physical disability</li>
			<li>Short-term disability (non-ambulatory)</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Client poses significant risk to residents of long term program</li>
			<li>History of aggressive or predatory behaviour</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake:<br>
		(416) 392-6049 <br>
		(416) 338-3175<br><br>
		
		Steering Committee Member:<br>
		Mark Headley (416) 392-5543
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxBirchmountresidence" value="Y" />Birchmount Residence
		<ul>
			<li>Age 55+ (men aged 50-55 considered)</li>
			<li>Chronic health issues</li>
			<li>Able to carry out the activities of daily living without staff assistance</li>
			<li>Able to climb stairs</li>
			<li>Able to behave in a reliable, predictable and respectful manner in the community</li>
			<li>Stable mental health</li>
			<li>Able to act appropriately and responsibly if consuming alcohol and willing to cooperate with a harm reduction approach</li>
			<li>Willing to take medication as prescribed by the attending physician and cooperative with medical and Nursing plan and attend medical appointments and procedures as required.</li>
			<li>Willing to be financially responsible and pay rent</li>
			<li>Has legal status in Canada</li>
			<li>Willing to cooperate with plan for future housing or appropriate care facility</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		<ul>
			<li>Recent history of incarceration, criminal activity, violence</li>
			<li>Requires bedside nursing or personal care</li>
			<li>Requires oxygen, IV or intrusive medical procedures of equipment</li>
			<li>Incontinence issues</li>
			<li>Danger to self or others</li>
			<li>Abuse of medications or street drugs</li>
		</ul>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-6167<br><br>
		
		Steering Committee Member:<br>
		Carla O'Brien (416) 392-5543<br><br>
		Referrals to Birchmount are made by Long Term program.<br><br>
		
		N.B. Client must be resident of Seaton House Long Term program for more than 30 days prior to admission (some exceptions considered).	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxOneillhouse" value="Y" />O'Neill House
		<ul>
			<li>Severe problems related to street drug use (Crack cocaine)</li>
			<li>Client does not want treatment</li>
			<li>Barred from Hostel due to drug use</li>
			<li>2 or more incident reports for drug use</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake <br>
		(416) 392-5407<br><br>
		
		Steering Committee Member:<br>
		Tom Fulgosi (416) 392-5436  
		
	</td>
  </tr>	

  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxFortyork" value="Y"/>Fort York Residence
		<ul>
	        <li>Willing and able to work</li>
    	    <li>If employed: willing to save 60% of income</li>
        	<li>Willing to do volunteer work</li>
	        <li>Has ID (hard copy)</li>
    	    <li>Has up-to-date resume</li>
        	<li>Willing and able to follow case plan</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		Clients with substance use issues will be required to complete a Rehab program prior to admission to Fort York Residence.	
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake:<br>
		Mark Horne: # TBA*<br><br>
		
		Steering Committee Member#: TBA*		
	</td>
  </tr>	
  <tr>
    <td  colspan="1"  align="left" valign="top" class="style76">
		<html:checkbox property="intake.cboxDownsviewdells" value="Y" />Downsview Dells
		<ul>
            <li>Chemically dependent now wishing abstinence</li>
            <li>Willing and able to complete 30-day treatment at Humber River Regional Hospital</li>
		</ul>
				
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
	<br><br>
		Disruptive behaviour	
	</td>
    <td colspan="1"  align="left" valign="top" class="style76">
		<br><br>
		Triage/Intake: <br>
		(416) 392-5452<br><br>
		
		Steering Committee:<br>
		MemberDon Inglis (416) 392-5452		
	</td>
  </tr>	
</table>--> <c:if
						test="${intakeAForm.map.intake.demographicNo == null or not empty in_community_program}">
						<table width="95%" border="1">
							<tr>
								<td colspan="3" align="center" class="style51">11.
								Admission</td>
							</tr>
							<tr>
								<td colspan="3" align="left" class="style76">You must admit
								this client into a program. If you cannot admit into a program
								in your domain, the client will be admitted to the "holding
								tank" program by default.</td>
							</tr>
							<tr>
								<td colspan="3" align="left" class="style76">Admit to bed
								program: <html:select property="view2.admissionProgram">
									<html:option value="0">&nbsp;</html:option>
									<html:options collection="programDomainBed" property="id"
										labelProperty="name" />
								</html:select></td>
							</tr>
						</table>

						<table width="95%" border="0">
							<tr>
								<td class="style76">&nbsp;</td>
							</tr>
						</table>
						<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
					</c:if>
					<table width="95%" border="1">
						<tr>
							<td align="center" class="style76">
							<div align="center"><html:submit value="Save"
								onclick="this.form.method.value='save';" /> <html:cancel
								onclick="this.form.method.value='cancel';" /> <input
								type="button" value="Print"
								onclick="javascript:return onPrint();" />
							</td>
						</tr>
					</table>
					<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->


					<table>
						<tr>
							<td height="15">&nbsp;</td>
						</tr>
					</table>


					</html:form>
</body>
</html:html>
