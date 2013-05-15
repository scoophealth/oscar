<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ page import="com.sun.pdfview.PDFFile" %>
<%@ page import="java.io.FileNotFoundException,java.io.IOException" %>
<%@ page import="org.apache.pdfbox.pdmodel.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.common.dao.DocumentDao, java.io.File, java.io.RandomAccessFile, java.nio.channels.FileChannel, java.nio.ByteBuffer, com.sun.pdfview.PDFFile" %>

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
DocumentDao docdao = (DocumentDao) WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext()).getBean("documentDao");
org.oscarehr.common.model.Document thisDocument = docdao.getDocument(documentId);

String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
File documentDir = new File(docdownload);

File docDir = new File(docdownload);
String documentDirName = documentDir.getName();
File parentDir = documentDir.getParentFile();
String filePath=docdownload+thisDocument.getDocfilename();

File cacheDir = new File(parentDir,documentDirName+"_cache");

if(!cacheDir.exists()){
    cacheDir.mkdir();
}

File file = new File(documentDir, thisDocument.getDocfilename());

RandomAccessFile raf = new RandomAccessFile(file, "r");
FileChannel channel = raf.getChannel();
ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
PDFFile pdffile = new PDFFile(buf);

for (int i = 1; i <= pdffile.getNumPages(); i++) {
%>
	<li><img class="page" src="<%= request.getContextPath() + "/dms/ManageDocument.do?method=viewDocPage&doc_no=" + documentId + "&curPage=" + i %>" /></li>
<% }

channel.close();
raf.close();
%>
</ul>
</div>
</div>

<input type="hidden" id="document_no" value="<%=documentId %>" />
</body>
</html>