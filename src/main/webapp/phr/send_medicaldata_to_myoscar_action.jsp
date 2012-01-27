<%@page import="org.oscarehr.common.service.myoscar.AllergiesManager"%>
<%@page import="org.oscarehr.common.service.myoscar.ImmunizationsManager"%>
<%@page import="org.oscarehr.common.service.myoscar.MeasurementsManager"%>
<%@page import="org.oscarehr.common.service.myoscar.PrescriptionMedicationManager"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%
	String parentPage = request.getParameter("parentPage");

	try
	{
		Integer demographicNo = Integer.parseInt(request.getParameter("demographicId"));
		String medicalDataType = request.getParameter("medicalDataType");
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
		
		WebUtils.addInfoMessage(session, LocaleUtils.getMessage(request,"ItemsHaveBeenSentToMyOscar"));
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		WebUtils.addErrorMessage(session, LocaleUtils.getMessage(request,"UnexpectedError"));
	}
	
	response.sendRedirect(parentPage);
%>
