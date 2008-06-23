<%@page import="org.oscarehr.util.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>

<%!
	private static void fillConsentParameters(HttpServletRequest request, IntegratorConsent integratorConsent)
	{
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
	}

	private static void fillComplexConsentParameters(HttpServletRequest request, IntegratorConsentComplexForm integratorConsentComplexForm)
	{
		integratorConsentComplexForm.setForm("A");
		integratorConsentComplexForm.setPrintedFormLocation(request.getParameter("consent.location"));
		integratorConsentComplexForm.setRefusedToSign(WebUtils.isChecked(request, "consent.refusedToSign"));
	}
%>

<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Integer facilityId= (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(facilityId,demographicId);
	IntegratorConsent integratorConsent=integratorConsentDao.find(pk);
	if (integratorConsent==null)
	{
		integratorConsent=new IntegratorConsent();
		integratorConsent.setId(pk);
		integratorConsent.setProvider_no(provider.getProvider_no());
	
		fillConsentParameters(request, integratorConsent);
		
		integratorConsentDao.persist(integratorConsent);
	}
	else
	{
		integratorConsent.setProvider_no(provider.getProvider_no());
	
		fillConsentParameters(request, integratorConsent);
	
		integratorConsentDao.merge(integratorConsent);
	}
	
	
	IntegratorConsentComplexFormDao integratorConsentComplexFormDao=(IntegratorConsentComplexFormDao)SpringUtils.getBean("integratorConsentComplexFormDao");
	IntegratorConsentComplexForm integratorConsentComplexForm=integratorConsentComplexFormDao.find(pk);
	if (integratorConsentComplexForm==null)
	{
		integratorConsentComplexForm=new IntegratorConsentComplexForm();
		integratorConsentComplexForm.setId(pk);
		
		fillComplexConsentParameters(request, integratorConsentComplexForm);
		
		integratorConsentComplexFormDao.persist(integratorConsentComplexForm);
	}
	else
	{
		fillComplexConsentParameters(request, integratorConsentComplexForm);
		
		integratorConsentComplexFormDao.merge(integratorConsentComplexForm);
	}
	
	response.sendRedirect("complex_integrator_consent_exit_interview_check.jsp?demographicId="+demographicId);
%>
