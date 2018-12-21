<%@page import="org.oscarehr.research.eaaps.EaapsPatientData" %><%


EaapsPatientData patientData = request.getSession().setAttribute("eaapsInfo");
if(patientData != null){	
	response.sendRedirect(patientData.getUrl());
	return
}


%>

Error No patient found.
