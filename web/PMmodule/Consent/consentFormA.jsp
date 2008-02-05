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

<%@ include file="/taglibs.jsp"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="org.oscarehr.PMmodule.model.Demographic"%>
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
	
		function submitConsent(form) {
			var doSubmission = true;
			
			var answer1 = getRadioValue(form.elements['consent.answer1']);
			var answer2 = getRadioValue(form.elements['consent.answer2']);
			var answer3 = getRadioValue(form.elements['consent.answer3']);												

			//make sure they've answered everything			
			if(typeof(answer1) == 'undefined' || typeof(answer2) == 'undefined' || typeof(answer3) == 'undefined') {
				alert('You must ask each question, and record whether the client answered correctly');
				return;
			}
			
			if(form.elements['consent.refusedToSign'].checked == false && form.elements['consent.signatureDeclaration'].checked == false) {
				alert('You must declare that you have obtained the client\'s signature');
				return;
			}

			
			
			//warning about incompetence
			if(answer1 != 1 || answer2 != 1 || answer3 != 1) {
				if(confirm('Not all questions were answered correctly. \nSubmitting this form will result in the inability to obtain informed consent')) {
					form.elements['consent.status'].value = '<%=Demographic.ConsentGiven.NONE.name()%>';					
				} else {
					return;
				}
			}
			
			if(doSubmission) {
				form.submit();
				opener.document.clientManagerForm.submit();
				
			}		
		}

		function refusedToSign(ctl) {
			if(ctl.checked) {
				document.consentForm.elements['consent.exclusionString'].value='all';				
				setExclusionChoice('all');
				document.consentForm.elements['consent.signatureDeclaration'].checked=false;
			} else {
				document.consentForm.elements['consent.exclusionString'].value='';				
			}
		}
		
		function doSignatureDeclaration(ctl) {
			if(ctl.checked) {
				document.consentForm.elements['consent.refusedToSign'].checked=false;				
				document.consentForm.elements['consent.exclusionString'].value='';
			} 
		}	
		
		function setExclusionChoice(value) {
			var radios = document.consentForm.elements['consent.exclusionString'];
						
			var i =0;
			for(;i<radios.length;i++) {
				
				if(radios[i].value == value) {
					radios[i].checked=true;
				} else {
					radios[i].checked=false;
				}
				
			}
		}
				
		function showListOfAgencies() {
			window.open('<html:rewrite action="/PMmodule/Consent"/>?method=agency_list','appendixa','width=500,height=500');
		}
	</script>
</head>

<body topmargin="20" leftmargin="10">

