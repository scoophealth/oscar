<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  String curUser_no = (String) session.getAttribute("user");
%>
<%@ page errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%
OscarAppointmentDao appointmentDao = 
	(OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
</script>
</head>
<body> 
	<center>
	<table border="0" cellspacing="0" cellpadding="0" width="90%">
		<tr bgcolor="#486ebd">
			<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
			DELETE A BILLING RECORD</font></th>
		</tr>
	</table>

	<% 


		oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
		String unbillStatus = as.unbillStatus(request.getParameter("status"));
		Appointment appt = appointmentDao.find(
			Integer.parseInt(request.getParameter("appointment_no")));
			
		if(appt != null) 
		{
			appt.setStatus(unbillStatus);
			appt.setLastUpdateUser((String)session.getAttribute("user"));
			appointmentDao.merge(appt);
		}
	%>
	<p>
	<h1>Successful Removed billed status</h1>

	<script LANGUAGE="JavaScript">
		self.opener.refresh();
	</script> <p></p>

	<hr width="90%">
	<form>
		<input type="button" value="Back to previous page" onClick="window.close()">
		<input type="button" value="Close this window" onClick="window.close()">
	</form>
	</center>
	</body>
</html>
