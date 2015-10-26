<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.OcanStaffFormData"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.List"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	int referralNumber = Integer.parseInt(request.getParameter("referral_num"));
	String _summary_of_referral_optimal = "";
	String _summary_of_referral_optimal_spec = "";
	String _summary_of_referral_actual = "";
	String _summary_of_referral_actual_spec = "";
	String _summary_of_referral_diff = "";
	String _summary_of_referral_status = "";
	
	int ocanStaffFormId = 0;
	
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	//OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel,ocanType);		
	OcanStaffForm ocanStaffForm = null;
	if(ocanStaffFormId != 0) {
		ocanStaffForm=OcanForm.getOcanStaffForm(Integer.valueOf(request.getParameter("ocanStaffFormId")));
	}else {
		ocanStaffForm = OcanForm.getOcanStaffForm(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,prepopulationLevel,ocanType);		
		
		if(ocanStaffForm.getAssessmentId()==null) {
		//prepopulate referral from last completed assessment.
		OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(loggedInInfo.getCurrentFacility().getId(),currentDemographicId, ocanType);
		if(lastCompletedForm!=null) {
			List<OcanStaffFormData> existingAnswers1 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_optimal",prepopulationLevel);
			if(existingAnswers1.size()>0)
				_summary_of_referral_optimal = existingAnswers1.get(0).getAnswer();
			
			List<OcanStaffFormData> existingAnswers2 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_optimal_spec",prepopulationLevel);
			if(existingAnswers2.size()>0)
				_summary_of_referral_optimal_spec = existingAnswers2.get(0).getAnswer();
			
			List<OcanStaffFormData> existingAnswers3 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_actual",prepopulationLevel);
			if(existingAnswers3.size()>0)
				_summary_of_referral_actual = existingAnswers3.get(0).getAnswer();
			
			List<OcanStaffFormData> existingAnswers4 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_actual_spec",prepopulationLevel);
			if(existingAnswers4.size()>0)				
				_summary_of_referral_actual_spec = existingAnswers4.get(0).getAnswer();
			
			List<OcanStaffFormData> existingAnswers5 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_diff",prepopulationLevel);
			if(existingAnswers5.size()>0)
				_summary_of_referral_diff = existingAnswers5.get(0).getAnswer();
			
			List<OcanStaffFormData> existingAnswers6 = OcanForm.getStaffAnswers(lastCompletedForm.getId(),referralNumber+"_summary_of_referral_status",prepopulationLevel);
			if(existingAnswers6.size()>0)
				_summary_of_referral_status = existingAnswers6.get(0).getAnswer();
		}
		}
		
	}
	
%>
<div id="referral_<%=referralNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Optimal Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_optimal">				
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_optimal", OcanForm.getOcanFormOptions("Action List"),_summary_of_referral_optimal,prepopulationLevel)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_optimal_spec", _summary_of_referral_optimal_spec, 128, prepopulationLevel)%>
			</td>
		</tr>		
		
		<tr>
			<td class="genericTableHeader">Actual Referral</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_actual">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_actual", OcanForm.getOcanFormOptions("Action List"), _summary_of_referral_actual, prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Actual Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),referralNumber+"_summary_of_referral_actual_spec",_summary_of_referral_actual_spec,128,prepopulationLevel)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Reason for Difference</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_diff">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_diff", OcanForm.getOcanFormOptions("Reason for Difference"),_summary_of_referral_diff,prepopulationLevel)%>
				</select>					
			</td>
		</tr>						


		<tr>
			<td class="genericTableHeader">Referral Status</td>
			<td class="genericTableData">
				<select name="<%=referralNumber%>_summary_of_referral_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), referralNumber+"_summary_of_referral_status", OcanForm.getOcanFormOptions("Referral Status"),_summary_of_referral_status,prepopulationLevel)%>
				</select>					
			</td>
		</tr>						
								
	</table>
</div>
