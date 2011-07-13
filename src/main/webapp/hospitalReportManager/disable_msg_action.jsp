<%@page import="org.oscarehr.hospitalReportManager.SFTPConnector"%>
<%
	SFTPConnector.addMeToDoNotSendList();
	response.sendRedirect("hospitalReportManager.jsp");
%>