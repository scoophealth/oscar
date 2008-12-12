
<%@page import="org.oscarehr.common.model.Provider"%><%@page
	import="java.util.Arrays"%>
<%@page import="org.oscarehr.common.model.FacilityDemographicPrimaryKey"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Enumeration"%>
<%@page
	import="org.oscarehr.PMmodule.web.ManageIntegratorLinkedDemographics"%><%@page
	import="org.oscarehr.util.SessionConstants"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	int currentFacilityId = (Integer) request.getSession().getAttribute(SessionConstants.CURRENT_FACILITY_ID);
	
	HashSet<FacilityDemographicPrimaryKey> linkedIds=new HashSet<FacilityDemographicPrimaryKey>();
	
	@SuppressWarnings("unchecked")
	Enumeration<String> e=request.getParameterNames();
	while (e.hasMoreElements())
	{
		try
		{
			String key=e.nextElement();
			if (key.startsWith("linked."))
			{
				String[] keySplit=key.split("\\.");
				
				FacilityDemographicPrimaryKey pk=new FacilityDemographicPrimaryKey();
				pk.setFacilityId(Integer.parseInt(keySplit[1]));
				pk.setDemographicId(Integer.parseInt(keySplit[2]));
				linkedIds.add(pk);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	Provider provider=(Provider)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
	ManageIntegratorLinkedDemographics.saveLinkedClients(currentFacilityId, provider.getProviderNo(), currentDemographicId, linkedIds);

	response.sendRedirect("../ClientManager.do?id="+currentDemographicId);
%>