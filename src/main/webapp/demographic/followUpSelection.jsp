<%@page import="org.oscarehr.caisi_integrator.ws.CachedProvider"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager"%>
<%@include file="/layouts/html_top.jspf" %>

<h3>Select a provider</h3>
<br />
<%
	String demographicId=request.getParameter("demographicId");

	List<CachedProvider> providers=CaisiIntegratorManager.getAllProviders();

	for (CachedProvider cachedProvider : providers)
	{
		%>
			<a href="followUp.jsp?demographicId=<%=demographicId%>&remoteFacilityId=<%=cachedProvider.getFacilityIdStringCompositePk().getIntegratorFacilityId()%>&remoteProviderId=<%=cachedProvider.getFacilityIdStringCompositePk().getCaisiItemId()%>" ><%=CaisiIntegratorManager.getRemoteFacility(cachedProvider.getFacilityIdStringCompositePk().getIntegratorFacilityId()).getName()%> : <%=cachedProvider.getLastName()+", "+cachedProvider.getFirstName()%></a>
			<br />
		<%
	}
%>

<%@include file="/layouts/html_bottom.jspf" %>