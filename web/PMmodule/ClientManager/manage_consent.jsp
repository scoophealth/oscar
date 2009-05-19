<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ManageConsent"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicTransfer"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	
	ManageConsent manageConsent=new ManageConsent(currentDemographicId);

	String viewConsentId=request.getParameter("viewConsentId");
	manageConsent.setViewConsentId(viewConsentId);
%>

<h3><%=viewConsentId!=null?"View Consent":"Manage Consent"%></h3>
<br />
<form action="manage_consent_action.jsp">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />

	<table class="genericTable" style="border:none">
		<tr>
			<td></td>
			<td class="genericTableHeader" style="text-align:center">Consent to share all data</td>
			<td class="genericTableHeader" style="text-align:center">Exclude mental health data</td>
		</tr>
		<tr>
			<%
				CachedFacility localCachedFacility=manageConsent.getLocalCachedFacility();
				int remoteFacilityId=localCachedFacility.getIntegratorFacilityId();
			%>
			<td class="genericTableHeader"><%=localCachedFacility.getName()%><input type="hidden" name="consent.<%=remoteFacilityId%>.placeholder" value="on" /></td>
			<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.consentToShareData" <%=manageConsent.displayAsCheckedConsentToShareData(remoteFacilityId)?"checked=\"on\"":""%> /></td>
			<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.excludeMentalHealth" <%=manageConsent.displayAsCheckedExcludeMentalHealthData(remoteFacilityId)?"checked=\"on\"":""%> /></td>
		</tr>
		<%
			for (CachedFacility cachedFacility : manageConsent.getAllFacilitiesToDisplay())
			{
				remoteFacilityId=cachedFacility.getIntegratorFacilityId();
				%>
					<tr>
						<td class="genericTableHeader"><%=cachedFacility.getName()%><input type="hidden" name="consent.<%=remoteFacilityId%>.placeholder" value="on" /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.consentToShareData" <%=manageConsent.displayAsCheckedConsentToShareData(remoteFacilityId)?"checked=\"on\"":""%> /></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.excludeMentalHealth" <%=manageConsent.displayAsCheckedExcludeMentalHealthData(remoteFacilityId)?"checked=\"on\"":""%> /></td>
					</tr>
				<%
			}
		%>
	</table>
		
	<%
		if (manageConsent.useDigitalSignatures())
		{
			String signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			
			String imageUrl=null;
			if (viewConsentId==null) imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&amp;"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
			else imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&amp;digitalSignatureId="+manageConsent.getPreviousConsentDigitalSignatureId();

			%>
				<br />
				<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>" value="<%=signatureRequestId%>" />
				Client Signature<br /><img style="border:solid gray 1px; width:500px; height:100px" id="signature" src="<%=imageUrl%>" alt="digital_signature" />
				<script type="text/javascript">
					var POLL_TIME=2500;
					var counter=0;

					function refreshImage()
					{
						counter=counter+1;
						var img=document.getElementById("signature");
						img.src='<%=imageUrl%>&amp;rand='+counter;
					}
				</script>
				<br />
				<input type="button" value="Sign Signature" onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>'" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> />
				<br />					
			<%								
		}
	%>
	<br />
	<input type="submit" value="save" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
