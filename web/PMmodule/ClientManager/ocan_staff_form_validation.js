$("document").ready(function() {
	$("input[name='immigration_issues'][value='4']").change(function(){
		if($("input[name='immigration_issues'][value='4']").attr('checked') == true) {
			$("input[name='immigration_issues'][value='3']").attr('checked',true);
			$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
			$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
		} else {
			$("input[name='immigration_issues'][value='3']").attr('readonly','');
			$("input[name='immigration_issues'][value='3']").attr('disabled','');
		}
	});
	
	if($("input[name='immigration_issues'][value='4']").attr('checked') == true) {
		$("input[name='immigration_issues'][value='3']").attr('readonly','readonly');
		$("input[name='immigration_issues'][value='3']").attr('disabled','disabled');
	}
});

function submitOcanForm() {
	var status = document.getElementById('assessment_status').value;
	if(status == 'Active') {
		$('#ocan_staff_form').unbind('submit').submit();		
		return true;
	}
	if(!$("#ocan_staff_form").valid()) {
		alert('Validation failed. Please check all required fields highlighted');
		return false;
	}

	if(!validateStartAndCompletionDates()) {
		return false;
	}
	
	if($("#clientDOBType").val() == 'EST') {		
		if($("#date_of_birth").val().length==0) {			
			alert('Date of Birth (YYYY-MM-DD) - Please provide date of birth');
			$("#date_of_birth").focus();
			return false;
		}		
	}
	if($("#clientDOBType").val() == 'UNK') {		
		if($("#date_of_birth").val().length!=0) {			
			alert('Date of Birth (YYYY-MM-DD) - Please do not provide date of birth');
			$("#date_of_birth").focus();
			return false;
		}		
	}
	/*
	if($("#clientDOBType").val().length == 0) {	
		alert('Date of Birth - Please choose the type of date of birth');
		$("#clientDOBType").focus();
		return false;				
	}
	*/
	
	var newCount = $("#center_count").val(); 
	for(var x=1;x<=newCount;x++) { 
		if($("#exitDate"+x).val().length != 0) {		
			if($("#serviceUseRecord_exitDisposition"+x).val().length==0) {			
				alert('Exit Disposition - Please specify one');
				$("#serviceUseRecord_exitDisposition"+x).focus();
				return false;
			}		
		}
	}
	
	if($("#reasonForAssessment").val() == 'OTHR') {		
		if($("#reason_for_assessment_other").val().length==0) {			
			alert('Reason for assessment - Please specify other');
			$("#reason_for_assessment_other").focus();
			return false;
		}		
	}
	
	if($("#power_attorney_property").val() == 'TRUE') {
		if($("#power_attorney_property_additional_information").val().length == 0) {
			alert('Client Capacity - please provider additional information');
			$("#power_attorney_property_additional_information").focus();
			return false;
		}
	}
	
	
	if($("#power_attorney_personal_care").val() == 'TRUE') {
		if($("#power_attorney_personal_care_additional_information").val().length == 0) {
			alert('Client Capacity - please provider additional information');
			$("#power_attorney_personal_care_additional_information").focus();
			return false;
		}
	}
	
	if($("#court_appointed_guardian").val() == 'TRUE') {
		if($("#court_appointed_guardian_additional_information").val().length == 0) {
			alert('Client Capacity - please provider additional information');
			$("#court_appointed_guardian_additional_information").focus();
			return false;
		}
	}

	if($("input[name='immigration_issues'][value='8']").attr('checked') == true) {
		if($("#immigration_issues_other").val().length == 0) {
			alert('Immigration issues - please provider other');
			$("#immigration_issues_other").focus();
			return false;
		}
	}
	
	if($("#discrimination").val() == '410515003') {
		if($("#discrimination_other").val().length == 0) {
			alert('Discrimination - please provide additional information');
			$("#discrimination_other").focus();
			return false;
		}
	}
	
	
	if($("input[name='6_medical_conditions'][value='408856003']").attr('checked') == true) {
		if($("#6_medical_conditions_autism").val().length == 0) {
			alert('Medical Conditions - please provide autism information');
			$("#6_medical_conditions_autism").focus();
			return false;
		}
	}
	
	if($("input[name='6_medical_conditions'][value='410515003']").attr('checked') == true) {
		if($("#6_medical_conditions_other").val().length == 0) {
			alert('Medical Conditions - please provide other information');
			$("#6_medical_conditions_other").focus();
			return false;
		}
	}
	

	if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
		if($("#6_physical_health_details_other").val().length == 0) {
			alert('Physical Health Details - please provide other information');
			$("#6_physical_health_details_other").focus();
			return false;
		}
	}
	
	
	//medication_1_se_description
	var medicationFailed=false;
	$("input[value='410515003']").each(function(){	
		if(medicationFailed) {return;}
		if($(this).attr('name').indexOf('_se_description')!=-1) {
			if($(this).attr('checked') == true) {
				var cbName = $(this).attr('name');
				var otherName = cbName + '_other';
				if($("#"+otherName).val().length == 0) {
					alert('Medication - Side Effects - please provide other information');
					$("#"+otherName).focus();
					medicationFailed=true;
				}
			}			
		}		
	});	
	if(medicationFailed) {
		return false;
	}
	
	if($("input[name='symptom_checklist'][value='410515003']").attr('checked') == true) {
		if($("#symptom_checklist_other").val().length == 0) {
			alert('Symptom Checklist - please provide other information');
			$("#symptom_checklist_other").focus();
			return false;
		}
	}
	
	if($("input[name='risks'][value='410515003']").attr('checked') == true) {
		if($("#risks_other").val().length == 0) {
			alert('Risks - please provide other information');
			$("#risks_other").focus();
			return false;
		}
	}
	
	if($("input[name='addiction_type'][value='410515003']").attr('checked') == true) {
		if($("#addiction_type_other").val().length == 0) {
			alert('Risks - please provide other information');
			$("#addiction_type_other").focus();
			return false;
		}
	}	
	

	
	var ppCount=0;
	$("input[name='presenting_issues']").each(function(){	
		if($(this).attr('checked')==true) {
			ppCount++;
		}
	});	
	
	if(ppCount==0) {
		alert('You must choose atleast 1 presenting issue');
		return false;
	}
	
	
	//alert('submitting');
	return true;
}


function validateStartAndCompletionDates()
{
	var start_date = $("#startDate").val();
	var completion_date = $("#completionDate").val();
	
	sd = start_date.split("-");
	cd = completion_date.split("-");
			
	var sdd = new Date(sd[0],sd[1]-1,sd[2]);
	var cdd = new Date(cd[0],cd[1]-1,cd[2]);

	
	if(cdd <= sdd) {
		alert('Completion Date must be after Start Date');
		return false;
	}
	
	var millis = cdd.getTime()-sdd.getTime();
	var secs = millis/1000;
	var mins = secs/60;
	var hours = mins/60;
	var days = hours/24;
	
	if(days > 30) {
		alert('Completion Date must be within 30 days of Start Date');
		return false;
	}
	return true;
}

function checkForRequired(elementId) {
	var domain = elementId.substring(0,elementId.indexOf('_'));
	var domain_q1_val = $("#"+domain+"_1").val();
	if(domain_q1_val == '1' || domain_q1_val == '2') {
		return true;
	}
	return false;
}