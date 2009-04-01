<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.PMmodule.web.ManageHnrClient"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility currentFacility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider currentProvider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	ManageHnrClient manageHnrClient=new ManageHnrClient(currentFacility, currentProvider, currentDemographicId);
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
		<td style="border-top:solid black 1px"><%=StringUtils.trimToEmpty(demographic.getFirstName())%></td>
		<td rowspan="8" style="vertical-align:middle;text-align:center;border-top:solid black 1px;border-right:solid black 1px"><input type="button" value="<%=manageHnrClient.getHcInfoValidationActionString()%>" <%=manageHnrClient.isHcInfoValidatable()?"":"disabled=\"disabled\""%> onclick="document.location='manage_hnr_client_action.jsp?action=<%=manageHnrClient.getHcInfoValidationActionString()%>HcInfo&demographicId=<%=currentDemographicId%>'" /></td>
		<td style="border-top:solid black 1px"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getFirstName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Last Name</td>
		<td><%=StringUtils.trimToEmpty(demographic.getLastName())%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getLastName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Gender</td>
		<td><%=StringUtils.trimToEmpty(demographic.getSex())%></td>
		<td><%=manageHnrClient.getRemoteGender()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Birth Date</td>
		<td><%=demographic.getFormattedDob()%></td>
		<td><%=manageHnrClient.getRemoteFormatedBirthDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Number</td>
		<td><%=StringUtils.trimToEmpty(demographic.getHin())%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHin())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Type</td>
		<td><%=StringUtils.trimToEmpty(demographic.getHcType())%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHinType())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Valid Start</td>
		<td><%=manageHnrClient.getLocalFormatedHinStartDate()%></td>
		<td><%=manageHnrClient.getRemoteFormatedHinStartDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Valid End</td>
		<td><%=manageHnrClient.getLocalFormatedHinEndDate()%></td>
		<td><%=manageHnrClient.getRemoteFormatedHinEndDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Street Address</td>
		<td style="border-top:solid black 1px"><%=StringUtils.trimToEmpty(demographic.getAddress())%></td>
		<td rowspan="3" style="vertical-align:middle;text-align:center;border-top:solid black 1px;border-right:solid black 1px"><input type="button" value="<%=manageHnrClient.getOtherInfoValidationActionString()%>" <%=manageHnrClient.isOtherInfoValidatable()?"":"disabled=\"disabled\""%> onclick="document.location='manage_hnr_client_action.jsp?action=<%=manageHnrClient.getOtherInfoValidationActionString()%>OtherInfo&demographicId=<%=currentDemographicId%>'" /></td>
		<td style="border-top:solid black 1px"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getStreetAddress())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">City</td>
		<td><%=StringUtils.trimToEmpty(demographic.getCity())%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getCity())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Province/State/Territory</td>
		<td><%=StringUtils.trimToEmpty(demographic.getProvince())%></td>
		<td><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getProvince())%></td>
	</tr>
</table>

<br />

<input type="button" value="Send validated local data To HNR" onclick="document.location='manage_hnr_client_action.jsp?action=copyLocalValidatedToHnr&demographicId=<%=currentDemographicId%>'" /><input type="button" value="Copy HNR data to local" <%=hnrClient==null?"disabled=\"disabled\"":""%> onclick="document.location='manage_hnr_client_action.jsp?action=copyHnrToLocal&demographicId=<%=currentDemographicId%>'" /><input type="button" value="Back to client summary" onclick="document.location='<%="../ClientManager.do?id="+currentDemographicId%>'" />


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
