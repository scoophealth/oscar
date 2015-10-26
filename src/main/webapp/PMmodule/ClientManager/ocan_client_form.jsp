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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed2=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed2=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed2) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@include file="/layouts/caisi_html_top-jquery.jspf"%>


<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	String ocanType = request.getParameter("ocanType");
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
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
		ocanStaffForm=OcanForm.getOcanStaffForm(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,prepopulationLevel,ocanType);	
		
		if(ocanStaffForm.getAssessmentId()==null) {
			
			OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,ocanType);
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
	boolean printOnly=request.getParameter("print")!=null;
	if (printOnly) request.setAttribute("noMenus", true);
%>

<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/PMmodule/ClientManager/ocan_staff_form_validation.js"></script>
-->	

<script>
//setup validation plugin
$("document").ready(function() {	
	$("#ocan_client_form").validate({meta: "validate"});	
});


function submitOcanClientForm() {
	var status = document.getElementById('assessment_status').value;
	if(status == 'Active') {
		$('#ocan_client_form').unbind('submit').submit();		
		return true;
	}
	if(!$("#ocan_client_form").valid()) {
		alert('Validation failed. Please check all required fields highlighted');
		return false;
	}
	return true;
}

</script>


<style>
.error {color:red;}
</style>

<form id="ocan_client_form" name="ocan_client_form" action="ocan_client_form_action.jsp" method="post" onsubmit="return submitOcanClientForm()">

	<input type="hidden" id="assessment_status" name="assessment_status" />
	<input type="hidden" id="startDate" name="startDate" value="<%=ocanStaffForm.getFormattedStartDate()%>"/>
	<input type="hidden" id="completionDate" name="completionDate" value="<%=ocanStaffForm.getFormattedCompletionDate()%>"/>
	<input type="hidden" id="date_of_birth" name="date_of_birth" value="<%=ocanStaffForm.getDateOfBirth()%>"/>
	<input type="hidden" id="addressLine1" name="addressLine1" value="<%=ocanStaffForm.getAddressLine1()%>"/>
	<input type="hidden" id="addressLine2" name="addressLine2" value="<%=ocanStaffForm.getAddressLine2()%>"/>
	<input type="hidden" id="city" name="city" value="<%=ocanStaffForm.getCity()%>"/>
	<input type="hidden" id="province" name="province" value="<%=ocanStaffForm.getProvince()%>"/>
	<input type="hidden" id="postalCode" name="postalCode" value="<%=ocanStaffForm.getPostalCode()%>"/>
	<input type="hidden" id="city" name="city" value="<%=ocanStaffForm.getCity()%>"/>
	<input type="hidden" id="phoneNumber" name="phoneNumber" value="<%=ocanStaffForm.getPhoneNumber()%>"/>
	<input type="hidden" id="email" name="email" value="<%=ocanStaffForm.getEmail()%>"/>
	<input type="hidden" id="hcNumber" name="hcNumber" value="<%=ocanStaffForm.getHcNumber()%>"/>	
	<input type="hidden" id="hcVersion" name="hcVersion" value="<%=ocanStaffForm.getHcVersion()%>"/>
	<input type="hidden" id="gender" name="gender" value="<%=ocanStaffForm.getGender()%>"/>
	
