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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="java.util.*,org.oscarehr.learning.web.CourseManagerAction,org.oscarehr.common.model.SecRole"%>



<html:html locale="true">

<head>
<title><bean:message key="oscarLearning.studentImport.title" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>


<div class="container-fluid">

<h3><bean:message key="admin.admin.learning.importStudent"/></h3>
<div class="well">

	
	<%
		String totalImported = request.getParameter("r");
		if(totalImported != null && totalImported.length()>0) {
	%>				
	
		<h4><%=totalImported%> Records Successfully Imported.</h4>
	<%
		}
	%>



	<div id="upload_form">
		
		<p>Batch upload student data into oscarLearning system. Format should be as follows (per line)</p>
		<p>lastname,firstname,username,password,pin,student#</p>
		<h4>Upload CSV file:</h4>
		
		<html:form action="/oscarLearning/StudentImport.do?method=uploadFile" method="post" enctype="multipart/form-data">
			<html:file property="file"></html:file>						
			<br/>
			<html:submit/>
		</html:form>
	</div>
</div>
</div>
</body>
</html:html>
