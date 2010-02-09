<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@include file="/layouts/caisi_html_top2.jspf"%>


<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	

	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId);		
	
%>

<script>

$("document").ready(function() {	
	$("#ocan_staff_form").validate({meta: "validate"});	
});

</script>

<script type="text/javascript">

$('document').ready(function(){
	//we want to load initial meds
	var demographicId='<%=currentDemographicId%>';
	var medCount = $("#medications_count").val();
	for(var x=1;x<=medCount;x++) {
		$.get('ocan_form_medication.jsp?demographicId='+demographicId+'&medication_num='+x, function(data) {
			  $("#medication_block").append(data);					 
			});				
	} 
});

</script>


<script>

function validateOcanForm() {
	var method = document.getElementById('method').value;
	if(method == 'save_draft') {
		return saveDraft();
	}
	//alert('submit');
	return true;
}

function saveDraft() {
	alert('save draft');
	return false;
}

function changeNumberOfMedications() {
	var newCount = $("#medications_count").val(); 

	var demographicId='<%=currentDemographicId%>';

	//do we need to add..loop through existing blocks, and see if we need more...create on the way.
	for(var x=1;x<=newCount;x++) {		
		if(document.getElementById("medication_" + x) == null) {
			$.get('ocan_form_medication.jsp?demographicId='+demographicId+'&medication_num='+x, function(data) {
				  $("#medication_block").append(data);					 
				});														
		}
	}

	//do we need to remove. If there's any blocks > newCount..we need to delete them.
	for(var x=(Number(newCount)+1);x<=25;x++) {			
		if(document.getElementById("medication_" + x) != null) {
			$("#medication_"+x).remove();
		}
	}
}
</script>

<script>
$("document").ready(function() {

	$("#generate_summary_of_actions").click(function(){
		$("#summary_of_actions_block").innerHTML='';
		var count=0;
		var domains = '';
		
		for(var x=1;x<=24;x++) {
			var actionVal = $("#"+x+"_actions").val();			
			if(actionVal.length > 0) {
				count++;
				if(domains.length>0) {
					domains += ',';
				} 	
				domains += x;
			}
		}		
		$("#summary_of_actions_count").val(count);
		$("#summary_of_actions_domains").val(domains);
		var demographicId='<%=currentDemographicId%>';
		$.get('ocan_form_summary_of_actions.jsp?demographicId='+demographicId+'&size='+count+'&domains='+domains, function(data) {
			  $("#summary_of_actions_block").append(data);					 
			});		
	});
	
});

function suaChangeDomain(selectBox)
{
	var selectBoxId = selectBox.id;
	var priority = selectBoxId.substring(0,selectBoxId.indexOf('_'));

	var selectBoxValue = selectBox.options[selectBox.selectedIndex].value;
	
	$("#" + priority + "_summary_of_actions_action").val($("#"+selectBoxValue+ "_actions").val());	
}

$("document").ready(function(){

	var summaryOfActionsCount = $("#summary_of_actions_count").val();
	var summaryOfActionsDomains = $("#summary_of_actions_domains").val();
	var demographicId='<%=currentDemographicId%>';
	
	$.get('ocan_form_summary_of_actions.jsp?demographicId='+demographicId+'&size='+summaryOfActionsCount+'&domains='+summaryOfActionsDomains, function(data) {
		  $("#summary_of_actions_block").append(data);					 
		});		
	
});
</script>

<script>

$('document').ready(function(){
	//we want to load initial meds
	var demographicId='<%=currentDemographicId%>';
	var medCount = $("#referrals_count").val();
	for(var x=1;x<=medCount;x++) {
		$.get('ocan_form_referral.jsp?demographicId='+demographicId+'&referral_num='+x, function(data) {
			  $("#referral_block").append(data);					 
			});				
	} 
});

function changeNumberOfReferrals() {
	var newCount = $("#referrals_count").val(); 

	var demographicId='<%=currentDemographicId%>';

	//do we need to add..loop through existing blocks, and see if we need more...create on the way.
	for(var x=1;x<=newCount;x++) {		
		if(document.getElementById("referral_" + x) == null) {
			$.get('ocan_form_referral.jsp?demographicId='+demographicId+'&referral_num='+x, function(data) {
				  $("#referral_block").append(data);					 
				});														
		}
	}

	//do we need to remove. If there's any blocks > newCount..we need to delete them.
	for(var x=(Number(newCount)+1);x<=25;x++) {			
		if(document.getElementById("referral_" + x) != null) {
			$("#referral_"+x).remove();
		}
	}
}
</script>

<style>
.error {color:red;}
</style>



