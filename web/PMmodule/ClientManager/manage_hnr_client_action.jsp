
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DuplicateHinExceptionException"%>
<%@page import="org.apache.log4j.LogManager"%><%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.PMmodule.web.ManageHnrClientAction"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility currentFacility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider currentProvider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	if ("copyLocalValidatedToHnr".equals(request.getParameter("action")))
	{
		try
		{
			ManageHnrClientAction.copyLocalValidatedToHnr(currentFacility, currentProvider, currentDemographicId);
		}
		catch (DuplicateHinExceptionException e)
		{
			WebUtils.addErrorMessage(session, "This HIN is already in use in the HNR, please link to that individual.");
		}
	}
	else if ("copyHnrToLocal".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.copyHnrToLocal(currentFacility, currentProvider, currentDemographicId);
	}
	else if ("validatePicture".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setPictureValidation(currentFacility, currentProvider, currentDemographicId, true);
	}
	else if ("invalidatePicture".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setPictureValidation(currentFacility, currentProvider, currentDemographicId, false);
	}
	else if ("validateHcInfo".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setHcInfoValidation(currentFacility, currentProvider, currentDemographicId, true);
	}
	else if ("invalidateHcInfo".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setHcInfoValidation(currentFacility, currentProvider, currentDemographicId, false);
	}
	else if ("validateOtherInfo".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setOtherInfoValidation(currentFacility, currentProvider, currentDemographicId, true);
	}
	else if ("invalidateOtherInfo".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.setOtherInfoValidation(currentFacility, currentProvider, currentDemographicId, false);
	}
	else
	{
		LogManager.getLogger(this.getClass()).error("Unexpected action. qs="+request.getQueryString());
	}
	
	response.sendRedirect("manage_hnr_client.jsp?demographicId="+currentDemographicId);
%>