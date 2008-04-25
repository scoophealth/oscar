<%@page import="org.oscarehr.PMmodule.web.admin.IntegratorJspBean"%>
<%@page import="org.oscarehr.caisi_integrator.ws.client.CachedFacilityInfo"%>
<%@page import="java.util.List"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	int facilityId=Integer.parseInt(request.getParameter("facilityId"));
	List<CachedFacilityInfo> facilities=IntegratorJspBean.getIntegratorFacilityCommunity(facilityId);
%>

<h3>Community of integrated facilities</h3>

<table style="border-collapse:collapse">
	<tr style="border:solid black 2px; background:silver">
		<td style="border:solid black 1px">name</td>
		<td style="border:solid black 1px">description</td>
		<td style="border:solid black 1px">contactName</td>
		<td style="border:solid black 1px">contactEmail</td>
		<td style="border:solid black 1px">contactPhone</td>
	</tr>
	<%
		for (CachedFacilityInfo x : facilities)
		{
			%>
				<tr style="border:solid black 2px;background:white;color:silver">
					<td style="border:solid black 1px"><%=x.getName()%></td>
					<td style="border:solid black 1px"><%=x.getDescription()%></td>
					<td style="border:solid black 1px"><%=x.getContactName()%></td>
					<td style="border:solid black 1px"><%=x.getContactEmail()%></td>
					<td style="border:solid black 1px"><%=x.getContactPhone()%></td>
				</tr>
			<%
		}
	%>		
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
