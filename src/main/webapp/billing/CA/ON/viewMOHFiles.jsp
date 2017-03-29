<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
    boolean bodd = false;
    EDTFolder folder = EDTFolder.getFolder(request.getParameter("folder"));
    String folderPath = folder.getPath();
%>

<%@ page import="java.util.*,oscar.*,java.io.*,java.net.*,oscar.util.*,org.apache.commons.io.FileUtils,java.text.SimpleDateFormat,org.oscarehr.billing.CA.ON.util.EDTFolder,org.oscarehr.util.MiscUtils" errorPage="errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<html>
<head>
<title><bean:message key="admin.admin.viewMOHFiles"/></title>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script LANGUAGE="JavaScript">
<!--
function viewMOHFile (filename) {
	var form = document.getElementById("form");
	document.getElementById("filename").value = filename;
	var fileType = filename.substring(0,1).toUpperCase();
	if (filename.substring(filename.length-4).toLowerCase() == ".zip") {
 		var r=alert("Please unzip "+filename+" before processing.");
		location.href="viewMOHFiles.jsp";
		return;
	} else if (fileType == "P" || fileType == "S") {
		form.action ="/<%= OscarProperties.getInstance().getProperty("project_home") %>/servlet/oscar.DocumentUploadServlet";
	} else if (fileType == "L") {
		form.action ="billingLreport.jsp";
	} else {
		form.action = "/<%= OscarProperties.getInstance().getProperty("project_home") %>/oscarBilling/DocumentErrorReportUpload.do";
	}
	form.submit();
}

function toggleCheckboxes(el) {
	jQuery("input[name='mohFile']").attr("checked", jQuery(el).attr("checked"));
}

function checkForm() {
	if (jQuery("input[name='mohFile']:checked").size() > 0) { return true; }
	alert("Please select a file first.");
	return false;
}
//-->
</script>
</head>

<body>
<h3><bean:message key="admin.admin.viewMOHFiles"/></h3>

<div class="container-fluid well">

<form id="form" method="POST">
	<input type="hidden" id="filename" name="filename" value="" >
</form>

		<%
		    if (folder == EDTFolder.INBOX) {
		%> <form method="POST" action="<%=request.getContextPath()%>/billing/CA/ON/moveMOHFiles.do" onsubmit="return checkForm();" class="form-inline">
<% } %>

<% if (folder  == EDTFolder.INBOX) {%>

		<input type="submit" value="Archive" class="btn">
<%}%>

		View: 
		<select name="folder" onchange="location.href='viewMOHFiles.jsp?folder='+this.options[selectedIndex].value">
			<option value="inbox" <% if (folder == EDTFolder.INBOX) {%>selected<%}%>>Inbox</option>
			<option value="outbox" <% if (folder == EDTFolder.OUTBOX) {%>selected<%}%>>Outbox</option>
			<option value="sent" <% if (folder == EDTFolder.SENT) {%>selected<%}%>>Sent</option>
			<option value="archive" <% if (folder == EDTFolder.ARCHIVE) {%>selected<%}%>>Archive</option>
		</select> 

		

<table class="table table-striped table-hover">
<thead>
	<tr>
		<% if (folder == EDTFolder.INBOX) {%><th><input type="checkbox" onclick="toggleCheckboxes(this)" title="select all"></th><% } %>
		<th>View File</th>
		<% if (folder.providesAccessToFiles()) {%><th>Download File</th><%}%>
		<th>Date</th>
	</tr>
</thead>

<tbody>
	<%
    if ( folderPath == null || folderPath.equals("") ) {
        Exception e = new Exception("Unable to find the key ONEDT_"+folder.name()+" in the properties file.  Please check the value of this key or add it if it is missing.");
        throw e;
    }
    session.setAttribute("backupfilepath", folderPath);

   // unzip any files indicated by <unzipfile>
   String zname = request.getParameter("unzipfile");
   String unzipMSG = "";
   try {
     if ( zname != null && !zname.equals("") ) {
    	 Boolean unzipDone = zip.unzipXML(folderPath,zname);
         if (!unzipDone) {
             unzipMSG="(Cannot unzip)";
         }
     }
   } catch(Exception e) {
       MiscUtils.getLogger().error("viewMOHFiles: unzip file Unhandled exception:", e);
       unzipMSG="(Cannot unzip)";
   }

    File f = new File(folderPath);
    File[] contents = null;
    if (f.exists()) { contents = f.listFiles(); }
    else { contents = new File[]{}; }

    Arrays.sort(contents,new FileSortByDate());
    if (contents == null) {
        Exception e = new Exception("Unable to find any files in the directory "+folderPath+".  (If this is the incorrect directory, please modify the value of ONEDT_"+folder.name()+" in your properties file to reflect the correct directory).");
        throw e;
    }
    for(int i=0; i<contents.length; i++) {
      bodd = bodd?false:true ;
      if (contents[i].isDirectory() || contents[i].getName().startsWith(".")) continue;
      if (contents[i].getName().endsWith(".sh")) continue;
      String archiveElement = "<td ><input type='checkbox' name='mohFile' value='"+URLEncoder.encode(contents[i].getName())+"' title='select to archive'/></td>";
      if (folder == EDTFolder.INBOX || folder == EDTFolder.ARCHIVE) {
          out.println("<tr>"+(folder == EDTFolder.INBOX ? archiveElement : "")+"<td><a HREF='#' onclick='viewMOHFile(\""+URLEncoder.encode(contents[i].getName())+"\")'>"+contents[i].getName()+unzipMSG+"</a></td>") ;
          out.println("<td><a HREF='../../../servlet/BackupDownload?filename="+URLEncoder.encode(contents[i].getName())+"'>Download</a></td>") ;
      } else {
          out.println("<tr><td>"+contents[i].getName()+"</td>") ;
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date d = new Date(contents[i].lastModified());
      out.println("<td align='right'>"+sdf.format(d)+"</td></tr>"); //+System.getProperty("file.separator")
    }
%>
</tbody>
</table>

<% if (contents.length > 20) { %>

<% if (folder  == EDTFolder.INBOX) {%>

		<input type="submit" value="Archive" class="btn">
<%}%>

		<select name="folder" onchange="location.href='viewMOHFiles.jsp?folder='+this.options[selectedIndex].value">
			<option value="inbox" <% if (folder == EDTFolder.INBOX) {%>selected<%}%>>Inbox</option>
			<option value="outbox" <% if (folder == EDTFolder.OUTBOX) {%>selected<%}%>>Outbox</option>
			<option value="sent" <% if (folder == EDTFolder.SENT) {%>selected<%}%>>Sent</option>
			<option value="archive" <% if (folder == EDTFolder.ARCHIVE) {%>selected<%}%>>Archive</option>
		</select> 



<% } %>
<% if (folder == EDTFolder.INBOX) {%> </form> <% } %>
</div><!--container-->
</body>
</html>