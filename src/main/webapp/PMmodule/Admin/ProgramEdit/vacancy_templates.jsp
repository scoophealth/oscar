
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


<%@page import="org.oscarehr.PMmodule.model.VacancyTemplate"%>
<%@page import="org.oscarehr.PMmodule.model.Criteria"%>
<%@page import="org.oscarehr.PMmodule.service.VacancyTemplateManager"%>
<%@page import="org.oscarehr.PMmodule.model.Program"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.List"%>
<%@ include file="/taglibs.jsp"%>

<%
// is only populated if it's an existing form, i.e. new one off existing form
	

	// must be populated some how
	int currentDemographicId=0;
	
	// must be populated some how
	VacancyTemplate template = null;
	String templateId = (String) request.getAttribute("vacancyTemplateId");
	if (!StringUtils.isBlank(templateId) && !templateId.equalsIgnoreCase("null"))	{				
		template=VacancyTemplateManager.getVacancyTemplateByTemplateId(Integer.parseInt(templateId));		
	}	else	{	
		template= new VacancyTemplate();
	}
%>
<script type="text/javascript">
	function save() {
		document.programManagerForm.method.value='save_vacancy_template';
		document.programManagerForm.submit()
	}
    $(document).ready(
        function () {
            $('#addGender').click(
                function (e) {
                    $('#sourceOfGender > option:selected').appendTo('#targetOfGender');
                    e.preventDefault();
                });
            $('#removeGender').click(
                function (e) {
                    $('#targetOfGender > option:selected').appendTo('#sourceOfGender');
                    e.preventDefault();
                });

            $('#addDiagnosis').click(
                    function (e) {
                        $('#sourceOfDiagnosis > option:selected').appendTo('#targetOfDiagnosis');
                        e.preventDefault();
                    });
            $('#removeDiagnosis').click(
                    function (e) {
                        $('#targetOfDiagnosis > option:selected').appendTo('#sourceOfDiagnosis');
                        e.preventDefault();
                    });
 
            $('#addReferralType').click(
                    function (e) {
                        $('#sourceOfReferralType > option:selected').appendTo('#targetOfReferralType');
                        e.preventDefault();
                    });
            $('#removeReferralType').click(
                    function (e) {
                        $('#targetOfReferralType > option:selected').appendTo('#sourceOfReferralType');
                        e.preventDefault();
                    });

            $('#addSupportType').click(
                    function (e) {
                        $('#sourceOfSupportType > option:selected').appendTo('#targetOfSupportType');
                        e.preventDefault();
                    });
            $('#removeSupportType').click(
                    function (e) {
                        $('#targetOfSupportType > option:selected').appendTo('#sourceOfSupportType');
                        e.preventDefault();
                    });

            $('#addLegalStatus').click(
                    function (e) {
                        $('#sourceOfLegalStatus > option:selected').appendTo('#targetOfLegalStatus');
                        e.preventDefault();
                    });
            $('#removeLegalStatus').click(
                    function (e) {
                        $('#targetOfLegalStatus > option:selected').appendTo('#sourceOfLegalStatus');
                        e.preventDefault();
                    });
        });
</script>

<div class="tabs" id="tabs">
<input type="hidden" name="vacancyTemplateId" id="vacancyTemplateId" value="<%=template.getId()%>" />
<input type="hidden" name="programId" id="programId" value="<%=request.getAttribute("id")%>" />
	<table cellpadding="3" cellspacing="0" border="0">
		<tr>
			<th title="Programs" class="nofocus"><a
				onclick="javascript:clickTab2('General', 'General');return false;"
				href="javascript:void(0)">General Information</a></th>
			<th title="Templates">Vacancy Templates</th>
		</tr>
	</table>
</div>

<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr class="b">
		<td width="30%" class="beright">Template is active:</td>
		<td><input type="checkbox" value="on" <%=template.getActive()==true?"checked":"" %>
			name="templateActive"></td>
	</tr>
	<tr class="b">
		<td class="beright">Template Name:</td>
		<td><input type="text" size="50" maxlength="50" value="<%=template.getName()==null?"":template.getName() %>"
			name="templateName"></td>
	</tr>
	<tr class="b">
		<td class="beright">Associated Program:</td>
		<td><select name="associatedProgramId">
		<% 
			List<Program> programs = VacancyTemplateManager.getPrograms(LoggedInInfo.loggedInInfo.get().currentFacility.getId());
			for(Program p : programs) {
				String selectedOrNot = "";
				Integer programIdFromTemplate = template.getProgramId();
				if(programIdFromTemplate !=null && programIdFromTemplate.intValue()==p.getId().intValue())
					selectedOrNot = "selected";
		%>				
			<option value="<%=p.getId()%>" <%=selectedOrNot%> ><%=p.getName() %></option>
		<%} %>
		</select></td>
	</tr>
