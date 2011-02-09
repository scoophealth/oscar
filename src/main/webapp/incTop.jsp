<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<body>
<%    oscar.OscarProperties props = oscar.OscarProperties.getInstance(); %>
<table border=0 width='100%' cellpadding="3">
	<tr bgcolor='gold'>
		<td>
		<center><font size="+1"><b><%=props.getProperty("logintitle", "")%>
		<% if (props.getProperty("logintitle", "").equals("")) { %> <bean:message
			key="loginApplication.alert" /> <% } %> </b></font></center>
		</td>
	</tr>
</table>