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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.PMmodule.web.ManageHnrClient"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.HinValidator"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	
	ManageHnrClient manageHnrClient=new ManageHnrClient(loggedInInfo, currentDemographicId);
	Demographic demographic=manageHnrClient.getDemographic();
	org.oscarehr.hnr.ws.Client hnrClient=manageHnrClient.getHnrClient();
%>

<h3>Manage Linked Clients</h3>
<br />
<table class="genericTable">
	<tr>
		<td class="genericTableHeader"></td>
		<td class="genericTableHeader">Local Information</td>
		<td class="genericTableHeader">Local Validation</td>
		<td class="genericTableHeader">Health Number Registry Information</td>
	</tr>
	<tr>
		<td class="genericTableHeader">Picture</td>
		<td><img style="height:96px; width:96px" src="<%=request.getContextPath()+manageHnrClient.getLocalClientImageUrl()%>" alt="client_image_<%=demographic.getDemographicNo()%>" /></td>
		<td style="vertical-align:middle;text-align:center;border-right:solid black 1px"><input type="button" value="<%=manageHnrClient.getPictureValidationActionString()%>" <%=manageHnrClient.isImageValidateable()?"":"disabled=\"disabled\""%> onclick="document.location='manage_hnr_client_action.jsp?action=<%=manageHnrClient.getPictureValidationActionString()%>Picture&demographicId=<%=currentDemographicId%>'" /></td>
		<td><img style="height:96px; width:96px" src="<%=request.getContextPath()+manageHnrClient.getHnrClientImageUrl()%>" alt="client_image_<%=demographic.getDemographicNo()%>" /></td>
	</tr>
	<tr>
		<td class="genericTableHeader">First Name</td>
		<td style="border-top:solid black 1px"><%=StringUtils.trimToEmpty(demographic.getFirstName())%><%=(StringUtils.trimToNull(demographic.getFirstName())==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td rowspan="8" style="vertical-align:middle;text-align:center;border-top:solid black 1px;border-right:solid black 1px">
			<script type="text/javascript">
				function validateHcInfo()
				{
					if (confirm('Have you called the Ministry of Health to validate this client\'s Health Card Number? If yes click "ok" if not select "cancel" and call the Ministry of Health before validating this Client\'s Health Card Number on the CAISI system.'))
					{
						document.location='manage_hnr_client_action.jsp?action=<%=manageHnrClient.getHcInfoValidationActionString()%>HcInfo&demographicId=<%=currentDemographicId%>';
						return(true);
					}
					else
					{
						return(false);
					}
				}
			</script>
			<input type="button" value="<%=manageHnrClient.getHcInfoValidationActionString()%>" <%=manageHnrClient.isHcInfoValidatable()?"":"disabled=\"disabled\""%> onclick="validateHcInfo()" />
		</td>
		<td style="border-top:solid black 1px"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getFirstName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Last Name</td>
		<td><%=StringUtils.trimToEmpty(demographic.getLastName())%><%=(demographic.getLastName()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getLastName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Gender</td>
		<td><%=StringUtils.trimToEmpty(demographic.getSex())%><%=(demographic.getSex()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=manageHnrClient.getRemoteGender()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Birth Date</td>
		<td><%=demographic.getFormattedDob()%><%=(demographic.getBirthDay()==null||demographic.getFormattedDob()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=manageHnrClient.getRemoteFormatedBirthDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Number</td>
		<td><%=StringUtils.trimToEmpty(demographic.getHin())%>&nbsp;<%=StringUtils.trimToEmpty(demographic.getVer())%><%=(demographic.getHin()==null||!HinValidator.isValid(demographic.getHin(),demographic.getHcType())?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHin())+' '+StringUtils.trimToEmpty(hnrClient.getHinVersion())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Type</td>
		<td><%=StringUtils.trimToEmpty(demographic.getHcType())%><%=(demographic.getHcType()==null||!HinValidator.isValid(demographic.getHin(),demographic.getHcType())?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHinType())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card EFF Date</td>
		<td><%=manageHnrClient.getLocalFormatedHinStartDate()%><%=(manageHnrClient.getLocalFormatedHinStartDate()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=manageHnrClient.getRemoteFormatedHinStartDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Renewal Date</td>
		<td><%=manageHnrClient.getLocalFormatedHinEndDate()%><%=(manageHnrClient.getLocalFormatedHinEndDate()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=manageHnrClient.getRemoteFormatedHinEndDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Street Address</td>
		<td style="border-top:solid black 1px"><%=StringUtils.trimToEmpty(demographic.getAddress())%><%=(demographic.getAddress()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td rowspan="3" style="vertical-align:middle;text-align:center;border-top:solid black 1px;border-right:solid black 1px"><input type="button" value="<%=manageHnrClient.getOtherInfoValidationActionString()%>" <%=manageHnrClient.isOtherInfoValidatable()?"":"disabled=\"disabled\""%> onclick="document.location='manage_hnr_client_action.jsp?action=<%=manageHnrClient.getOtherInfoValidationActionString()%>OtherInfo&demographicId=<%=currentDemographicId%>'" /></td>
		<td style="border-top:solid black 1px"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getStreetAddress())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">City</td>
		<td><%=StringUtils.trimToEmpty(demographic.getCity())%><%=(demographic.getCity()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getCity())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Province/State/Territory</td>
		<td><%=StringUtils.trimToEmpty(demographic.getProvince())%><%=(demographic.getProvince()==null?"<span style=\"color:red\">*</span>":"")%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getProvince())%></td>
	</tr>
</table>

<br />

<input type="button" value="Send validated local data To HNR" onclick="document.location='manage_hnr_client_action.jsp?action=copyLocalValidatedToHnr&demographicId=<%=currentDemographicId%>'" <%=manageHnrClient.canSendToHnr()?"":"disabled=\"disabled\""%> /><input type="button" value="Copy HNR data to local" <%=hnrClient==null?"disabled=\"disabled\"":""%> onclick="document.location='manage_hnr_client_action.jsp?action=copyHnrToLocal&demographicId=<%=currentDemographicId%>'" /><input type="button" value="Back to client summary" onclick="document.location='../ClientManager.do?id=<%=currentDemographicId%>'" />


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
