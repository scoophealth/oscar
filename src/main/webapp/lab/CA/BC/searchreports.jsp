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
    String start       = oscar.Misc.check(request.getParameter("start"), ""),
           end         = oscar.Misc.check(request.getParameter("end"), ""),
	        provider_no = oscar.Misc.check(request.getParameter("provider_no"), ""),
           orderby     = oscar.Misc.check(request.getParameter("orderby"), "pid_id"),
	        command     = oscar.Misc.check(request.getParameter("cmd_search"), ""),
		select_providers_with_reports = "SELECT DISTINCT provider.provider_no, provider.last_name, provider.first_name FROM hl7_link, demographic, provider WHERE hl7_link.demographic_no=demographic.demographic_no AND demographic.provider_no=provider.provider_no AND demographic.provider_no is not null;",
		select_reports_by_provider = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, provider.last_name, provider.first_name, hl7_message.date_time  FROM hl7_link, demographic, hl7_pid, hl7_obr, hl7_message left join provider on demographic.provider_no=provider.provider_no WHERE hl7_link.pid_id=hl7_obr.pid_id AND hl7_link.pid_id=hl7_pid.pid_id AND demographic.provider_no='@provider_no' AND hl7_message.message_id=hl7_pid.message_id AND demographic.demographic_no=hl7_link.demographic_no AND hl7_link.status!='P'",
		select_reports_linked_to_providers ="SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, provider.last_name, provider.first_name, hl7_message.date_time  FROM hl7_link, demographic, hl7_pid, hl7_obr, hl7_message, provider WHERE demographic.provider_no=provider.provider_no AND hl7_link.pid_id=hl7_obr.pid_id AND hl7_link.pid_id=hl7_pid.pid_id AND hl7_message.message_id=hl7_pid.message_id AND demographic.demographic_no=hl7_link.demographic_no AND hl7_link.status!='P'",
		select_unlinked_labs = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_obr.ordering_provider, hl7_obr.result_copies_to, hl7_link.status, hl7_link.signed_on, hl7_message.date_time, '' as `last_name`, '' as `first_name` FROM hl7_pid left join hl7_link on hl7_link.pid_id=hl7_pid.pid_id left join hl7_obr on hl7_pid.pid_id=hl7_obr.pid_id left join hl7_message on hl7_message.message_id=hl7_pid.message_id WHERE hl7_link.status='P' OR hl7_link.status is null",
		sqlWhere = (!start.equals("")? " AND hl7_message.date_time >= '" + start + " 00:00:00'" : "") + (!end.equals("")? " AND hl7_message.date_time <= '" + end + " 23:59:59'": ""),
		sqlOrderBy = " ORDER BY @orderby";
	String sql = null;
	if(command!=""){
		if(provider_no.equals("-ULL")){
			sql = select_unlinked_labs;
		}else if(provider_no.equals("-APL")){
			sql = select_reports_linked_to_providers;
		}else if(provider_no.equals("-UAP")){
			sql = select_reports_by_provider;
		}else{
			sql = select_reports_by_provider;
		}
		sql += sqlWhere + sqlOrderBy;
		sql = sql.replaceAll("@provider_no", provider_no.replaceAll("-UAP", "")).replaceAll("@orderby", orderby);
	}
	String url = "searchreports.jsp?cmd_search=search&provider_no=" + provider_no;
%>

