<%@page import="org.oscarehr.PMmodule.web.admin.IntegratorJspBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<%
	int facilityId=Integer.parseInt(request.getParameter("facilityId"));
	ArrayList<Properties> facilities=IntegratorJspBean.getIntegratorFacilityCommunity(facilityId);
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
		for (Properties p : facilities)
		{
			%>
				<tr style="border:solid black 2px;background:white;color:silver">
					<td style="border:solid black 1px"><%=p.get("name")%></td>
					<td style="border:solid black 1px"><%=p.get("description")%></td>
					<td style="border:solid black 1px"><%=p.get("contactName")%></td>
					<td style="border:solid black 1px"><%=p.get("contactEmail")%></td>
					<td style="border:solid black 1px"><%=p.get("contactPhone")%></td>
				</tr>
			<%
		}
	%>		
</table>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
