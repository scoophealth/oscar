<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients.LinkedDemographicHolder"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.DemographicWs"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedDemographic"%>
<%@page import="org.oscarehr.PMmodule.web.ManageLinkedClients"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacility"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	DemographicDao demographicDao=(DemographicDao)SpringUtils.getBean("demographicDao");
	CaisiIntegratorManager caisiIntegratorManager=(CaisiIntegratorManager)SpringUtils.getBean("caisiIntegratorManager");

	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	Facility currentFacility = (Facility) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY);
	Provider currentProvider = (Provider) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	
	Demographic demographic=demographicDao.getDemographicById(currentDemographicId);
%>


<h3>Manage Linked Clients</h3>

STUBBED PAGE

<input type="button" value="cancel" onclick="history.go(-1)" />
</form>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
