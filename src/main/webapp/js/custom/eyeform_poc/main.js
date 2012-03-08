//navlist
jQuery(document).ready(function(){
//	jQuery("#navlist").append("<li><a href=\"../eyeform/ConsultationReportList.do\">ConReport</a></li>");
	jQuery("<li><a href=\"#\" onclick=\"popupOscarRx(625,1024,'../eyeform/ConsultationReportList.do\');\" title\"View Consultation Reports\">ConReport</a></li>").insertAfter("#con");
	
});
