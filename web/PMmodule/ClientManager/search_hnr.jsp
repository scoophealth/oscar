<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("id"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
%>

<h2>Search HNR for Health Number</h2>

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
