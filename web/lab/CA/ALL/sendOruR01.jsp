<%@page import="oscar.oscarLab.ca.all.pageUtil.SendOruR01UIBean"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.Gender"%>

<%@include file="/layouts/html_top.jspf"%>


<h2 class="oscarBlueHeader">
	Send Data Electronically 
	<span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message)</span>
</h2>


<script type="text/javascript">
	function checkRequiredFields()
	{
		if (jQuery("#clientFirstName").val().length==0)
		{
			alert('The clients first name is required.');
			return(false);
		}

		if (jQuery("#dataName").val().length==0)
		{
			alert('The data name is required.');
			return(false);
		}

		if (jQuery("#textData").val().length==0 && jQuery("#uploadFile").val().length==0)
		{
			alert('Either Text Data or an Upload File is required.');
			return(false);
		}

		return(true);
	}
</script>

<form method="post" enctype="multipart/form-data" action="oruR01Upload.do" onsubmit="return checkRequiredFields()">
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
						for (ProfessionalSpecialist professionalSpecialist : SendOruR01UIBean.getRemoteCapableProfessionalSpecialists())
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
						<td><input type="text" id="clientFirstName" name="clientFirstName" value="" /></td>
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
			<td><input type="text" id="dataName" name="dataName" value="" /></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Text Data</td>
			<td><textarea id="textData" name="textData" style="width:40em;height:8em" ></textarea></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Upload File</td>
			<td><input type="file" id="uploadFile" name="uploadFile" /></td>
		</tr>
	</table>
	<br />
	<input type="submit" value="Electronically Send Data" />
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="close" onclick='window.close()' />
	
</form>

<%@include file="/layouts/html_bottom.jspf"%>