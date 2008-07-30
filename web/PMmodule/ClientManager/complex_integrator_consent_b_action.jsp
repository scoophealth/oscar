<%@page import="org.oscarehr.util.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>

<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Integer facilityId= (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	
	IntegratorConsent integratorConsent=new IntegratorConsent();
	
	integratorConsent.setProviderNo(provider.getProviderNo());
	integratorConsent.setFacilityId(facilityId);
	integratorConsent.setDemographicId(demographicId);
	
	String consent=request.getParameter("consent");
	if ("ALL".equals(consent))
	{
		integratorConsent.setConsentToBasicPersonalId(true);
		integratorConsent.setConsentToHealthCardId(true);
		integratorConsent.setConsentToIssues(true);
		integratorConsent.setConsentToNotes(true);
		integratorConsent.setConsentToStatistics(true);
		integratorConsent.setRestrictConsentToHic(false);
	}
	else if ("HIC_ALL".equals(consent))
	{
		integratorConsent.setConsentToBasicPersonalId(true);
		integratorConsent.setConsentToHealthCardId(true);
		integratorConsent.setConsentToIssues(true);
		integratorConsent.setConsentToNotes(true);
		integratorConsent.setConsentToStatistics(true);
		integratorConsent.setRestrictConsentToHic(true);
	}
	else if ("NONE".equals(consent))
	{
		integratorConsent.setConsentToBasicPersonalId(false);
		integratorConsent.setConsentToHealthCardId(false);
		integratorConsent.setConsentToIssues(false);
		integratorConsent.setConsentToNotes(false);
		integratorConsent.setConsentToStatistics(false);
		integratorConsent.setRestrictConsentToHic(false);
	}
	else
	{
		System.err.println("Error, missing consent option. option="+consent);
	}
	
	integratorConsent.setFormVersion("FORM_A");
	integratorConsent.setPrintedFormLocation(request.getParameter("consent.location"));
	integratorConsent.setRefusedToSign(WebUtils.isChecked(request, "consent.refusedToSign"));		

	integratorConsentDao.persist(integratorConsent);
		
	if ("true".equals(request.getParameter("gotoOptout")))
	{
		response.sendRedirect("complex_integrator_consent_b_optout.jsp?demographicId="+request.getParameter("demographicId"));
	}
	else
	{		
		response.sendRedirect("complex_integrator_consent_exit_interview_check.jsp?demographicId="+demographicId);
	}
%>
