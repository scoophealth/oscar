<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel,ocanType);		

	int referralNumber = Integer.parseInt(request.getParameter("referral_num"));
%>
<div id="referral_<%=referralNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Optimal Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_optimal">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_optimal", OcanForm.getOcanFormOptions("Action List"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_optimal_spec",128,prepopulationLevel)%>
			</td>
		</tr>		
		
		<tr>
			<td class="genericTableHeader">Actual Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_actual">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_actual", OcanForm.getOcanFormOptions("Action List"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Actual Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_actual_spec",128,prepopulationLevel)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Reason for Difference</td>
			<td class="genericTableData">
				<select name="1_summary_of_referral_diff">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_diff", OcanForm.getOcanFormOptions("Reason for Difference"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>						


		<tr>
			<td class="genericTableHeader">Referral Status</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_status", OcanForm.getOcanFormOptions("Referral Status"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>						
								
	</table>
</div>