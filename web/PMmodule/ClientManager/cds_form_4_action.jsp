<%
	Integer currentDemographicId=new Integer(request.getParameter("demographicId"));

	response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+currentDemographicId);
%>