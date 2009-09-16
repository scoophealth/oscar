<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	

	CdsForm4 cdsForm4=null;
	if ("NEW".equals(request.getParameter("action"))) cdsForm4=CdsForm4.makeNewCds(currentDemographicId);
%>

<form action="cds_form_4_action.jsp">
	<h3>CDS form (CDS-MH v4.05)</h3>

	<br />

	<table style="border:solid black 1px;margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td class="genericTableData">
				<select name="admissionId">
					<%
						for (Admission admission : CdsForm4.getAdmissions(currentDemographicId))
						{
							%>
								<option value="<%=admission.getId()%>"><%=CdsForm4.getEscapedAdmissionSelectionDisplay(admission)%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">8. Gender</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions("gender", CdsForm4.getCdsFormOptions("008"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">9. Age</td>
			<td class="genericTableData">
				<input type="text" readonly="readonly" name="age" value="<%=cdsForm4.getCdsData().getClientAge()%>" />
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10. Service Recipient Location</td>
			<td class="genericTableData">
				<select name="serviceRecipientLocation">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("010"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10a. Service Recipient LHIN</td>
			<td class="genericTableData">
				<select name="serviceRecipientLhin">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("10a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">11. Aboriginal Origin</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions("isAboriginal", CdsForm4.getCdsFormOptions("011"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">12. Service Recipient Preferred Language</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions("preferredLanguage", CdsForm4.getCdsFormOptions("012"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">13/14. Legal status</td>
			<td class="genericTableData">
				<select multiple="multiple" name="legalStatus" style="height:8em">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("013"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">15. Community Treatment Orders (CTOs)</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsRadioOptions("hasCto", CdsForm4.getCdsFormOptions("015"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">16. Diagnostic Categories</td>
			<td class="genericTableData">
				<select name="diagnosticCategories">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("016"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">16a. Other Illness Information</td>
			<td class="genericTableData">
				<%=CdsForm4.renderAsCheckBoxOptions("otherIllness", CdsForm4.getCdsFormOptions("16a"))%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">17. Presenting Issues (to be) Addressed</td>
			<td class="genericTableData">
				<select multiple="multiple" name="presentingIssues" style="height:8em">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("017"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">18. Source of Referral</td>
			<td class="genericTableData">
				<select name="sourceOfReferral">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("018"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">19. Exit Disposition</td>
			<td class="genericTableData">
				<select name="exitDisposition">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("019"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">20. Baseline Psychiatric Hospitalizations</td>
			<td class="genericTableData">
				UMMMM I'll look at this later
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">21. Current Psychiatric Hospitalizations</td>
			<td class="genericTableData">
				UMMMM I'll look at this later
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">22. Baseline Living Arrangement</td>
			<td class="genericTableData">
				<select name="baselineLivingArrangement">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("022"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">23. Current Living Arrangement</td>
			<td class="genericTableData">
				<select name="currentLivingArrangement">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("023"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24. Baseline Residence Type</td>
			<td class="genericTableData">
				<select name="baselineResidenceType">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("024"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">24a. Baseline Level of Residential Support</td>
			<td class="genericTableData">
				<select name="baselineResidentialSupport">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("24a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25. Current Residence Type</td>
			<td class="genericTableData">
				<select name="currentResidenceType">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("025"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">25a. Current Level of Residential Support</td>
			<td class="genericTableData">
				<select name="currentResidentialSupport">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("25a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">26. Baseline Employment Status</td>
			<td class="genericTableData">
				<select name="baselineEmploymentStatus">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("026"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">27.Current Employment Status</td>
			<td class="genericTableData">
				<select name="currentEmploymentStatus">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("027"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">28. Baseline Educational Status</td>
			<td class="genericTableData">
				<select name="baselineEducationStatus">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("028"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29. Current Educational Status</td>
			<td class="genericTableData">
				<select name="currentEducationStatus">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("029"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">29a. Highest Level of Education</td>
			<td class="genericTableData">
				<select name="highestLevelEducation">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("29a"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">30. Baseline Primary Income Source</td>
			<td class="genericTableData">
				<select name="baselinePrimaryIncome">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("030"))%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">31. Current Primary Income Source</td>
			<td class="genericTableData">
				<select name="currentPrimaryIncome">
					<%=CdsForm4.renderAsSelectOptions(CdsForm4.getCdsFormOptions("031"))%>
				</select>
			</td>
		</tr>
	</table>

	<br />

	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<input type="submit" value="Save Temporarily with out signing" />
	&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="submit" value="Save and sign" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
