<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ManageConsent"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacility"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility facility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider provider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);

	ManageConsent manageConsent=new ManageConsent(facility, provider, currentDemographicId);
%>


<h3>Manage Consent</h3>

<form action="manage_consent_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />

	<table class="genericTable">
		<tr>
			<td class="genericTableHeader"></td>
			<td class="genericTableHeader" style="text-align:center">Restrict<br/>To HICs</td>
			<td class="genericTableHeader" style="text-align:center">Allow<br />Searches</td>
			<td class="genericTableHeader" style="text-align:center">Allow<br />Basic Personal Data</td>
			<td class="genericTableHeader" style="text-align:center">Allow<br />Mental Health Data</td>
			<td class="genericTableHeader" style="text-align:center">Allow<br />Health Number Registry</td>
		</tr>
		<%
			for (CachedFacility cachedFacility : manageConsent.getAllRemoteFacilities())
			{
				int remoteFacilityId=cachedFacility.getIntegratorFacilityId();
				%>
					<tr>
						<td class="genericTableHeader"><%=cachedFacility.getName()%></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.hic" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"hic")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.search" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"search")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.personal" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"personal")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.mental" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"mental")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.hnr" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"hnr")?"checked=\"on\"":""%> /></td>
					</tr>
				<%
			}
		%>
	</table>
		
	<input type="submit" value="save" /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
