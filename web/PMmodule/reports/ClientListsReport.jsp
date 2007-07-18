<%@page import="java.util.List" %>
<%@page import="org.oscarehr.PMmodule.model.*" %>
<%@ include file="/taglibs.jsp" %>

<div class="h4">
	<h5>Client Lists Report Results</h5>
</div>
<div class="axial">

	<html:form action="/PMmodule/Reports/ClientListsReport">

		<table border="0" cellspacing="2" cellpadding="3">
			<tr>
				<th>Last name, First name</th>				
				<th>Date of birth</th>				
				<th>Program</th>				
			</tr>
			<%
				List<Demographic> demographics=(List<Demographic>)request.getAttribute("demographics");
				for (Demographic demographic : demographics)
				{
					%>
						<tr>
							<td><%=demographic.getFormattedName()%></td>				
							<td><%=demographic.getFormattedDob()%></td>				
							<td>fake program placeholder</td>				
						</tr>
					<%
				}
			%>
		</table>
			
			<html:submit value="go back to form" />
		
	</html:form>
	
</div>
