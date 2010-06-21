<%
	request.setAttribute("HEAD_ELEMENT", "");
%>

<%@include file="/layouts/html_top.jspf"%>


<%@page import="oscar.oscarLab.ca.all.pageUtil.SendOruR01UIBean"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.Gender"%><h2 class="oscarBlueHeader">
	Send Unsolicited Observation Message (ORU_R01)
</h2>

<form method="post" enctype="multipart/form-data" action="oruR01Upload.do">
	<table style="border-collapse:collapse; width:95%; font-size:12px">
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader" style="width:10em">From Provider</td>
			<td><%=SendOruR01UIBean.getLoggedInProviderDisplayLine()%></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">To Provider / Specialist</td>
			<td>
				<select name="professionalSpecialistId">
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
			<td class="oscarBlueHeader" style="vertical-align:top">For Client</td>
			<td>
				<table style="border-collapse:collapse">
					<tr>
						<td style="font-weight:bold;text-align:right">First Name</td>
						<td><input type="text" name="clientFirstName" value="" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right">Last Name</td>
						<td><input type="text" name="clientLastName" value="" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right">Health Number<br />(excluding version code)</td>
						<td><input type="text" name="clientHealthNumber" value="" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right;vertical-align:top">BirthDay</td>
						<td>
							<input type="text" id="clientBirthDay" name="clientBirthDay" />
							<script type="text/javascript">
								jQuery(document).ready(function() {
									Date.format='yy-mm-dd';
									jQuery("#clientBirthDay").datepicker({dateFormat: 'yy-mm-dd'});
								});
							</script>
						</td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right">Gender</td>
						<td>
							<select name="clientGender">
								<%
									for (Gender gender : Gender.values())
									{
										%>
											<option value="<%=gender.name()%>"><%=gender.getText()%></option>
										<%
									}
								%>
							</select>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Data Name</td>
			<td><input type="text" name="dataName" value="" /></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Text Data</td>
			<td><textarea name="textData" style="width:40em;height:8em" ></textarea></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Binary Data</td>
			<td><input type="file" name="uploadFile" /></td>
		</tr>
	</table>
	<br />
	<input type="submit" value="Electronically Send Data" />
	
</form>

<%@include file="/layouts/html_bottom.jspf"%>