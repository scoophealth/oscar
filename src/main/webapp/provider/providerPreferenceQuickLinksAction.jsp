<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%
   String action=request.getParameter("action");
	
	if ("add".equals(action))
	{
	   String name=request.getParameter("name");
	   String url=request.getParameter("url");
	   ProviderPreferencesUIBean.addQuickLink(name,url);
	}
	else if ("remove".equals(action))
	{
	   String name=request.getParameter("name");
	   ProviderPreferencesUIBean.removeQuickLink(name);		
	}
	else
	{
		MiscUtils.getLogger().error("Missing action case. action="+action);
		WebUtils.dumpParameters(request);
	}
	
	response.sendRedirect("providerpreference.jsp");
%>