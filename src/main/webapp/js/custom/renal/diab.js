function addToDx(codingSystem, code) {
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=addtoDx&demographicNo="+getDemographicNo()+"&codingSystem="+codingSystem+"&code="+code,async:false, success:function(data) {
		if(data.result != '0') {
			alert('added to Dx');
		}
		location.href='TemplateFlowSheet.jsp?demographic_no='+getDemographicNo()+'&template=diab2';
	}});
}

jQuery(document).ready(function(){
	jQuery("#print_box").before(
		"<div class=\"leftBox\">" +
		"<h3>&nbsp;Renal  <a class=\"DoNotPrint\" href=\"#\" onclick=\"Element.toggle('renalFullListing'); return false;\" style=\"font-size:x-small;\" >show/hide</a></h3>" +
		"<div class=\"wrapper\" id=\"renalFullListing\">" +
		"<ul id=\"dxaddshortcut_list\">" +
		"<li><b>Next Steps:</b><span id='renal_next_steps'></span></li>" +
		"</ul>" +
		"</div>" +
        "</div>");
	
	jQuery("#add_overdue").after(
			"<p id=\"add_renal\">" +
			"<a class=\"DoNotPrint\" href=\"javascript: function myFunction() {return false; }\"  onclick=\"javascript:fsPopup(760,670,'AddMeasurementData.jsp?demographic_no="+getDemographicNo()+"&measurement=EGFR&measurement=ACR&measurement=AORA&template=diab2','addMeasurementDataRenal')\" TITLE=\"Add renal measurements.\">" +
			"Add Renal" +
			"</a></p>"
	);
	
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=250",async:false, success:function(data) {
		  //got it
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<li><a href='javascript:void(0);' onclick=\"addToDx('icd9','250');return false;\">Add Diabetes to Dx</a></li>");
		}
	}});
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=401",async:false, success:function(data) {
		  //got it
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<li><a href='javascript:void(0);' onclick=\"addToDx('icd9','401');return false;\">Add Hypertension to Dx</a></li>");
		}
	}});
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=585",async:false, success:function(data) {
		  //got it
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<li><a href='javascript:void(0);' onclick=\"addToDx('icd9','585');return false;\">Add Chronic Renal Failure to Dx</a></li>");
		}
	}});
	
	//next steps
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=getNextSteps&demographicNo="+getDemographicNo(),async:false, success:function(data) {
		jQuery("#renal_next_steps").html(data.result);
	}});
	
	
});