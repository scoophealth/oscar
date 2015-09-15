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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
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
  if(session.getValue("user") == null)
    response.sendRedirect("../../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
           String docdownload = oscarVariables.getProperty("project_home") ;;
           session.setAttribute("homepath", docdownload);      

%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.admin.btnImportFormData" /></title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/js/jquery_css/smoothness/jquery-ui-1.10.2.custom.min.css"/>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-ui-1.10.2.custom.min.js"></script>
<script>
$(function() {
    $( document ).tooltip();
  });
</script>
</head>

<body>



<div class="well">

<h3><bean:message key="admin.admin.btnImportFormData" /></h3>

<html:form action="/form/xmlUpload.do" method="POST" enctype="multipart/form-data">

	<html:errors />



Select data in zip format:<br /> 

<input type="file" name="file1" value="">
<span title="<bean:message key="global.uploadWarningBody"/>" style="vertical-align:middle;font-family:arial;font-size:20px;font-weight:bold;color:#ABABAB;cursor:pointer"><img border="0" src="../images/icon_alertsml.gif"/></span></span>
        
 <input type="submit" name="Submit" class="btn btn-primary" value="Import">

<p><i class="icon-info-sign"></i> Use this function to import data for a specific form into the OSCAR database</p>
		
</html:form>

</div>
</body>
</html:html>
