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
	
	
	
	$("input[name='presenting_issues'][value='OTH']").change(function() {
		if($("input[name='presenting_issues'][value='OTH']").attr('checked') == true) {
			$("#presenting_issues_other").attr('disabled','');
			$("#presenting_issues_other").val("");
		} else {
			$("#presenting_issues_other").attr('disabled','disabled');
			$("#presenting_issues_other").val("");
		}		
	});
	
	$("input[name='presenting_issues'][value='OTH']").each(function() {
		if($("input[name='presenting_issues'][value='OTH']").attr('checked') == true) {
			$("#presenting_issues_other").attr('disabled','');			
		} else {
			$("#presenting_issues_other").attr('disabled','disabled');
			$("#presenting_issues_other").val("");
		}		
	});
	
	$("#assessment_status").each(function() {
		if($("#assessment_status").val() == 'Completed') {
			$("#assessment_status").attr('disabled','disabled');			
		}
	});
	
			
	$("#reasonForAssessment").change(function() {
		if($("#reasonForAssessment").val() == 'OTHR') {
			$("#reason_for_assessment_other").attr('disabled','');
			$("#reason_for_assessment_other").val("");
		} else {		
			$("#reason_for_assessment_other").attr('disabled','disabled');
			$("#reason_for_assessment_other").val("");
		}
		
		var demographicId1=$("#clientId").val();;
		var reasonForAssessment1 = $("#reasonForAssessment").val();
		var params={demographicId1:demographicId1,reasonForAssessment1:reasonForAssessment1};

		$("#reasonForAssessmentBlock")
		.load("ocan_check_assessment_type.jsp?", params, function(data){
			
			 $('#reasonForAssessmentBlock').hide();

			if(data.match("false")){				
				alert("You can not create a new initial assessment for this client for now. It already exists in the system.");
				$("#reasonForAssessment").val('').attr("selected", "selected");
			}
		});

	});
	
	$("#reasonForAssessment").each(function() {
		if($("#reasonForAssessment").val() == 'OTHR') {
			$("#reason_for_assessment_other").attr('disabled','');			
		} else {		
			$("#reason_for_assessment_other").attr('disabled','disabled');
			$("#reason_for_assessment_other").val("");
		}
		
	});
	
	$("#consumerSelfAxCompleted").change(function() {
		if($("#consumerSelfAxCompleted").val()=='TRUE' || $("#consumerSelfAxCompleted").val()=='') { 
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','disabled');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','disabled');
			
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
			
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
			
		} else {
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','');
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','');
			
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
			$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
			
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	});
	
	$("#consumerSelfAxCompleted").each(function() {
	if($("#consumerSelfAxCompleted").val()=='TRUE' || $("#consumerSelfAxCompleted").val()=='') { 
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','disabled');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','disabled');
		
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked',false);
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked',false);
		
		$("#otherReason").attr('disabled','disabled');
		$("#otherReason").val("");
		
	} else {
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('disabled','');
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('disabled','');
			
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			$("#otherReason").attr('disabled','');
			
		}else{
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	}
	
	});
	
	$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").change(function() {
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			$("#otherReason").attr('disabled','');
			$("#otherReason").val("");
		}else{
			$("#otherReason").attr('disabled','disabled');
			$("#otherReason").val("");
		}
	
	});
	
	$("#hospitalized_mental_illness").change(function() { 
		if($("#hospitalized_mental_illness").val()=='TRUE'){ 
			$("#hospitalized_mental_illness_admissions").attr('disabled','');
			$("#hospitalized_mental_illness_admissions").val("");
			$("#hospitalized_mental_illness_days").attr('disabled','');
			$("#hospitalized_mental_illness_days").val("");
		} else {
			$("#hospitalized_mental_illness_admissions").attr('disabled','disabled');
			$("#hospitalized_mental_illness_admissions").val("");	
			$("#hospitalized_mental_illness_days").attr('disabled','disabled');
			$("#hospitalized_mental_illness_days").val("");
		}
	});	
	
	$("#hospitalized_mental_illness").each(function() { 
		if($("#hospitalized_mental_illness").val()=='TRUE'){ 
			$("#hospitalized_mental_illness_admissions").attr('disabled','');
			$("#hospitalized_mental_illness_days").attr('disabled','');
		} else {
			$("#hospitalized_mental_illness_admissions").attr('disabled','disabled');
			$("#hospitalized_mental_illness_admissions").val("");
			$("#hospitalized_mental_illness_days").attr('disabled','disabled');
			$("#hospitalized_mental_illness_days").val("");
		}
	});
			
			
	$("#6_physical_health_concerns").change(function() { 
		if($("#6_physical_health_concerns").val()=='TRUE'){ 
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");
			
		} else {
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','disabled');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");			
		}
	});
	
	
	$("#6_physical_health_concerns").each(function() { 
		if($("#6_physical_health_concerns").val()=='TRUE'){ 
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','');
			
			if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
				$("#6_physical_health_details_other").attr('disabled','');
				
			}else{
				$("#6_physical_health_details_other").attr('disabled','disabled');
				$("#6_physical_health_details_other").val("");
			}						
		} else {
			$("input[name='6_physical_health_details'][value='118254002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='279084009']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='119415007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='302293008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='300479008']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='106076001']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118952005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='365092005']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='102957003']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118230007']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='118235002']").attr('disabled','disabled');
			$("input[name='6_physical_health_details'][value='410515003']").attr('disabled','disabled');
			
			$("input[name='6_physical_health_details'][value='118254002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='279084009']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='119415007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='302293008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='300479008']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='106076001']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118952005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='365092005']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='102957003']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118230007']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='118235002']").attr('checked',false);
			$("input[name='6_physical_health_details'][value='410515003']").attr('checked',false);
			
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");				
		}
	});
	
	$("input[name='6_physical_health_details'][value='410515003']").change(function() {
		if($("input[name='6_physical_health_details'][value='410515003']").attr('checked') == true) {
			$("#6_physical_health_details_other").attr('disabled','');
			$("#6_physical_health_details_other").val("");
		}else{
			$("#6_physical_health_details_other").attr('disabled','disabled');
			$("#6_physical_health_details_other").val("");
		}
	
	});
	
	
	//$("#serviceUseRecord_orgLHIN")
});

