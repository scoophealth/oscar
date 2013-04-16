<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.oscarehr.hospitalReportManager.model.HRMCategory"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.hospitalReportManager.dao.HRMCategoryDao"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%
	HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");

	String action=request.getParameter("action");

	if ("add".equals(action))
	{
		String categoryName=request.getParameter("categoryName");
		String subClassNameMnemonic=request.getParameter("subClassNameMnemonic");
		
		HRMCategory category=new HRMCategory();
		category.setCategoryName(categoryName);
		category.setSubClassNameMnemonic(subClassNameMnemonic);
		
		hrmCategoryDao.persist(category);
	}
	else if ("delete".equals(action))
	{
		Integer id=new Integer(request.getParameter("id"));
		hrmCategoryDao.remove(id);
	}
	else
	{
		MiscUtils.getLogger().error("Missed case, action="+action);
	}

	response.sendRedirect("hrmCategories.jsp");
%>
