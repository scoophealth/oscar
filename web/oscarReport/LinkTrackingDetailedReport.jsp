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
<%@page import="org.apache.commons.lang.time.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	RedirectLinkDao redirectLinkDao = (RedirectLinkDao) applicationContext.getBean("redirectLinkDao");
	RedirectLinkTrackingDao redirectLinkTrackingDao = (RedirectLinkTrackingDao) applicationContext.getBean("redirectLinkTrackingDao");
	ProviderDao providerDao = (ProviderDao) applicationContext.getBean("providerDao");
	
	int redirectLinkId=Integer.parseInt(request.getParameter("redirectLinkId"));
	RedirectLink redirectLink=redirectLinkDao.find(redirectLinkId);
	
	List<RedirectLinkTracking> trackingResults=redirectLinkTrackingDao.findByRedirectLinkId(redirectLinkId);
%>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/oscar.css" title="default" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/share/css/reporting.css" title="default" />
	<title></title>
</head>
<body>

	Details for : <%=redirectLink.getUrl()%>
	<br /><br />
	
	<table class="genericTable">
		<thead>
			<tr>
				<td class="Header">Date</td>
				<td class="Header">Provider</td>
			</tr>
		</thead>
		<tbody>
		<%
			for (RedirectLinkTracking redirectLinkTracking : trackingResults)
			{
				%>
					<tr>
						<td class="Section"><%=DateFormatUtils.ISO_DATETIME_FORMAT.format(redirectLinkTracking.date)%></td>
						<%
							Provider provider=providerDao.getProvider(String.valueOf(redirectLinkTracking.providerNo));
						%>
						<td class="Section"><%=provider.getFormattedName()%></td>
					</tr>
				<%
			}
		%>
		</tbody>
	</table>
</body>
</html>
