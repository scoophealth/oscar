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
				<th>Start Date</th>
				<td></td>
			</tr>

			<tr>
				<td align="center" colspan="2">
					<html:submit value="Generate Report" />
		    	</td>
			</tr>
		</table>
		
	</html:form>
	
</div>
