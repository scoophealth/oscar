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
		if(ocanStaffForm!=null) {
			ocanStaffFormId = ocanStaffForm.getId()==null?0:ocanStaffForm.getId().intValue();
		}
	}
	int medicationNumber = Integer.parseInt(request.getParameter("medication_num"));
%>
<div id="medication_<%=medicationNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Medication</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "medication_"+medicationNumber+"_medication", 128,prepopulationLevel)%>	
			</td>								
		</tr>
		<tr>
			<td class="genericTableHeader">Source of Information</td>
			<td class="genericTableData">
				<select name="medication_<%=medicationNumber%>_source_of_info">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "medication_"+medicationNumber+"_source_of_info", OcanForm.getOcanFormOptions("Source of Information"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Dosage</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "medication_"+medicationNumber+"_dosage", 128,prepopulationLevel)%>	
			</td>								
		</tr>
		<tr>
			<td class="genericTableHeader">Taken as prescribed?</td>
			<td class="genericTableData">
				<select name="medication_<%=medicationNumber%>_taken_as_prescribed">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "medication_"+medicationNumber+"_taken_as_prescribed", OcanForm.getOcanFormOptions("Taking as Prescribed"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Help is provided?</td>
			<td class="genericTableData">
				<select name="medication_<%=medicationNumber%>_help_provided">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "medication_"+medicationNumber+"_help_provided", OcanForm.getOcanFormOptions("Help Provided Needed"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Help is needed?</td>
			<td class="genericTableData">
				<select name="medication_<%=medicationNumber%>_help_needed">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "medication_"+medicationNumber+"_help_needed", OcanForm.getOcanFormOptions("Help Provided Needed"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
								
	</table>
</div>