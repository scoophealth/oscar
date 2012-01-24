<%@page import="org.oscarehr.common.service.myoscar.AllergiesManager"%>
<%@page import="org.oscarehr.common.service.myoscar.ImmunizationsManager"%>
<%@page import="org.oscarehr.common.service.myoscar.MeasurementsManager"%>
<%@page import="org.oscarehr.common.service.myoscar.PrescriptionMedicationManager"%>
<%@page import="org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%
	Integer demographicNo = null;
	String medicalDataType = request.getParameter("medicalDataType");
	String parentPage = request.getParameter("parentPage");

	try
	{
		String forceSend = request.getParameter("forceSend");
		String demographicNoString=request.getParameter("demographicId");
		demographicNo = Integer.parseInt(demographicNoString);
		
		String verificationLevel = MyOscarMedicalDataManagerUtils.getVerificationLevel(demographicNo);
		if ("+3".equals(verificationLevel) || "Yes".equals(forceSend)) {
			PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(session);
			
			if ("Allergies".equals(medicalDataType)) {
				AllergiesManager.sendAllergiesToMyOscar(auth, demographicNo);
			}
			else if ("Immunizations".equals(medicalDataType)) {
				ImmunizationsManager.sendImmunizationsToMyOscar(auth, demographicNo);
			}
			else if ("Measurements".equals(medicalDataType)) {
				MeasurementsManager.sendMeasurementsToMyOscar(auth, demographicNo);
			}
			else if ("Prescriptions".equals(medicalDataType)) {
				PrescriptionMedicationManager.sendPrescriptionsMedicationsToMyOscar(auth, demographicNo);
			}
			else {
				response.sendRedirect(parentPage);
			}
			
			WebUtils.addInfoMessage(session, "ItemsHaveBeenSentToMyOscar");
			response.sendRedirect(parentPage);
		}
		else if ("No".equals(forceSend)) {
			response.sendRedirect(parentPage);
		}
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		String msg=LocaleUtils.getMessage(request, "UnexpectedError");
		WebUtils.addErrorMessage(session, msg);
		response.sendRedirect(parentPage);
	}
%>

<html>
<head>
	<title>Confirm Sending <%=medicalDataType%> to MyOscar</title>
</head>
<body>
	<form action="<%=request.getRequestURI()%>" method="post">
		<h3>Warning!!</h3>
		<h3>This patient is not at +3 Level (in-person verification)</h3>
		<h3>Are you sure you want to send <%=medicalDataType%> to this MyOscar account?</h3>
		<p>
		<input type="hidden" name="forceSend" value="No" />
		<input type="hidden" name="demographicId" value="<%=demographicNo%>" />
		<input type="hidden" name="medicalDataType" value="<%=medicalDataType%>" />
		<input type="hidden" name="parentPage" value="<%=parentPage%>" />
		
		<input type="button" value="Yes" onclick="forceSend.value='Yes';submit();" />
		<p>
		<input type="submit" value="No" />
		
	</form>
</body>
</html>