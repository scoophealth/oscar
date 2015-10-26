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
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="org.oscarehr.common.dao.FunctionalCentreDao"%>
<%@page import="org.oscarehr.common.model.FunctionalCentre"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>


<%
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	SecRoleDao secRoleDao = (SecRoleDao) SpringUtils.getBean("secRoleDao");
	
	List<Program> allPrograms=programDao.getAllActivePrograms();
	List<SecRole> allRoles=secRoleDao.findAll();
	
	FunctionalCentreDao functionalCentreDao = (FunctionalCentreDao) SpringUtils.getBean("functionalCentreDao");
    ProviderManager2 providerManager = (ProviderManager2) SpringUtils.getBean("providerManager2");
    ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	List<FunctionalCentre> functionalCentres=functionalCentreDao.findInUseByFacility(loggedInInfo.getCurrentFacility().getId());
%>

<%@include file="/layouts/caisi_html_top.jspf"%>

<h1>Activity Report Form</h1>

<form method="post" action="activity_report.jsp">
<table>

	<tr style="vertical-align:top">
		<td>Functional Centre :
		<select name="functionalCentreId">
			<option value=""></option>
					<%
						for (FunctionalCentre functionalCentre : functionalCentres)
						{
							%>
								<option value="<%=functionalCentre.getAccountId()%>"><%=functionalCentre.getAccountId()+", "+StringEscapeUtils.escapeHtml(functionalCentre.getDescription())%></option>
							<%
						}
					%>
				
		</select></td>
	</tr>
	<tr><td>Or </td></tr>
	<tr>
		<td>Program :
		<select name="programId">
			<option value="0"></option>
			<%
				for (Program program : allPrograms)
				{
					if (program.isBed() || program.isService())
					{
						%>
							<option value="<%=program.getId() %>"><%=program.getName()%></option>
						<%
					}
				}
			%>
		</select></td>
	</tr>
	<tr>
		<td>Start Date (YYYY-MM): <input type="text" name="startDate" /><br /></td>
	</tr>
	<tr>
		<td>End Date (YYYY-MM):<input type="text" name="endDate" /><br /></td>	
	</tr>	

	<tr>		
		<td><input type="submit" /></td>
	</tr>
</table>
</form>

<hr />

<h2>Export to csv, report on all programs and all providers</h2>
(This will export all bed/service programs to a csv broken down by
month.)

<form method="post" action="activity_report_export.jsp">
<table>
	<tr>
		<td>Start Date</td>
		<td>EndDate (inclusive)</td>
	</tr>
	<tr style="vertical-align:top">
		<td><input type="text" name="startDate" /><br />(YYYY-MM)</td>

		<td><input type="text" name="endDate" /><br />(YYYY-MM)</td>
	</tr>

	<tr>
		<td></td>
		<td><input type="submit" value="export"  onclick="window.open('<%=request.getContextPath()%>/common/progress_dialog.jsp', '', 'height=300,width=500,location=no,scrollbars=no,menubars=no,toolbars=no,resizable=yes,top=200,left=400')" /></td>
	</tr>
</table>
</form>

<hr />

<h2>Export to csv, report by role and program</h2>
(This will export to csv based on the selected bed/service program for the specified providers, broken down by
month.)

<form method="post" action="activity_report_export_program_role.jsp">
<table>
	<tr>
		<td colspan=4>Functional Centre :
		</td>
	</tr>
	<tr>
		<td colspan=4>
		<select name="functionalCentreIds" multiple="multiple" style="width:40em;height:6em">
			<option value=""></option>
					<%
						for (FunctionalCentre functionalCentre : functionalCentres)
						{
							%>
								<option value="<%=functionalCentre.getAccountId()%>"><%=functionalCentre.getAccountId()+", "+functionalCentre.getDescription()%></option>
							<%
						}
					%>
				
		</select></td>
		
	</tr>
	<tr>
		<td>Programs</td>
		<td>Roles</td>
		<td>Start Date</td>
		<td>EndDate (inclusive)</td>
	</tr>
	<tr style="vertical-align:top">
		<td>
			<select name="programIds" multiple="multiple" style="width:16em;height:6em">
				<%
					for (Program program : allPrograms)
					{
						if (program.isBed() || program.isService())
						{
							%>
								<option value="<%=program.getId() %>" title="<%=program.getName()%>"><%=program.getName()%></option>
							<%
						}
					}
				%>
			</select>
		</td>
		<td>
			<select name="secRoleId">
				<%
					for (SecRole secRole : allRoles)
					{
						%>
							<option value="<%=secRole.getId() %>"><%=secRole.getName()%></option>
						<%
					}
				%>
			</select>
		</td>

		<td><input type="text" name="startDate" /><br />(YYYY-MM)</td>

		<td>
			<input type="text" name="endDate" />
			<br />
			(YYYY-MM)
			<br />
			<br />
			<input type="submit" value="export" onclick="alert('This report can take a while, please be patient.')" />
		</td>
	</tr>
</table>
</form>

<%@include file="/layouts/caisi_html_bottom.jspf"%>
