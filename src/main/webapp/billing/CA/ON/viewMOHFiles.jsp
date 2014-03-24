<%--

    Copyright (c) 2008-2012 Indivica Inc.
    
    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".
    
--%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean bodd = false;
    String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
	EDTFolder folder = EDTFolder.getFolder(request.getParameter("folder"));
	String folderPath = folder.getPath();
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.backup,_admin.billing" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("/oscar/logout.jsp");%>
</security:oscarSec>
<%@ page import="java.util.*,oscar.*,java.io.*,java.net.*,oscar.util.*,org.apache.commons.io.FileUtils,java.text.SimpleDateFormat,org.oscarehr.billing.CA.ON.util.EDTFolder,org.oscarehr.util.MiscUtils" errorPage="errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
<title>View MOH Files</title>
<link rel="stylesheet" href="/oscar/web.css">
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

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form id="form" method="POST">
	<input type="hidden" id="filename" name="filename" value="" >
</form>
<center>
<table cellspacing="0" cellpadding="2" width="100%" border="0">
	<tr>
		<th align="CENTER" bgcolor="<%=deepcolor%>">View MOH Files</th>
	</tr>
</table>

	<%= WebUtils.popMessages(request.getSession(), "messages") %>

		<%
		    if (folder == EDTFolder.INBOX) {
		%> <form method="POST" action="<%=request.getContextPath()%>/billing/CA/ON/moveMOHFiles.do" onsubmit="return checkForm();"><% } %>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr>
		<td>
		<select name="folder" onchange="location.href='viewMOHFiles.jsp?folder='+this.options[selectedIndex].value">
			<option value="inbox" <% if (folder == EDTFolder.INBOX) {%>selected<%}%>>Inbox</option>
			<option value="outbox" <% if (folder == EDTFolder.OUTBOX) {%>selected<%}%>>Outbox</option>
			<option value="sent" <% if (folder == EDTFolder.SENT) {%>selected<%}%>>Sent</option>
			<option value="archive" <% if (folder == EDTFolder.ARCHIVE) {%>selected<%}%>>Archive</option>
		</select> <input type="submit" value="Archive">
		</td>
		<td align="right"><a href="#" onClick='window.close()'> Close
		</a></td>
	</tr>
</table>

<table cellspacing="1" cellpadding="2" width="90%" border="0">
	<tr bgcolor='<%=deepcolor%>'>
		<% if (folder == EDTFolder.INBOX) {%><th><input type="checkbox" onclick="toggleCheckboxes(this)"><% } %>
		<th>View File</th>
		<% if (folder.providesAccessToFiles()) {%><th>Download File</th><%}%>
		<th>Date</th>
	</tr>
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
      String archiveElement = "<td style='text-align: center; vertical-align: middle;'><input type='checkbox' name='mohFile' value='"+URLEncoder.encode(contents[i].getName())+"' /></td>";
      if (folder == EDTFolder.INBOX || folder == EDTFolder.ARCHIVE) {
          out.println("<tr bgcolor='"+ (bodd?weakcolor:"white") +"'>"+(folder == EDTFolder.INBOX ? archiveElement : "")+"<td><a HREF='#' onclick='viewMOHFile(\""+URLEncoder.encode(contents[i].getName())+"\")'>"+contents[i].getName()+unzipMSG+"</a></td>") ;
          out.println("<td><a HREF='../../../servlet/BackupDownload?filename="+URLEncoder.encode(contents[i].getName())+"'>Download</a></td>") ;
      } else {
          out.println("<tr bgcolor='"+ (bodd?weakcolor:"white") +"'><td>"+contents[i].getName()+"</td>") ;
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date d = new Date(contents[i].lastModified());
      out.println("<td align='right'>"+sdf.format(d)+"</td></tr>"); //+System.getProperty("file.separator")
    }
%>
</table>
<% if (contents.length > 20) { %>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr>
		<td>
		<select name="folder" onchange="location.href='viewMOHFiles.jsp?folder='+this.options[selectedIndex].value">
			<option value="inbox" <% if (folder == EDTFolder.INBOX) {%>selected<%}%>>Inbox</option>
			<option value="outbox" <% if (folder == EDTFolder.OUTBOX) {%>selected<%}%>>Outbox</option>
			<option value="sent" <% if (folder == EDTFolder.SENT) {%>selected<%}%>>Sent</option>
			<option value="archive" <% if (folder == EDTFolder.ARCHIVE) {%>selected<%}%>>Archive</option>
		</select> <input type="submit" value="Archive">
		</td>
		<td align="right"><a href="#" onClick='window.close()'> Close
		</a></td>
	</tr>
</table>
</center>
<% } %>
<% if (folder == EDTFolder.INBOX) {%> </form> <% } %>
</body>
</html>
