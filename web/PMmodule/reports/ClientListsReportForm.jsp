<%@page import="java.util.List" %>
<%@page import="org.oscarehr.PMmodule.model.*" %>
<%@ include file="/taglibs.jsp" %>

<div class="h4">
	<h5>Client Lists Report Criteria</h5>
</div>
<div class="axial">

	<html:form action="/PMmodule/Reports/ClientListsReport">
		<input type="hidden" name="method" value="report"/>

		<table border="0" cellspacing="2" cellpadding="3">
			<tr>
				<th>Status</th>
				<td>active/inactive/all (not yet implemented)</td>
			</tr>

			<tr>
				<th>Seen by provider</th>
				<td>
					<select name="form.providerId" >
					<%
						for (Provider provider : (List<Provider>)request.getAttribute("providers"))
						{
							%>
								<option value="<%=provider.getProviderNo()%>"><%=provider.getFormattedName()%></option>
							<%						
						}
					%>
					</select>
				</td>
			</tr>

			<tr>
				<th>Seen during time</th>
				<td>Start Date <input type="text" name="form.seenStartDate" size="15" /> (yyyy-mm-dd) - End Date <input type="text" name="form.seenEndDate" size="15" /> (yyyy-mm-dd)</td>
			</tr>

			<tr>
				<th>Enrolled in program</th>
				<td>
					<select name="form.programId" >
					<%
						for (Program program : (List<Program>)request.getAttribute("programs"))
						{
							%>
								<option value="<%=program.getId()%>"><%=program.getName()%> - <%=program.getDescr()%></option>
							<%						
						}
					%>
					</select>
				</td>
			</tr>

			<tr>
				<th>Enrolled during time</th>
				<td>Start Date <input type="text" name="form.enrolledStartDate" size="15" /> (yyyy-mm-dd) - End Date <input type="text" name="form.enrolledEndDate" size="15" /> (yyyy-mm-dd)</td>
			</tr>

			<tr>
				<th>Specific issues</th>
				<td>ICD-10 issues? (not yet implemented)</td>
			</tr>

			<tr>
				<td align="center" colspan="2">
					<html:submit value="Generate Report" />
		    	</td>
			</tr>
		</table>
		
	</html:form>
	
</div>
