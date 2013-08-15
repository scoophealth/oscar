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
<%@page import="org.oscarehr.common.model.OcanStaffFormData"%>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.dao.AdmissionDao"%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils" %>
<%@include file="/layouts/caisi_html_top-jquery.jspf"%>


<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));
	String ocanType = request.getParameter("ocanType");
	int prepopulate = 0;
	prepopulate = Integer.parseInt(request.getParameter("prepopulate")==null?"0":request.getParameter("prepopulate"));
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	int ocanStaffFormId =0; boolean newForm=false;
	int referralCount = 0;
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	OcanStaffForm ocanStaffForm = null;
	if(ocanStaffFormId != 0) {
		ocanStaffForm=OcanForm.getOcanStaffForm(Integer.valueOf(request.getParameter("ocanStaffFormId")));
	}else {		
		ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId,prepopulationLevel,ocanType);		
		
		//If this is a new form, prepopulate referral from last completed assessment.
		if(ocanStaffForm.getAssessmentId()==null) {
			newForm = true;
			OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(currentDemographicId,ocanType);
			if(lastCompletedForm!=null) {
				List<OcanStaffFormData> existingAnswers = OcanForm.getStaffAnswers(lastCompletedForm.getId(),"referrals_count",prepopulationLevel);
				if(existingAnswers.size()>0)
					referralCount = Integer.valueOf(existingAnswers.get(0).getAnswer()).intValue();
						
		
				// prepopulate the form from last completed assessment
				if(prepopulate==1) {			
						
					//lastCompletedForm.setId(null);
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

	DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	String hc_type = demographicDao.getDemographicById(currentDemographicId).getHcType();
	String admissionDate = "0001-01-01";
	AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");	
	List<Admission> admissions = admissionDao.getAdmissionsASC(currentDemographicId);
	for(Admission ad : admissions) {
		if(!"community".equalsIgnoreCase(ad.getProgramType())) {
			admissionDate = DateFormatUtils.ISO_DATE_FORMAT.format(ad.getAdmissionDate());
			break;			
		}
	}
	String admission_year = admissionDate.substring(0,4);
	String admission_month = admissionDate.substring(5,7);
	
%>


<script type="text/javascript" src="<%=request.getContextPath()%>/PMmodule/ClientManager/ocan_staff_form_validation.js"></script>

<script type="text/javascript">
function clearDate(el)
{
	$("#"+el).val("");
}
</script>		

<script>
//setup validation plugin
$("document").ready(function() {	
	$("#ocan_staff_form").validate({meta: "validate"});
		
	$.validator.addMethod('postalCode', function (value) { 
	    return /^((\d{5}-\d{4})|(\d{5})|()|([A-Z]\d[A-Z]\s\d[A-Z]\d))$/.test(value); 
	}, 'Please enter a valid US or Canadian postal code.');

	$.validator.addMethod('digitalNumber', function(value) {
		 return /^((\d|\d{2}|\d{3}|\d{4}|()))$/.test(value);		  
	}, 'Digits only');
});


</script>


<style>
.error {color:red;}
</style>			
<form id="cbi_form" name="cbi_form" action="ocan_form_action.jsp" method="post" onsubmit="return submitOcanForm()">	
		
	<input type="hidden" name="client_date_of_birth" id="client_date_of_birth" value="<%=ocanStaffForm.getClientDateOfBirth()%>" />
	<input type="hidden" id="clientStartDate" name="clientStartDate" value="<%=ocanStaffForm.getFormattedClientStartDate()%>"/>
	<input type="hidden" id="clientCompletionDate" name="clientCompletionDate" value="<%=ocanStaffForm.getFormattedClientCompletionDate()%>"/>
<% if(prepopulate==1) { %> 
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="" />
	
	<!-- client data start here	 -->
	<input type="hidden" name="client_1_1" id="client_1_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_1", prepopulationLevel).get(0).getAnswer():""%>" />
	
	<input type="hidden" name="client_1_comments" id="client_1_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_1_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_2_1" id="client_2_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_2_comments" id="client_2_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_2_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_3_1" id="client_3_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_3_comments" id="client_3_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_3_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_4_1" id="client_4_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_4_comments" id="client_4_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_4_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_5_1" id="client_5_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_5_comments" id="client_5_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_5_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_6_1" id="client_6_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_6_comments" id="client_6_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_6_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_7_1" id="client_7_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_7_comments" id="client_7_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_7_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_8_1" id="client_8_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_8_comments" id="client_8_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_8_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_9_1" id="client_9_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_9_comments" id="client_9_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_9_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_10_1" id="client_10_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_10_comments" id="client_10_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_10_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_11_1" id="client_11_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_11_comments" id="client_11_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_11_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_12_1" id="client_12_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_12_comments" id="client_12_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_12_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_13_1" id="client_13_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_13_comments" id="client_13_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_13_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_14_1" id="client_14_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_14_comments" id="client_14_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_14_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_15_1" id="client_15_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_15_comments" id="client_15_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_15_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_16_1" id="client_16_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_16_comments" id="client_16_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_16_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_17_1" id="client_17_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_17_comments" id="client_17_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_17_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_18_1" id="client_18_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_1", prepopulationLevel).get(0).getAnswer():""%>" />


	<input type="hidden" name="client_18_comments" id="client_18_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_18_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_19_1" id="client_19_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_19_comments" id="client_19_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_19_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_20_1" id="client_20_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_20_comments" id="client_20_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_20_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_21_1" id="client_21_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_21_comments" id="client_21_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_21_comments", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_22_1" id="client_22_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_22_comments" id="client_22_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_22_comments", prepopulationLevel).get(0).getAnswer():""%>" />
					
	<input type="hidden" name="client_23_1" id="client_23_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_1", prepopulationLevel).get(0).getAnswer():""%>" />
		
	<input type="hidden" name="client_23_comments" id="client_23_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_23_comments", prepopulationLevel).get(0).getAnswer():""%>" />
				
	<input type="hidden" name="client_24_1" id="client_24_1" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_1", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_1", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_24_comments" id="client_24_comments" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_comments", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_24_comments", prepopulationLevel).get(0).getAnswer():""%>" />
		
	<input type="hidden" name="client_hopes_future" id="client_hopes_future" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hopes_future", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hopes_future", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_hope_future_need" id="client_hope_future_need" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hope_future_need", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_hope_future_need", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_view_mental_health" id="client_view_mental_health" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_view_mental_health", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_view_mental_health", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_sprituality" id="client_sprituality" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_sprituality", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_sprituality", prepopulationLevel).get(0).getAnswer():""%>" />

	<input type="hidden" name="client_culture_heritage" id="client_culture_heritage" value="<%=OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_culture_heritage", prepopulationLevel).size()>0?OcanForm.getStaffAnswers(ocanStaffForm.getId(), "client_culture_heritage", prepopulationLevel).get(0).getAnswer():""%>" />

		<!-- client data end here  -->
