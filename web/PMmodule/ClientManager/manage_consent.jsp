<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ManageConsent"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));

	Integer showConsentId=null;
	String tempString=request.getParameter("consentId");
	if (tempString!=null) showConsentId=Integer.parseInt(tempString);
	
	ManageConsent manageConsent=new ManageConsent(currentDemographicId, showConsentId);
%>



<%@page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%><h3><%=manageConsent.isReadOnly()?"View Previous Consent":"Manage Consent"%></h3>

<h4>Legend</h4>
<div style="font-size:smaller">
	<ul>
		<li>Health number registry : allow agencies to see this clients health number information in the health number registry</li>
		<li>Restrict To HIC's : allows retrieval of data only if the agency retrieving the data is an HIC, does not affect health number registry</li>
		<li>Searches : allows the client from this agency to show up in searches</li>
		<li>All non domain data : allow the selected agency to send any data not covered in the other consent domains</li>
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
			<td class="genericTableHeader" style="text-align:center;display:none">Allow to show up<br />in searches</td>
			<td class="genericTableHeader" style="text-align:center">Allow sending of<br />all non domain data</td>
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
									<td class="genericTableData" style="text-align:center;vertical-align:middle" rowspan="<%=manageConsent.getAllRemoteFacilities().size()%>" ><input type="checkbox" name="consent.hnr" <%=manageConsent.displayAsChecked(remoteFacilityId,"hnr")?"checked=\"on\"":""%> /></td>					
								<%								
								showHNRCheckbox=false;
							}
						%>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.hic" <%=manageConsent.displayAsChecked(remoteFacilityId,"hic")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center;display:none"><input type="checkbox" name="consent.<%=remoteFacilityId%>.search" <%=manageConsent.displayAsChecked(remoteFacilityId,"search")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.nonDomain" <%=manageConsent.displayAsChecked(remoteFacilityId,"nonDomain")?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.mental" <%=manageConsent.displayAsChecked(remoteFacilityId,"mental")?"checked=\"on\"":""%> /></td>
					</tr>
				<%
			}
		%>
	</table>
		
	<%
		if (manageConsent.useDigitalSignatures())
		{
			String signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			String imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
			%>
				<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>" value="<%=signatureRequestId%>" />
				<img id="signature" src="<%=imageUrl%>" alt="digital_signature" />
				<script type="text/javascript">
					var POLL_TIME=1500;
					var counter=0;

					function refreshImage()
					{
						counter=counter+1;
						var img=document.getElementById("signature");
						img.src='<%=imageUrl%>&amp;rand='+counter;
					}
				</script>
				<br />
				<input type="button" value="Get Digital Signature" onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>'"/>					
			<%								
		}
	%>
	<br />
	<input type="submit" value="save" <%=manageConsent.isReadOnly()?"disabled=\"disabled\"":""%> /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
