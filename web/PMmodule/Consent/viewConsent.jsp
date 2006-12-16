
<%@ include file="/taglibs.jsp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>










<body topmargin="20" leftmargin="10">

<%=request.getParameter("clientName")%>
<font color="#FF0000" face="Arial"><b><font size="3">CONSENT A "OPT-IN":</font></b></font>
<table border="2" width="700" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%"><b><font color="#FFFFFF" face="Century Gothic"
			size="4">Consent Form</font></b>


		<p><b>Part 1: Consent information (to be read to client, hard copy
		will be offered to client; identical information to ?Consent B? and
		?Consent C? )</b></p>
		</td>
		</tr>
	<tr>
		<td width="100%"><i>
		<p>?CAISI is a community project.</p>
		<p>The purpose of this project is to help clients like yourself have
		control oveinstr your personal information at different agencies. This
		information will be stored on computers. With your permission and
		control, this information will be sent to other agencies when they
		care for you so they can provide you better care. These other agencies
		are:</p>
		<li>Hospitals.inst
		<li>Ambulance services.
		<li>Public health.
		<li>Outreach teams.
		<li>Shelters.
		<li>Mental health teams.
		<li>Related agencies that are CAISI partners.

		<p>In these agencies, your information will only be seen by those with
		the appropriate training. For example, only health care teams will be
		allowed to see detailed medical information.</p>
		<p>The purpose of this program is to send information to agencies when
		they care for you so they can provide better care for you. The program
		will also be used to improve community programs, to do research and to
		compile statistics.</p>
		<p>Your care at [Agency A] will not change by allowing agencies to
		send information and work together through CAISI.</p>
		<p>You may withdraw permission to send information to other agencies
		at any time. To withdraw, contact [Agency A] staff or any other CAISI
		partner.</p>
		<p>Any questions can be directed to [Agency A] staff.?</p>
		</i> 
		
		

			</td>
	</tr>
	<tr>
		<td width="100%"><p><b>Part 2: Testing of comprehension (questions to be
		asked and scored by [Agency A] staff, correct answers to all 3
		questions required to provide a consent/refusal for part 3, if less
		than all 3 questions correct do not proceed to part 3) </b>
		</p>
		</td>
	</tr>
	<tr>
		<td width="100%">
		<p>1.<i>What is the purpose of the CAISI project? </i><br>
		Correct</p>
		<p>[Correct includes any one of: to send information to agencies when
		they care for you; to provide better care for clients; to give clients
		control over their information or any combination of these]</p>
		<p>2.<i>When are you able to withdraw from CAISI?Correct </p>
		<p>[Correct includes: any time]</p>
		<p>3.<i>Will your care at [Agency A] be affected by your participation
		in CAISI? </i> 
		Correct [Correct = no]</p>
		</td>
	</tr>

	<tr>
		<td width="100%">
		<p><b>Part 3: Read the following prompt and record if the client
		agrees to consent to CAISI. Nothing needs to be recorded if the client
		refuses to consent. Do not read prompt if less than three correct
		answers were given for the questions in Part 2 (above). </b></p>
		</td>
	</tr>
	<tr>
		<td>
		<p><br>
		&nbsp&nbsp&nbsp I, <b><U><%=request.getParameter("clientName")%></U></b>,
		permit <b><U><%=session.getAttribute("userfirstname")%>,<%=session.getAttribute("userlastname")%></U></b>
		and any other CAISI partner agencies to record, send and use my
		personal information for the purposes above.</p>
		</td>
	</tr>

<tr><td> Signature:
<img src="../images/<c:out value='${requestScope.signature_file}'/>" />

</td>
</tr>

<tr>
<td align="right">
<input type="button" value=" Close " onclick="window.close()">
</td>
</tr>

</table>
</body>
</html>