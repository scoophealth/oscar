
<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.PMmodule.web.ManageConsentAction"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SessionConstants"%>

<%
	Integer currentDemographicId=new Integer(request.getParameter("demographicId"));
	ManageConsentAction manageConsentAction=new ManageConsentAction(currentDemographicId);
	manageConsentAction.setSignatureRequestId(request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY));
	manageConsentAction.setExcludeMentalHealthData(WebUtils.isChecked(request, "excludeMentalHealth"));
	manageConsentAction.setConsentStatus(request.getParameter("consentStatus"));
	manageConsentAction.setSignatureStatus(request.getParameter("signatureStatus"));
	manageConsentAction.setExpiry(request.getParameter("consentExpiry"));
	
	@SuppressWarnings("unchecked")
	Enumeration<String> e=request.getParameterNames();
	while (e.hasMoreElements())
	{
		String key=e.nextElement();
		// must check for "on" because some versions of IE submit "off" or ""
		if (key.startsWith("consent.") && "on".equals(request.getParameter(key))) manageConsentAction.addExclude(key);
	}
	
	manageConsentAction.storeAllConsents();
    response.sendRedirect(request.getContextPath()+"/PMmodule/ClientManager.do?id="+currentDemographicId);
%>