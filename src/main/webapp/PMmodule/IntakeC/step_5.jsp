
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
		Collection Form -- Step5: Social and Living Arrangements
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
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">21. <a name="Num21">Baseline
		Social Support/Isolation </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasFriends"
			onclick="document.intakeCForm.elements['intake.cboxBaseHasSupportUnknown'].checked=false" /></td>
		<td width="95%" colspan="3" class="style76">Has friend(s) to get
		support, advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasRelatives"
			onclick="document.intakeCForm.elements['intake.cboxBaseHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has relative(s) to get support,
		advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasCommunity"
			onclick="document.intakeCForm.elements['intake.cboxBaseHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has community/social worker to
		get support, advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasSomeone"
			onclick="document.intakeCForm.elements['intake.cboxBaseHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has someone else to get support,
		advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseHasSupportUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">22. <a name="Num22">Current
		Social Support/Isolation </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasFriends"
			onclick="document.intakeCForm.elements['intake.cboxCurrHasSupportUnknown'].checked=false" /></td>
		<td width="95%" colspan="3" class="style76">Has friend(s) to get
		support, advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasRelatives"
			onclick="document.intakeCForm.elements['intake.cboxCurrHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has relative(s) to get support,
		advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasCommunity"
			onclick="document.intakeCForm.elements['intake.cboxCurrHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has community/social worker to
		get support, advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasSomeone"
			onclick="document.intakeCForm.elements['intake.cboxCurrHasSupportUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Has someone else to get support,
		advice, etc. from</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrHasSupportUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">27. <a name="Num27">Baseline
		Living Arrangement </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithChildren"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td width="95%" colspan="3" class="style76">Children</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithNonrelatives"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Non-relatives</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithParents"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Parents</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithRelatives"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Relatives</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithSelf"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Self</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithSpouse"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Spouse/partner</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithSpousePlus"
			onclick="document.intakeCForm.elements['intake.cboxBaseLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Spouse/partner & others</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxBaseLivingWithUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">28. <a name="Num28">Baseline
		Primary Residence Type (Check ONE only) </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="1" /></td>
		<td width="96%" colspan="3" class="style76">Correctional/Probationary
		Facility</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="2" /></td>
		<td colspan="3" class="style76">General Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="3" /></td>
		<td colspan="3" class="style76">Psychiatric Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="4" /></td>
		<td colspan="3" class="style76">Other Specialty Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="5" /></td>
		<td colspan="3" class="style76">Homeless</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="6" /></td>
		<td colspan="3" class="style76">Hostel/Shelter</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="7" /></td>
		<td colspan="3" class="style76">Long-Term Care Facility/Nursing
		Home</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="8" /></td>
		<td colspan="3" class="style76">Municipal Non-Profit Housing</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="9" /></td>
		<td colspan="3" class="style76">Private Non-Profit Housing</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="10" /></td>
		<td colspan="3" class="style76">Own House/Apartment [Private
		House/Condo (Service Recipient)]</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="11" /></td>
		<td colspan="3" class="style76">Friend or Relative's
		House/Apartment [Private House/Condo (Other)]</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="12" /></td>
		<td colspan="3" class="style76">Retirement Home/Senior's
		Residence</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="13" /></td>
		<td colspan="3" class="style76">Rooming/Boarding House</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="14" /></td>
		<td colspan="3" class="style76">Supportive Housing - Congregate
		Living (RTF, 24 hr Home and Group Homes)</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="15" /></td>
		<td colspan="3" class="style76">Supportive Housing - Assisted
		Living</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="16" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBasePrimaryResidenceType" value="17" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Description of Housing (e.g.
		housing issues, concerns, etc.):</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"><html:textarea
			property="intake.baseDescriptionOfHousing" cols="70"></html:textarea></td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">29. <a name="Num29">Baseline
		Residence Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBaseResidenceStatus" value="1" /></td>
		<td width="96%" colspan="3" class="style76">Independent</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBaseResidenceStatus" value="2" /></td>
		<td colspan="3" class="style76">Assisted/Supported</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBaseResidenceStatus" value="3" /></td>
		<td colspan="3" class="style76">Supervised Facility (e.g.
		hospital, prison, halfway house)</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioBaseResidenceStatus" value="4" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">30. <a name="Num30">Current
		Living Arrangement </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithChildren"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td width="95%" colspan="3" class="style76">Children</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithNonrelatives"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Non-relatives</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithParents"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Parents</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithRelatives"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Relatives</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithSelf"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Self</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithSpouse"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Spouse/partner</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithSpousePlus"
			onclick="document.intakeCForm.elements['intake.cboxCurrLivingWithUnknown'].checked=false" /></td>
		<td colspan="3" class="style76">Spouse/partner & others</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxCurrLivingWithUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">31. <a name="Num31">Current
		Residence Type </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="1" /></td>
		<td width="96%" colspan="3" class="style76">Correctional/Probationary
		Facility</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="2" /></td>
		<td colspan="3" class="style76">General Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="3" /></td>
		<td colspan="3" class="style76">Psychiatric Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="4" /></td>
		<td colspan="3" class="style76">Other Specialty Hospital</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="5" /></td>
		<td colspan="3" class="style76">Homeless</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="6" /></td>
		<td colspan="3" class="style76">Hostel/Shelter</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="7" /></td>
		<td colspan="3" class="style76">Long-Term Care Facility/Nursing
		Home</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="8" /></td>
		<td colspan="3" class="style76">Municipal Non-Profit Housing</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="9" /></td>
		<td colspan="3" class="style76">Private Non-Profit Housing</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="10" /></td>
		<td colspan="3" class="style76">Own House/Apartment [Private
		House/Condo (Service Recipient)]</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="11" /></td>
		<td colspan="3" class="style76">Friend or Relative's
		House/Apartment [Private House/Condo (Other)]</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="12" /></td>
		<td colspan="3" class="style76">Retirement Home/Senior's
		Residence</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="13" /></td>
		<td colspan="3" class="style76">Rooming/Boarding House</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="14" /></td>
		<td colspan="3" class="style76">Supportive Housing - Congregate
		Living (RTF, 24 hr Home and Group Homes)</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="15" /></td>
		<td colspan="3" class="style76">Supportive Housing - Assisted
		Living</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="16" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrPrimaryResidenceType" value="17" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76">Description of Housing (e.g.
		housing issues, concerns, etc.):</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"><html:textarea
			property="intake.currDescriptionOfHousing" cols="70"></html:textarea></td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">32. <a name="Num32">Current
		Residence Status </a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrResidenceStatus" value="1" /></td>
		<td width="96%" colspan="3" class="style76">Independent</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrResidenceStatus" value="2" /></td>
		<td colspan="3" class="style76">Assisted/Supported</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrResidenceStatus" value="3" /></td>
		<td colspan="3" class="style76">Supervised Facility (e.g.
		hospital, prison, halfway house)</td>
	</tr>
	<tr>
		<td width="4%"><html:radio
			property="intake.radioCurrResidenceStatus" value="4" /></td>
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
