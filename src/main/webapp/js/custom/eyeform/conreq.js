jQuery(document).ready(function(){

	jQuery.ajax({url:ctx+"/eyeform/Eyeform.do?method=getConReqCC&requestId="+requestId,dataType: "html",success: function(data) {
		jQuery("#conReqSendTo").after(data);
	}});

	jQuery.ajax({url:ctx+"/eyeform/conspecial.jsp",dataType: "html",async:false, success: function(data) {
		jQuery("#trConcurrentProblems").after(data);

	}});

	jQuery.getScript(ctx+"/eyeform/Eyeform.do?method=specialConRequest&requestId="+requestId+"&demographicNo="+demographicNo+"&appNo="+appointmentNo,function(data,status){});

});
