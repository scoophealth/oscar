<%@page import="java.util.List" %>
<%@ include file="/taglibs.jsp" %>

<div class="h4">
	<h5>Client Lists Report Criteria</h5>
</div>
<div class="axial">

	<html:form action="/PMmodule/Reports/ClientListsReport">
		<input type="hidden" name="method" value="report"/>

report criteria placeholder, not complete yet
		<table border="0" cellspacing="2" cellpadding="3">
			<tr>
				<th>Status</th>
				<td>active/inactive/all</td>
			</tr>

			<tr>
				<th>Seen by provider</th>
				<td>role drop down and provider drop down <%=((List)request.getAttribute("providers")).size()%></td>
			</tr>

			<tr>
				<th>Seen during time</th>
				<td>Start and End Date</td>
			</tr>

			<tr>
				<th>Enrolled in program</th>
				<td>program type drop down and program drop down</td>
			</tr>

			<tr>
				<th>Enrolled during time</th>
				<td>Start and End Date</td>
			</tr>

			<tr>
				<th>Specific issues</th>
				<td>ICD-10 issues?</td>
			</tr>

			<tr>
				<td align="center" colspan="2">
					<html:submit value="Generate Report" />
		    	</td>
			</tr>
		</table>
		
	</html:form>
	
</div>
