<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 */
-->
<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	RedirectLinkDao redirectLinkDao = (RedirectLinkDao) applicationContext.getBean("redirectLinkDao");
	RedirectLinkTrackingDao redirectLinkTrackingDao = (RedirectLinkTrackingDao) applicationContext.getBean("redirectLinkTrackingDao");
	
	int redirectLinkId=Integer.parseInt(request.getParameter("redirectLinkId"));
	RedirectLink redirectLink=redirectLinkDao.find(redirectLinkId);
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/oscar.css" title="default" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/reporting.css" title="default" />
	<title></title>
</head>
<body>

	Summary for : <%=redirectLink.getUrl()%>
	<br /><br />
	
	<table class="genericTable">
		<tbody>
			<%
				int countTotalRedirects=redirectLinkTrackingDao.countRedirects(redirectLinkId);
				int countProvidersWhoHaveUsedLink=redirectLinkTrackingDao.countProvidersWhoHaveUsedLink(true, redirectLinkId);
				int countProvidersWhoHaveNotUsedLink=redirectLinkTrackingDao.countProvidersWhoHaveUsedLink(false, redirectLinkId);
			%>
			<tr>
				<td class="Header">Total times link was used </td>
				<td class="Section"><%=countTotalRedirects %></td>
			</tr>
			<tr>
				<td class="Header"># of providers who have used link </td>
				<td class="Section"><%=countProvidersWhoHaveUsedLink %></td>
			</tr>
			<tr>
				<td class="Header"># of providers who have never used link </td>
				<td class="Section"><%=countProvidersWhoHaveNotUsedLink %></td>
			</tr>
			<tr>
				<td class="Header">Avg times link was used </td>
				<td class="Section"><%=countTotalRedirects/countProvidersWhoHaveUsedLink%></td>
			</tr>
		</tbody>
	</table>
</body>
</html>
