<%@page import="java.util.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.apache.commons.lang.time.*"%>
<%@ include file="/taglibs.jsp"%>

<div class="h4">
<h5>Client Lists Report Results</h5>
</div>
<div class="axial"><html:form
	action="/PMmodule/Reports/ClientListsReport">

	<table border="0" cellspacing="2" cellpadding="3">
		<tr>
			<th>Last name, First name</th>
			<th>Date of birth</th>
			<th>Program</th>
		</tr>
		<%
				Map<String, ClientDao.ClientListsReportResults> reportResults=(Map<String, ClientDao.ClientListsReportResults>)request.getAttribute("reportResults");
				for (ClientDao.ClientListsReportResults clientListsReportResults : reportResults.values())
				{
					%>
		<tr>
			<td><%=clientListsReportResults.lastName+", "+clientListsReportResults.firstName%></td>
			<td><%=DateFormatUtils.ISO_DATE_FORMAT.format(clientListsReportResults.dateOfBirth)%></td>
			<td><%=clientListsReportResults.programName%></td>
		</tr>
		<%
				}
			%>
	</table>

	<html:submit value="go back to form" />

</html:form></div>
