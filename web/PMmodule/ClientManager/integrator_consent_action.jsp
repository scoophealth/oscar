<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.IntegratorConsentDao"%>
<%@page import="org.oscarehr.common.model.IntegratorConsent"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%!
	private boolean isChecked(HttpServletRequest request, String parameter)
	{
		String temp=request.getParameter(parameter);
		return(temp!=null && (temp.equalsIgnoreCase("on") || temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("checked")));
	}

	private void fillConsentParameters(HttpServletRequest request, IntegratorConsent integratorConsent)
	{
		integratorConsent.setConsentToStatistics(isChecked(request, "Statistics"));
		integratorConsent.setConsentToBasicPersonalId(isChecked(request, "BasicPersonalId"));
		integratorConsent.setConsentToHealthCardId(isChecked(request, "HealthCardId"));
		integratorConsent.setConsentToIssues(isChecked(request, "Issues"));
		integratorConsent.setConsentToNotes(isChecked(request, "Notes"));
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