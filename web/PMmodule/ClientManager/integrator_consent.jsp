<%@page import="org.oscarehr.common.model.IntegratorConsent"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<form action="integrator_consent_action.jsp" onsubmit="if (readToClient.checked) {return(true);} else {alert('You must read the statement to the client.');return(false);}" >
	<h3 style="display:inline">Please read the following to the client :</h3>
	<br />
	<ul>
		<li>The purpose of sharing data with other agencies is to provide better care.</li>
		<li>Your care will not change by allowing or dis-allowing agencies to share your information with other shelters. (Need to change this, it's not true)</li>
		<li>You may withdraw permission to send information to other agencies at any time.</li>
	</ul>
	<br />
	<h3 style="display:inline">I have read the above statement to the client : </h3><input type="checkbox" id="readToClient" />
	<br /><br />
	
	<h3 style="display:inline">Consent Level : </h3> 
	<select name="consentLevel">
		<%
			String currentConsentLevel=request.getParameter("consentLevel");
			for (IntegratorConsent.ConsentLevel consentLevel : IntegratorConsent.ConsentLevel.values())
			{
				String selected="";
				if (consentLevel.name().equals(currentConsentLevel)) selected="selected=\"selected\"";
				%>
					<option <%=selected%> value="<%=consentLevel.name()%>"><%=consentLevel.name()%></option>
				<%
			}
		%>
	</select>
	<br /><br />
	<input type="hidden" name="demographicId" value="<%=request.getParameter("demographicId")%>" />
	<input type="submit" value="save" /> &nbsp; <input type="button" value="Cancel" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
