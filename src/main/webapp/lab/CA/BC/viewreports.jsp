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
    String demo_no = request.getParameter("demo_no"),
    pid = request.getParameter("pid");
	if(null != request.getParameter("unlink")){
		String update_link = "UPDATE hl7_link SET hl7_link.status = 'P' WHERE hl7_link.pid_id='@pid';";
		DBHandler.RunSQL(update_link.replaceAll("@pid", pid));
	}
	if(null == demo_no){
		out.println("<script language=\"JavaScript\">javascript:window.close();</SCRIPT>");
		return;
	}
	String select_lab_reports = "SELECT DISTINCT hl7_link.pid_id, hl7_obr.requested_date_time, hl7_obr.diagnostic_service_sect_id FROM hl7_link, hl7_obr WHERE hl7_link.demographic_no='@demo_no' AND hl7_link.pid_id=hl7_obr.pid_id AND (hl7_link.status='N' OR hl7_link.status='A' OR hl7_link.status='S') ORDER BY hl7_obr.requested_date_time DESC";
%>

<%@page import="oscar.oscarDB.DBHandler"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR PathNET - View Lab Reports</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var lab;
window.name= 'LabDemoIndex';
function PopupLab(pid)
{
	lab = window.open('report.jsp?viewed=true&pid=' + pid,'Lab','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	lab.focus();
	return false;
}
</script>
</head>
<body>
<table width="100%" class="DarkBG">
	<tr>
		<td height="40" width="25"></td>
		<td width="90%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - View Lab Reports</font></font></b></font>
		</td>
	</tr>
</table>
<form action="viewreports.jsp" method="post">
<table width="100%">
	<%
	java.sql.ResultSet rs = DBHandler.GetSQL(select_lab_reports.replaceAll("@demo_no", demo_no));
	if(rs.isBeforeFirst()){
		String dpid = "",
		diagnostic = "";
		java.text.SimpleDateFormat format = null;
		java.util.Date date = null;
		boolean other = false;
		while(rs.next()){
			format = new java.text.SimpleDateFormat("yyyy-MM-d HH:mm:ss");
			date = (format.parse(oscar.Misc.getString(rs,"requested_date_time")));
			format.applyPattern("MMM d, yyyy");
			if(dpid.equals(oscar.Misc.getString(rs,"pid_id"))){
				diagnostic += ", " + oscar.Misc.getString(rs,"diagnostic_service_sect_id");
			}else{
				if(!dpid.equals("")){
					out.println("<tr bgcolor='" + (other? "F6F6F6" : "WHITE") + "'><td class=\"Text\"><a href=\"#\" onclick=\"return PopupLab('" + dpid + "');\">" + format.format(date) + " (" + diagnostic + ")</a></td><td class=\"Text\"><a onclick=\"return confirm('Are you sure you want to unlink this lab report?');\" href=\"viewreports.jsp?unlink=true&demo_no=" + demo_no + "&pid=" + dpid + "\">unlink</a></td></tr>");
				}
				dpid = oscar.Misc.getString(rs,"pid_id");
				diagnostic = oscar.Misc.getString(rs,"diagnostic_service_sect_id");
				other = !other;
			}
		}
		out.println("<tr bgcolor='" + (other? "F6F6F6" : "WHITE") + "'><td class=\"Text\"><a href=\"#\" onclick=\"return PopupLab('" + dpid + "');\">" + format.format(date) + " (" + diagnostic + ")</a></td><td class=\"Text\"><a onclick=\"return confirm('Are you sure you want to unlink this lab report?');\" href=\"viewreports.jsp?unlink=true&demo_no=" + demo_no + "&pid=" + dpid + "\">unlink</a></td></tr>");
		rs.close();
	}
%>
</table>
</form>
</body>
</html>
