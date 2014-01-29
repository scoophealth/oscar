<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMCategory"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.hospitalReportManager.dao.HRMCategoryDao"%>
<%@page import="java.util.List"%>
<%@include file="/layouts/html_top.jspf"%>

<%
	HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");

	Integer id = new Integer(request.getParameter("id"));
	List<HRMCategory>  HRMCategoryList = hrmCategoryDao.findById(id);
	
	HRMCategory hrmCategory = null;
	if(!HRMCategoryList.isEmpty()) {

		hrmCategory = HRMCategoryList.get(0);
    } else {
    	response.sendRedirect("hrmCategories.jsp");
    }
	
%>



<h2 class="oscarBlueHeader">
	HRM Categories Update
</h2>

<form method="post" action="hrm_categories_action.jsp">
	<input type="hidden" name="action" value="update" />
	<input type="hidden" name="id" value="<%= hrmCategory.getId() %>" />
	
	Category Name : <input type="text" name="categoryName"  value="<%= hrmCategory.getCategoryName() %>" />
	<br />
	SubClass Name Mnemonic : <input type="text" name="subClassNameMnemonic" value="<%= hrmCategory.getSubClassNameMnemonic() %>"  /> (should be of the format &lt;subclass_name&gt;:&lt;subclass_mnemonic&gt;)
	<br />
	Sending Facility ID : <input type="text" name="sendingFacilityID" value="<%= hrmCategory.getSendingFacilityId() %>" />
	<br />
	
	<input type="submit" value="Update" />
</form>

<hr />

<%@include file="/layouts/html_bottom.jspf"%>
