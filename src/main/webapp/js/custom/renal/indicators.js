function addToDx(codingSystem, code) {
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=addtoDx&demographicNo="+getDemographicNo()+"&codingSystem="+codingSystem+"&code="+code,async:false, success:function(data) {
		if(data.result != '0') {
			alert('added to Dx');
			location.href='TemplateFlowSheet.jsp?demographic_no='+getDemographicNo()+'&template=diab3';
		}
	}});
}

jQuery(document).ready(function(){
	$('#Renal_update').before(
			"<tr><td class='field'><b>Next Steps</b></td>" +
			"<td colspan='4' class='comments' id='renal_next_steps'>Consider Referral</td>" +
			"<td align='right' style='border-top: 1px solid #9d9d9d;'></td></tr>" +
			"<tr><td class='comments'><b>Web Links</b></td>" + 
			"<td colspan='5'><a target='_blank' href='"+getContextPath()+"/renal/csn_algorithm.pdf'>CSN Algorithm Detection, Monitoring & Referral of CKD</a></td></tr>" +
			"<tr><td>&nbsp</td><td colspan='5'><a target='_blank' href='"+getContextPath()+"/renal/kfoc.pdf'>Kidney Foundation of Canada</a></td></tr>" +
			"<tr><td></td><td id='dxaddshortcut_list' colspan='5'>"+
			"</td></tr>"
	);

	
	$('#Renal_update_comments').css('border','none');
	
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=250",async:false, success:function(data) {
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<input type='button' onclick=\"addToDx('icd9','250');return false;\" value='Add Diabetes to Dx'/>&nbsp;");
		}
	}});
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=401",async:false, success:function(data) {
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<input type='button' onclick=\"addToDx('icd9','401');return false;\" value='Add Hypertension to Dx'/>&nbsp;");
		}
	}});
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=checkForDx&demographicNo="+getDemographicNo()+"&codingSystem=icd9&code=585",async:false, success:function(data) {
		if(data.result == false) {
			jQuery('#dxaddshortcut_list').append("<input type='button' onclick=\"addToDx('icd9','585');return false;\" value='Add Chronic Renal Failure to Dx'/>");
		}
	}});

	//next steps
	jQuery.ajax({url:getContextPath() + "/renal/Renal.do?method=getNextSteps&demographicNo="+getDemographicNo(),async:false, success:function(data) {
		jQuery("#renal_next_steps").html(data.result);
	}});
	
});
