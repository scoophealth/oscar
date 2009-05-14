
<%@page import="org.oscarehr.PMmodule.web.ManageConsentAction"%><%@page import="org.oscarehr.util.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.model.Provider"%>

<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility facility= (Facility)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider provider=(Provider)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	String consent=request.getParameter("consent");
	String formLocation=request.getParameter("location");
	boolean refusedToSign=WebUtils.isChecked(request, "refusedToSign");

	ManageConsentAction manageConsentAction=new ManageConsentAction(demographicId, "FORM_B");	
	manageConsentAction.setConsent(consent, formLocation, refusedToSign);
	manageConsentAction.storeAllConsents();
		
	if ("true".equals(request.getParameter("gotoOptout")))
	{
		response.sendRedirect("complex_integrator_consent_b_optout.jsp?demographicId="+request.getParameter("demographicId"));
	}
	else
	{		
		response.sendRedirect("complex_integrator_consent_exit_interview_check.jsp?demographicId="+demographicId);
	}
%>