</table>
<%
			Criteria criteria = VacancyTemplateManager.getSelectedCriteria(template.getId(), "Age");
			String ageMin = "", ageMax="",ageRequired="";
			if(criteria !=null) {
				ageMin = String.valueOf(criteria.getRangeStartValue());
				ageMax = String.valueOf(criteria.getRangeEndValue());
				ageRequired = (criteria.getCanBeAdhoc()==true?"selected":"");
			}
%>
<fieldset>
	<legend>Criteria Required For this Template</legend>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires specific age range:</td>
			<td><input type="checkbox" value="on" <%=ageRequired %>
				name="ageRangeRequired"></td>
		</tr>
		
		<tr class="b">
			<td class="beright">Age Range Minimum:</td>
			<td><input type="text" size="50" maxlength="50" <%=ageMin%>
				name="ageMinimum"></td>
		</tr>
		<tr class="b">
			<td class="beright">Age Range Maximum:</td>
			<td><input type="text" size="50" maxlength="50" <%=ageMax %>
				name="ageMaximum"></td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Gender:</td>
			<td><input type="checkbox" value="on"
				name="genderRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Gender List</div>
					<div>
						<select id="sourceOfGender" name="sourceOfGender" multiple="multiple" size="7"
							style="width: 200px;">							
							<%=VacancyTemplateManager.renderAsSelectOptions("Gender") %>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addGender" name="addGender" value=">>">
					</div>
					<div>
						<input type="button" id="removeGender" name="removeGender" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Gender</div>
					<div>
						<select id="targetOfGender" name="targetOfGender" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAnswersAsSelectOptions(template.getId(), "Gender") %>
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Diagnosis:</td>
			<td><input type="checkbox" value="on"
				name="program.diagnosisRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Diagnosis List</div>
					<div>
						<select id="sourceOfDiagnosis" name="sourceOfDiagnosis" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAsSelectOptions("Mental health diagnosis") %>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addDiagnosis" name="addDiagnosis" value=">>">
					</div>
					<div>
						<input type="button" id="removeDiagnosis" name="removeDiagnosis" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Diagnosis</div>
					<div>
						<select id="targetOfDiagnosis" name="targetOfDiagnosis" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAnswersAsSelectOptions(template.getId(), "Mental health diagnosis") %>
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Referral Type:</td>
			<td><input type="checkbox" value="on"
				name="program.referralTypeRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Referral Type List</div>
					<div>
						<select id="sourceOfReferralType" name="sourceOfReferralType" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAsSelectOptions("Referral source") %>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addReferralType" name="addReferralType" value=">>">
					</div>
					<div>
						<input type="button" id="removeReferralType" name="ReferralType" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Referral Type</div>
					<div>
						<select id="targetOfReferralType" name="targetOfReferralType" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAnswersAsSelectOptions(template.getId(), "Referral source") %>
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Support Type:</td>
			<td><input type="checkbox" value="on"
				name="program.supportTypeRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Support Type List</div>
					<div>
						<select id="sourceOfSupportType" name="sourceOfSupportType" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAsSelectOptions("Support level") %>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addSupportType" name="addSupportType" value=">>">
					</div>
					<div>
						<input type="button" id="removeSupportType" name="removeSupportType" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Support Type</div>
					<div>
						<select id="targetOfSupportType" name="targetOfSupportType" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAnswersAsSelectOptions(template.getId(), "Support level") %>
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%" border="1" cellspacing="2" cellpadding="3">
		<tr class="b">
			<td width="30%" class="beright">Requires Specific Legal Status:</td>
			<td><input type="checkbox" value="on"
				name="program.legalStatusRequired"></td>
		</tr>
		<tr class="b">
			<td colspan="2" style="padding-left: 10%;">
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Legal Status List</div>
					<div>
						<select id="sourceOfLegalStatus" name="sourceOfLegalStatus" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAsSelectOptions("Legal History") %>
						</select>
					</div>
				</div>
				<div class="horizonton" style="padding-top: 40px;">
					<div>
						<input type="button" id="addLegalStatus" name="addLegalStatus" value=">>">
					</div>
					<div>
						<input type="button" id="removeLegalStatus" name="removeLegalStatus" value="<<">
					</div>
				</div>
				<div class="horizonton">
					<div style="margin-bottom: 3px;">Required Legal Status</div>
					<div>
						<select id="targetOfLegalStatus" name="targetOfLegalStatus" multiple="multiple" size="7"
							style="width: 200px;">
							<%=VacancyTemplateManager.renderAnswersAsSelectOptions(template.getId(), "Legal History") %>
						</select>
					</div>
				</div>
			</td>
		</tr>
	</table>
</fieldset>
<table width="100%" border="1" cellspacing="2" cellpadding="3">
	<tr>
		 <td colspan="2"><input type="button" value="Save" onclick="return save()" /> <html:cancel /></td>
	</tr>
</table>

