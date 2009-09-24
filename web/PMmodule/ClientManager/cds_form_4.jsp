<%@page import="org.oscarehr.common.model.CdsClientForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	

	CdsClientForm cdsClientForm=CdsForm4.getCdsClientForm(currentDemographicId);
%>

<form action="cds_form_4_action.jsp">
	<h3>CDS form (CDS-MH v4.05)</h3>

	<br />

	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td class="genericTableData">
				<select name="admissionId">
					<%
						for (Admission admission : CdsForm4.getAdmissions(currentDemographicId))
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
			<td class="genericTableHeader">8. Gender</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "gender", CdsForm4.getCdsFormOptions("008"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">9. Age</td>
			<td class="genericTableData">
				<input type="text" name="age" value="<%=cdsClientForm.getClientAge()%>" />
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10. Service Recipient Location</td>
			<td class="genericTableData">
				<select name="serviceRecipientLocation">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "serviceRecipientLocation", CdsForm4.getCdsFormOptions("010"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10a. Service Recipient LHIN</td>
			<td class="genericTableData">
				<select name="serviceRecipientLhin">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "serviceRecipientLhin", CdsForm4.getCdsFormOptions("10a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">11. Aboriginal Origin</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "isAboriginal", CdsForm4.getCdsFormOptions("011"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">12. Service Recipient Preferred Language</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "preferredLanguage", CdsForm4.getCdsFormOptions("012"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">13. Baseline Legal status</td>
			<td class="genericTableData">
				<select multiple="multiple" name="baselineLegalStatus" style="height:8em">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineLegalStatus", CdsForm4.getCdsFormOptions("013"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">14. Current Legal status</td>
			<td class="genericTableData">
				<select multiple="multiple" name="currentLegalStatus" style="height:8em">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentLegalStatus", CdsForm4.getCdsFormOptions("014"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">15. Community Treatment Orders (CTOs)</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions(cdsClientForm.getId(), "hasCto", CdsForm4.getCdsFormOptions("015"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">16. Diagnostic Categories</td>
			<td class="genericTableData">
				<select name="diagnosticCategories">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "diagnosticCategories", CdsForm4.getCdsFormOptions("016"))%>
				</select>
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
				<select multiple="multiple" name="presentingIssues" style="height:8em">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "presentingIssues", CdsForm4.getCdsFormOptions("017"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">18. Source of Referral</td>
			<td class="genericTableData">
				<select name="sourceOfReferral">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "sourceOfReferral", CdsForm4.getCdsFormOptions("018"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">19. Exit Disposition</td>
			<td class="genericTableData">
				<select name="exitDisposition">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "exitDisposition", CdsForm4.getCdsFormOptions("019"))%>
				</select>
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
					<tr>
						<td>Total number of episodes</td>
						<td>
							<select name="baselineTotalNumberOfEpisodes">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "baselineTotalNumberOfEpisodes", 999)%>
							</select>
						</td>
					</tr>
					<tr>
						<td>Total number of hospitalised days</td>
						<td>
							<select name="baselineTotalNumberOfHospitalisedDays">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "baselineTotalNumberOfHospitalisedDays", 999)%>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">21. Current Psychiatric Hospitalizations</td>
			<td class="genericTableData">
				<table>
					<tr>
						<td>Total number of episodes</td>
						<td>
							<select name="currentTotalNumberOfEpisodes">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "currentTotalNumberOfEpisodes", 999)%>
							</select>
						</td>
					</tr>
					<tr>
						<td>Total number of episodes</td>
						<td>
							<select name="currentTotalNumberOfEpisodes">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "currentTotalNumberOfEpisodes", 999)%>
							</select>
						</td>
					</tr>
					<tr>
						<td>Total number of hospitalised days</td>
						<td>
							<select name="currentTotalNumberOfHospitalisedDays">
								<%=CdsForm4.renderNumbersAsSelectOptions(cdsClientForm.getId(), "currentTotalNumberOfHospitalisedDays", 999)%>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">22. Baseline Living Arrangement</td>
			<td class="genericTableData">
				<select name="baselineLivingArrangement">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineLivingArrangement", CdsForm4.getCdsFormOptions("022"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">23. Current Living Arrangement</td>
			<td class="genericTableData">
				<select name="currentLivingArrangement">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentLivingArrangement", CdsForm4.getCdsFormOptions("023"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24. Baseline Residence Type</td>
			<td class="genericTableData">
				<select name="baselineResidenceType">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineResidenceType", CdsForm4.getCdsFormOptions("024"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24a. Baseline Level of Residential Support</td>
			<td class="genericTableData">
				<select name="baselineResidentialSupport">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineResidentialSupport", CdsForm4.getCdsFormOptions("24a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25. Current Residence Type</td>
			<td class="genericTableData">
				<select name="currentResidenceType">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentResidenceType", CdsForm4.getCdsFormOptions("025"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25a. Current Level of Residential Support</td>
			<td class="genericTableData">
				<select name="currentResidentialSupport">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentResidentialSupport", CdsForm4.getCdsFormOptions("25a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">26. Baseline Employment Status</td>
			<td class="genericTableData">
				<select name="baselineEmploymentStatus">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineEmploymentStatus", CdsForm4.getCdsFormOptions("026"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">27.Current Employment Status</td>
			<td class="genericTableData">
				<select name="currentEmploymentStatus">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentEmploymentStatus", CdsForm4.getCdsFormOptions("027"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">28. Baseline Educational Status</td>
			<td class="genericTableData">
				<select name="baselineEducationStatus">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselineEducationStatus", CdsForm4.getCdsFormOptions("028"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29. Current Educational Status</td>
			<td class="genericTableData">
				<select name="currentEducationStatus">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentEducationStatus", CdsForm4.getCdsFormOptions("029"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29a. Highest Level of Education</td>
			<td class="genericTableData">
				<select name="highestLevelEducation">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "highestLevelEducation", CdsForm4.getCdsFormOptions("29a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">30. Baseline Primary Income Source</td>
			<td class="genericTableData">
				<select name="baselinePrimaryIncome">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "baselinePrimaryIncome", CdsForm4.getCdsFormOptions("030"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">31. Current Primary Income Source</td>
			<td class="genericTableData">
				<select name="currentPrimaryIncome">
					<%=CdsForm4.renderAsSelectOptions(cdsClientForm.getId(), "currentPrimaryIncome", CdsForm4.getCdsFormOptions("031"))%>
				</select>
			</td>
		</tr>
		<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" value="<%=currentDemographicId%>" />
				Sign <input type="checkbox" name="signed" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" value="Save" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="Cancel" onclick="history.go(-1)" />
			</td>
		</tr>
	</table>

</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
