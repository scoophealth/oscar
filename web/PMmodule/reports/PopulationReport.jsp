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
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.caisi.util.*"%>
<%
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	int programId=Integer.parseInt(request.getParameter("programId"));
	PopulationReportUIBean populationReportUIBean=new PopulationReportUIBean(applicationContext, programId);
	Program program=populationReportUIBean.getProgram();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

	<h1>Population Report : <%=program.getName() %></h1>
	
	<table class="genericTable">
		<tr class="genericTableRow">
			<td class="genericTableHeader">Role Provider</td>
			<td class="genericTableHeader">Encounter Type</td>
			<%
				for (IssueGroup issueGroup : populationReportUIBean.getIssueGroups())
				{
					%>
						<td class="genericTableHeader"><%=issueGroup.getName()%></td>
					<%
				}
			%>
			<td class="genericTableHeader">Total</td>
		</tr>
		<%
			for (Role role : populationReportUIBean.getRoles())
			{
				boolean hasPrintedRole=false;
				for (EncounterUtil.EncounterType encounterType : EncounterUtil.EncounterType.values())
				{
					%>
						<tr class="genericTableRow">
							<td class="genericTableHeader">
							<%
								String temp="";
								if (!hasPrintedRole)
								{
									temp=role.getName();
									hasPrintedRole=true;
								}
							%>
							<%=temp%>
							</td>
							<td class="genericTableData"><%=encounterType %></td>
							<%
								for (IssueGroup issueGroup : populationReportUIBean.getIssueGroups())
								{
									%>
										<td class="genericTableData">TODO : numeric data</td>
									<%
								}
							%>
							<td class="genericTableData">TODO : numeric data</td>
						</tr>
					<%
				}
			%>
			<tr class="genericTableRow">
				<td class="genericTableHeader"></td>
				<td class="genericTableHeader">Total</td>
				<%
					for (IssueGroup issueGroup : populationReportUIBean.getIssueGroups())
					{
						%>
							<td class="genericTableData">TODO : numeric data</td>
						<%
					}
				%>
				<td class="genericTableData">TODO : numeric data</td>
			</tr>
		<%
			}
		%>
	</table>
	
<%@include file="/layouts/caisi_html_bottom.jspf"%>
