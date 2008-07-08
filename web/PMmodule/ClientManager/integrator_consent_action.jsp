<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
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
	FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(facilityId,demographicId);
    IntegratorConsent integratorConsent=integratorConsentDao.find(pk);
    if (integratorConsent==null)
    {
    	integratorConsent=new IntegratorConsent();
    	integratorConsent.setId(pk);
    	integratorConsent.setProviderNo(provider.getProviderNo());

    	fillConsentParameters(request, integratorConsent);
    	
    	integratorConsentDao.persist(integratorConsent);
    }
    else
    {
    	integratorConsent.setProviderNo(provider.getProviderNo());

    	fillConsentParameters(request, integratorConsent);

    	integratorConsentDao.merge(integratorConsent);
    }
    
    response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+demographicId);
%>