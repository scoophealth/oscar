<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>
<%@page import="org.oscarehr.admin.traceability.BuildNumberPropertiesFileReader" %>


<%
String curUser_no;
curUser_no = (String) session.getAttribute("user");
String tite = (String) request.getAttribute("provider.title");
String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}

function doDocuments() {
	var form = document.loadDoc;
	form.submit();
}

</script>
<html:base />
<meta http-equiv="Content-Type" content="text/html;">
<title><bean:message key="admin.oscarStatus.oscarStatus" /></title>

<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">

</head>
<body 
<%if (request.getAttribute("documentStatusText") == null) { %>
onLoad="setTimeout('doDocuments()',3000);"
<%} %>
>
<form name="loadDoc" method="post" action="<%=request.getContextPath() %>/admin/oscarStatus.do">
	<input type="hidden" name="delayed" value="do" />
</form>
<% if (request.getAttribute("restartOscar") != null) { %>
	<h4>Oscar is restarting.</h4>
	<pre><%=request.getAttribute("restartOscar") %></pre>
<% } %>

<% if (request.getAttribute("rebootServer") != null) { %>
	<h4>Server is rebooting.</em>
	<pre><%=request.getAttribute("rebootServer") %></pre>
<% } %>
	
<div class="page-header">
	<h4><bean:message key="admin.oscarStatus.oscarStatus" /></h4>
</div>

<h5>Master Status:</h5>
	<pre><%=request.getAttribute("sqlMasterStatusText") %></pre>

<h5>Slave Status:</h5>
<pre><%=request.getAttribute("sqlSlaveStatusText") %></pre>

<h5>Filesystem:</h5>
<pre><%=request.getAttribute("filesystemStatusText") %></pre>

<h5>Uptime:</h5>
<pre><%=request.getAttribute("uptimeText") %></pre>

<h5>Virtual Memory:</h5>
<pre><%=request.getAttribute("vmstatText") %></pre>

<%if (request.getAttribute("documentStatusText") != null) { %>
	<h5>Oscar Document Storage:</h5>
	<pre><%=request.getAttribute("documentStatusText") %></pre>
<%} else { %>
	<h5>Oscar Document Storage:</h5>
	<div class="well">
		<img src="<%= request.getContextPath() %>/images/loader.gif" />
	</div>
<%} %>

<%if (request.getAttribute("hl7StatusText") != null) { %>
	<h5>HL7 Status:</h5>
	<pre><%=request.getAttribute("hl7StatusText") %></pre>
<%} else { %>
	<h5>HL7 Status:</h5>
	<div class="well">
		<img src="<%= request.getContextPath() %>/images/loader.gif" />
	</div>
<%} %>

<h5>Build ID:</h5>
<pre>Git SHA-1: <%=BuildNumberPropertiesFileReader.getGitSha1()%></pre>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>">
	<h4><bean:message key="admin.oscarStatus.restart" /></h4>
	<div class="well">
		<form method="post" action="rebootConfirmation.jsp" name="confirmOscarReboot">
			<p>If you are having issues with Oscar, click the button below to restart Oscar.</p>
			<input class="btn btn-danger" type="submit" name="subbutton" value="REBOOT OSCAR" > 
			<p>If the server is experiencing problems, click the button below to reboot the server.</p>
			<input class="btn btn-danger" type="submit" name="subbutton" value="REBOOT SERVER" >
		</form>
	</div>
</security:oscarSec>

<script>
$( document ).ready(function() {	
    parent.parent.resizeIframe($('html').height());	
	
});
</script>	
</body>

</html:html>
