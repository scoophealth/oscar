<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel);		

	int size=0;
	try {
		size = Integer.parseInt(request.getParameter("size"));
	}catch(NumberFormatException e){}
	
	String domains = request.getParameter("domains");

	String[] domainsAsArray = domains.split(",");
%>
<%if(size>0){  %>
<table>	
	<tr>
		<td width="10%">Priority</td>
		<td width="40%">Domain</td>
		<td width="50%">Action(s)</td>
	</tr>	
<%} %>
								
<% for(int x=1;x<=size;x++) { %>
			
<tr>
	<td>
		<input type="text" value="<%=x%>" readonly="readonly"/>					
	</td>
	<td>
		<select name="<%=x%>_summary_of_actions_domain" id="<%=x%>_summary_of_actions_domain" onchange="suaChangeDomain(this)">
					<option value=""></option>
					<%=OcanForm.renderAsDomainSelectOptions(ocanStaffForm.getId(), x+"_summary_of_actions_domain", OcanForm.getOcanFormOptions("Domain"),domainsAsArray, prepopulationLevel)%>
		</select>
	</td>
	<td>
		<%=OcanForm.renderAsSoATextArea(ocanStaffForm.getId(),x+"_summary_of_actions_action",5,50,prepopulationLevel)%>							
	</td>
</tr>		


<%}%>

</table>