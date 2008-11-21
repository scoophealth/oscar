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
	int currentFacilityId = (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	Facility currentFacility = (Facility)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<title>Consent Form</title>
	<script>
	
		function getRadioValue(ctl) {
			for(var x=0;x<ctl.length;x++) {
				if(ctl[x].checked) {
					return ctl[x].value;
				}
			}
		}
	
		function submitConsent(form,gotoOptout) {
			
			var answer1 = getRadioValue(form.elements['answer1']);
			var answer2 = getRadioValue(form.elements['answer2']);
			var answer3 = getRadioValue(form.elements['answer3']);												

			//make sure they've answered everything			
			if(typeof(answer1) == 'undefined' || typeof(answer2) == 'undefined' || typeof(answer3) == 'undefined') {
				alert('You must ask each question, and record whether the client answered correctly');
				return;
			}			
			
			//warning about incompetence
			if(answer1 != 1 || answer2 != 1 || answer3 != 1) {
				if(confirm('Not all questions were answered correctly. \nSubmitting this form will result in the inability to obtain informed consent')) {
					//form.elements['status'].value = 'unable to obtain informed consent';
					form.elements['consent'].value='NONE';					
				} else {
					return;
				}
			}

			if(form.elements['refusedToSign'].checked == true) {
				if(confirm('Due to the client refusing to sign, the consent form cannot be completed\nClick ok to exit or cancel to return to form')) {
					window.close();
				} else {
					return;
				}
			}
						
			if(form.elements['signatureDeclaration'].checked == false) {
				alert('You must declare that you have obtained the client\'s signature');
				return;
			}

			if(!document.getElementById('readOptOutStatement').checked) {
				alert('Please read the opt-out statement to the client.');
				return;
			}
			
			document.consentForm.elements['gotoOptout'].value=gotoOptout;
			
			form.submit();
		}
	</script>
</head>

<body>
<table border="2" width="700" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%">

		<p><b><font color="blue">Part 1: Please give the client
		a copy of this consent form. Please read the following to the client:
		</font></b></p>
		</td>
	</tr>

	<tr>
		<td width="100%">
		<p>The CAISI program was created to link many of the city's health
		and shelter service agencies together so that people who are homeless
		can get better and faster services from them. The program wants people
		who are homeless to have control over their personal information and
		to have a better quality of life. Your personal information is now
		stored on computers at the different places you get care. With your
		permission, this information will be sent to other agencies when you
		seek services from them so that they can give you better care. The
		types of agencies are:</p>
		<li>Hospitals
		<li>Ambulance services
		<li>Public health
		<li>Outreach teams
		<li>Shelters
		<li>Mental health teams
		<li>Related agencies that are CAISI partners

		<p>In the CAISI program, only those people who give you care will
		be able to see your information. CAISI is set up so that people with
		similar jobs and training can communicate with one another. For
		example, only health care teams will be allowed to see detailed
		medical information.</p>


		<p>Your information will be used by this agency and by other
		agencies to give you better care. Your information will be put
		together with the information from other people who are homeless to
		improve the services offered, to do research, and to compile
		statistics for advocacy.</p>

		<p>Your care at <%=currentFacility.getName()%> will not change by allowing agencies
		to send information and work together through CAISI.</p>

		<p>You may withdraw permission to send information to other
		agencies at any time. To withdraw, contact <%=currentFacility.getName()%> or any other
		CAISI partner. Your care at <%=currentFacility.getName()%> will not change if you
		withdraw permission.</p>

		<p>Any questions can be directed to <%=currentFacility.getName()%> staff.</p>

		<p>You are allowed to ask for more information about CAISI and/or
		to talk to your worker or other people about CAISI. You are allowed to
		make decisions based on this information.</p>


		</i>


		<form name="consentForm" method="post" action="complex_integrator_consent_b_action.jsp">
			<input type="hidden" name="demographicId" value="<%=currentDemographicId%>">
			<input type="hidden" name="consent" value="ALL">
			<input type="hidden" name="gotoOptout" value="false" />
		</td>
	</tr>
	<tr>
		<td width="100%">
		<p><b><font color="blue">Part 2: This is a test of
		comprehension. Clients must correctly answer all 3 questions to give
		informed consent. If one or more questions are answered incorrectly,
		do not proceed to part 3. </font></b></p>
		</td>
	</tr>
	<tr>

		<td width="100%">
		<p>1.<i>What is the purpose of the CAISI project? </i><br>
		<input type="radio" name="answer1" value="1" <%=viewOnly?"checked=\"checked\"":""%> >Correct <input
			type="radio" name="answer1" value="0">Incorrect</p>
		<p>[Correct includes any one of: to send information to agencies
		when they care for you; to provide better care for clients; to give
		clients control over their information or any combination of these]</p>
		<p>2.<i>When are you able to withdraw from CAISI?</i><input
			type="radio" name="answer2" value="1" <%=viewOnly?"checked=\"checked\"":""%> >Correct <input
			type="radio" name="answer2" value="0">Incorrect</p>

		<p>[Correct includes: any time]</p>
		<p>3.<i>Will your care at <%=currentFacility.getName()%> be affected by your
		participation in CAISI? </i> <input type="radio" name="answer3"
			value="1" <%=viewOnly?"checked=\"checked\"":""%> > Correct <input type="radio" name="answer3"
			value="0">Incorrect</p>
		<p>[Correct = no]</p>
		</td>

	</tr>

	<tr>
		<td width="100%">
		<p><b><font color="blue">Part 3: Please read the
		following prompts and record if the client understands CAISI and if
		she/he chooses not to consent to CAISI. Do not read prompt if less
		than three correct answers were given for the questions in Part 2. </font></b></p>
		</td>
	</tr>
	<tr>
		<td>

		<p><br>
		I, <b><U><%=currentDemographic.getFormattedName()%></U></b>, understand the terms and conditions of the
		CAISI project above
		</td>
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

	<%
		String location="";
		if (integratorConsent.getPrintedFormLocation()!=null) location=integratorConsent.getPrintedFormLocation();
	%>
	<tr>
		<td>This form has been printed and signed manually. It is kept in
		the location: <input type="text" name="location" size="40"
			value="<%=location%>"></td>
	</tr>
	<tr>

		<td><input type="checkbox" name="signatureDeclaration"
			value="on" onclick="if (form.elements['signatureDeclaration'].checked) form.elements['refusedToSign'].checked=false" <%=viewOnly?"checked=\"checked\"":""%> > I,
		<%=provider.getFormattedName()%>, state that I have aquired the client's signature
		on a printed copy of this form</td>
	</tr>

	<tr>
		<td><input type="checkbox" name="refusedToSign"
			value="on" onclick="if (form.elements['refusedToSign'].checked) form.elements['signatureDeclaration'].checked=false"> The Client has
		refused to sign the form.</td>

	</tr>

	<tr>
		<td><input type="checkbox" id="readOptOutStatement" <%=viewOnly?"checked=\"checked\"":""%> /> I have
		read the following to the client "You may withdraw permission to send
		information to other CAISI agencies thatare caring for you at any
		time."</td>
	</tr>

</table>

</form>



<tr>

	<td><br>
	<input <%=viewOnly?"disabled=\"disabled\"":""%>  type="button" value="Continue to Opt-Out Form"
		onclick="submitConsent(document.consentForm,true)" /> 
   <input <%=viewOnly?"disabled=\"disabled\"":""%> 
		type="button" value="Save the Form"
		onclick="submitConsent(document.consentForm,false)" /> 
   <input
		type="button" value="Cancel" onclick="window.close()" /> <input
		type="button" value="Print to Sign" onClick="window.print()" />
	</td>
</tr>

<tr>
	<td>
	<p align="center"><b>CAISI©&nbsp;&nbsp; <a
		href="http://www.caisi.ca">www.caisi.ca</a></b>
	</td>
</tr>
</table>

</body>

</html>
