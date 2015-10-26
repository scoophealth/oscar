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

<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent.ConsentStatus"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent.SignatureStatus"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ManageConsent"%>
<%@page import="org.oscarehr.caisi_integrator.ws.CachedFacility"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicTransfer"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>
<script>
	String.prototype.trim = function() { return this.replace(/^\s+|\s+$|\n$/g, ''); };
</script>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	
	ManageConsent manageConsent=new ManageConsent(loggedInInfo,currentDemographicId);

	String viewConsentId=request.getParameter("viewConsentId");
	manageConsent.setViewConsentId(viewConsentId);
	
	String signatureRequestId = null;
%>
<script type="text/javascript">
	function submitManageConsentForm() {
		var val = "";
		for (var i=0; i < document.manageConsentForm.signatureStatus.length; i++)
		{
		   		if (document.manageConsentForm.signatureStatus[i].checked)
		      	{
		      		 val = document.manageConsentForm.signatureStatus[i].value;
		      	}
		}
		
		if(val == "") {
			alert("In order to save this form, please select whether a paper or electronic signature has been collected.");
			return false;
		}		
		else if(val == "PAPER") {
			return confirm("Has a paper copy of this consent been signed and retained at this agency?");
		}
		else
			return true;
	}
	
</script>

<h3><%=viewConsentId!=null?"View Consent":"Manage Consent"%></h3>
<hr />
<div style="border:solid black 1px">
	<%@include file="manage_consent_text.jspf" %>
</div>
<hr />


<form name="manageConsentForm" action="manage_consent_action.jsp" onsubmit="return submitManageConsentForm()">
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<input type="hidden" name="signature_status" id="signature_status" value="NOT_FOUND"/>
	<div style="font-weight:bold">Client consent</div>
	<table style="background-color:#ddddff">
		<tr>
			<td><input type="radio" name="consentStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.GIVEN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.GIVEN)?"checked=\"on\"":""%> /></td>
			<%
				String tempColour="black";
				if (manageConsent.disableEdit() && !manageConsent.displayAsSelectedConsentStatus(ConsentStatus.GIVEN)) tempColour="grey";
			%>
			<td style="color:<%=tempColour%>">
				I understand the purpose of CAISI, and the benefits and risks associated with consenting to integrate my personal information, including personal health information, among the participating CAISI integrating agencies, and I have been given a list of participating agencies. I consent to the integration of my information for the purposes described above.
				<%-- removed until further notice
					<br /><br />
					<input type="checkbox" name="excludeMentalHealth" <%=manageConsent.displayAsCheckedExcludeMentalHealthData()?"checked=\"on\"":""%> <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> /><span style="font-weight:bold">I choose to exclude my mental health record from the integration of my information.</span>
					<br /><br />
				--%>
				
					<br />
					<br />
					Check to indicate which agencies to exclude from consent :
					<br />
					<%
						Collection<CachedFacility> facilitiesToDisplay=manageConsent.getAllFacilitiesToDisplay(loggedInInfo);
					
						if (facilitiesToDisplay!=null)
						{
							for (CachedFacility cachedFacility : facilitiesToDisplay)
							{
								int remoteFacilityId=cachedFacility.getIntegratorFacilityId();
								%>
									<input type="checkbox" name="consent.<%=remoteFacilityId%>.excludeShareData" <%=manageConsent.displayAsCheckedExcludeFacility(remoteFacilityId)?"checked=\"on\"":""%> <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> /><%=cachedFacility.getName()%><br />
								<%
							}
						}
						else
						{
							%>
								<h3 style="color:red">System is unavailable.</h3>
							<%						
						}
					%>
					<br />	
			</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REVOKED%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REVOKED)?"checked=\"on\"":""%> /></td>
			<%
				tempColour="black";
				if (manageConsent.disableEdit() && !manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REVOKED)) tempColour="grey";
			%>
			<td style="color:<%=tempColour%>">I do not consent to the integration of my information for the integration purposes described above.</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.DEFERRED_CONSIDER_LATER%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED_CONSIDER_LATER)?"checked=\"on\"":""%> /></td>
			<%
				tempColour="black";
				if (manageConsent.disableEdit() && !manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED_CONSIDER_LATER)) tempColour="grey";
			%>
			<td style="color:<%=tempColour%>">Deferred : client wishes to consider consent at a future date.</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.DEFERRED_NOT_APPROPRIATE%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED_NOT_APPROPRIATE)?"checked=\"on\"":""%> /></td>
			<%
				tempColour="black";
				if (manageConsent.disableEdit() && !manageConsent.displayAsSelectedConsentStatus(ConsentStatus.DEFERRED_NOT_APPROPRIATE)) tempColour="grey";
			%>
			<td style="color:<%=tempColour%>">Deferred : staff decided that collection of consent not appropriate at this time.</td>
		</tr>
		<tr>
			<td><input type="radio" name="consentStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=ConsentStatus.REFUSED_TO_SIGN%>" <%=manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REFUSED_TO_SIGN)?"checked=\"on\"":""%> /></td>
			<%
				tempColour="black";
				if (manageConsent.disableEdit() && !manageConsent.displayAsSelectedConsentStatus(ConsentStatus.REFUSED_TO_SIGN)) tempColour="grey";
			%>
			<td style="color:<%=tempColour%>">Refused to sign : client is not interested in integration.</td>
		</tr>
	
	<tr><td></td><td></td></tr>
	
	<tr>
		<td colspan="2"><b>Expiry of consent at a predefined time</b>
		<select id="consentExpiry" name="consentExpiry" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%>>
			<option <%=manageConsent.displayAsSelectedExpiry(-1)?"selected=\"selected\"":""%> value="-1">I do not wish this consent to expire at a predefined time</option>
			<option <%=manageConsent.displayAsSelectedExpiry(6)?"selected=\"selected\"":""%> value="6">I wish this consent to expire and require a new consent in 6 months.</option>
			<option <%=manageConsent.displayAsSelectedExpiry(12)?"selected=\"selected\"":""%> value="12">I wish this consent to expire and require a new consent in 12 months.</option>
			<option <%=manageConsent.displayAsSelectedExpiry(60)?"selected=\"selected\"":""%> value="60">I wish this consent to expire and require a new consent in 60 months.</option>
		</select>
		</td>
	</tr>	
