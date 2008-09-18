<%--  
/*
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
 */
--%>
<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	RedirectLinkDao redirectLinkDao = (RedirectLinkDao) applicationContext.getBean("redirectLinkDao");
	List<RedirectLink> links=redirectLinkDao.findAll();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

	<table class="genericTable">
		<thead>
			<tr>
				<td class="genericTableHeader">Tracked Url</td>
				<td class="genericTableHeader">&nbsp;</td>
				<td class="genericTableHeader">&nbsp;</td>
			</tr>
		</thead>
		<tbody>
		<%
			for (RedirectLink redirectLink : links)
			{
				%>
					<tr class="genericTableRow">
						<td class="genericTableData"><a href="<%=redirectLink.getUrl()%>"><%=redirectLink.getUrl()%></a></td>
						<td class="genericTableData"><a href="LinkTrackingDetailedReport.jsp?redirectLinkId=<%=redirectLink.getId()%>">Detailed Report</a></td>
						<td class="genericTableData"><a href="LinkTrackingSummaryReport.jsp?redirectLinkId=<%=redirectLink.getId()%>">Summary Report</a></td>
					</tr>
				<%
			}
		%>
		</tbody>
	</table>
	
<%@include file="/layouts/caisi_html_bottom.jspf"%>
