<%@page import="org.oscarehr.caisi_integrator.ws.client.ConsentLevel"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.IntegratorConsentDao"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	String consentLevelString=request.getParameter("consentLevel");
    ConsentLevel consentLevel=ConsentLevel.valueOf(consentLevelString);
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
    	integratorConsent.setConsentLevel(consentLevel);
    	
    	integratorConsentDao.persist(integratorConsent);
    }
    else
    {
    	integratorConsent.setProvider_no(provider.getProvider_no());
    	integratorConsent.setConsentLevel(consentLevel);

    	integratorConsentDao.merge(integratorConsent);
    }
    
    response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+demographicId);
%>