<form id="ocan_staff_form" action="ocan_form_action.jsp">
	<input type="hidden" id="method" name="method" value=""/>
	<h3>OCAN form (v1.2)</h3>

	<br />
	
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">
		<tr>
			<td class="genericTableHeader">Start Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "start_date",true)%>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Completion Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "completion_date",true)%>			
			</td>
		</tr>
		<tr>
			<td colspan="2">Organization Record</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Organization Name</td>
			<td class="genericTableData">
				<input type="text" value="<%=LoggedInInfo.loggedInInfo.get().currentFacility.getName() %>" readonly=readonly onfocus="this.blur();"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Organization Number</td>
			<td class="genericTableData">
				<input type="text" value="<%=LoggedInInfo.loggedInInfo.get().currentFacility.getOcanServiceOrgNumber() %>" readonly=readonly onfocus="this.blur();"/>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Select corresponding admission</td>
			<td class="genericTableData">
				<select name="admissionId">
					<%
						for (Admission admission : OcanForm.getAdmissions(currentDemographicId))
						{
							String selected="";
							
							if (ocanStaffForm.getAdmissionId()!=null && ocanStaffForm.getAdmissionId().intValue()==admission.getId().intValue()) selected="selected=\"selected\"";
							
							%>
								<option <%=selected%> value="<%=admission.getId()%>"><%=OcanForm.getEscapedAdmissionSelectionDisplay(admission)%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Function (MIS Functional Centre)</td>
			<td class="genericTableData">
				<select name="function">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "function", OcanForm.getOcanFormOptions("MIS Functional Centre List"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td colspan="2"></td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableData">
				<input type="text" name="lastName" id="lastName" value="<%=ocanStaffForm.getLastName()%>" />
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableData">
				<input type="text" name="firstName" id="firstName" value="<%=ocanStaffForm.getFirstName()%>" />
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Address Line 1</td>
			<td class="genericTableData">
				<input type="text" name="addressLine1" id="addressLine1" value="<%=ocanStaffForm.getAddressLine1()%>" />
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Address Line 2</td>
			<td class="genericTableData">
				<input type="text" name="addressLine2" id="addressLine2" value="<%=ocanStaffForm.getAddressLine2()%>" />
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">City</td>
			<td class="genericTableData">
				<input type="text" name="city" id="city" value="<%=ocanStaffForm.getCity()%>" />
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Province</td>
			<td class="genericTableData">
				<select name="function">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "province", OcanForm.getOcanFormOptions("Province"),ocanStaffForm.getProvince())%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Postal Code</td>
			<td class="genericTableData">
				<input type="text" name="postalCode" id="postalCode" value="<%=ocanStaffForm.getPostalCode()%>" />
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Telephone Number</td>
			<td class="genericTableData">
				<input type="text" name="phoneNumber" id="phoneNumber" value="<%=ocanStaffForm.getPhoneNumber()%>" />
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Email</td>
			<td class="genericTableData">
				<input type="text" name="email" id="email" value="<%=ocanStaffForm.getEmail()%>" />
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Health Card # and Version</td>
			<td class="genericTableData">
				<input type="text" name="hcNumber" id="hcNumber" value="<%=ocanStaffForm.getHcNumber()%>" />
				<input type="text" name="hcVersion" id="hcVersion" value="<%=ocanStaffForm.getHcVersion()%>" />
			</td>
		</tr>	
		
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Reason for Assessment</td>
			<td class="genericTableData">
				<select name="reason_for_assessment">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "reason_for_assessment", OcanForm.getOcanFormOptions("Reason for Assessment"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">If Other, Specify</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"reason_for_assessment_other",5,30)%>
			</td>
		</tr>
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Doctor</td>
			<td class="genericTableData">
				<select name="doctor">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "doctor", OcanForm.getOcanFormOptions("Doctor Psychiatrist"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Contact Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"doctor_contact_information",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Seen</td>
			<td class="genericTableData">
				<select name="doctor_last_seen">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "doctor_last_seen", OcanForm.getOcanFormOptions("Last Seen"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Psychiatrist</td>
			<td class="genericTableData">
				<select name="psychiatrist">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "psychiatrist", OcanForm.getOcanFormOptions("Doctor Psychiatrist"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Contact Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"psychiatrist_contact_information",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Seen</td>
			<td class="genericTableData">
				<select name="psychiatrist_last_seen">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "psychiatrist_last_seen", OcanForm.getOcanFormOptions("Last Seen"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Other Contact</td>
			<td class="genericTableData">
				<select name="other_contact">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "other_contact", OcanForm.getOcanFormOptions("Practioner List"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Contact Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"other_contact_contact_information",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Seen</td>
			<td class="genericTableData">
				<select name="other_contact_last_seen">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "other_contact_last_seen", OcanForm.getOcanFormOptions("Last Seen"))%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Other Agency</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "other_agency", 25)%>						
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Contact Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"other_agency_contact_information",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Seen</td>
			<td class="genericTableData">
				<select name="other_agency_last_seen">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "other_agency_last_seen", OcanForm.getOcanFormOptions("Last Seen"))%>
				</select>					
			</td>
		</tr>				

		<tr>
			<td colspan="2"></td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Recipient Location</td>
			<td class="genericTableData">
				<select name="service_recipient_location">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_location", OcanForm.getOcanFormOptions("Recipient Location"))%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Service Recipient LHIN</td>
			<td class="genericTableData">
				<select name="service_recipient_lhin">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_recipient_lhin", OcanForm.getOcanFormOptions("LHIN code"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Delivery LHIN</td>
			<td class="genericTableData">
				<select name="service_delivery_lhin">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_delivery_lhin", OcanForm.getOcanFormOptions("LHIN code"))%>
				</select>					
			</td>
		</tr>						
		
		<tr>
			<td class="genericTableHeader">Date of Birth</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "date_of_birth",true,ocanStaffForm.getDateOfBirth())%>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Gender</td>
			<td class="genericTableData">
				<select name="gender">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "gender", OcanForm.getOcanFormOptions("Administrative Gender"))%>
				</select>					
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Marital Status</td>
			<td class="genericTableData">
				<select name="marital_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "marital_status", OcanForm.getOcanFormOptions("Marital Status"))%>
				</select>					
			</td>
		</tr>	

		<tr>
			<td colspan="2">Client Capacity</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Does the client have power of attorney for property?</td>
			<td class="genericTableData">
				<select name="power_attorney_property">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "power_attorney_property", OcanForm.getOcanFormOptions("Client Capacity"))%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Additional Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"power_attorney_property_additional_information",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Does the client have power of attorney or substitute decision maker for personal care?</td>
			<td class="genericTableData">
				<select name="power_attorney_personal_care">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "power_attorney_personal_care", OcanForm.getOcanFormOptions("Client Capacity"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Additional Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"power_attorney_personal_care_additional_information",5,30)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Does the client have a court appointed guardian?</td>
			<td class="genericTableData">
				<select name="court_appointed_guardian">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "court_appointed_guardian", OcanForm.getOcanFormOptions("Client Capacity"))%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Additional Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"court_appointed_guardian_additional_information",5,30)%>
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Who referred you to this service?</td>
			<td class="genericTableData">
				<select name="source_of_referral">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "source_of_referral", OcanForm.getOcanFormOptions("Referral Source"))%>
				</select>					
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">What culture do you identify with?</td>
			<td class="genericTableData">
				<select name="culture">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "culture", OcanForm.getOcanFormOptions("Ethniticity"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Aboriginal Origin?</td>
			<td class="genericTableData">
				<select name="aboriginal">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "aboriginal", OcanForm.getOcanFormOptions("Aboriginal Origin"))%>
				</select>					
			</td>
		</tr>		

		<tr>
			<td class="genericTableHeader">Citizenship Status?</td>
			<td class="genericTableData">
				<select name="citizenship_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "citizenship_status", OcanForm.getOcanFormOptions("Citizenship Status"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Length of time lived in Canada?</td>
			<td class="genericTableData">
			Years: 
				<select name="years_in_canada">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "years_in_canada", OcanForm.getOcanFormOptions("Years in Canada"))%>
				</select>
			&nbsp;&nbsp;
			Months:
				<select name="months_in_canada">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "months_in_canada", OcanForm.getOcanFormOptions("Months in Canada"))%>
				</select>		
			</td>
		</tr>				
		<tr>
			<td class="genericTableHeader">Can you tell me about your immigration experience?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"immigration_experience",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Do you have any issues with your immigration experience?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "immigration_issues", OcanForm.getOcanFormOptions("Immigration Experience"))%>						
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Immigration Issues - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"immigration_issues_other",5,30)%>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Do you have any experience of discrimination?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "discrimination", OcanForm.getOcanFormOptions("Discrimination Experience"))%>						
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Discrimination - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"discrimination_other",5,30)%>
			</td>
		</tr>						
		
		<tr>
			<td class="genericTableHeader">Service recipient preferred language?</td>
			<td class="genericTableData">
				<select name="preferred_language">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "preferred_language", OcanForm.getOcanFormOptions("Language"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Language of Service Provision?</td>
			<td class="genericTableData">
				<select name="language_service_provision">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "language_service_provision", OcanForm.getOcanFormOptions("Language"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Do you have any Legal issues?</td>
			<td class="genericTableData">
				<select name="legal_issues">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "legal_issues", OcanForm.getOcanFormOptions("Legal Issues"))%>
				</select>					
			</td>
		</tr>													
		<tr>
			<td class="genericTableHeader">Legal Status?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "legal_status", OcanForm.getOcanFormOptions("Legal History Type"))%>						
			</td>
		</tr>		

		<tr>
			<td class="genericTableHeader">Exit Disposition?</td>
			<td class="genericTableData">
				<select name="exit_disposition">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "exit_disposition", OcanForm.getOcanFormOptions("Exit Disposition"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"exit_disposition_commments",5,30)%>
			</td>
		</tr>						
		
		<tr>
			<td colspan="2">1. Accommodation</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack a current place to stay?</td>
			<td class="genericTableData">
				<select name="1_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with accommodation does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="1_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with accommodation does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="1_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with accommodation does the person need from local services?</td>
			<td class="genericTableData">
				<select name="1_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"1_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"1_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "1_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "1_review_date",false)%>				
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Where do you live?</td>
			<td class="genericTableData">
				<select name="1_where_live">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_where_live", OcanForm.getOcanFormOptions("Residence Type"))%>
				</select>					
			</td>
		</tr>		
				
		<tr>
			<td class="genericTableHeader">Do you receive any support?</td>
			<td class="genericTableData">
				<select name="1_any_support">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_any_support", OcanForm.getOcanFormOptions("Residential Support"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Do you live with anyone?</td>
			<td class="genericTableData">
				<select name="1_live_with_anyone">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "1_live_with_anyone", OcanForm.getOcanFormOptions("Living Arrangement Type"))%>
				</select>					
			</td>
		</tr>				
	
		<tr>
			<td colspan="2">2. Food</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty in getting enough to eat?</td>
			<td class="genericTableData">
				<select name="2_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "2_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with getting enough to eat does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="2_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "2_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with getting enough to eat does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="2_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "2_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with getting enough to eat does the person need from local services?</td>
			<td class="genericTableData">
				<select name="2_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "2_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"2_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"2_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "2_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "2_review_date",false)%>				
			</td>
		</tr>							
	
	
		<tr>
			<td colspan="2">3. Looking after the home</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after the home?</td>
			<td class="genericTableData">
				<select name="3_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "3_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with looking after the home does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="3_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "3_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with looking after the home does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="3_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "3_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with looking after the home does the person need from local services?</td>
			<td class="genericTableData">
				<select name="3_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "3_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"3_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"3_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "3_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "3_review_date",false)%>				
			</td>
		</tr>			
	
		<tr>
			<td colspan="2">4. Self-care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty with self-care?</td>
			<td class="genericTableData">
				<select name="4_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "4_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with self-care does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="4_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "4_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with self-care does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="4_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "4_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with self-care does the person need from local services?</td>
			<td class="genericTableData">
				<select name="4_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "4_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"4_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"4_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "4_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "4_review_date",false)%>				
			</td>
		</tr>			
		
	
		<tr>
			<td colspan="2">5. Daytime activities</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty with regular, appropriate daytime activities?</td>
			<td class="genericTableData">
				<select name="5_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives in finding and keeping regular and appropriate daytime activities?</td>
			<td class="genericTableData">
				<select name="5_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services in finding and keeping regular and appropriate daytime activities?</td>
			<td class="genericTableData">
				<select name="5_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services in finding and keeping regular and appropriate daytime activities?</td>
			<td class="genericTableData">
				<select name="5_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"5_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"5_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "5_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "5_review_date",false)%>				
			</td>
		</tr>		
	
		<tr>
			<td class="genericTableHeader">What is your current employment status?</td>
			<td class="genericTableData">
				<select name="5_current_employment_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_current_employment_status", OcanForm.getOcanFormOptions("Employment Status"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Are you currently in school?</td>
			<td class="genericTableData">
				<select name="5_education_program_status">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "5_education_program_status", OcanForm.getOcanFormOptions("Education Program Status"))%>
				</select>					
			</td>
		</tr>		
	
		<tr>
			<td class="genericTableHeader">Are you at risk of unemployment or disrupted education?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "5_unemployment_risk", OcanForm.getOcanFormOptions("Unemployed Education Risk"))%>						
			</td>
		</tr>	
	
	
		<tr>
			<td colspan="2">6. Physical health</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any physical disability or any physical illness?</td>
			<td class="genericTableData">
				<select name="6_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "6_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives for physical health problems?</td>
			<td class="genericTableData">
				<select name="6_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "6_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services for physical health problems?</td>
			<td class="genericTableData">
				<select name="6_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "6_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services for physical health problems?</td>
			<td class="genericTableData">
				<select name="6_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "6_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "6_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "6_review_date",false)%>				
			</td>
		</tr>						
				
		<tr>
			<td class="genericTableHeader">Medical Conditions?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "6_medical_conditions", OcanForm.getOcanFormOptions("Medical Conditions"))%>						
			</td>
		</tr>					
		<tr>
			<td class="genericTableHeader">Autism</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_medical_conditions_autism",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_medical_conditions_other",5,30)%>
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Do you have any concerns about your physical health?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "6_physical_health_concerns", OcanForm.getOcanFormOptions("Physical Health Concerns"))%>						
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">If Yes, please indicate the areas where you have concerns</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "6_physical_health_details", OcanForm.getOcanFormOptions("Concerns Detail"))%>						
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">If Yes, please indicate the areas where you have concerns - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_physical_health_details_other",5,30)%>
			</td>
		</tr>								

		<tr>
			<td class="genericTableHeader">Number of Medications?</td>
			<td class="genericTableData">
				<select name="medications_count" id="medications_count" onchange="changeNumberOfMedications();">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "medications_count", OcanForm.getOcanFormOptions("Years in Canada"))%>
				</select>					
			</td>
		</tr>
		<tr>
			<td colspan="2">				
				<div id="medication_block">
					<!-- results from adding/removing medication will go into this block -->
				</div>
			</td>

		<tr>
			<td colspan="2">7. Psychotic Symptoms</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any psychotic symptoms?</td>
			<td class="genericTableData">
				<select name="7_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "7_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2.  How much help does the person receive from friends or relatives for these psychotic symptoms?</td>
			<td class="genericTableData">
				<select name="7_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "7_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services for these psychotic symptoms?</td>
			<td class="genericTableData">
				<select name="7_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "7_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b.  How much help does the person need from local services for these psychotic symptoms?</td>
			<td class="genericTableData">
				<select name="7_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "7_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"7_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"7_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "7_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "7_review_date",false)%>				
			</td>
		</tr>	
		
		<tr>
			<td colspan="2">Psychiatric History</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Have you been hospitalized due to your mental health during the past two years?</td>
			<td class="genericTableData">
				<select name="hospitalized_mental_illness">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "hospitalized_mental_illness", OcanForm.getOcanFormOptions("Hospitalized Mental Illness"))%>
				</select>					
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">If Yes, Total number of admissions (last two years)</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "hospitalized_mental_illness_admissions", 25)%>						
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">If Yes, Total number of hospitalization days per admission. (last two years)</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "hospitalized_mental_illness_days", 25)%>						
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Community Treatment Orders</td>
			<td class="genericTableData">
				<select name="community_treatment_orders">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "community_treatment_orders", OcanForm.getOcanFormOptions("Community Treatment Orders"))%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Psychiatric History - Additional Information</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"6_psychiatric_history_addl_info",5,30)%>
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Symptom Checklist</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "symptom_checklist", OcanForm.getOcanFormOptions("Symptoms Checklist"))%>						
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Symptom Checklist - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"symptom_checklist_other",5,30)%>
			</td>
		</tr>										



		<tr>
			<td colspan="2">8. Information on condition and treatment</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Has the person had clear verbal or written information about condition and treatment?</td>
			<td class="genericTableData">
				<select name="8_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "8_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives in obtaining such information?</td>
			<td class="genericTableData">
				<select name="8_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "8_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services in obtaining such information? </td>
			<td class="genericTableData">
				<select name="8_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "8_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b.  How much help does the person need from local services in obtaining such information?</td>
			<td class="genericTableData">
				<select name="8_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "8_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"8_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"8_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "8_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "8_review_date",false)%>				
			</td>
		</tr>	

		<tr>
			<td class="genericTableHeader">Diagnostic Categories</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "diagnostic_categories", OcanForm.getOcanFormOptions("Diagnostic Categories"))%>						
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Other Illness Information</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "diagnostic_categories", OcanForm.getOcanFormOptions("Diagnostic - Other Illness"))%>						
			</td>
		</tr>		



		<tr>
			<td colspan="2">9. Psychological Distress</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person suffer from current psychological distress?</td>
			<td class="genericTableData">
				<select name="9_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "9_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives for this distress?</td>
			<td class="genericTableData">
				<select name="9_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "9_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services for this distress?</td>
			<td class="genericTableData">
				<select name="9_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "9_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services for this distress?</td>
			<td class="genericTableData">
				<select name="9_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "9_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"9_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"9_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "9_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "9_review_date",false)%>				
			</td>
		</tr>	


		
		<tr>
			<td colspan="2">10. Safefy to self</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Is the person a danger to him- or herself?</td>
			<td class="genericTableData">
				<select name="10_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "10_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2.  How much help does the person receive from friends or relatives to reduce the risk of self-harm?</td>
			<td class="genericTableData">
				<select name="10_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "10_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services to reduce the risk of self-harm?</td>
			<td class="genericTableData">
				<select name="10_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "10_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services to reduce the risk of self-harm?</td>
			<td class="genericTableData">
				<select name="10_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "10_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"10_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"10_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "10_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "10_review_date",false)%>				
			</td>
		</tr>


		<tr>
			<td class="genericTableHeader">Have you attempted suicide in the past?</td>
			<td class="genericTableData">
				<select name="suicide_past">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "suicide_past", OcanForm.getOcanFormOptions("Suicide in Past"))%>
				</select>					
			</td>
		</tr>					
		
		<tr>
			<td class="genericTableHeader">Do you have any concerns for your own safety?</td>
			<td class="genericTableData">
				<select name="safety_concerns">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "safety_concerns", OcanForm.getOcanFormOptions("Own Safety Concerns"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Do you currently have suicidal thoughts?</td>
			<td class="genericTableData">
				<select name="suicidal_thoughts">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "suicidal_thoughts", OcanForm.getOcanFormOptions("Suicidal Thoughts"))%>
				</select>					
			</td>
		</tr>														
		<tr>
			<td class="genericTableHeader">Risks</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "risks", OcanForm.getOcanFormOptions("Risks List"))%>						
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Risks - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"risks_other",5,30)%>
			</td>
		</tr>			
					
					
					
		<tr>
			<td colspan="2">11. Safefy to others</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Is the person a current or potential risk to other's people safety?</td>
			<td class="genericTableData">
				<select name="11_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "11_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives to reduce the risk that he or she might harm someone else? </td>
			<td class="genericTableData">
				<select name="11_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "11_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a.  How much help does the person receive from local services to reduce the risk that he or she might harm someone else?</td>
			<td class="genericTableData">
				<select name="11_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "11_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services to reduce the risk that he or she might harm someone else?</td>
			<td class="genericTableData">
				<select name="11_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "11_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"11_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"11_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "11_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "11_review_date",false)%>				
			</td>
		</tr>					


					
					
		<tr>
			<td colspan="2">12. Alcohol</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person drink excessively, or have a problem controlling his or her drinking?</td>
			<td class="genericTableData">
				<select name="12_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "12_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives for this drinking?</td>
			<td class="genericTableData">
				<select name="12_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "12_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services for this drinking?</td>
			<td class="genericTableData">
				<select name="12_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "12_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services for this drinking?</td>
			<td class="genericTableData">
				<select name="12_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "12_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"12_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"12_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "12_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "12_review_date",false)%>				
			</td>
		</tr>					
					
		<tr>
			<td class="genericTableHeader">Number of Drinks</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "num_drinks", 25)%>						
			</td>
		</tr>							
					
		<tr>
			<td class="genericTableHeader">How often do you drink alcohol?</td>
			<td class="genericTableData">
				<select name="frequency_alcohol">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "frequency_alcohol", OcanForm.getOcanFormOptions("Frequency of Alcohol Use"))%>
				</select>					
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Indicate the stage of change client is at</td>
			<td class="genericTableData">
				<select name="state_of_change_alcohol">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "state_of_change_alcohol", OcanForm.getOcanFormOptions("Stage of Change - Alcohol"))%>
				</select>					
			</td>
		</tr>								
		<tr>
			<td class="genericTableHeader">How has drinking had an impact on your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"drinking_impact",5,30)%>
			</td>
		</tr>	








					
					
		<tr>
			<td colspan="2">13. Drugs</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with drug misuse?</td>
			<td class="genericTableData">
				<select name="13_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "13_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with drug misuse does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="13_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "13_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with drug misuse does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="13_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "13_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with drug misuse does the person need from local services?</td>
			<td class="genericTableData">
				<select name="13_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "13_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"13_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"13_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "13_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "13_review_date",false)%>				
			</td>
		</tr>					
							
		<tr>
			<td class="genericTableHeader">Which of the following drugs have you used?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "drug_list", OcanForm.getOcanFormOptions("Drug List"))%>						
			</td>
		</tr>	
					
		
		<tr>
			<td class="genericTableHeader">Which of the following drugs have you used?- Frequency</td>
			<td class="genericTableData">
				<select name="drug_use_freq">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "drug_use_freq", OcanForm.getOcanFormOptions("Frequence of Drug Use"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Has the substance been injected?</td>
			<td class="genericTableData">
				<select name="drug_use_injected">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "drug_use_injected", OcanForm.getOcanFormOptions("Frequence of Drug Use"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Indicate the stage of change client is at</td>
			<td class="genericTableData">
				<select name="state_of_change_addiction">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "state_of_change_addiction", OcanForm.getOcanFormOptions("Stage of Change - Alcohol"))%>
				</select>					
			</td>
		</tr>			
										
		<tr>
			<td class="genericTableHeader">How has the substance(s) of choice had an impact on your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"drug_impact",5,30)%>
			</td>
		</tr>			
		


					
		<tr>
			<td colspan="2">14. Other Addictions</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with addictions?</td>
			<td class="genericTableData">
				<select name="14_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "14_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with addictions does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="14_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "14_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a.How much help with addictions does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="14_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "14_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with addictions does the person need from local services?</td>
			<td class="genericTableData">
				<select name="14_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "14_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"14_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"14_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "14_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "14_review_date",false)%>				
			</td>
		</tr>			
		<tr>
			<td class="genericTableHeader">Type of Addiction</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "addiction_type", OcanForm.getOcanFormOptions("Addiction Type"))%>						
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Type of Addiction - Other</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"addiction_type_other",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Indicate the stage of change client is at</td>
			<td class="genericTableData">
				<select name="state_of_change_addiction">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "14_state_of_change", OcanForm.getOcanFormOptions("Stage of Change - Alcohol"))%>
				</select>					
			</td>
		</tr>			
										
		<tr>
			<td class="genericTableHeader">How has the addiction had an impact on your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"addiction_impact",5,30)%>
			</td>
		</tr>			
								
		
		
		
		<tr>
			<td colspan="2">15. Company</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person need help with social contact?</td>
			<td class="genericTableData">
				<select name="15_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "15_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with social contact does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="15_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "15_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services in organizing social contact?</td>
			<td class="genericTableData">
				<select name="15_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "15_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services in organizing social contact?</td>
			<td class="genericTableData">
				<select name="15_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "15_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"15_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"15_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "15_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "15_review_date",false)%>				
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Have there been any changes to your social patterns recently?</td>
			<td class="genericTableData">
				<select name="social_patterns">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "social_patterns", OcanForm.getOcanFormOptions("Social Patterns Changes"))%>
				</select>					
			</td>
		</tr>						
		


		
		
		<tr>
			<td colspan="2">16. Intimate relationships</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person have any difficulty in finding a partner or in maintaining a close relationship?</td>
			<td class="genericTableData">
				<select name="16_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "16_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with forming and maintaining close relationships does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="16_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "16_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with forming and maintaining close relationships does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="16_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "16_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with forming and maintaining close relationships does the person need from local services in organizing social contact?</td>
			<td class="genericTableData">
				<select name="16_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "16_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"16_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"16_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "16_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "16_review_date",false)%>				
			</td>
		</tr>		
		
			
		<tr>
			<td colspan="2">17. Sexual Expression</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with his or her sex life?</td>
			<td class="genericTableData">
				<select name="17_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "17_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with problems in his or her sex life does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="17_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "17_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with problems in his or her sex life does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="17_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "17_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with problems in his or her sex life does the person need from local services in organizing social contact?</td>
			<td class="genericTableData">
				<select name="17_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "17_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"17_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"17_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "17_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "17_review_date",false)%>				
			</td>
		</tr>	
		
		
		<tr>
			<td colspan="2">18. Child care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after his or her children?</td>
			<td class="genericTableData">
				<select name="18_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "18_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with looking after the children does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="18_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "18_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with looking after the children does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="18_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "18_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with looking after the children does the person need from local services?</td>
			<td class="genericTableData">
				<select name="18_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "18_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"18_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"18_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "18_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "18_review_date",false)%>				
			</td>
		</tr>			


		<tr>
			<td colspan="2">19. Other dependents</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after other dependents?</td>
			<td class="genericTableData">
				<select name="19_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "19_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with looking after other dependants does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="19_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "19_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with looking after other dependents does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="19_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "19_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with looking after other dependents does the person need from local services?</td>
			<td class="genericTableData">
				<select name="19_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "19_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"19_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"19_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "19_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "19_review_date",false)%>				
			</td>
		</tr>


		<tr>
			<td colspan="2">20. Basic education</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack basic skills in numeracy and literacy?</td>
			<td class="genericTableData">
				<select name="20_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "20_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with numeracy and literacy does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="20_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "20_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with numeracy and literacy does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="20_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "20_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with numeracy and literacy does the person need from local services in organizing social contact?</td>
			<td class="genericTableData">
				<select name="20_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "20_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"20_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"20_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "20_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "20_review_date",false)%>				
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">What is your highest level of education?</td>
			<td class="genericTableData">
				<select name="level_of_education">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "level_of_education", OcanForm.getOcanFormOptions("Education Level"))%>
				</select>					
			</td>
		</tr>
		
		
		
		<tr>
			<td colspan="2">21. Telephone</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack basic skills in getting access to or using a telephone?</td>
			<td class="genericTableData">
				<select name="21_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "21_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives to make telephone calls?</td>
			<td class="genericTableData">
				<select name="21_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "21_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services to make telephone calls?</td>
			<td class="genericTableData">
				<select name="21_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "21_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services to make telephone calls?</td>
			<td class="genericTableData">
				<select name="21_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "21_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"21_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"21_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "21_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "21_review_date",false)%>				
			</td>
		</tr>		
		
							
							
		<tr>
			<td colspan="2">22. Transport</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any problems using public transport?</td>
			<td class="genericTableData">
				<select name="22_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "22_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help with traveling does the person receive from friends or relatives?</td>
			<td class="genericTableData">
				<select name="22_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "22_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help with traveling does the person receive from local services?</td>
			<td class="genericTableData">
				<select name="22_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "22_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help with traveling does the person need from local services?</td>
			<td class="genericTableData">
				<select name="22_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "22_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"22_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"22_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "22_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "22_review_date",false)%>				
			</td>
		</tr>
		
		
		
		<tr>
			<td colspan="2">23. Money</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems budgeting his or her money?</td>
			<td class="genericTableData">
				<select name="23_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "23_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives in managing his or her money?</td>
			<td class="genericTableData">
				<select name="23_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "23_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services in managing his or her money?</td>
			<td class="genericTableData">
				<select name="23_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "23_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services in managing his or her money?</td>
			<td class="genericTableData">
				<select name="23_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "23_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"23_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"23_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "23_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "23_review_date",false)%>				
			</td>
		</tr>		

		<tr>
			<td class="genericTableHeader">What is your primary source of income?</td>
			<td class="genericTableData">
				<select name="income_source_type">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "income_source_type", OcanForm.getOcanFormOptions("Income Source Type"))%>
				</select>					
			</td>
		</tr>
		
		
		<tr>
			<td colspan="2">24. Benefits</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Is the person definitely receiving all the benefits that he or she is entitled to?</td>
			<td class="genericTableData">
				<select name="24_1">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "24_1", OcanForm.getOcanFormOptions("Camberwell Need"))%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">2. How much help does the person receive from friends or relatives in obtaining the full benefit entitlement?</td>
			<td class="genericTableData">
				<select name="24_2">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "24_2", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3a. How much help does the person receive from local services in obtaining the full benefit entitlement?</td>
			<td class="genericTableData">
				<select name="24_3a">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "24_3a", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">3b. How much help does the person need from local services in obtaining the full benefit entitlement?</td>
			<td class="genericTableData">
				<select name="24_3b">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "24_3b", OcanForm.getOcanFormOptions("Camberwell Help"))%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"24_comments",5,30)%>
			</td>
		</tr>			
		
		<tr>
			<td class="genericTableHeader">Action(s)</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"24_actions",5,30)%>
			</td>
		</tr>					
			
		<tr>
			<td class="genericTableHeader">By Whom?</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(), "24_by_whom", 25)%>						
			</td>
		</tr>				
		
		<tr>
			<td class="genericTableHeader">Review Date</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "24_review_date",false)%>				
			</td>
		</tr>		
		
		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">What are your hopes for the future?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"hopes_future",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">What do you think you need in order to get there?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"hope_future_need",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">How do you view your mental health?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"view_mental_health",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is spirituality an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"sprituality",5,30)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is culture (heritage) an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"culture_heritage",5,30)%>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Presenting Issues</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsCheckBoxOptions(ocanStaffForm.getId(), "presenting_issues", OcanForm.getOcanFormOptions("Presenting Issues"))%>						
			</td>
		</tr>
			
		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>
		<tr>
			<td colspan="2">Summary of Actions</td>
		</tr>	
		<tr>
			<td colspan="2"><input type="button" value="Generate Summary" name="generate_summary_of_actions" id="generate_summary_of_actions"/></td>
			<%=OcanForm.renderAsHiddenField(ocanStaffForm.getId(), "summary_of_actions_count")%>
			<%=OcanForm.renderAsHiddenField(ocanStaffForm.getId(), "summary_of_actions_domains")%>		
		</tr>
		<tr>
			<td colspan="2">			
				<div id="summary_of_actions_block">
							
							
				
				</div>
			</td>
		</tr>

		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>
		<tr>
			<td colspan="2">Summary of Referrals</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Number of Referrals?</td>
			<td class="genericTableData">
				<select name="referrals_count" id="referrals_count" onchange="changeNumberOfReferrals();">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "referrals_count", OcanForm.getOcanFormOptions("Years in Canada"))%>
				</select>					
			</td>
		</tr>													
		<tr>
			<td colspan="2">
				<div id="referral_block">
						
				</div>
			</td>
		</tr>
		<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" value="<%=currentDemographicId%>" />
				Sign <input type="checkbox" name="signed" />
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="submit" value="Submit"  onclick="document.getElementById('method').value='submit';"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="submit" value="Save Draft"  onclick="document.getElementById('method').value='save_draft';"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="Cancel" onclick="history.go(-1)" />
			</td>
		</tr>		
	</table>
	

</form>


<%@include file="/layouts/caisi_html_bottom2.jspf"%>
