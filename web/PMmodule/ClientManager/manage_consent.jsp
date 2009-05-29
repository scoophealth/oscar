<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent.ConsentStatus"%>
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

	<span style="font-weight:bold">Do not share clients mental health data</span><input type="checkbox" name="excludeMentalHealth" <%=manageConsent.displayAsCheckedExcludeMentalHealthData()?"checked=\"on\"":""%> <%=viewConsentId!=null?"disabled=\"disabled\"":""%> />
	<br /><br />

	<table class="genericTable" style="border:none">
		<tr>
			<td></td>
			<td class="genericTableHeader" style="text-align:center">Share data from</td>
		</tr>
		<%
			for (CachedFacility cachedFacility : manageConsent.getAllFacilitiesToDisplay())
			{
				int remoteFacilityId=cachedFacility.getIntegratorFacilityId();
				%>
					<tr>
						<td class="genericTableHeader"><%=cachedFacility.getName()%></td>
						<td class="genericTableData" style="text-align:center"><input type="checkbox" name="consent.<%=remoteFacilityId%>.consentToShareData" <%=manageConsent.displayAsCheckedConsentToShareData(remoteFacilityId)?"checked=\"on\"":""%> <%=viewConsentId!=null?"disabled=\"disabled\"":""%> /></td>
					</tr>
				<%
			}
		%>
	</table>
	<br />		
	<div style="font-weight:bold">Client consent</div>
	<input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.GIVEN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.GIVEN)?"checked=\"on\"":""%> /> given <br />
	<input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REVOKED%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REVOKED)?"checked=\"on\"":""%> /> revoked <br />
	<input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.DEFERRED%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED)?"checked=\"on\"":""%> /> deferred <br />
	<input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REFUSED_TO_SIGN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REFUSED_TO_SIGN)?"checked=\"on\"":""%> /> refused to sign <br />
	<br />
	<div style="font-weight:bold">Consent expiry : </div>
	<select name="consentExpiry" <%=viewConsentId!=null?"disabled=\"disabled\"":""%>>
		<option <%=manageConsent.displayAsSelectedExpiry(-1)?"selected=\"selected\"":""%> value="-1">Does not expire</option>
		<option <%=manageConsent.displayAsSelectedExpiry(6)?"selected=\"selected\"":""%> value="6">Expires in 6 months</option>
		<option <%=manageConsent.displayAsSelectedExpiry(12)?"selected=\"selected\"":""%> value="12">Expires in 12 months</option>
		<option <%=manageConsent.displayAsSelectedExpiry(60)?"selected=\"selected\"":""%> value="60">Expires in 60 months</option>
	</select>
	<br />

	<%
		if (manageConsent.useDigitalSignatures())
		{
			String signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
			
			String imageUrl=null;
			if (viewConsentId==null)
			{
				imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&amp;"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
			}
			else
			{
				Integer previousDigitalSignatureId=manageConsent.getPreviousConsentDigitalSignatureId();
				if (previousDigitalSignatureId==null) imageUrl=request.getContextPath()+"/images/1x1.gif";
				else imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&amp;digitalSignatureId="+previousDigitalSignatureId;
			}

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
				<input type="button" value="Sign Signature" onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>'" <%=viewConsentId!=null?"disabled=\"disabled\" style=\"display:none\"":""%> />
				<br />					
			<%								
		}
	%>
	<br />
	<input type="submit" value="save" <%=viewConsentId!=null?"disabled=\"disabled\" style=\"display:none\"":""%> /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
