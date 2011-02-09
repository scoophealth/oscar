
<%@page import="org.oscarehr.util.WebUtils"%><%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Calendar"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%
	Integer clientId=Integer.parseInt(request.getParameter("clientId"));
	String admissionString=StringUtils.trimToNull(request.getParameter("hospitalAdmission"));
	String dischargeString=StringUtils.trimToNull(request.getParameter("hospitalDischarge"));
	
	Calendar admissionDate=DateUtils.toGregorianCalendarDate(admissionString);
	Calendar dischargeDate=DateUtils.toGregorianCalendarDate(dischargeString);
	CdsForm4.addHospitalisationDay(clientId, admissionDate, dischargeDate);
	
	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
%>