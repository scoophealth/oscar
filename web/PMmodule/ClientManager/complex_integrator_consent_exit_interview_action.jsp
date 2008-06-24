<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.IntegratorConsentComplexExitInterviewDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.IntegratorConsentComplexExitInterview"%>
<%@page import="org.oscarehr.common.model.FacilityDemographicPrimaryKey"%>
<%@page import="org.apache.commons.lang.StringUtils"%><br />
<%
	int demographicId=Integer.parseInt(request.getParameter("demographicId"));
	Integer facilityId= (Integer)request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);

	IntegratorConsentComplexExitInterviewDao integratorConsentComplexExitInterviewDao=(IntegratorConsentComplexExitInterviewDao)SpringUtils.getBean("integratorConsentComplexExitInterviewDao");
	IntegratorConsentComplexExitInterview integratorConsentComplexExitInterview=new IntegratorConsentComplexExitInterview();
	
	FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(facilityId,demographicId);
	integratorConsentComplexExitInterview.setId(pk);
	
	String temp=null;
	
	integratorConsentComplexExitInterview.setAdditionalComments(request.getParameter("interview.commentsOther"));
	integratorConsentComplexExitInterview.setEducation(request.getParameter("interview.education"));
	integratorConsentComplexExitInterview.setMoreInfo(request.getParameter("interview.information"));
	integratorConsentComplexExitInterview.setMoreInfoComments(request.getParameter("interview.informationOther"));
	integratorConsentComplexExitInterview.setPressured(request.getParameter("interview.pressure"));
	integratorConsentComplexExitInterview.setPressuredComments(request.getParameter("interview.pressureOther"));

	temp=StringUtils.trimToNull(request.getParameter("interview.languageReadOther"));
	if (temp!=null) integratorConsentComplexExitInterview.setReadLanguage(temp);
	else integratorConsentComplexExitInterview.setReadLanguage(request.getParameter("interview.languageRead"));
	
	integratorConsentComplexExitInterview.setReAskConsent(request.getParameter("interview.followup"));
	integratorConsentComplexExitInterview.setReAskConsentComments(request.getParameter("interview.followupOther"));

	temp=StringUtils.trimToNull(request.getParameter("interview.languageOther"));
	if (temp!=null) integratorConsentComplexExitInterview.setSpokenLanguage(temp);
	else integratorConsentComplexExitInterview.setSpokenLanguage(request.getParameter("interview.language"));
	
	integratorConsentComplexExitInterview.setTimeToReviewConsent(request.getParameter("interview.review"));
	integratorConsentComplexExitInterview.setTimeToReviewConsentComments(request.getParameter("interview.reviewOther"));
	
	integratorConsentComplexExitInterviewDao.persist(integratorConsentComplexExitInterview);
%>
<script>
	window.close();
</script>
