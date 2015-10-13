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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.nio.charset.Charset"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*,oscar.*,java.io.*,java.net.*,oscar.util.*,org.apache.commons.io.FileUtils" errorPage="errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
<title>MOH Report</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/billing.css" >
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/extractedFromPages.css" />

<%
String INBOX = OscarProperties.getInstance().getProperty("ONEDT_INBOX");
String filename = (String)request.getAttribute("filename");
if (filename == null) { 
    filename = request.getParameter("filename"); 
}

String fileContents = null;
if( !filename.matches(".*\\.\\..*") ) {

	File file = new File(INBOX + "/" + filename);
	fileContents = FileUtils.readFileToString(file, "UTF-8");
}
else {
    fileContents = "";
}
%>

<script>
<!--

function loadXMLDoc(xmldoc) 
{
   if (window.XMLHttpRequest) {
     // Support for IE7, Firefox and Safari only 
     xhttp=new XMLHttpRequest();
   } else if (window.ActiveXObject) {
     // for IE5, IE6 
     xhttp=new ActiveXObject("Microsoft.XMLHTTP");
   }
   xhttp.open("GET",xmldoc,false);
   xhttp.send("");
   
   return xhttp.responseXML;
}

function displayReport() 
{ 	
   var cpath="<%=request.getContextPath()%>";
   var fname = "<%=filename%>";
   sname= cpath + "/billing/CA/ON/ES.xsl";
   if (fname.substring(2,4) == "OU") {
	   sname=cpath + "/billing/CA/ON/OU.xsl";
   }
   
   xml='<%=StringEscapeUtils.escapeJavaScript(fileContents)%>';
   try {            
      xsl=loadXMLDoc(sname);
      
   } catch(err) {
      txt="Cannot load XSL document.\n";
      txt+="xsl doc="+sname+"\n";
      txt+="Error description: " + err.description;      
      alert(txt);
      return;
  }
   
   var xmlDoc = null;
   
   if (navigator.appName == 'Microsoft Internet Explorer') {
       xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
       xmlDoc.async = false;
       xmlDoc.loadXML(xml);
   } else if (window.DOMParser) {
       parser = new DOMParser();
       xmlDoc = parser.parseFromString(xml, "text/xml");
   } else {
       alert("Your browser doesn't suppoprt XML parsing!");
   }

   // code for Mozilla, Firefox, Opera
   if (document.implementation && document.implementation.createDocument) {
      xsltProcessor=new XSLTProcessor();      
      xsltProcessor.importStylesheet(xsl);
      resultDocument = xsltProcessor.transformToFragment(xmlDoc,document);
      jQuery("#MOHreport").html(resultDocument);
   } else if (window.ActiveXObject) {
      // code for IE
      ex=xmlDoc.transformNode(xsl);
      jQuery('#MOHreport').html(ex);
   } else {
      alert("Viewing report is not supported by this Browser.");
   }

}
// -->
</script>

<style>
@media print {
	.noprint {display:none !important;}
}
</style>
</head>

<body onload="displayReport()">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="noprint">
	<tr>
		<td height="40" width="10%" class="Header">
			<font size="3">Billing</font>
		</td>
		<td width="90%" align="right" class="Header">
			<input type="button" name="print" value="<bean:message key="global.btnPrint"/>"	onClick="window.print()">
		</td>
	</tr>
</table>
<div id="MOHreport" ></div>

</body>
</html:html>

