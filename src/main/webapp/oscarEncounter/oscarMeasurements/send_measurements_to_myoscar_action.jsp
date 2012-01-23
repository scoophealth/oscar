<%@page import="org.oscarehr.common.service.myoscar.MeasurementsManager"%>
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
		
		String verificationLevel = MeasurementsManager.getVerificationLevel(demographicNo);
		if ("+3".equals(verificationLevel) || "Yes".equals(forceSend)) {
			PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(session);
			MeasurementsManager.sendMeasurementsToMyOscar(auth, Integer.parseInt(demographicNoString));
			WebUtils.addInfoMessage(session, "ItemsHaveBeenSentToMyOscar");
			response.sendRedirect("HistoryIndex.jsp");
		}
		else if ("No".equals(forceSend)) {
			response.sendRedirect("HistoryIndex.jsp");
		}
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		String msg=LocaleUtils.getMessage(request, "UnexpectedError");
		WebUtils.addErrorMessage(session, msg);
		response.sendRedirect("HistoryIndex.jsp");
	}
%>

<html>
<head>
	<title>Confirm Sending Measurements to MyOscar</title>
</head>
<body>
	<form action="send_measurements_to_myoscar_action.jsp" method="post">
		<h3>Warning!!</h3>
		<h3>This patient is not at +3 Level (in-person verification)</h3>
		<h3>Are you sure you want to send Measurements to this MyOscar account?</h3>
		<p>
		<input type="hidden" name="forceSend" value="No" />
		<input type="hidden" name="demographicId" value="<%=demographicNo%>" />
		<input type="button" value="Yes" onclick="forceSend.value='Yes';submit();" />
		<p>
		<input type="submit" value="No" />
		
	</form>
</body>
</html>