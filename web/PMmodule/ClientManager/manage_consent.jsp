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

	Integer showConsentId=null;
	String tempString=request.getParameter("consentId");
	if (tempString!=null) showConsentId=Integer.parseInt(tempString);
	
	ManageConsent manageConsent=new ManageConsent(facility, provider, currentDemographicId, showConsentId);
%>


<h3><%=manageConsent.isReadOnly()?"View Previous Consent":"Manage Consent"%></h3>

<h4>Legend</h4>
<div style="font-size:smaller">
	<ul>
		<li>Health number registry : allow agencies to see this clients health number information in the health number registry</li>
		<li>Restrict To HIC's : allows retrieval of data only if the agency retrieving the data is an HIC, does not affect health number registry</li>
		<li>Searches : allows the selected agency to see this client in searches</li>
		<li>Basic personal data : allow the selected agency to send person data to other agencies</li>
		<li>Mental health data : allow the selected agency to send mental health data to other agencies</li>
	</ul>
</div>

<form action="manage_consent_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />

	<table class="genericTable">
		<tr>
			<td class="genericTableHeader"></td>
			<td class="genericTableHeader" style="text-align:center">Allow health number registry<br />information to be viewed</td>
			<td class="genericTableHeader" style="text-align:center">Restrict<br/>to HICs</td>
			<td class="genericTableHeader" style="text-align:center">Allow retrieving<br />for searches</td>
			<td class="genericTableHeader" style="text-align:center">Allow sending of<br />basic personal data</td>
			<td class="genericTableHeader" style="text-align:center">Allow sending of<br />mental health data</td>
		</tr>
		<%
			boolean showHNRCheckbox=true;
			for (CachedFacility cachedFacility : manageConsent.getAllRemoteFacilities())
			{
				int remoteFacilityId=cachedFacility.getIntegratorFacilityId();
				%>
					<tr>
						<td class="genericTableHeader"><%=cachedFacility.getName()%></td>
						<%
							if (showHNRCheckbox)
							{
								%>
									<td class="genericTableData" style="text-align:center;vertical-align:middle" rowspan="<%=manageConsent.getAllRemoteFacilities().size()%>" ><input type="checkbox" name="consent.hnr" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"hnr")?"checked=\"on\"":""%> /></td>					
								<%								
								showHNRCheckbox=false;
							}
						%>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.hic" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"hic")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.search" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"search")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.personal" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"personal")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.mental" <%=manageConsent.wasPreviouslyChecked(remoteFacilityId,"mental")?"checked=\"on\"":""%> /></td>
					</tr>
				<%
			}
		%>
	</table>
		
	<input type="submit" value="save" <%=manageConsent.isReadOnly()?"disabled=\"disabled\"":""%> /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
