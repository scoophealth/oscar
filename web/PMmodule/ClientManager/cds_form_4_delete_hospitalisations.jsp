<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%
	Integer hospitalisationId=Integer.parseInt(request.getParameter("hospitalisationId"));
	CdsForm4.deleteHospitalisationDay(hospitalisationId);

	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
%>