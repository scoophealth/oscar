
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
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style63">INTAKE C. Mental Health Team Data
		Collection Form -- Step6: Employment, Education and Income Status
		&nbsp;&nbsp;&nbsp;</td>
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
		<td colspan="4" class="style51">33. <a name="Num33">Baseline
		Employment Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Independent/Competitive</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="2" /></td>
		<td colspan="3" class="style76">Assisted/Supportive</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="3" /></td>
		<td colspan="3" class="style76">Alternative Businesses (e.g.
		A-Way, Fresh Start)</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="4" /></td>
		<td colspan="3" class="style76">Sheltered Workshop</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="5" /></td>
		<td colspan="3" class="style76">Non-Paid Work Experience</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="6" /></td>
		<td colspan="3" class="style76">Casual/Sporadic</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="7" /></td>
		<td colspan="3" class="style76">In School [No employment - Other
		Activity]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="8" /></td>
		<td colspan="3" class="style76">Retired/on disability [No
		employment - Other Activity]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="9" /></td>
		<td colspan="3" class="style76">No Employment [No employment of
		any kind]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseEmploymentStatus" value="10" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">34. <a name="Num34">Current
		Employment Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Independent/Competitive</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="2" /></td>
		<td colspan="3" class="style76">Assisted/Supportive</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="3" /></td>
		<td colspan="3" class="style76">Alternative Businesses (e.g.
		A-Way, Fresh Start)</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="4" /></td>
		<td colspan="3" class="style76">Sheltered Workshop</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="5" /></td>
		<td colspan="3" class="style76">Non-Paid Work Experience</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="6" /></td>
		<td colspan="3" class="style76">Casual/Sporadic</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="7" /></td>
		<td colspan="3" class="style76">In School [No employment - Other
		Activity]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="8" /></td>
		<td colspan="3" class="style76">Retired/on disability [No
		employment - Other Activity]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="9" /></td>
		<td colspan="3" class="style76">No Employment [No employment of
		any kind]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrEmploymentStatus" value="10" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">35. <a name="Num35">Baseline
		Educational Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Highest Level of Education at
		Intake</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="1" /></td>
		<td width="95%" colspan="3" class="style76">No Formal Schooling</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="2" /></td>
		<td colspan="3" class="style76">Elementary/Junior High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="3" /></td>
		<td colspan="3" class="style76">Secondary/High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="4" /></td>
		<td colspan="3" class="style76">Trade School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="5" /></td>
		<td colspan="3" class="style76">Vocational/Training Centre</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="6" /></td>
		<td colspan="3" class="style76">Adult Education</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="7" /></td>
		<td colspan="3" class="style76">Community College</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="8" /></td>
		<td colspan="3" class="style76">University</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="9" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseHighestEductionLevel" value="10" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Educational Status (participating
		in education at intake)</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Not in school [No
		formal schooling/not in school]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="2" /></td>
		<td width="95%" colspan="3" class="style76">Elementary/Junior
		High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="3" /></td>
		<td width="95%" colspan="3" class="style76">Secondary/High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="4" /></td>
		<td width="95%" colspan="3" class="style76">Trade School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="5" /></td>
		<td width="95%" colspan="3" class="style76">Vocational/Training
		Centre</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="6" /></td>
		<td width="95%" colspan="3" class="style76">Adult Education</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="7" /></td>
		<td width="95%" colspan="3" class="style76">Community College</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="8" /></td>
		<td width="95%" colspan="3" class="style76">University</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="9" /></td>
		<td width="95%" colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioBaseParticipateInEduction" value="10" /></td>
		<td width="95%" colspan="3" class="style76">Unknown or Service
		Recipient Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">36. <a name="Num36">Current
		Educational Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Highest Level of Education
		Currently</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="1" /></td>
		<td width="95%" colspan="3" class="style76">No Formal Schooling</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="2" /></td>
		<td colspan="3" class="style76">Elementary/Junior High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="3" /></td>
		<td colspan="3" class="style76">Secondary/High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="4" /></td>
		<td colspan="3" class="style76">Trade School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="5" /></td>
		<td colspan="3" class="style76">Vocational/Training Centre</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="6" /></td>
		<td colspan="3" class="style76">Adult Education</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="7" /></td>
		<td colspan="3" class="style76">Community College</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="8" /></td>
		<td colspan="3" class="style76">University</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="9" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrHighestEductionLevel" value="10" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Educational Status (participating
		in education currently)</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Not in school [No
		formal schooling/not in school]</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="2" /></td>
		<td width="95%" colspan="3" class="style76">Elementary/Junior
		High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="3" /></td>
		<td width="95%" colspan="3" class="style76">Secondary/High School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="4" /></td>
		<td width="95%" colspan="3" class="style76">Trade School</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="5" /></td>
		<td width="95%" colspan="3" class="style76">Vocational/Training
		Centre</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="6" /></td>
		<td width="95%" colspan="3" class="style76">Adult Education</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="7" /></td>
		<td width="95%" colspan="3" class="style76">Community College</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="8" /></td>
		<td width="95%" colspan="3" class="style76">University</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="9" /></td>
		<td width="95%" colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="5%"><html:radio
			property="intake.radioCurrParticipateInEduction" value="10" /></td>
		<td width="95%" colspan="3" class="style76">Unknown or Service
		Recipient Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">37. <a name="Num37">Baseline
		Primary Income Source (Check ONE only) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="1"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="4" class="style76">Employment</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="2"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Employment Insurance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="3"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Pension</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="4"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">ODSP</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="5"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Social Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="6"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Disability Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="7"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Family</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="8"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">No Source of Income</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="9" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:radio
			property="intake.radioBasePrimaryIncomeSourceOther"
			onclick="checkPrimaryIncomeOther(this,document.intakeCForm)"
			value="1" /></td>
		<td width="91%" colspan="2" class="style76">Panhandling</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="5%"><html:radio
			property="intake.radioBasePrimaryIncomeSourceOther"
			onclick="checkPrimaryIncomeOther(this,document.intakeCForm)"
			value="2" /></td>
		<td colspan="2" class="style76">Informal economy</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryIncomeSource" value="10"
			onclick="unClickPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">38. <a name="Num38">Baseline
		Secondary Income Source (Check ALL that apply) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeEmployment"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="4" class="style76">Employment</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeEi"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Employment Insurance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomePension"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Pension</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeODSp"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">ODSP</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeSocialAssistance"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Social Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeDisabilityAssistance"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Disability Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeFamily"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Family</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeNone"
			onclick="document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">No Source of Income</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeOther" styleId="38_PARENT"
			onclick="unCheck2ndIncomeOther(this,document.intakeCForm);document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomePanhandlingOther"
			styleId="38_0_CHILD"
			onclick="check2ndIncomeOther(this,document.intakeCForm);document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td width="92%" colspan="2" class="style76">Panhandling</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeInformalOther" styleId="38_1_CHILD"
			onclick="check2ndIncomeOther(this,document.intakeCForm);document.intakeCForm.elements['intake.cboxBase2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="2" class="style76">Informal economy</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBase2ndIncomeUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">39. <a name="Num39">Current
		Primary Income Source (Check ONE only) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="1"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="4" class="style76">Employment</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="2"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Employment Insurance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="3"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Pension</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="4"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">ODSP</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="5"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Social Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="6"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Disability Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="7"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Family</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="8"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">No Source of Income</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="9" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSourceOther" value="1"
			onclick="clickCurrPrimaryIncomeOther(this,document.intakeCForm)" /></td>
		<td width="92%" colspan="2" class="style76">Panhandling</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSourceOther" value="2"
			onclick="clickCurrPrimaryIncomeOther(this,document.intakeCForm)" /></td>
		<td colspan="2" class="style76">Informal economy</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryIncomeSource" value="10"
			onclick="unclickCurrPrimaryIncomeChildren(document.intakeCForm)" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">40. <a name="Num40">Current
		Secondary Income Source (Check ALL that apply) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeEmployment"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="4" class="style76">Employment</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox property="intake.cbox2ndIncomeEi"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Employment Insurance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomePension"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Pension</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox property="intake.cbox2ndIncomeODSp"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">ODSP</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeSocialAssistance"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Social Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeDisabilityAssistance"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Disability Assistance</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeFamily"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Family</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox property="intake.cbox2ndIncomeNone"
			onclick="document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">No Source of Income</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeOther"
			onclick="uncheck2ndIncomeOtherChildren(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomePanhandlingOther" styleId="40_0_CHILD"
			onclick="checkCurrent2ndIncomeOther(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td width="92%" colspan="2" class="style76">Panhandling</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeInformalOther" styleId="40_1_CHILD"
			onclick="checkCurrent2ndIncomeOther(this,document.intakeCForm);document.intakeCForm.elements['intake.cbox2ndIncomeUnknown'].checked=false" /></td>
		<td colspan="2" class="style76">Informal economy</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cbox2ndIncomeUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">41. <a name="Num41">Baseline
		Income Management </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBaseIncomeMgmentHasTrustee"
			onclick="document.intakeCForm.elements['intake.cboxBaseIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Trustee</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBaseIncomeMgmentNeedsTrustee"
			onclick="document.intakeCForm.elements['intake.cboxBaseIncomeMgmentUnknown'].checked=false" /></td>
		<td width="96%" colspan="3" class="style76">Needs trustee but
		does not have one</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBaseIncomeMgmentNeedsTrusteeButDoNotWant"
			onclick="document.intakeCForm.elements['intake.cboxBaseIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Needs trustee but does not want
		one</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBaseIncomeMgmentDoNotNeedTrustee"
			onclick="document.intakeCForm.elements['intake.cboxBaseIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Does not need trustee</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxBaseIncomeMgmentUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">42. <a name="Num42">Current
		Income Management </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxCurrIncomeMgmentHasTrustee"
			onclick="document.intakeCForm.elements['intake.cboxCurrIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Trustee</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxCurrIncomeMgmentNeedsTrustee"
			onclick="document.intakeCForm.elements['intake.cboxCurrIncomeMgmentUnknown'].checked=false" /></td>
		<td width="96%" colspan="3" class="style76">Needs trustee but
		does not have one</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxCurrIncomeMgmentNeedsTrusteeButDoNotWant"
			onclick="document.intakeCForm.elements['intake.cboxCurrIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Needs trustee but does not want
		one</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxCurrIncomeMgmentDoNotNeedTrustee"
			onclick="document.intakeCForm.elements['intake.cboxCurrIncomeMgmentUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Does not need trustee</td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxCurrIncomeMgmentUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
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