<%@page import="oscar.oscarDB.DBHandler"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR PathNET - Search Lab Reports</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var labReport;
function PopupLab(pid)
{
	labReport = window.opener.open('report.jsp?pid=' + pid,'LabSearchReport','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	labReport.focus();
}
</script>
</head>
<body>
<form action="searchreports.jsp" method="post">
<table width="100%" class="DarkBG">
	<tr>
		<td height="40" width="25"></td>
		<td width="50%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - Search Lab
		Reports</font></font></b></font></td>
		<td class="Text" align="right"><a href="index.jsp">Patient
		Linking</a>&nbsp;</td>
	</tr>
</table>
<table width="100%">
	<tr bgcolor="E6E6E6">
		<td class="Text">Provider No</td>
		<td class="Text">Start Date</td>
		<td class="Text">End Date</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td class="Text"><select name="provider_no" id="provider_no"
			size=1>
			<option value="-APL"
				<%=(provider_no.equals("-APL")? "selected" : "")%>>Linked
			Labs</option>
			<option value="-ULL"
				<%=(provider_no.equals("-ULL")? "selected" : "")%>>Unlinked
			Labs</option>
			<option value="-UAP"
				<%=(provider_no.equals("-UAP")? "selected" : "")%>>Unassigned
			Patients</option>

			<%
	java.sql.ResultSet rs = DBHandler.GetSQL(select_providers_with_reports);
	while(rs.next()){
		out.println("<option value='" + oscar.Misc.getString(rs,"provider_no") + "'" + (oscar.Misc.getString(rs,"provider_no").equals(provider_no)? "selected" : "") + ">" + oscar.Misc.getString(rs,"last_name") + ", " + oscar.Misc.getString(rs,"first_name") + "</option>");
	}
	rs.close();
%>
		</select></td>
		<td class="Text"><input name="start" value="<%=start%>" /></td>
		<td class="Text"><input name="end" value="<%=end%>" /></td>
		<td class="Text"><input type="submit" name="cmd_search"
			value="Search" /></td>
	</tr>
</table>
</form>
<table width="100%">
	<tr>
		<td class="Header" nowrap><a href="<%=url%>">Lab Id</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=patient_name"%>">Patient Name</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=ordering_provider"%>">Ordering Provider</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=result_copies_to"%>">Result Copies To</a></td>
		<td class="Header" nowrap><a href="<%=url + "&orderby=status"%>">Status</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=signed_on"%>">Signed</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=last_name"%>">Signing Provider</a></td>
		<td class="Header" nowrap><a
			href="<%=url + "&orderby=date_time"%>">Date Received</a></td>
	</tr>
	<%
	if(sql != null){
		rs = DBHandler.GetSQL(sql);
		boolean other = true;
		while(rs.next()){
%>
	<tr class="<%=(other? "LightBG" : "WhiteBG")%>">
		<td class="Text"><a href="searchreports.jsp"
			onclick="PopupLab('<%=oscar.Misc.getString(rs,"pid_id")%>'); return false;"><%=oscar.Misc.getString(rs,"pid_id")%></a></td>
		<td class="Text" nowrap><%=oscar.Misc.check(oscar.Misc.getString(rs,"patient_name"), "")%></td>
		<td class="Text" nowrap><%=oscar.Misc.check(oscar.Misc.getString(rs,"ordering_provider"), "").replaceAll("~", ",<br/>")%></td>
		<td class="Text"><%=oscar.Misc.check(oscar.Misc.getString(rs,"result_copies_to"), "").replaceAll("~", ",<br/>")%></td>
		<td class="Text" nowrap>
		<%
			if(oscar.Misc.getString(rs,"status") != null){
				switch(oscar.Misc.getString(rs,"status").toCharArray()[0]){
					case 'S':
						out.print("Signed");
						break;
					case 'P':
						out.print("Pending");
						break;
					case 'A':
						out.print("Assigned");
						break;
					case 'N':
						out.print("New");
						break;
				}
			}
%>
		</td>
		<td class="Text" nowrap>
		<%String signed = oscar.Misc.check(oscar.Misc.getString(rs,"signed_on"), "");
									out.print( (signed.indexOf(" ")>-1)? signed.substring(0, signed.indexOf(" ")) : signed);%>
		</td>
		<td class="Text" nowrap><%=((oscar.Misc.getString(rs,"last_name") != null && !oscar.Misc.getString(rs,"last_name").equals(""))? oscar.Misc.check(oscar.Misc.getString(rs,"last_name"), "") + ", " + oscar.Misc.check(oscar.Misc.getString(rs,"first_name"), "") : "&nbsp;")%></td>
		<td class="Text" nowrap><%=oscar.Misc.getString(rs,"date_time").substring(0, oscar.Misc.getString(rs,"date_time").indexOf(" "))%></td>
	</tr>
	<%
		other = !other;
		}
		rs.close();
	}
%>
</table>
</body>
</html>
