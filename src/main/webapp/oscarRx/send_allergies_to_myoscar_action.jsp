<%@page import="org.oscarehr.common.service.myoscar.AllergiesManager"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%
	Integer demographicNo = null;

	try
	{
		String forceSend = request.getParameter("forceSend");
		String demographicNoString=request.getParameter("demographicId");
		demographicNo = Integer.parseInt(demographicNoString);
		
		String verificationLevel = AllergiesManager.getVerificationLevel(demographicNo);
		if ("+3".equals(verificationLevel) || "Yes".equals(forceSend)) {
			PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(session);
			AllergiesManager.sendAllergiesToMyOscar(auth, Integer.parseInt(demographicNoString));
			WebUtils.addInfoMessage(session, "ItemsHaveBeenSentToMyOscar");
			response.sendRedirect("ShowAllergies2.jsp");
		}
		else if ("No".equals(forceSend)) {
			response.sendRedirect("ShowAllergies2.jsp");
		}
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		String msg=LocaleUtils.getMessage(request, "UnexpectedError");
		WebUtils.addErrorMessage(session, msg);
		response.sendRedirect("ShowAllergies2.jsp");
	}
%>

<html>
<head>
	<title>Confirm Sending Allergies to MyOscar</title>
</head>
<body>
	<form action="send_allergies_to_myoscar_action.jsp" method="post">
		<h3>Warning!!</h3>
		<h3>This patient is not at +3 Level (in-person verification)</h3>
		<h3>Are you sure you want to send Allergies to this MyOscar account?</h3>
		<p>
		<input type="hidden" name="forceSend" value="No" />
		<input type="hidden" name="demographicId" value="<%=demographicNo%>" />
		<input type="button" value="Yes" onclick="forceSend.value='Yes';submit();" />
		<p>
		<input type="submit" value="No" />
		
	</form>
</body>
</html>