</table>	
	<%
		if (manageConsent.useDigitalSignatures())
		{
			signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
			
			String imageUrl=null;
			String statusUrl=null;
			
			if (viewConsentId==null)
			{
				imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
			}
			else
			{
				Integer previousDigitalSignatureId=manageConsent.getPreviousConsentDigitalSignatureId();
				if (previousDigitalSignatureId==null) imageUrl=request.getContextPath()+"/images/1x1.gif";
				else imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&digitalSignatureId="+previousDigitalSignatureId;
			}
			statusUrl = request.getContextPath()+"/PMmodule/ClientManager/check_signature_status.jsp?" + DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
			%>
				<br />
				<input type="hidden" name="<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>" value="<%=signatureRequestId%>" />
				
				Client Signature<br />
				<table><tr><td rowspan="2">
				<img style="border:solid gray 1px; width:480px; height:100px" id="signature" src="<%=imageUrl%>" alt="digital_signature" />
				
				<script type="text/javascript">
					var POLL_TIME=2500;
					var counter=0;

					function refreshImage()
					{
						counter=counter+1;
						var img=document.getElementById("signature");
						img.src='<%=imageUrl%>&rand='+counter;

						
                        var request = dojo.io.bind({
                            url: '<%=statusUrl%>',
                            method: "post",
                            mimetype: "text/html",
                            load: function(type, data, evt){   
                                	var x = data.trim();                                	
                                    document.getElementById('signature_status').value=x;                                   
                            }
                    	});
						
					}
				</script>
				</td>
				<td>
				<input type="radio" name="signatureStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=SignatureStatus.PAPER%>" <%=manageConsent.displayAsSelectedSignatureStatus(SignatureStatus.PAPER)?"checked=\"on\"":""%> />
				A paper copy of this consent has been signed and retained at this agency
				<br />
				<input type="radio" name="signatureStatus" <%=manageConsent.disableEdit()?"disabled=\"disabled\"":""%> value="<%=SignatureStatus.ELECTRONIC%>" <%=manageConsent.displayAsSelectedSignatureStatus(SignatureStatus.ELECTRONIC)?"checked=\"on\"":""%> />
				An electronic signature has been recorded
				<br />
				</td>
				</tr>
				</table>
				<input type="button" value="Sign Signature" onclick="setInterval('refreshImage()', POLL_TIME); document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>'" <%=manageConsent.disableEdit()?"disabled=\"disabled\" style=\"display:none\"":""%> />
				<br />					
			<%								
		}
	%>
	<br />
	<%
		if (viewConsentId!=null)
		{
			%>
				Consent obtained by <%=manageConsent.getPreviousConsentProvider()%> on <%=manageConsent.getPreviousConsentDate()%> <br/>
			<%
		}
	%>		
	<input type="submit" value="sign save and exit" <%=manageConsent.disableEdit()?"disabled=\"disabled\" style=\"display:none\"":""%> /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
