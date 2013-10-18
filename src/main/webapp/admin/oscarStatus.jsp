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
if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
String curUser_no;
curUser_no = (String) session.getAttribute("user");
String tite = (String) request.getAttribute("provider.title");
%>

<%
	if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
	String roleName$ = (String)session.getAttribute("userrole") + "," + (String)session.getAttribute("user");
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery-1.4.2.js"></script>
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

<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">

</head>


<body 
<%if (request.getAttribute("documentStatusText") == null) { %>
onLoad="setTimeout('doDocuments()',3000);"
<%} %>
>
<table class="MainTable" id="scrollNumber1" name="encounterTable">

	<form name="loadDoc" method="post" action="<%=request.getContextPath() %>/admin/oscarStatus.do">
		<input type="hidden" name="delayed" value="do" />
	</form>
	
	<% if (request.getAttribute("restartOscar") != null) { %>
		<div style="margin-bottom: 20px;">
		<em>Oscar is restarting.</em>
		<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
			<pre><%=request.getAttribute("restartOscar") %></pre>
		</div>
		</div>
	<% } %>
	
	<% if (request.getAttribute("rebootServer") != null) { %>
		<div style="margin-bottom: 20px;">
		<em>Server is rebooting.</em>
		<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
			<pre><%=request.getAttribute("rebootServer") %></pre>
		</div>
		</div>
	<% } %>

	<tr class="MainTableTopRow">	
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message key="admin.oscarStatus.oscarStatus" /></td>
	</tr>
	<tr>
		<td>
			<div>
			<strong>Oscar Server Status</strong>
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
					Master Status:
					<pre><%=request.getAttribute("sqlMasterStatusText") %></pre>
				</div>
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
					Slave Status:
					<pre><%=request.getAttribute("sqlSlaveStatusText") %></pre>
				</div>
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
					Filesystem:
					<pre><%=request.getAttribute("filesystemStatusText") %></pre>
				</div>
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
					Uptime:
					<pre><%=request.getAttribute("uptimeText") %></pre>
				</div>
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
					Virtual Memory:
					<pre><%=request.getAttribute("vmstatText") %></pre>
				</div>
				
				<%if (request.getAttribute("documentStatusText") != null) { %>
					<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
						Oscar Document Storage:
						<pre><%=request.getAttribute("documentStatusText") %></pre>
					</div>
				<%} else { %>
					<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
						Oscar Document Storage: <br>
						<img src="<%= request.getContextPath() %>/images/loader.gif" />
					</div>
				<%} %>
				
				<%if (request.getAttribute("hl7StatusText") != null) { %>
					<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px; max-height:200px; overflow:auto;">
						HL7 Status:
						<pre><%=request.getAttribute("hl7StatusText") %></pre>
					</div>
				<%} else { %>
					<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px;">
						HL7 Status: <br>
						<img src="<%= request.getContextPath() %>/images/loader.gif" />
					</div>
				<%} %>
				<!-- Used to display the git sha1 id for this build from the code repository -->
				<div style="colour: black; border: 1px solid black; background-color: lightgrey; padding: 5px; margin: 10px; max-height:200px; overflow:auto;">
					Build ID:
					<pre>Git SHA-1: <%=BuildNumberPropertiesFileReader.getGitSha1()%></pre>
				</div>
				
				
			</div>
		</td>
	</tr>
	<tr class="MainTableTopRow">	
		<td style="color: white" class="MainTableTopRowRightColumn"><bean:message key="admin.oscarStatus.restart" /></td>
	</tr>
	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_admin,_admin.misc" rights="r" reverse="<%=false%>">
	<tr>
		<td>
			<div style="margin-top: 10px;">
				<form method="post" action="admincontrol.jsp" name="confirmOscarReboot">
					<input type="hidden" name="displaymode" value="reboot_confirmation">
					If you are having issues with Oscar, click the button below to restart Oscar.
					<br>
					<input type="submit" name="subbutton" value="REBOOT OSCAR" > 
					<br>
					If the server is experiencing problems, click the button below to reboot the server.
					<br>
					<input type="submit" name="subbutton" value="REBOOT SERVER" >
				</form>
			</div>
		</td>
	</tr>
	</security:oscarSec>
	
</table>
</body>

</html:html>
