<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.web.*"%>
<%@page import="org.oscarehr.common.model.*"%>
<%@page import="org.oscarehr.util.*"%>
<%@page import="java.text.*"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	String functionalCentreId=request.getParameter("functionalCentreId");
	int programId=Integer.parseInt(request.getParameter("programId"));
	String startDateString=request.getParameter("startDate");
	String endDateString=request.getParameter("endDate");
	SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd");
	Date startDate=null;
	Date endDate=null;
	try
	{
		startDate=dateFormatter.parse(startDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	try
	{
		endDate=dateFormatter.parse(endDateString);
	}
	catch (Exception e)
	{
		// do nothing, bad input
	}
	
	PopulationReportUIBean populationReportUIBean=new PopulationReportUIBean(loggedInInfo,functionalCentreId, programId, startDate, endDate);
	Program program=populationReportUIBean.getProgram();
	FunctionalCentre functionalCentre=populationReportUIBean.getFunctionalCentre();
	PopulationReportDataObjects.RoleDataGrid roleDataGrid=populationReportUIBean.getRoleDataGrid();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h1>Activity Report : <%=functionalCentre==null?"":functionalCentre.getDescription()%> <%=program==null?"":program.getName()%> from <%=startDateString%> to <%=endDateString%></h1>
<div>
The numbers here represent the number of encounters/notes except in the total unique clients column where it represents clients.
The total and subtotal rows don't always add up to all the encounter types as some encounters are left blank for the encounter type.
The total encounters column is a unique encounter count, so if an encounter involved 3 issues, it will only be counted once in the total unique encounters column.
The total unique clients column is a unique count so if a client had 10 encounters for 15 issues, they will only be counted once in the total unique client column
</div>

<input type="button" value="Back" onclick="history.go(-1);" />

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
		<td class="genericTableHeader">Total unique encounters</td>
		<td class="genericTableHeader">Total unique clients</td>
	</tr>
	<%
		for (Map.Entry<SecRole, PopulationReportDataObjects.EncounterTypeDataGrid> roleEntry : roleDataGrid.entrySet())
		{
			boolean hasPrintedRole = false;
			for (Map.Entry<EncounterUtil.EncounterType, PopulationReportDataObjects.EncounterTypeDataRow> encounterEntry : roleEntry.getValue().entrySet())
			{
				String tempRoleName = "";
				if (!hasPrintedRole)
				{
					tempRoleName = roleEntry.getKey().getName();
					hasPrintedRole = true;
				}
				%>
					<tr class="genericTableRow">
					<td class="genericTableHeader"><%=tempRoleName%></td>
					<td class="genericTableData"><%=encounterEntry.getKey().name()%></td>
					<%
						for (Integer issueGroupEntry : encounterEntry.getValue().values())
						{
							%>
								<td class="genericTableData"><%=issueGroupEntry%></td>
							<%
						}
					%>
	
					<td class="genericTableData"><%=encounterEntry.getValue().rowTotalUniqueEncounters%></td>
					<td class="genericTableData"><%=encounterEntry.getValue().rowTotalUniqueClients%></td>
					</tr>
				<%
			}
		%>
		<tr class="genericTableRow">
			<td class="genericTableHeader"></td>
			<td class="genericTableHeader">Sub Total</td>
			<%
				PopulationReportDataObjects.EncounterTypeDataRow encounterTypeDataRow = roleEntry.getValue().subTotal;
				for (Integer issueGroupEntry : encounterTypeDataRow.values())
				{
					%>
					<td class="genericTableData"><%=issueGroupEntry%></td>
					<%
				}
			%>
			<td class="genericTableData"><%=encounterTypeDataRow.rowTotalUniqueEncounters%></td>
			<td class="genericTableData"><%=encounterTypeDataRow.rowTotalUniqueClients%></td>
		</tr>
		<%
	}
%>
	<tr class="genericTableRow">
		<td class="genericTableHeader">Total</td>
		<td class="genericTableData"></td>
		<%
			PopulationReportDataObjects.EncounterTypeDataRow encounterTypeDataRow = roleDataGrid.total;
			for (Integer issueGroupEntry : encounterTypeDataRow.values())
			{
		%>
		<td class="genericTableData"><%=issueGroupEntry%></td>
		<%
		}
		%>
		<td class="genericTableData"><%=encounterTypeDataRow.rowTotalUniqueEncounters%></td>
		<td class="genericTableData"><%=encounterTypeDataRow.rowTotalUniqueClients%></td>
	</tr>
</table>

<input type="button" value="Back" onclick="document.location='<%=request.getContextPath()%>/PMmodule/ProviderInfo.do'" />


<%@include file="/layouts/caisi_html_bottom.jspf"%>
