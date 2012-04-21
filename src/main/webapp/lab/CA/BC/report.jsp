<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

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
		DBHandler.RunSQL(update_doc_notes.replaceAll("@pid", pid).replaceAll("@notes", oscar.Misc.mysqlEscape(request.getParameter("notes"))));
	}
	if(null != sign){
		DBHandler.RunSQL(update_lab_report_signed.replaceAll("@pid", sign).replaceAll("@provider_no", (String)session.getAttribute("user")));
	}
	if(null == pid){
		out.print("<script language=\"JavaScript\">window.close();</script>");
	}
	if(request.getParameter("viewed") != null){
		DBHandler.RunSQL(update_lab_report_viewed.replaceAll("@pid", pid));
	}
	java.sql.ResultSet rs = DBHandler.GetSQL(select_signed.replaceAll("@pid", pid));
	if(rs.next()){
		boolean signed = (oscar.Misc.getString(rs,"status")!=null && oscar.Misc.getString(rs,"status").equalsIgnoreCase("S"));
%>

<%@page import="oscar.oscarDB.DBHandler"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
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
		<td width="50%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - View Lab Report</font></font></b></font>
		</td>
		<td align="right" class="Text" nowrap><%=(signed? ((oscar.Misc.getString(rs,"last_name")!=null)? "<b>Signed Off By: </b>" +  oscar.Misc.getString(rs,"last_name") + ", " + oscar.Misc.getString(rs,"first_name") : "<b>Signed Off By Provider No.:</b> " + oscar.Misc.getString(rs,"provider_no")) + " on " + oscar.Misc.getString(rs,"signed_on") : "" )%>
		<input type="checkbox" name="cmd_sign" onclick="Sign(this);"
			value="<%=pid%>" <%=(signed? "checked disabled" : "")%> /><input
			type="hidden" name="pid" value="<%=pid%>" />Sign</td>
	</tr>
</table>
<%
	}
	rs.close();
	rs = DBHandler.GetSQL(select_pid_information.replaceAll("@pid", pid));
	if(rs.next()){
		int age = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-d HH:mm:ss");
		java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(format.parse(oscar.Misc.getString(rs,"date_of_birth")));
		age = oscar.MyDateFormat.getAge(calendar.get(java.util.GregorianCalendar.YEAR), calendar.get(java.util.GregorianCalendar.MONTH), calendar.get(java.util.GregorianCalendar.DATE));
%>
<table width="100%">
	<tr>
		<td colspan="4" align="right"><input type="button" name="print"
			value="Print" onclick="window.print(); return false;" /></td>
	</tr>
	<tr>
		<td class="Text" width="100px">Patient:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"patient_name")%></td>
		<td class="Text" align="right">DOB:</td>
		<td class="Text" width="100px"><%=oscar.Misc.getString(rs,"date_of_birth").substring(0, oscar.Misc.getString(rs,"date_of_birth").indexOf(" "))%></td>
	</tr>
	<tr>
		<td class="Text">PHN:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"external_id")%></td>
		<td class="Text" align="right">Age:</td>
		<td class="Text"><%=age%></td>
	</tr>
	<tr>
		<td class="Text"></td>
		<td class="Text"></td>
		<td class="Text" align="right">Sex:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"sex")%></td>
	</tr>
	<tr>
		<td class="Text">Address:</td>
		<td class="Text" colspan="3"><%=oscar.Misc.getString(rs,"patient_address").replaceAll("\\\\\\.br\\\\", " ")%></td>
	</tr>
	<tr>
		<td class="Text">Phone:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"home_number")%></td>
		<td class="Text"></td>
		<td class="Text"></td>
	</tr>
	<%
	}
	rs.close();
	rs = DBHandler.GetSQL(select_header_information.replaceAll("@pid", pid));
	if(rs.next()){
%>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td class="Text">Lab:</td>
		<td class="Text" colspan="3"><%=oscar.Misc.getString(rs,"filler_order_number").substring(0, oscar.Misc.getString(rs,"filler_order_number").indexOf("-", 3))%></td>
	</tr>
	<tr>
		<td class="Text">Ordered By:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"ordering_provider").replaceAll("~", ",<br/>")%></td>
		<td class="Text">Requested On:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"requested_date_time")%></td>
	</tr>
	<tr>
		<td class="Text">Copies To:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"result_copies_to").replaceAll("~", ",<br/>")%></td>
		<td class="Text">Observed On:</td>
		<td class="Text"><%=oscar.Misc.getString(rs,"observation_date_time")%></td>
	</tr>
	<%
	}
	rs.close();
