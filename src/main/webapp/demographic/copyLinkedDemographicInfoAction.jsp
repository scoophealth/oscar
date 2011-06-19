<%@page import="org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper"%>
<%
	Integer demographicId=Integer.parseInt(request.getParameter("demographicId"));

	ConformanceTestHelper.copyLinkedDemographicsPropertiesToLocal(demographicId);
	
	response.sendRedirect("demographiccontrol.jsp?"+request.getQueryString());
%>