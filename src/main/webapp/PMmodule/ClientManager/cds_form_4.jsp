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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.common.model.CdsClientForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
   		 
	// is only populated if it's an existing form, i.e. new one off existing form
	Integer cdsFormId=null;

	// must be populated some how
	int currentDemographicId=0;
	
	// must be populated some how
	CdsClientForm cdsClientForm=null;
	
	if (request.getParameter("cdsFormId")!=null)
	{
		cdsFormId=Integer.parseInt(request.getParameter("cdsFormId"));
		cdsClientForm=CdsForm4.getCdsClientFormByCdsFormId(cdsFormId);
		currentDemographicId=cdsClientForm.getClientId();
	}
	else
	{
		currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
		cdsClientForm=CdsForm4.getCdsClientFormByClientId(loggedInInfo.getCurrentFacility().getId(), currentDemographicId);
	}
	
	boolean printOnly=request.getParameter("print")!=null;
	if (printOnly) request.setAttribute("noMenus", true);
%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

Client name : <%=CdsForm4.getEscapedClientName(currentDemographicId)%> 
<br />
Client date of birth : <%=CdsForm4.getFormattedClientBirthDay(currentDemographicId)%>
<br />
<br />
CDS form (CDS-MH v4.05)
<br />

<form action="cds_form_4_action.jsp" name="form">
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td class="genericTableData">
				<select name="admissionId">
					<%
						for (Admission admission : CdsForm4.getAdmissions(loggedInInfo.getCurrentFacility().getId(), currentDemographicId))
						{
							String selected="";
							
							if (cdsClientForm.getAdmissionId()!=null && cdsClientForm.getAdmissionId().intValue()==admission.getId().intValue()) selected="selected=\"selected\"";
							
							%>
								<option <%=selected%> value="<%=admission.getId()%>"><%=CdsForm4.getEscapedAdmissionSelectionDisplay(admission)%></option>
							<%
						}
					%>
				</select>			
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">7a. Waiting list / assessment</td>
			<td class="genericTableData">
				<table>
					<tr>
						<td>Date of initial contact (waiting list)</td>
						<td><input type="text" name="initialContactDate" id="initialContactDate" value="<%=CdsForm4.getDateAsISOString(cdsClientForm.getInitialContactDate())%>" size="10"> <img src="<%=request.getContextPath()%>/images/cal.gif" id="initialContactDate_cal"></td>
					</tr>
					<tr>
						<td>Date of assessment / interview</td>
						<td><input type="text" name="assessmentDate" id="assessmentDate" value="<%=CdsForm4.getDateAsISOString(cdsClientForm.getAssessmentDate())%>" size="10"> <img src="<%=request.getContextPath()%>/images/cal.gif" id="assessmentDate_cal"></td>
					</tr>
				</table>
				
				<script type="text/javascript">
					Calendar.setup({ inputField : "initialContactDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "initialContactDate_cal", singleClick : true, step : 1 });
					Calendar.setup({ inputField : "assessmentDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "assessmentDate_cal", singleClick : true, step : 1 });
				</script>			
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">8. Gender</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "gender", CdsForm4.getCdsFormOptions("008"), CdsForm4.getClientGenderAsCdsOption(currentDemographicId))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10. Service Recipient Location</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "serviceRecipientLocation", CdsForm4.getCdsFormOptions("010"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10a. Service Recipient LHIN</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "serviceRecipientLhin", CdsForm4.getCdsFormOptions("10a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">11. Aboriginal Origin</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "isAboriginal", CdsForm4.getCdsFormOptions("011"), "011-03")%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">12. Service Recipient Preferred Language</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "preferredLanguage", CdsForm4.getCdsFormOptions("012"), "012-04")%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">13. Baseline Legal status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(true, false, printOnly, cdsClientForm.getId(), "baselineLegalStatus", CdsForm4.getCdsFormOptions("013"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">14. Current Legal status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(true, false, printOnly, cdsClientForm.getId(), "currentLegalStatus", CdsForm4.getCdsFormOptions("014"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">15. Community Treatment Orders (CTOs)</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "hasCto", CdsForm4.getCdsFormOptions("015"), "015-03")%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">16. Diagnostic Categories</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "diagnosticCategories", CdsForm4.getCdsFormOptions("016"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">16a. Other Illness Information</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsCheckBoxOptions(cdsClientForm.getId(), "otherIllness", CdsForm4.getCdsFormOptions("16a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">17. Presenting Issues (to be) Addressed</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(true, false, printOnly, cdsClientForm.getId(), "presentingIssues", CdsForm4.getCdsFormOptions("017"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">18. Source of Referral</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "sourceOfReferral", CdsForm4.getCdsFormOptions("018"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">19. Exit Disposition</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "exitDisposition", CdsForm4.getCdsFormOptions("019"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">20. Baseline Psychiatric Hospitalizations</td>
			<td class="genericTableData">
				<table>
					<tr>
						<td>Age of first psychiatric hospitalization</td>
						<td>
							<select name="ageOfFirstPsychiatricHospitalization">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "ageOfFirstPsychiatricHospitalization", 120)%>
							</select>
						</td>
					</tr>
					<tr>
						<td>Age at the onset of mental illness</td>
						<td>
							<select name="ageOfOnsetOfMentalIllness">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "ageOfOnsetOfMentalIllness", 120)%>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">
				20./21. Psychiatric Hospitalizations
				<div style="font-weight:normal;font-size:smaller">
					This shows all historical hospitalisations, the correct ones for the report will be automatically calculated for base line (20) and current (21).
					<br /><br />
					To update a hospitalisation date, create a new entry then delete the old one.
				</div>
			</td>
			<td class="genericTableData">
				<table>
					<tr>
						<td>
							<span id="hospitalisedDaysList">
								Hospitalised Days List
							</span>
							
							<script type="text/javascript">
								function deleteHospitalisationDay(hospitalisationId)
								{
									var ajaxArgs =
									{
										method:'post',
										parameters: {hospitalisationId : hospitalisationId},
										onSuccess: updateHospitalisedListDisplay,
										onFailure: function(transport)
										{
											alert('Error deleting hospital day : '+transport) 
										}
									}
						        
									new Ajax.Request('cds_form_4_delete_hospitalisations.jsp', ajaxArgs);
								}
												
								function updateHospitalisedListDisplay()
								{
									var ajaxArgs =
									{
										method:'get',
										parameters: {clientId : <%=currentDemographicId%>, preventCache: (new Date()).getMilliseconds()},
										onFailure: function(transport)
										{
											alert('Error retrieving hospital days : '+transport) 
										}
									}
						        
									new Ajax.Updater('hospitalisedDaysList', 'cds_form_4_current_hospitalisations.jsp', ajaxArgs);
								}
			
								updateHospitalisedListDisplay();
							</script>
						</td>
						<td>
							Refused To Answer : <input type="checkbox" name="refused21" <%="on".equals(CdsForm4.getSingleAnswer(cdsClientForm.getId(), "refused21"))?"checked=\"checked\"":""%> />
							<br /><br />
							<table style="border-collapse:collapse;border:solid black 1px">
								<tr>
									<td class="genericTableHeader" style="border:solid black 1px;text-align:center" colspan="2">Add</td>
								</tr>
								<tr>
									<td>Admission Date</td>
									<td><input type="text" name="hospitalAdmission" id="hospitalAdmission" value="" size="10"> <img src="<%=request.getContextPath()%>/images/cal.gif" id="hospitalAdmission_cal"></td>
								</tr>
								<tr>
									<td>Discharge Date</td>
									<td><input type="text" name="hospitalDischarge" id="hospitalDischarge" value="" size="10"> <img src="<%=request.getContextPath()%>/images/cal.gif" id="hospitalDischarge_cal"></td>
								</tr>
								<tr>
									<td></td>
									<td><input type="button" value="Add" onclick="addHospitalDays()" /></td>
								</tr>
							</table>
							
							<script type="text/javascript">
								Calendar.setup({ inputField : "hospitalAdmission", ifFormat : "%Y-%m-%d", showsTime :false, button : "hospitalAdmission_cal", singleClick : true, step : 1 });
								Calendar.setup({ inputField : "hospitalDischarge", ifFormat : "%Y-%m-%d", showsTime :false, button : "hospitalDischarge_cal", singleClick : true, step : 1 });

								function addHospitalDays()
								{
									var ajaxArgs =
									{
										method:'post',
										parameters: {clientId : <%=currentDemographicId%>, hospitalAdmission: $(hospitalAdmission).value, hospitalDischarge : $(hospitalDischarge).value },
										asynchronous : false,
										onSuccess: updateHospitalisedListDisplay,
										onFailure: function(transport)
										{
											alert('Error updating hospital days : '+transport) 
										}
									}
						        
									new Ajax.Request('cds_form_4_add_hospitalisations.jsp', ajaxArgs);
								}
							</script>				
						</td>
					</tr>
				</table>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">22. Baseline Living Arrangement</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselineLivingArrangement", CdsForm4.getCdsFormOptions("022"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">23. Current Living Arrangement</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentLivingArrangement", CdsForm4.getCdsFormOptions("023"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24. Baseline Residence Type</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselineResidenceType", CdsForm4.getCdsFormOptions("024"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24a. Baseline Level of Residential Support</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselineResidentialSupport", CdsForm4.getCdsFormOptions("24a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25. Current Residence Type</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentResidenceType", CdsForm4.getCdsFormOptions("025"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25a. Current Level of Residential Support</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentResidentialSupport", CdsForm4.getCdsFormOptions("25a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">26. Baseline Employment Status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselineEmploymentStatus", CdsForm4.getCdsFormOptions("026"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">27.Current Employment Status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentEmploymentStatus", CdsForm4.getCdsFormOptions("027"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">28. Baseline Educational Status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselineEducationStatus", CdsForm4.getCdsFormOptions("028"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29. Current Educational Status</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentEducationStatus", CdsForm4.getCdsFormOptions("029"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29a. Highest Level of Education</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "highestLevelEducation", CdsForm4.getCdsFormOptions("29a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">30. Baseline Primary Income Source</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "baselinePrimaryIncome", CdsForm4.getCdsFormOptions("030"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">31. Current Primary Income Source</td>
			<td class="genericTableData">
				<%=CdsForm4.renderSelectQuestion(false, true, printOnly, cdsClientForm.getId(), "currentPrimaryIncome", CdsForm4.getCdsFormOptions("031"))%>
			</td>
		</tr>
		<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" value="<%=currentDemographicId%>" />
				Sign <input type="checkbox" name="signed" <%=printOnly&&cdsClientForm.isSigned()?"checked=\"checked\"":""%>/>

				<%
					if (!printOnly)
					{
						%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="submit" value="Save CDS Data" />
						<%
					}
				%>

				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="cancel" value="Cancel" onclick="history.go(-1)" />

				<%
					if (printOnly)
					{
						%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" name="print" value="Print" onclick="window.print()">
						<%
					}
				%>
			</td>
		</tr>
	</table>
	
	<%
		if (printOnly)
		{
			%>
				<script>
					setEnabledAll(document.form, false);

					document.getElementsByName('cancel')[0].disabled=false;
					document.getElementsByName('print')[0].disabled=false;
				</script>
			<%
		}
	%>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
