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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.HRMCategory"%>
<%
	String deepColor = "#CCCCFF", weakColor = "#EEEEFF";
	String country = request.getLocale().getCountry();
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
	<title>Show Mappings</title>
	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/DT_bootstrap.js"></script>
</head>

<body onunload="updateAjax()" class="BodyStyle" vlink="#0000FF">
<h4>Show Mappings</h4>
<p class="pull-right">
	<a href="javascript:popupStart(300,400,'Help.jsp')"><bean:message key="global.help" /></a> | 
	<a href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about" /></a> |
	<a href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license" /></a>
</p>
<div>
	<% if (request.getAttribute("success") != null ) {
		if ((Boolean) request.getAttribute("success")) {
	%>
		Successfully added the mapping
	<% 	} else { %>
		Error encountered while adding the mapping<br />
	<% 
		}
	}
	%>
</div>
<div>
	<a href="<%=request.getContextPath() %>/hospitalReportManager/hrmAddClassMapping.jsp">+ Add a class mapping</a>
</div>
<hr/>
<table class="table table-bordered table-striped table-hover table-condensed">
	<tbody>
		<tr>
			<th>Sending Facility Id</th>
			<th>Class Name</th>
			<th>SubClass Name Mnemonic</th>
			<th>Mnemonic</th>
			<th>Description</th>
			<th>Category</th>
			<th></th>
		</tr>
		<%
		ArrayList<HashMap<String,? extends Object>> hrmmappings = HRMUtil.listMappings();
		for (int i = 0; i < hrmmappings.size(); i++) {
			HashMap<String,? extends Object> curmapping = hrmmappings.get(i);
		%>
		<tr>
			<td><%=curmapping.get("id")%>&nbsp;</td>
			<td><%=curmapping.get("class")%>&nbsp;</td>
			<td align='center'><%=curmapping.get("sub_class")%>&nbsp;</td>
			<td><%=curmapping.get("mnemonic") %>&nbsp;</td>
			<td><%=curmapping.get("description") %>&nbsp;</td>
			<td><%=((HRMCategory) curmapping.get("category")).getCategoryName() %>&nbsp;</td>
			<td><a href="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do?deleteMappingId=<%=curmapping.get("mappingId") %>">Delete</a></td>
		</tr>
		<%
		} 
		%>
	</tbody>
</table>
</body>
</html:html>
