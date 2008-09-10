<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.web.ManageIntegratorLinkedDemographics"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	ArrayList<ManageIntegratorLinkedDemographics.IntegratorLinkedDemographicHolder> demographicsToDisplay=ManageIntegratorLinkedDemographics.getDemographicsToDisplay(currentFacilityId, currentDemographicId);
%>


<h3>Integrator Linked Demographics</h3>
currentDemographicId=<%=currentDemographicId%><br />
currentFacilityId=<%=currentFacilityId%>

<hr />
results size : <%=demographicsToDisplay.size()%> 
<br />
<%
	for (ManageIntegratorLinkedDemographics.IntegratorLinkedDemographicHolder temp : demographicsToDisplay)
	{
		%>
			<%=temp.getMatchingScore() %> : <%=temp.isLinked() %> : <%=temp.getCachedDemographic().getFacilityIdIntegerCompositePk().getIntegratorFacilityId() %> : <%=temp.getCachedDemographic().getFacilityIdIntegerCompositePk().getCaisiItemId() %><br />
		<%
	}
%>
<hr />
- get possible matches
- get current matches
- display entire list
- check current matches
- on submit work out removals + additions, send removals and send links

<%@include file="/layouts/caisi_html_bottom2.jspf"%>
