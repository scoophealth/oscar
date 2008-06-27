<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	IntegratorConsentDao integratorConsentDao=(IntegratorConsentDao)SpringUtils.getBean("integratorConsentDao");
	FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey(currentFacilityId, currentDemographicId);
	IntegratorConsent integratorConsent=integratorConsentDao.find(pk);
%>

<form action="integrator_consent_action.jsp" onsubmit="if (readToClient.checked) {return(true);} else {alert('You must read the statement to the client.');return(false);}" >
	<h3 style="display:inline">Please read the following to the client :</h3>
	<br />
	<ul>
		<li>The purpose of sharing data with other agencies is to provide better care.</li>
		<li>Services provided to you will not change by allowing or dis-allowing agencies to share your information with other shelters.</li>
		<li>You may withdraw permission to share information with other agencies at any time.</li>
	</ul>
	<br />
	<h3 style="display:inline">I have read the above statement to the client and have acquired their consent : </h3><input type="checkbox" id="readToClient" />
	<br /><br />
	
	<h3 style="display:inline">Consent to share : </h3>
	<div style="text-align:right;width:175px">
	Statistics <input type="checkbox" name="Statistics" <%=integratorConsent!=null&&integratorConsent.isConsentToStatistics()?"checked=\"checked\"":""%>/><br />
	BasicPersonalId <input type="checkbox" name="BasicPersonalId" <%=integratorConsent!=null&&integratorConsent.isConsentToBasicPersonalId()?"checked=\"checked\"":""%>/><br />
	HealthCardId <input type="checkbox" name="HealthCardId" <%=integratorConsent!=null&&integratorConsent.isConsentToHealthCardId()?"checked=\"checked\"":""%>/><br />
	Issues <input type="checkbox" name="Issues" <%=integratorConsent!=null&&integratorConsent.isConsentToIssues()?"checked=\"checked\"":""%>/><br />
	Notes <input type="checkbox" name="Notes" <%=integratorConsent!=null&&integratorConsent.isConsentToNotes()?"checked=\"checked\"":""%>/><br />
	<br />
	Restrict Consent To HIC's <input type="checkbox" name="RestrictToHic" <%=integratorConsent!=null&&integratorConsent.isRestrictConsentToHic()?"checked=\"checked\"":""%>/><br />
	</div>
	<br /><br />
	<input type="hidden" name="demographicId" value="<%=currentDemographicId%>" />
	<input type="submit" value="save" /> &nbsp; <input type="button" value="Cancel" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ClientManager.do?id=<%=currentDemographicId%>'"/>
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
