<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
%>

<h3>Integrator Linked Demographics</h3>
currentDemographicId=<%=currentDemographicId%><br />
currentFacilityId=<%=currentFacilityId%>

<hr />
- get possible matches
- get current matches
- display entire list
- check current matches
- on submit work out removals + additions, send removals and send links

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
