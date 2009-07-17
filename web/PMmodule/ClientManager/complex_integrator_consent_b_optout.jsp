<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->

<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>

<%
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	
	String currentDemographicId = request.getParameter("demographicId");
	Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	IntegratorConsent integratorConsent=new IntegratorConsent();
	// if a consentId is passed it that means it's an existing consent, i.e. some one is viewing it, so saving shouldn't be allowed.
	boolean viewOnly=false;
	
	String consentId=request.getParameter("consentId");
	if (consentId!=null)
	{
		integratorConsent=integratorConsentDao.find(Integer.parseInt(consentId));
		provider=providerDao.getProvider(integratorConsent.getProviderNo());
		viewOnly=true;
	}
	
	Demographic currentDemographic=demographicDao.getDemographic(""+currentDemographicId);
	Facility currentFacility = (Facility)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);    
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Consent Form</title>
<script>
		function submitConsent(form) {
			var doSubmission = true;
			if(form.elements['signatureDeclaration'].checked == false) {
				alert('You must declare that you have obtained the client\'s signature');
				return;
			}
			
			if(doSubmission) {
				form.submit();
				opener.document.clientManagerForm.submit();
				
			}		
		}

		function clickOptOut(ctl) {
			if(ctl.checked) {
				document.consentForm.elements['exclusionString'].value='all';
			} else {
				document.consentForm.elements['exclusionString'].value='';			
			}
			document.getElementById('chk_hic').checked=false;
		}
		
		function clickHicOptOut(ctl) {
			if(ctl.checked) {
				document.consentForm.elements['exclusionString'].value='non-hic';
			} else {
				document.consentForm.elements['exclusionString'].value='';			
			}
			document.getElementById('chk_all').checked=false;
		}

		function setCheckboxes() {
			var exclusion = document.consentForm.elements['exclusionString'].value;
			
			if(exclusion == 'all') {
				var ctl = document.getElementById('chk_all');
				ctl.checked=true;
			}
			if(exclusion == 'non-hic') {
				var ctl = document.getElementById('chk_hic');
				ctl.checked=true;
			}
		}	
	</script>
</head>

<body onload="setCheckboxes()">


<br />
<table border="2" width="700" cellspacing="0" cellpadding="0">
	<form name="consentForm" method="post"
		action="complex_integrator_consent_b_optout_action.jsp"><input
		type="hidden" name="id" value=""> <input type="hidden"
		name="method" value="saveConsent" /> <input type="hidden"
		name="answer1" value=""> <input type="hidden" name="answer2"
		value=""> <input type="hidden" name="answer3" value="">
	<input type="hidden" name="location" value="">
	<tr>
		<td width="100%">
		<p><b><font color="blue">Part 3b: Read the following
		prompt and record only if the client chooses not to withdraw consent
		from agencies sending information to each other when they provide care
		for the client. Nothing needs to be recorded if the client agrees to
		consent. This section does not need to be read if the client refused
		to sign the form above. </font></b></p>

		</td>
	</tr>
	<tr>
		<td><input type="checkbox" name="consent" value="NONE"
			<%=integratorConsent.isConsentToNone()?"checked=\"checked\"":""%> />I,
		<b><%=currentDemographic.getFormattedName()%></b>, do not permit <b><%=currentFacility.getName()%></b>
		and any other CAISI partner agencies to record, send and use my
		personal information for the purposes above.</td>
	</tr>

	<tr>
		<td><input type="checkbox" name="consent" value="HIC_ALL"
			<%=integratorConsent.isConsentToAllButRestrictToHic()?"checked=\"checked\"":""%> />I,
		<b><%=currentDemographic.getFormattedName()%></b>, do not permit any
		non-health providing programs to record, send and use my personal
		information for the purposes above.</td>
	</tr>

	<tr>
		<td>
		<div align="left"><!--  button which invokes the sign now call -->

		<!--  <input type="button" value="Sign This Form" name="B1" onClick="signNow()">-->
		<br>
		Signature___________________________<br>
		Date_________________________<br>

		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <br>
		Signature___________________________<br>
		Date_________________________
		</td>

		</div>

	</tr>

	<tr>
		<td><input type="checkbox" name="signatureDeclaration" value="on"
			<%=viewOnly?"checked=\"checked\"":""%>> I, <%=provider.getFormattedName()%>,
		state that I have aquired the client's signature on a printed copy of
		this form</td>
	</tr>

	<tr>
		<td><br>

		<input type="hidden" name="demographicId"
			value="<%=currentDemographicId%>" /> <input
			<%=viewOnly?"disabled=\"disabled\"":""%> type="button"
			value="Save the form and Exit"
			onclick="submitConsent(document.consentForm)" /> <input
			type="button" value="Cancel" onclick="window.close()" /> <input
			type="button" value="Print to Sign" onClick="window.print()" /></td>
	</tr>

	<tr>
		<td>

		<p align="center"><b>CAISI©&nbsp;&nbsp; <a
			href="http://www.caisi.ca">www.caisi.ca</a></b>
		</td>
	</tr>

	</form>
</table>

</body>
</html>
