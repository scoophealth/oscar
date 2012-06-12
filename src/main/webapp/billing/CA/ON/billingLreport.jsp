<%--

    Copyright (c) 2008-2012 Indivica Inc.
    
    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".
    
--%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*,oscar.*,java.io.*,java.net.*,oscar.util.*,org.apache.commons.io.FileUtils" errorPage="errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title>MOH Report</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/billing.css" >
<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/css/extractedFromPages.css" />

<script>
<!--

function getRelative(path) {
   // we are assuming that the file path is in the OscarDocument location
   cpath="<%=request.getContextPath()%>";
   i=path.indexOf("OscarDocument");
   subpath=path.substr(i); 
   var dpath=cpath+"/../"+subpath;
   return dpath;
}

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
	<%
	String filename = (String)request.getAttribute("filename");
	if (filename == null) { filename = request.getParameter("filename"); }
	%>
   var fname="<%=filename%>";
   var backuppath="<%=session.getAttribute("backupfilepath")%>";
   var fullpath=getRelative(backuppath)+fname;
   var sname= cpath + "/billing/CA/ON/ES.xsl";
   if (fname.substring(2,4) == "OU") {
	   sname=cpath + "/billing/CA/ON/OU.xsl";
   }
   try {
      xml=loadXMLDoc(fullpath);
      xsl=loadXMLDoc(sname);
   } catch(err) {
      txt="Cannot load XML & XSL documents.\n";
      txt+="xml doc="+fullpath+"\n";
      txt+="xsl doc="+sname+"\n";
      txt+="Error description: " + err.description;
      alert(txt);
      return;
  }

   // code for Mozilla, Firefox, Opera
   if (document.implementation && document.implementation.createDocument) {
      xsltProcessor=new XSLTProcessor();
      xsltProcessor.importStylesheet(xsl);
      resultDocument = xsltProcessor.transformToFragment(xml,document);
      document.getElementById("MOHreport").appendChild(resultDocument);
   } else if (window.ActiveXObject) {
      // code for IE
      ex=xml.transformNode(xsl);
      document.getElementById("MOHreport").innerHTML=ex;
   } else {
      alert("Viewing report is not supported by this Browser.");
   }

}
// -->
</script>
</head>

<body onload="displayReport()" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="40" width="10%" class="Header"><input type="button"
			name="print" value="<bean:message key="global.btnPrint"/>"
			onClick="window.print()"></td>
		<td width="80%" align="left" class="Header">oscar<font size="3">Billing</font>
		</td>
		<td width="10%" align="right" class="Header"><input type="button" name="close"
                        value="<bean:message key="global.btnClose"/>"
                        onClick="window.close()"></td>

	</tr>
</table>
<div id="MOHreport" ></div>

</body>
</html:html>

