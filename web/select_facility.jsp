<%@page import="org.oscarehr.PMmodule.dao.FacilityDAO"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.PMmodule.model.Facility"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.PMmodule.model.Provider"%>
<%@include file="/layouts/caisi_html_top.jspf"%>

<h2>Please select which facility you would like to currently work in</h2> 
<%
	FacilityDAO facilityDAO=(FacilityDAO)SpringUtils.beanFactory.getBean("facilityDAO");

	Provider provider=(Provider)session.getAttribute("provider");
	List<Integer> facilityIds=ProviderDao.getFacilityIds(provider.getProvider_no());
%>
<ul>
	<%
		for (int facilityId : facilityIds)
		{
			Facility facility=facilityDAO.getFacility(facilityId);
			%>
				<li><a href='?nextPage=<%=request.getParameter("nextPage")%>&currentFacilityId=<%=facility.getId()%>'><%=facility.getName()%></a></li>
			<%
		}
	%>
</ul>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
