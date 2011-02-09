
$(document).ready(function() {


	$("input[name='intake.answerMapped(201146).value']").change(function(){
		$("input[name='intake.answerMapped(201145).value']").attr('checked',true);
	});

	//initialize validate - we can just do this in main jsp
	$("form").validate({meta: "validate"});

	//service org name/number - set variables, and set to read-only
	$("input[question_id='service_org_name']").val($("#facility_name").val());
	$("input[question_id='service_org_number']").val($("#ocan_service_org_number").val());
	$("input[question_id='service_org_name']").attr("disabled", true); 
	$("input[question_id='service_org_number']").attr("disabled", true);
	
	
	//onsubmit
	$("form").submit(function() { 
		if(!$("form").valid()) {
			return false;
		}

		if(!validateStartAndCompletionDates()) {
			return false;
		}
		
		if($("select[question_id='reason_for_assessment']").val() == 'OTHR') {			
			if($("[question_id='reason_for_assessment_other']").val().length==0) {
				alert('Reason for assessment - Please specify other');
				return false;
			}		
		}
		
		alert('passed validation');
		return false;
	});

});

function validateStartAndCompletionDates()
{
	var start_date = $("input[question_id='start_date']").val();
	var completion_date = $("input[question_id='completion_date']").val();
	
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