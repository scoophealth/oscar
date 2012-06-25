<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin.reporting" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="oscar.oscarReport.data.DoctorList"%>
<%@ page import="oscar.oscarProvider.bean.ProviderNameBean"%>
<%@ page import="java.util.ArrayList"%>
<% ArrayList dnl = new DoctorList().getDoctorNameList();%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PatientList</title>
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<link rel="stylesheet" href="../web.css">
</head>
<body>
    <form action="../patientlistbyappt" method="get" target="_blank">
<table width="525">
	<tr>
		<td>Select a Doctor</td>
		<td><select name="provider_no">
			<option value="all">All Doctors</option>
			<%      
 	for (int i = 0; i < dnl.size(); i++){
            ProviderNameBean pb = (ProviderNameBean)dnl.get(i);
%>
			<option value="<%=pb.getProviderID()%>"><%=pb.getProviderName()%></option>
			<%
 	 }

%>
		</select></td>
	<tr>
		<td width="20%">Appointment Date From:</td>
		<td width="20%"><input type="text" name="date_from"
			id="date_from" value="" size="10" readonly /> <img
			src="../images/cal.gif" id="date_from" /></td>
		<td width="20%">To:</td>
		<td width="20%"><input type="text" name="date_to" id="date_to"
			value="" size="10" readonly /> <img src="../images/cal.gif"
			id="date_to" /></td>
	</tr>
	<tr>
		<td><input type="submit" name="Submit" value="Submit" /></td>
	</tr>
</table>

</form>

</body>
</html>

<script type="text/javascript">
Calendar.setup({ inputField : "date_from", ifFormat : "%Y-%m-%d", showsTime :false, button : "date_from" });
</script>

<script type="text/javascript">
Calendar.setup({ inputField : "date_to", ifFormat : "%Y-%m-%d", showsTime :false, button : "date_to" });
</script>