<% if(prepopulate==1) { %> 
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="" />
<% } else { %>
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="<%=ocanStaffForm.getId()%>" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="<%=ocanStaffForm.getAssessmentId()%>" />

<%} %>	
	<h3>OCAN Consumer Self-Assessment (v2.0)</h3>

	<br />
	
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">

		<tr>
			<td class="genericTableHeader">Start Date</td>
			<td class="genericTableData">
				<input id="clientStartDate" name="clientStartDate" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" type="text" value="<%=ocanStaffForm.getFormattedClientStartDate()%>"> <img title="Calendar" id="cal_startDate" src="../../images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'clientStartDate',ifFormat :'%Y-%m-%d',button :'cal_startDate',align :'cr',singleClick :true,firstDay :1});</script>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableData">
				<input type="text" name="lastName" id="lastName" value="<%=ocanStaffForm.getLastName()%>" class="{validate: {required:true}}"/>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableData">
				<input type="text" name="firstName" id="firstName" value="<%=ocanStaffForm.getFirstName()%>" class="{validate: {required:true}}"/>
			</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">Date of Birth</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<input type="text" name="client_date_of_birth" id="client_date_of_birth" value="<%=ocanStaffForm.getClientDateOfBirth()%>" class="{validate: {required:true}}"/>
			</td>
		</tr>

		<tr>
			<td colspan="2">1. Accommodation</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. What kind of place do you live in? </td>
			<td class="genericTableData">
				<select name="client_1_1" id="client_1_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_1_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_1_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

		<tr>
			<td colspan="2">2. Food</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you get enough to eat? </td>
			<td class="genericTableData">
				<select name="client_2_1" id="client_2_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_2_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_2_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	
		<tr>
			<td colspan="2">3. Looking after the home</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Are you able to look after your home?</td>
			<td class="genericTableData">
				<select name="client_3_1" id="client_3_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_3_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_3_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

		<tr>
			<td colspan="2">4. Self-care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you have problems keeping clean and tidy?</td>
			<td class="genericTableData">
				<select name="client_4_1" id="client_4_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_4_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_4_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	
		<tr>
			<td colspan="2">5. Daytime activities</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. How do you spend your day?</td>
			<td class="genericTableData">
				<select name="client_5_1" id="client_5_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_5_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_5_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

	
		<tr>
			<td colspan="2">6. Physical health</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. How well do you feel physically?</td>
			<td class="genericTableData">
				<select name="client_6_1" id="client_6_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_6_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_6_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">7. Psychotic Symptoms</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Do you ever hear voices or have problems with your thoughts? </td>
			<td class="genericTableData">
				<select name="client_7_1"  id="client_7_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_7_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_7_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	

		<tr>
			<td colspan="2">8. Information on condition and treatment</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Have you been given clear information about your medication?</td>
			<td class="genericTableData">
				<select name="client_8_1" id="client_8_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_8_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_8_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">9. Psychological Distress</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Have you recently felt very sad or low?</td>
			<td class="genericTableData">
				<select name="client_9_1"  id="client_9_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_9_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_9_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">10. Safefy to self</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you ever have thoughts of harming yourself?</td>
			<td class="genericTableData">
				<select name="client_10_1" id="client_10_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_10_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_10_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

					
		<tr>
			<td colspan="2">11. Safefy to others</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you think you could be a danger to other people's safety?</td>
			<td class="genericTableData">
				<select name="client_11_1" id="client_11_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_11_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
			
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_11_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
	
					
		<tr>
			<td colspan="2">12. Alcohol</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does drinking cause you any problems?</td>
			<td class="genericTableData">
				<select name="client_12_1" id="client_12_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_12_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_12_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
					
		<tr>
			<td colspan="2">13. Drugs</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you take any drugs that aren't prescribed?</td>
			<td class="genericTableData">
				<select name="client_13_1" id="client_13_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_13_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_13_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">14. Other Addictions</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you have any other addictions - such as gambling? </td>
			<td class="genericTableData">
				<select name="client_14_1" id="client_14_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_14_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_14_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">15. Company</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Are you happy with your social life?</td>
			<td class="genericTableData">
				<select name="client_15_1" id="client_15_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_15_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_15_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">16. Intimate relationships</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Do you have a partner? </td>
			<td class="genericTableData">
				<select name="client_16_1" id="client_16_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_16_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_16_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">17. Sexual Expression</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. How is your sex life? </td>
			<td class="genericTableData">
				<select name="client_17_1" id="client_17_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_17_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
	
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_17_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">18. Child care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you have any children under 18?</td>
			<td class="genericTableData">
				<select name="client_18_1" id="client_18_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_18_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_18_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			



		<tr>
			<td colspan="2">19. Other dependents</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you have any dependents other than children under 18, such as an elderly parent or beloved pet? </td>
			<td class="genericTableData">
				<select name="client_19_1" id="client_19_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_19_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_19_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">20. Basic education</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Any difficulty in reading, writing or understanding English?</td>
			<td class="genericTableData">
				<select name="client_20_1" id="client_20_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_20_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_20_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">21. Telephone</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Do you know how to use a telephone? </td>
			<td class="genericTableData">
				<select name="client_21_1" id="client_21_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_21_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_21_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
	
							
		<tr>
			<td colspan="2">22. Transport</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. How do you find using the bus, streetcar or train? </td>
			<td class="genericTableData">
				<select name="client_22_1" id="client_22_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_22_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
	
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_22_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">23. Money</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. How do you find budgeting your money?</td>
			<td class="genericTableData">
				<select name="client_23_1" id="client_23_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_23_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_23_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">24. Benefits</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Are you getting all the money you are entitled to?</td>
			<td class="genericTableData">
				<select name="client_24_1" id="client_24_1" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "client_24_1", OcanForm.getOcanFormOptions("Client Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_24_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">What are your hopes for the future?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_hopes_future",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">What do you think you need in order to get there?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_hope_future_need",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">How do you view your mental health?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_view_mental_health",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is spirituality an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_sprituality",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is culture (heritage) an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"client_culture_heritage",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
	
		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>

		<tr>
			<td class="genericTableHeader">Completion Date</td>
			<td class="genericTableData">
					<input id="clientCompletionDate" name="clientCompletionDate" onfocus="this.blur()" readonly="readonly" type="text" value="<%=ocanStaffForm.getFormattedClientCompletionDate()%>"> <img title="Calendar" id="cal_completionDate" src="../../images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'clientCompletionDate',ifFormat :'%Y-%m-%d',button :'cal_completionDate',align :'cr',singleClick :true,firstDay :1});</script>									
			</td>
		</tr>

		<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" value="<%=currentDemographicId%>" />
				<input type="hidden" name="ocanType" value="<%=ocanType%>" />
				<%
					if (!printOnly)
					{
						%>
				
				<input type="submit" name="submit" value="Save" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				
				<%
					}
				%>					
				<input type="button" name="cancel" value="Cancel" onclick="history.go(-1)" />
				<%
					if (printOnly)
					{
						%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" name="print" value="Print" onclick="window.print()">
						<%
					}
				%>
			</td>
		</tr>		
	</table>
	<%
		if (printOnly)
		{
			%>
				<script>
					setEnabledAll(document.ocan_client_form, false);

					document.getElementsByName('cancel')[0].disabled=false;
					document.getElementsByName('print')[0].disabled=false;
				</script>
			<%
		}
	%>

</form>


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
