
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
<%@ page import="org.oscarehr.PMmodule.model.*"%>
<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>

<%
	DynaValidatorForm form = (DynaValidatorForm) session.getAttribute("intakeCForm");
	
	IntakeCFormBean formBean = (IntakeCFormBean) form.get("view2");
	Formintakec intake = (Formintakec) form.get("intake");
	Provider provider = (Provider) session.getAttribute("provider");
	
	pageContext.setAttribute("formBean", formBean);
%>

<%@page import="org.oscarehr.common.model.Provider"%><script>
	function go_to_top() {
		var form = document.intakeCForm;
		form.method.value='refresh';
		form.submit();
	}
</script>
<html:hidden property="view2.numPastAddresses" />
<html:hidden property="view2.numContacts" />
<html:hidden property="view2.numIdentification" />
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td height="10" align="right" colspan="4">&nbsp; <c:if
			test="${requestScope.demographicNo gt 0 }">
			<a href="javascript:void(0);"
				onclick="window.open('<caisi:CaseManagementLink demographicNo="<%=intake.getDemographicNo().intValue()%>" providerNo="<%=provider.getProviderNo()%>" providerName="<%=provider.getFullName()%>" />', 'casemgmt', 'width=700,height=800')">
			<span class="style126">Case Management Notes</span> </a>
		</c:if></td>
	</tr>
	<tr>
		<td width="13%" class="style76">Client Number:</td>
		<td width="21%" class="style76">&nbsp;<c:out
			value="${requestScope.demographicNo}" /></td>
		<td colspan="2" class="style76">&nbsp;</td>
	</tr>
	<tr>
		<td class="style76">Client First Name:</td>
		<td width="21%" class="style76"><html:text
			property="intake.clientFirstName" styleClass="style71" /></td>
		<td width="14%" class="style76">Client Last Name:</td>
		<td width="52%"><html:text property="intake.clientSurname"
			styleClass="style71" /></td>
	</tr>
	<tr>
		<td class="style76">Staff Name:</td>
		<td width="21%"><html:text styleClass="style71"
			property="intake.staffName" /></td>
		<td colspan="2" class="style76">&nbsp;</td>
	</tr>
	<tr>
	<tr>
		<td class="style76">Admission Date:</td>
		<td width="21%"><html:text styleClass="style71"
			property="intake.admissionDate" /></td>
		<td colspan="2" class="style76">(YYYY/MM/DD)</td>
	</tr>
	<tr>
		<td><html:checkbox property="intake.cboxCaseFile" /></td>
		<td width="21%" class="style76">Case File</td>
		<td colspan="2" class="style76">&nbsp;</td>
	</tr>
	<tr>
		<td><html:checkbox property="intake.cboxPreAdmission" /></td>
		<td width="21%" class="style76">Pre-admission</td>
		<td colspan="2" class="style76">&nbsp;</td>
	</tr>
	<c:if test="${clientId == null or not empty in_community_program}">
		<tr>
			<td colspan="1" align="left" class="style76" nowrap>Admit to Bed
			Program:</td>
			<td colspan="3" align="left" class="style76"><html:select
				property="view2.admissionProgram">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="programDomainBed" property="id"
					labelProperty="name" />
			</html:select></td>
		</tr>
		<tr>
			<td colspan="1" nowrap align="left" class="style76">Admit to
			Service Programs:</td>
			<td colspan="3" nowrap align="left" class="style76"><c:forEach
				var="program" items="${programDomainService}">
				<input type="checkbox" name="admit_service"
					value="<c:out value="${program.id}"/>" />
				<c:out value="${program.name}" />
				<br />
			</c:forEach></td>
		</tr>
	</c:if>
</table>
<table>
	<tr>
		<td colspan="4" height="15">&nbsp;</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="3" class="style51">Client Address</td>
		<td class="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></td>
	</tr>
	<tr>
		<td width="15%" class="style76">Current Address:</td>
		<td width="28%"><html:textarea styleClass="style71"
			property="intake.currAddress" cols="35" rows="5"></html:textarea></td>
		<td class="style76" colspan="2">Current Phone:&nbsp;&nbsp; <html:text
			styleClass="style71" property="intake.currPhone" /></td>
	</tr>
	<c:forEach var="addresses" items="${intakeCForm.map.addresses}"
		varStatus="stat">
		<c:if test="${stat.index < formBean.numPastAddresses }">
			<tr>
				<td class="style76">Past Address <c:out
					value="${stat.index + 1}" />:</td>
				<td><html:textarea styleClass="style71" indexed="true"
					name="addresses" property="info" cols="35" rows="5"></html:textarea></td>
				<td width="29%" class="style76">Start Date:&nbsp; <html:text
					styleClass="style71" indexed="true" name="addresses"
					property="startDate" /></td>
				<td width="28%" class="style76">End Date:&nbsp; <html:text
					styleClass="style71" indexed="true" name="addresses"
					property="endDate" /></td>
			</tr>
		</c:if>
	</c:forEach>
	<c:if test="${formBean.numPastAddresses < 20 }">
		<tr>
			<td colspan="4" class="style76"><input type="radio" name=""
				onclick="this.form.method.value='add_address';this.form.submit()">
			Add new address</td>
		</tr>
	</c:if>
