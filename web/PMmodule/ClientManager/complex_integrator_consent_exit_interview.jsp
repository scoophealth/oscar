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


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Exit Interview</title>
</head>

<body topmargin="20" leftmargin="10">

<font color="#FF0000" face="Arial"><b><font size="3">Exit
interview:</font></b></font>
<p>
<form name="consentForm" method="post"
	action="complex_integrator_consent_exit_interview_action.jsp"><input
	type="hidden" name="demographicId"
	value="<%=request.getParameter("demographicId")%>">

<table border="1" width="100%" cellspacing="3" cellpadding="3">

	<tr>
		<td width="100%" colspan="2">
		<p><b><font color="blue"> Please answer the following
		questions to help us help make this consent process better. The
		questions may sound repetitive or may not apply to you, however we
		need to ask everyone the same questions and in the same way. </font></b></p>
		</td>
	</tr>

	<tr>
		<td valign="top">1.<i>What language do you prefer to speak? </i>
		</td>
		<td width="40%"><select name="interview.language">
			<option value=""></option>
			<option value="English">English</option>
			<option value="French">French</option>
			<option value="">Other</option>

			<option value="Refused">Refused</option>
		</select> <br />
		Other: <input type="text" name="interview.languageOther" size="50"
			value=""> <br />
		</td>
	</tr>

	<tr>
		<td valign="top">2.<i>What language do you prefer to read?</i></td>

		<td><select name="interview.languageRead">
			<option value=""></option>
			<option value="English">English</option>
			<option value="French">French</option>
			<option value="">Other</option>
			<option value="Refused">Refused</option>
		</select> <br />

		Other: <input type="text" name="interview.languageReadOther" size="50"
			value=""> <br />
		</td>
	</tr>

	<tr>
		<td valign="top">3.<i>What is the highest level of education
		you achieved?</i></td>
		<td><select name="interview.education">
			<option value=""></option>
			<option value="Grade 9">Grade 9</option>

			<option value="Completed grade 9-12">Completed grade 9-12</option>
			<option value="Some college/university">Some
			college/university</option>
			<option value="Completed college/university">Completed
			college/university</option>
			<option value="Refused">Refused</option>
		</select></td>
	</tr>

	<tr>

		<td valign="top">4.<i>Were you given enough time to review
		the consent information?</i></td>
		<td><select name="interview.review">
			<option value=""></option>
			<option value="Yes">Yes</option>
			<option value="No">No</option>
			<option value="Refused">Refused</option>
		</select> <br />
		Comments: <input type="text" name="interview.reviewOther" size="50"
			value=""> <br />
		</td>
	</tr>

	<tr>
		<td valign="top">5.<i>Did you feel pressured to allow CAISI
		partner agencies to work together by sending, receiving and using your
		information?</i></td>
		<td><select name="interview.pressure">
			<option value=""></option>
			<option value="Yes">Yes</option>
			<option value="No">No</option>
			<option value="Refused">Refused</option>
		</select> <br />
		Comments: <input type="text" name="interview.pressureOther" size="50"
			value=""> <br />

		</td>
	</tr>

	<tr>
		<td valign="top">6.<i>Do you require more information about
		CAISI?</i></td>
		<td><select name="interview.information">
			<option value=""></option>
			<option value="Yes">Yes</option>
			<option value="No">No</option>

			<option value="Refused">Refused</option>
		</select> <br />
		Comments: <input type="text" name="interview.informationOther"
			size="50" value=""> <br />
		</td>
	</tr>

	<tr>
		<td valign="top">7.<i>When would you like to be asked again
		about continuing to allow CAISI agencies to work together when they
		care for you by sending, receiving and using your information?</i></td>

		<td><select name="interview.followup">
			<option value=""></option>
			<option value="Next Visit">Next Visit</option>
			<option value="1 year">1 year</option>
			<option value="3 years">3 years</option>
			<option value="Never">Never</option>
			<option value="Other">Other</option>

			<option value="Refused">Refused</option>
		</select> <br />
		Comments: <input type="text" name="interview.followupOther" size="50"
			value=""> <br />
		</td>
	</tr>

	<tr>
		<td valign="top">8.<i>Do you have any additional comments on
		the CAISI consent process?</i></td>

		<td><select name="interview.comments">
			<option value=""></option>
			<option value="Yes">Yes</option>
			<option value="No">No</option>
			<option value="Refused">Refused</option>
		</select> <br />
		Comments: <input type="text" name="interview.commentsOther" size="50"
			value=""> <br />

		</td>
	</tr>

	<tr>
		<td colspan="2"><input type="submit" value="Save Interview" /></td>
	</tr>
</table>
</form>
</body>

</html>
