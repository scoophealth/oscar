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

<%@page import="java.util.*, java.io.*, org.oscarehr.util.MiscUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.measurements" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin.measurements");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<html:html locale="true">

<head>
<title><bean:message key="admin.renal.managePatientLetter"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<%
	if(request.getParameter("action") != null && request.getParameter("action").equals("save")) {
		String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
		try {
			File f = new File(documentDir,"orn_patient_letter.txt");
			PrintWriter pw = new PrintWriter(new FileWriter(f),true);
			pw.println(request.getParameter("letter"));
			pw.close();
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}

	}
%>

<%
	String currentLetter = "";

	String documentDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR","");
	File f = new File(documentDir,"orn_patient_letter.txt");
	if(f.exists()) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line =null;
			while((line=in.readLine())!=null) {
				currentLetter += line + "\n";
			}
			in.close();
		}catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}
	}

%>

<style>
input, textarea {
    width: 800px;
}
</style>
</head>

<body>
<h3><bean:message key="admin.renal.managePatientLetter"/></h3>

<div class="container-fluid well">
	Use this section to customize the patient letter generated from the screening report.
	<br/>
	<form action="patientLetterManager.jsp?action=save">
		<input type="hidden" name="action" value="save"/>
		<textarea name="letter" rows="30" cols="80"><%=currentLetter %></textarea>
		<br/>
		<input class="btn btn-primary" type="submit" value="Save"/>
	</form>
</div>
</body>
</html:html>

