<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	

	CdsForm4 cdsForm4=null;
	if ("NEW".equals(request.getParameter("action"))) cdsForm4=CdsForm4.makeNewCds(currentDemographicId);
		
%>


<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%><form action="cds_form_4_action.jsp">
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
			<td class="genericTableHeader">Client's age</td>
			<td class="genericTableData"><%=cdsForm4.getCdsData().getClientAge()%></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's gender</td>
			<td class="genericTableData"><%=cdsForm4.getCdsData().getClientGender().name()%></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's home district</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is client of aboriginal origins</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's prefered language</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's legal status</td>
			<td class="genericTableData">
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's main mental disorder</td>
			<td class="genericTableData">
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
