<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.model.CdsHospitalisationDays"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>

<%
	Integer clientId=Integer.parseInt(request.getParameter("clientId"));
	List<CdsHospitalisationDays> hospitalisationDays=CdsForm4.getHospitalisationDays(clientId);
%>

<table style="border-collapse:collapse;text-align:center;font-family:monospace">
	<tr>
		<td class="genericTableHeader" style="border:solid black 1px">Admission</td>
		<td class="genericTableHeader" style="border:solid black 1px">Discharge</td>
		<td class="genericTableHeader" style="border:solid black 1px"></td>
	</tr>
	<%
		for (CdsHospitalisationDays entry : hospitalisationDays)
		{
			%>
				<tr>
					<td style="border:solid black 1px"><%=DateFormatUtils.ISO_DATE_FORMAT.format(entry.getAdmitted())%></td>
					<td style="border:solid black 1px"><%=entry.getDischarged()!=null?DateFormatUtils.ISO_DATE_FORMAT.format(entry.getDischarged()):"-"%></td>
					<td style="border:solid black 1px"><image src="<%=request.getContextPath()%>/images/delete.png" onclick="deleteHospitalisationDay(<%=entry.getId()%>)" /></td>
				</tr>
			<%
		}
	%>
</table>
