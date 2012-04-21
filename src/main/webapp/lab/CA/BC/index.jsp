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
    String
    	update_linking = "UPDATE hl7_link SET hl7_link.status='N' WHERE hl7_link.pid_id='@pid'",
    	insert_auto_matching = "INSERT INTO hl7_link ( pid_id, demographic_no ) SELECT hl7_pid.pid_id, demographic.demographic_no FROM demographic, hl7_pid LEFT JOIN hl7_link ON hl7_pid.pid_id=hl7_link.pid_id WHERE demographic.hin=hl7_pid.external_id AND hl7_link.pid_id IS NULL",
    	delete_linking = "DELETE FROM hl7_link WHERE hl7_link.pid_id='@pid'",
    	insert_linking = "INSERT INTO hl7_link ( pid_id, demographic_no ) VALUES ('@pid', '@demo'); ",
    	select_lab_matching = "SELECT DISTINCT hl7_pid.pid_id, hl7_pid.patient_name, hl7_pid.date_of_birth as birth, hl7_pid.sex, demographic.demographic_no, demographic.last_name, demographic.first_name, demographic.year_of_birth as year, demographic.month_of_birth as month, demographic.date_of_birth as day, hl7_obr.ordering_provider, hl7_obr.result_copies_to FROM hl7_pid left join hl7_link on hl7_link.pid_id=hl7_pid.pid_id left join demographic on hl7_link.demographic_no=demographic.demographic_no left join hl7_obr on hl7_pid.pid_id=hl7_obr.pid_id WHERE hl7_link.status='P' OR hl7_link.status is null;";
    if(request.getParameterValues("chk")!= null){
    	String[] values = request.getParameterValues("chk");
    	for(int i = 0; i < values.length; ++i){
    		DBHandler.RunSQL(update_linking.replaceAll("@pid", values[i]));
    	}
    }
	DBHandler.RunSQL(insert_auto_matching);
    if(request.getParameter("demo_id") != null && request.getParameter("pid") != null){
    	DBHandler.RunSQL(delete_linking.replaceAll("@pid", request.getParameter("pid")));
    	DBHandler.RunSQL(insert_linking.replaceAll("@pid", request.getParameter("pid")).replaceAll("@demo", request.getParameter("demo_id")));
    }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="oscar.oscarDB.DBHandler"%><html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>OSCAR oscarPathNET - Patient Linking</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<script language="JavaScript">
var demo;
var lab;
window.name= 'LabIndex';
function PopupDemo(pid){
	demo = window.open('demo_select.jsp?postTo=index.jsp?pid=' + pid + '-demo_id=','Patients','height=500,width=750,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	demo.focus();
	return false;
}
function PopupLab(pid){
	lab = window.open('report.jsp?pid=' + pid,'Lab','height=500,width=900,scrollbars=1,toolbar=0,status=1,menubar=0,location=0,directories=0,resizable=1');
	lab.focus();
	return false;
}
</script>
</head>
<body>
<form action="index.jsp" method="post">
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#D3D3D3">
	<tr>
		<td height="40" width="25"></td>
		<td width="50%" align="left"><font color="#4D4D4D"><b><font
			size="4">oscar<font size="3">PathNET - Patient Linking</font></font></b></font>
		</td>
		<td class="Text" align="right"><a href="searchreports.jsp">Search
		Lab Reports</a>&nbsp;</td>
	</tr>
</table>
<table width="100%" cellspacing="2" cellpadding="2">
	<tr bgcolor="E6E6E6">
		<td class="Text" align="center" colspan="6"
			style="border-right: #464646 1px solid;">Lab Information</td>
		<td class="Text" align="center" colspan="3"
			style="border-left: #464646 1px solid;">Demographic Information</td>
	</tr>
	<tr>
		<td class="Header">Valid</td>
		<td class="Header">Patient</td>
		<td class="Header">BirthDate</td>
		<td class="Header">Gender</td>
		<td class="Header">Ordered By</td>
		<td class="Header" style="border-right: #464646 1px solid;">Copies
		To</td>
		<td class="Header" style="border-left: #464646 1px solid;">Demo.
		No.</td>
		<td class="Header">Name</td>
		<td class="Header">BirthDate</td>
	</tr>
	<%
		java.sql.ResultSet result = DBHandler.GetSQL(select_lab_matching);
		boolean other = true;
		while(result.next()){
%>
	<tr class="<%=(other? "WhiteBG" : "LightBG")%>">
		<td class="Text"><input type="checkbox" name="chk"
			value="<%=result.getString("pid_id")%>" /></td>
		<td class="Text"><a href="#"
			onclick="return PopupLab('<%=result.getString("pid_id")%>');"><%=oscar.Misc.check(result.getString("patient_name"), "")%></a></td>
		<td class="Text"><%=oscar.Misc.check(result.getString("birth").split(" ")[0], "")%></td>
		<td class="Text" align="center"><%=oscar.Misc.check(result.getString("sex"), "")%></td>
		<td class="Text"><%=oscar.Misc.check(result.getString("ordering_provider"), "").replaceAll("~", ",<br/>")%></td>
		<td class="Text" style="border-right: #464646 1px solid;"><%=oscar.Misc.check(result.getString("result_copies_to"), "").replaceAll("~", ",<br/>")%></td>
		<td class="Text" style="border-left: #464646 1px solid;"><a
			href="#"
			onclick="return PopupDemo('<%=result.getString("pid_id")%>');"><%=oscar.Misc.check(result.getString("demographic_no"), "select")%>
		</a></td>
		<td class="Text"><%=((result.getString("last_name")==null && result.getString("first_name")==null)? "" : result.getString("last_name") + ", " + result.getString("first_name"))%></td>
		<td class="Text"><%=((result.getString("year")==null && result.getString("month")==null && result.getString("day")==null)? "" : result.getString("year") + "-" + result.getString("month") + "-" + result.getString("day"))%></td>
	</tr>
	<%
			other=!other;
		}
%>
	<tr>
		<td colspan="9" align="left"><input type="submit" name="submit"
			value="Link" /></td>
	</tr>
</table>
</form>
</body>
</html:html>
