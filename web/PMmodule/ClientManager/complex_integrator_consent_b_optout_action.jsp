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
%>

<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Integer facilityId= (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	IntegratorConsent integratorConsent=integratorConsentDao.findByFacilityIdAndDemographicId(facilityId,demographicId);
	if (integratorConsent==null)
	{
		integratorConsent=new IntegratorConsent();
		
		integratorConsent.setFacilityId(facilityId);
		integratorConsent.setDemographicId(demographicId);
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
%>
<script>
window.opener.location.reload();
window.close();
</script>