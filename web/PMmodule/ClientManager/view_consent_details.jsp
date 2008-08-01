<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%
	int consentId=Integer.parseInt(request.getParameter("consentId"));
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	IntegratorConsent integratorConsent=integratorConsentDao.find(consentId);
	
	if ("FORM_A".equals(integratorConsent.getFormVersion()))
	{
		response.sendRedirect("complex_integrator_consent_a.jsp?consentId="+consentId+"&demographicId="+demographicId);		
	}
	if ("FORM_B".equals(integratorConsent.getFormVersion()))
	{
		response.sendRedirect("complex_integrator_consent_b.jsp?consentId="+consentId+"&demographicId="+demographicId);		
	}
	if ("FORM_B_OPTOUT".equals(integratorConsent.getFormVersion()))
	{
		response.sendRedirect("complex_integrator_consent_b_optout.jsp?consentId="+consentId+"&demographicId="+demographicId);		
	}
%>