<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.oscarehr.cds.CdsLhin"%>
<%@page import="org.oscarehr.cds.CdsLegalStatus"%>
<%@page import="org.oscarehr.cds.CdsMunicipality"%>

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
			<td class="genericTableHeader">Type of form (baseline, or current status)</td>
			<td class="genericTableData">
				<input type="radio" name="formType" value="BASELINE" /> Baseline
				<br />
				<input type="radio" name="formType" value="STATUS" /> Current Status
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">8. Client's gender</td>
			<td class="genericTableData"><%=cdsForm4.getCdsData().getClientGender().name()%></td>
		</tr>
		<tr>
			<td class="genericTableHeader">9. Client's age</td>
			<td class="genericTableData"><%=cdsForm4.getCdsData().getClientAge()%></td>
		</tr>
		<tr>
			<td class="genericTableHeader">10. Client's home district</td>
			<td class="genericTableData">
				<select name="clientHomeDistrict">
					<%
						for (CdsMunicipality cdsMunicipality : CdsMunicipality.valuesSorted())
						{
							%>
								<option value="<%=cdsMunicipality.name()%>"><%=cdsMunicipality.name()%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">10a. Client's home LHIN</td>
			<td class="genericTableData">
				<select name="clientHomeLhin">
					<%
						for (CdsLhin cdsLhin : CdsLhin.valuesSorted())
						{
							%>
								<option value="<%=cdsLhin.name()%>"><%=cdsLhin.name()%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">11. Is client of aboriginal origins</td>
			<td class="genericTableData">
				<input type="radio" name="isAboriginal" value="TRUE" /> Yes
				<br />
				<input type="radio" name="isAboriginal" value="FALSE" /> No
				<br />
				<input type="radio" name="isAboriginal" value="UNKNOWN" /> Unknown or declined to answer
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">12. Client's preferred language</td>
			<td class="genericTableData">
				<input type="radio" name="clientLanguage" value="EN" /> English
				<br />
				<input type="radio" name="clientLanguage" value="FR" /> French
				<br />
				<input type="radio" name="clientLanguage" value="OTHER" /> Other
				<br />
				<input type="radio" name="clientLanguage" value="UNKNOWN" /> Unknown or declined to answer
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">13/14. Client's legal status</td>
			<td class="genericTableData">
				<select multiple="multiple" name="clientLegalStatus" style="height:6em">
					<%
						for (CdsLegalStatus cdsLegalStatus : CdsLegalStatus.valuesSorted())
						{
							%>
								<option value="<%=cdsLegalStatus.name()%>"><%=cdsLegalStatus.name()%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">15. Client's main mental disorder</td>
			<td class="genericTableData">
				<input type="radio" name="hasCto" value="TRUE" /> Issued CTO
				<br />
				<input type="radio" name="hasCto" value="FALSE" /> No CTO
				<br />
				<input type="radio" name="hasCto" value="UNKNOWN" /> Unknown or declined to answer
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's presenting problems</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's source of referral</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's discharge reason</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's living arrangements</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's residence type & support</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's employment status</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's education status</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's primary income</td>
			<td class="genericTableData">
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
