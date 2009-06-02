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

	<div style="font-weight:bold">Client consent</div>
	<table>
		<tr>
			<td><input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.GIVEN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.GIVEN)?"checked=\"on\"":""%> /></td>
			<td style="border:solid silver 1px">
				I understand the purpose of CAISI, and the benefits and risks associated with consenting to integrate my personal information, including personal health information, among the participating CAISI integrating agencies. I consent to the integration of my information for the purposes described above.
				<br /><br />
				<input type="checkbox" name="excludeMentalHealth" <%=manageConsent.displayAsCheckedExcludeMentalHealthData()?"checked=\"on\"":""%> <%=viewConsentId!=null?"disabled=\"disabled\"":""%> /><span style="font-weight:bold">I choose to exclude my mental health record from the integration of my information.</span>
				<br /><br />
				I do not wish records from the following agencies to be seen in other agencies that provide me care.
				<br />
				Check to indicate which agencies to exclude
				<br />
				<%
					for (CachedFacility cachedFacility : manageConsent.getAllFacilitiesToDisplay())
					{
						int remoteFacilityId=cachedFacility.getIntegratorFacilityId();
						%>
							<input type="checkbox" name="consent.<%=remoteFacilityId%>.excludeShareData" <%=manageConsent.displayAsCheckedExcludeFacility(remoteFacilityId)?"checked=\"on\"":""%> <%=viewConsentId!=null?"disabled=\"disabled\"":""%> /><%=cachedFacility.getName()%><br />
						<%
					}
				%>
				<br />		
			</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REVOKED%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REVOKED)?"checked=\"on\"":""%> /></td>
			<td>I do not consent to the integration of my information for the integration purposes described above.</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.DEFERRED%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED)?"checked=\"on\"":""%> /></td>
			<td>Deferred : staff decided that collection of consent not appropriate at this time.</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=viewConsentId!=null?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REFUSED_TO_SIGN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REFUSED_TO_SIGN)?"checked=\"on\"":""%> /></td>
			<td>Refused to sign : client is not interested in integration.</td>
		</tr>
	</table>
	<br />
	<div style="font-weight:bold">Consent expiry : </div>
	<select name="consentExpiry" <%=viewConsentId!=null?"disabled=\"disabled\"":""%>>
		<option <%=manageConsent.displayAsSelectedExpiry(-1)?"selected=\"selected\"":""%> value="-1">I do not wish this consent to expire at a predefined time</option>
		<option <%=manageConsent.displayAsSelectedExpiry(6)?"selected=\"selected\"":""%> value="6">I wish this consent to expire and require a new consent in 6 months.</option>
		<option <%=manageConsent.displayAsSelectedExpiry(12)?"selected=\"selected\"":""%> value="12">I wish this consent to expire and require a new consent in 12 months.</option>
		<option <%=manageConsent.displayAsSelectedExpiry(60)?"selected=\"selected\"":""%> value="60">I wish this consent to expire and require a new consent in 60 months.</option>
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
