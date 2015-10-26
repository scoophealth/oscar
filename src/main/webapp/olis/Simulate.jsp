<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>

<script>
$(function() {
    $( document ).tooltip();
  });
</script>

</head>
<body>
	
	<%
		if(request.getAttribute("result")!=null) {
			%><span style="color:red"><%=request.getAttribute("result")%></span><%
		}
		if(request.getAttribute("errors")!=null) {
			%><span style="color:red"><%=request.getAttribute("errors")%></span><%
		}
	%>
	<form action="<%=request.getContextPath() %>/olis/UploadSimulationData.do" method="POST" enctype="multipart/form-data" name="simulate_form">
<table>	
		<tr>
			<td colspan="2"><b>Simulation File:</b><input type="file" name="simulateFile"/>
			<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
			</td>
		</tr>
		<tr>
			<td colspan="2"><b>Simulate Error:</b><input type="checkbox" name="simulateError"/></td>
		</tr>
		<tr>
			<td> <input type="submit" value="Upload Simulation Data"/></td>
		</tr>
		</table>
	</form>	
	
</body>
</html>
