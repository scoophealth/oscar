<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.IntegratorConsentDao"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%!
	private void fillConsentParameters(HttpServletRequest request, IntegratorConsent integratorConsent)
	{
		integratorConsent.setConsentToStatistics(WebUtils.isChecked(request, "Statistics"));
		integratorConsent.setConsentToBasicPersonalId(WebUtils.isChecked(request, "BasicPersonalId"));
		integratorConsent.setConsentToHealthCardId(WebUtils.isChecked(request, "HealthCardId"));
		integratorConsent.setConsentToIssues(WebUtils.isChecked(request, "Issues"));
		integratorConsent.setConsentToNotes(WebUtils.isChecked(request, "Notes"));
		integratorConsent.setRestrictConsentToHic(WebUtils.isChecked(request, "RestrictToHic"));
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
    
    response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+demographicId);
%>