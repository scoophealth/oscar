<!-- 
/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
 -->
<%@page import="java.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	IssueGroupDao issueGroupDao = (IssueGroupDao) applicationContext.getBean("issueGroupDao");
	
	List<IssueGroup> issueGroups=issueGroupDao.findAll();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

	<h1>Population Report</h1>
	
	<table class="genericTable">
		<tr class="genericTableRow">
			<%
				for (IssueGroup issueGroup : issueGroups)
				{
					%>
						<td class="genericTableData"><%=issueGroup.getName()%></td>
					<%
				}
			%>
		</tr>
	</table>
	
<%@include file="/layouts/caisi_html_bottom.jspf"%>
