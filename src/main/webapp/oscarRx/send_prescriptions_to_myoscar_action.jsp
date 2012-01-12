<%@page import="org.oscarehr.common.service.myoscar.PrescriptionMedicationManager"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.phr.PHRAuthentication"%>
<%
	try
	{
		String demographicNoString=request.getParameter("demographicId");
		PHRAuthentication auth=MyOscarUtils.getPHRAuthentication(session);
		PrescriptionMedicationManager.sendPrescriptionsMedicationsToMyOscar(auth, Integer.parseInt(demographicNoString));
		WebUtils.addErrorMessage(session, "ItemsHaveBeenSentToMyOscar");
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		String msg=LocaleUtils.getMessage(request, "UnexpectedError");
		WebUtils.addErrorMessage(session, msg);
	}

	response.sendRedirect("SearchDrug3.jsp");
%>