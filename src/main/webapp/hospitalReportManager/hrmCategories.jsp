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
<%@include file="/layouts/html_top.jspf"%>


<h2 class="oscarBlueHeader">
	HRM Categories
</h2>

<form method="post" action="hrm_categories_action.jsp">
	<input type="hidden" name="action" value="add" />
	
	Category Name : <input type="text" name="categoryName" />
	<br />
	SubClass Name Mnemonic : <input type="text" name="subClassNameMnemonic" /> (should be of the format &lt;subclass_name&gt;:&lt;subclass_mnemonic&gt;)
	<br />
	Sending Facility ID : <input type="text" name="sendingFacilityID" />
	<br />
	
	<input type="submit" value="Add" />
</form>

<hr />

<table style="border-collapse:collapse">
	<tr style="background-color:#8888ff">
		<td style="border:solid black 1px">ID</td>
		<td style="border:solid black 1px">CategoryName</td>
		<td style="border:solid black 1px">SubClass Name Mnemonic</td>
		<td style="border:solid black 1px">Sending Facility ID</td>
        <td style="border:solid black 1px">Action</td>
	</tr>
	<%
		HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
		
		for (HRMCategory category:  hrmCategoryDao.findAll())
		{
			%>
				<tr>
					<td style="border:solid black 1px">
						<%=category.getId()%>
					</td>
					<td style="border:solid black 1px">
						<%=StringEscapeUtils.escapeHtml(category.getCategoryName())%>
					</td>
					<td style="border:solid black 1px">
						<%=StringEscapeUtils.escapeHtml(category.getSubClassNameMnemonic())%>
					</td>
					<td style="border:solid black 1px">
						<%=StringEscapeUtils.escapeHtml(category.getSendingFacilityId())%>
					</td>
					<td style="border:solid black 1px">
					  <b><a href="hrm_categories_action.jsp?id=<%=category.getId()%>&action=delete">Delete</a></b>-	
					  <b><a href="hrmCategoriesUpdate.jsp?id=<%=category.getId()%>">Update</a></b>				
					</td>
				</tr>
			<%
		}
	%>
</table>

<%@include file="/layouts/html_bottom.jspf"%>