</table>
<table>
	<tr>
		<td colspan="4" height="15">&nbsp;</td>
	</tr>
</table>
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<c:forEach var="contact" items="${intakeCForm.map.contact}"
		varStatus="stat">
		<c:if test="${stat.index < formBean.numContacts }">
			<tr>
				<td colspan="3" valign="top" class="style51">Contact <c:out
					value="${stat.index + 1 }" /> Information</td>
				<td class="right"><a href="javascript:void(0);"
					onclick="go_to_top();"><span class="style126">Top</span></a><br>
				<!-- case management --></td>
			</tr>
			<tr>
				<td width="28%" height="53" class="style76">Name: <html:text
					styleClass="style71" indexed="true" name="contact" property="name" /></td>
				<td class="style76" colspan="3">Relationship with
				cient:&nbsp;&nbsp; <html:text styleClass="style71" indexed="true"
					name="contact" property="relationship" /></td>
			</tr>
			<tr>
				<td width="28%" class="style76">Address: <br>
				<br>
				<html:textarea styleClass="style71" indexed="true" name="contact"
					property="address" cols="35" rows="5"></html:textarea></td>
				<td class="style76" colspan="2">Phone:&nbsp; <html:text
					styleClass="style71" indexed="true" name="contact" property="phone" />
				<br>
				<br>
				Email:&nbsp; <html:text styleClass="style71" indexed="true"
					name="contact" property="email" /> <br>
				<br>
				Fax:&nbsp; <html:text styleClass="style71" indexed="true"
					name="contact" property="fax" /></td>
				<td width="35%" class="style76">Other Info: <br>
				<html:textarea styleClass="style71" indexed="true" name="contact"
					property="otherInfo" cols="35" rows="5"></html:textarea></td>
			</tr>
		</c:if>
	</c:forEach>
	<c:if test="${formBean.numContacts < 20 }">
		<tr>
			<td colspan="4" class="style76"><input type="radio" name=""
				onclick="this.form.method.value='add_contact';this.form.submit()">
			Add new contact</td>
		</tr>
	</c:if>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" cellpadding="1" cellspacing="1">
	<tr>
		<td height="5">&nbsp;</td>
	</tr>
</table>
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<c:forEach var="identification"
		items="${intakeCForm.map.identification}" varStatus="stat">
		<c:if test="${stat.index < formBean.numIdentification }">
			<tr>
				<td width="37%" class="style76">(<c:out
					value="${stat.index + 1}" />) Type of ID:&nbsp;&nbsp; <html:text
					styleClass="style71" indexed="true" name="identification"
					property="type" /></td>
				<td width="63%" colspan="2" class="style76">ID
				Number:&nbsp;&nbsp; <html:text styleClass="style71" indexed="true"
					name="identification" property="number" /></td>
			</tr>
		</c:if>
	</c:forEach>
	<c:if test="${formBean.numIdentification < 20 }">
		<tr>
			<td colspan="4" class="style76"><input type="radio" name=""
				onclick="this.form.method.value='add_identification';this.form.submit()">
			Add new Id</td>
		</tr>
	</c:if>
</table>
<!-- ###################################################################################################### -->
<table width="95%" align="center" cellpadding="1" cellspacing="1">
	<tr>
		<td height="10">&nbsp;</td>
	</tr>
