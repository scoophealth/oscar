<%@page import="org.oscarehr.util.SessionConstants"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	

	response.sendRedirect("../ClientManager.do?id="+currentDemographicId);
%>