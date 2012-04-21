
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
<%@ page import="org.oscarehr.PMmodule.web.formbean.*"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%
	IntakeCFormBean formBean;
	DynaValidatorForm form = (DynaValidatorForm) session.getAttribute("intakeCForm");
	formBean = (IntakeCFormBean) form.get("view2");
	Formintakec intake = (Formintakec) form.get("intake");
	pageContext.setAttribute("formBean", formBean);
%>
<script>
	function go_to_top() {
		var form = document.intakeCForm;
		form.method.value='refresh';
		form.submit();
	}
</script>
<html:hidden property="view2.numHospitalization" />
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style63">INTAKE C. Mental Health Team Data
		Collection Form -- Step4: Medical Status &nbsp;&nbsp;&nbsp; <input
			type="button" name="backToClientSearch" value="   Back   "
			onclick="javascript:redirectToClientSearch(
		     '','','<html:rewrite page="/PMmodule/ClientSearchAction.admit"/>')">
		</td>
	</tr>
	<tr>
		<td height="10" align="right" colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td width="13%" class="style76">Client Number:</td>
		<td width="21%" class="style76">&nbsp;<c:out
			value="${requestScope.demographicNo}" /></td>
		<td colspan="2" class="style76">&nbsp;</td>
	</tr>
	<tr>
		<td class="style76">Client First Name:</td>
		<td width="21%" class="style76"><html:text styleClass="style71"
			property="intake.clientFirstName" readonly="true" /></td>
		<td width="14%" class="style76">Client Last Name:</td>
		<td width="52%"><html:text styleClass="style71"
			property="intake.clientSurname" readonly="true" /></td>
	</tr>