$("document").ready(function() {
	
	$("#otherContact").each(function() {
		if($("#otherContact").val()!='TRUE' || $("#otherContact").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherContactType").attr('disabled','disabled');
				$("#"+x+"_otherContactType").val("");
				$("#"+x+"_otherContactName").attr('disabled','disabled');
				$("#"+x+"_otherContactName").val("");
				$("#"+x+"_otherContactAddress1").attr('disabled','disabled');
				$("#"+x+"_otherContactAddress1").val("");
				$("#"+x+"_otherContactAddress2").attr('disabled','disabled');
				$("#"+x+"_otherContactAddress2").val("");
				$("#"+x+"_otherContactCity").attr('disabled','disabled');
				$("#"+x+"_otherContactCity").val("");
				$("#"+x+"_otherContactProvince").attr('disabled','disabled');
				$("#"+x+"_otherContactProvince").val("");
				$("#"+x+"_otherContactPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherContactPostalCode").val("");
				$("#"+x+"_otherContactPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherContactPhoneNumber").val("");
				$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherContactPhoneNumberExt").val("");
				$("#"+x+"_otherContactEmail").attr('disabled','disabled');
				$("#"+x+"_otherContactEmail").val("");
				$("#"+x+"_otherContactLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherContactLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherContactType").attr('disabled','');
				$("#"+x+"_otherContactName").attr('disabled','');
				$("#"+x+"_otherContactAddress1").attr('disabled','');
				$("#"+x+"_otherContactAddress2").attr('disabled','');
				$("#"+x+"_otherContactCity").attr('disabled','');
				$("#"+x+"_otherContactProvince").attr('disabled','');
				$("#"+x+"_otherContactPostalCode").attr('disabled','');
				$("#"+x+"_otherContactPhoneNumber").attr('disabled','');
				$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherContactEmail").attr('disabled','');
				$("#"+x+"_otherContactLastSeen").attr('disabled','');
			}
		}
		});
		
	
	$("#otherContact").change(function() {		
	if($("#otherContact").val()!='TRUE' || $("#otherContact").val()=='') { 
		for(var x=1;x<=3;x++) { 
			$("#"+x+"_otherContactType").attr('disabled','disabled');
			$("#"+x+"_otherContactType").val("");
			$("#"+x+"_otherContactName").attr('disabled','disabled');
			$("#"+x+"_otherContactName").val("");
			$("#"+x+"_otherContactAddress1").attr('disabled','disabled');
			$("#"+x+"_otherContactAddress1").val("");
			$("#"+x+"_otherContactAddress2").attr('disabled','disabled');
			$("#"+x+"_otherContactAddress2").val("");
			$("#"+x+"_otherContactCity").attr('disabled','disabled');
			$("#"+x+"_otherContactCity").val("");
			$("#"+x+"_otherContactProvince").attr('disabled','disabled');
			$("#"+x+"_otherContactProvince").val("");
			$("#"+x+"_otherContactPostalCode").attr('disabled','disabled');
			$("#"+x+"_otherContactPostalCode").val("");
			$("#"+x+"_otherContactPhoneNumber").attr('disabled','disabled');
			$("#"+x+"_otherContactPhoneNumber").val("");
			$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','disabled');
			$("#"+x+"_otherContactPhoneNumberExt").val("");
			$("#"+x+"_otherContactEmail").attr('disabled','disabled');
			$("#"+x+"_otherContactEmail").val("");
			$("#"+x+"_otherContactLastSeen").attr('disabled','disabled');
			$("#"+x+"_otherContactLastSeen").val("");
		}
				
	} else {
		for(var x=1;x<=3;x++) { 
			$("#"+x+"_otherContactType").attr('disabled','');
			$("#"+x+"_otherContactName").attr('disabled','');
			$("#"+x+"_otherContactAddress1").attr('disabled','');
			$("#"+x+"_otherContactAddress2").attr('disabled','');
			$("#"+x+"_otherContactCity").attr('disabled','');
			$("#"+x+"_otherContactProvince").attr('disabled','');
			$("#"+x+"_otherContactPostalCode").attr('disabled','');
			$("#"+x+"_otherContactPhoneNumber").attr('disabled','');
			$("#"+x+"_otherContactPhoneNumberExt").attr('disabled','');
			$("#"+x+"_otherContactEmail").attr('disabled','');
			$("#"+x+"_otherContactLastSeen").attr('disabled','');
		}
	}
	});
	
	
	$("#otherAgency").each(function() {
		if($("#otherAgency").val()!='TRUE' || $("#otherAgency").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','disabled');
				$("#"+x+"_otherAgencyType").val("");
				$("#"+x+"_otherAgencyName").attr('disabled','disabled');
				$("#"+x+"_otherAgencyName").val("");
				$("#"+x+"_otherAgencyAddress1").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress1").val("");
				$("#"+x+"_otherAgencyAddress2").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress2").val("");
				$("#"+x+"_otherAgencyCity").attr('disabled','disabled');
				$("#"+x+"_otherAgencyCity").val("");
				$("#"+x+"_otherAgencyProvince").attr('disabled','disabled');
				$("#"+x+"_otherAgencyProvince").val("");
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPostalCode").val("");
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumber").val("");
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumberExt").val("");
				$("#"+x+"_otherAgencyEmail").attr('disabled','disabled');
				$("#"+x+"_otherAgencyEmail").val("");
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherAgencyLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','');
				$("#"+x+"_otherAgencyName").attr('disabled','');
				$("#"+x+"_otherAgencyAddress1").attr('disabled','');
				$("#"+x+"_otherAgencyAddress2").attr('disabled','');
				$("#"+x+"_otherAgencyCity").attr('disabled','');
				$("#"+x+"_otherAgencyProvince").attr('disabled','');
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherAgencyEmail").attr('disabled','');
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','');
			}
		}
		});
	
	
	
	$("#otherAgency").change(function() {		
		if($("#otherAgency").val()!='TRUE' || $("#otherAgency").val()=='') { 
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','disabled');
				$("#"+x+"_otherAgencyType").val("");
				$("#"+x+"_otherAgencyName").attr('disabled','disabled');
				$("#"+x+"_otherAgencyName").val("");
				$("#"+x+"_otherAgencyAddress1").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress1").val("");
				$("#"+x+"_otherAgencyAddress2").attr('disabled','disabled');
				$("#"+x+"_otherAgencyAddress2").val("");
				$("#"+x+"_otherAgencyCity").attr('disabled','disabled');
				$("#"+x+"_otherAgencyCity").val("");
				$("#"+x+"_otherAgencyProvince").attr('disabled','disabled');
				$("#"+x+"_otherAgencyProvince").val("");
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPostalCode").val("");
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumber").val("");
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','disabled');
				$("#"+x+"_otherAgencyPhoneNumberExt").val("");
				$("#"+x+"_otherAgencyEmail").attr('disabled','disabled');
				$("#"+x+"_otherAgencyEmail").val("");
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','disabled');
				$("#"+x+"_otherAgencyLastSeen").val("");
			}
					
		} else {
			for(var x=1;x<=3;x++) { 
				$("#"+x+"_otherAgencyType").attr('disabled','');
				$("#"+x+"_otherAgencyName").attr('disabled','');
				$("#"+x+"_otherAgencyAddress1").attr('disabled','');
				$("#"+x+"_otherAgencyAddress2").attr('disabled','');
				$("#"+x+"_otherAgencyCity").attr('disabled','');
				$("#"+x+"_otherAgencyProvince").attr('disabled','');
				$("#"+x+"_otherAgencyPostalCode").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumber").attr('disabled','');
				$("#"+x+"_otherAgencyPhoneNumberExt").attr('disabled','');
				$("#"+x+"_otherAgencyEmail").attr('disabled','');
				$("#"+x+"_otherAgencyLastSeen").attr('disabled','');
			}
		}
		});	
	
	
	
	
	
});


