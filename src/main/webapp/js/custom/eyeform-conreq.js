jQuery(document).ready(function(){
	
	jQuery.ajax({url:ctx+"/eyeform/Eyeform.do?method=getConReqCC&requestId="+requestId,dataType: "html",success: function(data) {
		//alert(data);
		jQuery("#conReqSendTo").after(data);
	}});
	
	jQuery.ajax({url:ctx+"/eyeform/Eyeform.do?method=specialConRequest&requestId="+requestId+"&demographicNo="+demographicNo+"&appNo="+appointmentNo,dataType: "html",success: function(data) {
		//alert(data);
		jQuery("#trConcurrentProblems").after(data);
	}});	
	
});
