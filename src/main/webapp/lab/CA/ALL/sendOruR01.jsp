<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="oscar.oscarLab.ca.all.pageUtil.SendOruR01UIBean"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.Gender"%>

<%@include file="/layouts/html_top.jspf"%>

<%--
This jsp accepts parameters with the same name as
the fields in the SendOruR01UIBean. All parameters are optional
for pre-populating data.
--%>
<%
	SendOruR01UIBean sendOruR01UIBean=new SendOruR01UIBean(request);
%>

<%@page import="org.apache.commons.lang.StringUtils"%><h2 class="oscarBlueHeader">
	Send eData 
	<span style="font-size:9px">(ORU_R01 : Unsolicited Observation Message)</span>
</h2>


<script type="text/javascript">
	function checkRequiredFields()
	{
		if (jQuery("#professionalSpecialistId").val().length==0)
		{
			alert('Select a provider / specialist to send to.');
			return(false);
		}
		
		if (jQuery("#clientFirstName").val().length==0 || jQuery("#clientLastName").val().length==0)
		{
			alert('The clients first and last name is required.');
			return(false);
		}

		if (jQuery("#subject").val().length==0)
		{
			alert('The data name is required.');
			return(false);
		}

		if (jQuery("#textMessage").val().length==0 && jQuery("#uploadFile").val().length==0)
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
				<select name="professionalSpecialistId" id="professionalSpecialistId">
					<option value="">--- none selected ---</option>
					<%
						for (ProfessionalSpecialist professionalSpecialist : SendOruR01UIBean.getRemoteCapableProfessionalSpecialists())
						{
							%>
								<option value="<%=professionalSpecialist.getId()%>" <%=sendOruR01UIBean.renderSelectedProfessionalSpecialistOption(professionalSpecialist.getId())%> ><%=SendOruR01UIBean.getProfessionalSpecialistDisplayString(professionalSpecialist)%></option>
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
						<td><input type="text" id="clientFirstName" name="clientFirstName" value="<%=sendOruR01UIBean.getClientFirstName()%>" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right">Last Name</td>
						<td><input type="text" id="clientLastName" name="clientLastName" value="<%=sendOruR01UIBean.getClientLastName()%>" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right">Health Number<br />(excluding version code)</td>
						<td><input type="text" name="clientHealthNumber" value="<%=sendOruR01UIBean.getClientHin()%>" /></td>
					</tr>
					<tr>
						<td style="font-weight:bold;text-align:right;vertical-align:top">BirthDay</td>
						<td>
							<input type="text" id="clientBirthDay" name="clientBirthDay" value="<%=sendOruR01UIBean.getClientBirthDate()%>" />
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
								<option value="">--- none selected ---</option>
								<%
									for (Gender gender : Gender.values())
									{
										%>
											<option value="<%=gender.name()%>" <%=sendOruR01UIBean.renderSelectedGenderOption(gender)%> ><%=gender.getText()%></option>
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
			<td class="oscarBlueHeader">Subject</td>
			<td><input type="text" id="subject" name="subject" value="<%=sendOruR01UIBean.getSubject()%>" /></td>
		</tr>
		<tr style="border:solid grey 1px">
			<td class="oscarBlueHeader">Text Message</td>
			<td><textarea id="textMessage" name="textMessage" style="width:40em;height:8em" ><%=sendOruR01UIBean.getTextMessage()%></textarea></td>
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
