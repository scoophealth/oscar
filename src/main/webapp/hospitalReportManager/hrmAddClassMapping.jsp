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
<%@page import="java.util.*, org.oscarehr.hospitalReportManager.*, org.oscarehr.hospitalReportManager.model.HRMCategory, org.oscarehr.hospitalReportManager.dao.HRMCategoryDao, org.oscarehr.util.SpringUtils"%>
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

<body onunload="updateAjax()">
<h4>Add Mapping</h4>
<p class="pull-right">
	<a href="javascript:popupStart(300,400,'Help.jsp')"><bean:message key="global.help" /></a> | 
	<a href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about" /></a> | 
	<a href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license" /></a>
</p>
<form method="post" action="<%=request.getContextPath() %>/hospitalReportManager/Mapping.do">
	<fieldset>
		<div class="control-group">
			<label class="control-label">Report class:</label>
			<div class="controls">
				<select name="class"><option value="Medical Records Report">Medical Records Report</option><option value="Diagnostic Imaging Report">Diagnostic Imaging Report</option><option value="Cardio Respiratory Report">Cardio Respiratory Report</option></select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Sub-class:</label>
			<div class="controls">
				<input type="text" name="subclass" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Sub-class mmenoic:</label>
			<div class="controls">
				<input type="text" name="mnemonic" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Sub-class description:</label>
			<div class="controls">
				<input type="text" name="description" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Sending Facility ID (* for all):</label>
			<div class="controls">
				<select name="category">
				<%
					HRMCategoryDao categoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
					List<HRMCategory> categoryList = categoryDao.findAll();
					for (HRMCategory category : categoryList) {
				%>
					<option value="<%=category.getId() %>"><%=category.getCategoryName() %></option>
				<% 
					}
				%>
				</select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Category:</label>
			<div class="controls">
				<input type="text" name="sendingFacilityId" value="*" />
			</div>
		</div>
		<div class="control-group">
			<input type="submit" class="btn btn-primary" name="submit" value="Save" />
		</div>
	
	</fieldset>
</form>
</body>
</html:html>
