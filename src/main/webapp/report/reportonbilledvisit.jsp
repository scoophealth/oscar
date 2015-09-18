<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page errorPage="../errorpage.jsp"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>REPORT PHCP</title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css">
<script language="JavaScript">
        <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
//-->
        
      </script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> PHCP </font></th>
		<td><input type="button" name="Button" value="Cancel"
			onClick="window.close()"></td>
		</th>
	</tr>
</table>
<table width="20%" border="0" bgcolor="ivory" cellspacing="0"
	cellpadding="1">
	<tr bgcolor="silver">
		<th bgcolor="silver" width="10%" nowrap><b>Setting</b></th>
	</tr>
	<tr>
		<td><a href="reportonbilledvisitprovider.jsp"> Provider list
		</a></td>
	</tr>
	<tr>
		<td><a href="reportonbilleddxgrp.jsp"> Dx group list </a></td>
	</tr>
</table>
</body>
</html>
