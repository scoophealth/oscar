jQuery(document).ready(function(){
	//add checkboxes
	jQuery("#apptHistoryTbl tr:first").html("<th width=\"5%\"></th>"+jQuery("#apptHistoryTbl tr:first").html());
	jQuery("#apptHistoryTbl tr:first").html(jQuery("#apptHistoryTbl tr:first").html() + "<th width=\"15%\">Consult</th>");
	jQuery("#apptHistoryTbl tr:gt(0)").each(function() {
		var eURL = ctx + "/oscarEncounter/IncomingEncounter.do?providerNo="+jQuery(this).attr("providerNo")
			+ "&appointmentNo=" + jQuery(this).attr("appt_no") + "&demographicNo=" + jQuery(this).attr("demographic_no")
			+ "&curProviderNo=" + jQuery(this).attr("curProviderNo") + "&reason=" + jQuery(this).attr("reason")
			+ "&encType=" + jQuery(this).attr("encType") + "&userName="  + jQuery(this).attr("userName")
			+ "&curDate="  + jQuery(this).attr("curDate") + "&appointmentDate="  + jQuery(this).attr("appointmentDate")
			+ "&status="  + jQuery(this).attr("status") + "&apptProvider_no="  + jQuery(this).attr("curProviderNo")
			+ "&providerview=" + jQuery(this).attr("curProviderNo");
		var requestUrl = ctx + "/oscarEncounter/oscarConsultationRequest/ConsultationFormRequest.jsp?de="+jQuery(this).attr("demographic_no")+"&teamVar=&appNo="+jQuery(this).attr("appt_no");
		var reportUrl = ctx + "/eyeform/Eyeform.do?method=prepareConReport&demographicNo="+jQuery(this).attr("demographic_no")+"&appNo="+jQuery(this).attr("appt_no")+"&flag=new";
		jQuery(this).html("<td><input type=\"checkbox\" name=\"sel\" value=\""+jQuery(this).attr("appt_no")+"\"/></td>" + jQuery(this).html() 
				+ "<td><a href=\""+eURL+"\">E</a>&nbsp;<a href=\""+requestUrl+"\">Request</a>&nbsp;<a href=\""+reportUrl+"\">Report</a></td>");
	});
	
	//add button	
	jQuery("#apptHistoryTbl").append("<tr><td colspan=\"7\"><input type=\"button\" onclick=\"selectAllCheckboxes()\" value=\"Select All\"/>&nbsp;<input type=\"button\" onclick=\"deselectAllCheckboxes()\" value=\"Deselect All\"/><input type=\"button\" value=\"Print Visit Data\" onclick=\"printVisit_eyeform2()\"/></td></tr>");
});