</table>
<!-- ################################################################################################### -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">11. <a name="Num11">Primary
		Diagnosis (Check ONE only)</a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="1"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Adjustment Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="2"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Anxiety Disorders</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox property="intake.cboxPTSd"
			styleId="11_0_CHILD"
			onclick="checkAnxietyDisorder(this,document.intakeCForm)" /></td>
		<td width="90%" colspan="2" class="style76">Post-traumatic stress
		disorder (PTSD)</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox property="intake.cboxOCd"
			styleId="11_1_CHILD"
			onclick="checkAnxietyDisorder(this,document.intakeCForm)" /></td>
		<td colspan="2" class="style76">Obsessive compulsive disorder
		(OCD)</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cboxSubstanceAnxietyDisorder" styleId="11_2_CHILD"
			onclick="checkAnxietyDisorder(this,document.intakeCForm)" /></td>
		<td colspan="2" class="style76">Substance induced anxiety
		disorder</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cboxOtherAnxietyDisorder" styleId="11_3_CHILD"
			onclick="checkAnxietyDisorder(this,document.intakeCForm)" /></td>
		<td colspan="2" class="style76">Other anxiety disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="3"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Delirium, Dementia, Amnestic and
		Cognitive Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="4"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Disorder of Childhood/Adolescence</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="5"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Disassociative Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="6"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Eating Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="7"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Factitious Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="8"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Impulse Control Disorders Not
		Elsewhere Classified</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="9"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Mental Disorders due to General
		Medical Conditions</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="10"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Mood Disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="11"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Personality Disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="12"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Schizophrenia and other Psychotic
		Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="13"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Sexual and Gender Identity
		Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="14"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Sleep Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="15"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Somatoform Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="16"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Substance-Related Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="17"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Developmental Handicap</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioPrimaryDiagnosis" value="18"
			onclick="uncheckPrimaryDiagnosisChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">12. <a name="Num12">Secondary
		Diagnoses (Check ALL that apply) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAdjustDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Adjustment Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAnxietyDisorder" styleId="12_PARENT"
			onclick="uncheck2ndAnxietyDisorderChildren(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Anxiety Disorders</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAnxietyDisorderPSd" styleId="12_0_CHILD"
			onclick="check2ndAnxietyDisorder(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td width="90%" colspan="2" class="style76">Post-traumatic stress
		disorder (PTSD)</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAnxietyDisorderOCd" styleId="12_1_CHILD"
			onclick="check2ndAnxietyDisorder(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="2" class="style76">Obsessive compulsive disorder
		(OCD)</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAnxietyDisorderFromSubstance"
			styleId="12_2_CHILD"
			onclick="check2ndAnxietyDisorder(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="2" class="style76">Substance induced anxiety
		disorder</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndAnxietyDisorderOther" styleId="12_3_CHILD"
			onclick="check2ndAnxietyDisorder(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="2" class="style76">Other anxiety disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndCognitiveDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Delirium, Dementia, Amnestic and
		Cognitive Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndChildhoodDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Disorder of Childhood/Adolescence</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndDisassociativeDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Disassociative Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndEatingDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Eating Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndFactitiousDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Factitious Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndImpulsiveDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Impulse Control Disorders Not
		Elsewhere Classified</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndMedicalMentalDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Mental Disorders due to General
		Medical Conditions</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndMoodDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Mood Disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndPersonalityDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Personality Disorder</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndSchizophrenia"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Schizophrenia and other Psychotic
		Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndGenderIdentityDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Sexual and Gender Identity
		Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndSleepDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Sleep Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndSomatoformDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Somatoform Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndSubstanceDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Substance-Related Disorders</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cbox2ndDevelopmentalDisorder"
			onclick="document.intakeCForm.elements['intake.cbox2ndUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Developmental Handicap</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox property="intake.cbox2ndUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">13. <a name="Num13">Other
		Illness Information (Check ALL that apply) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxConcurrentDisorder"
			onclick="document.intakeCForm.elements['intake.cboxNa'].checked=false;" /></td>
		<td width="95%" colspan="3" class="style76">Concurrent Disorder
		(Substance Abuse & Mental Illness)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox property="intake.cboxDualDisorder"
			onclick="document.intakeCForm.elements['intake.cboxNa'].checked=false;" /></td>
		<td colspan="3" class="style76">Dual Diagnosis (Developmental
		Disability & Mental Illness)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxMOHLTCDisorder"
			onclick="document.intakeCForm.elements['intake.cboxNa'].checked=false;" /></td>
		<td colspan="3" class="style76">MOHLTC Initiatives (targeted
		illness such as Cancer, Diabetes and Cardiac)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxOtherChronicIllness"
			onclick="document.intakeCForm.elements['intake.cboxNa'].checked=false;" /></td>
		<td colspan="3" class="style76">Other Chronic Illnesses and/or
		Physical Disabilities</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox property="intake.cboxNa" /></td>
		<td colspan="3" class="style76">Not Applicable</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">17. <a name="Num17">Baseline
		Health Care Access </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Has someone to go to for health
		care when needed?</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHealthCareAccess" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Yes</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHealthCareAccess" value="2" /></td>
		<td colspan="3" class="style76">Does not access health care</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHealthCareAccess" value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Where does client go for health
		care? (Check ALL that apply)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasRegularHealthProvider"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td width="95%" colspan="3" class="style76">Has regular health
		care provider (e.g. doctor, nurse practitioner, community health
		centre)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseUseShelterClinic"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses a clinic at a shelter,
		hostel or drop-in (e.g. nursing clinics)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseUseHealthBus"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses the Health Bus</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseUseWalkinClinic"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses walk-in clinic</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseUseHospitalEmergency"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses hospital emergency room</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseDoNotAccessHealthCare"
			onclick="document.intakeCForm.elements['intake.cboxBaseAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Does not access health care</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseAccessHealthCareUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">18. <a name="Num18">Current
		Health Care Access </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Has someone to go to for health
		care when needed?</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHealthCareAccess" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Yes</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHealthCareAccess" value="2" /></td>
		<td colspan="3" class="style76">Does not access health care</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHealthCareAccess" value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Where does client go for health
		care? (Check ALL that apply)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasRegularHealthProvider"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td width="95%" colspan="3" class="style76">Has regular health
		care provider (e.g. doctor, nurse practitioner, community health
		centre)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrUseShelterClinic"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses a clinic at a shelter,
		hostel or drop-in (e.g. nursing clinics)</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrUseHealthBus"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses the Health Bus</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrUseWalkinClinic"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses walk-in clinic</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrUseHospitalEmergency"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Uses hospital emergency room</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrDoNotAccessHealthCare"
			onclick="document.intakeCForm.elements['intake.cboxCurrAccessHealthCareUnknown'].checked=false;" /></td>
		<td colspan="3" class="style76">Does not access health care</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrAccessHealthCareUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">23. <a name="Num23">Resistant
		to Treatment? </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioResistTreatment"
			value="1" /></td>
		<td width="95%" colspan="3" class="style76">Yes</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioResistTreatment"
			value="2" /></td>
		<td colspan="3" class="style76">No</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioResistTreatment"
			value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">26. <a name="Num26">Current
		Hospitalizations </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<c:forEach var="hospitalization"
		items="${intakeCForm.map.hospitalization}" varStatus="stat">
		<c:if test="${stat.index < formBean.numHospitalization }">
			<tr>
				<td width="17%" class="style76">(<c:out
					value="${stat.index + 1}" />)Date of hospitalization:</td>
				<td width="15%"><html:text styleClass="style71" indexed="true"
					name="hospitalization" property="date" /></td>
				<td width="68%" colspan="2" class="style76">(YYYY/MM/DD)</td>
			</tr>
			<tr>
				<td width="17%" class="style76">Length of hospitalization:</td>
				<td width="15%"><html:text styleClass="style71" indexed="true"
					name="hospitalization" property="length" /></td>
				<td width="68%" colspan="2" class="style76">days</td>
			</tr>
			<tr>
				<td width="11%"><html:checkbox indexed="true"
					name="hospitalization" property="psychiatric" /></td>
				<td colspan="3" class="style76">Psychiatric Hospitalization</td>
			</tr>
			<tr>
				<td width="11%"><html:checkbox indexed="true"
					name="hospitalization" property="physicalHealth" /></td>
				<td colspan="3" class="style76">Physical Health Hospitalization</td>
			</tr>
			<tr>
				<td width="11%"><html:checkbox indexed="true"
					name="hospitalization" property="unknown" /></td>
				<td colspan="3" class="style76">Unknown or Service Recipient
				Declined</td>
			</tr>
		</c:if>
	</c:forEach>
	<tr>
		<td colspan="4" class="style76"><input type="radio" name=""
			onclick="this.form.method.value='add_hospitalization';this.form.submit()">
		Add new hospitalization</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1">
	<tr>
		<td align="center" class="style76">
		<div align="center"><html:submit value="Save"
			onclick="this.form.method.value='saveAndClose';" /> <html:submit
			value="Save Without Closing"
			onclick="this.form.method.value='saveWithoutClose';" /> <html:cancel
			onclick="this.form.method.value='cancel';" /> <input type="button"
			value="Print" onclick="javascript:return onPrint();" /></div>
		</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table>
	<tr>
		<td height="15">&nbsp;</td>
	</tr>
</table>
