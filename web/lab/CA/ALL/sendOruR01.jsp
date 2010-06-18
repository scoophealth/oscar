<%@include file="/layouts/html_top.jspf"%>


<%@page import="oscar.oscarLab.ca.all.pageUtil.SendOruR01UIBean"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%><h2 class="oscarBlueHeader">
	Send Unsolicited Observation Message (ORU_R01)
</h2>

<table style="border-collapse:collapse; width:95%; font-size:12px">
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader" style="width:10em">From Provider</td>
		<td><%=SendOruR01UIBean.getLoggedInProviderDisplayLine()%></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">To Provider / Specialist</td>
		<td>
			<select>
				<%
					for (ProfessionalSpecialist professionalSpecialist : SendOruR01UIBean.getAllProfessionalSpecialists())
					{
						%>
							<option value="<%=professionalSpecialist.getId()%>"><%=SendOruR01UIBean.getProfessionalSpecialistDisplayString(professionalSpecialist)%></option>
						<%
					}
				%>
			</select>
		</td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">For Client</td>
		<td>
			Name 
			<br />
			Health Number
			<br />
			BirthDay
			<br />
			Gender			
		</td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Data Name</td>
		<td></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Text Data</td>
		<td></td>
	</tr>
	<tr style="border:solid grey 1px">
		<td class="oscarBlueHeader">Binary Data</td>
		<td></td>
	</tr>
</table>

<%@include file="/layouts/html_bottom.jspf"%>