function submitOcanForm() {
	var status = document.getElementById('assessment_status').value;
	if(status != 'Completed') {
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
	if($("#date_of_birth").val().length==0) {	
		if($("#clientDOBType").val() != 'UNK') {
			alert('Date of Birth - do not know the date of birth? please choose unknown');
			$("#clientDOBType").focus();
			return false;
		}
	}
	
	if($("#clientDOBType").val() == 'UNK') {		
		if($("#date_of_birth").val().length!=0) {			
			alert('Date of Birth (YYYY-MM-DD) - You should not provide date of birth and choose unknown at the same time');
			$("#date_of_birth").focus();
			return false;
		}		
	}
	
	
	var newCount = $("#center_count").val(); 
	var ocanLeadNumber = 0;
	for(var x=1;x<=newCount;x++) { 
		if($("#exitDate"+x).val().length != 0) {		
			if($("#serviceUseRecord_exitDisposition"+x).val().length==0) {			
				alert('Exit Disposition - Please specify one');
				$("#serviceUseRecord_exitDisposition"+x).focus();
				return false;
			}		
		}
		
		if($("#serviceUseRecord_OCANLead"+x).val() == 'TRUE') { 
			ocanLeadNumber ++;
		}
	}
	if (ocanLeadNumber > 1) {
		alert('OCAN Lead can only have one answered Yes');
		$("#serviceUseRecord_OCANLead1").focus();
		return false;
	}	
		
	if($("#completedByOCANLead").val() == 'FALSE') { 
		if($("#reasonForAssessment").val() != 'REV' && $("#reasonForAssessment").val() != 'REK') {
			alert('Reason for OCAN -- This OCAN was not completed by OCAN lead. You can only choose Re-view or Rekey');
			$("#reasonForAssessment").focus();
			return false;
		}
		
	}	
	
	
	if($("#reasonForAssessment").val() == 'OTHR') {		
		if($("#reason_for_assessment_other").val().length==0) {			
			alert('Reason for assessment - Please specify other');
			$("#reason_for_assessment_other").focus();
			return false;
		}		
	}
	
		
	if($("#consumerSelfAxCompleted").val() == 'FALSE') {
		if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='CMFLVL']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LOA']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LIT']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='MHC']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='PYSCON']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='LANG']").attr('checked') == false &&
		$("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == false 
		) {
			alert('Reason for the Consumer Self-Assessment not completed -- please select one');
			$("#consumerSelfAxCompleted").focus();
			return false;
		}
	}
	
	if($("input[name='reasonConsumerSelfAxNotCompletedList'][value='OTH']").attr('checked') == true) {
			if($("#otherReason").val().length==0) {
				alert('Other reason for the Consumer Self-Assessment not completed -- please provide other reason');
				$("#otherReason").focus();
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
	

		
	if($("#hospitalized_mental_illness").val()=='TRUE') {
		if($("#hospitalized_mental_illness_admissions").val().length == 0) {
			alert('Please input - Total Number of Admissions for Mental Health Reasons');
			$("#hospitalized_mental_illness_admissions").focus();
			return false;
		}
		if($("#hospitalized_mental_illness_days").val().length == 0) {
			alert('Please input - Total Number of Hospitalization Days for Mental Health Reasons');
			$("#hospitalized_mental_illness_days").focus();
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
		$("input[name='presenting_issues']").focus();
		return false;
	}
	
	
	if($("#assessment_status").val() == 'Completed') {
		var r = comfirm("Are you sure you have completed this assessment?");
		if(r == true) {
			return true;
		}
		else {
			return false;
		}
	}
		
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
	
	// a warning message may be displayed when
	//Completion Date is greater than 30 days from Start Date but this should not
	//prevent you from completing and saving the OCAN.
	if(days > 30) {
		alert('Completion Date must be within 30 days of Start Date');
		
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
