<%@page import="java.util.Calendar"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%
	Integer clientId=Integer.parseInt(request.getParameter("clientId"));
	String admissionString=request.getParameter("hospitalAdmission");
	String dischargeString=request.getParameter("hospitalDischarge");
	
	Calendar admissionDate=DateUtils.toGregorianCalendarDate(admissionString);
	Calendar dischargeDate=DateUtils.toGregorianCalendarDate(dischargeString);
	CdsForm4.addHospitalisationDay(clientId, admissionDate, dischargeDate);
	
	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
%>