<% } else { %>
	<input type="hidden" name="ocanStaffFormId" id="ocanStaffFormId" value="<%=ocanStaffForm.getId()%>" />
	<input type="hidden" name="assessmentId" id="assessmentId" value="<%=ocanStaffForm.getAssessmentId()%>" />
<%} %>
	<input type="hidden" name="prepopulate" id="prepopulate" value="<%=prepopulate%>" />
	<input type="hidden" name="assessment_status" id="assessment_status" value="In Progress" />	
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
	<tr><td><h3>CBI FORM</h3></td>
		<td></td>	
	</tr>
	<tr><td></td>
		<td></td>
	</tr>
		<tr>
			<td class="genericTableHeader">Last Name at Birth</td>
			<td class="genericTableData">
				<input type="text" name="lastNameAtBirth" id="lastNameAtBirth" value="<%=ocanStaffForm.getLastNameAtBirth()%>" size="32" maxlength="32"/>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">Current Last Name </td>
			<td class="genericTableData">
				<input type="text" name="lastName" id="lastName" value="<%=ocanStaffForm.getLastName()%>" size="32" maxlength="32"/>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">Middle Name</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"middle",32, prepopulationLevel)%>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableData">
				<input type="text" name="firstName" id="firstName" value="<%=ocanStaffForm.getFirstName()%>" size="32" maxlength="32" />
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Preferred Name</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"preferred",32, prepopulationLevel)%>
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Address Line 1</td>
			<td class="genericTableData">
				<input type="text" name="addressLine1" id="addressLine1" value="<%=ocanStaffForm.getAddressLine1()%>" size="64" maxlength="64"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Address Line 2</td>
			<td class="genericTableData">
				<input type="text" name="addressLine2" id="addressLine2" value="<%=ocanStaffForm.getAddressLine2()%>" size="64" maxlength="64"/>
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">City</td>
			<td class="genericTableData">
				<input type="text" name="city" id="city" value="<%=ocanStaffForm.getCity()%>" size="32" maxlength="32"/>
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Province</td>
			<td class="genericTableData">
				<select name="province">
					<%=OcanForm.renderAsProvinceSelectOptions(ocanStaffForm)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Postal Code (e.g. M4H 2T1)</td>
			<td class="genericTableData">
				<input type="text" name="postalCode" id="postalCode" value="<%=ocanStaffForm.getPostalCode()%>" size="8" maxlength="8" class="{validate: {postalCode:true}}"/>
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Phone Number</td>
			<td class="genericTableData">
				<input type="text" name="phoneNumber" id="phoneNumber" value="<%=ocanStaffForm.getPhoneNumber()%>" size="32" maxlength="32"/>
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Ext: </td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"extension",16, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Email address</td>
			<td class="genericTableData">
				<input type="text" name="email" id="email" value="<%=ocanStaffForm.getEmail()%>" size="64" maxlength="64"/>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Date of Birth (YYYY-MM-DD)</td>
			<td class="genericTableData">					
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "date_of_birth",false,ocanStaffForm.getDateOfBirth(),prepopulationLevel)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Estimated Age</td>
			<td class="genericTableData">
				<input type="text" name="estimatedAge" id="estimatedAge" value="<%=ocanStaffForm.getEstimatedAge()%>" size="3" maxlength="3"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Health Card # and Version Code</td>
			<td class="genericTableData">
				<input type="text" name="hcNumber" id="hcNumber" value="<%=ocanStaffForm.getHcNumber()%>" size="32" maxlength="32"/>
				<input type="text" name="hcVersion" id="hcVersion" value="<%=ocanStaffForm.getHcVersion()%>" size="8" maxlength="8"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Issuing Territory</td>
			<td class="genericTableData">
				<select name="issuingTerritory">				
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "issuingTerritory", OcanForm.getOcanFormOptions("Province List"),  hc_type, prepopulationLevel)%>
				</select>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Recipient Location</td>
			<td class="genericTableData">
				<select name="service_recipient_location" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_location", OcanForm.getOcanFormOptions("Recipient Location"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">LHIN Consumer Resides in</td>
			<td class="genericTableData">
				<select name="service_recipient_lhin" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_lhin", OcanForm.getOcanFormOptions("LHIN code"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Gender</td>
			<td class="genericTableData">
				<select name="gender" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "gender", OcanForm.getOcanFormOptions("Administrative Gender"),ocanStaffForm.getGender(),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Marital Status</td>
			<td class="genericTableData">
				<select name="marital_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "marital_status", OcanForm.getOcanFormOptions("Marital Status"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>
			
			<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" id="clientId" value="<%=currentDemographicId%>" />
				<input type="hidden" name="ocanType" id="ocanType" value="<%=ocanType%>" />				
				
				<input type="submit" name="submit" value="Save"/>&nbsp;&nbsp;&nbsp;&nbsp;			
				
				<input type="button" name="cancel" value="Cancel" onclick="history.go(-1)" />
				
			</td>
		</tr>		
		</table>
	
</form>


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
