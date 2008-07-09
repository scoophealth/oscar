<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
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
		<td style="border:solid black 1px">Name</td>
		<td style="border:solid black 1px">Description</td>
		<td style="border:solid black 1px">Contact Name</td>
		<td style="border:solid black 1px">Contact Email</td>
		<td style="border:solid black 1px">Contact Phone</td>
		<td style="border:solid black 1px">Last Updated</td>
	</tr>
	<%
		for (CachedFacilityInfo x : facilities)
		{
			%>
				<tr style="border:solid black 2px;background:white;color:gray">
					<td style="border:solid black 1px"><%=x.getName()%></td>
					<td style="border:solid black 1px"><%=x.getDescription()%></td>
					<td style="border:solid black 1px"><%=x.getContactName()%></td>
					<td style="border:solid black 1px"><%=x.getContactEmail()%></td>
					<td style="border:solid black 1px"><%=x.getContactPhone()%></td>
					<td style="border:solid black 1px"><%=DateFormatUtils.ISO_DATETIME_FORMAT.format(x.getLastUpdated().toGregorianCalendar())%></td>
				</tr>
			<%
		}
	%>		
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
