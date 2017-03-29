<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>
<%    
  String user_no;
  user_no = (String) session.getAttribute("user");
  String docdownload = oscarVariables.getProperty("project_home") ;;
  session.setAttribute("homepath", docdownload);      

%>
<!DOCTYPE html>
<html:html>
<head>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<title>EDT OBEC Response Report Generator</title>
</head>

<body>

<p>EDT OBEC Response Report Generator</p>

<html:form action="/oscarBilling/DocumentErrorReportUpload.do"	method="POST" enctype="multipart/form-data">
	


    <div class="alert alert-error">
  
    <html:errors />
    </div>

<div class="well">
Select diskette <input type="file" name="file1" value="" required>

<input type="submit" name="Submit" class="btn btn-primary" value="Create Report">
</div>


</html:form>
</body>
</html:html>