<c:out value="${clientName}"/>
<br/>
<table border="2" width="700" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%">

		<p><b><font color="blue">Please give the client a copy of this consent form.  Please read the following to the client:</font></b></p>
		</td>
		</tr>
	<tr>
		<td width="100%">
		<p>“CAISI is a community project.</p>
		<p>The CAISI program was created to link many of the city's health and shelter 
		service agencies together so that people who are homeless can get better and 
		faster services from them.  The program wants people who are homeless to have 
		control over their personal information and to have a better quality of life.  
		Your personal information is now stored on computers at the different places you get care. 
		 With your permission, this information will be sent to other agencies when you seek services 
		 from them so that they can give you better care.  The types of agencies are: 
		</p>
		<li>Hospitals
		<li>Ambulance services
		<li>Public health
		<li>Outreach teams
		<li>Shelters
		<li>Mental health teams
		<li>Related agencies that are CAISI partners

		<p>In the CAISI program, only those people who give you care will be able to see your information.  
		CAISI is set up so that people with similar jobs and training can communicate with one another. 
		 For example, only health care teams will be allowed to see detailed medical information.</p>
		 
		<p>Your information will be used by this agency and by other agencies to give you better care.  
		Your information will be put together with the information from other people who are homeless 
		to improve the services offered, to do research, and to compile statistics for advocacy.   </p>
		
		<p>Your care at <c:out value="${agency.name}"/> will not change by allowing agencies to
		send information and work together through CAISI.</p>
		
		<p>You may withdraw permission to send information to other agencies
		at any time. To withdraw, contact <c:out value="${agency.name}"/> staff or any other CAISI
		partner.Your care at <c:out value="${agency.name}"/> will not change if you withdraw permission.  </p>
		
		<p>Any questions can be directed to <c:out value="${agency.name}"/> staff.”</p>
		
		<p>You are allowed to ask for more information about CAISI and/or to talk to your worker or other people 
		about CAISI.  You are allowed to make decisions based on this information.  </p>
		
		</i> 
		
		
		<html:form action="/PMmodule/Consent.do">
			<input type="hidden" name="id"
				value="<c:out value="${requestScope.id}"/>">
			<input type="hidden" name="method" value="saveConsent" />
			<input type="hidden" name="consent.formName" value="formA">
			<input type="hidden" name="consent.formVersion" value="1.0">			 
			</td>
	</tr>
	<tr>
		<td width="100%"><p><b><font color="blue">Part 2: This is a test of comprehension. 
		 Clients must correctly answer all 3 questions to give informed consent.  
		 If one or more questions are answered incorrectly, do not proceed to part 3.  
		 </font></b>
		</p>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<p>1.<i>What is the purpose of the CAISI project? </i><br>
		<html:radio property="consent.answer1" value="1" />Correct
			<html:radio property="consent.answer1" value="0"/>Incorrect</p>
		<p>[Correct includes any one of: to send information to agencies when
		they care for you; to provide better care for clients; to give clients
		control over their information or any combination of these]</p>
		<p>2.<i>When are you able to withdraw from CAISI?</i><html:radio
			property="consent.answer2" value="1" />Correct <html:radio
			property="consent.answer2" value="0" />Incorrect</p>
		<p>[Correct includes: any time]</p>
		<p>3.<i>Will your care at <c:out value="${agency.name}"/> be affected by your participation
		in CAISI? </i> <html:radio property="consent.answer3" value="1"/>
		Correct <html:radio property="consent.answer3" value="0"/>Incorrect</p>
		<p>[Correct = no]</p>
		</td>
	</tr>

	<tr>
		<td width="100%">
		<p><b><font color="blue">Part 3: Read the following prompt and record if the client
		agrees to consent to CAISI. Nothing needs to be recorded if the client
		refuses to consent. Do not read prompt if less than three correct
		answers were given for the questions in Part 2 (above). </font></b></p>
		</td>
	</tr>
	<tr>
		<td>
		<p>
		<html:radio property="consent.status" value="<%=Demographic.ConsentGiven.ALL.name()%>"/>
		&nbsp&nbsp&nbsp I, <b><c:out value="${clientName}"/></b>,
		permit all CAISI partner agencies to record, send and use my
		personal information for the purposes above.</p>
		</td>
	</tr>
	<tr>
		<td>
		<p>
		<html:radio property="consent.status" value="<%=Demographic.ConsentGiven.CIRCLE_OF_CARE.name()%>"/>
		&nbsp&nbsp&nbsp I, <b><c:out value="${clientName}"/></b>,
		permit only health care providers to record, send and use
		my personal information for the purposes above.</p>
		</td>
	</tr>
	<tr>
		<td>
		<p>
		<html:radio property="consent.status" value="<%=Demographic.ConsentGiven.EMPI.name()%>"/>
		&nbsp&nbsp&nbsp I, <b><c:out value="${clientName}"/></b>,
		permit all CAISI partner agencies to record, send and
		use only my demographic information for the purposes above.</p>
		</td>
	</tr>	
	<tr>
		<td>
		<p>
		<html:radio property="consent.status" value="<%=Demographic.ConsentGiven.NONE.name()%>"/>
		&nbsp&nbsp&nbsp I, <b><c:out value="${clientName}"/></b>,
		do <b>not</b> permit do not permit City of Toronto or any other CAISI partner
		agencies to record, send and use my personal information for the purposes above.
		</p>
		</td>
	</tr>

	<tr>
	<td>
	<div align="left">
	<!--  button which invokes the sign now call -->
		<!--  <input type="button" value="Sign This Form" name="B1" onClick="signNow()">-->
		<br>Signature___________________________<br>
		     Date_________________________<br>
		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<br>Signature___________________________<br>
		     Date_________________________</td>
	
	</div>
	</tr>

	<tr>
	<td>
	
	This form has been printed and  signed manually. It is kept in the location: <html:text property="consent.location" size="40"/>
	</td>
	</tr>
	<tr>
		<td>
			<html:checkbox property="consent.signatureDeclaration" onclick="doSignatureDeclaration(this)"/>
			I, <c:out value="${provider.formattedName}"/>, state that I have aquired the client's signature on a printed copy of this form
		</td>
	</tr>
	
	<tr>
		<td>
			<html:checkbox property="consent.refusedToSign" onclick="refusedToSign(this)"/>
			The Client has refused to sign the form.
		</td>
	</tr>	
		
	
	</table>
	
	</html:form>
	
	
	
	<tr>
	<td>
	<br>
	<input type="button" value="Save Consent" onclick="submitConsent(document.consentForm)" />
	<input type="button" value="Cancel"	onclick="window.close()" />
	<input type="button" value="Print to Sign" onClick="window.print()" />
	&nbsp;&nbsp;
	<input type="button" value="List of Agencies" onclick="showListOfAgencies()"/>		
	
	</td>
	</tr>

	<tr>
		<td>
		<p align="center"><b>CAISI©&nbsp;&nbsp; <a href="http://www.caisi.ca">www.caisi.ca</a></b>
		</td>
	</tr>
</table>

</body>

</html>
