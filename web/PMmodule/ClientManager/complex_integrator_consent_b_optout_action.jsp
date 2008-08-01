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
		integratorConsent.setConsentToAll();
	}
	else if ("HIC_ALL".equals(consent))
	{
		integratorConsent.setConsentToAll();
		integratorConsent.setRestrictConsentToHic(true);
	}
	else if ("NONE".equals(consent))
	{
		integratorConsent.setConsentToNone();
	}
	else
	{
		System.err.println("Error, missing consent option. option="+consent);
	}
	
	integratorConsent.setFormVersion("FORM_B_OPTOUT");
	integratorConsent.setPrintedFormLocation(request.getParameter("consent.location"));
	integratorConsent.setRefusedToSign(WebUtils.isChecked(request, "consent.refusedToSign"));		

	integratorConsentDao.persist(integratorConsent);
			
	response.sendRedirect("complex_integrator_consent_exit_interview_check.jsp?demographicId="+demographicId);
%>
