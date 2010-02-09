<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	

	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId);		

	int referralNumber = Integer.parseInt(request.getParameter("referral_num"));
%>
<div id="referral_<%=referralNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Optimal Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_optimal">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_optimal", OcanForm.getOcanFormOptions("Action List"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_optimal_spec",5,30)%>
			</td>
		</tr>		
		
		<tr>
			<td class="genericTableHeader">Actual Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_actual">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_actual", OcanForm.getOcanFormOptions("Action List"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_actual_spec",5,30)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Reason for Difference</td>
			<td class="genericTableData">
				<select name="1_summary_of_referral_diff">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_diff", OcanForm.getOcanFormOptions("Reason for Difference"))%>
				</select>					
			</td>
		</tr>						


		<tr>
			<td class="genericTableHeader">Referral Status</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_status", OcanForm.getOcanFormOptions("Referral Status"))%>
				</select>					
			</td>
		</tr>						
								
	</table>
</div>