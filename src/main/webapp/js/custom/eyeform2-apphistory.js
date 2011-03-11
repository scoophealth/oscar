jQuery(document).ready(function(){
	//add checkboxes
	jQuery("#apptHistoryTbl tr:first").html("<th width=\"5%\"></th>"+jQuery("#apptHistoryTbl tr:first").html());
	jQuery("#apptHistoryTbl tr:gt(0)").each(function() {				
		jQuery(this).html("<td><input type=\"checkbox\" name=\"sel\" value=\""+jQuery(this).attr("appt_no")+"\"/></td>" + jQuery(this).html());
	});
	
	//add button	
	jQuery("#apptHistoryTbl").append("<tr><td colspan=\"7\"><input type=\"button\" value=\"Print Visit Data\" onclick=\"printVisit()\"/></td></tr>");
});
