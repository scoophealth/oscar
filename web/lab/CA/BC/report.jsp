<%
	if(session.getAttribute("user") == null || !session.getAttribute("userprofession").equals("doctor")){
    	response.sendRedirect("../../../logout.jsp");
    }
	String select_pid_information = "SELECT patient_name, external_id, date_of_birth, patient_address, sex, home_number  FROM hl7_pid WHERE hl7_pid.pid_id='@pid'",
		select_header_information = "SELECT DISTINCT filler_order_number, requested_date_time, observation_date_time, ordering_provider, result_copies_to FROM hl7_OBR WHERE pid_id = '@pid'",
		select_lab_results = "SELECT hl7_obr.filler_order_number, hl7_obr.universal_service_id, hl7_obr.ordering_provider, hl7_obr.results_report_status_change, hl7_obr.diagnostic_service_sect_id, hl7_obr.result_status, hl7_obr.result_copies_to, hl7_obr.note as obrnote, hl7_obx.set_id, hl7_obx.observation_identifier, hl7_obx.observation_results, hl7_obx.units, hl7_obx.reference_range, hl7_obx.abnormal_flags, hl7_obx.observation_result_status, hl7_obx.note as obxnote FROM hl7_obr left join hl7_obx on hl7_obr.obr_id=hl7_obx.obr_id WHERE hl7_obr.pid_id='@pid' ORDER BY hl7_obr.diagnostic_service_sect_id",
		select_signed = "SELECT hl7_pid.pid_id, hl7_link.status, hl7_link.provider_no, hl7_link.signed_on, provider.last_name, provider.first_name FROM hl7_pid left join hl7_link on hl7_pid.pid_id=hl7_link.pid_id left join provider on provider.provider_no=hl7_link.provider_no WHERE hl7_pid.pid_id='@pid';",
		update_lab_report_signed = "UPDATE hl7_link SET hl7_link.status='S', hl7_link.provider_no='@provider_no', hl7_link.signed_on=NOW() WHERE hl7_link.pid_id='@pid';",
		update_lab_report_viewed = "UPDATE hl7_link SET hl7_link.status='A' WHERE hl7_link.pid_id='@pid' AND hl7_link.status!='S';",
		select_doc_notes = "SELECT hl7_message.notes FROM hl7_pid, hl7_message WHERE hl7_pid.pid_id='@pid' AND hl7_pid.message_id=hl7_message.message_id;",
		update_doc_notes = "UPDATE hl7_pid, hl7_message SET hl7_message.notes='@notes' WHERE hl7_pid.pid_id='@pid' AND hl7_pid.message_id=hl7_message.message_id;";
	String pid = request.getParameter("pid"),
	sign = request.getParameter("cmd_sign"),
	save = request.getParameter("cmd_save");
	if(null != save){
		oscar.oscarDB.DBHandler dbSave = new oscar.oscarDB.DBHandler(oscar.oscarDB.DBHandler.OSCAR_DATA);
		dbSave.RunSQL(update_doc_notes.replaceAll("@pid", pid).replaceAll("@notes", oscar.Misc.mysqlEscape((String)request.getParameter("notes"))));
		dbSave.CloseConn();
	}
	if(null != sign){
		oscar.oscarDB.DBHandler dbSign = new oscar.oscarDB.DBHandler(oscar.oscarDB.DBHandler.OSCAR_DATA);
		dbSign.RunSQL(update_lab_report_signed.replaceAll("@pid", sign).replaceAll("@provider_no", (String)session.getAttribute("user")));
		dbSign.CloseConn();
	}
	if(null == pid){
		out.print("<script language=\"JavaScript\">window.close();</script>");
	}
	oscar.oscarDB.DBHandler db = new oscar.oscarDB.DBHandler(oscar.oscarDB.DBHandler.OSCAR_DATA);
	if(request.getParameter("viewed") != null){
		db.RunSQL(update_lab_report_viewed.replaceAll("@pid", pid));
	}
	java.sql.ResultSet rs = db.GetSQL(select_signed.replaceAll("@pid", pid));
	if(rs.next()){
		boolean signed = (rs.getString("status")!=null && rs.getString("status").equalsIgnoreCase("S"));
%>
<html>
<head>
<title>OSCAR PathNET - View Lab Report</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
window.opener.location.reload();
function Sign(check){
	check.form.submit();
}
</script>
</head>
<body>
<form name="signForm" action="report.jsp" method="post">
<table width="100%" class="DarkBG">
  <tr> 
    <td height="40" width="25"></td>
    <td width="50%" align="left"> 
      <font color="#4D4D4D"><b><font size="4">oscar<font size="3">PathNET - View Lab Report</font></font></b></font> 
    </td>
    <td align="right" class="Text" nowrap><%=(signed? ((rs.getString("last_name")!=null)? "<b>Signed Off By: </b>" +  rs.getString("last_name") + ", " + rs.getString("first_name") : "<b>Signed Off By Provider No.:</b> " + rs.getString("provider_no")) + " on " + rs.getString("signed_on") : "" )%>
    	<input type="checkbox" name="cmd_sign" onclick="Sign(this);" value="<%=pid%>" <%=(signed? "checked disabled" : "")%>/><input type="hidden" name="pid" value="<%=pid%>" />Sign</td>
  </tr>
</table>
<%
	}
	rs.close();
	rs = db.GetSQL(select_pid_information.replaceAll("@pid", pid));
	if(rs.next()){
		int age = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-d HH:mm:ss");
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(format.parse(rs.getString("date_of_birth")));
		age = oscar.MyDateFormat.getAge(calendar.get(java.util.GregorianCalendar.YEAR), calendar.get(java.util.GregorianCalendar.MONTH), calendar.get(java.util.GregorianCalendar.DATE));
%>
<table width="100%">
  <tr>
  	<td colspan="4" align="right"><input type="button" name="print" value="Print" onclick="window.print(); return false;" /></td>
  </tr>
	<tr>
		<td class="Text" width="100px">Patient:</td>
		<td class="Text"><%=rs.getString("patient_name")%></td>
		<td class="Text" align="right">DOB:</td>
		<td class="Text" width="100px"><%=rs.getString("date_of_birth").substring(0, rs.getString("date_of_birth").indexOf(" "))%></td>
	</tr>
	<tr>
		<td class="Text">PHN:</td>
		<td class="Text"><%=rs.getString("external_id")%></td>
		<td class="Text" align="right">Age:</td>
		<td class="Text"><%=age%></td>
	</tr>
	<tr>
		<td class="Text"></td>
		<td class="Text"></td>
		<td class="Text" align="right">Sex:</td>
		<td class="Text"><%=rs.getString("sex")%></td>
	</tr>
	<tr>
		<td class="Text">Address:</td>
		<td class="Text" colspan="3"><%=rs.getString("patient_address").replaceAll("\\\\\\.br\\\\", " ")%></td>
	</tr>
	<tr>
		<td class="Text">Phone:</td>
		<td class="Text"><%=rs.getString("home_number")%></td>
		<td class="Text"></td>
		<td class="Text"></td>
	</tr>
<%
	}
	rs.close();
	rs = db.GetSQL(select_header_information.replaceAll("@pid", pid));
	if(rs.next()){
%>
	<tr><td colspan="4">&nbsp;</td></tr>
	<tr>
		<td class="Text">Lab:</td>
		<td class="Text" colspan="3"><%=rs.getString("filler_order_number").substring(0, rs.getString("filler_order_number").indexOf("-", 3))%></td>
	</tr>
	<tr>
		<td class="Text">Ordered By:</td>
		<td class="Text"><%=rs.getString("ordering_provider").replaceAll("~", ",<br/>")%></td>
		<td class="Text">Requested On:</td>
		<td class="Text"><%=rs.getString("requested_date_time")%></td>
	</tr>
	<tr>
		<td class="Text">Copies To:</td>
		<td class="Text"><%=rs.getString("result_copies_to").replaceAll("~", ",<br/>")%></td>
		<td class="Text">Observed On:</td>
		<td class="Text"><%=rs.getString("observation_date_time")%></td>
	</tr>
<%
	}
	rs.close();