</table>
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td width="10%" class="style76">Entry Date:</td>
		<td width="19%"><html:text styleClass="style71"
			property="intake.entryDate" /></td>
		<td width="71%" colspan="2" class="style76">(YYYY/MM/DD)</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"></td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">1. <a name="Num1">Gender</a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioGender"
			value="1" /></td>
		<td colspan="3" class="style76">Female</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioGender"
			value="2" /></td>
		<td colspan="3" class="style76">Male</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioGender"
			value="3" /></td>
		<td colspan="3" class="style76">Other</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioGender"
			value="4" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td align="left" colspan="4" class="style76">
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case managment --></div>
		<span class="style51">2. <a name="Num2">Date of Birth</a>
		(MM/DD/YYYY)</span><br>
		<br>
		<html:select styleClass="style71" property="intake.monthOfBirth"
			onclick="document.intakeCForm.elements['intake.cboxDateOfBirthUnknown'].checked=false">
			<html:option value="" />
			<%
			for (int x = 1; x < 13; x++) {
			%>
			<html:option value="<%=String.valueOf(x) %>" />
			<%
			}
			%>
		</html:select> <html:select styleClass="style71" property="intake.dayOfBirth">
			<html:option value="" />
			<%
			for (int x = 1; x < 32; x++) {
			%>
			<html:option value="<%=String.valueOf(x) %>" />
			<%
			}
			%>
		</html:select> <html:text styleClass="style71" property="intake.yearOfBirth"
			size="7" maxlength="4"
			onclick="document.intakeCForm.elements['intake.cboxDateOfBirthUnknown'].checked=false" />
		<br>
		<br>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"></td>
	</tr>
	<tr>
		<td width="4%"><html:checkbox
			property="intake.cboxDateOfBirthUnknown"
			onclick="document.intakeCForm.elements['intake.yearOfBirth'].value='0000'" /></td>
		<td width="96%" colspan="3" class="style76">Unknown or Service
		Recipient Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">3. <a name="Num3">Ethno-racial
		Background</a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioIsAboriginal"
			onclick="setEthnicRadio(document.intakeCForm)" value="1" /></td>
		<td width="95%" colspan="3" class="style76">Aboriginal</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioIsAboriginal"
			onclick="setEthnicRadio(document.intakeCForm)" value="2" /></td>
		<td width="95%" colspan="3" class="style76">Non-Aboriginal:</td>
	</tr>
	<tr>
		<td width="5%">&nbsp;</td>
		<td width="95%" colspan="3" class="style76"><html:radio
			property="intake.radioRaceCaucasian"
			onclick="setNonAboriginal(document.intakeCForm)" value="1" />
		Caucasian</td>
	</tr>
	<tr>
		<td width="5%">&nbsp;</td>
		<td width="95%" colspan="3" class="style76"><html:radio
			property="intake.radioRaceCaucasian"
			onclick="setNonAboriginal(document.intakeCForm)" value="2" /> Person
		of Colour</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioIsAboriginal"
			onclick="setEthnicRadio(document.intakeCForm)" value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">4. <a name="Num4">First
		Language English?</a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case management --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioLanguageEnglish"
			value="1" /></td>
		<td width="95%" colspan="3" class="style76">Yes</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioLanguageEnglish"
			value="2" /></td>
		<td width="95%" colspan="3" class="style76">No</td>
	</tr>
	<tr>
		<td width="5%"><html:radio property="intake.radioLanguageEnglish"
			value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="2" valign="top" class="style51">5. <a name="Num5">If
		not English, Service Recipient Preferred Language: </a></td>
		<td width="39%" colspan="2" valign="middle"><html:text
			styleClass="style71" property="intake.preferredLanguage"
			onclick="document.intakeCForm.elements['intake.cboxPreferredLanguageUnknown'].checked=false" />
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!-- case managment --></div>
		</td>
	</tr>
	<tr>
		<td width="5%"><html:checkbox
			property="intake.cboxPreferredLanguageUnknown" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"></td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="4" class="style51">6. <a name="Num6">Country of
		Origin</a>
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!--  case managment --></div>
		</td>
	</tr>
	<tr>
		<td width="2%"><html:radio property="intake.radioCountryOfOrigin"
			value="1" /></td>
		<td colspan="3" class="style76">Canada</td>
	</tr>
	<tr>
		<td width="2%"><html:radio property="intake.radioCountryOfOrigin"
			value="2" /></td>
		<td width="6%" colspan="1" class="style76">Other:</td>
		<td width="92%" colspan="2"><html:text styleClass="style71"
			property="intake.countryOfOrigin"
			onchange="document.intakeCForm.elements['intake.radioCountryOfOrigin'].value='2'" /></td>
	</tr>
	<tr>
		<td width="2%"><html:radio property="intake.radioCountryOfOrigin"
			value="3" /></td>
		<td colspan="3" class="style76">Unknown or Service Recipient
		Declined</td>
	</tr>
</table>
<!-- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% -->
<table width="95%" align="center" border="1" cellpadding="1"
	cellspacing="1">
	<tr>
		<td colspan="2" valign="top" class="style51">7. <a name="Num7">If
		not Canada, year client arrived in Canada: </a></td>
		<td width="39%" colspan="2" class="style76"><html:text
			styleClass="style71" property="intake.yearArrivedInCanada" /> (YYYY)
		<div align="right"><a href="javascript:void(0);"
			onclick="go_to_top();"><span class="style126">Top</span></a><br>
		<!--  case management --></div>
		</td>
	</tr>
	<tr>
		<td colspan="4" class="style76"></td>
	</tr>
	<tr>
		<td width="3%"><html:radio property="intake.radioCanadianBorn"
			value="1" /></td>
		<td colspan="3" class="style76">N/A (Canadian-born)</td>
	</tr>
	<tr>
		<td width="3%"><html:radio property="intake.radioCanadianBorn"
			value="2" /></td>
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
