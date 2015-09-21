<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.oscarehr.common.dao.DocumentDao" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	  boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_lab" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_lab");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html>
<head>
<title>PDF Sorter</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery.rotate.1-1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-ui-1.8.4.custom_full.min.js"></script>
<link rel="stylesheet" href="<%= request.getContextPath() %>/share/javascript/jquery/jquery-ui-1.8.4.custom.css" type="text/css" />
<link rel="stylesheet" href="<%= request.getContextPath() %>/share/css/sorter.css" type="text/css" />
</head>
<body>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/sorter.js"></script>
<div id="mastercontainer">
<div id="buildercontainer">
	<ul id="builder">
	</ul>
</div>

<div id="pickertoolscontainer">
	<ul id="pickertools">
		<li id="tool_add"><img src="../images/icons/103.png"><span>Add</span></li>
		<li id="tool_remove"><img src="../images/icons/101.png"><span>Remove</span></li>
		<li id="tool_rotate"><img src="../images/icons/114.png"><span>Rotate</span></li>
		<li id="tool_savecontinue"><img src="../images/icons/172.png"><span>Save &amp; Continue</span></li>
		<li id="tool_done"><img src="../images/icons/071.png"><span>Done</span></li>
	</ul>
</div>

<div id="pickercontainer">
<ul id="picker">
<%
String documentId = request.getParameter("document");
String queueID = request.getParameter("queueID");
String demoName = request.getParameter("demoName");
DocumentDao docdao = SpringUtils.getBean(DocumentDao.class);
org.oscarehr.common.model.Document thisDocument = docdao.getDocument(documentId);

for (int i = 1; i <= thisDocument.getNumberofpages(); i++) {
%>
	<li><img class="page" src="<%= request.getContextPath() + "/dms/ManageDocument.do?method=viewDocPage&doc_no=" + documentId + "&curPage=" + i %>" /></li>
<% }

%>
</ul>
</div>
</div>

<input type="hidden" id="document_no" value="<%=documentId %>" />
<input type="hidden" id="queueID" value="<%=queueID %>" />
<input type="hidden" id="demoName" value="<%=StringEscapeUtils.escapeJavaScript(demoName)%>"/>
</body>
</html>