%>
</table>
<table cellspacing="0" cellpadding="0" width="100%">
<%
	rs = db.GetSQL(select_lab_results.replaceAll("@pid", pid));
	boolean other = true;
	String section = "";
	while(rs.next()){
		if(rs.getString("set_id") == null || rs.getInt("set_id") == 1){
			if(!section.equalsIgnoreCase(rs.getString("diagnostic_service_sect_id"))){
				section = rs.getString("diagnostic_service_sect_id");
%>
	<tr>
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td class="Section" colspan="7"><%=((rs.getString("diagnostic_service_sect_id")!=null)? rs.getString("diagnostic_service_sect_id") : "Other")%></td>
	</tr>
<%
			}
%>
	<tr>
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td class="Text" colspan="3"><b>Service Id:</b><%=rs.getString("universal_service_id").substring(rs.getString("universal_service_id").indexOf(" "))%></td>
		<td class="Text" nowrap><b>Last Modified:</b><%=rs.getString("results_report_status_change")%></td>
		<td class="Text" nowrap colspan="3"><b>Result Status:</b><%=(rs.getString("result_status").equalsIgnoreCase("f")? "Final" : "Pending")%></td>
	</tr>
	<tr>
		<td class="Text" valign="top">Note:</td>
		<td class="Text" colspan="6"><%=rs.getString("obrnote").replaceAll("\\\\\\.br\\\\", " ")%>&nbsp;</td>
	</tr>
<%
		}
		if(rs.getString("set_id") != null){
			if(rs.getInt("set_id") == 1){
			other = true;
%>
	<tr>
		<td>&nbsp;</td>
		<td style="width:15%;" class="Header">Test</td>
		<td style="width:5%;" class="Header">Flags</td>
		<td style="width:55%;" class="Header">Results</td>
		<td style="width:8%;" class="Header">Range</td>
		<td style="width:8%;" class="Header">Units</td>
		<td class="Header">Note</td>
	</tr>
<%
			}
%>
	<tr>
		<td>&nbsp;</td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=rs.getString("observation_identifier").substring(rs.getString("observation_identifier").indexOf(" "))%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><b><%=((rs.getString("abnormal_flags").toUpperCase().equals("N"))? "&nbsp;" : oscar.Misc.check(rs.getString("abnormal_flags"), "", "&nbsp;"))%></b></td>
		<td class="Text" class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((rs.getString("abnormal_flags").toUpperCase().equals("N"))? rs.getString("observation_results") : "<b>" + rs.getString("observation_results") + "</b>").replaceAll("\\\\\\.br\\\\", " ")%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=rs.getString("reference_range")%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=rs.getString("units")%></td>
		<td class="Text" nowrap title="<%=rs.getString("obxnote").replaceAll("\\\\\\.br\\\\", " ")%>" class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((rs.getString("obxnote").length() < 20)? rs.getString("obxnote") : rs.getString("obxnote").substring(0, 20)).replaceAll("\\\\\\.br\\\\", " ")%></td>
	</tr>
<%
		}
		other = !other;
	}
	rs.close();
	rs = db.GetSQL(select_doc_notes.replaceAll("@pid", pid));
	if(rs.next()){
%>
	<tr>
		<td colspan="7"><br><b>Notes:</b></td>
	</tr>
	<tr>
		<td colspan="7"><textarea name="notes" rows="7" style="width:100%;"><%=oscar.Misc.check(rs.getString("notes"), "")%></textarea></td>
	</tr>
	<tr class="LightBG">
		<td colspan="7" align="right"><input type="submit" name="cmd_save" value="Save" /></td>
	</tr>
</table>
<%
	}
	rs.close();
	db.CloseConn();
%>
</form>
</body>
</html>