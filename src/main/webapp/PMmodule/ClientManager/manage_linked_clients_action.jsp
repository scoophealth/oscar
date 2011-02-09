
<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClientsAction"%><%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.oscarehr.common.model.FacilityDemographicPrimaryKey"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility currentFacility=(Facility)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	ManageLinkedClientsAction manageLinkedClientsAction=new ManageLinkedClientsAction();

	@SuppressWarnings("unchecked")
	Enumeration<String> e=request.getParameterNames();
	while (e.hasMoreElements())
	{
		try
		{
			String key=e.nextElement();
			// must check for "on" because some versions of IE submit "off" or ""
			if (key.startsWith("linked.") && "on".equals(request.getParameter(key))) manageLinkedClientsAction.addLinkedId(key);
		}
		catch (Exception ex)
		{
			MiscUtils.getLogger().error("Error", ex);
		}
	}

	Provider provider=(Provider)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	manageLinkedClientsAction.saveLinkedIds(currentFacility, provider, currentDemographicId);

	response.sendRedirect("../ClientManager.do?id="+currentDemographicId);
%>