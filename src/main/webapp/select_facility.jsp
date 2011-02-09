<%@page import="org.oscarehr.common.dao.FacilityDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Facility"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.Provider"%>

<%@include file="/layouts/caisi_html_top.jspf"%>


<%@page import="oscar.login.LoginAction"%><h2>Please select which facility you would like to currently work in</h2> 
<%
	FacilityDao facilityDao=(FacilityDao)SpringUtils.beanFactory.getBean("facilityDao");

	Provider provider=(Provider)session.getAttribute("provider");
	List<Integer> facilityIds=ProviderDao.getFacilityIds(provider.getProviderNo());
%>
<ul>
	<%
		for (Integer facilityId : facilityIds)
		{
			Facility facility=facilityDao.find(facilityId);
			%>
				<li><a href='?nextPage=<%=request.getParameter("nextPage")%>&<%=LoginAction.SELECTED_FACILITY_ID%>=<%=facility.getId()%>'><%=facility.getName()%></a></li>
			<%
		}
	%>
</ul>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
