<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
%>

<form action="cds_form_4_action.jsp">
	<h3>CDS form (CDS-MH v4.05)</h3>

	<br />

	<table style="border: solid black 1px; width:90%; margin-left:auto;margin-right:auto; background-color:#f0f0f0">
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Type of form (baseline, or current status)</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's home district</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is client of aboriginal origins</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's prefered language</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's legal status</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's main mental disorder</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's presenting problems</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's source of referral</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's discharge reason</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's living arrangements</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's residence type & support</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's employment status</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's education status</td>
			<td>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Client's primary income</td>
			<td>
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
