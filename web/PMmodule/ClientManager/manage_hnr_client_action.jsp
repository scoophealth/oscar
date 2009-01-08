<%@page import="org.oscarehr.util.SessionConstants"%>
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
	
	if ("copyLocalToHnr".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.copyLocalToHnr(currentFacility, currentProvider, currentDemographicId);
	}
	else if ("copyHnrToLocal".equals(request.getParameter("action")))
	{
		ManageHnrClientAction.copyHnrToLocal(currentFacility, currentProvider, currentDemographicId);
	}
	
	response.sendRedirect("manage_hnr_client.jsp?demographicId="+currentDemographicId);
%>