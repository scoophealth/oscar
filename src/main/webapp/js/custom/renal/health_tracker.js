function addToDx(codingSystem, code) {
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=addtoDx&demographicNo="+getDemographicNo()+"&codingSystem="+codingSystem+"&code="+code,async:false, success:function(data) {
		if(data.result != '0') {
			alert('added to Dx');
			location.href='TemplateFlowSheet.jsp?demographic_no='+getDemographicNo()+'&template=diab3';
		}
	}});
}

jQuery(document).ready(function(){

	//next steps
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=getNextSteps&demographicNo="+getDemographicNo(),async:false, success:function(data) {
		jQuery("#renal_next_steps").html(data.result);
	}});
	
});
