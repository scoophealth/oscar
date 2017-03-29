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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat" errorPage="errorpage.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.DiagnosticCode" %>
<%@ page import="org.oscarehr.common.dao.DiagnosticCodeDao" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>

<%
	DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }
    //-->
</script>

</head>
<body onload="start()">

<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%
  boolean error = false;
  String code = request.getParameter("update");
  code = code.substring(code.length()-3);

  try {
  	 List<DiagnosticCode> dcodes = diagnosticCodeDao.findByDiagnosticCode(code);
  	 for(DiagnosticCode dcode:dcodes) {
  		 dcode.setDescription(request.getParameter(code));
  		diagnosticCodeDao.merge(dcode);
  	 }
  }
  catch(Exception ex) {
	  error = true;
	  MiscUtils.getLogger().error("Error", ex);
  }
%> 

<%
if(!error) {
%>
	<p><h1>Successful Addition of a billing Record.</h1></p>
<%
}
else {
%>
	<p><h1>Sorry, addition has failed.</h1></p>
<%
}
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
