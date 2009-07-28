<%@page import="org.oscarehr.util.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.model.Provider"%>

<%
	// this jsp should check to see if an exit interview is required or not
	// if it is, forward to exit interview
	// if not close window

	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	
	IntegratorConsentComplexExitInterviewDao integratorConsentComplexExitInterviewDao=(IntegratorConsentComplexExitInterviewDao)SpringUtils.getBean("integratorConsentComplexExitInterviewDao");
	FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(loggedInInfo.currentFacility.getId(),demographicId);
	IntegratorConsentComplexExitInterview integratorConsentComplexExitInterview=integratorConsentComplexExitInterviewDao.find(pk);
	if (integratorConsentComplexExitInterview==null)
	{
		response.sendRedirect("complex_integrator_consent_exit_interview.jsp?demographicId="+demographicId);
	}
	else
	{
		%>
			<script>
				window.opener.location=window.opener.location;
				window.close();
			</script>
		<%
	}
%>
