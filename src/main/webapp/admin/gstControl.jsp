<!DOCTYPE html>
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
<%@ page import="java.util.*,oscar.oscarReport.data.*, java.util.Properties, oscar.oscarBilling.ca.on.administration.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">

<%

Properties props = new Properties();
GstControlAction db = new GstControlAction();
props = db.readDatabase();
String percent = props.getProperty("gstPercent");

%>

<script type="text/javascript">
    function submitcheck(){
            document.getElementById("gstPercent").value = extractNums(document.getElementById("gstPercent").value);
    }
    function extractNums(str){
        return str.replace(/\D/g, "");
    }
</script>
<head>
<title><bean:message key="admin.admin.manageGSTControl"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>
<body onload="loadData()">

<h3><bean:message key="admin.admin.manageGSTControl"/></h3>

<html:form action="/admin/GstControl">
GST:<br>
<div class="input-append">
	<input type="text" class="span2" maxlength="3" id="gstPercent" name="gstPercent" value="<%=percent%>" />
	<span class="add-on">%</span>
</div>
<br>
<input class="btn btn-primary" type="submit" value="save" onclick="submitcheck()" />
</html:form>
</body>
</html:html>
