<!DOCTYPE html>
<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.backup" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.backup");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>
<%
  boolean bodd = false;
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.*,oscar.*,java.io.*,java.net.*,oscar.util.*,org.apache.commons.io.FileUtils"
	errorPage="errorpage.jsp"%>
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>

<html>
<head>

<title>ADMIN PAGE</title>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
</head>
<body>

<h3><bean:message key="admin.admin.btnAdminBackupDownload" /></h3>

<%
String backuppath = oscarVariables.getProperty("backup_path"); 

File dir = new File(backuppath);
boolean exists = dir.exists();

if(exists){
%>
<div class="well">
<table class="table table-striped  table-condensed">
<thead>
	<tr>
		<th>File Name</th>
		<th>Size</th>
	</tr>
</thead>

<tbody>
	<%

    if ( backuppath == null || backuppath.equals("") ) {
        Exception e = new Exception("Unable to find the key backup_path in the properties file.  Please check the value of this key or add it if it is missing.");
        throw e;
    }
    session.setAttribute("backupfilepath", backuppath);

    File f = new File(backuppath);
    File[] contents = f.listFiles(); 
    
    Arrays.sort(contents,new FileSortByDate());
    if (contents == null) {
        Exception e = new Exception("Unable to find any files in the directory "+backuppath+".  (If this is the incorrect directory, please modify the value of backup_path in your properties file to reflect the correct directory).");
        throw e;
    }
    for(int i=0; i<contents.length; i++) {
      bodd = bodd?false:true ;
      if(contents[i].isDirectory() || contents[i].getName().equals("BackupClient.class")  || contents[i].getName().startsWith(".")) continue;
      out.println("<tr><td><a HREF='../servlet/BackupDownload?filename="+URLEncoder.encode(contents[i].getName())+"'>"+contents[i].getName()+"</a></td>") ;
      long bytes = contents[i].length( );
      String display = FileUtils.byteCountToDisplaySize( bytes );

      out.println("<td align='right' title=\""+bytes+" by\">"+display+"</td></tr>"); //+System.getProperty("file.separator")
    }
%>
</tbody>
</table>
</div>
<%}else{%>

    <div class="alert alert-error">
    <strong>Warning!</strong> It appears that your backup directory does not exist or there is a problem with the path. Please check <i>backup_path</i> in your properties file.      
    </div>

<%}%>
</body>
</html>
