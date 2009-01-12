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
	org.oscarehr.hnr.ws.client.Client hnrClient=manageHnrClient.getHnrClient();
%>


<h3>Manage Linked Clients</h3>
<br />
<table class="genericTable">
	<tr>
		<td class="genericTableHeader"></td>
		<td class="genericTableHeader">Local Information</td>
		<td class="genericTableHeader">Health Number Registry Information</td>
	</tr>
	<tr>
		<td class="genericTableHeader">Picture</td>
		<td class="genericTableData"><img style="height:96px; width:96px" src="<%=request.getContextPath()+manageHnrClient.getLocalClientImageUrl()%>" alt="client_image_<%=demographic.getDemographicNo()%>" /></td>
		<td class="genericTableData"><img style="height:96px; width:96px" src="<%=request.getContextPath()+manageHnrClient.getHnrClientImageUrl()%>" alt="client_image_<%=demographic.getDemographicNo()%>" /></td>
	</tr>
	<tr>
		<td class="genericTableHeader">First Name</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getFirstName())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getFirstName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Last Name</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getLastName())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getLastName())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Birth Date</td>
		<td class="genericTableData"><%=demographic.getFormattedDob()%></td>
		<td class="genericTableData"><%=manageHnrClient.getRemoteFormatedBirthDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Number</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getHin())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHin())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Type</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getHcType())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHinType())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Version</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getVer())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getHinVersion())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Valid Start</td>
		<td class="genericTableData"><%=manageHnrClient.getLocalFormatedHinStartDate()%></td>
		<td class="genericTableData"><%=manageHnrClient.getRemoteFormatedHinStartDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Health Card Valid End</td>
		<td class="genericTableData"><%=manageHnrClient.getLocalFormatedHinEndDate()%></td>
		<td class="genericTableData"><%=manageHnrClient.getRemoteFormatedHinEndDate()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Province/State/Territory</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getProvince())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getProvince())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">City</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getCity())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getCity())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Street Address</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getAddress())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getStreetAddress())%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">Gender</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getSex())%></td>
		<td class="genericTableData"><%=manageHnrClient.getRemoteGender()%></td>
	</tr>
	<tr>
		<td class="genericTableHeader">SIN</td>
		<td class="genericTableData"><%=StringUtils.trimToEmpty(demographic.getSin())%></td>
		<td class="genericTableData"><%=hnrClient==null?"":StringUtils.trimToEmpty(hnrClient.getSin())%></td>
	</tr>
</table>

<br />

<input type="button" value="Send Local Data To HNR" onclick="document.location='manage_hnr_client_action.jsp?action=copyLocalToHnr&demographicId=<%=currentDemographicId%>'" /><input type="button" value="Copy HNR Data To Local" <%=hnrClient==null?"disabled=\"disabled\"":""%> onclick="document.location='manage_hnr_client_action.jsp?action=copyHnrToLocal&demographicId=<%=currentDemographicId%>'" /><input type="button" value="cancel" onclick="document.location='<%="../ClientManager.do?id="+currentDemographicId%>'" />

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
