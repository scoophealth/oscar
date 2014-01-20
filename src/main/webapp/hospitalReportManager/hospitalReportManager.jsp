<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- This JSP is the first page you see when you enter 'report by template' --%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.hospitalReportManager.SFTPConnector, org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
	<title>Hospital Report Manager</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>   
	
	<script type="text/javascript" language="JavaScript" src="../share/javascript/prototype.js"></script>
	<script type="text/javascript" language="JavaScript" src="../share/javascript/Oscar.js"></script>
	
	<script type="text/javascript">
	function runFetch() {
		window.location = "<%=request.getContextPath() %>/hospitalReportManager/hospitalReportManager.jsp?fetch=true";
	}
	</script>
</head>
<body>
<h4>Hospital Report Manager</h4>
<% if (request.getParameter("fetch") != null && request.getParameter("fetch").equalsIgnoreCase("true"))
		SFTPConnector.startAutoFetch();
%>
<p>
	HRM Status: <%=SFTPConnector.isFetchRunning() ? "Fetching data from HRM" : "Idle" %><br />
	<% if (!SFTPConnector.isFetchRunning()) { %>
		<input type="button" class="btn" onClick="runFetch()" value="Fetch New Data from HRM" />
	<% } else { %>
		Please wait until the current fetch task completes before requesting another data fetch.
	<% } %>
</p>
<form enctype="multipart/form-data" action="<%=request.getContextPath() %>/hospitalReportManager/UploadLab.do" method="post">
    Upload an HRM report from your computer: <input type="file" name="importFile" /> <input type="submit" class="btn" name="submit" value="Upload" />
</form>
<%
	HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");
	String statement = hrmProviderConfidentialityStatementDao.getConfidentialityStatementForProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
%>
<form action="<%=request.getContextPath() %>/hospitalReportManager/Statement.do" method="post">
	<div class="control-group">
		<label class="control-label">Provider Confidentiality Statement</label>
		<div class="controls">
			<textarea name="statement"><%=statement %></textarea>
		</div>
	</div>
	<div>
		<input type="submit" class="btn btn-primary" name="submit" value="Save Statement" />
		<% if (request.getAttribute("statementSuccess") != null && (Boolean) request.getAttribute("statementSuccess")) { %>
			Success
		<% } else if (request.getAttribute("statementSuccess") != null && !((Boolean) request.getAttribute("statementSuccess")))  { %>
			Error
		<% } %>
	</div>
</form>
<input type="button" class="btn" value="I don't want to receive any more HRM outtage messages for this outtage instance" onclick="window.location='disable_msg_action.jsp'" />
</body>
</html:html>