%>
</table>
<table cellspacing="0" cellpadding="0" width="100%">
	<%
	rs = DBHandler.GetSQL(select_lab_results.replaceAll("@pid", pid));
	boolean other = true;
	String section = "";
	while(rs.next()){
		if(oscar.Misc.getString(rs,"set_id") == null || rs.getInt("set_id") == 1){
			if(!section.equalsIgnoreCase(oscar.Misc.getString(rs,"diagnostic_service_sect_id"))){
				section = oscar.Misc.getString(rs,"diagnostic_service_sect_id");
%>
	<tr>
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td class="Section" colspan="7"><%=((oscar.Misc.getString(rs,"diagnostic_service_sect_id")!=null)? oscar.Misc.getString(rs,"diagnostic_service_sect_id") : "Other")%></td>
	</tr>
	<%
			}
%>
	<tr>
		<td colspan="7">&nbsp;</td>
	</tr>
	<tr>
		<td class="Text" colspan="3"><b>Service Id:</b><%=oscar.Misc.getString(rs,"universal_service_id").substring(oscar.Misc.getString(rs,"universal_service_id").indexOf(" "))%></td>
		<td class="Text" nowrap><b>Last Modified:</b><%=oscar.Misc.getString(rs,"results_report_status_change")%></td>
		<td class="Text" nowrap colspan="3"><b>Result Status:</b><%=(oscar.Misc.getString(rs,"result_status").equalsIgnoreCase("f")? "Final" : "Pending")%></td>
	</tr>
	<tr>
		<td class="Text" valign="top">Note:</td>
		<td class="Text" colspan="6"><%=oscar.Misc.getString(rs,"obrnote").replaceAll("\\\\\\.br\\\\", " ")%>&nbsp;</td>
	</tr>
	<%
		}
		if(oscar.Misc.getString(rs,"set_id") != null){
			if(rs.getInt("set_id") == 1){
			other = true;
%>
	<tr>
		<td>&nbsp;</td>
		<td style="width: 15%;" class="Header">Test</td>
		<td style="width: 5%;" class="Header">Flags</td>
		<td style="width: 55%;" class="Header">Results</td>
		<td style="width: 8%;" class="Header">Range</td>
		<td style="width: 8%;" class="Header">Units</td>
		<td class="Header">Note</td>
	</tr>
	<%
			}
%>
	<tr>
		<td>&nbsp;</td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=oscar.Misc.getString(rs,"observation_identifier").substring(oscar.Misc.getString(rs,"observation_identifier").indexOf(" "))%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><b><%=((oscar.Misc.getString(rs,"abnormal_flags").toUpperCase().equals("N"))? "&nbsp;" : oscar.Misc.check(oscar.Misc.getString(rs,"abnormal_flags"), "", "&nbsp;"))%></b></td>
		<td class="Text" class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((oscar.Misc.getString(rs,"abnormal_flags").toUpperCase().equals("N"))? oscar.Misc.getString(rs,"observation_results") : "<b>" + oscar.Misc.getString(rs,"observation_results") + "</b>").replaceAll("\\\\\\.br\\\\", " ")%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=oscar.Misc.getString(rs,"reference_range")%></td>
		<td class="Text" nowrap class="<%=(other? "LightBG" : "WhiteBG")%>"><%=oscar.Misc.getString(rs,"units")%></td>
		<td class="Text" nowrap
			title="<%=oscar.Misc.getString(rs,"obxnote").replaceAll("\\\\\\.br\\\\", " ")%>"
			class="<%=(other? "LightBG" : "WhiteBG")%>"><%=((oscar.Misc.getString(rs,"obxnote").length() < 20)? oscar.Misc.getString(rs,"obxnote") : oscar.Misc.getString(rs,"obxnote").substring(0, 20)).replaceAll("\\\\\\.br\\\\", " ")%></td>
	</tr>
	<%
		}
		other = !other;
	}
	rs.close();
	rs = DBHandler.GetSQL(select_doc_notes.replaceAll("@pid", pid));
	if(rs.next()){
%>
	<tr>
		<td colspan="7"><br>
		<b>Notes:</b></td>
	</tr>
	<tr>
		<td colspan="7"><textarea name="notes" rows="7"
			style="width: 100%;"><%=oscar.Misc.check(oscar.Misc.getString(rs,"notes"), "")%></textarea></td>
	</tr>
	<tr class="LightBG">
		<td colspan="7" align="right"><input type="submit"
			name="cmd_save" value="Save" /></td>
	</tr>
</table>
<%
	}
	rs.close();
%>
</form>
</body>
</html>
