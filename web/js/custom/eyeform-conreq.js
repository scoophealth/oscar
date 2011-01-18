jQuery(document).ready(function(){
	
	jQuery.ajax({url:ctx+"/eyeform/Eyeform.do?method=getConReqCC&requestId="+requestId,dataType: "html",success: function(data) {		
		jQuery("#conReqSendTo").after(data);
	}});
	
	
	
});
