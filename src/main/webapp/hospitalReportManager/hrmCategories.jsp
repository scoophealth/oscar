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

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMCategory"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.hospitalReportManager.dao.HRMCategoryDao"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html:html locale="true">
<head>
	<title>HRM Categories</title>
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
<h4>HRM Categories</h4>

<form method="post" action="hrm_categories_action.jsp">
	<input type="hidden" name="action" value="add" />
	<fieldset>
		<div class="control-group">
			<label class="control-label">Category Name:</label>
			<div class="controls">
				<input type="text" name="categoryName" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">SubClass Name Mnemonic:</label>
			<div class="controls">
				<input type="text" name="subClassNameMnemonic" /> (should be of the format &lt;subclass_name&gt;:&lt;subclass_mnemonic&gt;)
			</div>
		</div>
		<div class="control-group">
			<input type="submit" class="btn btn-primary" value="Add" />
		</div>
	</fieldset>	
</form>
<hr />
<table class="table table-bordered table-striped table-hover table-condensed">
	<thead>
		<tr>
			<th>ID</th>
			<th>Category Name</th>
			<th>SubClass Name Mnemonic</th>
		</tr>
	</thead>
	<tbody>
	<%
		HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
		for (HRMCategory category:  hrmCategoryDao.findAll()) {
	%>
		<tr>
			<td><%=category.getId()%>&nbsp;</td>
			<td><%=StringEscapeUtils.escapeHtml(category.getCategoryName())%>&nbsp;</td>
			<td><%=StringEscapeUtils.escapeHtml(category.getSubClassNameMnemonic())%>&nbsp;</td>
		</tr>
	<%
		}
	%>
	</tbody>
</table>
</body>
</html>
