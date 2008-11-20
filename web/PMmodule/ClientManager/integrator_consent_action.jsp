<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Integer facilityId= (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
    Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);

    IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
    IntegratorConsent integratorConsent=new IntegratorConsent();
	integratorConsent.setFacilityId(facilityId);
	integratorConsent.setDemographicId(demographicId);
   	integratorConsent.setProviderNo(provider.getProviderNo());

	integratorConsent.setConsentToStatistics(WebUtils.isChecked(request, "Statistics"));
	integratorConsent.setConsentToBasicPersonalId(WebUtils.isChecked(request, "BasicPersonalId"));
	integratorConsent.setConsentToHealthCardId(WebUtils.isChecked(request, "HealthCardId"));
	integratorConsent.setConsentToIssues(WebUtils.isChecked(request, "Issues"));
	integratorConsent.setConsentToNotes(WebUtils.isChecked(request, "Notes"));
	integratorConsent.setConsentToPreventions(WebUtils.isChecked(request, "Preventions"));
	integratorConsent.setConsentToPhoto(WebUtils.isChecked(request, "Photo"));
	integratorConsent.setRestrictConsentToHic(WebUtils.isChecked(request, "RestrictToHic"));
   	
	integratorConsent.setFormVersion("QUICK");

   	integratorConsentDao.persist(integratorConsent);
        
    response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+demographicId);
%>