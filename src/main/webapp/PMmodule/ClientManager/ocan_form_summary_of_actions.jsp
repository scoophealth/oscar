<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	int ocanStaffFormId =0;
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	int prepopulate = 0;
	prepopulate = Integer.parseInt(request.getParameter("prepopulate")==null?"0":request.getParameter("prepopulate"));
	
	//OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel,ocanType);		
	OcanStaffForm ocanStaffForm = null;
	if(ocanStaffFormId != 0) {
		ocanStaffForm=OcanForm.getOcanStaffForm(Integer.valueOf(request.getParameter("ocanStaffFormId")));
	}else {
		ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId,prepopulationLevel,ocanType);
		
		if(ocanStaffForm.getAssessmentId()==null) {
			
			OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(currentDemographicId,ocanType);
			if(lastCompletedForm!=null) {
				
				// prepopulate the form from last completed assessment
				if(prepopulate==1) {			
						
					lastCompletedForm.setAssessmentId(null);
					lastCompletedForm.setAssessmentStatus("In Progress");
						
					ocanStaffForm = lastCompletedForm;
				}
			}
		}
	}
	int size=0;
	try {
		size = Integer.parseInt(request.getParameter("size"));
	}catch(NumberFormatException e){}
	
	String domains = request.getParameter("domains");

	String[] domainsAsArray = domains.split(",");
%>
<%if(size>0){  %>


								
<% for(int x=1;x<=size;x++) { %>
<div id="summary_of_actions_<%=x %>">
<table>		
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

</table></div>
<%}
}%>

</table>